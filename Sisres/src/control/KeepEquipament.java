package control;

import java.sql.SQLException;
import java.util.Vector;
import persistence.EquipamentDAO;
import exception.PatrimonyException;
import model.Equipament;

public class KeepEquipament {

	private Vector<Equipament> Equipament_vector = new Vector<Equipament>();

	
	private static KeepEquipament instance;
	private KeepEquipament() {
		//nothing
	}
	public static KeepEquipament getInstance() {
		if (instance == null) {
			instance = new KeepEquipament();
		}
		
		return instance;
	}

		
	public Vector<Equipament> getEquipament_vector() throws SQLException, PatrimonyException {
		this.Equipament_vector = EquipamentDAO.getInstance().searchAllEquipaments();
		return this.Equipament_vector;
	}

	public void insert(String code, String description) throws PatrimonyException, SQLException {
		Equipament equipament = new Equipament(code, description);
		EquipamentDAO.getInstance().include(equipament);
		getEquipament_vector();
	}

	public void alterate(String code, String description, Equipament equipament) throws PatrimonyException, SQLException {
		if (equipament == null) {
			throw new PatrimonyException("Equipamento em branco");
		}
		Equipament equipament_old = new Equipament(equipament.getCode(), equipament.getDescription());
		equipament.setCode(code);
		equipament.setDescription(description);
		EquipamentDAO.getInstance().alterate(equipament_old, equipament);
		getEquipament_vector();
	}

	public void delete(Equipament equipament) throws SQLException, PatrimonyException {
		if (equipament == null) {
			throw new PatrimonyException("Equipamento em branco");
		}
		EquipamentDAO.getInstance().delete(equipament);
		getEquipament_vector();
	}
}
