import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Date;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
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
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

//import com.sun.perseus.model.Set;

public class CarMobileSystemMidlet extends MIDlet implements CommandListener,
		Runnable, MessageListener {
	Display display;
	Alert content;
	Command cmdSair;
	Displayable resumeScreen;
	private MessageConnection smsConn;
	private Message msg;
	private Thread thread;
	private Command comandoOK, comandoCancel;
	private Form telaSenha;
	private TextField campoSenha;
	private RecordStore dadosSenha;
	private ByteArrayInputStream streamLeBytes;
	private ByteArrayOutputStream streamEscreveBytes;
	private DataInputStream streamLeDados;
	private DataOutputStream streamEscreveDados;
	
	static final String nomeRecordStore = "Senha";
	static final int minutos = 15;
	static final int numVezes = 5;

	String smsConnection = "sms://:" + "5000";
	private Form checaSenha;
	private TextField campoVerificaSenha;
	private StringItem sItemErro;

	public CarMobileSystemMidlet() {
		display = Display.getDisplay(this);
		cmdSair = new Command("Sair", Command.CANCEL, 0);
		content = new Alert("SMS Receive");
		content.setTimeout(Alert.FOREVER);
		content.addCommand(cmdSair);
		content.setCommandListener(this);
		content.setString("Receiving...");

		resumeScreen = content;

		criaTelaSenha();
		criaChecaSenha();
		
		if ( senhaCarregada() )
		{
			resumeScreen = checaSenha;
		}
		else
		{
			resumeScreen = telaSenha;
		}
	}

	private void criaTelaSenha() {
		// Iniciando a tela de senha
		telaSenha = new Form("Senha");

		comandoOK = new Command("Gravar", Command.OK, 0);
		telaSenha.addCommand(comandoOK);
		comandoCancel = new Command("Cancela", Command.CANCEL, 0);
		campoSenha = new TextField("Senha", "", 10, TextField.PASSWORD);
		telaSenha.addCommand(comandoCancel);
		telaSenha.append(campoSenha);
		telaSenha.setCommandListener(this);
	}
	
	private void criaChecaSenha() {
		// Iniciando a tela de senha
		checaSenha = new Form("Troca Senha");
		sItemErro = new StringItem(null, null);

		checaSenha.addCommand(comandoOK);
		campoVerificaSenha = new TextField("Digite sua Senha", "", 10, TextField.PASSWORD);
		checaSenha.addCommand(comandoCancel);
		checaSenha.append(campoVerificaSenha);
		checaSenha.append(sItemErro);
		checaSenha.setCommandListener(this);
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {

		if (smsConn == null) {
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
		if (c == cmdSair || c == Alert.DISMISS_COMMAND) {
			try {
				destroyApp(false);
				notifyDestroyed();
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (d == telaSenha)
			commandActionTelaSenha(c);
		if (d == checaSenha)
			commandActionChecaSenha(c);
	}

	private void commandActionChecaSenha(Command c) {
		if (c == comandoOK) {
			if ( verificaSenha(campoVerificaSenha.getString()))
			{
				resumeScreen = telaSenha;
				display.setCurrent(resumeScreen);
			}
			else
			{
				sItemErro.setText("Senha incorreta!");
			}
		} else if (c == comandoCancel) {
			notifyDestroyed();
		}
	}

	private void commandActionTelaSenha(Command c) {
		if (c == comandoOK) {
			abreArquivo();
			criaSenha();
			fechaArquivo();
			notifyDestroyed();
		} else if (c == comandoCancel) {
			notifyDestroyed();
		}
	}

	public void run() {
			while (true) {
				try {
					msg = smsConn.receive();
					if (msg != null) {
						String myMsg = ((TextMessage) msg).getPayloadText();
						if (verificaSenha(myMsg)) {
							FoiRoubado();
						}
						msg = null;
					}
				} catch (InterruptedIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	/*
	public class StolenMidlet extends MIDlet implements Runnable
	{

		protected void destroyApp(boolean unconditional)
				throws MIDletStateChangeException {
			// TODO Auto-generated method stub
			
		}

		protected void pauseApp() {
			// TODO Auto-generated method stub
			
		}

		protected void startApp() throws MIDletStateChangeException {
			// TODO Auto-generated method stub
			
		}

		public void run() {
			String cn = (new StolenMidlet()).getClass().getName();
			Date nextWakeUp = new Date();
			nextWakeUp.setTime(nextWakeUp.getTime() + minutos*1000);
			try {
				stolenCell();
				PushRegistry.registerAlarm(cn, nextWakeUp.getTime());
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
			notifyDestroyed();
		}
	}*/

	private void stolenCell() {
		resumeScreen = content;
		display.setCurrent(content);
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
		notifyDestroyed();
	}
	private void FoiRoubado() {
		String cn = "StolenAlarmMidlet";
		stolenCell();
		Date nextWakeUp = new Date();
		nextWakeUp.setTime(nextWakeUp.getTime() + minutos*1000);
		try {
			PushRegistry.registerAlarm(cn, nextWakeUp.getTime());
		} catch (ConnectionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyIncomingMessage(MessageConnection conn) {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private boolean senhaCarregada ()
	{
		if ( RecordStore.listRecordStores() != null )
		{
			String[] s;
			s = RecordStore.listRecordStores();
			for ( int i = 0 ; i < s.length ; i++ )
			{
				if ( s[i].equals(nomeRecordStore) )
					return true;
			}
		}
		
		return false;
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

	private void criaSenha() {
		try {
			streamEscreveBytes = new ByteArrayOutputStream();
			streamEscreveDados = new DataOutputStream(streamEscreveBytes);
			streamEscreveDados.writeUTF(campoSenha.getString());
			streamEscreveDados.flush();

			byte[] bytesAEscrever = streamEscreveBytes.toByteArray();
			dadosSenha.addRecord(bytesAEscrever, 0, bytesAEscrever.length);
			streamEscreveBytes.close();
			streamEscreveDados.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean verificaSenha(String myMsg) 
	{
		String senha = leSenha();

		if (myMsg.equals(senha))
			return true;
		
		return false;
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

}
