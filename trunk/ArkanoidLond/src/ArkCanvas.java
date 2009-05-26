import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;


public class ArkCanvas extends Canvas
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
	
	private Thread bThread;
	
	static final int BGColor = 0x0000FF;
	static final int FontColor = 0xFFFFFF;
	static final int BarColor = 0x00FF00;
	static final int BallColor = 0x00FFFF;
	
	static final int hBat = 5;
	static final int wBat = 30;
	
	static final int hBall = 5;
	static final int wBall = 5;
	
	static final int leapingFactor = 8;
	static final int yMargin = 20;
	
	public ArkCanvas()
	{
		acceleration = leapingFactor;
		scored = taken = 0;
		height = getHeight();
		width = getWidth();
		xbat = width / 2;
		
		fonte = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
		
		bThread = new Thread();
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
		while ( score.length() < 5 )
			score = '0' + score;

		took = String.valueOf(taken);
		while ( took.length() < 5 )
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
		g.fillRect(xbat - (wBat/2), height - (yMargin + (hBat/2)), wBat, hBat);
	}

	public void keyPressed(int key)
	{
		chave = getKeyName(key);
		if ( chave.equals("4") )
		{
			xbat -= wBat/leapingFactor;
			if ( xbat - (wBat/2) <= 0 )
				xbat = wBat/2;
		}
		if ( chave.equals("6") )
		{
			xbat += wBat/leapingFactor;
			if ( xbat + (wBat/2) >= width )
				xbat = width - (wBat/2);
		}
		if ( chave.equals("8") )
			yp += 10;
		if ( chave.equals("2") )
			yp -= 10;
		repaint();
	}

	public void ballIncrease()
	{
		yBall += acceleration;
		repaint();
	}
	
	public class ballThread implements Runnable
	{
		public ArkCanvas parent;
		
		public void run()
		{
			parent.ballIncrease();
		}
		
	}

}
