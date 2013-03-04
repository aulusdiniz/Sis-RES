package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.SalaDAO;
import exception.PatrimonyException;
import model.Room;

public class RoomController {

	private Vector<Room> salas_vet = new Vector<Room>();
	
	//Singleton
		private static RoomController instance;
		private RoomController() {
		}
		public static RoomController getInstance() {
		if(instance == null)
			instance = new RoomController();
		return instance;
	}
	//
		
	public Vector<Room> getSalas_vet() throws SQLException, PatrimonyException{
		this.salas_vet = SalaDAO.getInstance().buscarTodos();
		return this.salas_vet;
	}

	public void inserir(String codigo, String descricao, String capacidade) throws PatrimonyException, SQLException {
		Room room = new Room(codigo, descricao, capacidade);
		SalaDAO.getInstance().incluir(room);
		this.salas_vet.add(room);
	}

	public void alterar(String codigo, String descricao, String capacidade, Room room) throws PatrimonyException, SQLException {
		Room old_sala = new Room(room.getCode(), room.getDescription(),
								room.getCapacity());
		room.setCode(codigo);
		room.setDescription(descricao);
		room.setCapacity(capacidade);
		SalaDAO.getInstance().alterar(old_sala, room);
	}

	public void excluir(Room room) throws SQLException, PatrimonyException {
		SalaDAO.getInstance().excluir(room);
		this.salas_vet.remove(room);
	}

}
