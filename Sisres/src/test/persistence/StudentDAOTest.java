package test.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Student;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import exception.ClientException;

import persistence.StudentDAO;
import persistence.FactoryConnection;

public class StudentDAOTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	
	@Test
	public void testInstance() {
		assertTrue("Instanciando StudentDAO", StudentDAO.getInstance() instanceof StudentDAO);
	}
	
	@Test
	public void testSingleton() {
		StudentDAO p = StudentDAO.getInstance();
		StudentDAO q = StudentDAO.getInstance();
		assertSame("Testando o Padrao Singleton", p, q);
	}
	

	@Test
	public void testIncluir() throws ClientException, SQLException {
		boolean resultado = false;
		Student aluno = new Student("Incluindo", "040.757.021-70", "098765", "9999-9999", "aluno@email");
		StudentDAO.getInstance().include(aluno);
		
		resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
		"aluno.nome = \"" + aluno.getName() + "\" and " +
		"aluno.cpf = \"" + aluno.getCpf() + "\" and " +
		"aluno.telefone = \"" + aluno.getPhone() + "\" and " +
		"aluno.email = \"" + aluno.getEmail() + "\" and " +
		"aluno.matricula = \"" + aluno.getRegistration() + "\";");
		
		if(resultado){
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno.getName() + "\" and " +
					"aluno.cpf = \"" + aluno.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno.getPhone() + "\" and " +
					"aluno.email = \"" + aluno.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno.getRegistration() + "\";");
		}
		assertTrue("Teste de Inclusão.", resultado);
	}
	
	@Test (expected= ClientException.class)
	public void testIncluirNulo() throws ClientException, SQLException {
		StudentDAO.getInstance().include(null);
	}
	
	@Test (expected= ClientException.class)
	public void testIncluirComMesmoCpf() throws ClientException, SQLException {
		boolean resultado = true;
		Student aluno = new Student("Incluindo", "040.757.021-70", "098765", "1111-1111", "aluno@email");
		Student aluno2 = new Student("Incluindo CPF Igual", "040.747.021-70", "987654", "2222-2222", "aluno2@email");
		StudentDAO.getInstance().include(aluno);
		try{
			StudentDAO.getInstance().include(aluno2);
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno.getName() + "\" and " +
					"aluno.cpf = \"" + aluno.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno.getPhone() + "\" and " +
					"aluno.email = \"" + aluno.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testIncluirComMesmaRegistration() throws ClientException, SQLException {
		boolean resultado = true;
		Student aluno = new Student("Incluindo", "040.757.021-70", "111111", "1111-1111", "aluno@email");
		Student aluno2 = new Student("Incluindo Registration Igual", "490.491.781-20", "111111", "2222-2222", "aluno2@email");
		StudentDAO.getInstance().include(aluno);
		try{
			StudentDAO.getInstance().include(aluno2);
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno.getName() + "\" and " +
					"aluno.cpf = \"" + aluno.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno.getPhone() + "\" and " +
					"aluno.email = \"" + aluno.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testIncluirJaExistente() throws ClientException, SQLException {
		boolean resultado = true;
		Student aluno = new Student("Incluindo", "040.757.021-70", "58801", "3333-3333", "aluno@email");
		Student aluno2 = new Student("Incluindo", "040.757.021-70", "58801", "3333-3333", "aluno@email");
		StudentDAO.getInstance().include(aluno);
		try{
			StudentDAO.getInstance().include(aluno2);
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + aluno.getName() + "\" and " +
					"aluno.cpf = \"" + aluno.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno.getPhone() + "\" and " +
					"aluno.email = \"" + aluno.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
					"aluno.nome = \"" + aluno2.getName() + "\" and " +
					"aluno.cpf = \"" + aluno2.getCpf() + "\" and " +
					"aluno.telefone = \"" + aluno2.getPhone() + "\" and " +
					"aluno.email = \"" + aluno2.getEmail() + "\" and " +
					"aluno.matricula = \"" + aluno2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	
	
	
	@Test
	public void testAlterar() throws ClientException, SQLException {
		boolean resultado = false;
		Student a = new Student("Incluindo", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Student an = new Student("Alterando", "387.807.647-97", "098765", "(123)4567-8899", "email@Nome");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		StudentDAO.getInstance().alterate(a, an);
		
		resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + an.getName() + "\" and " +
				"aluno.cpf = \"" + an.getCpf() + "\" and " +
				"aluno.telefone = \"" + an.getPhone() + "\" and " +
				"aluno.email = \"" + an.getEmail() + "\" and " +
				"aluno.matricula = \"" + an.getRegistration() + "\";");
		boolean resultado2 =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + an.getName() + "\" and " +
					"aluno.cpf = \"" + an.getCpf() + "\" and " +
					"aluno.telefone = \"" + an.getPhone() + "\" and " +
					"aluno.email = \"" + an.getEmail() + "\" and " +
					"aluno.matricula = \"" + an.getRegistration() + "\";");
		if(resultado2)
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", resultado == true && resultado2 == false);
	}
	
	@Test (expected= ClientException.class)
	public void testAlterarPrimeiroArgNulo() throws ClientException, SQLException {
		Student an = new Student("Alterando", "00.757.021-70", "123456", "(999)9999-9999", "aluno@email");
		StudentDAO.getInstance().alterate(null, an);
	}
	
	@Test (expected= ClientException.class)
	public void testAlterarSegundoArgNulo() throws ClientException, SQLException {
		Student an = new Student("Alterando", "00.757.021-70", "123456", "(999)9999-9999", "aluno@email");
		StudentDAO.getInstance().alterate(an, null);
	}
	@Test (expected= ClientException.class)
	public void testAlterarNaoExistente() throws ClientException, SQLException {
		boolean resultado = true;
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "1111-1111", "aluno@email");
		Student an = new Student("Alterando", "490.491.781-20", "098765", "(999)9999-9999", "email@aluno");
		
		try{
			StudentDAO.getInstance().alterate(a, an);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + an.getName() + "\" and " +
				"aluno.cpf = \"" + an.getCpf() + "\" and " +
				"aluno.telefone = \"" + an.getPhone() + "\" and " +
				"aluno.email = \"" + an.getEmail() + "\" and " +
				"aluno.matricula = \"" + an.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + an.getName() + "\" and " +
					"aluno.cpf = \"" + an.getCpf() + "\" and " +
					"aluno.telefone = \"" + an.getPhone() + "\" and " +
					"aluno.email = \"" + an.getEmail() + "\" and " +
					"aluno.matricula = \"" + an.getRegistration() + "\";");
		}
		assertFalse("Teste de Alteração.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaJaExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		Student a = new Student("Incluindo", "040.757.021-70", "058801", "9999-9999", "aluno@email");
		Student an = new Student("Incluindo", "040.757.021-70", "058801", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		try{
			StudentDAO.getInstance().alterate(a, an);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + an.getName() + "\" and " +
				"aluno.cpf = \"" + an.getCpf() + "\" and " +
				"aluno.telefone = \"" + an.getPhone() + "\" and " +
				"aluno.email = \"" + an.getEmail() + "\" and " +
				"aluno.matricula = \"" + an.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + an.getName() + "\" and " +
					"aluno.cpf = \"" + an.getCpf() + "\" and " +
					"aluno.telefone = \"" + an.getPhone() + "\" and " +
					"aluno.email = \"" + an.getEmail() + "\" and " +
					"aluno.matricula = \"" + an.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		}
		assertTrue("Teste de Alteração.", resultado == false && resultado2 == true);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaCpfExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		boolean resultado3 = false;
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		Student an = new Student("Incluindo Segundo", "490.491.781-20", "1234", "4444-4444", "novoStudent@email");
		Student ann = new Student("Incluindo Segundo", "040.757.021-70", "1234", "4444-4444", "novoStudent@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		this.executaNoBanco("INSERT INTO " +
				"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + an.getName() + "\", " +
				"\"" + an.getCpf()+ "\", " +
				"\"" + an.getPhone() + "\", " +
				"\"" + an.getEmail() + "\", " +
				"\"" + an.getRegistration() + "\"); ");
		
		try{
			StudentDAO.getInstance().alterate(an, ann);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + an.getName() + "\" and " +
				"aluno.cpf = \"" + an.getCpf() + "\" and " +
				"aluno.telefone = \"" + an.getPhone() + "\" and " +
				"aluno.email = \"" + an.getEmail() + "\" and " +
				"aluno.matricula = \"" + an.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
			resultado3 =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
					"aluno.nome = \"" + ann.getName() + "\" and " +
					"aluno.cpf = \"" + ann.getCpf() + "\" and " +
					"aluno.telefone = \"" + ann.getPhone() + "\" and " +
					"aluno.email = \"" + ann.getEmail() + "\" and " +
					"aluno.matricula = \"" + ann.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + an.getName() + "\" and " +
					"aluno.cpf = \"" + an.getCpf() + "\" and " +
					"aluno.telefone = \"" + an.getPhone() + "\" and " +
					"aluno.email = \"" + an.getEmail() + "\" and " +
					"aluno.matricula = \"" + an.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
			if(resultado3)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"aluno.nome = \"" + ann.getName() + "\" and " +
					"aluno.cpf = \"" + ann.getCpf() + "\" and " +
					"aluno.telefone = \"" + ann.getPhone() + "\" and " +
					"aluno.email = \"" + ann.getEmail() + "\" and " +
					"aluno.matricula = \"" + ann.getRegistration() + "\";");
		}
		assertTrue("Teste de Alteração.", resultado == true && resultado2 == true && resultado3 == false);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaRegistrationExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		boolean resultado3 = false;
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-99999", "aluno@email");
		Student an = new Student("Incluindo Segundo", "490.491.781-20", "0987", "5555-5555", "alunoNovo@email");
		Student ann = new Student("Incluindo Segundo", "490.491.781-20", "123456", "5555-5555", "alunoNovo@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		this.executaNoBanco("INSERT INTO " +
				"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + an.getName() + "\", " +
				"\"" + an.getCpf()+ "\", " +
				"\"" + an.getPhone() + "\", " +
				"\"" + an.getEmail() + "\", " +
				"\"" + an.getRegistration() + "\"); ");
		
		try{
			StudentDAO.getInstance().alterate(an, ann);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + an.getName() + "\" and " +
				"aluno.cpf = \"" + an.getCpf() + "\" and " +
				"aluno.telefone = \"" + an.getPhone() + "\" and " +
				"aluno.email = \"" + an.getEmail() + "\" and " +
				"aluno.matricula = \"" + an.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
			resultado3 =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
					"aluno.nome = \"" + ann.getName() + "\" and " +
					"aluno.cpf = \"" + ann.getCpf() + "\" and " +
					"aluno.telefone = \"" + ann.getPhone() + "\" and " +
					"aluno.email = \"" + ann.getEmail() + "\" and " +
					"aluno.matricula = \"" + ann.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
						"aluno.nome = \"" + an.getName() + "\" and " +
						"aluno.cpf = \"" + an.getCpf() + "\" and " +
						"aluno.telefone = \"" + an.getPhone() + "\" and " +
						"aluno.email = \"" + an.getEmail() + "\" and " +
						"aluno.matricula = \"" + an.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
			if(resultado3)
				this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + ann.getName() + "\" and " +
					"aluno.cpf = \"" + ann.getCpf() + "\" and " +
					"aluno.telefone = \"" + ann.getPhone() + "\" and " +
					"aluno.email = \"" + ann.getEmail() + "\" and " +
					"aluno.matricula = \"" + ann.getRegistration() + "\";");
		}
		assertTrue("Teste de Alteração.", resultado == true && resultado2 == true && resultado3 == false);
	}
	@Ignore // (expected= ClientException.class)
	public void testAlterarEnvolvidoEmReserva() throws ClientException, SQLException {
		fail();
	}
	
	
	
	@Test
	public void testExcluir() throws ClientException, SQLException {
		boolean resultado = true;
		Student a = new Student("Incluindo", "040.757.021-70", "058801", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		StudentDAO.getInstance().delete(a);
		

		resultado =  this.estaNoBanco("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertFalse("Teste de Alteração.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testExcluirNulo() throws ClientException, SQLException {
		StudentDAO.getInstance().delete(null);
	}
	@Ignore // (expected= ClientException.class)
	public void testExcluirEnvolvidoEmReserva() throws ClientException, SQLException {
		fail();
	}
	@Test (expected= ClientException.class)
	public void testExcluirNaoExistente() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		StudentDAO.getInstance().delete(a);
	}
	
	
	
	@Test
	public void testBuscarNome() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		Vector<Student> vet = StudentDAO.getInstance().searchName("Incluindo");

		this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarCpf() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		Vector<Student> vet = StudentDAO.getInstance().searchCpf("040.757.021-70");

		this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarRegistration() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
						"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + a.getName() + "\", " +
						"\"" + a.getCpf()+ "\", " +
						"\"" + a.getPhone() + "\", " +
						"\"" + a.getEmail() + "\", " +
						"\"" + a.getRegistration() + "\"); ");
		
		Vector<Student> vet = StudentDAO.getInstance().searchRegistration("123456");

		this.executaNoBanco("DELETE FROM aluno WHERE " +
					"aluno.nome = \"" + a.getName() + "\" and " +
					"aluno.cpf = \"" + a.getCpf() + "\" and " +
					"aluno.telefone = \"" + a.getPhone() + "\" and " +
					"aluno.email = \"" + a.getEmail() + "\" and " +
					"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarPhone() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
				"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + a.getName() + "\", " +
				"\"" + a.getCpf()+ "\", " +
				"\"" + a.getPhone() + "\", " +
				"\"" + a.getEmail() + "\", " +
				"\"" + a.getRegistration() + "\"); ");
		
		Vector<Student> vet = StudentDAO.getInstance().searchPhone("9999-9999");

		this.executaNoBanco("DELETE FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarEmail() throws ClientException, SQLException {
		Student a = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "aluno@email");
		this.executaNoBanco("INSERT INTO " +
				"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + a.getName() + "\", " +
				"\"" + a.getCpf()+ "\", " +
				"\"" + a.getPhone() + "\", " +
				"\"" + a.getEmail() + "\", " +
				"\"" + a.getRegistration() + "\"); ");
		
		Vector<Student> vet = StudentDAO.getInstance().searchEmail("aluno@email");

		this.executaNoBanco("DELETE FROM aluno WHERE " +
				"aluno.nome = \"" + a.getName() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getPhone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	
	
	

	private void executaNoBanco(String msg) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(msg);
		pst.executeUpdate();
		pst.close();
		con.close();
	}
	private boolean estaNoBanco(String query) throws SQLException{
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		if(!rs.next())
		{
			rs.close();
			pst.close();
			con.close();
			return false;
		}
		else {
			rs.close();
			pst.close();
			con.close();
			return true;
		}
	}
}

	