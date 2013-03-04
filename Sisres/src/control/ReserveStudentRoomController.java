package control;

import java.sql.SQLException;
import java.util.Vector;

import model.Student;
import model.ReserveRoomStudent;
import model.Room;
import persistence.ReserveStudentRoomDAO;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveStudentRoomController {

	private Vector<ReserveRoomStudent> reserveStudentRoomVector = new Vector<ReserveRoomStudent>();
	
	private static ReserveStudentRoomController instance;

	private ReserveStudentRoomController() {
	}

	public static ReserveStudentRoomController getInstance() {
		if (instance == null) {
			instance = new ReserveStudentRoomController();
		}
		return instance;
	}


	public Vector<ReserveRoomStudent> getReserveHour(String hour) throws SQLException, PatrimonyException, ClientException, ReserveException{
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findByHour(hour);
		return this.reserveStudentRoomVector; 
		
	}
	
	public Vector<ReserveRoomStudent> getReserveMonth(String data) throws SQLException, PatrimonyException, ClientException, ReserveException{
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findByDate(data);
		return this.reserveStudentRoomVector;
	}
	
	public Vector<ReserveRoomStudent> getReserveStudentRoomVector() throws SQLException, PatrimonyException, ClientException, ReserveException {
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findAll();
		return this.reserveStudentRoomVector;
	}

	public int availableChairs(Room room, String data, String hour) throws SQLException, PatrimonyException, ClientException, ReserveException {
		return ReserveStudentRoomDAO.getInstance().availableChairs(room, data, hour);
	}

	public void insert(Room room, Student student,
		String data, String hour, String finalidade, String cadeiras_reservadas)
		throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveRoomStudent r = new ReserveRoomStudent(data, hour, room, finalidade, cadeiras_reservadas, student);
		ReserveStudentRoomDAO.getInstance().include(r);
		this.reserveStudentRoomVector.add(r);
	}

	public void alterate(String finalidade, String cadeiras_reservadas, ReserveRoomStudent reserveRoomStudent)
		throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveRoomStudent oldReserveStudentRoom = new ReserveRoomStudent(reserveRoomStudent.getDate(), reserveRoomStudent.getHour(), reserveRoomStudent.getRoom(),
			reserveRoomStudent.getFinality(), reserveRoomStudent.getReservedChairs(), reserveRoomStudent.getStudent());
		reserveRoomStudent.setFinality(finalidade);
		reserveRoomStudent.setReservedChairs(cadeiras_reservadas);
		ReserveStudentRoomDAO.getInstance().alterate(oldReserveStudentRoom, reserveRoomStudent);
	}

	public void delete(ReserveRoomStudent r) throws SQLException, ReserveException {
		ReserveStudentRoomDAO.getInstance().delete(r);
		this.reserveStudentRoomVector.remove(r);
	}
}
