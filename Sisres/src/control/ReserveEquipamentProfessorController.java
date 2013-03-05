package control;

import java.sql.SQLException;
import java.util.Vector;

import model.Equipament;
import model.Professor;
import model.ReserveEquipamentProfessor;
import persistence.ReserveEquipamentProfessorDAO;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveEquipamentProfessorController {
    private Vector<ReserveEquipamentProfessor> reserveEquipamentProfessorVector = new Vector<ReserveEquipamentProfessor>();

    private static ReserveEquipamentProfessorController instance;

    private ReserveEquipamentProfessorController() {
    	//nothing here
    }

    public static ReserveEquipamentProfessorController getInstance() {
        if (instance == null){
            instance = new ReserveEquipamentProfessorController();
        }
        else{
        	//nothing here
        }
        return instance;
    }


    public Vector<ReserveEquipamentProfessor> getReserveHour(String hour) throws SQLException, PatrimonyException,
            																	 ClientException, ReserveException {
        return ReserveEquipamentProfessorDAO.getInstance().findByHour(hour);

    }

    public Vector<ReserveEquipamentProfessor> getReserveMonth(int month) throws SQLException, PatrimonyException, 
    																			ClientException, ReserveException {
        return ReserveEquipamentProfessorDAO.getInstance().findByMonth(month);
    }

    public Vector<ReserveEquipamentProfessor> getReserveEquipamentProfessorVector() throws SQLException, PatrimonyException,
    																					   ClientException, ReserveException {
        return ReserveEquipamentProfessorDAO.getInstance().findAll();
    }

    public void insert(Equipament equipament, Professor professor, String date, String hour) throws SQLException, ReserveException {
        ReserveEquipamentProfessor reserveEquipamentProfessor = new ReserveEquipamentProfessor(date, hour, equipament, professor);
        ReserveEquipamentProfessorDAO.getInstance().include(reserveEquipamentProfessor);
        this.reserveEquipamentProfessorVector.add(reserveEquipamentProfessor);
    }

    public void alterate(String finality, ReserveEquipamentProfessor reserveEquipamentProfessor) throws SQLException, ReserveException {
        ReserveEquipamentProfessor oldReserve = new ReserveEquipamentProfessor(reserveEquipamentProfessor.getDate(),
        																	   reserveEquipamentProfessor.getHour(),
        																	   reserveEquipamentProfessor.getEquipament(), 
        																	   reserveEquipamentProfessor.getProfessor());
        ReserveEquipamentProfessorDAO.getInstance().alterate(oldReserve, reserveEquipamentProfessor);
    }

    public void delete(ReserveEquipamentProfessor reserveEquipamentProfessor) throws SQLException, ReserveException {
        ReserveEquipamentProfessorDAO.getInstance().delete(reserveEquipamentProfessor);
        this.reserveEquipamentProfessorVector.remove(reserveEquipamentProfessor);
    }
}
