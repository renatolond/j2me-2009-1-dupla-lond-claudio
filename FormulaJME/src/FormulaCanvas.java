import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class FormulaCanvas extends GameCanvas implements Runnable
{

	private static final int tileSize = (750/5);
	private int inputDelay;
	private int frameDelay;
	private int celWidth;
	private int celHeight;
	private Display display;
	private boolean sleeping;
	private boolean gameOver;
	private int speed;
	private Random rand;
	// private Sprite cars;
	private Sprite redCar, blueCar, greenCar;
	private Image[] pista = new Image[5*5];
	private int redCarAng, blueCarAng, greenCarAng;
	int playerX, playerY;
	private int[] map = { 17, 18, 17, 18, 17,
							13, 24, 24, 24, 24,
							14, 24, 18, 24, 24,
							13, 24, 24, 24, 24,
							14, 24, 24, 24, 24};
	private int t;

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
			speed = 0;

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
			
			for (int i = 0; i < 5; i++)
			{
				for (int j = 0; j < 5; j++)
				{
					int s = 750 / 5;
					pista[i+j*5] = Image.createImage(Image.createImage("/pista.png"), i * s, j
							* s, 750 / 5, 750 / 5, Sprite.TRANS_NONE);
				}
			}
//			t = 0;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerX = celWidth / 2 - redCar.getWidth()/2;
		playerY = celHeight / 2 - redCar.getHeight()/2;
		redCar.setPosition(playerX, playerY);
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
		int amountX, amountY, mapX, mapY;
		amountX = (celWidth / tileSize) + 2;
		amountY = (celHeight / tileSize) + 2;
		
		mapX = playerX / tileSize; 
		mapY = playerY / tileSize;
		
		for (int i = 0; i < amountX; i++)
		{
			for (int j = 0; j < amountY; j++)
			{
				int p;
				p = (mapX+i)+(mapY+j)*5;
				g.drawImage(pista[map[p]], i*tileSize, j*tileSize, Graphics.LEFT|Graphics.TOP);
			}
		}
		//g.setColor(0xFF, 0xFF, 0xFF);
		//g.fillRect(0, 0, celHeight, celWidth);
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
			if ((keyState & RIGHT_PRESSED) != 0 && (keyState & LEFT_PRESSED) == 0)
			{
				int rx, ry;
				rx = redCar.getX();
				ry = redCar.getY();
				
				redCar.nextFrame();
				redCarAng = turnRight(redCar, redCarAng);
				
				playerX += redCar.getX() - rx;
				playerY += redCar.getY() - ry;
				redCar.setPosition(rx, ry);

				inputDelay = 0;
			}
			else if ( (keyState & LEFT_PRESSED) != 0 && (keyState & RIGHT_PRESSED) == 0)
			{
				int rx, ry;
				rx = redCar.getX();
				ry = redCar.getY();
				redCar.prevFrame();
				redCarAng = turnLeft(redCar, redCarAng);
				
				playerX += redCar.getX() - rx;
				playerY += redCar.getY() - ry;
				redCar.setPosition(rx, ry);

				inputDelay = 0;
			}
			else if ( (keyState & UP_PRESSED ) != 0 )
			{
				speed+=2;
				inputDelay = 0;
			}
			else if ( (keyState & DOWN_PRESSED ) != 0 )
			{
				speed-=2;
				inputDelay = 0;
			}
			greenCar.nextFrame();
			blueCar.nextFrame();
		}
		int dx, dy;
		dx = dy = 1;
		int ang = redCarAng;
		ang += redCar.getFrame() * 45/2;
		double dsin = Math.sin(ang*Math.PI/180)*speed;
		double dcos = Math.cos(ang*Math.PI/180)*speed;
		dx = (int)dsin;
		dy = -(int)dcos;
		playerX += dx;
		playerY += dy;
		//redCar.move(dx, dy);
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
