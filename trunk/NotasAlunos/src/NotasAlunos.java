import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class NotasAlunos extends MIDlet implements CommandListener
{
	int numeroAviso = -1;
	private Display display;
	private List telaInicial;
	private Command comandoCancelaInicio, comandoOKCadastro, comandoCancelCadastro; 
	private Form telaCadastroNotas, telaConsultaNotas;
	private TextField textDisciplina, textDRE, textNota;
	private StringItem aviso;
	private RecordStore dadosAlunos;
	private ByteArrayInputStream streamleBytes;
	private ByteArrayOutputStream streamEscreveBytes;
	private DataInputStream streamLeDados;
	private DataOutputStream streamEscreveDados;
	
	public NotasAlunos()
	{

		display = Display.getDisplay(this);
		
		//Tela Inicial
		
		String[] opcoes = {"Adicionando Notas", "Consultando Notas"};
		telaInicial = new List("Notas Mobile", List.IMPLICIT, opcoes, null);
		comandoCancelaInicio = new Command("Cancelar", Command.CANCEL, 1);
		telaInicial.addCommand(comandoCancelaInicio);
		telaInicial.setCommandListener(this);
		
		//--------------
		
		//Tela de Cadastro
		textDisciplina = new TextField("Disciplina: ", "", 10, TextField.ANY);
		textDRE = new TextField("DRE: ", "", 9, TextField.NUMERIC);
		textNota = new TextField("Nota: ", "", 4, TextField.DECIMAL);
		Item[] campos = {textDisciplina, textDRE, textNota};
		telaCadastroNotas = new Form("Cadastrando Notas", campos);
		
		comandoOKCadastro = new Command("Salvar", Command.OK, 1);
		comandoCancelCadastro = new Command("Voltar", Command.CANCEL, 1);
		telaCadastroNotas.addCommand(comandoOKCadastro);
		telaCadastroNotas.addCommand(comandoCancelCadastro);
		aviso = new StringItem("", "Todos os campos devem ser preenchidos");
		telaCadastroNotas.setCommandListener(this);
		//----------------

	}

	public void commandAction(Command c, Displayable d)
	{
		if(d == telaInicial)
		{
			if(c == comandoCancelaInicio)
			{
				notifyDestroyed();
			}
			else if( c == List.SELECT_COMMAND)
			{
				if(telaInicial.getSelectedIndex() == 0)
				{
					display.setCurrent(telaCadastroNotas);
				}
				else if(telaInicial.getSelectedIndex() == 1)
				{
					display.setCurrent(telaConsultaNotas);
				}
			}
			
		}
		else if(d == telaCadastroNotas)
		{
			
			if(c == comandoOKCadastro)
			{			
				
				if(textDisciplina.getString().equals("") || textDRE.getString().equals("") || textNota.getString().equals(""))
				{
					if(numeroAviso != -1)
					{
						telaCadastroNotas.delete(numeroAviso);
					}
					numeroAviso = telaCadastroNotas.append(aviso);
				}
				else
				{
					adicionaDados();
					telaCadastroNotas.delete(numeroAviso);
					textDisciplina.delete(0, textDisciplina.getString().length());
					textDRE.delete(0, textDRE.getString().length());
					textNota.delete(0, textNota.getString().length());
					aviso.setText("Nota salva com Sucesso");
					numeroAviso = telaCadastroNotas.append(aviso);

				}
			}
			else if(c == comandoCancelCadastro)
			{
				telaCadastroNotas.delete(numeroAviso);
				textDisciplina.delete(0, textDisciplina.getString().length());
				textDRE.delete(0, textDRE.getString().length());
				textNota.delete(0, textNota.getString().length());
				display.setCurrent(telaInicial);

			}
		}
		
	}

	void adicionaDados()
	{
		try
		{
			streamEscreveBytes = new ByteArrayOutputStream();
			streamEscreveDados = new DataOutputStream(streamEscreveBytes);
			
			streamEscreveDados.writeUTF(textDisciplina.getString());
			streamEscreveDados.writeInt(Integer.parseInt(textDRE.getString()));
			streamEscreveDados.writeDouble(Double.parseDouble(textNota.getString()));
			streamEscreveDados.flush();
			
			byte[] bytesAEscrever = streamEscreveBytes.toByteArray();
			dadosAlunos.addRecord(bytesAEscrever, 0, bytesAEscrever.length);
			streamEscreveBytes.close();
			streamEscreveDados.close();

			System.out.println(dadosAlunos);
			return;

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void listaNotas()
	{
		try
		{
			RecordEnumeration pedidos = dadosAlunos.enumerateRecords(null, null, false);
			
			while(pedidos.hasNextElement())
			{
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{

	}

	protected void pauseApp()
	{

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(telaInicial);
	}



}
