import javax.microedition.lcdui.game.Sprite;


public class Player extends Entity
{
	
	int x, y;
	int dx, dy;
	
	Player()
	{
		carAng = 180;
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
		dx = -(int)dsin;
		dy = (int)dcos;
		
		x += dx;
		if ( x <= tileWidth )
		{
			dx = dx + (tileWidth - x);
			x = tileWidth;
		}
		if ( x >= (size*tileWidth-1)-tileWidth/2 )
		{
			dx = dx - (x - ((size*tileWidth-1)-tileWidth/2));
			x = (size*tileWidth-1)-tileWidth/2;
		}
		
		y += dy;
		if ( y <= tileHeight )
		{
			dy = dy + (tileHeight - y);
			y = tileHeight;
		}
		if ( y >= (size*tileHeight-1)-tileHeight/2 )
		{
			dy = dy - (y - ((size*tileHeight-1)-tileHeight/2));
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
