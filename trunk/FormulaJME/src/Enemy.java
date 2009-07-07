import javax.microedition.lcdui.game.Sprite;


public class Enemy extends Entity
{
	int wayPoint;
	
	Enemy(Sprite s)
	{
		car = s;
		carAng = 0;
		wayPoint = 0;
	}
	
	void move(int x, int y)
	{
		car.move(x, y);
	}
	
	public void update()
	{
		
	}
	
}
