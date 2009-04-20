import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class NotasAlunos extends MIDlet implements CommandListener
{
	private int quantidadeEscolhida;
	private Display display;
	private List telaInicial;
	private Command comandoCancelaInicio, comandoOKCadastro, comandoCancelCadastro; 
	private Form telaCadastroNotas, telaConsultaNotas;
	private TextField textDisciplina, textDRE, textNota;
	private ChoiceGroup escolhaSabor, escolhaTamanho;
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
			String aviso = "Todos os dados devem ser preenchidos";
			
			if(c == comandoOKCadastro)
			{
				
				//adicionaDados();
				if(textDisciplina.getString().equals("") || textDRE.getString().equals("") || textNota.getString().equals(""))
				{
					telaCadastroNotas.append(aviso);
					textDisciplina.delete(0, textDisciplina.getString().length());
				}
				//display.setCurrent(telaCadastroNotas);
			}
			else if(c == comandoCancelCadastro)
			{
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
