import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;


public class Entity
{
	protected static final int tileHeight = (750/10);
	protected static final int tileWidth = (675/9);
	protected static final int size = 14;
	public Sprite car;
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
