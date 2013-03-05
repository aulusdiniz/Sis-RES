package control;

import java.sql.SQLException;
import java.util.Vector;

import model.Student;
import model.ReserveStudentRoom;
import model.Room;
import persistence.ReserveStudentRoomDAO;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveStudentRoomController {

	private Vector<ReserveStudentRoom> reserveStudentRoomVector = new Vector<ReserveStudentRoom>();
	
	private static ReserveStudentRoomController instance;

	private ReserveStudentRoomController() {
	}

	public static ReserveStudentRoomController getInstance() {
		if (instance == null) {
			instance = new ReserveStudentRoomController();
		}
		return instance;
	}


	public Vector<ReserveStudentRoom> getReserveHour(String hour) throws SQLException, PatrimonyException, ClientException, ReserveException{
		
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findByHour(hour);
		return this.reserveStudentRoomVector; 
		
	}
	
	public Vector<ReserveStudentRoom> getReserveMonth(String date) throws SQLException, PatrimonyException, ClientException, ReserveException{
		
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findByDate(date);
		return this.reserveStudentRoomVector;
	}
	
	public Vector<ReserveStudentRoom> getReserveStudentRoomVector() throws SQLException, PatrimonyException, ClientException, ReserveException {
		
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findAll();
		return this.reserveStudentRoomVector;
	}

	public int availableChairs(Room room, String date, String hour) throws SQLException, PatrimonyException, ClientException, ReserveException {
		
		return ReserveStudentRoomDAO.getInstance().availableChairs(room, date, hour);
	}

	public void insert(Room room, Student student, String date,
					   String hour, String finality, 
					   String reservedChairs) throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveStudentRoom reserveStudentRoom = new ReserveStudentRoom(date, hour, room, finality, reservedChairs, student);
		ReserveStudentRoomDAO.getInstance().include(reserveStudentRoom);
		this.reserveStudentRoomVector.add(reserveStudentRoom);
	}

	public void alterate(String finality, String reservedChairs, ReserveStudentRoom reserveStudentRoom)
		throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveStudentRoom oldReserveStudentRoom = new ReserveStudentRoom(reserveStudentRoom.getDate(),
																		  reserveStudentRoom.getHour(), 
																		  reserveStudentRoom.getRoom(),
																		  reserveStudentRoom.getFinality(), 
																		  reserveStudentRoom.getReservedChairs(), 
																		  reserveStudentRoom.getStudent());
		reserveStudentRoom.setFinality(finality);
		reserveStudentRoom.setReservedChairs(reservedChairs);
		ReserveStudentRoomDAO.getInstance().alterate(oldReserveStudentRoom, reserveStudentRoom);
	}

	public void delete(ReserveStudentRoom reserveStudentRoom) throws SQLException, ReserveException {
		
		ReserveStudentRoomDAO.getInstance().delete(reserveStudentRoom);
		this.reserveStudentRoomVector.remove(reserveStudentRoom);
	}
}
