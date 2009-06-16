import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class HCanvas extends GameCanvas implements Runnable
{
	private Display display;
	private boolean sleeping;
	private long frameDelay;
	private int inputDelay;
	private Random rand;
	private Image background;
	private Image chickenHead;
	private static final int galinhas = 4;
	private static final int carros = 3;
	private static final int maxSplatters = 10;
	private Sprite[] chickenSprite = new Sprite[galinhas];
	private int[] chickenXSpeed = new int[galinhas];
	private Sprite[] carSprite = new Sprite[carros];
	private Sprite carPlayerSprite;
	private Sprite blood;
	private int[] carYSpeed = new int[carros];
	private SpriteQueue bloodQ;
	private boolean gameOver;
	private int numLives;
	private int score;
	private int celHeight;
	private int celWidth;

	public HCanvas(Display d)
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
		numLives = 3;
		score = 0;
		bloodQ = new SpriteQueue();

		// Initialize the background image and chicken and car sprites
		try
		{
			background = Image.createImage("/Highway.png");
			chickenHead = Image.createImage("/ChickenHead.png");

			for (int i = 0; i < (galinhas / 2) + (galinhas % 2); i++)
			{
				chickenSprite[i] = new Sprite(
						Image.createImage("/Chicken.png"), 22, 22);
			}
			for (int i = (galinhas / 2) + (galinhas % 2); i < galinhas; i++)
			{
				chickenSprite[i] = new Sprite(chickenSprite[0]);
				chickenSprite[i].setTransform(Sprite.TRANS_MIRROR);
			}
			carSprite[0] = new Sprite(Image.createImage("/Car1.png"));
			carSprite[1] = new Sprite(Image.createImage("/Car2.png"));
			carPlayerSprite = new Sprite(Image.createImage("/Car3.png"));
			carSprite[2] = new Sprite(Image.createImage("/Car4.png"));
			blood = new Sprite(Image.createImage("/Blood.png"));

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
				resetPositions();
				gameOver = false;
				score = 0;
				numLives = 3;
			}

			// The game is over, so don't update anything
			return;
		}

		int keyState = getKeyStates();
		// Process user input to move the chicken
		if (++inputDelay > 2)
		{
			if ((keyState & DOWN_PRESSED) == 0)
			{
				if ((keyState & LEFT_PRESSED) != 0)
				{
					carPlayerSprite.move(-6, 0);
				} else if ((keyState & RIGHT_PRESSED) != 0)
				{
					carPlayerSprite.move(6, 0);
				}
			}

			// Reset the input delay
			inputDelay = 0;
		}
		if ((keyState & DOWN_PRESSED) == 0)
			carPlayerSprite.move(0, -2);
		if ( checkBounds(carPlayerSprite, true) )
			score += 25;

		// See whether the chicken made it across
		// if (chickenSprite.getX() > 154) {
		// Play a sound for making it safely across
		// AlertType.WARNING.playSound(display);

		// Reset the chicken position and increment the score
		// chickenSprite.setPosition(2, 77);
		// score += 25;
		// }

		// Update the enemy chicken sprites
		for (int i = 0; i < galinhas; i++)
		{
			chickenSprite[i].move(chickenXSpeed[i], 0);
			chickenSprite[i].nextFrame();
			checkBounds(chickenSprite[i], true);

			if (carPlayerSprite.collidesWith(chickenSprite[i], true))
			{
				// Play a sound for losing a life
				AlertType.ERROR.playSound(display);

				// Check for a game over
				if (--numLives == 0)
				{
					gameOver = true;
				} else
				{
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
			if (carPlayerSprite.collidesWith(carSprite[i], true))
			{
				// Play a sound for losing a life
				AlertType.ERROR.playSound(display);

				// Check for a game over
				if (--numLives == 0)
				{
					gameOver = true;
				} else
				{
					// Reset positions
					resetPositions();
				}

				// No need to continue updating the car sprites
				break;
				// }
			}

			for (int j = 0; j < galinhas; j++)
			{
				if (carSprite[i].collidesWith(chickenSprite[j], true))
				{
					Sprite t;
					t = new Sprite(blood);
					t.setPosition(chickenSprite[j].getX(), chickenSprite[j].getY());
					bloodQ.enqueue(t);
					if ( bloodQ.size() > maxSplatters )
						bloodQ.dequeue();
					
					if (chickenXSpeed[j] > 0)
					{
						chickenSprite[j]
								.setPosition(2, rand.nextInt(celHeight));
						chickenXSpeed[j] = rand.nextInt(3) + 1;
					} else
					{
						chickenSprite[j].setPosition(celWidth - 2, rand
								.nextInt(celHeight));
						chickenXSpeed[j] = -rand.nextInt(3) - 1;
					}
				}
			}
		}
	}

	private void resetPositions()
	{
		for (int i = 0; i < (galinhas / 2) + (galinhas % 2); i++)
		{
			chickenSprite[i].setPosition(2, rand.nextInt(celHeight));
			chickenXSpeed[i] = rand.nextInt(3) + 1;
		}
		for (int i = (galinhas / 2) + (galinhas % 2); i < galinhas; i++)
		{
			chickenSprite[i].setPosition(celWidth - 2, rand.nextInt(celHeight));
			chickenXSpeed[i] = -rand.nextInt(3) - 1;
		}
		bloodQ = new SpriteQueue();

		carSprite[0].setPosition(27, 0);
		carYSpeed[0] = rand.nextInt(10) + 1;
		carSprite[1].setPosition(62, 0);
		carYSpeed[1] = rand.nextInt(10) + 1;
		carPlayerSprite.setPosition(93, celHeight);
		carSprite[2].setPosition(128, celHeight);
		carYSpeed[2] = -rand.nextInt(10) - 1;
	}

	private void draw(Graphics g)
	{
		// Draw the highway background
		g.drawImage(background, 0, 0, Graphics.TOP | Graphics.LEFT);

		for (int i = 0 ; i < bloodQ.size() ; i++ )
		{
			bloodQ.at(i).paint(g);
		}

		// Draw the number of remaining lives
		for (int i = 0; i < numLives; i++)
			g.drawImage(chickenHead, 180 - ((i + 1) * 8), celHeight - 15,
					Graphics.TOP | Graphics.LEFT);

		// Draw the chicken sprite
		for (int i = 0; i < galinhas; i++)
		{
			chickenSprite[i].paint(g);
		}

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

	private boolean checkBounds(Sprite sprite, boolean wrap)
	{
		boolean touched;
		
		touched = false;
		
		// Wrap/stop the sprite if necessary
		if (wrap)
		{
			// Wrap the sprite around the edges of the screen
			if (sprite.getX() < -sprite.getWidth())
			{
				sprite.setPosition(getWidth(), sprite.getY());
				touched = true;
			}
			else if (sprite.getX() > getWidth())
			{
				sprite.setPosition(-sprite.getWidth(), sprite.getY());
				touched = true;
			}
			if (sprite.getY() < -sprite.getHeight())
			{
				sprite.setPosition(sprite.getX(), getHeight());
				touched = true;
			}
			else if (sprite.getY() > getHeight())
			{
				sprite.setPosition(sprite.getX(), -sprite.getHeight());
				touched = true;
			}
		} else
		{
			// Stop the sprite at the edges of the screen
			if (sprite.getX() < 0)
			{
				sprite.setPosition(0, sprite.getY());
				touched = true;
			}
			else if (sprite.getX() > (getWidth() - sprite.getWidth()))
			{
				sprite.setPosition(getWidth() - sprite.getWidth(), sprite
						.getY());
				touched = true;
			}
			if (sprite.getY() < 0)
			{
				sprite.setPosition(sprite.getX(), 0);
				touched = true;
			}
			else if (sprite.getY() > (getHeight() - sprite.getHeight()))
			{
				sprite.setPosition(sprite.getX(), getHeight()
						- sprite.getHeight());
				touched = true;
			}
		}
		
		return touched;
	}
}
