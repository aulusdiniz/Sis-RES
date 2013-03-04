package test.control;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import model.Aluno;
import model.ReservaSalaAluno;
import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import control.ReserveStudentRoomController;

import exception.ClienteException;
import exception.PatrimonyException;
import exception.ReserveException;

import persistence.AlunoDAO;
import persistence.FactoryConnection;
import persistence.RoomDAO;

public class ReserveRoomStudentControllerTest {
	private static Room sala1;
	private static Aluno aluno1;
	private static Vector<ReservaSalaAluno> vet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		vet = ReserveStudentRoomController.getInstance().getResAlunoSala_vet();
		sala1 = new Room("123", "Room de Aula", "120");
		aluno1 = new Aluno("testInstance", "501.341.852-69", "456678", "", "");
		
		AlunoDAO.getInstance().include(aluno1);
		RoomDAO.getInstance().incluir(sala1);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		AlunoDAO.getInstance().delete(aluno1);
		RoomDAO.getInstance().excluir(sala1);
	}

	
	
	@Test
	public void testInstance() {
		assertTrue("Teste de Instancia.", ReserveStudentRoomController.getInstance() instanceof ReserveStudentRoomController);
	}
	@Test
	public void testSingleton() {
		assertSame("Teste de Instancia.", ReserveStudentRoomController.getInstance(), ReserveStudentRoomController.getInstance());
	}
	
	
	@Test
	public void testInserir() throws SQLException, ReserveException, ClienteException, PatrimonyException {
		String cadeiras_reservadas = "120";
		String finalidade = "Room de Estudos";
		String data = "20/12/33";
		String hora = "9:11";
		ReservaSalaAluno r = new ReservaSalaAluno(data, hora, sala1, finalidade, cadeiras_reservadas, aluno1);
		ReserveStudentRoomController.getInstance().inserir(sala1, aluno1, data, hora, finalidade, cadeiras_reservadas);
		boolean resultado = this.inDB(r);
		boolean resultado2 = r.equals(vet.lastElement());
		if(resultado)
			this.delete_from(r);
		assertTrue("Teste de Insercao.", resultado && resultado2);
	}
	@Test
	public void testAlterar() throws ReserveException, SQLException, ClienteException, PatrimonyException {
		String cadeiras_reservadas = "120";
		String finalidade = "Room de Estudos";
		String data = "20/12/33";
		String hora = "9:11";
		ReservaSalaAluno r = new ReservaSalaAluno(data, hora, sala1, finalidade, cadeiras_reservadas, aluno1);
		this.insert_into(r);
		vet.add(r);
		ReservaSalaAluno r2 = new ReservaSalaAluno(data, hora, sala1, finalidade, "100", aluno1);
		ReserveStudentRoomController.getInstance().alterar(finalidade, "100", vet.lastElement());
		boolean resultado = this.inDB(r2);
		boolean resultado2 = r2.equals(vet.lastElement());
		if(resultado)
			this.delete_from(r2);
		if(this.inDB(r))
			this.delete_from(r);
		assertTrue("Teste de Alteracao.", resultado && resultado2);
	}
	@Test
	public void testExcluir() throws ReserveException, SQLException {
		String cadeiras_reservadas = "120";
		String finalidade = "Room de Estudos";
		String data = "20/12/33";
		String hora = "9:11";
		ReservaSalaAluno r = new ReservaSalaAluno(data, hora, sala1, finalidade, cadeiras_reservadas, aluno1);
		this.insert_into(r);
		vet.add(r);
		ReserveStudentRoomController.getInstance().delete(r);
		boolean resultado = this.inDB(r);
		boolean resultado2 = true;
		if(vet.size() > 0)
			resultado2 = !r.equals(vet.lastElement());
		if(resultado)
			this.delete_from(r);
		assertTrue("Teste de Alteracao.", !resultado && resultado2);
	}
	
	@Test
	public void testVetDia() throws SQLException, ReserveException, ClienteException, PatrimonyException {
		Aluno aluno2 = new Aluno("testInswewee", "490.491.781-20", "4324678", "", "");
		ReservaSalaAluno r = new ReservaSalaAluno("1/3/20", "9:11", sala1, "Room de Estudos", "60", aluno1);
		ReservaSalaAluno r2 = new ReservaSalaAluno("1/3/20", "9:11", sala1,"Room de Estudos", "30", aluno2);
		ReservaSalaAluno r3 = new ReservaSalaAluno("1/3/20", "10:00", sala1,"Room de Estudos", "120", aluno1);
		AlunoDAO.getInstance().include(aluno2);
		this.insert_into(r);
		this.insert_into(r2);
		this.insert_into(r3);
		Vector<ReservaSalaAluno> vet2 = ReserveStudentRoomController.getInstance().getReservasMes("1/3/20");
		this.delete_from(r);
		this.delete_from(r2);
		this.delete_from(r3);
		AlunoDAO.getInstance().delete(aluno2);
		boolean resultado = false;
		boolean resultado2 = false;
		boolean resultado3 = false;
		
		Iterator<ReservaSalaAluno> it = vet2.iterator();
		while(it.hasNext()){
			ReservaSalaAluno obj = it.next();
			if(obj.equals(r))
				resultado = true;
			else if(obj.equals(r2))
				resultado2 = true;
			else if(obj.equals(r3))
				resultado3 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2 && resultado3);
	}
	
	@Test
	public void testVetDiaHoje() throws SQLException, ReserveException, ClienteException, PatrimonyException {
		Aluno aluno2 = new Aluno("testInswewee", "490.491.781-20", "4324678", "", "");
		ReservaSalaAluno r = new ReservaSalaAluno("26/02/2013", "20:00", sala1, "Room de Estudos", "60", aluno1);
		ReservaSalaAluno r2 = new ReservaSalaAluno("26/02/2013", "20:00", sala1,"Room de Estudos", "30", aluno2);
		ReservaSalaAluno r3 = new ReservaSalaAluno("26/02/2013", "21:00", sala1,"Room de Estudos", "120", aluno1);
		AlunoDAO.getInstance().include(aluno2);
		this.insert_into(r);
		this.insert_into(r2);
		this.insert_into(r3);
		Vector<ReservaSalaAluno> vet2 = ReserveStudentRoomController.getInstance().getReservasMes("26/02/2013");
		this.delete_from(r);
		this.delete_from(r2);
		this.delete_from(r3);
		AlunoDAO.getInstance().delete(aluno2);
		boolean resultado = false;
		boolean resultado2 = false;
		boolean resultado3 = false;
		
		Iterator<ReservaSalaAluno> it = vet2.iterator();
		while(it.hasNext()){
			ReservaSalaAluno obj = it.next();
			if(obj.equals(r))
				resultado = true;
			else if(obj.equals(r2))
				resultado2 = true;
			else if(obj.equals(r3))
				resultado3 = true;
		}
		
		assertTrue("Teste de busca", resultado && resultado2 && resultado3);
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
