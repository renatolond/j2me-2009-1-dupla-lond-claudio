import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class FormulaCanvas extends GameCanvas implements Runnable
{

	private int inputDelay;
	private int frameDelay;
	private int celWidth;
	private int celHeight;
	private Display display;
	private boolean sleeping;
	private boolean gameOver;
	private Random rand;
	// private Sprite cars;
	private Sprite redCar, blueCar, greenCar;
	int redCarAng, blueCarAng, greenCarAng;

	protected FormulaCanvas(Display d)
	{
		super(true);

		display = d;

		celHeight = getHeight();
		celWidth = getWidth();

		// Set the frame rate (30 fps)
		frameDelay = 33;

		// Clear the input delay
		inputDelay = 0;
	}

	public void start()
	{
		// Set the canvas as the current screen
		display.setCurrent(this);

		// Initialize the random number generator
		rand = new Random();

		// Initialize the game variables
		gameOver = false;
		try
		{

			// cars = new Sprite(
			// Image.createImage("/carros.png"), 192/3, 256/4);
			Image cars;
			cars = Image.createImage("/carros.png");
			redCar = new Sprite(Image.createImage(cars, 0, 0, cars.getWidth(),
					cars.getHeight() / 3, 0), 192 / 3, 256 / 4);
			blueCar = new Sprite(Image.createImage(cars, 0, 192 / 3, cars
					.getWidth(), cars.getHeight() / 3, 0), 192 / 3, 256 / 4);
			greenCar = new Sprite(Image.createImage(cars, 0, 2 * 192 / 3, cars
					.getWidth(), cars.getHeight() / 3, 0), 192 / 3, 256 / 4);
			;
			// greenCar;
			// return;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		redCar.setPosition(celWidth / 2, celHeight / 2);
		redCarAng = 0;

		sleeping = false;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop()
	{
		// Stop the animation
		sleeping = true;
	}

	public void run()
	{
		Graphics g = getGraphics();

		// The main game loop
		while (!sleeping)
		{
			update();
			draw(g);
			try
			{
				Thread.sleep(frameDelay);
			} catch (InterruptedException ie)
			{}
		}
	}

	private void draw(Graphics g)
	{
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, celHeight, celWidth);
		redCar.paint(g);
		// greenCar.paint(g);
		// blueCar.paint(g);
		flushGraphics();
	}

	private void update()
	{
		if (++inputDelay > 3)
		{
			int keyState = getKeyStates();
			if ((keyState & RIGHT_PRESSED) != 0)
			{
				redCar.nextFrame();
				redCarAng = turnRight(redCar, redCarAng);
				inputDelay = 0;
			}
			else if ( (keyState & LEFT_PRESSED) != 0 )
			{
				redCar.prevFrame();
				redCarAng = turnLeft(redCar, redCarAng);
				inputDelay = 0;
			}
			greenCar.nextFrame();
			blueCar.nextFrame();
		}
		// TODO Auto-generated method stub

	}

	private int turnLeft(Sprite sprite, int spriteAng)
	{
		if (sprite.getFrame() == 3)
		{
			if (spriteAng == 0)
			{
				sprite.setTransform(Sprite.TRANS_ROT270);
				spriteAng = 270;
				sprite.move(0, sprite.getWidth());
			}
			else if (spriteAng == 90)
			{
				sprite.setTransform(Sprite.TRANS_NONE);
				spriteAng = 0;
				sprite.move(-sprite.getHeight(), 0);
			}
			else if (spriteAng == 180)
			{
				sprite.setTransform(Sprite.TRANS_ROT90);
				spriteAng = 90;
				sprite.move(0, -sprite.getWidth());
			}
			else if (spriteAng == 270)
			{
				sprite.setTransform(Sprite.TRANS_ROT180);
				spriteAng = 180;
				sprite.move(sprite.getHeight(), 0);
			}
		}
		
		return spriteAng;
	}

	private int turnRight(Sprite sprite, int spriteAng)
	{
		if (sprite.getFrame() == 0)
		{
			if (spriteAng == 0)
			{
				sprite.setTransform(Sprite.TRANS_ROT90);
				spriteAng = 90;
				sprite.move(sprite.getHeight(), 0);
			}
			else if (spriteAng == 90)
			{
				sprite.setTransform(Sprite.TRANS_ROT180);
				spriteAng = 180;
				sprite.move(0, sprite.getWidth());
			}
			else if (spriteAng == 180)
			{
				sprite.setTransform(Sprite.TRANS_ROT270);
				spriteAng = 270;
				sprite.move(-sprite.getHeight(), 0);
			}
			else if (spriteAng == 270)
			{
				sprite.setTransform(Sprite.TRANS_NONE);
				spriteAng = 0;
				sprite.move(0, -sprite.getWidth());
			}
		}
		
		return spriteAng;
	}
	
	

}
