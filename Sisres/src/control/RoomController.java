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
			//Nothing here
		}
		public static RoomController getInstance() {
		if(instance == null){
			instance = new RoomController();
		}
		else{
			//Nothing here
		}
		return instance;
	}
		
	public Vector<Room> getRoomVector() throws SQLException, PatrimonyException{
		this.roomVector = RoomDAO.getInstance().findAll();
		return this.roomVector;
	}

	public void insert(String code, String desciption, String capacity) throws PatrimonyException, SQLException {
		Room room = new Room(code, desciption, capacity);
		RoomDAO.getInstance().include(room);
		this.roomVector.add(room);
	}

	public void alterate(String code, String desciption, String capacity, Room room) throws PatrimonyException, SQLException {
		Room oldRoom = new Room(room.getCode(), room.getDescription(),
								room.getCapacity());
		room.setCode(code);
		room.setDescription(desciption);
		room.setCapacity(capacity);
		RoomDAO.getInstance().alterate(oldRoom, room);
	}

	public void delete(Room room) throws SQLException, PatrimonyException {
		RoomDAO.getInstance().delete(room);
		this.roomVector.remove(room);
	}
}
