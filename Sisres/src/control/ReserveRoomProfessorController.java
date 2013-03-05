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
		else{
			//Nothing here
		}
		return instance;
	}
		
		public Vector<ReserveRoomProfessor> findByDate(String date) throws SQLException, PatrimonyException, 
																		   ClientException, ReserveException{
			this.reserveRoomProfessorVector =  ReserveProfessorRoomDAO.getInstance().findByDate(date);
			return this.reserveRoomProfessorVector;
	    } 
	    	
		
	public Vector<ReserveRoomProfessor> getReserveProfessorRoomVector() throws SQLException, PatrimonyException, 
																			   ClientException, ReserveException {
		this.reserveRoomProfessorVector = ReserveProfessorRoomDAO.getInstance().findAll();
		return this.reserveRoomProfessorVector;
	}

	public void insert(Room room, Professor professor, String date, String hour, String finality) throws SQLException, ReserveException {

		ReserveRoomProfessor reserve = new ReserveRoomProfessor(date, hour, room , finality, professor);
		ReserveProfessorRoomDAO.getInstance().include(reserve);
		this.reserveRoomProfessorVector.add(reserve);
	}

	public void alterate(String finality, ReserveRoomProfessor reserve) throws SQLException, ReserveException {
		
		ReserveRoomProfessor oldReserve = new ReserveRoomProfessor(reserve.getDate(), reserve.getHour(), reserve.getRoom() , 
				reserve.getFinality(), reserve.getProfessor());
		
		reserve.setFinality(finality);
		ReserveProfessorRoomDAO.getInstance().alterate(oldReserve, reserve);
		
	}

	public void delete(ReserveRoomProfessor reserve) throws SQLException, ReserveException {
		ReserveProfessorRoomDAO.getInstance().delete(reserve);
		this.reserveRoomProfessorVector.remove(reserve);
	}
}