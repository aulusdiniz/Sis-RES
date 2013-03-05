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

	public void alterate(Student student_old, Student student_new) throws SQLException, ClientException {
		if(student_old == null) {
			throw new ClientException(STUDENT_NULL);
		}
		
		if(student_new == null) {
			throw new ClientException(STUDENT_NULL);
		}
		
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst;
		
		if(!this.inDB(student_old)){
			throw new ClientException(STUDENT_NOT_EXISTING);
		}
		else
			if(this.inOtherDB(student_old)) {
				throw new ClientException(STUDENT_RESERVED);
			}
			else 
				if(!student_old.getCpf().equals(student_new.getCpf()) && this.inDBCpf(student_new.getCpf())) {
					throw new ClientException(CPF_EXISTING);
				}
				else 
					if(!student_old.getRegistration().equals(student_new.getRegistration()) && this.inDBRegistration(student_new.getRegistration())) {
						throw new ClientException(REGISTRATION_EXISTING);
					}
					else 
						if(!this.inDB(student_new)) {
							String msg = "UPDATE aluno SET " +
								"nome = \"" + student_new.getName() + "\", " +
								"cpf = \"" + student_new.getCpf() + "\", " +
								"telefone = \"" + student_new.getPhone() + "\", " +
								"email = \"" + student_new.getEmail() + "\", " +
								"matricula = \"" + student_new.getRegistration() + "\""+
								" WHERE " +
								"aluno.nome = \"" + student_old.getName() + "\" and " +
								"aluno.cpf = \"" + student_old.getCpf() + "\" and " +
								"aluno.telefone = \"" + student_old.getPhone() + "\" and " +
								"aluno.email = \"" + student_old.getEmail() + "\" and " +
								"aluno.matricula = \"" + student_old.getRegistration() + "\";";
							con.setAutoCommit(false);
							pst = con.prepareStatement(msg);
							pst.executeUpdate();
							con.commit();
						}
						else
							throw new ClientException(STUDENT_EXISTING);
		pst.close();
		con.close();
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
		
		Vector<Student> vector_student = new Vector<Student>();
		
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepare_statement = connection.prepareStatement(query);
		ResultSet result_set = prepare_statement.executeQuery();
		
		while(result_set.next()) {
			vector_student.add(this.fetchStudent(result_set));
		}
		
		prepare_statement.close();
		result_set.close();
		connection.close();
		
		return vector_student;
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
	
	
	private Student fetchStudent(ResultSet result_set) throws ClientException, SQLException{
		return new Student(result_set.getString("nome"), result_set.getString("cpf"), result_set.getString("matricula"),
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
