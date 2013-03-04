package test.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Student;
import model.ReserveRoomStudent;
import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveRoomStudentTest{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testInstance() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		assertTrue("Teste de Instancia.", reserva instanceof ReserveRoomStudent);
	}
	
	
	
	@Test (expected= ReserveException.class)
	public void testStudentNulo() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = null;
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", "30", aluno);
	}
	
	
	
	@Test (expected= ReserveException.class)
	public void testCadeirasNula() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", null, aluno);
	}
	
	@Test (expected= ReserveException.class)
	public void testCadeirasVazias() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", "     ", aluno);
	}
	
	@Test (expected= ReserveException.class)
	public void testCadeirasDespadronizadas() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", "3A-", aluno);
	}
	
	@Test (expected= ReserveException.class)
	public void testCadeirasAcimaCapacidade() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", "121", aluno);
	}
	
	
	
	@Test (expected= ReserveException.class)
	public void testFinalidadeNula() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, null, "11", aluno);
	}
	@Test (expected= ReserveException.class)
	public void testFinalidadeVazia() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "     ", "11", aluno);
	}
	
	
	
	@Test (expected= ReserveException.class)
	public void testSalaNula() throws PatrimonyException, ClientException, ReserveException {
		Room room = null;
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room, "Grupo de Estudos", "30", aluno);
	}
	
	
	
	@Test
	public void testHora() throws PatrimonyException, ClientException, ReserveException {
		String hora = this.horaAtualAMais(100000000);
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(),
				hora, room,
				"Grupo de Estudos", "120", aluno);
		assertTrue("", reserva.getHour() == hora);
	}
	@Test (expected= ReserveException.class)
	public void testHoraNula() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), null, room, "Grupo de Estudos", "120", aluno);
	}
	@Test (expected= ReserveException.class)
	public void testHoraVazia() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), "    ", room, "Grupo de Estudos", "120", aluno);
	}
	@Test (expected= ReserveException.class)
	public void testHoraDespadronizada() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(this.dataAtual(), "1000", room, "Grupo de Estudos", "120", aluno);
	}
	
	
	
	@Test
	public void testData() throws PatrimonyException, ClientException, ReserveException {
		String data = "12/2/33";
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(data,
				"8:00", room, "Grupo de Estudos", "120", aluno);

		assertTrue("", reserva.getDate().equals("12/02/2033"));
	}
	@Test (expected= ReserveException.class)
	public void testDataNula() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent(null, this.horaAtual(), room, "Grupo de Estudos", "120", aluno);
	}
	@Test (expected= ReserveException.class)
	public void testDataVazia() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent("    ", this.horaAtual(), room, "Grupo de Estudos", "120", aluno);
	}
	@Test (expected= ReserveException.class)
	public void testDataDespadronizada() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomStudent("12/q2/2030", this.horaAtual(), room, "Grupo de Estudos", "120", aluno);
	}
	
	
	@Test
	public void testEqualsTrue() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		assertTrue("Teste de Equals.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseSala() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Room sala2 = new Room("1233", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), sala2,
				"Grupo de Estudos", "120", aluno);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseStudent() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		Student aluno2 = new Student("testInstanceD", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno2);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseData() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtualAMais(100000000), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseHora() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtualAMais(10000000), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseFinalidade() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos So q n", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseCadierasReservadas() throws PatrimonyException, ClientException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Student aluno = new Student("testInstance", "501.341.852-69", "456678", "", "");
		ReserveRoomStudent reserva = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "120", aluno);
		ReserveRoomStudent reserva2 = new ReserveRoomStudent(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", "1", aluno);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
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
}
