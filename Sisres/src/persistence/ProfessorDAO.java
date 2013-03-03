package persistence;

import model.Professor;

import java.sql.*;
import java.util.Vector;

import exception.ClientException;

public class ProfessorDAO {

		private static final String PROFESSOR_EXISTING = "O Professor ja esta cadastrado.";
		private static final String PROFESSOR_NOT_EXISTING = "O Professor nao esta cadastrado.";
		private static final String PROFESSOR_NULL = "O Professor esta nulo.";
		private static final String PROFESSOR_RESERVED = "Sala esta sendo utilizada em uma reserva.";
		private static final String CPF_EXISTING = "Ja existe um professor cadastrado com esse CPF.";
		private static final String REGISTRATION_EXISTING = "Ja existe um professor cadastrado com essa matricula.";
		
		
		private static ProfessorDAO instance;
		private ProfessorDAO(){
			//nothing
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
					if(this.inDBMatricula(prof.getRegistration())) {
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

	public void alterate(Professor professor_old, Professor professor_new) throws SQLException, ClientException {
		if(professor_old == null) {
			throw new ClientException(PROFESSOR_NULL);
		}
		
		if(professor_new == null) {
			throw new ClientException(PROFESSOR_NULL);
		}
		
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement;
		
		if(!this.inDB(professor_old)) {
			throw new ClientException(PROFESSOR_NOT_EXISTING);
		}
		if(this.inOtherDB(professor_old)) {
			throw new ClientException(PROFESSOR_RESERVED);
		}
		else 
			if(!professor_old.getCpf().equals(professor_new.getCpf()) && this.inDBCpf(professor_new.getCpf())) {
				throw new ClientException(CPF_EXISTING);
			}
			else
				if(!professor_old.getRegistration().equals(professor_new.getRegistration()) &&
						this.inDBMatricula(professor_new.getRegistration())) {
					throw new ClientException(REGISTRATION_EXISTING);
				}
				else
					if(!this.inDB(professor_new)) {
						String msg = "UPDATE professor SET " +
								"nome = \"" + professor_new.getName() + "\", " +
								"cpf = \"" + professor_new.getCpf() + "\", " +
								"telefone = \"" + professor_new.getPhone() + "\", " +
								"email = \"" + professor_new.getEmail() + "\", " +
								"matricula = \"" + professor_new.getRegistration() + "\""+
								" WHERE " +
								"professor.nome = \"" + professor_old.getName() + "\" and " +
								"professor.cpf = \"" + professor_old.getCpf() + "\" and " +
								"professor.telefone = \"" + professor_old.getPhone() + "\" and " +
								"professor.email = \"" + professor_old.getEmail() + "\" and " +
								"professor.matricula = \"" + professor_old.getRegistration() + "\";";
						connection.setAutoCommit(false);
						prepare_statement = connection.prepareStatement(msg);
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
	 * Metodos Privados
	 * */
	
	private Vector<Professor> search(String query) throws SQLException, ClientException {		
		Vector<Professor> vet = new Vector<Professor>();
		
		Connection connection =  FactoryConnection.getInstance().getConnection();
		
		PreparedStatement prepare_statement = connection.prepareStatement(query);
		ResultSet result_set = prepare_statement.executeQuery();
		
		while(result_set.next()) {
			vet.add(this.fetchProfessor(result_set));
		}
		
		prepare_statement.close();
		result_set.close();
		connection.close();
		return vet;
	}
	
	
	private boolean inDBGeneric(String query) throws SQLException{
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement = connection.prepareStatement(query);
		ResultSet result_set = prepare_statement.executeQuery();
		
		if(!result_set.next()) {
			result_set.close();
			prepare_statement.close();
			connection.close();
			return false;
		}
		else {
			result_set.close();
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
	
	private boolean inDBMatricula(String code) throws SQLException{
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
	
	
	private Professor fetchProfessor(ResultSet result_set) throws ClientException, SQLException{
		return new Professor(result_set.getString("nome"), result_set.getString("cpf"), result_set.getString("matricula"),
				result_set.getString("telefone"), result_set.getString("email"));
	}
	
	private void updateQuery(String msg) throws SQLException{
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement = connection.prepareStatement(msg);
		prepare_statement.executeUpdate();		
		prepare_statement.close();
		connection.close();
	}

}
