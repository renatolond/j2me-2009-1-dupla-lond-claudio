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
		double dsin = Math.sin(phi)*speed;
		double dcos = Math.cos(phi)*speed;
		dx = -(int)dsin;
		dy = (int)dcos;
		
		double dd = Math.sqrt(dx*dx + dy*dy);
		double vd = Math.sqrt(vx*vx + vy*vy);
		double dot = (vx/vd * dx/dd) + (vy/vd * dy/dd);
		double clock = (vx*dy) - (vy*dx);
		double gamma = mMath.acos(dot);
		
		if ( gamma >= Math.PI/4 )
		{
			if ( clock < 0 )
			{
				car.nextFrame();
				turnRight();
			}
			else
			{
				car.prevFrame();
				turnLeft();
			}
		}
		
		/*
		'calculate crossproduct to determine if the target vector is clockwise or anticlockwise
		'from the current vector: Note this is not a full crossproduct as we are only interested
		'the information of clockwiseness, not in a vector perpendicular to both v1 and v2
		
		w = (v1.X * v2.Y) - (v1.Y * v2.X)
		
		'Add or subtract to the current angle depending on the direction
		If w > 0 Then
		        CurrentAngle = CurrentAngle + (ElapsedTime * RotationalVelocity)
		Else
		        CurrentAngle = CurrentAngle - (ElapsedTime * RotationalVelocity)
		End If
		
		'Calculate dot product and thus distance (angle) between the two vectors
		dot = (v1.X * v2.X) + (v1.Y * v2.Y)
		theta = ArcCosine(dot)
		
		'If the vectors are really close togeather then just set them equal
		If theta < (ElapsedTime * RotationalVelocity) Then
		    CurrentAngle = TargetAngle
		End If 
		 */

//		double diff = phi - teta;
//		//if ( diff < 0.0001 ) diff += Math.PI*2;
//		double diff2 = teta - phi;
//		//if ( diff2 < 0.0001 ) diff2 += Math.PI*2;
//		
//		if ( Math.abs(diff) > Math.abs(diff2) )
//			diff = diff2;
		
//		if ( ++delay > 10 )
//		{
//			System.out.println("waypoint x:" + waypoint[wayPoint].x + "waypoint y:" + waypoint[wayPoint].y);
//			System.out.println("playerY: "+player.y+" enemyY:"+car.getY()+"playerCarY:" +player.car.getY());
//			System.out.println("vx/vy: "+vx/vy+" vx "+vx+" vy "+vy+" teta: "+teta*180.0/Math.PI);
//			System.out.println("teta: "+teta+" carAng: "+carAng+" ang: "+ang+" phi: "+phi+"diff: " + diff*180/Math.PI);
//			delay = 0;
//			if ( diff > Math.PI/4 )
//			{
//				car.nextFrame();
//				turnRight();
//			}
//			if ( diff < -Math.PI/4 )
//			{
//				car.prevFrame();
//				turnLeft();
//			}
//		}
		
		//else if ( phi - Math.PI/4 < teta )
//			turnLeft();
			
		move(dx, dy);
	}
	
}
