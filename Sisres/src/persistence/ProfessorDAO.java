package persistence;

import model.Professor;

import java.sql.*;
import java.util.Vector;

import exception.ClientException;

public class ProfessorDAO {

		private static final String PROFESSOR_EXISTING = "O Professor ja esta cadastrado.";
		private static final String PROFESSOR_NOT_EXISTING = "O Professor nao esta cadastrado.";
		private static final String PROFESSOR_NULL = "O Professor esta nulo.";
		private static final String PROFESSOR_RESERVED = "Room esta sendo utilizada em uma reserva.";
		private static final String CPF_EXISTING = "Ja existe um professor cadastrado com esse CPF.";
		private static final String REGISTRATION_EXISTING = "Ja existe um professor cadastrado com essa matricula.";
		
		
		private static ProfessorDAO instance;
		private ProfessorDAO(){
			//nothing here
		}
		
		public static ProfessorDAO getInstance(){
			if(instance == null) {
				instance = new ProfessorDAO();
			}
			
			return instance;
		}
	
	
	public void include(Professor prof) throws SQLException, ClientException {
			if(prof == null) {
				throw new ClientException(PROFESSOR_NULL);
			}
			else
				if(this.inDBCpf(prof.getCpf())) {
					throw new ClientException(CPF_EXISTING);
				}
				else
					if(this.inDBRegistration(prof.getRegistration())) {
						throw new ClientException(REGISTRATION_EXISTING);
					}
			this.updateQuery("INSERT INTO " +
						"professor (nome, cpf, telefone, email, matricula) VALUES (" +
						"\"" + prof.getName() + "\", " +
						"\"" + prof.getCpf()+ "\", " +
						"\"" + prof.getPhone() + "\", " +
						"\"" + prof.getEmail() + "\", " +
						"\"" + prof.getRegistration() + "\"); "
					);			
	}

	public void alterate(Professor oldProfessor, Professor newProfessor) throws SQLException, ClientException {
		if(oldProfessor == null) {
			throw new ClientException(PROFESSOR_NULL);
		}
		
		if(newProfessor == null) {
			throw new ClientException(PROFESSOR_NULL);
		}
		
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement;
		
		if(!this.inDB(oldProfessor)) {
			throw new ClientException(PROFESSOR_NOT_EXISTING);
		}
		if(this.inOtherDB(oldProfessor)) {
			throw new ClientException(PROFESSOR_RESERVED);
		}
		else 
			if(!oldProfessor.getCpf().equals(newProfessor.getCpf()) && this.inDBCpf(newProfessor.getCpf())) {
				throw new ClientException(CPF_EXISTING);
			}
			else
				if(!oldProfessor.getRegistration().equals(newProfessor.getRegistration()) &&
						this.inDBRegistration(newProfessor.getRegistration())) {
					throw new ClientException(REGISTRATION_EXISTING);
				}
				else
					if(!this.inDB(newProfessor)) {
						String message = "UPDATE professor SET " +
								"nome = \"" + newProfessor.getName() + "\", " +
								"cpf = \"" + newProfessor.getCpf() + "\", " +
								"telefone = \"" + newProfessor.getPhone() + "\", " +
								"email = \"" + newProfessor.getEmail() + "\", " +
								"matricula = \"" + newProfessor.getRegistration() + "\""+
								" WHERE " +
								"professor.nome = \"" + oldProfessor.getName() + "\" and " +
								"professor.cpf = \"" + oldProfessor.getCpf() + "\" and " +
								"professor.telefone = \"" + oldProfessor.getPhone() + "\" and " +
								"professor.email = \"" + oldProfessor.getEmail() + "\" and " +
								"professor.matricula = \"" + oldProfessor.getRegistration() + "\";";
						connection.setAutoCommit(false);
						prepare_statement = connection.prepareStatement(message);
						prepare_statement.executeUpdate();
						connection.commit();
					}
					else
						throw new ClientException(PROFESSOR_EXISTING);
					
		prepare_statement.close();
		connection.close();
	}

	public void delete(Professor prof) throws SQLException, ClientException {
		if(prof == null) {
			throw new ClientException(PROFESSOR_NULL);
		}
		
		if(this.inOtherDB(prof)) {
			throw new ClientException(PROFESSOR_RESERVED);
		}
		else
			if(this.inDB(prof)) {
				this.updateQuery("DELETE FROM professor WHERE " +
					"professor.nome = \"" + prof.getName() + "\" and " +
					"professor.cpf = \"" + prof.getCpf() + "\" and " +
					"professor.telefone = \"" + prof.getPhone() + "\" and " +
					"professor.email = \"" + prof.getEmail() + "\" and " +
					"professor.matricula = \"" + prof.getRegistration() + "\";"
					);
			}
			else
				throw new ClientException(PROFESSOR_NOT_EXISTING);
			}

	
	
	public Vector<Professor> searchAllProfessors() throws SQLException, ClientException {
		return this.search("SELECT * FROM professor;");
	}
	
	public Vector<Professor> searchName(String value) throws SQLException, ClientException {
		return this.search("SELECT * FROM professor WHERE nome = " + "\"" + value + "\";");
	}
	
	public Vector<Professor> searchCpf(String value) throws SQLException, ClientException {
		return this.search("SELECT * FROM professor WHERE cpf = " + "\"" + value + "\";");
	}
	
	public Vector<Professor> searchRegistration(String value) throws SQLException, ClientException {
		return this.search("SELECT * FROM professor WHERE matricula = " + "\"" + value + "\";");
	}
	
	public Vector<Professor> searchEmail(String value) throws SQLException, ClientException {
		return this.search("SELECT * FROM professor WHERE email = " + "\"" + value + "\";");
	}
	
	public Vector<Professor> searchPhone(String value) throws SQLException, ClientException {
		return this.search("SELECT * FROM professor WHERE telefone = " + "\"" + value + "\";");
	}

	/**
	 * Private Methods
	 * */
	
	private Vector<Professor> search(String query) throws SQLException, ClientException {		
		Vector<Professor> vet = new Vector<Professor>();
		
		Connection connection =  FactoryConnection.getInstance().getConnection();
		
		PreparedStatement prepare_statement = connection.prepareStatement(query);
		ResultSet resultSet = prepare_statement.executeQuery();
		
		while(resultSet.next()) {
			vet.add(this.fetchProfessor(resultSet));
		}
		
		prepare_statement.close();
		resultSet.close();
		connection.close();
		return vet;
	}
	
	
	private boolean inDBGeneric(String query) throws SQLException{
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement = connection.prepareStatement(query);
		ResultSet resultSet = prepare_statement.executeQuery();
		
		if(!resultSet.next()) {
			resultSet.close();
			prepare_statement.close();
			connection.close();
			return false;
		}
		else {
			resultSet.close();
			prepare_statement.close();
			connection.close();
			return true;
		}
	}
	
	private boolean inDB(Professor professor) throws SQLException{
		return this.inDBGeneric("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + professor.getName() + "\" and " +
				"professor.cpf = \"" + professor.getCpf() + "\" and " +
				"professor.telefone = \"" + professor.getPhone() + "\" and " +
				"professor.email = \"" + professor.getEmail() + "\" and " +
				"professor.matricula = \"" + professor.getRegistration() + "\";");
	}
	
	private boolean inDBCpf(String code) throws SQLException{
		return this.inDBGeneric("SELECT * FROM professor WHERE " +
				"cpf = \"" + code + "\";");
	}
	
	private boolean inDBRegistration(String code) throws SQLException{
		return this.inDBGeneric("SELECT * FROM professor WHERE " +
				"matricula = \"" + code + "\";");
	}
	
	private boolean inOtherDB(Professor professor) throws SQLException{
		if ( this.inDBGeneric(
				"SELECT * FROM reserva_sala_professor WHERE " +
				"id_professor = (SELECT id_professor FROM professor WHERE " +
				"professor.nome = \"" + professor.getName() + "\" and " +
				"professor.cpf = \"" + professor.getCpf() + "\" and " +
				"professor.telefone = \"" + professor.getPhone() + "\" and " +
				"professor.email = \"" + professor.getEmail() + "\" and " +
				"professor.matricula = \"" + professor.getRegistration() + "\");") == false) {
			if(this.inDBGeneric(
					"SELECT * FROM reserva_equipamento WHERE " +
					"id_professor = (SELECT id_professor FROM professor WHERE " +
					"professor.nome = \"" + professor.getName() + "\" and " +
					"professor.cpf = \"" + professor.getCpf() + "\" and " +
					"professor.telefone = \"" + professor.getPhone() + "\" and " +
					"professor.email = \"" + professor.getEmail() + "\" and " +
					"professor.matricula = \"" + professor.getRegistration() + "\");") == false) {
				return false;
			}
		}
		return true;
	}
	
	
	private Professor fetchProfessor(ResultSet resultSet) throws ClientException, SQLException{
		return new Professor(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("matricula"),
				resultSet.getString("telefone"), resultSet.getString("email"));
	}
	
	private void updateQuery(String message) throws SQLException{
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement = connection.prepareStatement(message);
		prepare_statement.executeUpdate();		
		prepare_statement.close();
		connection.close();
	}

}
