import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
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
	private Command comandoOK, comandoCancel;
	private Form telaSenha;
	private TextField campoSenha;
	private RecordStore dadosSenha;
	private ByteArrayInputStream streamLeBytes;
	private ByteArrayOutputStream streamEscreveBytes;
	private DataInputStream streamLeDados;
	private DataOutputStream streamEscreveDados;
	private boolean stolen;

	String smsConnection = "sms://:" + "5000";

	public CarMobileSystemMidlet() {
		stolen = false;
		display = Display.getDisplay(this);
		cmdSair = new Command("Sair", Command.CANCEL, 0);
		content = new Alert("SMS Receive");
		content.setTimeout(Alert.FOREVER);
		content.addCommand(cmdSair);
		content.setCommandListener(this);
		content.setString("Receiving...");

		resumeScreen = content;
		
		// Iniciando a tela de senha
		telaSenha = new Form("Senha");
		
		comandoOK = new Command("Gravar", Command.OK, 0);
		telaSenha.addCommand(comandoOK);
		comandoCancel = new Command("Cancela", Command.OK, 0);
		campoSenha = new TextField("Campo Senha", "", 10, TextField.ANY);
		telaSenha.addCommand(comandoCancel);
		telaSenha.append(campoSenha);
		telaSenha.setCommandListener(this);
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		
		//Temos que colocar algum tipo de flag pra ver se é a primeira vez que
		
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
		
		//Cláudio
		if(d == telaSenha)
		{
			if(c == comandoOK)
			{
				abreArquivo();
				criaSenha();
			}
			else if(c == comandoCancel)
			{
				notifyDestroyed();
			}
		}
	}

	public void run() {
		if ( stolen )
		{
			stolenCell();
		}
		while (true) {
			try {
				msg = smsConn.receive();
				if (msg != null) {
					String myMsg =((TextMessage) msg).getPayloadText();
					if ( myMsg.equals(MinhaSenha()) )
					{
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

	private void stolenCell() {
		// TODO Auto-generated method stub
		
	}

	private void FoiRoubado() {
		// TODO Botar o PushRegistry() pra timer e tal
		String cn = this.getClass().getName();
		try {
			PushRegistry.registerAlarm(cn, 5*60);
		} catch (ConnectionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stolen = true;
	}

	private String MinhaSenha() {
		
		abreArquivo();
		criaSenha();
		return null;
	}

	public void notifyIncomingMessage(MessageConnection conn) {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	//Métodos adicionados pelo Cláudio
	
	void abreArquivo()
	{
		try	
		{
			if(RecordStore.listRecordStores() != null){RecordStore.deleteRecordStore("Senha");}

			dadosSenha = RecordStore.openRecordStore("Senha", true, RecordStore.AUTHMODE_ANY, true);
		}
		
		catch(Exception e){	e.printStackTrace();}
	}
	
	
	void criaSenha()
	{
		try
		{
			streamEscreveBytes = new ByteArrayOutputStream();
			streamEscreveDados = new DataOutputStream(streamEscreveBytes);
			streamEscreveDados.writeUTF(campoSenha.getString());
			streamEscreveDados.flush();
			
			byte[] bytesAEscrever = streamEscreveBytes.toByteArray();
			dadosSenha.addRecord(bytesAEscrever, 0, bytesAEscrever.length);
			streamEscreveBytes.close();
			streamEscreveDados.close();
			
			dadosSenha.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	boolean verificaSenha()  //Temos que comparar a senha do SMS com a do RecordStore
	{
		String senhaSMS = new String(); //Fazendo de conta que tenho a senha
		
		if(senhaSMS.equals(leSenha()))
		{
			return true;
		}
		
		return false;
	}
	
	String leSenha()
	{
		String senha = new String();
		try
		{
			abreArquivo();
			RecordEnumeration dados = dadosSenha.enumerateRecords(null, null, false);
			streamLeBytes = new ByteArrayInputStream(dados.nextRecord());
			streamLeDados = new DataInputStream(streamLeBytes);
			senha = streamLeDados.readUTF();
			streamLeBytes.close();
			streamLeDados.close();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return 	senha;
	}

}
