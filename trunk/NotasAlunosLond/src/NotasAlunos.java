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
import javax.microedition.rms.RecordStore;

public class NotasAlunos extends MIDlet implements CommandListener
{
	Display display;
	Form formNotas;
	List listEscolha, listNotas;
	Command cmdCancelar, cmdVoltar, cmdSalvar;
	TextField txtDisciplina, txtDRE, txtNota;
	String opcoes[] = { "Adicionando Notas", "Consultando Notas" };
	StringItem strErro;
	RecordStore rsAlunos;

	public NotasAlunos()
	{
		display = Display.getDisplay(this);

		AbreRMS();

		CriaListEscolha();
		CriaFormNotas();
		CriaListNotas();
	}

	private void CriaListNotas()
	{
		byte[] array = new byte[1024];
		listNotas = new List("Consultando notas", List.EXCLUSIVE);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
		DataInputStream dInputStream = new DataInputStream(inputStream);
		String registro;
		try
		{
			for (int i = 1; i <= rsAlunos.getNumRecords(); i++)
			{
				rsAlunos.getRecord(i, array, 0);
				registro = dInputStream.readUTF() + " "
						+ dInputStream.readInt() + " "
						+ dInputStream.readDouble();
				listNotas.append(registro, null);
				inputStream.reset();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		listNotas.setCommandListener(this);
		listNotas.addCommand(cmdVoltar);
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
		// TODO Auto-generated method stub

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
	}

	private void commandActionListNotas(Command c)
	{
		if ( c == cmdVoltar )
		{
			display.setCurrent(listEscolha);
		}
	}

	private void commandActionListEscolha(Command c)
	{
		if (c == List.SELECT_COMMAND)
		{
			if (listEscolha.getString(listEscolha.getSelectedIndex()) == opcoes[0])
				display.setCurrent(formNotas);
			if (listEscolha.getString(listEscolha.getSelectedIndex()) == opcoes[1])
				display.setCurrent(listNotas);
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
}
