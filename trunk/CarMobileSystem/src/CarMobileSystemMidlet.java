import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
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
	private ByteArrayInputStream streamleBytes;
	private ByteArrayOutputStream streamEscreveBytes;
	private DataInputStream streamLeDados;
	private DataOutputStream streamEscreveDados;

	String smsConnection = "sms://:" + "5000";

	public CarMobileSystemMidlet() {
		display = Display.getDisplay(this);
		cmdSair = new Command("Sair", Command.CANCEL, 0);
		content = new Alert("SMS Receive");
		content.setTimeout(Alert.FOREVER);
		content.addCommand(cmdSair);
		content.setCommandListener(this);
		content.setString("Receiving...");
		try {
			PushRegistry.registerConnection(smsConnection, "CarMobileSystemMidlet", "*");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resumeScreen = content;
		
		// Iniciando a tela de senha
		telaSenha = new Form("Senha");
		
		comandoOK = new Command("Gravar", Command.OK, 0);
		telaSenha.addCommand(comandoOK);
		comandoCancel = new Command("Cancela", Command.OK, 0);
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
		// o programa está sendo rodado, pra pedir a senha e comparar a senha também
		
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
				adicionaDados();
			}
			else if(c == comandoCancel)
			{
				notifyDestroyed();
			}
		}
	}

	public void run() {
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

	private void FoiRoubado() {
		// TODO Botar o PushRegistry() pra timer e tal 
		
	}

	private String MinhaSenha() {
		
		abreArquivo();
		adicionaDados();
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
		try	{dadosSenha = RecordStore.openRecordStore("Dados", true, RecordStore.AUTHMODE_ANY, true);}
		
		catch(Exception e){	e.printStackTrace();}
	}
	
	
	void adicionaDados()
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
		return true;
	}
	


}
