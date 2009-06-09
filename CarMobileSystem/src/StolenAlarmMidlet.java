import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.util.Date;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

import com.sun.cldc.io.ConsoleOutputStream;

/***
 * Trabalho CarSystemMobile
 * Cláudio Sérgio Forain Júnior DRE: 105049864
 * Renato dos Santos Cerqueira DRE: 105093538
 */

public class StolenAlarmMidlet extends MIDlet implements Runnable, CommandListener, MessageListener {
	Display display;
	Alert content;
	Command cmdSair;
	Displayable resumeScreen;
	private MessageConnection smsConn;
	private Message msg;
	private Thread thread;
	private RecordStore dadosSenha;
	private ByteArrayInputStream streamLeBytes;
	private DataInputStream streamLeDados;
	
	static final String nomeRecordStore = "Senha";
	static final int minutos = 5;
	static final int numVezes = 60;

	String smsConnection = "sms://:" + "5000";

	public StolenAlarmMidlet() {
		display = Display.getDisplay(this);
		cmdSair = new Command("Sair", Command.CANCEL, 0);
		content = new Alert("SMS Receive");
		content.setTimeout(Alert.FOREVER);
		content.addCommand(cmdSair);
		content.setCommandListener(this);
		content.setString("Receiving...");
		resumeScreen = content;
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		if ( leSenha() == "" )
			notifyDestroyed();
		display.setCurrent(resumeScreen);
		FoiRoubado();
		thread = null;
		notifyDestroyed();
	}

	public void run() {
			display.setCurrent(resumeScreen);
			FoiRoubado();
			thread = null;
			notifyDestroyed();
	}
	
	private void abreArquivo() {
		try {
			dadosSenha = RecordStore.openRecordStore(nomeRecordStore, true,
					RecordStore.AUTHMODE_ANY, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fechaArquivo() {
		try
		{
			dadosSenha.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String leSenha() {
		String senha = new String();
		try {
			abreArquivo();
			RecordEnumeration dados = dadosSenha.enumerateRecords(null, null,
					false);
			streamLeBytes = new ByteArrayInputStream(dados.nextRecord());
			streamLeDados = new DataInputStream(streamLeBytes);
			senha = streamLeDados.readUTF();
			streamLeBytes.close();
			streamLeDados.close();
			fechaArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return senha;
	}

	private boolean verificaSenha(String myMsg) 
	{
		String senha = leSenha();

		if (myMsg.equals(senha))
			return true;
		
		return false;
	}
	
	private void FoiRoubado() {
		String cn = this.getClass().getName();
		stolenCell();
//		Date nextWakeUp = new Date();
//		nextWakeUp.setTime(nextWakeUp.getTime() + minutos*60*1000);
		try {
			PushRegistry.registerAlarm(cn, (new Date()).getTime() + minutos*60*1000);
		} catch (ConnectionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void stolenCell() {
		resumeScreen = content;
		display.setCurrent(content);
		ConsoleOutputStream c = new ConsoleOutputStream();
		String s = "Stolen!!";
		try {
			c.write( s.getBytes() );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int vezes = 0;
		
		while ( vezes < numVezes )
		{
			display.vibrate(1000);
			display.flashBacklight(1000);
			content.setString("Celular Roubado!!!");
			try {
				Thread.sleep(999);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vezes++;
		}
	}

	public void commandAction(Command c, Displayable d) {
		if (c == cmdSair || c == Alert.DISMISS_COMMAND) {
			try {
				destroyApp(false);
				notifyDestroyed();
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void notifyIncomingMessage(MessageConnection conn) {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
}
