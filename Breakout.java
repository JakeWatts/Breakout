import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.ActionListener;

public class Breakout extends Canvas implements KeyListener, Runnable
{
	private Ball ball;
	private Paddle paddle;
	private boolean[] keys;
	private BufferedImage back;
	private boolean b=true;
	public ArrayList<Block> list=new ArrayList<Block>();
	private boolean flag=false;


	public Breakout()
	{
		//set up all variables related to the game
		ball=new Ball();
		paddle=new Paddle(100,520,100,10,Color.BLUE,4);
		for(int i=0;i<16;i++){
			for(int j=0;j<4;j++){
				list.add(new Block(41*i+20,21*j+20,40,20));
			}
		}
		keys = new boolean[2];

    
    	setBackground(Color.WHITE);
		setVisible(true);
		
		new Thread(this).start();
		addKeyListener(this);		//starts the key thread to log key strokes
	}
	
   public void update(Graphics window){
	   paint(window);
   }

   public void paint(Graphics window)
   {
	   
	   if(list.size()==0){
		   System.out.print("YOU WIN!");
		   System.exit(0);
	   }
		//set up the double buffering to make the game animation nice and smooth
		Graphics2D twoDGraph = (Graphics2D)window;

		//take a snap shop of the current screen and same it as an image
		//that is the exact same width and height as the current screen
		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));

		//create a graphics reference to the back ground image
		//we will draw all changes on the background image
		Graphics graphToBack = back.createGraphics();

		for(int i=0;i<list.size();i++){
			if(ball.didCollideBottom(list.get(i))){
				ball.setYSpeed(-ball.getYSpeed());
				flag=true;
			}
			if(ball.didCollideRight(list.get(i))){
				ball.setXSpeed(-ball.getXSpeed());
				flag=true;
			}
			if(flag){
				list.get(i).draw(graphToBack,Color.WHITE);
				list.remove(i);
				i--;
				flag=false;
			}
		}
		ball.moveAndDraw(graphToBack);
		paddle.draw(graphToBack);
		for(Block i:list){
			i.draw(graphToBack);
		}



		//see if ball hits left wall or right wall
		if(!(ball.getX()>=10 && ball.getX()<=780))
		{
			ball.setXSpeed(-ball.getXSpeed());
		}

		
		//see if the ball hits the top or bottom wall 
		if(!(ball.getY()>5))
		{
			ball.setYSpeed(-ball.getYSpeed());
		}
		if(ball.getY()>540){
			System.out.print("YOU LOSE!");
			System.exit(0);
		}



		//see if the ball hits the left paddle
		if(ball.didCollideBottom(paddle))
			ball.setYSpeed(-ball.getYSpeed());
		
		
		//see if the ball hits the right paddle
		
		//see if the paddles need to be moved

		if(keys[0] == true)
		{
			//move left paddle up and draw it on the window
			paddle.moveRightAndDraw(graphToBack);
		}
		if(keys[1] == true)
		{
			//move left paddle down and draw it on the window
			paddle.moveLeftAndDraw(graphToBack);

		}
		












		
		twoDGraph.drawImage(back, null, 0, 0);
	}

	public void keyPressed(KeyEvent e)
	{
		switch(toUpperCase(e.getKeyChar()))
		{
			case 'D' : keys[0]=true; break;
			case 'A' : keys[1]=true; break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(toUpperCase(e.getKeyChar()))
		{
			case 'D' : keys[0]=false; break;
			case 'A' : keys[1]=false; break;
		}
	}

	public void keyTyped(KeyEvent e){}
	
   public void run()
   {
   	try
   	{
   		while(true)
   		{
   		   Thread.currentThread().sleep(8);
            repaint();
         }
      }catch(Exception e)
      {
      }
  	}	
}