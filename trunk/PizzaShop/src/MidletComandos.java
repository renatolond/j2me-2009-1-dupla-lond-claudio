import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MidletComandos extends MIDlet implements CommandListener
{
	private Display display;
	private Command comando1, comando2;
	private Form form;
	private TextBox box;

	public MidletComandos()
	{
		display = Display.getDisplay(this);
		comando1 = new Command("Form", Command.CANCEL, 1);
		comando2 = new Command("TexTBox", Command.OK, 1);
		form = new Form("Apresentando Comandos e TextBoxes");
		box = new TextBox("Digite o seu Nome", "", 10, TextField.ANY);
		form.addCommand(comando1);
		form.addCommand(comando2);
		form.setCommandListener(this);
		box.addCommand(comando1);
		box.addCommand(comando2);
		box.setCommandListener(this);
	}

	public void pauseApp()
	{
	}

	public void startApp()
	{
		display.setCurrent(form);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c == comando1)
		{
			display.setCurrent(form);
		}
		if (c == comando2)
		{
			display.setCurrent(box);
		}
	}

	public void destroyApp(boolean unconditional)
	{
	}
}
