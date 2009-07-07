import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class FormulaCanvas extends GameCanvas implements Runnable
{
	private static final int tileHeight = (750/10);
	private static final int tileWidth = (675/9);
	private static final int size = 14;
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
	private Image[] pista = new Image[10*9];
	private Player player;
	int maxEnemies = 2;
	Enemy enemies[] = new Enemy[maxEnemies]; 
	private int[] map = {   80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80,
							80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80,
							80, 80,  0,  1, 52, 52, 52, 52, 52, 52,  2,  3, 80, 80,
							80, 80, 10, 11, 53, 53, 53, 53, 53, 53, 12, 13, 80, 80,
							80, 80, 50, 51, 60, 61, 60, 61, 60, 61, 50, 51, 80, 80,
							80, 80, 50, 51, 70, 71, 70, 71, 70, 71, 50, 51, 80, 80,
							80, 80, 50, 51, 23, 23, 80, 80, 21, 21, 50, 51, 80, 80,
							80, 80, 50, 51, 23, 23, 80, 80, 21, 21, 50, 51, 80, 80,
							80, 80, 50, 51, 64, 65, 64, 65, 64, 65, 50, 51, 80, 80,
							80, 80, 50, 51, 74, 75, 74, 75, 74, 75, 50, 51, 80, 80,
							80, 80, 20, 21, 52, 52, 52, 52, 52, 52, 22, 23, 80, 80,
							80, 80, 30, 31, 53, 53, 53, 53, 53, 53, 32, 33, 80, 80,
							80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80,
							80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80};
	private Ponto[] waypoint = new Ponto[4];
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
		waypoint[0] = new Ponto( 3*tileWidth,  3*tileHeight);
		waypoint[1] = new Ponto(11*tileWidth,  3*tileHeight);
		waypoint[2] = new Ponto(11*tileWidth, 11*tileHeight);
		waypoint[3] = new Ponto( 3*tileWidth, 11*tileHeight);
		player = new Player();
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
			
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					int sh = 675/9; int sw = 750 / 10;
					pista[i+j*10] = Image.createImage(Image.createImage("/pista.png"), i * sw, j
							* sh, sw, sh, Sprite.TRANS_NONE);
				}
			}
//			t = 0;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.x = celWidth / 2 - redCar.getWidth()/2;
		player.y = celHeight / 2 - redCar.getHeight()/2;
		
		redCar.setPosition(player.x, player.y);
		
		greenCar.setPosition(player.x, player.y);
		blueCar.setPosition(player.x, player.y);
		player.x = 4*tileWidth - celWidth/2 - tileWidth/2 + tileWidth/10;
		greenCar.move(redCar.getWidth() - redCar.getWidth()/10,0);
		blueCar.move(0, redCar.getHeight() + redCar.getHeight()/2);

		enemies[0] = new Enemy(greenCar);
		enemies[1] = new Enemy(blueCar);
		
		player.y = 8*tileHeight;
		player.setSprite(redCar);

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
		amountX = (celWidth / tileWidth) + 2;
		amountY = (celHeight / tileHeight) + 2;
		
		mapX = player.x / tileWidth -1 ; 
		mapY = player.y / tileHeight -1 ;
		
		int sobraX, sobraY;
		
		sobraX = player.x % tileWidth;
		sobraY = player.y % tileHeight;
		
//		System.out.println("Map: "+mapX+","+mapY);
//		System.out.println("Player: "+playerX+","+playerY);
//		System.out.println("Sobra: "+sobraX+","+sobraY);
		
		for (int i = 0; i < amountX; i++)
		{
			for (int j = 0; j < amountY; j++)
			{
				int p;
				p = (mapX+i)+(mapY+j)*size;
				if ( p >= (size*size) ) continue;
				try
				{
				g.drawImage(pista[map[p]], i*tileWidth - sobraX, j*tileHeight - sobraY, Graphics.LEFT|Graphics.TOP);
				}
				catch (Exception e)
				{
					System.out.println(p);
					e.printStackTrace();
				}
			}
		}
		//g.setColor(0xFF, 0xFF, 0xFF);
		//g.fillRect(0, 0, celHeight, celWidth);
		for ( int i = 0 ; i < maxEnemies ; i++ )
			enemies[i].paint(g);
		player.paint(g);
		flushGraphics();
	}

	private void update()
	{
		if (++inputDelay > 3)
		{
			int keyState = getKeyStates();
			if ((keyState & RIGHT_PRESSED) != 0 && (keyState & LEFT_PRESSED) == 0)
			{
				player.right();
			}
			else if ( (keyState & LEFT_PRESSED) != 0 && (keyState & RIGHT_PRESSED) == 0)
			{
				player.left();
			}
			else if ( (keyState & UP_PRESSED ) != 0 )
			{
				player.speed+=2;
			}
			else if ( (keyState & DOWN_PRESSED ) != 0 )
			{
				player.speed-=2;
			}
			
			enemies[0].update();
			enemies[1].update();
			inputDelay = 0;
		}
		
		int dx, dy;
		
		player.update();
		dx = player.dx;
		dy = player.dy;
		
		for ( int i = 0 ; i < maxEnemies ; i++ )
			enemies[i].move(-dx, -dy);
		//blueCar.move(-dx, -dy);
		//greenCar.move(-dx, -dy);
		//redCar.move(dx, dy);
		// TODO Auto-generated method stub

	}
}
