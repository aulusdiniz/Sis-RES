package test.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import model.Aluno;
import model.ReservaSalaAluno;
import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.AlunoDAO;
import persistence.FactoryConnection;
import persistence.ReserveStudentRoomDAO;
import persistence.SalaDAO;

import exception.ClienteException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ResSalaAlunoDAOTest {

	private static Room sala1;
	private static Room sala2;
	private static Aluno aluno1;
	private static Aluno aluno2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sala1 = new Room("123", "Room de Aula", "120");
		sala2 = new Room("543", "Laboratorio", "30");
		aluno1 = new Aluno("testInstance", "501.341.852-69", "456678", "", "");
		aluno2 = new Aluno("Incluindo Matricula Igual", "490.491.781-20", "345543", "2222-2222", "aluno2@email");
		
		AlunoDAO.getInstance().include(aluno1);
		AlunoDAO.getInstance().include(aluno2);
		SalaDAO.getInstance().incluir(sala1);
		SalaDAO.getInstance().incluir(sala2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		AlunoDAO.getInstance().delete(aluno1);
		AlunoDAO.getInstance().delete(aluno2);
		SalaDAO.getInstance().excluir(sala1);
		SalaDAO.getInstance().excluir(sala2);
	}

	@Test
	public void testInstance() {
		assertTrue("Teste de Instancia", ReserveStudentRoomDAO.getInstance() instanceof ReserveStudentRoomDAO);
	}
	@Test
	public void testSingleton() {
		assertSame("Teste de Singleton", ReserveStudentRoomDAO.getInstance(), ReserveStudentRoomDAO.getInstance());
	}
	
	
	@Test
	public void testIncluir() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		
		ReserveStudentRoomDAO.getInstance().incluir(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
		assertTrue("Teste de Inclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testIncluirNulo() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReserveStudentRoomDAO.getInstance().incluir(null);
	}
	@Test (expected= ReserveException.class)
	public void testIncluirAlunoInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", new Aluno("tepp", "501.341.852-69", "456678", "", ""));
		
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
		boolean resultado = this.inDB(reserva);

		if(resultado)
			this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", new Room("22277883", "Laboratorio", "120"),
				"Grupo de Estudos", "120", aluno1);
		
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
		boolean resultado = this.inDB(reserva);

		if(resultado)
			this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaReservadaProf() throws ReserveException, ClienteException, 
											PatrimonyException, SQLException 
	{
		this.executeQuery("INSERT INTO professor (nome, cpf, matricula) " +
		"VALUES (\"ProfessorDAO\", \"257.312.954-33\", \"11009988\");");
		this.executeQuery("INSERT INTO reserva_sala_professor (id_professor,id_sala,finalidade,hora,data) "+
		"VALUES ((SELECT id_professor FROM professor WHERE cpf = \"257.312.954-33\")," +
				"(SELECT id_sala FROM room WHERE codigo = \"123\")," +
				"\"Aula de Calculo\", \"08:00\", \"20/12/2034\");");
		
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "60", aluno1);
		
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
		if(this.inDB(reserva))
			this.delete_from(reserva);
		
		this.executeQuery("DELETE FROM professor WHERE cpf = \"257.312.954-33\";");
		this.executeQuery("DELETE FROM reserva_sala_professor WHERE data = \"20/12/2034\";");
		
		}
		
	}
	@Test (expected= ReserveException.class)
	public void testIncluirAlunoOcupado() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala2,
				"Grupo de Estudos", ""+sala2.getCapacity(), aluno1);
		ReserveStudentRoomDAO.getInstance().incluir(reserva);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva2);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaCheia() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "60", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "70", aluno2);
		ReserveStudentRoomDAO.getInstance().incluir(reserva);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva2);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouAno() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/1990", "8:00", sala1,
				"Grupo de Estudos", "60", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouMes() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/01/2013", "8:00", sala1,
				"Grupo de Estudos", "60", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouDia() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno(this.dataAtualAMais(-100000000), this.horaAtual(), sala1,
				"Grupo de Estudos", "60", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirHoraPassouHora() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno(this.dataAtual(),
				 this.horaAtualAMais(-10000000), sala1,
				"Grupo de Estudos", "60", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirHoraPassouMinutos() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno(this.dataAtual(),
				this.horaAtualAMais(-100000), sala1,
				"Grupo de Estudos", "60", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().incluir(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	
	
	@Test
	public void testAlterar() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("21/12/34", "19:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		
		this.insert_into(reserva);
		
		ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		
		boolean resultado = this.inDB(reserva2);
		
		if(resultado)
			this.delete_from(reserva2);
		if(this.inDB(reserva))
			this.delete_from(reserva);
		assertTrue("Teste de Inclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNulo1() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		ReserveStudentRoomDAO.getInstance().alterar(null, reserva);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNulo2() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
			"Grupo de Estudos", "120", aluno1);
		ReserveStudentRoomDAO.getInstance().alterar(reserva, null);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarJaInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("27/12/34", "9:00", sala2,
				"Grupo d", ""+sala2.getCapacity(), aluno2);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva2, reserva);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraDifAlunoOcupado() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "9:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		ReservaSalaAluno reserva3 = new ReservaSalaAluno("20/12/34", "8:00", sala2,
				"Grupo de Estudos", ""+sala2.getCapacity(), aluno1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva2, reserva3);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		if(this.inDB(reserva3))
			this.delete_from(reserva3);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataDifSalaOcupado() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		this.executeQuery("INSERT INTO professor (nome, cpf, matricula) " +
				"VALUES (\"ProfessorDAO\", \"257.312.954-33\", \"11009988\");");
		this.executeQuery("INSERT INTO reserva_sala_professor (id_professor,id_sala,finalidade,hora,data) "+
				"VALUES ((SELECT id_professor FROM professor WHERE cpf = \"257.312.954-33\")," +
						"(SELECT id_sala FROM room WHERE codigo = \"123\")," +
						"\"Aula de Calculo\", \"08:00\", \"20/12/2034\");");
				
		
		ReservaSalaAluno reserva = new ReservaSalaAluno("21/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		this.insert_into(reserva);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		
		this.executeQuery("DELETE FROM professor WHERE cpf = \"257.312.954-33\";");
		this.executeQuery("DELETE FROM reserva_sala_professor WHERE data = \"20/12/2034\";");
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarAlunoInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("21/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		this.insert_into(reserva);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", new Aluno("tepp", "501.341.852-69", "456678", "", ""));
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarSalaInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("21/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		this.insert_into(reserva);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", new Room("22277883", "Laboratorio", "120"),
				"Grupo de Estudos", "120", aluno1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNaoHaCadeira() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "20", aluno2);
		ReservaSalaAluno reserva3 = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno2);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva2, reserva3);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		if(this.inDB(reserva3))
			this.delete_from(reserva3);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataPassouAno() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/12/1990", "8:00", sala1,
				"Grupo de Estudos", "20", aluno2);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataPassouMes() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("20/01/2013", "8:00", sala1,
				"Grupo de Estudos", "20", aluno2);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataPassouDia() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno(this.dataAtualAMais(-100000000), this.horaAtual(), sala1,
				"Grupo de Estudos", "60", aluno1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraPassouHora() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno(this.dataAtual(),
				 this.horaAtualAMais(-10000000), sala1,
				"Grupo de Estudos", "60", aluno1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraPassouMinutos() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "30", aluno1);
		ReservaSalaAluno reserva2 = new ReservaSalaAluno(this.dataAtual(),
				this.horaAtualAMais(-100000), sala1,
				"Grupo de Estudos", "60", aluno1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterar(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	
	
	
	@Test
	public void testExcluir() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);
		this.insert_into(reserva);
		
		ReserveStudentRoomDAO.getInstance().delete(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
		assertFalse("Teste de Exclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testExcluirNulo() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReserveStudentRoomDAO.getInstance().delete(null);
	}
	@Test (expected= ReserveException.class)
	public void testExcluirInexistente() throws ReserveException, ClienteException, PatrimonyException, SQLException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "8:00", sala1,
				"Grupo de Estudos", "120", aluno1);

		ReserveStudentRoomDAO.getInstance().delete(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
	}
	
	
	@Test
	public void testBuscarPorDia() throws SQLException, PatrimonyException, ClienteException, ReserveException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("21/12/34", "8:00", sala1,
				"Grupo de Estudos", "40", aluno1);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("21/12/34", "19:00", sala1,
				"Grupo de Estudos", "50", aluno1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		Vector<ReservaSalaAluno> vet = ReserveStudentRoomDAO.getInstance().findByDate("21/12/34");
		this.delete_from(reserva);
		this.delete_from(reserva2);
		
		boolean resultado = false;
		boolean resultado2 = false;
		
		Iterator<ReservaSalaAluno> it = vet.iterator();
		while(it.hasNext()){
			ReservaSalaAluno obj = it.next();
			if(obj.equals(reserva))
				resultado = true;
			else if(obj.equals(reserva2))
				resultado2 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2);
	}
	@Test
	public void testBuscarPorHora() throws SQLException, PatrimonyException, ClienteException, ReserveException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("20/12/34", "9:00", sala1,
				"Grupo de Estudos", "40", aluno1);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("21/12/34", "09:00", sala1,
				"Grupo de Estudos", "50", aluno1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		Vector<ReservaSalaAluno> vet = ReserveStudentRoomDAO.getInstance().buscarPorHora("09:00");
		this.delete_from(reserva);
		this.delete_from(reserva2);
		
		boolean resultado = false;
		boolean resultado2 = false;
		
		Iterator<ReservaSalaAluno> it = vet.iterator();
		while(it.hasNext()){
			ReservaSalaAluno obj = it.next();
			if(obj.equals(reserva))
				resultado = true;
			else if(obj.equals(reserva2))
				resultado2 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2);
	}
	
	
	@Test
	public void testCadeirasDisponiveis() throws SQLException, PatrimonyException, ClienteException, ReserveException {
		ReservaSalaAluno reserva = new ReservaSalaAluno("21/12/34", "19:00", sala1,
				"Grupo de Estudos", "40", aluno1);
		
		ReservaSalaAluno reserva2 = new ReservaSalaAluno("21/12/34", "19:00", sala1,
				"Grupo de Estudos", "50", aluno1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		int c = ReserveStudentRoomDAO.getInstance().availableChairs(sala1, "21/12/34", "19:00");
		this.delete_from(reserva);
		this.delete_from(reserva2);
		assertEquals("Teste de disponibilidade de Cadeiras", c, 30);
	}

	
	private String dataAtual(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		return formatador.format(date);
	}
	
	private String horaAtual(){
		Date date = new Date(System.currentTimeMillis());
		return date.toString().substring(11, 16);
	}
	
	private String horaAtualAMais(int fator){
		Date date = new Date(System.currentTimeMillis()+fator);
		return date.toString().substring(11, 16);
	}
	
	private String dataAtualAMais(int fator){
		Date date = new Date(System.currentTimeMillis()+fator);
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		return formatador.format(date);
	}
	
	
	private String select_id_aluno(Aluno a){
		return "SELECT id_aluno FROM aluno WHERE " +
				"aluno.nome = \"" + a.getNome() + "\" and " +
				"aluno.cpf = \"" + a.getCpf() + "\" and " +
				"aluno.telefone = \"" + a.getTelefone() + "\" and " +
				"aluno.email = \"" + a.getEmail() + "\" and " +
				"aluno.matricula = \"" + a.getMatricula() + "\"";
	}
	private String select_id_sala(Room room){
		return "SELECT id_sala FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity();
	}
	private String where_reserva_sala_aluno(ReservaSalaAluno r){
		return " WHERE " +
		"id_aluno = ( " + select_id_aluno(r.getAluno()) + " ) and " +
		"id_sala = ( " + select_id_sala(r.getRoom()) + " ) and " +
		"finalidade = \"" + r.getFinality() + "\" and " +
		"hora = \"" + r.getHora() + "\" and " +
		"data = \"" + r.getData() + "\" and " +
		"cadeiras_reservadas = " + r.getCadeiras_reservadas();
	}
	private String values_reserva_sala_aluno(ReservaSalaAluno r){
		return "( " + select_id_aluno(r.getAluno()) + " ), " +
		"( " + select_id_sala(r.getRoom()) + " ), " +
		"\"" + r.getFinality() + "\", " +
		"\"" + r.getHora() + "\", " +
		"\"" + r.getData() + "\", " +
		r.getCadeiras_reservadas();
	}
	private void insert_into(ReservaSalaAluno r){
		try {
			this.executeQuery("INSERT INTO " +
					"reserva_sala_aluno (id_aluno, id_sala, finalidade, hora, data, cadeiras_reservadas) " +
					"VALUES ( " + values_reserva_sala_aluno(r) + " );");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void delete_from(ReservaSalaAluno r){
		try {
			this.executeQuery("DELETE FROM reserva_sala_aluno " + 
								this.where_reserva_sala_aluno(r) + " ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private boolean inDB(ReservaSalaAluno r) throws SQLException{
		return this.inDBGeneric("SELECT * FROM reserva_sala_aluno " + 
								this.where_reserva_sala_aluno(r) + " ;");
	}
	private boolean inDBGeneric(String query) throws SQLException{
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
	private void executeQuery(String msg) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(msg);
		pst.executeUpdate();		
		pst.close();
		con.close();
	}

}
