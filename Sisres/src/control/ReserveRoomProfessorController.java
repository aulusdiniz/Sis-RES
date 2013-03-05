package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.ReserveProfessorRoomDAO;

import model.Professor;
import model.ReserveRoomProfessor;
import model.Room;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveRoomProfessorController {
	private Vector<ReserveRoomProfessor> reserveRoomProfessorVector = new Vector<ReserveRoomProfessor>();
	
		private static ReserveRoomProfessorController instance;
		private ReserveRoomProfessorController() {
		}
		public static ReserveRoomProfessorController getInstance() {
		if(instance == null)
			instance = new ReserveRoomProfessorController();
		return instance;
	}
		
		public Vector<ReserveRoomProfessor> findByDate(String data) throws SQLException, ClientException, PatrimonyException, ReserveException{
			this.reserveRoomProfessorVector =  ReserveProfessorRoomDAO.getInstance().findByDate(data);
			return this.reserveRoomProfessorVector;
	    } 
	    	
		
	public Vector<ReserveRoomProfessor> getReserveProfessorRoomVector() throws SQLException, ClientException, PatrimonyException, ReserveException {
		this.reserveRoomProfessorVector = ReserveProfessorRoomDAO.getInstance().findAll();
		return this.reserveRoomProfessorVector;
	}

	public void insert(Room room, Professor prof,
						String data, String hora, String finalidade) 
					throws SQLException, ReserveException {

		ReserveRoomProfessor reserva = new ReserveRoomProfessor(data, hora, room , finalidade, prof);
		ReserveProfessorRoomDAO.getInstance().include(reserva);
		this.reserveRoomProfessorVector.add(reserva);
	}

	public void alterate(String finalidade, ReserveRoomProfessor reserva) 
				throws SQLException, ReserveException {
		
		ReserveRoomProfessor reserva_old = new ReserveRoomProfessor(reserva.getDate(), reserva.getHour(), reserva.getRoom() , 
				reserva.getFinality(), reserva.getProfessor());
		
		reserva.setFinality(finalidade);
		ReserveProfessorRoomDAO.getInstance().alterate(reserva_old, reserva);
		
	}

	public void delete(ReserveRoomProfessor reserva) throws SQLException, ReserveException {
		ReserveProfessorRoomDAO.getInstance().delete(reserva);
		this.reserveRoomProfessorVector.remove(reserva);
	}
}