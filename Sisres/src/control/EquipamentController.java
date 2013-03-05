package control;

import java.sql.SQLException;
import java.util.Vector;
import persistence.EquipamentDAO;
import exception.PatrimonyException;
import model.Equipament;



public class EquipamentController {

	private final String EQUIPAMENT_BLANK = "Equipamento em branco";
	
	private Vector<Equipament> equipamentVector = new Vector<Equipament>();

	
	private static EquipamentController instance;
	private EquipamentController() {
		//nothing here
	}
	public static EquipamentController getInstance() {
		if (instance == null) {
			instance = new EquipamentController();
		}
		
		return instance;
	}

		
	public Vector<Equipament> getEquipamentVector() throws SQLException, PatrimonyException {
		this.equipamentVector = EquipamentDAO.getInstance().searchAllEquipaments();
		return this.equipamentVector;
	}

	public void insert(String code, String description) throws PatrimonyException, SQLException {
		Equipament equipament = new Equipament(code, description);
		EquipamentDAO.getInstance().include(equipament);
		getEquipamentVector();
	}

	public void alterate(String code, String description, Equipament equipament) throws PatrimonyException, SQLException {
		if (equipament == null) {
			throw new PatrimonyException(EQUIPAMENT_BLANK);
		}
		Equipament oldEquipament = new Equipament(equipament.getCode(), equipament.getDescription());
		equipament.setCode(code);
		equipament.setDescription(description);
		EquipamentDAO.getInstance().alterate(oldEquipament, equipament);
		getEquipamentVector();
	}

	public void delete(Equipament equipament) throws SQLException, PatrimonyException {
		if (equipament == null) {
			throw new PatrimonyException(EQUIPAMENT_BLANK);
		}
		EquipamentDAO.getInstance().delete(equipament);
		getEquipamentVector();
	}
}
