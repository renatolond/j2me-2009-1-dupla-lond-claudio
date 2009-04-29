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
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;

public class NotasAlunos extends MIDlet implements CommandListener
{
	int numeroAviso = -1;
	private Display display;
	private List telaInicial;
	private Command comandoCancelaInicio, comandoOKCadastro, comandoCancelCadastro,
			 comandoOKFiltro, comandoCancelFiltro, comandoCancelListagem; 
	private Form telaCadastroNotas, telaFiltro, telaListagem;
	private TextField textDisciplina, textDRE, textNota, textDREFiltro;
	private StringItem aviso, nota, disciplina, dre;
	private RecordStore dadosAlunos;
	private ByteArrayInputStream streamLeBytes, streamLeBytesFiltro;
	private ByteArrayOutputStream streamEscreveBytes;
	private DataInputStream streamLeDados, streamLeDadosFiltro;
	public String dreRegistro, dreFiltro, lixo;
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

		//Tela de Filtro
		telaFiltro = new Form("DRE");
		textDREFiltro = new TextField("DRE: ", "", 9, TextField.NUMERIC);
		telaFiltro.append(textDREFiltro);
		
		comandoOKFiltro = new Command("Ver", Command.OK, 1);
		comandoCancelFiltro = new Command("Voltar", Command.CANCEL, 1);
		
		telaFiltro.addCommand(comandoOKFiltro);
		telaFiltro.addCommand(comandoCancelFiltro);
		telaFiltro.setCommandListener(this);
		//----------------
		
		//Tela de Listagem
		telaListagem = new Form("Notas");
		
		comandoCancelListagem = new Command("Voltar", Command.CANCEL, 1);
		telaListagem.addCommand(comandoCancelListagem);
		telaListagem.setCommandListener(this);		
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
					display.setCurrent(telaFiltro);
				}
			}
			
		}
		else if(d == telaCadastroNotas)
		{
			
			if(c == comandoOKCadastro)
			{			
				aviso = new StringItem("", "Todos os campos devem ser preenchidos");
				if(textDisciplina.getString().equals("") || textDRE.getString().equals("") || textNota.getString().equals(""))
				{
					if(numeroAviso != -1)
					{
						telaCadastroNotas.delete(numeroAviso);
						numeroAviso = -1;
					}
					numeroAviso = telaCadastroNotas.append(aviso);
				}
				else
				{
					adicionaDados();
					if(numeroAviso != -1){telaCadastroNotas.delete(numeroAviso);}
					textDisciplina.delete(0, textDisciplina.getString().length());
					textDRE.delete(0, textDRE.getString().length());
					textNota.delete(0, textNota.getString().length());
					aviso.setText("Nota salva com Sucesso");
					numeroAviso = telaCadastroNotas.append(aviso);
					display.setCurrent(telaCadastroNotas);

				}
			}
			else if(c == comandoCancelCadastro)
			{
				if(numeroAviso != -1)
				{	
					telaCadastroNotas.delete(numeroAviso);
					numeroAviso = -1;
				}
				textDisciplina.delete(0, textDisciplina.getString().length());
				textDRE.delete(0, textDRE.getString().length());
				textNota.delete(0, textNota.getString().length());
				display.setCurrent(telaInicial);

			}
		}
		else if(d == telaFiltro)
		{
			if(c == comandoOKFiltro)
			{
				listaNotas();
				display.setCurrent(telaListagem);
			}
			else if(c == comandoCancelFiltro)
			{
				display.setCurrent(telaInicial);
			}
		}
		
		else if(d == telaListagem)
		{
			if(c == comandoCancelListagem)
			{
				display.setCurrent(telaFiltro);
			}
		}
		
	}

	void adicionaDados()
	{
		try
		{			
			abreArquivo();
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
			fechaArquivo();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void listaNotas()
	{
		if(telaListagem.size() != 0){telaListagem.deleteAll();}
		Filtra filtra = new Filtra(textDREFiltro.getString());
		
		try
		{
			abreArquivo();
			RecordEnumeration dados = dadosAlunos.enumerateRecords(filtra, null, false);
			
			while(dados.hasNextElement())
			{
				
				streamLeBytes = new ByteArrayInputStream(dados.nextRecord());
				streamLeDados = new DataInputStream(streamLeBytes);
				
				disciplina = new StringItem("Disciplina: ",streamLeDados.readUTF()); 
				dre = new StringItem("DRE: " , Integer.toString(streamLeDados.readInt()));
				nota = new StringItem("Nota: ", Float.toString(streamLeDados.readFloat()));
				
				telaListagem.append(disciplina);
				telaListagem.append(dre);
				telaListagem.append(nota);

				streamLeBytes.close();
				streamLeDados.close();
				
			}
			
			fechaArquivo();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	class Filtra implements RecordFilter
	{

		
		public String criterio = null;
		
		public Filtra(String criterio)
		{
			dreFiltro = criterio;
		}
		public boolean matches(byte[] registroLido)
		{
			streamLeBytesFiltro = new ByteArrayInputStream(registroLido);
			streamLeDadosFiltro = new DataInputStream(streamLeBytesFiltro);

			try
			{
				lixo = streamLeDadosFiltro.readUTF();
				dreRegistro = Integer.toString(streamLeDadosFiltro.readInt());

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(dreRegistro.equals(dreFiltro) || dreFiltro.equals(""))
			{
				return true;
			}
			else
			{
				return false;
			}
			
			
		}		
		
	}
	void abreArquivo()
	{
		try
		{
			dadosAlunos = RecordStore.openRecordStore("CadastroAlunos", true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	void fechaArquivo()
	{
		try
		{
			dadosAlunos.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		notifyDestroyed();
	}

	protected void pauseApp()
	{

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(telaInicial);
	}



}
