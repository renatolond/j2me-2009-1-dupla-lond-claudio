import java.io.IOException;
import java.io.InterruptedIOException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

public class CarMobileSystemMidlet extends MIDlet implements CommandListener,
		Runnable, MessageListener {
	Display display;
	Alert content;
	Command cmdSair;
	Displayable resumeScreen;
	private MessageConnection smsConn;
	private Message msg;
	private Thread thread;

	public CarMobileSystemMidlet() {
		display = Display.getDisplay(this);
		cmdSair = new Command("Sair", Command.CANCEL, 0);
		content = new Alert("SMS Receive");
		content.setTimeout(Alert.FOREVER);
		content.addCommand(cmdSair);
		content.setCommandListener(this);
		content.setString("Receiving...");
		resumeScreen = content;
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		String smsConnection = "sms://:" + "5000";
		if ( smsConn == null )
		{
			try {
				smsConn = (MessageConnection) Connector.open(smsConnection);
				smsConn.setMessageListener(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		display.setCurrent(resumeScreen);
	}

	public void commandAction(Command c, Displayable d) {
		if ( c == cmdSair || c == Alert.DISMISS_COMMAND )
		{
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			msg = smsConn.receive();
			if ( msg != null )
			{
				if ( msg instanceof TextMessage )
				{
					content.setString(((TextMessage)msg).getPayloadText());
				}
				display.setCurrent(content);
			}
		} catch (InterruptedIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void notifyIncomingMessage(MessageConnection conn) {
		if ( thread == null )
		{
			thread = new Thread(this);
			thread.start();
		}
	}

}
