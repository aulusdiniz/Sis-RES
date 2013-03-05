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

import model.Student;
import model.ReserveStudentRoom;
import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.StudentDAO;
import persistence.FactoryConnection;
import persistence.ReserveStudentRoomDAO;
import persistence.RoomDAO;

import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveRoomStudentDAOTest {

	private static Room room1;
	private static Room room2;
	private static Student student1;
	private static Student student2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		room1= new Room("123", "Room de Aula", "120");
		room2 = new Room("543", "Laboratorio", "30");
		student1 = new Student("testInstance", "501.341.852-69", "456678", "", "");
		student2 = new Student("Incluindo Matricula Igual", "490.491.781-20", "345543", "2222-2222", "student2@email");
		
		StudentDAO.getInstance().include(student1);
		StudentDAO.getInstance().include(student2);
		RoomDAO.getInstance().include(room1);
		RoomDAO.getInstance().include(room2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		StudentDAO.getInstance().delete(student1);
		StudentDAO.getInstance().delete(student2);
		RoomDAO.getInstance().delete(room1);
		RoomDAO.getInstance().delete(room2);
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
	public void testIncluir() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		
		ReserveStudentRoomDAO.getInstance().include(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
		assertTrue("Teste de Inclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testIncluirNulo() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoomDAO.getInstance().include(null);
	}
	@Test (expected= ReserveException.class)
	public void testIncluirStudentInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", new Student("tepp", "501.341.852-69", "456678", "", ""));
		
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
		boolean resultado = this.inDB(reserva);

		if(resultado)
			this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", new Room("22277883", "Laboratorio", "120"),
				"Grupo de Estudos", "120", student1);
		
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
		boolean resultado = this.inDB(reserva);

		if(resultado)
			this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaReservadaProf() throws ReserveException, ClientException, 
											PatrimonyException, SQLException 
	{
		this.executeQuery("INSERT INTO professor (nome, cpf, matricula) " +
		"VALUES (\"ProfessorDAO\", \"257.312.954-33\", \"11009988\");");
		this.executeQuery("INSERT INTO reserva_room_professor (id_professor,id_room,finalidade,hora,data) "+
		"VALUES ((SELECT id_professor FROM professor WHERE cpf = \"257.312.954-33\")," +
				"(SELECT id_room FROM room WHERE codigo = \"123\")," +
				"\"Aula de Calculo\", \"08:00\", \"20/12/2034\");");
		
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "60", student1);
		
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
		if(this.inDB(reserva))
			this.delete_from(reserva);
		
		this.executeQuery("DELETE FROM professor WHERE cpf = \"257.312.954-33\";");
		this.executeQuery("DELETE FROM reserva_room_professor WHERE data = \"20/12/2034\";");
		
		}
		
	}
	@Test (expected= ReserveException.class)
	public void testIncluirStudentOcupado() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room2,
				"Grupo de Estudos", ""+room2.getCapacity(), student1);
		ReserveStudentRoomDAO.getInstance().include(reserva);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva2);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirSalaCheia() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "60", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "70", student2);
		ReserveStudentRoomDAO.getInstance().include(reserva);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva2);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouAno() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/1990", "8:00", room1,
				"Grupo de Estudos", "60", student1);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouMes() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/01/2013", "8:00", room1,
				"Grupo de Estudos", "60", student1);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirDataPassouDia() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom(this.dataAtualAMais(-100000000), this.horaAtual(), room1,
				"Grupo de Estudos", "60", student1);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirHoraPassouHora() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom(this.dataAtual(),
				 this.horaAtualAMais(-10000000), room1,
				"Grupo de Estudos", "60", student1);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	@Test (expected= ReserveException.class)
	public void testIncluirHoraPassouMinutos() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom(this.dataAtual(),
				this.horaAtualAMais(-100000), room1,
				"Grupo de Estudos", "60", student1);
		try{
			ReserveStudentRoomDAO.getInstance().include(reserva);
		} finally {
			if(this.inDB(reserva))
				this.delete_from(reserva);
		}
	}
	
	
	@Test
	public void testAlterar() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("21/12/34", "19:00", room1,
				"Grupo de Estudos", "120", student1);
		
		this.insert_into(reserva);
		
		ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		
		boolean resultado = this.inDB(reserva2);
		
		if(resultado)
			this.delete_from(reserva2);
		if(this.inDB(reserva))
			this.delete_from(reserva);
		assertTrue("Teste de Inclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNulo1() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		ReserveStudentRoomDAO.getInstance().alterate(null, reserva);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNulo2() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
			"Grupo de Estudos", "120", student1);
		ReserveStudentRoomDAO.getInstance().alterate(reserva, null);
	}
	@Test (expected= ReserveException.class)
	public void testAlterarInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
			if(this.inDB(reserva2))
				this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarJaInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("27/12/34", "9:00", room2,
				"Grupo d", ""+room2.getCapacity(), student2);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva2, reserva);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraDifStudentOcupado() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "9:00", room1,
				"Grupo de Estudos", "120", student1);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		ReserveStudentRoom reserva3 = new ReserveStudentRoom("20/12/34", "8:00", room2,
				"Grupo de Estudos", ""+room2.getCapacity(), student1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva2, reserva3);
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
	public void testAlterarDataDifSalaOcupado() throws ReserveException, ClientException, PatrimonyException, SQLException {
		this.executeQuery("INSERT INTO professor (nome, cpf, matricula) " +
				"VALUES (\"ProfessorDAO\", \"257.312.954-33\", \"11009988\");");
		this.executeQuery("INSERT INTO reserva_room_professor (id_professor,id_room,finalidade,hora,data) "+
				"VALUES ((SELECT id_professor FROM professor WHERE cpf = \"257.312.954-33\")," +
						"(SELECT id_room FROM room WHERE codigo = \"123\")," +
						"\"Aula de Calculo\", \"08:00\", \"20/12/2034\");");
				
		
		ReserveStudentRoom reserva = new ReserveStudentRoom("21/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		this.insert_into(reserva);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		
		this.executeQuery("DELETE FROM professor WHERE cpf = \"257.312.954-33\";");
		this.executeQuery("DELETE FROM reserva_room_professor WHERE data = \"20/12/2034\";");
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarStudentInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("21/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		this.insert_into(reserva);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", new Student("tepp", "501.341.852-69", "456678", "", ""));
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarSalaInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("21/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		this.insert_into(reserva);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", new Room("22277883", "Laboratorio", "120"),
				"Grupo de Estudos", "120", student1);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarNaoHaCadeira() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "20", student2);
		ReserveStudentRoom reserva3 = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student2);
		this.insert_into(reserva);
		this.insert_into(reserva2);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva2, reserva3);
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
	public void testAlterarDataPassouAno() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/12/1990", "8:00", room1,
				"Grupo de Estudos", "20", student2);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataPassouMes() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("20/01/2013", "8:00", room1,
				"Grupo de Estudos", "20", student2);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarDataPassouDia() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom(this.dataAtualAMais(-100000000), this.horaAtual(), room1,
				"Grupo de Estudos", "60", student1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraPassouHora() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom(this.dataAtual(),
				 this.horaAtualAMais(-10000000), room1,
				"Grupo de Estudos", "60", student1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	@Test (expected= ReserveException.class)
	public void testAlterarHoraPassouMinutos() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "30", student1);
		ReserveStudentRoom reserva2 = new ReserveStudentRoom(this.dataAtual(),
				this.horaAtualAMais(-100000), room1,
				"Grupo de Estudos", "60", student1);
		this.insert_into(reserva);
		
		try{
			ReserveStudentRoomDAO.getInstance().alterate(reserva, reserva2);
		} finally {
		
		if(this.inDB(reserva))
			this.delete_from(reserva);
		if(this.inDB(reserva2))
			this.delete_from(reserva2);
		}
	}
	
	
	
	@Test
	public void testExcluir() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);
		this.insert_into(reserva);
		
		ReserveStudentRoomDAO.getInstance().delete(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
		assertFalse("Teste de Exclusao.", resultado);
	}
	@Test (expected= ReserveException.class)
	public void testExcluirNulo() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoomDAO.getInstance().delete(null);
	}
	@Test (expected= ReserveException.class)
	public void testExcluirInexistente() throws ReserveException, ClientException, PatrimonyException, SQLException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "8:00", room1,
				"Grupo de Estudos", "120", student1);

		ReserveStudentRoomDAO.getInstance().delete(reserva);
		
		boolean resultado = this.inDB(reserva);
		
		if(resultado)
			this.delete_from(reserva);
	}
	
	
	@Test
	public void testBuscarPorDia() throws SQLException, PatrimonyException, ClientException, ReserveException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("21/12/34", "8:00", room1,
				"Grupo de Estudos", "40", student1);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("21/12/34", "19:00", room1,
				"Grupo de Estudos", "50", student1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		Vector<ReserveStudentRoom> vet = ReserveStudentRoomDAO.getInstance().findByDate("21/12/34");
		this.delete_from(reserva);
		this.delete_from(reserva2);
		
		boolean resultado = false;
		boolean resultado2 = false;
		
		Iterator<ReserveStudentRoom> it = vet.iterator();
		while(it.hasNext()){
			ReserveStudentRoom obj = it.next();
			if(obj.equals(reserva))
				resultado = true;
			else if(obj.equals(reserva2))
				resultado2 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2);
	}
	@Test
	public void testBuscarPorHora() throws SQLException, PatrimonyException, ClientException, ReserveException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("20/12/34", "9:00", room1,
				"Grupo de Estudos", "40", student1);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("21/12/34", "09:00", room1,
				"Grupo de Estudos", "50", student1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		Vector<ReserveStudentRoom> vet = ReserveStudentRoomDAO.getInstance().findByHour("09:00");
		this.delete_from(reserva);
		this.delete_from(reserva2);
		
		boolean resultado = false;
		boolean resultado2 = false;
		
		Iterator<ReserveStudentRoom> it = vet.iterator();
		while(it.hasNext()){
			ReserveStudentRoom obj = it.next();
			if(obj.equals(reserva))
				resultado = true;
			else if(obj.equals(reserva2))
				resultado2 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2);
	}
	
	
	@Test
	public void testCadeirasDisponiveis() throws SQLException, PatrimonyException, ClientException, ReserveException {
		ReserveStudentRoom reserva = new ReserveStudentRoom("21/12/34", "19:00", room1,
				"Grupo de Estudos", "40", student1);
		
		ReserveStudentRoom reserva2 = new ReserveStudentRoom("21/12/34", "19:00", room1,
				"Grupo de Estudos", "50", student1);
		
		this.insert_into(reserva);
		this.insert_into(reserva2);
		int c = ReserveStudentRoomDAO.getInstance().availableChairs(room1, "21/12/34", "19:00");
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
	
	
	private String select_id_student(Student a){
		return "SELECT id_student FROM student WHERE " +
				"student.nome = \"" + a.getName() + "\" and " +
				"student.cpf = \"" + a.getCpf() + "\" and " +
				"student.telefone = \"" + a.getPhone() + "\" and " +
				"student.email = \"" + a.getEmail() + "\" and " +
				"student.matricula = \"" + a.getRegistration() + "\"";
	}
	private String select_id_room(Room room){
		return "SELECT id_room FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity();
	}
	private String where_reserva_room_student(ReserveStudentRoom r){
		return " WHERE " +
		"id_student = ( " + select_id_student(r.getStudent()) + " ) and " +
		"id_room = ( " + select_id_room(r.getRoom()) + " ) and " +
		"finalidade = \"" + r.getFinality() + "\" and " +
		"hora = \"" + r.getHour() + "\" and " +
		"data = \"" + r.getDate() + "\" and " +
		"cadeiras_reservadas = " + r.getReservedChairs();
	}
	private String values_reserva_room_student(ReserveStudentRoom r){
		return "( " + select_id_student(r.getStudent()) + " ), " +
		"( " + select_id_room(r.getRoom()) + " ), " +
		"\"" + r.getFinality() + "\", " +
		"\"" + r.getHour() + "\", " +
		"\"" + r.getDate() + "\", " +
		r.getReservedChairs();
	}
	private void insert_into(ReserveStudentRoom r){
		try {
			this.executeQuery("INSERT INTO " +
					"reserva_room_student (id_student, id_room, finalidade, hora, data, cadeiras_reservadas) " +
					"VALUES ( " + values_reserva_room_student(r) + " );");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void delete_from(ReserveStudentRoom r){
		try {
			this.executeQuery("DELETE FROM reserva_room_student " + 
								this.where_reserva_room_student(r) + " ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private boolean inDB(ReserveStudentRoom r) throws SQLException{
		return this.inDBGeneric("SELECT * FROM reserva_room_student " + 
								this.where_reserva_room_student(r) + " ;");
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
