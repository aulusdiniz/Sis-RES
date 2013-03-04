package test.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Professor;
import model.ReserveRoomProfessor;
import model.Room;

import org.junit.Test;

import exception.ClienteException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReservaSalaProfessorTest {

	
	@Test
	public void testInstance() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor);
		assertTrue("Teste de Instancia.", reserva instanceof ReserveRoomProfessor);
	}

	
	
	
	@Test (expected= ReserveException.class)
	public void testProfessorNulo() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = null;
		new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room, "Pesquisa", professor);
	}
	
	@Test (expected= ReserveException.class)
	public void testFinalidadeNula() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room, null, professor);
	}
	@Test (expected= ReserveException.class)
	public void testFinalidadeVazia() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room, "     ", professor);
	}
	
	
	
	@Test (expected= ReserveException.class)
	public void testSalaNula() throws PatrimonyException, ClienteException, ReserveException {
		Room room = null;
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room, "Pesquisa", professor);
	}
	
	
	
	@Test
	public void testHora() throws PatrimonyException, ClienteException, ReserveException {
		String hora = this.horaAtualAMais(100000000);
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(),
				hora, room, "Reuniao", professor);
		assertTrue("", reserva.getHora() == hora);
	}
	@Test (expected= ReserveException.class)
	public void testHoraNula() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), null, room, "Reuniao", professor);
	}
	@Test (expected= ReserveException.class)
	public void testHoraVazia() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), "    ", room, "Pesquisa", professor);
	}
	@Test (expected= ReserveException.class)
	public void testHoraDespadronizada() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(this.dataAtual(), "1000", room, "Reuniao", professor);
	}
	
	@Test
	public void testData() throws PatrimonyException, ClienteException, ReserveException {
		String data = "12/2/33";
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(data,
				this.horaAtual(), room, "Aula de DS", professor);

		assertTrue("", reserva.getData().equals("12/02/2033"));
	}
	@Test (expected= ReserveException.class)
	public void testDataNula() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor(null, this.horaAtual(), room, "Aula de C1", professor);
	}
	@Test (expected= ReserveException.class)
	public void testDataVazia() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		new ReserveRoomProfessor("    ", this.horaAtual(), room, "Aula de fisica", professor);
	}
	
	@Test (expected= ReserveException.class)
	public void testDataComChar() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "501.341.852-69", "456678", "", "");
		new ReserveRoomProfessor("12/q2/2030", this.horaAtual(), room, "Grupo de Estudos", professor);
	}
	
	@Test
	public void testEqualsTrue() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reforco", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reforco", professor);
		assertTrue("Teste de Equals.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseSala() throws PatrimonyException, ClienteException, ReserveException {//mesma reserva mas em salas dif
		Room room = new Room("123", "Room de Aula", "120");
		Room sala2 = new Room("1233", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), sala2,
				"Reuniao", professor);
		
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseProfessor() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		Professor professor2 = new Professor("testInstanceD", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor2);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseData() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtualAMais(100000000), this.horaAtual(), room,
				"Grupo de Estudos", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Grupo de Estudos", professor);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseHora() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtualAMais(10000000), room,
				"Reuniao", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor);
		assertFalse("Teste de Equals False.", reserva.equals(reserva2));
	}
	@Test
	public void testEqualsFalseFinalidade() throws PatrimonyException, ClienteException, ReserveException {
		Room room = new Room("123", "Room de Aula", "120");
		Professor professor = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "Node@email");
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Reuniao", professor);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor(this.dataAtual(), this.horaAtual(), room,
				"Pesquisa", professor);
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
