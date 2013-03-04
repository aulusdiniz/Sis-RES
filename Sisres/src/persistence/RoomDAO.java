package persistence;

import model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import exception.PatrimonyException;

public class RoomDAO {

	//Mensagens
		private static final String SALA_JA_EXISTENTE = "Room ja cadastrada.";
		private static final String SALA_NAO_EXISTENTE = "Room nao cadastrada.";
		private static final String SALA_EM_USO = "Room esta sendo utilizada em uma reserva.";
		private static final String SALA_NULA = "Room esta nula.";
		private static final String CODIGO_JA_EXISTENTE = "Room com o mesmo codigo ja cadastrada.";
	
	//Singleton
		private static RoomDAO instance;
		private RoomDAO(){
		}
		public static RoomDAO getInstance(){
			if(instance == null)
				instance = new RoomDAO();
			return instance;
		}
	//

		
	public void include(Room room) throws SQLException, PatrimonyException {	
		if(room == null)
			throw new PatrimonyException(SALA_NULA);
		else if(this.inDBCodigo(room.getCode()))
			throw new PatrimonyException(CODIGO_JA_EXISTENTE);
		this.updateQuery("INSERT INTO " +
					"room (codigo, descricao, capacidade) VALUES (" +
					"\"" + room.getCode() + "\", " +
					"\"" + room.getDescription() + "\", " +
					room.getCapacity() + ");");
	}

	public void alterar(Room old_sala, Room new_sala) throws SQLException, PatrimonyException {
		if(new_sala == null)
			throw new PatrimonyException(SALA_NULA);
		if(old_sala == null)
			throw new PatrimonyException(SALA_NULA);
		
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst;
		
		if(!this.inDB(old_sala))
			throw new PatrimonyException(SALA_NAO_EXISTENTE);
		else if(this.inOtherDB(old_sala))
			throw new PatrimonyException(SALA_EM_USO);
		else if(!old_sala.getCode().equals(new_sala.getCode()) && this.inDBCodigo(new_sala.getCode()))
			throw new PatrimonyException(CODIGO_JA_EXISTENTE);
		if(!this.inDB(new_sala)){
			String msg = "UPDATE room SET " +				
				"codigo = \"" + new_sala.getCode() + "\", " +
				"descricao = \"" + new_sala.getDescription() + "\", " +
				"capacidade = " + new_sala.getCapacity() +
				" WHERE " +
				"room.codigo = \"" + old_sala.getCode() + "\" and " +
				"room.descricao = \"" + old_sala.getDescription() +  "\" and " +
				"room.capacidade = " + old_sala.getCapacity() +";";
			con.setAutoCommit(false);
			pst = con.prepareStatement(msg);
			pst.executeUpdate();
			con.commit();
		}
		else
			throw new PatrimonyException(SALA_JA_EXISTENTE);
		
		pst.close();
		con.close();
	}

	public void excluir(Room room) throws SQLException, PatrimonyException {
		if(room == null)
			throw new PatrimonyException(SALA_NULA);
		else if(this.inOtherDB(room))
			throw new PatrimonyException(SALA_EM_USO);
		else if(this.inDB(room)){
			this.updateQuery("DELETE FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity() + ";"				
				);
		}
		else
			throw new PatrimonyException(SALA_NAO_EXISTENTE);
	}

	
	
	public Vector<Room> buscarTodos() throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM room;");
	}
	public Vector<Room> buscarPorCodigo(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM room WHERE codigo = " + "\"" + valor + "\";");
	}
	public Vector<Room> buscarPorDescricao(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM room WHERE descricao = " + "\"" + valor + "\";");
	}
	public Vector<Room> buscarPorCapacidade(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM room WHERE capacidade = " + valor + ";");
	}
	
	
	/**
	 * Metodos Privados
	 * */
	
	private Vector<Room> buscar(String query) throws SQLException, PatrimonyException {
		Vector<Room> vet = new Vector<Room>();
		
		Connection con =  FactoryConnection.getInstance().getConnection();
		
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next())
			vet.add(this.fetchSala(rs));
		
		pst.close();
		rs.close();
		con.close();
		return vet;
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
	private boolean inDB(Room room) throws SQLException{
		return this.inDBGeneric("SELECT * FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() + "\" and " +
				"room.capacidade = " + room.getCapacity() +
				";");
	}
	private boolean inDBCodigo(String codigo) throws SQLException{
		return this.inDBGeneric("SELECT * FROM room WHERE " +
				"room.codigo = \"" + codigo + "\";");
	}
	private boolean inOtherDB(Room room) throws SQLException{
		if( this.inDBGeneric("SELECT * FROM reserva_sala_professor WHERE " +
				"id_sala = (SELECT id_sala FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity() +" );") == false)
		{
			if(this.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
							"id_sala = (SELECT id_sala FROM room WHERE " +
							"room.codigo = \"" + room.getCode() + "\" and " +
							"room.descricao = \"" + room.getDescription() +  "\" and " +
							"room.capacidade = " + room.getCapacity() +" );") == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	private Room fetchSala(ResultSet rs) throws PatrimonyException, SQLException{
		return new Room(rs.getString("codigo"), rs.getString("descricao"), rs.getString("capacidade"));
	}
	
	private void updateQuery(String msg) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(msg);
		pst.executeUpdate();		
		pst.close();
		con.close();
	}

}
