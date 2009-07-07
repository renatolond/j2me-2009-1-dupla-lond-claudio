import javax.microedition.lcdui.game.Sprite;


public class Enemy extends Entity
{
	int wayPoint;
	int delay;
	
	Enemy(Sprite s)
	{
		car = s;
		carAng = 0;
		wayPoint = 0;
		speed = 3;
		delay = 0;
	}
	
	void move(int x, int y)
	{
		car.move(x, y);
	}
	
	public void update()
	{
		double teta, teta2;
		double vy, vx;
		int ry, rx;
		ry = player.y + (car.getY()-player.car.getY());
		rx = player.x + (car.getX()-player.car.getX());
		vy = waypoint[wayPoint].y - ry;
		vx = waypoint[wayPoint].x - rx;
		if ( Math.abs(vx) < 6 && Math.abs(vy) < 6 )
		{
			wayPoint++;
			wayPoint %= waypoint.length;
			return;
		}
		//vy = vx = 0;
		//vx = 10;
		teta = mMath.atan2(vy, vx);
		if ( Math.abs(vy) <= 6 )
		{
			if ( vx < 0 )
				teta2 = -Math.PI/2;
			else
				teta2 = Math.PI/2;
		}
		else
		{
			double d = vx/vy;
			if ( Math.abs(vx/vy) < 1 )
			{
				teta2 = d/(1 + 0.28*d*d);
			}
			else
			{
				teta2 = Math.PI/2 - d/(d*d + 0.28);
			}
			//teta2 = mMath.atan(vx/vy);
			//teta2 = teta2;
			if ( vy < 0 )
				teta2 = teta2+Math.PI;
		
		}
		teta = teta2;
		
		int dx, dy;
		dx = dy = 1;
		if ( ++delay > 10 )
		{
			System.out.println("playerY: "+player.y+" enemyY:"+car.getY()+"playerCarY:" +player.car.getY());
			System.out.println("vx/vy: "+vx/vy+" vx "+vx+" vy "+vy+" teta: "+teta*180.0/Math.PI+" teta2:"+teta2);
			delay = 0;

		}
//		int ang = (int)//carAng;
//		ang += car.getFrame() * 45/2;
		if ( Math.abs(teta) < 0.001 )
			return;
		double dsin = Math.sin(teta)*speed;
		double dcos = Math.cos(teta)*speed;
		dx = (int)dsin;
		dy = (int)dcos;
		move(dx, dy);
	}
	
}
