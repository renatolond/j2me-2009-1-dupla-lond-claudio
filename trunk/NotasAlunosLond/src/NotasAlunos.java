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

public class NotasAlunos extends MIDlet implements CommandListener
{
	Display display;
	Form formNotas;
	List listEscolha;
	Command cmdCancelar, cmdVoltar, cmdSalvar;
	TextField txtDisciplina, txtDRE, txtNota;
	String opcoes[] = { "Adicionando Notas", "Consultando Notas" };
	StringItem strErro;
	int i;

	public NotasAlunos()
	{
		display = Display.getDisplay(this);

		listEscolha = new List("Notas Mobile", List.IMPLICIT);
		listEscolha.setCommandListener(this);
		cmdCancelar = new Command("Cancelar", Command.CANCEL, 1);
		listEscolha.addCommand(cmdCancelar);
		for (i = 0; i < opcoes.length; i++)
		{
			listEscolha.append(opcoes[i], null);
		}

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
			if (c == List.SELECT_COMMAND)
				if (listEscolha.getString(listEscolha.getSelectedIndex()) == opcoes[0])
				{
					display.setCurrent(formNotas);
				}
			if (c == cmdCancelar)
			{
				try
				{
					destroyApp(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		if (d == formNotas)
		{
			if ( c == cmdVoltar )
			{
				display.setCurrent(listEscolha);
			}
			if ( c == cmdSalvar )
			{
				String msg = "";
				
				if ( txtDisciplina.getString().length() == 0 )
				{
					msg = "É preciso preencher o campo disciplina.\n";
				}
				if ( txtDRE.getString().length() == 0 )
				{
					msg += "É preciso preencher o campo DRE.\n";
				}
				if ( txtNota.getString().length() == 0 )
				{
					msg += "É preciso preencher o campo Nota.\n";
				}
				
				if ( msg == null )
				{
					strErro.setText("");
				}
				else
				{
					strErro.setText(msg);
				}
			}
		}
	}
}
