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
	
	public Vector<ReserveStudentRoom> getReservasMes(String data) throws SQLException, PatrimonyException, ClientException, ReserveException{
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findByDate(data);
		return this.reserveStudentRoomVector;
	}
	
	public Vector<ReserveStudentRoom> getResAlunoSala_vet() throws SQLException, PatrimonyException, ClientException, ReserveException {
		this.reserveStudentRoomVector = ReserveStudentRoomDAO.getInstance().findAll();
		return this.reserveStudentRoomVector;
	}

	public int cadeirasDisponveis(Room room, String data, String hour) throws SQLException, PatrimonyException, ClientException, ReserveException {
		return ReserveStudentRoomDAO.getInstance().availableChairs(room, data, hour);
	}

	public void inserir(Room room, Student student,
		String data, String hour, String finalidade, String cadeiras_reservadas)
		throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveStudentRoom r = new ReserveStudentRoom(data, hour, room, finalidade, cadeiras_reservadas, student);
		ReserveStudentRoomDAO.getInstance().include(r);
		this.reserveStudentRoomVector.add(r);
	}

	public void alterar(String finalidade, String cadeiras_reservadas, ReserveStudentRoom reserveStudentRoom)
		throws SQLException, ReserveException, ClientException, PatrimonyException {

		ReserveStudentRoom oldReserveStudentRoom = new ReserveStudentRoom(reserveStudentRoom.getDate(), reserveStudentRoom.getHour(), reserveStudentRoom.getRoom(),
			reserveStudentRoom.getFinality(), reserveStudentRoom.getReservedChairs(), reserveStudentRoom.getStudent());
		reserveStudentRoom.setFinality(finalidade);
		reserveStudentRoom.setReservedChairs(cadeiras_reservadas);
		ReserveStudentRoomDAO.getInstance().alterate(oldReserveStudentRoom, reserveStudentRoom);
	}

	public void excluir(ReserveStudentRoom r) throws SQLException, ReserveException {
		ReserveStudentRoomDAO.getInstance().delete(r);
		this.reserveStudentRoomVector.remove(r);
	}
}
