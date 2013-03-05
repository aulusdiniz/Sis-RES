package persistence;

import model.Student;
import java.sql.*;
import java.util.Vector;
import exception.ClientException;

public class StudentDAO {

	private static final String STUDENT_EXISTING = "O aluno ja esta cadastrado.";
	private static final String STUDENT_NULL = "O aluno está nulo.";
	private static final String STUDENT_NOT_EXISTING = "O aluno não está cadastrado.";
	private static final String STUDENT_RESERVED = "Room esta sendo utilizada em uma reserva.";
	private static final String CPF_EXISTING = "Ja existe um aluno cadastrado com esse CPF.";
	private static final String REGISTRATION_EXISTING = "Ja existe um aluno cadastrado com essa matricula.";	


	private static StudentDAO instance;
	private StudentDAO(){
		//nothing here
	}
	public static StudentDAO getInstance() {
		if(instance == null) {
			instance = new StudentDAO();
		}
		else{
			//nothing here
		}
		
		return instance;
	}
	
		
	public void include(Student student) throws SQLException, ClientException {
		if(student == null) {
			throw new ClientException(STUDENT_NULL);
		}
		else
			if(this.inDBCpf(student.getCpf())) {
				throw new ClientException(CPF_EXISTING);
			}
			else
				if(this.inDBRegistration(student.getRegistration())) {
					throw new ClientException(REGISTRATION_EXISTING);
				}
				else
					if(!this.inDB(student)) {
						this.updateQuery("INSERT INTO " +
								"aluno (nome, cpf, telefone, email, matricula) VALUES (" +
								"\"" + student.getName() + "\", " +
								"\"" + student.getCpf()+ "\", " +
								"\"" + student.getPhone() + "\", " +
								"\"" + student.getEmail() + "\", " +
								"\"" + student.getRegistration() + "\"); "
								);
					}
					else
						throw new ClientException(STUDENT_EXISTING);
	}

	public void alterate(Student oldStudent, Student newStudent) throws SQLException, ClientException {
		if(oldStudent == null) {
			throw new ClientException(STUDENT_NULL);
		}
		
		if(newStudent == null) {
			throw new ClientException(STUDENT_NULL);
		}
		
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement;
		
		if(!this.inDB(oldStudent)){
			throw new ClientException(STUDENT_NOT_EXISTING);
		}
		else
			if(this.inOtherDB(oldStudent)) {
				throw new ClientException(STUDENT_RESERVED);
			}
			else 
				if(!oldStudent.getCpf().equals(newStudent.getCpf()) && this.inDBCpf(newStudent.getCpf())) {
					throw new ClientException(CPF_EXISTING);
				}
				else 
					if(!oldStudent.getRegistration().equals(newStudent.getRegistration()) && this.inDBRegistration(newStudent.getRegistration())) {
						throw new ClientException(REGISTRATION_EXISTING);
					}
					else 
						if(!this.inDB(newStudent)) {
							String message = "UPDATE aluno SET " +
								"nome = \"" + newStudent.getName() + "\", " +
								"cpf = \"" + newStudent.getCpf() + "\", " +
								"telefone = \"" + newStudent.getPhone() + "\", " +
								"email = \"" + newStudent.getEmail() + "\", " +
								"matricula = \"" + newStudent.getRegistration() + "\""+
								" WHERE " +
								"aluno.nome = \"" + oldStudent.getName() + "\" and " +
								"aluno.cpf = \"" + oldStudent.getCpf() + "\" and " +
								"aluno.telefone = \"" + oldStudent.getPhone() + "\" and " +
								"aluno.email = \"" + oldStudent.getEmail() + "\" and " +
								"aluno.matricula = \"" + oldStudent.getRegistration() + "\";";
							connection.setAutoCommit(false);
							prepareStatement = connection.prepareStatement(message);
							prepareStatement.executeUpdate();
							connection.commit();
						}
						else
							throw new ClientException(STUDENT_EXISTING);
		prepareStatement.close();
		connection.close();
	}

	public void delete(Student student) throws SQLException, ClientException {
		if(student == null) {
			throw new ClientException(STUDENT_NULL);
		}
		else
			if(this.inOtherDB(student)) {
				throw new ClientException(STUDENT_RESERVED);
			}
			else 
				if(this.inDB(student)) {
					this.updateQuery("DELETE FROM aluno WHERE " +
									"aluno.nome = \"" + student.getName() + "\" and " +
									"aluno.cpf = \"" + student.getCpf() + "\" and " +
									"aluno.telefone = \"" + student.getPhone() + "\" and " +
									"aluno.email = \"" + student.getEmail() + "\" and " +
									"aluno.matricula = \"" + student.getRegistration() + "\";");
				}
				else
					throw new ClientException(STUDENT_NOT_EXISTING);
	}
	
	public Vector<Student> searchAllStudents() throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno;");
	}
	
	public Vector<Student> searchName(String value) throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno WHERE nome = " + "\"" + value + "\";");
	}
	
	public Vector<Student> searchCpf(String value) throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno WHERE cpf = " + "\"" + value + "\";");
	}
	
	public Vector<Student> searchRegistration(String value) throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno WHERE matricula = " + "\"" + value + "\";");
	}
	
	public Vector<Student> searchEmail(String value) throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno WHERE email = " + "\"" + value + "\";");
	}
	
	public Vector<Student> searchPhone(String value) throws SQLException, ClientException {
		
		return this.search("SELECT * FROM aluno WHERE telefone = " + "\"" + value + "\";");
	}
	
	/**
	 * Private Methods
	 * */
	
	private Vector<Student> search(String query) throws SQLException, ClientException {
		
		Vector<Student> studentVector = new Vector<Student>();
		
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet resultSet = prepareStatement.executeQuery();
		
		while(resultSet.next()) {
			studentVector.add(this.fetchStudent(resultSet));
		}
		
		prepareStatement.close();
		resultSet.close();
		connection.close();
		
		return studentVector;
	}
	
	
	private boolean inDBGeneric(String query) throws SQLException{
		
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet resultSet = prepareStatement.executeQuery();
		
		if(!resultSet.next()) {
			resultSet.close();
			prepareStatement.close();
			connection.close();
			
			return false;
		}
		else {
			resultSet.close();
			prepareStatement.close();
			connection.close();
			
			return true;
		}
	}
	
	private boolean inDB(Student student) throws SQLException{
		return this.inDBGeneric("SELECT * FROM aluno WHERE " +
				"aluno.nome = \"" + student.getName() + "\" and " +
				"aluno.cpf = \"" + student.getCpf() + "\" and " +
				"aluno.telefone = \"" + student.getPhone() + "\" and " +
				"aluno.email = \"" + student.getEmail() + "\" and " +
				"aluno.matricula = \"" + student.getRegistration() + "\";");
	}
	
	private boolean inDBCpf(String code) throws SQLException{
		return this.inDBGeneric("SELECT * FROM aluno WHERE " +
				"aluno.cpf = \"" + code + "\";");
	}
	
	private boolean inDBRegistration(String registration) throws SQLException{
		return this.inDBGeneric("SELECT * FROM aluno WHERE " +
				"aluno.matricula = \"" + registration + "\";");
	}
	
	private boolean inOtherDB(Student student) throws SQLException, ClientException{
		return this.inDBGeneric(
				"SELECT * FROM reserva_sala_aluno WHERE " +
				"id_aluno = (SELECT id_aluno FROM aluno WHERE " +
				"aluno.nome = \"" + student.getName() + "\" and " +
				"aluno.cpf = \"" + student.getCpf() + "\" and " +
				"aluno.telefone = \"" + student.getPhone() + "\" and " +
				"aluno.email = \"" + student.getEmail() + "\" and " +
				"aluno.matricula = \"" + student.getRegistration() + "\");");
	}
	
	
	private Student fetchStudent(ResultSet resultSet) throws ClientException, SQLException{
		return new Student(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("matricula"),
				resultSet.getString("telefone"), resultSet.getString("email"));
	}
	
	private void updateQuery(String message) throws SQLException{
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(message);
		prepareStatement.executeUpdate();		
		prepareStatement.close();
		connection.close();
	}
}
