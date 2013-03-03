package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.SalaDAO;
import exception.PatrimonyException;
import model.Sala;

public class ManterSala {

	private Vector<Sala> salas_vet = new Vector<Sala>();
	
	//Singleton
		private static ManterSala instance;
		private ManterSala() {
		}
		public static ManterSala getInstance() {
		if(instance == null)
			instance = new ManterSala();
		return instance;
	}
	//
		
	public Vector<Sala> getSalas_vet() throws SQLException, PatrimonyException{
		this.salas_vet = SalaDAO.getInstance().buscarTodos();
		return this.salas_vet;
	}

	public void inserir(String codigo, String descricao, String capacidade) throws PatrimonyException, SQLException {
		Sala sala = new Sala(codigo, descricao, capacidade);
		SalaDAO.getInstance().incluir(sala);
		this.salas_vet.add(sala);
	}

	public void alterar(String codigo, String descricao, String capacidade, Sala sala) throws PatrimonyException, SQLException {
		Sala old_sala = new Sala(sala.getCode(), sala.getDescription(),
								sala.getCapacidade());
		sala.setCode(codigo);
		sala.setDescription(descricao);
		sala.setCapacidade(capacidade);
		SalaDAO.getInstance().alterar(old_sala, sala);
	}

	public void excluir(Sala sala) throws SQLException, PatrimonyException {
		SalaDAO.getInstance().excluir(sala);
		this.salas_vet.remove(sala);
	}

}
