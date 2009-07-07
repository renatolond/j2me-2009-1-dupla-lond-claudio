import javax.microedition.lcdui.game.Sprite;


public class Player extends Entity
{
	
	int x, y;
	int speed;
	int dx, dy;
	
	Player()
	{
		carAng = 0;
		speed = 0;
		dx = dy = 0;
	}
	
	void update()
	{
		dx = dy = 1;
		int ang = carAng;
		ang += car.getFrame() * 45/2;
		double dsin = Math.sin(ang*Math.PI/180)*speed;
		double dcos = Math.cos(ang*Math.PI/180)*speed;
		dx = (int)dsin;
		dy = -(int)dcos;
		
		x += dx;
		if ( x <= tileWidth )
		{
			dx = 0;
			x = tileWidth;
		}
		if ( x >= (size*tileWidth-1)-tileWidth/2 )
		{
			dx = 0;
			x = (size*tileWidth-1)-tileWidth/2;
		}
		y += dy;
		if ( y <= tileHeight )
		{
			y = tileHeight;
			dy = 0;
		}
		if ( y >= (size*tileHeight-1)-tileHeight/2 )
		{
			dy = 0;
			y = (size*tileHeight-1)-tileHeight/2;
		}
		
		if ( dx == 0 && dy == 0 )
		{
			speed = 0;
		}
	}
	
	void setSprite(Sprite s)
	{
		car = s;
	}
	
	void right()
	{
		car.nextFrame();
		turnRight();
	}
	
	void left()
	{
		car.prevFrame();
		turnLeft();
	}
	
}
