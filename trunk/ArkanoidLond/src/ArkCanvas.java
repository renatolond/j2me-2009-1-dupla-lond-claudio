import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;


public class ArkCanvas extends Canvas
{

	protected void paint(Graphics g)
	{
		int largura = getWidth();
		int altura = getHeight();
		
		g.setColor(0x0000FF);
		g.fillRect(0, 0, largura, altura);
		
		g.setColor(0x000000);
		for (int i = 0 ; i < largura ; i += 10)
		{
			g.drawLine(0, largura-i, i, 0);
		}
	}

}
