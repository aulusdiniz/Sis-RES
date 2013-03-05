package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.RoomDAO;
import exception.PatrimonyException;
import model.Room;

public class RoomController {

	private Vector<Room> roomVector = new Vector<Room>();
	
		private static RoomController instance;
		private RoomController() {
		}
		public static RoomController getInstance() {
		if(instance == null)
			instance = new RoomController();
		return instance;
	}
		
	public Vector<Room> getRoomVector() throws SQLException, PatrimonyException{
		this.roomVector = RoomDAO.getInstance().findAll();
		return this.roomVector;
	}

	public void insert(String codigo, String descricao, String capacidade) throws PatrimonyException, SQLException {
		Room room = new Room(codigo, descricao, capacidade);
		RoomDAO.getInstance().include(room);
		this.roomVector.add(room);
	}

	public void alterate(String codigo, String descricao, String capacidade, Room room) throws PatrimonyException, SQLException {
		Room old_sala = new Room(room.getCode(), room.getDescription(),
								room.getCapacity());
		room.setCode(codigo);
		room.setDescription(descricao);
		room.setCapacity(capacidade);
		RoomDAO.getInstance().alterate(old_sala, room);
	}

	public void delete(Room room) throws SQLException, PatrimonyException {
		RoomDAO.getInstance().delete(room);
		this.roomVector.remove(room);
	}
}
