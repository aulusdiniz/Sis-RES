package test.model;

import static org.junit.Assert.*;

import model.Professor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.ClientException;

public class ProfessorTest {
	
	/**
	 *	Os teste da classe Cliente foram feitos aqui, por se tratar de uma classe
	 * abstrata, ela nao eh instaciavel, entao todas as suas funcionalidade foram
	 * testadas a partir das instancias da classe Professor.
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	
	@Test
	public void testInstance() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste de Instanciamento do Professor", p instanceof Professor);
	}
	
	@Test
	public void testNome() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste do Nome do Professor", "Nome" == p.getName());
	}

	@Test
	public void testCpf() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste do CPF do Professor", "868.563.327-34" == p.getCpf());
	}
	
	@Test
	public void testMatricula() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste da Matricula do Professor", "123456" == p.getRegistration());
	}
	
	@Test
	public void testTelefone() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste de Telefone do Professor", "1234-5678" == p.getPhone());
	}
	
	@Test
	public void testEmail() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		assertTrue("Teste do E-mail do Professor", "Nome@email" == p.getEmail());
	}

	
	
	@Test (expected= ClientException.class)
	public void testNomeVazio() throws ClientException {
		new Professor("", "868.563.327-34", "123456", "1234-5678", "Nome@email");
	}

	@Test (expected= ClientException.class)
	public void testNomeNumero() throws ClientException {
		new Professor("Nome1", "868.563.327-34", "123456", "1234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testNomeCaractere() throws ClientException {
		new Professor("Nome+", "868.563.327-34", "123456", "1234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testNomeNulo() throws ClientException {
		new Professor(null, "868.563.327-34", "123456", "1234-5678", "Nome@email");
	}

	
	
	@Test (expected= ClientException.class)
	public void testCpfVazio() throws ClientException {
		new Professor("Nome", "", "123456", "1234-5678", "Nome@email");
	}

	@Test (expected= ClientException.class)
	public void testCpfLetras() throws ClientException {
		new Professor("Nome", "868.563.327-3d", "123456", "1234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testCpfDespadronizado() throws ClientException {
		new Professor("Nome", "86856332734", "123456", "1234-5678", "Nome@email");
	}
	
	@Test (expected = ClientException.class)
	public void testCpfInvalido() throws ClientException {
		new Professor("Nome", "868.563.327-21", "123456", "1234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testCpfNulo() throws ClientException {
		new Professor("Nome", null, "123456", "1234-5678", "Nome@email");
	}
	
	
	
	@Test (expected= ClientException.class)
	public void testMatriculaVazia() throws ClientException {
		new Professor("Nome", "868.563.327-34", "", "1234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testMatriculaNula() throws ClientException {
		new Professor("Nome", "868.563.327-34", null, "1234-5678", "Nome@email");
	}
	
	
	
	@Test
	public void testTelefoneVazio() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		assertTrue("Teste de Telefone Vazio do Professor", "" == p.getPhone());
	}
	
	@Test (expected= ClientException.class)
	public void testTelefoneDespadronizado() throws ClientException {
		new Professor("Nome", "868.563.327-34", "123456", "(901234-5678", "Nome@email");
	}
	
	@Test (expected= ClientException.class)
	public void testTelefoneNulo() throws ClientException {
		new Professor("Nome", "868.563.327-34", "123456", null, "Nome@email");
	}

	
	
	@Test
	public void testEmailVazio() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "1234-5678", "");
		assertTrue("Teste de Email Vazio do Professor", "" == p.getEmail());
	}
	
	@Test (expected= ClientException.class)
	public void testEmailNulo() throws ClientException {
		new Professor("Nome", "868.563.327-34", "123456", "123456", null);
	}

	
	
	@Test
	public void testEqualsTrue() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		assertTrue("Teste do E-mail do Professor", p.equals(q));
	}
	
	@Test
	public void testEqualsFalseNome() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("NomeDiferente", "868.563.327-34", "12356", "(90) 1234-3344", "Nom@email");
		assertFalse("Teste do E-mail do Professor", p.equals(q));
	}
	@Test
	public void testEqualsFalseCpf() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("Nome", "338.688.964-65", "12356", "(90) 1234-3344", "Nom@email");
		assertFalse("Teste do E-mail do Professor", p.equals(q));
	}
	@Test
	public void testEqualsFalseMatricula() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("Nome", "868.563.327-34", "12356", "(90) 1234-3344", "Nom@email");
		assertFalse("Teste do E-mail do Professor", p.equals(q));
	}
	@Test
	public void testEqualsFalseTelefone() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("Nome", "868.563.327-34", "123456", "(90) 1234-3344", "Nom@email");
		assertFalse("Teste do E-mail do Professor", p.equals(q));
	}
	@Test
	public void testEqualsFalseEmail() throws ClientException {
		Professor p = new Professor("Nome", "868.563.327-34", "123456", "", "Nome@email");
		Professor q = new Professor("Nome", "868.563.327-34", "123456", "", "Nom@el");
		assertFalse("Teste do E-mail do Professor", p.equals(q));
	}
}
