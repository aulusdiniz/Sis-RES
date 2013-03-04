package test.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Professor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import exception.ClientException;

import persistence.FactoryConnection;
import persistence.ProfessorDAO;

public class ProfessorDAOTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	
	@Test
	public void testInstance() {
		assertTrue("Instanciando ProfessorDAO", ProfessorDAO.getInstance() instanceof ProfessorDAO);
	}
	
	@Test
	public void testSingleton() {
		ProfessorDAO p = ProfessorDAO.getInstance();
		ProfessorDAO q = ProfessorDAO.getInstance();
		assertSame("Testando o Padrao Singleton", p, q);
	}
	
	
	
	@Test
	public void testIncluir() throws ClientException, SQLException {
		boolean resultado = false;
		Professor prof = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		ProfessorDAO.getInstance().include(prof);
		
		resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
		"professor.nome = \"" + prof.getName() + "\" and " +
		"professor.cpf = \"" + prof.getCpf() + "\" and " +
		"professor.telefone = \"" + prof.getPhone() + "\" and " +
		"professor.email = \"" + prof.getEmail() + "\" and " +
		"professor.matricula = \"" + prof.getRegistration() + "\";");
		
		if(resultado){
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof.getName() + "\" and " +
					"professor.cpf = \"" + prof.getCpf() + "\" and " +
					"professor.telefone = \"" + prof.getPhone() + "\" and " +
					"professor.email = \"" + prof.getEmail() + "\" and " +
					"professor.matricula = \"" + prof.getRegistration() + "\";");
		}
		assertTrue("Teste de Inclusão.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testIncluirNulo() throws ClientException, SQLException {
		ProfessorDAO.getInstance().include(null);
	}
	@Test (expected= ClientException.class)
	public void testIncluirComMesmoCpf() throws ClientException, SQLException {
		boolean resultado = true;
		Professor prof = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor prof2 = new Professor("Nome para Incluir Segundo", "868.563.327-34", "0987", "5678-5555", "");
		ProfessorDAO.getInstance().include(prof);
		try{
			ProfessorDAO.getInstance().include(prof2);
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof.getName() + "\" and " +
					"professor.cpf = \"" + prof.getCpf() + "\" and " +
					"professor.telefone = \"" + prof.getPhone() + "\" and " +
					"professor.email = \"" + prof.getEmail() + "\" and " +
					"professor.matricula = \"" + prof.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testIncluirComMesmaMatricula() throws ClientException, SQLException {
		boolean resultado = true;
		Professor prof = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor prof2 = new Professor("Nome para Incluir Segundo", "387.807.647-97", "123456", "5678-5555", "");
		ProfessorDAO.getInstance().include(prof);
		try{
			ProfessorDAO.getInstance().include(prof2);
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof.getName() + "\" and " +
					"professor.cpf = \"" + prof.getCpf() + "\" and " +
					"professor.telefone = \"" + prof.getPhone() + "\" and " +
					"professor.email = \"" + prof.getEmail() + "\" and " +
					"professor.matricula = \"" + prof.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testIncluirJaExistente() throws ClientException, SQLException {
		boolean resultado = true;
		Professor prof = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor prof2 = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		ProfessorDAO.getInstance().include(prof);
		try{
			ProfessorDAO.getInstance().include(prof2);
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
			
		} finally {
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof.getName() + "\" and " +
					"professor.cpf = \"" + prof.getCpf() + "\" and " +
					"professor.telefone = \"" + prof.getPhone() + "\" and " +
					"professor.email = \"" + prof.getEmail() + "\" and " +
					"professor.matricula = \"" + prof.getRegistration() + "\";");
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
					"professor.nome = \"" + prof2.getName() + "\" and " +
					"professor.cpf = \"" + prof2.getCpf() + "\" and " +
					"professor.telefone = \"" + prof2.getPhone() + "\" and " +
					"professor.email = \"" + prof2.getEmail() + "\" and " +
					"professor.matricula = \"" + prof2.getRegistration() + "\";");
		}
		
		assertFalse("Teste de Inclusão.", resultado);
	}
	
	
	
	@Test
	public void testAlterar() throws ClientException, SQLException {
		boolean resultado = false;
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor pn = new Professor("Nome para Alterar", "387.807.647-97", "098765", "(123)4567-8899", "email@Nome");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		ProfessorDAO.getInstance().alterate(p, pn);
		
		resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + pn.getName() + "\" and " +
				"professor.cpf = \"" + pn.getCpf() + "\" and " +
				"professor.telefone = \"" + pn.getPhone() + "\" and " +
				"professor.email = \"" + pn.getEmail() + "\" and " +
				"professor.matricula = \"" + pn.getRegistration() + "\";");
		boolean resultado2 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + p.getName() + "\" and " +
				"professor.cpf = \"" + p.getCpf() + "\" and " +
				"professor.telefone = \"" + p.getPhone() + "\" and " +
				"professor.email = \"" + p.getEmail() + "\" and " +
				"professor.matricula = \"" + p.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pn.getName() + "\" and " +
					"professor.cpf = \"" + pn.getCpf() + "\" and " +
					"professor.telefone = \"" + pn.getPhone() + "\" and " +
					"professor.email = \"" + pn.getEmail() + "\" and " +
					"professor.matricula = \"" + pn.getRegistration() + "\";");
		if(resultado2)
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", resultado == true && resultado2 == false);
	}
	@Test (expected= ClientException.class)
	public void testAlterarPrimeiroArgNulo() throws ClientException, SQLException {
		Professor pn = new Professor("Nome para Alterar", "868.563.327-34", "098765", "(123)4567-8899", "email@Nome");
		ProfessorDAO.getInstance().alterate(null, pn);
	}
	@Test (expected= ClientException.class)
	public void testAlterarSegundoArgNulo() throws ClientException, SQLException {
		Professor pn = new Professor("Nome para Alterar", "868.563.327-34", "098765", "(123)4567-8899", "email@Nome");
		ProfessorDAO.getInstance().alterate(pn, null);
	}
	@Test (expected= ClientException.class)
	public void testAlterarNaoExistente() throws ClientException, SQLException {
		boolean resultado = true;
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor pn = new Professor("Nome para Alterar", "387.807.647-97", "098765", "(123)4567-8899", "email@Nome");
		
		try{
			ProfessorDAO.getInstance().alterate(p, pn);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + pn.getName() + "\" and " +
				"professor.cpf = \"" + pn.getCpf() + "\" and " +
				"professor.telefone = \"" + pn.getPhone() + "\" and " +
				"professor.email = \"" + pn.getEmail() + "\" and " +
				"professor.matricula = \"" + pn.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pn.getName() + "\" and " +
					"professor.cpf = \"" + pn.getCpf() + "\" and " +
					"professor.telefone = \"" + pn.getPhone() + "\" and " +
					"professor.email = \"" + pn.getEmail() + "\" and " +
					"professor.matricula = \"" + pn.getRegistration() + "\";");
		}
		assertFalse("Teste de Alteração.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaJaExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor pn = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		try{
			ProfessorDAO.getInstance().alterate(p, pn);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + pn.getName() + "\" and " +
				"professor.cpf = \"" + pn.getCpf() + "\" and " +
				"professor.telefone = \"" + pn.getPhone() + "\" and " +
				"professor.email = \"" + pn.getEmail() + "\" and " +
				"professor.matricula = \"" + pn.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + p.getName() + "\" and " +
				"professor.cpf = \"" + p.getCpf() + "\" and " +
				"professor.telefone = \"" + p.getPhone() + "\" and " +
				"professor.email = \"" + p.getEmail() + "\" and " +
				"professor.matricula = \"" + p.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pn.getName() + "\" and " +
					"professor.cpf = \"" + pn.getCpf() + "\" and " +
					"professor.telefone = \"" + pn.getPhone() + "\" and " +
					"professor.email = \"" + pn.getEmail() + "\" and " +
					"professor.matricula = \"" + pn.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		}
		assertTrue("Teste de Alteração.", resultado == false && resultado2 == true);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaCpfExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		boolean resultado3 = false;
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor pn = new Professor("Nome para Incluir Segundo", "387.807.647-97", "0987", "5555-5678", "Ne@email");
		Professor pnn = new Professor("Nome para Incluir Segundo", "868.563.327-34", "0987", "5555-5678", "Ne@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		this.executaNoBanco("INSERT INTO " +
				"professor (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + pn.getName() + "\", " +
				"\"" + pn.getCpf()+ "\", " +
				"\"" + pn.getPhone() + "\", " +
				"\"" + pn.getEmail() + "\", " +
				"\"" + pn.getRegistration() + "\"); ");
		
		try{
			ProfessorDAO.getInstance().alterate(pn, pnn);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + pn.getName() + "\" and " +
				"professor.cpf = \"" + pn.getCpf() + "\" and " +
				"professor.telefone = \"" + pn.getPhone() + "\" and " +
				"professor.email = \"" + pn.getEmail() + "\" and " +
				"professor.matricula = \"" + pn.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + p.getName() + "\" and " +
				"professor.cpf = \"" + p.getCpf() + "\" and " +
				"professor.telefone = \"" + p.getPhone() + "\" and " +
				"professor.email = \"" + p.getEmail() + "\" and " +
				"professor.matricula = \"" + p.getRegistration() + "\";");
			resultado3 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
					"professor.nome = \"" + pnn.getName() + "\" and " +
					"professor.cpf = \"" + pnn.getCpf() + "\" and " +
					"professor.telefone = \"" + pnn.getPhone() + "\" and " +
					"professor.email = \"" + pnn.getEmail() + "\" and " +
					"professor.matricula = \"" + pnn.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pn.getName() + "\" and " +
					"professor.cpf = \"" + pn.getCpf() + "\" and " +
					"professor.telefone = \"" + pn.getPhone() + "\" and " +
					"professor.email = \"" + pn.getEmail() + "\" and " +
					"professor.matricula = \"" + pn.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
			if(resultado3)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pnn.getName() + "\" and " +
					"professor.cpf = \"" + pnn.getCpf() + "\" and " +
					"professor.telefone = \"" + pnn.getPhone() + "\" and " +
					"professor.email = \"" + pnn.getEmail() + "\" and " +
					"professor.matricula = \"" + pnn.getRegistration() + "\";");
		}
		assertTrue("Teste de Alteração.", resultado == true && resultado2 == true && resultado3 == false);
	}
	@Test (expected= ClientException.class)
	public void testAlterarParaMatriculaExistente() throws ClientException, SQLException {
		boolean resultado = true;
		boolean resultado2 = false;
		boolean resultado3 = false;
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		Professor pn = new Professor("Nome para Incluir Segundo", "387.807.647-97", "0987", "5555-5678", "Ne@email");
		Professor pnn = new Professor("Nome para Incluir Segundo", "387.807.647-97", "123456", "5555-5678", "Ne@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		this.executaNoBanco("INSERT INTO " +
				"professor (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + pn.getName() + "\", " +
				"\"" + pn.getCpf()+ "\", " +
				"\"" + pn.getPhone() + "\", " +
				"\"" + pn.getEmail() + "\", " +
				"\"" + pn.getRegistration() + "\"); ");
		
		try{
			ProfessorDAO.getInstance().alterate(pn, pnn);
		} finally {
			resultado = this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + pn.getName() + "\" and " +
				"professor.cpf = \"" + pn.getCpf() + "\" and " +
				"professor.telefone = \"" + pn.getPhone() + "\" and " +
				"professor.email = \"" + pn.getEmail() + "\" and " +
				"professor.matricula = \"" + pn.getRegistration() + "\";");
			resultado2 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + p.getName() + "\" and " +
				"professor.cpf = \"" + p.getCpf() + "\" and " +
				"professor.telefone = \"" + p.getPhone() + "\" and " +
				"professor.email = \"" + p.getEmail() + "\" and " +
				"professor.matricula = \"" + p.getRegistration() + "\";");
			resultado3 =  this.estaNoBanco("SELECT * FROM professor WHERE " +
					"professor.nome = \"" + pnn.getName() + "\" and " +
					"professor.cpf = \"" + pnn.getCpf() + "\" and " +
					"professor.telefone = \"" + pnn.getPhone() + "\" and " +
					"professor.email = \"" + pnn.getEmail() + "\" and " +
					"professor.matricula = \"" + pnn.getRegistration() + "\";");
			if(resultado)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pn.getName() + "\" and " +
					"professor.cpf = \"" + pn.getCpf() + "\" and " +
					"professor.telefone = \"" + pn.getPhone() + "\" and " +
					"professor.email = \"" + pn.getEmail() + "\" and " +
					"professor.matricula = \"" + pn.getRegistration() + "\";");
			if(resultado2)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
			if(resultado3)
				this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + pnn.getName() + "\" and " +
					"professor.cpf = \"" + pnn.getCpf() + "\" and " +
					"professor.telefone = \"" + pnn.getPhone() + "\" and " +
					"professor.email = \"" + pnn.getEmail() + "\" and " +
					"professor.matricula = \"" + pnn.getRegistration() + "\";");
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
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		ProfessorDAO.getInstance().delete(p);
		

		resultado =  this.estaNoBanco("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + p.getName() + "\" and " +
				"professor.cpf = \"" + p.getCpf() + "\" and " +
				"professor.telefone = \"" + p.getPhone() + "\" and " +
				"professor.email = \"" + p.getEmail() + "\" and " +
				"professor.matricula = \"" + p.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertFalse("Teste de Alteração.", resultado);
	}
	@Test (expected= ClientException.class)
	public void testExcluirNulo() throws ClientException, SQLException {
		ProfessorDAO.getInstance().delete(null);
	}
	@Ignore //(expected= ClientException.class)
	public void testExcluirEnvolvidoEmReserva() throws ClientException, SQLException {
		fail();
	}
	@Test (expected= ClientException.class)
	public void testExcluirNaoExistente() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		ProfessorDAO.getInstance().delete(p);
	}
	
	
	
	@Test
	public void testBuscarNome() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		Vector<Professor> vet = ProfessorDAO.getInstance().searchName("Nome para Incluir");

		this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarCpf() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		Vector<Professor> vet = ProfessorDAO.getInstance().searchCpf("868.563.327-34");

		this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarMatricula() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		Vector<Professor> vet = ProfessorDAO.getInstance().searchRegistration("123456");

		this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarTelefone() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		Vector<Professor> vet = ProfessorDAO.getInstance().searchPhone("1234-5678");

		this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
		assertTrue("Teste de Alteração.", vet.size() > 0);
	}
	@Test
	public void testBuscarEmail() throws ClientException, SQLException {
		Professor p = new Professor("Nome para Incluir", "868.563.327-34", "123456", "1234-5678", "Nome@email");
		this.executaNoBanco("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + p.getName() + "\", " +
						"\"" + p.getCpf()+ "\", " +
						"\"" + p.getPhone() + "\", " +
						"\"" + p.getEmail() + "\", " +
						"\"" + p.getRegistration() + "\"); ");
		
		Vector<Professor> vet = ProfessorDAO.getInstance().searchEmail("Nome@email");

		this.executaNoBanco("DELETE FROM professor WHERE " +
					"professor.nome = \"" + p.getName() + "\" and " +
					"professor.cpf = \"" + p.getCpf() + "\" and " +
					"professor.telefone = \"" + p.getPhone() + "\" and " +
					"professor.email = \"" + p.getEmail() + "\" and " +
					"professor.matricula = \"" + p.getRegistration() + "\";");
		
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
