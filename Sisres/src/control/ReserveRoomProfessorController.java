package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.ResSalaProfessorDAO;

import model.Professor;
import model.ReserveRoomProfessor;
import model.Room;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveRoomProfessorController {
	private Vector<ReserveRoomProfessor> reserveRoomProfessorVector = new Vector<ReserveRoomProfessor>();
	
	//Singleton
		private static ReserveRoomProfessorController instance;
		private ReserveRoomProfessorController() {
		}
		public static ReserveRoomProfessorController getInstance() {
		if(instance == null)
			instance = new ReserveRoomProfessorController();
		return instance;
	}
	//
		
		public Vector<ReserveRoomProfessor> buscarPorData(String data) throws SQLException, ClientException, PatrimonyException, ReserveException{
			this.reserveRoomProfessorVector =  ResSalaProfessorDAO.getInstance().buscarPorData(data);
			return this.reserveRoomProfessorVector;
	    } 
	    	
		
	public Vector<ReserveRoomProfessor> getResProfessorSala_vet() throws SQLException, ClientException, PatrimonyException, ReserveException {
		this.reserveRoomProfessorVector = ResSalaProfessorDAO.getInstance().buscarTodos();
		return this.reserveRoomProfessorVector;
	}

	public void inserir(Room room, Professor prof,
						String data, String hora, String finalidade) 
					throws SQLException, ReserveException {

		ReserveRoomProfessor reserva = new ReserveRoomProfessor(data, hora, room , finalidade, prof);
		ResSalaProfessorDAO.getInstance().incluir(reserva);
		this.reserveRoomProfessorVector.add(reserva);
	}

	public void alterar(String finalidade, ReserveRoomProfessor reserva) 
				throws SQLException, ReserveException {
		
		ReserveRoomProfessor reserva_old = new ReserveRoomProfessor(reserva.getDate(), reserva.getHour(), reserva.getRoom() , 
				reserva.getFinality(), reserva.getProfessor());
		
		reserva.setFinality(finalidade);
		ResSalaProfessorDAO.getInstance().alterar(reserva_old, reserva);
		
	}

	public void excluir(ReserveRoomProfessor reserva) throws SQLException, ReserveException {
		ResSalaProfessorDAO.getInstance().excluir(reserva);
		this.reserveRoomProfessorVector.remove(reserva);
	}
}