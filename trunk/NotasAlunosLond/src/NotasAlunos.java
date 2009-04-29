import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
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
	Display display;
	Form formNotas, formFiltro;
	List listEscolha, listNotas;
	Command cmdCancelar, cmdVoltar, cmdSalvar, cmdOK;
	TextField txtDisciplina, txtDRE, txtNota, txtFiltroDRE;
	String opcoes[] = { "Adicionando Notas", "Consultando Notas" };
	StringItem strErro;
	RecordStore rsAlunos;
	Filtra filtra;

	public NotasAlunos()
	{
		display = Display.getDisplay(this);

		AbreRMS();

		CriaListEscolha();
		CriaFormNotas();
		CriaFormFiltro();
		CriaListNotas();
	}

	private void AbreRMS()
	{
		try
		{
			rsAlunos = RecordStore.openRecordStore("CadastroAlunos", true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void CriaListEscolha()
	{
		int i;
		listEscolha = new List("Notas Mobile", List.IMPLICIT);
		listEscolha.setCommandListener(this);
		cmdCancelar = new Command("Cancelar", Command.CANCEL, 1);
		listEscolha.addCommand(cmdCancelar);
		for (i = 0; i < opcoes.length; i++)
		{
			listEscolha.append(opcoes[i], null);
		}
	}

	private void CriaFormNotas()
	{
		formNotas = new Form("TesteFoca");
		formNotas.setCommandListener(this);
		cmdVoltar = new Command("Voltar", Command.CANCEL, 1);
		cmdSalvar = new Command("Salvar", Command.OK, 1);
		formNotas.addCommand(cmdVoltar);
		formNotas.addCommand(cmdSalvar);
		txtDisciplina = new TextField("Disciplina", null, 10, TextField.ANY);
		txtDRE = new TextField("DRE", null, 9, TextField.NUMERIC);
		txtNota = new TextField("Nota", null, 4, TextField.DECIMAL);
		formNotas.append(txtDisciplina);
		formNotas.append(txtDRE);
		formNotas.append(txtNota);
		strErro = new StringItem(null, null);
		formNotas.append(strErro);

	}

	private void CriaFormFiltro()
	{
		formFiltro = new Form("Filtrar DRE");
		formFiltro.setCommandListener(this);
		cmdOK = new Command("OK", Command.OK, 1);
		formFiltro.addCommand(cmdOK);
		formFiltro.addCommand(cmdVoltar);
		txtFiltroDRE = new TextField("DRE", null, 9, TextField.NUMERIC);
		formFiltro.append(txtFiltroDRE);
	}

	private void CriaListNotas()
	{
		listNotas = new List("Consultando notas", List.EXCLUSIVE);
		listNotas.setCommandListener(this);
		listNotas.addCommand(cmdVoltar);
	}

	private void fechaRMS()
	{
		try
		{
			rsAlunos.closeRecordStore();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException
	{
		fechaRMS();
		notifyDestroyed();
	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(listEscolha);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (d == listEscolha)
		{
			commandActionListEscolha(c);
		}
		if (d == formNotas)
		{
			commandActionFormNotas(c);
		}
		if (d == listNotas)
		{
			commandActionListNotas(c);
		}
		if (d == formFiltro)
		{
			commandActionFormFiltro(c);
		}
	}

	private void commandActionFormFiltro(Command c)
	{
		if (c == cmdVoltar)
		{
			display.setCurrent(listEscolha);
		}
		if (c == cmdOK)
		{
			filtra = new Filtra(txtFiltroDRE.getString());
			listNotasApresenta();
			display.setCurrent(listNotas);
		}
	}

	private void listNotasApresenta()
	{
		byte[] array = new byte[1024];
		String registro;
		listNotas.deleteAll();
		try
		{
			if (rsAlunos.getNumRecords() > 0)
			{
				RecordEnumeration pesquisa = rsAlunos.enumerateRecords(filtra,
						null, false);
				while (pesquisa.hasNextElement())
				{
					array = pesquisa.nextRecord();
					ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
					DataInputStream dInputStream = new DataInputStream(inputStream);
					registro = dInputStream.readUTF() + " "
							+ dInputStream.readInt() + " "
							+ dInputStream.readDouble();
					listNotas.append(registro, null);
					inputStream.reset();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void commandActionListNotas(Command c)
	{
		if (c == cmdVoltar)
		{
			display.setCurrent(formFiltro);
		}
	}

	private void commandActionListEscolha(Command c)
	{
		if (c == List.SELECT_COMMAND)
		{
			if (listEscolha.getString(listEscolha.getSelectedIndex()) == opcoes[0])
				display.setCurrent(formNotas);
			if (listEscolha.getString(listEscolha.getSelectedIndex()) == opcoes[1])
				display.setCurrent(formFiltro);
		}
		if (c == cmdCancelar)
		{
			try
			{
				destroyApp(true);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void commandActionFormNotas(Command c)
	{
		if (c == cmdVoltar)
		{
			display.setCurrent(listEscolha);
		}
		if (c == cmdSalvar)
		{
			String msg = ChecaCampos();

			if (msg.length() == 0)
			{
				SalvaRegistro();
			} else
			{
				strErro.setText(msg);
			}
		}
	}

	private String ChecaCampos()
	{
		String msg = "";

		if (txtDisciplina.getString().length() == 0)
		{
			msg += "É preciso preencher o campo disciplina.\n";
		}
		if (txtDRE.getString().length() == 0)
		{
			msg += "É preciso preencher o campo DRE.\n";
		}
		if (txtNota.getString().length() == 0)
		{
			msg += "É preciso preencher o campo Nota.\n";
		}
		return msg;
	}

	private void SalvaRegistro()
	{
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			DataOutputStream dOutputStream = new DataOutputStream(outputStream);
			dOutputStream.writeUTF(txtDisciplina.getString());
			dOutputStream.writeInt(Integer.parseInt(txtDRE.getString()));
			dOutputStream.writeDouble(Double.parseDouble(txtNota.getString()));
			dOutputStream.flush();

			byte[] array = outputStream.toByteArray();
			rsAlunos.addRecord(array, 0, array.length);
			dOutputStream.close();
			outputStream.close();
			strErro.setText("Informações Gravadas com Sucesso!");
			txtDRE.setString(null);
			txtNota.setString(null);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	class Filtra implements RecordFilter
	{
		private String criterio = null;

		public Filtra(String criterio)
		{
			this.criterio = criterio;
		}

		public boolean matches(byte[] candidate)
		{
			int dre;
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(candidate);
			DataInputStream dInputStream = new DataInputStream(inputStream);

			if (criterio.length() == 0) // Se não tem critério, não é pra filtrar nada.
				return true;
			
			try
			{
				dInputStream.readUTF(); // Lê o nome da disciplina;
				dre = dInputStream.readInt();
				if ( Integer.parseInt(criterio) == dre )
					return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return false;
		}
	}

}
