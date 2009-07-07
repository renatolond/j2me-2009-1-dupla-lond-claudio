import javax.microedition.lcdui.game.Sprite;


public class Enemy extends Entity
{
	int wayPoint;
	int delay;
	
	Enemy(Sprite s)
	{
		car = s;
		carAng = 180;
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
		double teta;
		double vy, vx;
		double dist;
		int ry, rx;
		ry = player.y + (car.getY()-player.car.getY());
		rx = player.x + (car.getX()-player.car.getX());
		vy = waypoint[wayPoint].y - ry;
		vx = waypoint[wayPoint].x - rx;
		
		dist = (vx*vx) + (vy*vy);
		dist = Math.sqrt(dist);
		if ( dist <= car.getHeight()/2 )
		{
			wayPoint++;
			wayPoint %= 4;
			return;
		}
		//vy = vx = 0;
		//vx = 10;
		teta = mMath.atan2(vy, vx);
		teta = aTan2(vy, vx);
		
		int dx, dy;
		int ang = (int)carAng;
		double phi;
		ang += car.getFrame() * 45/2;
		phi = ang*Math.PI/180.0;
		if ( phi < 0 )
			phi += 2*Math.PI;
		dx = dy = 1;
		double diff = phi - teta;
		//if ( diff < 0.0001 ) diff += Math.PI*2;
		double diff2 = teta - phi;
		//if ( diff2 < 0.0001 ) diff2 += Math.PI*2;
		
		if ( Math.abs(diff) > Math.abs(diff2) )
			diff = diff2;
		
		if ( ++delay > 10 )
		{
//			System.out.println("waypoint x:" + waypoint[wayPoint].x + "waypoint y:" + waypoint[wayPoint].y);
//			System.out.println("playerY: "+player.y+" enemyY:"+car.getY()+"playerCarY:" +player.car.getY());
//			System.out.println("vx/vy: "+vx/vy+" vx "+vx+" vy "+vy+" teta: "+teta*180.0/Math.PI);
			System.out.println("teta: "+teta+" carAng: "+carAng+" ang: "+ang+" phi: "+phi+"diff: " + diff*180/Math.PI);
			delay = 0;
			if ( diff > Math.PI/4 )
			{
				car.nextFrame();
				turnRight();
			}
			if ( diff < -Math.PI/4 )
			{
				car.prevFrame();
				turnLeft();
			}
		}
		
		//else if ( phi - Math.PI/4 < teta )
//			turnLeft();
			

		double dsin = Math.sin(phi)*speed;
		double dcos = Math.cos(phi)*speed;
		dx = -(int)dsin;
		dy = (int)dcos;

		move(dx, dy);
	}
	
}
