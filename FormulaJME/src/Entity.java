import java.util.Date;

import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;


public class Entity
{
	public static double aTan2(double y, double x) {
		double teta;
		if ( Math.abs(y) <= 6 )
		{
			if ( x < 0 )
				teta = -Math.PI/2;
			else
				teta = Math.PI/2;
		}
		else
		{
			double d = x/y;
				teta = d/(1 + 0.28*d*d);
			if ( y < 0 )
				teta = teta+Math.PI;
		}
		
		if ( teta < 0 )
			teta += Math.PI*2;
		
		return teta;
	}


	protected static final int tileHeight = (750/10);
	protected static final int tileWidth = (675/9);
	protected static final int mapWidth = 14;
	protected static final int mapHeight = 30;
	protected static Player player;
	public static FormulaCanvas canvas;
	public static Ponto[] waypoint = new Ponto[5];
	public static int[] map;
	public Sprite car;
	protected int speed;
	protected int carAng;
	public int lap;
	protected int checkpoint;
	public Date time;
	
	public void paint(Graphics g)
	{
		car.paint(g);
	}
	
	protected void turnLeft()
	{
		if (car.getFrame() == 3)
		{
			if (carAng == 180)
			{
				car.setTransform(Sprite.TRANS_ROT270);
				carAng = 90;
				car.move(0, car.getWidth());
			}
			else if (carAng == 270)
			{
				car.setTransform(Sprite.TRANS_NONE);
				carAng = 180;
				car.move(-car.getHeight(), 0);
			}
			else if (carAng == 0)
			{
				car.setTransform(Sprite.TRANS_ROT90);
				carAng = 270;
				car.move(0, -car.getWidth());
			}
			else if (carAng == 90)
			{
				car.setTransform(Sprite.TRANS_ROT180);
				carAng = 0;
				car.move(car.getHeight(), 0);
			}
		}
		
	}

	protected void turnRight()
	{
		if (car.getFrame() == 0)
		{
			if (carAng == 180)
			{
				car.setTransform(Sprite.TRANS_ROT90);
				carAng = 270;
				car.move(car.getHeight(), 0);
			}
			else if (carAng == 270)
			{
				car.setTransform(Sprite.TRANS_ROT180);
				carAng = 0;
				car.move(0, car.getWidth());
			}
			else if (carAng == 0)
			{
				car.setTransform(Sprite.TRANS_ROT270);
				carAng = 90;
				car.move(-car.getHeight(), 0);
			}
			else if (carAng == 90)
			{
				car.setTransform(Sprite.TRANS_NONE);
				carAng = 180;
				car.move(0, -car.getWidth());
			}
		}
		
	}

}
