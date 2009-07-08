import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import com.sun.perseus.model.Time;

public class FormulaCanvas extends GameCanvas implements Runnable
{
	private static final int tileHeight = (750/10);
	private static final int tileWidth = (675/9);
	private static final int mapWidth = 14;
	private static final int mapHeight = 30;
	private static final int maxLaps = 3;
	private int inputDelay;
	private int frameDelay;
	private int celWidth;
	private int celHeight;
	private Display display;
	private String msg;
	private boolean sleeping;
	private boolean gameOver;
	private int speed;
	private Random rand;
	// private Sprite cars;
	private Sprite redCar, blueCar, greenCar;
	private Image[] pista = new Image[10*9];
	private Player player;
	int position;
	int maxEnemies = 2;
	Enemy enemies[] = new Enemy[maxEnemies]; 
	private int[] map = {   80, 
							64, 65, 64, 65, 64, 65, 64, 65, 64, 65, 64, 65, 64, 65, 
							74, 75, 74, 75, 74, 75, 74, 75, 74, 75, 74, 75, 74, 75, 
							80, 80,  0,  1, 52, 52, 52, 52, 52, 52,  2,  3, 66, 67, 
							80, 80, 10, 11, 53, 53, 53, 53, 53, 53, 12, 13, 76, 77,
							80, 80, 50, 51, 60, 61, 60, 61, 60, 61, 50, 51, 66, 67, 
							80, 80, 50, 51, 70, 71, 70, 71, 70, 71, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 76, 77,
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 66, 67, 
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 66, 67, 
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 76, 77,
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 66, 67, 
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 76, 77,
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 66, 67, 
							80, 80, 50, 51, 66, 67, 80, 80, 62, 63, 50, 51, 76, 77,
							80, 80, 50, 51, 76, 77, 80, 80, 72, 73, 50, 51, 66, 67, 
							80, 80, 50, 51, 64, 65, 64, 65, 64, 65, 50, 51, 76, 77,
							80, 80, 50, 51, 74, 75, 74, 75, 74, 75, 50, 51, 66, 67, 
							80, 80, 20, 21, 52, 52, 52, 52, 52, 52, 22, 23, 76, 77,
							80, 80, 30, 31, 53, 53, 53, 53, 53, 53, 32, 33, 66, 67, 
							60, 61, 60, 61, 60, 61, 60, 61, 60, 61, 60, 61, 60, 61,
							60, 61, 60, 61, 60, 61, 60, 61, 60, 61, 60, 61, 60, 61};
	private int t;
	private long startime;

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
	
	private String parseTime(long time)
    {
            String s = "";

            long min = (time-startime ) / 60 / 1000;
            long seg = ((time-startime ) / 1000) - min * 60;

            if (min < 10)
                    s += "0" + min;
            else
                    s += min;

            if (seg < 10)
                    s += ":0" + seg;
            else
                    s += ":" + seg;

            return s;
    }
	public void setMsg(long s)
	{
		msg = parseTime(s);
		position = 1;
		
		for ( int i = 0 ; i < maxEnemies ; i++ )
		{
			//msg += player.checkpoint + " " + enemies[i].checkpoint;
			if ( player.lap < enemies[i].lap )
			{
				position++;
				continue;
			}
			else if ( player.lap == enemies[i].lap )
				if ( player.checkpoint < enemies[i].checkpoint )
				{
					position++;
					continue;
				}
				else if ( player.checkpoint == enemies[i].checkpoint )
					if ( player.time.getTime() > enemies[i].time.getTime() )
					{
						position++;
						continue;
					}
		}
	}

	public void start()
	{
		
		// Set the canvas as the current screen
		display.setCurrent(this);

		// Initialize the random number generator
		rand = new Random();
		startime = (new Date()).getTime();

		// Initialize the game variables
		gameOver = false;
		msg = new String();
		position = 1;
		Entity.waypoint[0] = new Ponto( 3*tileWidth,  3*tileHeight);
		Entity.waypoint[1] = new Ponto((mapWidth-3)*tileWidth,  3*tileHeight);
		Entity.waypoint[2] = new Ponto((mapWidth-3)*tileWidth, (mapHeight-3)*tileHeight);
		Entity.waypoint[3] = new Ponto( 3*tileWidth, (mapHeight-3)*tileHeight);
		Entity.canvas = this;
		Entity.map = map;
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
		resetState();
		Entity.waypoint[4] = new Ponto( player.x, player.y - player.car.getHeight() );
		Thread t = new Thread(this);
		t.start();
	}

	private void resetState()
	{
		player = new Player();
		Entity.player = player;
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
		
		g.setColor(0xFF, 0xFF, 0xFF);
		g.fillRect(0, 0, celHeight, celWidth);
		
		for (int i = 0; i < amountX; i++)
		{
			for (int j = 0; j < amountY; j++)
			{
				int p;
				p = ((mapX+i)+(mapY+j)*mapWidth) +1;
				if ( p > (mapHeight*mapWidth) ) p = 0;
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
		for ( int i = 0 ; i < maxEnemies ; i++ )
			enemies[i].paint(g);
		player.paint(g);
		if (!gameOver)
		{
			g.drawString("LAP: "+(player.lap+1) + "/" + maxLaps, 0, 0, Graphics.TOP | Graphics.LEFT);
			g.drawString(msg, 0, 10, Graphics.TOP | Graphics.LEFT);
			g.drawString("POS: "+position + "/" + (maxEnemies+1), 0, celHeight-20, Graphics.TOP | Graphics.LEFT);
		}
		
		if ( gameOver )
		{
			g.drawString("Game Over!", celHeight/2, celWidth/2, Graphics.TOP | Graphics.LEFT);
			
			
			if ( position == 1 )
				g.drawString("You Win!!", celWidth/2, celHeight/2 +20, Graphics.TOP | Graphics.LEFT);
			else
				g.drawString("You lose!", celWidth/2, celHeight/2 +20, Graphics.TOP | Graphics.LEFT);

		}
		flushGraphics();
	}

	private void update()
	{
		if ( player.lap == maxLaps )
		{
			gameOver = true;
		}
		if ( gameOver )
		{
			int keyState = getKeyStates();
			if ((keyState & FIRE_PRESSED) != 0)
			{
				gameOver = false;
				resetState();
			}
			
			return;
		}
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
			inputDelay = 0;
		}
		
		int dx, dy;
		
		player.update();
		dx = player.dx;
		dy = player.dy;
		
		for ( int i = 0 ; i < maxEnemies ; i++ )
			enemies[i].move(-dx, -dy);
		
		for ( int i = 0 ; i < maxEnemies ; i++ )
			enemies[i].update();
	}
}
