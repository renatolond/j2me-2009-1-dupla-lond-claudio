import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;


public class Entity
{
	public static double aTan2(double y, double x) {
		double coeff_1 = Math.PI / 4d;
		double coeff_2 = 3d * coeff_1;
		double abs_y = Math.abs(y);
		double angle;
		if (x >= 0d) {
			double r = (x - abs_y) / (x + abs_y);
			angle = coeff_1 - coeff_1 * r;
		} else {
			double r = (x + abs_y) / (abs_y - x);
			angle = coeff_2 - coeff_1 * r;
		}
		return y < 0d ? -angle : angle;
	}


	protected static final int tileHeight = (750/10);
	protected static final int tileWidth = (675/9);
	protected static final int size = 14;
	protected static Player player;
	public static Ponto[] waypoint = new Ponto[4];
	public Sprite car;
	int speed;
	int carAng;
	
	public void paint(Graphics g)
	{
		car.paint(g);
	}
	
	protected void turnLeft()
	{
		if (car.getFrame() == 3)
		{
			if (carAng == 0)
			{
				car.setTransform(Sprite.TRANS_ROT270);
				carAng = 270;
				car.move(0, car.getWidth());
			}
			else if (carAng == 90)
			{
				car.setTransform(Sprite.TRANS_NONE);
				carAng = 0;
				car.move(-car.getHeight(), 0);
			}
			else if (carAng == 180)
			{
				car.setTransform(Sprite.TRANS_ROT90);
				carAng = 90;
				car.move(0, -car.getWidth());
			}
			else if (carAng == 270)
			{
				car.setTransform(Sprite.TRANS_ROT180);
				carAng = 180;
				car.move(car.getHeight(), 0);
			}
		}
		
	}

	protected void turnRight()
	{
		if (car.getFrame() == 0)
		{
			if (carAng == 0)
			{
				car.setTransform(Sprite.TRANS_ROT90);
				carAng = 90;
				car.move(car.getHeight(), 0);
			}
			else if (carAng == 90)
			{
				car.setTransform(Sprite.TRANS_ROT180);
				carAng = 180;
				car.move(0, car.getWidth());
			}
			else if (carAng == 180)
			{
				car.setTransform(Sprite.TRANS_ROT270);
				carAng = 270;
				car.move(-car.getHeight(), 0);
			}
			else if (carAng == 270)
			{
				car.setTransform(Sprite.TRANS_NONE);
				carAng = 0;
				car.move(0, -car.getWidth());
			}
		}
		
	}

}
