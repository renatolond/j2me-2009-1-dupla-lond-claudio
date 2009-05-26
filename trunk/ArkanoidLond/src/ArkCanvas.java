import java.util.Random;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class ArkCanvas extends Canvas implements Runnable
{

	private String chave;
	private int xp;
	private int yp;
	private int height;
	private int width;
	private Font fonte;
	private int xbat;
	private int taken;
	private int scored;
	private int acceleration;
	private int xBall;
	private int yBall;
	private int mAtraso;

	private Random rand;

	private Thread bThread;
	private int yBat;

	static final int BGColor = 0x0000FF;
	static final int FontColor = 0xFFFFFF;
	static final int BarColor = 0x00FF00;
	static final int BallColor = 0xFFFF00;

	static final int hBat = 5;
	static final int wBat = 30;

	static final int hBall = 10;
	static final int wBall = 10;

	static final int leapingFactor = 8;
	static final int yMargin = 20;

	public ArkCanvas()
	{
		acceleration = leapingFactor;
		scored = taken = 0;
		height = getHeight();
		width = getWidth();
		xbat = width / 2;

		fonte = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,
				Font.SIZE_LARGE);

		rand = new Random();
		rand.setSeed(10930193);
		yBat = height - yMargin;

		mAtraso = 60;
		bThread = new Thread(this);
		bThread.start();

		yBall = 0;
		xBall = width / 2;
	}

	protected void paint(Graphics g)
	{
		DesenhaFundo(g);
		DesenhaScore(g);
		DesenhaBola(g);
		DesenhaBastao(g);
	}

	private void DesenhaFundo(Graphics g)
	{
		g.setColor(BGColor);
		g.fillRect(0, 0, width, height);
	}

	private void DesenhaScore(Graphics g)
	{
		String score;
		String took;
		Font tmp;

		tmp = g.getFont();
		g.setColor(FontColor);
		g.setFont(fonte);

		score = String.valueOf(scored);
		while (score.length() < 5)
			score = '0' + score;

		took = String.valueOf(taken);
		while (took.length() < 5)
			took = '0' + took;

		g.drawString(score, 0, 0, Graphics.TOP | Graphics.LEFT);
		g.drawString(took, width, 0, Graphics.TOP | Graphics.RIGHT);

		g.setFont(tmp);
	}

	private void DesenhaBola(Graphics g)
	{
		g.setColor(BallColor);
		g.fillArc(xBall, yBall, wBall, hBall, 0, 360);
	}

	private void DesenhaBastao(Graphics g)
	{
		g.setColor(BarColor);
		g.fillRect(xbat - (wBat / 2), yBat - (hBat / 2), wBat, hBat);
	}

	public void keyPressed(int key)
	{
		chave = getKeyName(key);
		if (chave.equals("4")||chave.equals("LEFT"))
		{
			xbat -= wBat / leapingFactor;
			if (xbat - (wBat / 2) <= 0)
				xbat = wBat / 2;
		}
		if (chave.equals("6")||chave.equals("RIGHT"))
		{
			xbat += wBat / leapingFactor;
			if (xbat + (wBat / 2) >= width)
				xbat = width - (wBat / 2);
		}
		if (chave.equals("8"))
			yp += 10;
		if (chave.equals("2"))
			yp -= 10;
		repaint();
	}

	public void run()
	{
		while (true)
		{
			tick();
			yBall += acceleration;
			repaint();
			Collision();
			ScoreCount();

			try
			{
				Thread.sleep(mAtraso);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void Collision()
	{
		int nextPlace = yBall + acceleration;
		if (xBall >= xbat - (wBat / 2) && xBall <= xbat + (wBat / 2))
			if (nextPlace >= yBat - (hBat / 2))
			{
				yBall += acceleration/2;
				acceleration = -acceleration;
			}
	}

	private void ScoreCount()
	{
		if (yBall + acceleration >= height)
		{
			taken++;
			yBall = 0;
			xBall = (rand.nextInt() % (width - wBall)) + (wBall / 2);
			if (xBall - (wBall / 2) <= 0)
				xBall = wBall / 2;
			if (xBall + (wBall / 2) >= width)
				xBall = width - (wBall / 2);
			
		} else if (yBall + acceleration <= 0)
		{
			yBall = 0;
			xBall = (rand.nextInt() % (width - wBall)) + (wBall / 2);
			if (xBall - (wBall / 2) <= 0)
				xBall = wBall / 2;
			if (xBall + (wBall / 2) >= width)
				xBall = width - (wBall / 2);
			acceleration = -acceleration;
			scored++;
		}
	}

	private void tick()
	{
		// TODO Auto-generated method stub

	}

}
