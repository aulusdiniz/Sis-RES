package test.control;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.FactoryConnection;
import control.RoomController;
import model.Room;
import exception.PatrimonyException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;


public class RoomControllerTest {
	
	@BeforeClass
	public static void setUpClass(){
		
	}

	@AfterClass
	public static void tearDownClass(){
	}
	
	@Test
	public void testGetInstance() {
		assertTrue("Verifica metodo getInstance().", RoomController.getInstance() instanceof RoomController);
	}
	
	@Test
	public void testSingleton() {
		RoomController p = RoomController.getInstance();
		RoomController q = RoomController.getInstance();
		assertSame("Testando o Padrao Singleton", p, q);
	}


	@Test
	public void testInserir() throws PatrimonyException, SQLException {
		Room sala_new = new Room("codigo", "descricao", "2");
		RoomController.getInstance().inserir("codigo", "descricao", "2");
		assertNotNull("Falha ao inserir", this.procurarNoVetor(sala_new));
		this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + sala_new.getCode() + "\" and " +
				"room.descricao = \"" + sala_new.getDescription() +  "\" and " +
				"room.capacidade = " + sala_new.getCapacity() + ";"
				);
	}

	@Test
	public void testAlterar() throws PatrimonyException, SQLException {
		Room room = new Room("codigo_old", "descricao", "1");
		Room sala_new = new Room("codigo", "descricao", "2");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + room.getCode() + "\", " +
				"\"" + room.getDescription() + "\", " +
				"" + room.getCapacity() + "); "
				);
		RoomController.getInstance().alterar("codigo", "descricao", "2", room);
		
		assertNotNull("Falha ao alterar", this.procurarNoVetor(sala_new));
		
		this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + sala_new.getCode() + "\" and " +
				"room.descricao = \"" + sala_new.getDescription() +  "\" and " +
				"room.capacidade = " + sala_new.getCapacity() + ";"
				);
	}

	@Test
	public void testExcluir() throws SQLException, PatrimonyException {
		Room room = new Room("codigo_old", "descricao", "1");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + room.getCode() + "\", " +
				"\"" + room.getDescription() + "\", " +
				"" + room.getCapacity() + "); "
				);
		
		RoomController.getInstance().excluir(room);
		
		assertNull("Falha ao excluir", this.procurarNoVetor(room));
	}

	public Room procurarNoVetor(Room teste) throws PatrimonyException, SQLException {
		Vector<Room> todos = RoomController.getInstance().getSalas_vet();
		Iterator<Room> i = todos.iterator();
		while(i.hasNext()){
			Room e = i.next();
			if(e.equals(teste))
				return e;			
		}
		return null;
	}
	
	private void executaNoBanco(String msg) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(msg);
		pst.executeUpdate();
		pst.close();
		con.close();
	}
	
}
