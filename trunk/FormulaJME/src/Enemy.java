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
		//vy = vx = 0;
		//vx = 10;
		teta = mMath.atan2(vy, vx);
		if ( vy == 0 )
		{
			if ( vx < 0 )
				teta2 = -90;
			else
				teta2 = 90;
		}
		else
		{
			teta2 = mMath.atan(vx/vy);
			teta2 = (teta2*180.0)/Math.PI;
			if ( vy < 0 )
				teta2 = teta2+ 180;
		
		}
		if ( teta2 < 0 ) teta2 += 360;
		
		int dx, dy;
		dx = dy = 1;
		if ( ++delay > 10 )
		{
			System.out.println("playerY: "+player.y+" enemyY:"+car.getY()+"playerCarY:" +player.car.getY());
			System.out.println("ry: "+ry+" vx "+vx+" vy "+vy+" teta: "+teta*180.0/Math.PI+" teta2:"+teta2);
			delay = 0;

		}
//		int ang = (int)//carAng;
//		ang += car.getFrame() * 45/2;
		if ( Math.abs(teta) < 0.001 )
			return;
		double dsin = Math.sin(teta)*speed;
		double dcos = Math.cos(teta)*speed;
		dx = -(int)dcos;
		dy = (int)dsin;
		move(dx, dy);
	}
	
}
