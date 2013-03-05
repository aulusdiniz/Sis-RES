package test.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Room;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import persistence.FactoryConnection;
import persistence.RoomDAO;
import exception.PatrimonyException;


public class RoomDAOTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	
	@Test
	public void testInstance() {
		assertTrue("Instanciando RoomDAO", RoomDAO.getInstance() instanceof RoomDAO);
	}
	@Test
	public void testSingleton() {
		RoomDAO inst1 = RoomDAO.getInstance();
		RoomDAO inst2 = RoomDAO.getInstance();
		assertSame("Testando o Padrao Singleton", inst2, inst1);
	}
	

	@Test
	public void testIncluir() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		boolean rs = false;
		
		RoomDAO.getInstance().include(s);
		
		rs = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() + "\" and " +
				"room.capacidade = " + s.getCapacity() +
				";");
		
		if(rs)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		assertTrue("Testando Inclusao no Banco", rs);
	}
	@Test (expected= PatrimonyException.class)
	public void testIncluirNulo() throws PatrimonyException, SQLException {
		RoomDAO.getInstance().include(null);
	}
	@Test (expected= PatrimonyException.class)
	public void testIncluirCodigoExistente() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		Room s2 = new Room("CodigoInc", "Descricao Dois", "200");
		boolean rs = false;
		
		RoomDAO.getInstance().include(s2);
		try{
			RoomDAO.getInstance().include(s);
		} finally {
			rs = this.estaNoBanco("SELECT * FROM room WHERE " +
					"room.codigo = \"" + s.getCode() + "\" and " +
					"room.descricao = \"" + s.getDescription() + "\" and " +
					"room.capacidade = " + s.getCapacity() +
					";");
			if(rs)
				this.executaNoBanco("DELETE FROM room WHERE " +
						"room.codigo = \"" + s.getCode() + "\" and " +
						"room.descricao = \"" + s.getDescription() +  "\" and " +
						"room.capacidade = " + s.getCapacity() + ";");
			this.executaNoBanco("DELETE FROM room WHERE " +
					"room.codigo = \"" + s2.getCode() + "\" and " +
					"room.descricao = \"" + s2.getDescription() +  "\" and " +
					"room.capacidade = " + s2.getCapacity() + ";");
		}
		assertFalse("Teste de Inclusão.", rs);
	}
	
	@Test
	public void testAlerar() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		Room s2 = new Room("CodigoAlt", "Descricao Dois", "200");
		boolean rs = true, rs2 = false;
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		RoomDAO.getInstance().alterate(s, s2);
		
		rs = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() + "\" and " +
				"room.capacidade = " + s.getCapacity() +
				";");
		
		if(rs)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		
		rs2 = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() + "\" and " +
				"room.capacidade = " + s2.getCapacity() +
				";");
		if(rs2)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() +  "\" and " +
				"room.capacidade = " + s2.getCapacity() + ";");
		
		assertTrue("Testando Inclusao no Banco", rs2 && !rs);
	}
	@Test (expected= PatrimonyException.class)
	public void testAletarPrimeiroNulo() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		RoomDAO.getInstance().alterate(null, s);
	}
	@Test (expected= PatrimonyException.class)
	public void testAletarSegundoNulo() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		RoomDAO.getInstance().alterate(s, null);
	}
	@Test (expected= PatrimonyException.class)
	public void testAletarNaoExistente() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		Room s2 = new Room("CodigoAlt", "Descricao Dois", "200");
		boolean rs2 = true;
		
		try{
			RoomDAO.getInstance().alterate(s, s2);
		} finally {		
		
		rs2 = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() + "\" and " +
				"room.capacidade = " + s2.getCapacity() +
				";");
		if(rs2)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() +  "\" and " +
				"room.capacidade = " + s2.getCapacity() + ";");
		}
		assertTrue("Testando Inclusao no Banco", !rs2);
	}
	@Ignore // (expected= PatrimonioException.class)
	public void testAletarEvolvidoEmReserva() throws PatrimonyException, SQLException {
	}
	@Test (expected= PatrimonyException.class)
	public void testAletarComMesmoCodigo() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		Room s2 = new Room("CodigoAlt", "Descricao Dois", "200");
		Room s3 = new Room("CodigoInc", "Descricao Dois", "200");
		boolean rs = false, rs2 = false, rs3 = true;
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s2.getCode() + "\", " +
				"\"" + s2.getDescription() + "\", " +
				s2.getCapacity() + ");");
		
		try{
			RoomDAO.getInstance().alterate(s, s2);
		} finally {
		
		rs = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() + "\" and " +
				"room.capacidade = " + s.getCapacity() +
				";");
		
		if(rs)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		
		rs2 = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() + "\" and " +
				"room.capacidade = " + s2.getCapacity() +
				";");
		if(rs2)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() +  "\" and " +
				"room.capacidade = " + s2.getCapacity() + ";");
		
		rs3 = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s3.getCode() + "\" and " +
				"room.descricao = \"" + s3.getDescription() + "\" and " +
				"room.capacidade = " + s3.getCapacity() +
				";");
		if(rs3)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s3.getCode() + "\" and " +
				"room.descricao = \"" + s3.getDescription() +  "\" and " +
				"room.capacidade = " + s3.getCapacity() + ";");
		}
		assertTrue("Testando Inclusao no Banco", rs && rs2 && !rs3);
	}
	@Test (expected= PatrimonyException.class)
	public void testAletarParaExistente() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoAlt", "Descricao Dois", "200");
		Room s2 = new Room("CodigoAlt", "Descricao Dois", "200");
		boolean rs = false, rs2 = true;
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		try{
			RoomDAO.getInstance().alterate(s, s2);
		} finally {
		
		rs = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() + "\" and " +
				"room.capacidade = " + s.getCapacity() +
				";");
		
		if(rs)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		rs2 = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() + "\" and " +
				"room.capacidade = " + s2.getCapacity() +
				";");
		if(rs2)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s2.getCode() + "\" and " +
				"room.descricao = \"" + s2.getDescription() +  "\" and " +
				"room.capacidade = " + s2.getCapacity() + ";");
		}
		assertTrue("Testando Inclusao no Banco", rs && !rs2);
	}
	
	
	@Test
	public void testExcluir() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		boolean rs = true;
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		RoomDAO.getInstance().delete(s);
		
		rs = this.estaNoBanco("SELECT * FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() + "\" and " +
				"room.capacidade = " + s.getCapacity() +
				";");
		
		if(rs)
			this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		assertTrue("Testando Inclusao no Banco", !rs);
	}
	@Test (expected= PatrimonyException.class)
	public void testExcluirNulo() throws PatrimonyException, SQLException {
		RoomDAO.getInstance().delete(null);
	}
	@Ignore // (expected= PatrimonioException.class)
	public void testExcluirEnvolvidoEmReserva() throws PatrimonyException, SQLException {
		
	}
	@Test (expected= PatrimonyException.class)
	public void testExcluirNaoExistente() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		RoomDAO.getInstance().delete(s);
	}
	
	
	@Test
	public void testBuscarCodigo() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		Vector<Room> vet = RoomDAO.getInstance().buscarPorCodigo("CodigoInc");
		
		this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		assertTrue("Testando Buscar o Vetor de ", vet.size() > 0);
	}
	@Test
	public void testDescricao() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		Vector<Room> vet = RoomDAO.getInstance().buscarPorDescricao("Descricao Da Room Inclusao");
		
		this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		assertTrue("Testando Buscar o Vetor de ", vet.size() > 0);
	}
	@Test
	public void testCapacidade() throws PatrimonyException, SQLException {
		Room s = new Room("CodigoInc", "Descricao Da Room Inclusao", "123");
		
		this.executaNoBanco("INSERT INTO " +
				"room (codigo, descricao, capacidade) VALUES (" +
				"\"" + s.getCode() + "\", " +
				"\"" + s.getDescription() + "\", " +
				s.getCapacity() + ");");
		
		Vector<Room> vet = RoomDAO.getInstance().buscarPorCapacidade("123");
		
		this.executaNoBanco("DELETE FROM room WHERE " +
				"room.codigo = \"" + s.getCode() + "\" and " +
				"room.descricao = \"" + s.getDescription() +  "\" and " +
				"room.capacidade = " + s.getCapacity() + ";");
		
		assertTrue("Testando Buscar o Vetor de ", vet.size() > 0);
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
