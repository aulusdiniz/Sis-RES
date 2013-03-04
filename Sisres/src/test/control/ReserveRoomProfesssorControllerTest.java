package test.control;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Professor;
import model.ReserveRoomProfessor;
import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import control.ReserveRoomProfessorController;
import exception.ClienteException;
import exception.PatrimonyException;
import exception.ReserveException;

import persistence.FactoryConnection;
import persistence.ProfessorDAO;
import persistence.RoomDAO;

public class ReserveRoomProfesssorControllerTest {
	private static Room sala1;
	private static Professor professor1;
	private static Vector<ReserveRoomProfessor> vet;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		vet = ReserveRoomProfessorController.getInstance().getResProfessorSala_vet();
		sala1 = new Room("123", "Room de Aula", "120");
		professor1 = new Professor("testInstance", "040.757.021-70", "0058801", "3333-3333", "nome@email");
		
		ProfessorDAO.getInstance().include(professor1);
		RoomDAO.getInstance().incluir(sala1);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ProfessorDAO.getInstance().delete(professor1);
		RoomDAO.getInstance().excluir(sala1);
	}

	@Test
	public void testInstance() {
		assertTrue("Teste de Instancia.", ReserveRoomProfessorController.getInstance() instanceof ReserveRoomProfessorController);
	}
	@Test
	public void testSingleton() {
		assertSame("Teste de Instancia.", ReserveRoomProfessorController.getInstance(), ReserveRoomProfessorController.getInstance());
	}
	
	
	@Test
	public void testInserir() throws SQLException, ReserveException, ClienteException, PatrimonyException {
		String finalidade = "Room de Estudos";
		String data = "20/12/33";
		String hora = "9:11";
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(data, hora, sala1, finalidade, professor1);
		ReserveRoomProfessorController.getInstance().inserir(sala1, professor1, data, hora, finalidade);
		boolean resultado = this.inDB(reserva);
		boolean resultado2 = reserva.equals(vet.lastElement());
		if(resultado)
			this.delete_from(reserva);
		assertTrue("Teste de Insercao.", resultado && resultado2);
	}
	@Test
	public void testAlterar() throws ReserveException, SQLException, ClienteException, PatrimonyException {
		
		ReserveRoomProfessor reserva = new ReserveRoomProfessor("20/12/33", "9:11", sala1, "Pesquisa", professor1);
		this.insert_into(reserva);
		vet.add(reserva);
		ReserveRoomProfessor reserva2 = new ReserveRoomProfessor("20/12/33", "9:11", sala1, "Reuniao", professor1);
		ReserveRoomProfessorController.getInstance().alterar("Reuniao", vet.lastElement());
		boolean resultado = this.inDB(reserva2);
		boolean resultado2 = reserva2.equals(vet.lastElement());
		if(resultado)
			this.delete_from(reserva2);
		if(this.inDB(reserva))
			this.delete_from(reserva);
		assertTrue("Teste de Alteracao.", resultado && resultado2);
	}
	@Test
	public void testExcluir() throws ReserveException, SQLException {
		String finalidade = "Pesquisa";
		String data = "20/12/33";
		String hora = "9:11";
		ReserveRoomProfessor reserva = new ReserveRoomProfessor(data, hora, sala1, finalidade, professor1);
		this.insert_into(reserva);
		vet.add(reserva);
		ReserveRoomProfessorController.getInstance().excluir(reserva);
		boolean resultado = this.inDB(reserva);
		vet.remove(reserva);

		if(resultado)
			this.delete_from(reserva);
		assertTrue("Teste de Exclusao.", !resultado );
	}

	private String select_id_professor(Professor prof){
		return "SELECT id_professor FROM professor WHERE " +
				"professor.nome = \"" + prof.getNome() + "\" and " +
				"professor.cpf = \"" + prof.getCpf() + "\" and " +
				"professor.telefone = \"" + prof.getTelefone() + "\" and " +
				"professor.email = \"" + prof.getEmail() + "\" and " +
				"professor.matricula = \"" + prof.getMatricula() + "\"";
	}
	private String select_id_sala(Room room){
		return "SELECT id_sala FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity();
	}
	private String where_reserva_sala_professor(ReserveRoomProfessor reserva){
		return " WHERE " +
		"id_professor = ( " + select_id_professor(reserva.getProfessor()) + " ) and " +
		"id_sala = ( " + select_id_sala(reserva.getRoom()) + " ) and " +
		"finalidade = \"" + reserva.getFinality() + "\" and " +
		"hora = \"" + reserva.getHora() + "\" and " +
		"data = \"" + reserva.getData() + "\" ";
	}
	private String values_reserva_sala_professor(ReserveRoomProfessor reserva){
		return "( " + select_id_professor(reserva.getProfessor()) + " ), " +
		"( " + select_id_sala(reserva.getRoom()) + " ), " +
		"\"" + reserva.getFinality() + "\", " +
		"\"" + reserva.getHora() + "\", " +
		"\"" + reserva.getData() + "\"";
	}
	private void insert_into(ReserveRoomProfessor reserva){
		try {
			this.executeQuery("INSERT INTO " +
					"reserva_sala_professor (id_professor, id_sala, finalidade, hora, data) " +
					"VALUES ( " + values_reserva_sala_professor(reserva) + " );");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void delete_from(ReserveRoomProfessor reserva){
		try {
			this.executeQuery("DELETE FROM reserva_sala_professor " + 
								this.where_reserva_sala_professor(reserva) + " ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private boolean inDB(ReserveRoomProfessor reserva) throws SQLException{
		return this.inDBGeneric("SELECT * FROM reserva_sala_professor " + 
								this.where_reserva_sala_professor(reserva) + " ;");
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
