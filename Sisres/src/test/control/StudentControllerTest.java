package test.control;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Student;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.FactoryConnection;

import control.StudentController;
import exception.ClientException;

public class StudentControllerTest {

	private static Vector<Student> Students;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Students = StudentController.getInstance().getStudentVector();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	
	@Test
	public void testGetInstance() {
		assertTrue("Verifica método getInstance() de StudentController.", StudentController.getInstance() instanceof StudentController);
	}

	@Test
	public void testSingleton() {
		StudentController p = StudentController.getInstance();
		StudentController q = StudentController.getInstance();
		assertSame("Testando o Padrao Singleton em StudentController", p, q);
	}

	
	
	@Test
	public void testInserir() throws ClientException, SQLException {
		Student student = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "student@email");
		StudentController.getInstance().insert("Incluindo", "040.757.021-70", "123456", "9999-9999", "student@email");
		
		boolean resultado = this.estaNoBanco("SELECT * FROM student WHERE " +
				"student.nome = \"" + student.getName() + "\" and " +
				"student.cpf = \"" + student.getCpf() + "\" and " +
				"student.telefone = \"" + student.getPhone() + "\" and " +
				"student.email = \"" + student.getEmail() + "\" and " +
				"student.matricula = \"" + student.getRegistration() + "\";");
				
		if(resultado){
			this.executaNoBanco("DELETE FROM student WHERE " +
					"student.nome = \"" + student.getName() + "\" and " +
					"student.cpf = \"" + student.getCpf() + "\" and " +
					"student.telefone = \"" + student.getPhone() + "\" and " +
					"student.email = \"" + student.getEmail() + "\" and " +
					"student.matricula = \"" + student.getRegistration() + "\";");
		}
		
		Student a = Students.lastElement();
		boolean resultado2 = student.equals(a);
		Students.remove(Students.lastElement());
		assertTrue("Teste de Inclusao do Student.", resultado == true && resultado2 == true);
	}
	
	
	@Test
	public void testAlterar() throws ClientException, SQLException {
		Student student = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "student@email");
		Student a = new Student("Alterando", "040.757.021-70", "123456", "9999-9999", "Nome@email");
		
		this.executaNoBanco("INSERT INTO " +
				"student (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + student.getName() + "\", " +
				"\"" + student.getCpf()+ "\", " +
				"\"" + student.getPhone() + "\", " +
				"\"" + student.getEmail() + "\", " +
				"\"" + student.getRegistration() + "\"); ");
		
		StudentController.getInstance().alterate("Alterando", "040.757.021-70", "123456", 
				"9999-9999", "Nome@email", student);
		
		boolean resultado =  this.estaNoBanco("SELECT * FROM student WHERE " +
				"student.nome = \"" + a.getName() + "\" and " +
				"student.cpf = \"" + a.getCpf() + "\" and " +
				"student.telefone = \"" + a.getPhone() + "\" and " +
				"student.email = \"" + a.getEmail() + "\" and " +
				"student.matricula = \"" + a.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM student WHERE " +
					"student.nome = \"" + a.getName() + "\" and " +
					"student.cpf = \"" + a.getCpf() + "\" and " +
					"student.telefone = \"" + a.getPhone() + "\" and " +
					"student.email = \"" + a.getEmail() + "\" and " +
					"student.matricula = \"" + a.getRegistration() + "\";");
		
		assertTrue("Teste de Alteracao do Student.", resultado);
	}
	
	@Test
	public void testExcluir() throws ClientException, SQLException {
		Student student = new Student("Incluindo", "040.757.021-70", "123456", "9999-9999", "student@email");
		
		this.executaNoBanco("INSERT INTO " +
				"student (nome, cpf, telefone, email, matricula) VALUES (" +
				"\"" + student.getName() + "\", " +
				"\"" + student.getCpf()+ "\", " +
				"\"" + student.getPhone() + "\", " +
				"\"" + student.getEmail() + "\", " +
				"\"" + student.getRegistration() + "\");");
		
		StudentController.getInstance().delete(student);
		
		boolean resultado =  this.estaNoBanco("SELECT * FROM student WHERE " +
				"student.nome = \"" + student.getName() + "\" and " +
				"student.cpf = \"" + student.getCpf() + "\" and " +
				"student.telefone = \"" + student.getPhone() + "\" and " +
				"student.email = \"" + student.getEmail() + "\" and " +
				"student.matricula = \"" + student.getRegistration() + "\";");
		if(resultado)
			this.executaNoBanco("DELETE FROM student WHERE " +
					"student.nome = \"" + student.getName() + "\" and " +
					"student.cpf = \"" + student.getCpf() + "\" and " +
					"student.telefone = \"" + student.getPhone() + "\" and " +
					"student.email = \"" + student.getEmail() + "\" and " +
					"student.matricula = \"" + student.getRegistration() + "\";");
		
		boolean resultado2 = true;
		if(Students.size() > 0)
			resultado2 = !Students.lastElement().equals(student);
		
		assertTrue("Teste de Exclusao do Professor.", resultado == false && resultado2 == true);
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
