import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;

public class PizzaShop extends MIDlet implements CommandListener
{
	private Display display;
	private Command comandoOKCadastro, comandoCancelCadastro, comandoOKSabores,
					 comandoCancelSabores, comandoOKTamanho, comandoCancelTamanho,
					 comandoOKQuantidade, comandoCancelQuantidade, comandoOKConfirma,
					 comandoCancelConfirma, comandoOKAdicionaPedido;					 
	private Form telaCadastro, telaSabor, telaTamanho, telaQuantidade, telaConfirmaPedido;
	private TextField textQtdComprada, textNomeCliente, textEndereco, textTelefone;
	private ChoiceGroup escolhaSabor, escolhaTamanho;
	private RecordStore dadosCliente;
	
	public PizzaShop()
	{

		display = Display.getDisplay(this);

		//tela de cadastro
		telaCadastro = new Form("Cadastro");
		
		comandoOKCadastro = new Command("Confirma", Command.OK, 1);
		telaCadastro.addCommand(comandoOKCadastro);
		
		comandoCancelCadastro = new Command("Sair", Command.CANCEL, 1);
		telaCadastro.addCommand(comandoCancelCadastro);
		
		textNomeCliente = new TextField("Nome: ","", 10, TextField.ANY);
		textEndereco = new TextField("Endereco: ", "", 10, TextField.ANY);
		textTelefone = new TextField("Telefone: ", "", 10, TextField.DECIMAL);
		telaCadastro.append(textNomeCliente);
		telaCadastro.append(textEndereco);
		telaCadastro.append(textTelefone);
		
		telaCadastro.setCommandListener(this);
		//----------------
		
		
		//escolha de sabores
		String[] sabores = {"Mussarela", "Calabresa", "Alho", "Foca com cheddar"};
		escolhaSabor = new ChoiceGroup("Sabor", ChoiceGroup.EXCLUSIVE, sabores, null);
		telaSabor = new Form("Escolha o Sabor");
		telaSabor.append(escolhaSabor);
		
		comandoOKSabores = new Command("Tamanho", Command.OK, 1);
		telaSabor.addCommand(comandoOKSabores);
		comandoCancelSabores = new Command("Sair", Command.CANCEL, 1);	
		telaSabor.addCommand(comandoCancelSabores);
		
		telaSabor.setCommandListener(this);
		//------------------
		
		//escolha de Tamanho
		String[] tamanhos = {"Pequena(R$10,00)", "Média(R$15,00)", "Grande(R$20,00)"};
		escolhaTamanho = new ChoiceGroup("Tamanho", ChoiceGroup.EXCLUSIVE, tamanhos, null);
		telaTamanho = new Form("Escolha o Tamanho");
		telaTamanho.append(escolhaTamanho);
		
		comandoOKTamanho = new Command("Quantidade", Command.OK, 1);
		telaTamanho.addCommand(comandoOKTamanho);
		comandoCancelTamanho = new Command("Sabores", Command.CANCEL, 1);
		telaTamanho.addCommand(comandoCancelTamanho);
		
		telaTamanho.setCommandListener(this);
		//------------------
		
		//Informa a Quantidade
		telaQuantidade = new Form("Quantidade");
		textQtdComprada = new TextField("Quantidade:", "", 3, TextField.NUMERIC);
		telaQuantidade.append(textQtdComprada);
		
		comandoOKQuantidade = new Command("Confirma", Command.OK, 1);
		telaQuantidade.addCommand(comandoOKQuantidade);
		comandoCancelQuantidade = new Command("Tamanho", Command.CANCEL, 1);
		telaQuantidade.addCommand(comandoCancelQuantidade);
		
		telaQuantidade.setCommandListener(this);
		//-------------------
		
		//Tela Confirmação
		
		telaConfirmaPedido = new Form("Confirmacao");		 
		comandoOKConfirma = new Command("Confirmar", Command.OK, 1);
		comandoOKAdicionaPedido = new Command("Adicionar", Command.OK, 1);
		telaConfirmaPedido.addCommand(comandoOKConfirma);
		telaConfirmaPedido.addCommand(comandoOKAdicionaPedido);
		comandoCancelConfirma = new Command("Quantidade", Command.CANCEL, 1);
		telaConfirmaPedido.addCommand(comandoCancelConfirma);
		
		telaConfirmaPedido.setCommandListener(this);
		//------------------ 
		
	}

	public void commandAction(Command c, Displayable d)
	{

		if(d == telaCadastro)
		{
			if(c == comandoOKCadastro)
			{
				display.setCurrent(telaSabor);
			}
			else if(c == comandoCancelCadastro)
			{
				notifyDestroyed();
			}
		}
		else if(d == telaSabor)
		{
			if(c == comandoOKSabores)
			{
				display.setCurrent(telaTamanho);
			}
			else if(c == comandoCancelSabores)
			{
				notifyDestroyed();
			}
		}
		else if(d == telaTamanho)
		{
			if(c == comandoOKTamanho)
			{
				display.setCurrent(telaQuantidade);
			}
			else if(c == comandoCancelTamanho)
			{
				display.setCurrent(telaSabor);
			}		
		}
		else if(d == telaQuantidade)
		{
			if(c == comandoOKQuantidade)
			{
				defineTelaConfirmacao();
				display.setCurrent(telaConfirmaPedido);
			}
			else if(c == comandoCancelQuantidade)
			{
				display.setCurrent(telaTamanho);
			}
		}
		else if(d == telaConfirmaPedido)
		{
			if(c == comandoOKConfirma)
			{
				
			}
			else if(c == comandoOKAdicionaPedido)
			{
				display.setCurrent(telaSabor);
			}
			else if(c == comandoCancelQuantidade)
			{
				display.setCurrent(telaTamanho);
			}
		}
		
	}

	void defineTelaConfirmacao()
	{
		String saborEscolhido = escolhaSabor.getString(escolhaSabor.getSelectedIndex());
		String tamanhoEscolhido = escolhaTamanho.getString(escolhaTamanho.getSelectedIndex());
		int quantidadeEscolhida = Integer.parseInt(textQtdComprada.getString());
		
		//StringItem saborComprado = new StringItem("Sabor: ", saborEscolhido);
		telaConfirmaPedido.append("Sabor: " + saborEscolhido);
		
		//StringItem tamanhoComprado = new StringItem("Tamanho: ", tamanhoEscolhido);
		telaConfirmaPedido.append("Tamanho: " + tamanhoEscolhido);
		
		//StringItem quantidadeComprada = new StringItem("Quantidade: ", textQtdComprada.getString());
		telaConfirmaPedido.append("Quantidade: " + textQtdComprada.getString());
		
		float precoTotal = calculaPreco();
		telaConfirmaPedido.append("Total: R$" + precoTotal);

	}
	
	float calculaPreco()
	{

		int[] precos = {10, 15, 20};
		
		float precoTotal = precos[escolhaTamanho.getSelectedIndex()] * Integer.parseInt(textQtdComprada.getString());
		return precoTotal;
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{

	}

	protected void pauseApp()
	{

	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(telaCadastro);
	}



}
