package user_stories;

import java.awt.Dimension;
import java.sql.SQLException;

import model.Aluno;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.AlunoDAO;
import view.Main2;
import exception.ClienteException;

/**
 * US4 T�tulo: Alterar Aluno. Como aluno Eu quero alterar meus dados Para
 * que meu cadastro esteja sempre atualizado.
 * 
 * Cen�rio 1: N�o h� cadastro do aluno. Dado que o aluno n�o est�
 * cadastrado, Quando o usu�rio solicita a altera��o do cadastro, Ent�o o
 * sistema informa que o cadastro n�o existe.
 * 
 * Cen�rio 2: H� cadastro e o novos dados todos s�o v�lidos. Dado que h� o
 * cadastro do aluno, E todos os novos dados inseridos s�o v�lidos, Quando o
 * usu�rio solicita altera��o do cadastro do aluno, Ent�o o sistema altera
 * os dados, E informa que os dados foram alterados.
 * 
 * Cen�rio 3: H� cadastro e algum novo(s) dado(s) � (s�o) inv�lidos. Dado
 * que h� o cadastro do aluno, E algum novo dado inserido � inv�lido, Quando
 * o usu�rio solicita altera��o do cadastro do aluno, Ent�o o sistema deve
 * exibir a seguinte mensagem: �O campo [campo] � inv�lido�, E o sistema n�o
 * altera os dados.
 */

public class US04_AlterarAluno {
	private FrameFixture window;
	private Robot robot;
	private Aluno aluno;
	private DialogFixture dialog;
	private int index;
	
	@Before
	public void setUp() throws ClienteException, SQLException {
		robot = BasicRobot.robotWithNewAwtHierarchy();
		robot.settings().delayBetweenEvents(5);

		window = new FrameFixture(robot, new Main2());
		window.show(new Dimension(900, 500)); // shows the frame to test
		
		aluno = new Aluno("Teste", "658.535.144-40", "110038096","9211-2144", "teste incluir repetido");
		AlunoDAO.getInstance().include(aluno);
		
		index = AlunoDAO.getInstance().searchAllEquipaments().size() - 1;
		
		window.button("Aluno").click();
		dialog = window.dialog("AlunoView");
	}
	
	@After
	public void tearDown() throws SQLException, ClienteException {
		if(aluno != null)
			AlunoDAO.getInstance().delete(aluno);
		window.cleanUp();
	}

	public void sleep(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testCancelar() {
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		cadastro.button("Cancelar").click();
	}
	
	@Test
	public void testCenario1() throws SQLException, ClienteException{
		if(aluno != null)
			AlunoDAO.getInstance().delete(aluno);
		dialog.button("Alterar").click();
		dialog.optionPane().requireMessage("Selecione uma linha!");
		aluno = null;
	}
	
	@Test
	public void testCenario2() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("Nome").setText("Novo Teste");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("Aluno alterado com sucesso");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
	
	@Test
	public void testCenario3NomeInvalido() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("Nome").setText("123");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("Nome Invalido.");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
	
	@Test
	public void testCenario3NomeEmBranco() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("Nome").setText("");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("Nome em Branco.");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
	
	@Test
	public void testCenario3CpfInvalido() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("CPF").setText("123");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("CPF Invalido.");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
	
	@Test
	public void testCenario3CpfEmBranco() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("CPF").setText("");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("CPF em Branco.");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
	
	@Test
	public void testCenario3TelefeoneInvalido() throws SQLException, ClienteException{
		
		dialog.table("tabelaCliente").selectRows(index);
		dialog.button("Alterar").click();
		DialogFixture cadastro = dialog.dialog("AlterarAluno");
		
		cadastro.textBox("Telefone").setText("123");
		cadastro.textBox("E-mail").setText("Alteracao Teste automatizado");

		cadastro.button("Alterar").click();
		cadastro.optionPane().requireMessage("Telefone Invalido.");
		sleep();
		cadastro.optionPane().okButton().click();

		aluno = AlunoDAO.getInstance().searchAllEquipaments().get(index);
		sleep();
	}
}
