import java.util.Date;

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
		lap = checkpoint = 0;
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
		
		int mapX = x / tileWidth; 
		int mapY = y / tileHeight;
		
		int p = mapX + mapY*mapWidth;
		if ( p >= mapWidth*mapHeight )
			p = 0;
		
		if ( map[p] == 80 )
		{
			if ( speed > 3 )
				speed = (int)(speed * 0.9);
		}
		
		double vx, vy, dist;
		vy = waypoint[checkpoint].y - y;
		vx = waypoint[checkpoint].x - x;
		dist = (vx*vx) + (vy*vy);
		dist = Math.sqrt(dist);
		
		if ( dist <= tileHeight *2 )
		{
			time = new Date();
			checkpoint++;
			if ( checkpoint == waypoint.length )
			{
				lap++;
				checkpoint %= waypoint.length;
			}
			canvas.setMsg(time.toString());
			return;
		}
		
		x += dx;
		if ( x <= tileWidth )
		{
			dx = dx + (tileWidth - x);
			x = tileWidth;
		}
		if ( x >= (mapWidth*tileWidth-1)-tileWidth/2 )
		{
			dx = dx - (x - ((mapWidth*tileWidth-1)-tileWidth/2));
			x = (mapWidth*tileWidth-1)-tileWidth/2;
		}
		
		y += dy;
		if ( y <= tileHeight )
		{
			dy = dy + (tileHeight - y);
			y = tileHeight;
		}
		if ( y >= (mapHeight*tileHeight-1)-tileHeight/2 )
		{
			dy = dy - (y - ((mapHeight*tileHeight-1)-tileHeight/2));
			y = (mapHeight*tileHeight-1)-tileHeight/2;
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
