import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class FormulaMIDlet extends MIDlet implements CommandListener
{
	private FormulaCanvas canvas;

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		canvas.stop();
	}

	protected void pauseApp()
	{
	}

	protected void startApp() throws MIDletStateChangeException
	{
		if (canvas == null) {
		      canvas = new FormulaCanvas(Display.getDisplay(this));
		      Command exitCommand = new Command("Exit", Command.EXIT, 0);
		      canvas.addCommand(exitCommand);
		      canvas.setCommandListener(this);
		    }
		    
		    // Start up the canvas
		    canvas.start();
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.getCommandType() == Command.EXIT) {
		      try
			{
				destroyApp(true);
			} catch (MIDletStateChangeException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      notifyDestroyed();
		    }
	}
}
