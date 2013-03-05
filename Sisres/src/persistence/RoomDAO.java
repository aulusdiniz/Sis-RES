package persistence;

import model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import exception.PatrimonyException;

public class RoomDAO {

		private static final String ROOM_EXISTING = "Room ja cadastrada.";
		private static final String ROOM_NOT_EXISTING = "Room nao cadastrada.";
		private static final String RESERVE_EXISTING = "Room esta sendo utilizada em uma reserva.";
		private static final String ROOM_NULL = "Room esta nula.";
		private static final String CODE_EXISTING = "Room com o mesmo codigo ja cadastrada.";
	
		private static RoomDAO instance;
		private RoomDAO(){
		}
		public static RoomDAO getInstance(){
			if(instance == null)
				instance = new RoomDAO();
			return instance;
		}

		
	public void include(Room room) throws SQLException, PatrimonyException {	
		if(room == null)
			throw new PatrimonyException(ROOM_NULL);
		else if(this.inDBCode(room.getCode()))
			throw new PatrimonyException(CODE_EXISTING);
		this.updateQuery("INSERT INTO " +
					"room (codigo, descricao, capacidade) VALUES (" +
					"\"" + room.getCode() + "\", " +
					"\"" + room.getDescription() + "\", " +
					room.getCapacity() + ");");
	}

	public void alterate(Room oldRoom, Room newRoom) throws SQLException, PatrimonyException {
		if(newRoom == null)
			throw new PatrimonyException(ROOM_NULL);
		if(oldRoom == null)
			throw new PatrimonyException(ROOM_NULL);
		
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst;
		
		if(!this.inDB(oldRoom))
			throw new PatrimonyException(ROOM_NOT_EXISTING);
		else if(this.inOtherDB(oldRoom))
			throw new PatrimonyException(RESERVE_EXISTING);
		else if(!oldRoom.getCode().equals(newRoom.getCode()) && this.inDBCode(newRoom.getCode()))
			throw new PatrimonyException(CODE_EXISTING);
		if(!this.inDB(newRoom)){
			String msg = "UPDATE room SET " +				
				"codigo = \"" + newRoom.getCode() + "\", " +
				"descricao = \"" + newRoom.getDescription() + "\", " +
				"capacidade = " + newRoom.getCapacity() +
				" WHERE " +
				"room.codigo = \"" + oldRoom.getCode() + "\" and " +
				"room.descricao = \"" + oldRoom.getDescription() +  "\" and " +
				"room.capacidade = " + oldRoom.getCapacity() +";";
			con.setAutoCommit(false);
			pst = con.prepareStatement(msg);
			pst.executeUpdate();
			con.commit();
		}
		else
			throw new PatrimonyException(ROOM_EXISTING);
		
		pst.close();
		con.close();
	}

	public void delete(Room room) throws SQLException, PatrimonyException {
		if(room == null)
			throw new PatrimonyException(ROOM_NULL);
		else if(this.inOtherDB(room))
			throw new PatrimonyException(RESERVE_EXISTING);
		else if(this.inDB(room)){
			this.updateQuery("DELETE FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity() + ";"				
				);
		}
		else
			throw new PatrimonyException(ROOM_NOT_EXISTING);
	}

	
	
	public Vector<Room> findAll() throws SQLException, PatrimonyException {
		return this.find("SELECT * FROM room;");
	}
	public Vector<Room> findByCode(String value) throws SQLException, PatrimonyException {
		return this.find("SELECT * FROM room WHERE codigo = " + "\"" + value + "\";");
	}
	public Vector<Room> findByDescription(String value) throws SQLException, PatrimonyException {
		return this.find("SELECT * FROM room WHERE descricao = " + "\"" + value + "\";");
	}
	public Vector<Room> findByCapacity(String value) throws SQLException, PatrimonyException {
		return this.find("SELECT * FROM room WHERE capacidade = " + value + ";");
	}
	
	
	private Vector<Room> find(String query) throws SQLException, PatrimonyException {
		Vector<Room> vet = new Vector<Room>();
		
		Connection con =  FactoryConnection.getInstance().getConnection();
		
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next())
			vet.add(this.fetchRoom(rs));
		
		pst.close();
		rs.close();
		con.close();
		return vet;
	}
	
	
	private boolean inDBGeneric(String query) throws SQLException{
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		if(!rs.next()) {
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
	private boolean inDBCode(String code) throws SQLException{
		return this.inDBGeneric("SELECT * FROM room WHERE " +
				"room.codigo = \"" + code + "\";");
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
	
	
	private Room fetchRoom(ResultSet rs) throws PatrimonyException, SQLException{
		return new Room(rs.getString("codigo"), rs.getString("descricao"), rs.getString("capacidade"));
	}
	
	private void updateQuery(String query) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(query);
		pst.executeUpdate();		
		pst.close();
		con.close();
	}

}
