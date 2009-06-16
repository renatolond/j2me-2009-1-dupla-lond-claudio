import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;
import java.io.*;

public class HCanvas extends GameCanvas implements Runnable
{
	private Display display;
	private boolean sleeping;
	private long frameDelay;
	private int inputDelay;
	private Random rand;
	private Image background;
	private Image chickenHead;
	private static final int galinhas = 1;
	private static final int carros = 3;
	private Sprite[] chickenSprite = new Sprite[galinhas];
	private int[] chickenXSpeed = new int[galinhas];
	private Sprite[] carSprite = new Sprite[carros];
	private Sprite carPlayerSprite;
	private int[] carYSpeed = new int[carros];
	private boolean gameOver;
	private int numLives;
	private int score;

	public HCanvas(Display d)
	{
		super(true);
		display = d;

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
		numLives = 3;
		score = 0;

		// Initialize the background image and chicken and car sprites
		try
		{
			background = Image.createImage("/Highway.png");
			chickenHead = Image.createImage("/ChickenHead.png");
			
			chickenSprite[0] = new Sprite(Image.createImage("/Chicken.png"),
					22, 22);
			carSprite[0] = new Sprite(Image.createImage("/Car1.png"));
			carSprite[1] = new Sprite(Image.createImage("/Car2.png"));
			carPlayerSprite = new Sprite(Image.createImage("/Car3.png"));
			carSprite[2] = new Sprite(Image.createImage("/Car4.png"));

		} catch (IOException e)
		{
			System.err.println("Failed loading images!");
		}

		resetPositions();
		// Start the animation thread
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

	private void update()
	{
		// Check to see whether the game is being restarted
		if (gameOver)
		{
			int keyState = getKeyStates();
			if ((keyState & FIRE_PRESSED) != 0)
			{
				// Start a new game
				chickenSprite[0].setPosition(2, 77);
				gameOver = false;
				score = 0;
				numLives = 3;
			}

			// The game is over, so don't update anything
			return;
		}

		// Process user input to move the chicken
		if (++inputDelay > 2)
		{
			int keyState = getKeyStates();
			if ((keyState & LEFT_PRESSED) != 0)
			{
				// chickenSprite.move(-6, 0);
				carPlayerSprite.move(-6, 0);
				// chickenSprite.nextFrame();
			} else if ((keyState & RIGHT_PRESSED) != 0)
			{
				carPlayerSprite.move(6, 0);
				// chickenSprite.move(6, 0);
				// chickenSprite.nextFrame();
			}
			if ((keyState & UP_PRESSED) != 0)
			{
				// chickenSprite.move(0, -6);
				// chickenSprite.nextFrame();
			} else if ((keyState & DOWN_PRESSED) != 0)
			{
				// chickenSprite.move(0, 6);
				// chickenSprite.nextFrame();
			}
			// checkBounds(chickenSprite, false);

			// Reset the input delay
			inputDelay = 0;
		}
		carPlayerSprite.move(0, -2);
		checkBounds(carPlayerSprite, true);

		// See whether the chicken made it across
		// if (chickenSprite.getX() > 154) {
		// Play a sound for making it safely across
		// AlertType.WARNING.playSound(display);

		// Reset the chicken position and increment the score
		// chickenSprite.setPosition(2, 77);
		// score += 25;
		// }

		// Update the enemy chicken sprites
		for (int i = 0 ; i < galinhas ; i++ )
		{
			chickenSprite[i].move(3, 0);
			checkBounds(chickenSprite[i], true);
			
			if ( carPlayerSprite.collidesWith(chickenSprite[i], true))
			{
				// Play a sound for losing a life
				AlertType.ERROR.playSound(display);

				// Check for a game over
				 if (--numLives == 0) {
				 gameOver = true;
				 } else {
				// Reset positions
					 resetPositions();
				 }

				// No need to continue updating the chicken sprites
				 break;
			}
		}
		
		// Update the enemy car sprites
		for (int i = 0; i < carros; i++)
		{
			// Move the enemy car sprites
			carSprite[i].move(0, carYSpeed[i]);
			checkBounds(carSprite[i], true);

			// Check for a collision between the chicken and cars
			if ( carPlayerSprite.collidesWith(carSprite[i], true))
			{
			// Play a sound for losing a life
			AlertType.ERROR.playSound(display);

			// Check for a game over
			 if (--numLives == 0) {
			 gameOver = true;
			 } else {
			// Reset positions
				 resetPositions();
			 }

			// No need to continue updating the car sprites
			 break;
			// }
			}
			
			for ( int j = 0 ; j < galinhas ; j++ )
			{
				if ( carSprite[i].collidesWith(chickenSprite[j], true))
				{
					chickenSprite[j].setPosition(2, 77);
				}
			}
		}
	}

	private void resetPositions()
	{
		chickenSprite[0].setPosition(2, 77);

		carSprite[0].setPosition(27, 0);
		carYSpeed[0] = 3;
		carSprite[1].setPosition(62, 0);
		carYSpeed[1] = 1;
		carPlayerSprite.setPosition(93, 67);
		// carYSpeed[2] = -2;
		carSprite[2].setPosition(128, 64);
		carYSpeed[2] = -5;
	}

	private void draw(Graphics g)
	{
		// Draw the highway background
		g.drawImage(background, 0, 0, Graphics.TOP | Graphics.LEFT);

		// Draw the number of remaining lives
		for (int i = 0; i < numLives; i++)
			g.drawImage(chickenHead, 180 - ((i + 1) * 8), 170, Graphics.TOP
					| Graphics.LEFT);

		// Draw the chicken sprite
		chickenSprite[0].paint(g);

		// Draw the car sprites
		for (int i = 0; i < carros; i++)
			carSprite[i].paint(g);

		carPlayerSprite.paint(g);

		if (gameOver)
		{
			// Draw the game over message and score
			g.setColor(255, 255, 255); // white
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
					Font.SIZE_LARGE));
			g.drawString("GAME OVER", 90, 40, Graphics.TOP | Graphics.HCENTER);
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
					Font.SIZE_MEDIUM));
			g.drawString("You scored " + score + " points.", 90, 70,
					Graphics.TOP | Graphics.HCENTER);
		}

		// Flush the offscreen graphics buffer
		flushGraphics();
	}

	private void checkBounds(Sprite sprite, boolean wrap)
	{
		// Wrap/stop the sprite if necessary
		if (wrap)
		{
			// Wrap the sprite around the edges of the screen
			if (sprite.getX() < -sprite.getWidth())
				sprite.setPosition(getWidth(), sprite.getY());
			else if (sprite.getX() > getWidth())
				sprite.setPosition(-sprite.getWidth(), sprite.getY());
			if (sprite.getY() < -sprite.getHeight())
				sprite.setPosition(sprite.getX(), getHeight());
			else if (sprite.getY() > getHeight())
				sprite.setPosition(sprite.getX(), -sprite.getHeight());
		} else
		{
			// Stop the sprite at the edges of the screen
			if (sprite.getX() < 0)
				sprite.setPosition(0, sprite.getY());
			else if (sprite.getX() > (getWidth() - sprite.getWidth()))
				sprite.setPosition(getWidth() - sprite.getWidth(), sprite
						.getY());
			if (sprite.getY() < 0)
				sprite.setPosition(sprite.getX(), 0);
			else if (sprite.getY() > (getHeight() - sprite.getHeight()))
				sprite.setPosition(sprite.getX(), getHeight()
						- sprite.getHeight());
		}
	}
}
