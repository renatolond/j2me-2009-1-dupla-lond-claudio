import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class PizzaMobile extends MIDlet implements CommandListener
{
	public class Registro
	{
		public int sabor;
		public int tamanho;
		public int quantidade;
		Registro ()
		{
			sabor = 0;
			tamanho = 0;
			quantidade = 0;
		}
	}

	private Vector pedido;
	private Registro temp;
	private String[] sabores = { "Muzzarela", "Presunto", "4 Queijos",
			"Portuguesa" };
	private String[] tamanhos = { "Pequena", "M�dia",
			"Grande" };
	private double[] precos = { 10.00, 15.00, 20.00 };
	private Display display;
	private Form formInicial, formTamanho, formQtde, formFechamento;
	private ChoiceGroup sabor, tamanho;
	private Command cmdCancel, cmdTamanho, cmdQtde, cmdOutroPedido,
			cmdFecharPedido, cmdOK, cmdVoltar;
	private TextField txtQtde;

	public PizzaMobile()
	{
		display = Display.getDisplay(this);
		pedido = new Vector();
		temp = new Registro();

		formInicial = new Form("Pizza Mobile");
		formTamanho = new Form("Pizza Mobile");
		formQtde = new Form("Pizza Mobile");
		formFechamento = new Form("Pizza Mobile");

		sabor = new ChoiceGroup("Escolha o sabor:", Choice.EXCLUSIVE);
		for (int i = 0; i < sabores.length; i++)
		{
			sabor.append(sabores[i], null);
		}
		cmdCancel = new Command("Cancelar", Command.CANCEL, 1);
		cmdTamanho = new Command("Tamanho", Command.OK, 1);
		formInicial.append(sabor);
		formInicial.setCommandListener(this);
		formInicial.addCommand(cmdCancel);
		formInicial.addCommand(cmdTamanho);

		tamanho = new ChoiceGroup("Escolha o tamanho:", Choice.EXCLUSIVE);
		for (int i = 0; i < tamanhos.length; i++)
		{
			tamanho.append(tamanhos[i] + " (R$" + precos[i] + ")", null);
		}
		cmdVoltar = new Command("Voltar", Command.CANCEL, 1);
		cmdQtde = new Command("Quantidade", Command.OK, 1);
		formTamanho.append(tamanho);
		formTamanho.setCommandListener(this);
		formTamanho.addCommand(cmdVoltar);
		formTamanho.addCommand(cmdQtde);
		
		txtQtde = new TextField("Quantidade:", "0", 3, TextField.NUMERIC);
		cmdOutroPedido = new Command("Fazer outro pedido", Command.OK, 1);
		cmdFecharPedido = new Command("Fechar o pedido", Command.OK, 2);
		formQtde.addCommand(cmdVoltar);
		formQtde.addCommand(cmdFecharPedido);
		formQtde.addCommand(cmdOutroPedido);
		formQtde.setCommandListener(this);
		formQtde.append(txtQtde);
		
		cmdOK = new Command("OK", Command.OK, 1);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub

	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(formInicial);
	}

	public void commandAction(Command c, Displayable d)
	{
		if ( d == formInicial )
		{
			if ( c == cmdCancel )
			{
				notifyDestroyed();
			}
			if ( c == cmdTamanho )
			{
				temp.sabor = sabor.getSelectedIndex();
				display.setCurrent(formTamanho);
			}
		}
		else if ( d == formTamanho )
		{
			if ( c == cmdVoltar )
			{
				sabor.setSelectedIndex(temp.sabor, true);
				display.setCurrent(formInicial);
			}
			if ( c == cmdQtde )
			{
				temp.tamanho = tamanho.getSelectedIndex();
				display.setCurrent(formQtde);
			}
		}
		else if ( d == formQtde )
		{
			if ( c == cmdVoltar )
			{
				display.setCurrent(formTamanho);
			}
			if ( c == cmdOutroPedido )
			{
			}
			if ( c == cmdFecharPedido )
			{
			}
		}

	}

}
