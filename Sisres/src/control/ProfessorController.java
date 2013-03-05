package control;

import java.sql.SQLException;
import java.util.Vector;
import persistence.ProfessorDAO;
import exception.ClientException;
import model.Professor;

public class ProfessorController {
	
	private Vector<Professor> professorVector = new Vector<Professor>();
	
	private static ProfessorController instance;
	private ProfessorController() {
		//nothing here
	}
	
	public static ProfessorController getInstance() {
		if(instance == null) {
			instance = new ProfessorController();
		}
		else{
			//Nothing here
		}
	return instance;
	} 
	
	public Vector<Professor> searchName(String value) throws SQLException, ClientException {
		return ProfessorDAO.getInstance().searchName(value);
	}
	
	public Vector<Professor> searchCpf(String value) throws SQLException, ClientException {
		return ProfessorDAO.getInstance().searchCpf(value);
	}
	
	public Vector<Professor> searchRegistration(String value) throws SQLException, ClientException {
		return ProfessorDAO.getInstance().searchRegistration(value);
	}
	
	public Vector<Professor> searchEmail(String value) throws SQLException, ClientException {
		return ProfessorDAO.getInstance().searchEmail(value);
	}
	
	public Vector<Professor> searchPhone(String value) throws SQLException, ClientException {
		return ProfessorDAO.getInstance().searchPhone(value);
	}	
		
	public Vector<Professor> getProfessorVector() throws SQLException, ClientException{
		this.professorVector = ProfessorDAO.getInstance().searchAllProfessors();
		return this.professorVector;
	}
	
	public void insert(String name, String cpf, String registration, String phone,
					   String email) throws ClientException, SQLException {
		Professor professor = new Professor(name, cpf, registration, phone, email);
		ProfessorDAO.getInstance().include(professor);
		this.professorVector.add(professor);
	}

	public void alterate(String name, String cpf, String registration, String phone,
			             String email, Professor professor) throws ClientException, SQLException {
		Professor professor_old = new Professor( professor.getName(), professor.getCpf(), 
				professor.getRegistration(), professor.getPhone(), professor.getEmail());
		professor.setName(name);
		professor.setCpf(cpf);
		professor.setRegistration(registration);
		professor.setPhone(phone);
		professor.setEmail(email);
		ProfessorDAO.getInstance().alterate(professor_old, professor);
	}

	public void delete(Professor professor) throws SQLException, ClientException {
		ProfessorDAO.getInstance().delete(professor);
		this.professorVector.remove(professor);
	}

}
