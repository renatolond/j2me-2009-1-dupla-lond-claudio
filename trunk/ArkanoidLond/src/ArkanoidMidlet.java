import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class ArkanoidMidlet extends MIDlet implements CommandListener
{
	Display display;
	ArkCanvas canvas;
	private Command cmdVoltar;
	private Command cmdOk;

	public ArkanoidMidlet()
	{
		display = Display.getDisplay(this);
		canvas = new ArkCanvas();
		
		cmdVoltar = new Command("Cancelar", Command.CANCEL, 1);
		cmdOk = new Command("OK", Command.OK, 1);
		
		canvas.addCommand(cmdOk);
		canvas.addCommand(cmdVoltar);
		canvas.setCommandListener(this);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub

	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(canvas);
	}

	public void commandAction(Command c, Displayable d)
	{
		if ( d == canvas )
			commandActionCanvas(c);
	}

	private void commandActionCanvas(Command c)
	{
		if ( c == cmdVoltar )
			try
			{
				destroyApp(true);
			} catch (MIDletStateChangeException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if ( c == cmdOk )
		{
		}
	}

}
