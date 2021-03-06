package control;

import java.sql.SQLException;
import java.util.Vector;

import persistence.StudentDAO;
import exception.ClientException;
import model.Student;

public class StudentController {
	
	private Vector<Student> studentVector = new Vector<Student>();
	

		private static StudentController instance;
		private StudentController() {
			//nothing here
		}
		public static StudentController getInstance() {
		if(instance == null) {
			instance = new StudentController();
		}
		else{
			//Nothing here
		}
		return instance;
	}

	
	public Vector<Student> searchName(String value) throws SQLException, ClientException {
		
		return StudentDAO.getInstance().searchName(value);
	}
	
	public Vector<Student> searchCpf(String value) throws SQLException, ClientException {
		
		return StudentDAO.getInstance().searchCpf(value);
	}
	
	public Vector<Student> searchRegistration(String value) throws SQLException, ClientException {
		
		return StudentDAO.getInstance().searchRegistration(value);
	}
	
	public Vector<Student> searchEmail(String value) throws SQLException, ClientException {
		
		return StudentDAO.getInstance().searchEmail(value);
	}
	
	public Vector<Student> searchPhone(String value) throws SQLException, ClientException {
		
		return StudentDAO.getInstance().searchPhone(value);
	}
			
	public Vector<Student> getStudentVector() throws SQLException, ClientException{
		
		this.studentVector = StudentDAO.getInstance().searchAllStudents();
		return this.studentVector;
	}
	
	public void insert(String name, String cpf, String registration, 
					   String phone, String email) throws ClientException, SQLException {
		
		Student student = new Student(name, cpf, registration, phone, email);
		StudentDAO.getInstance().include(student);
		this.studentVector.add(student);
	}

	public void alterate(String name, String cpf, String registration,
			String phone, String email, Student student) throws ClientException, SQLException {
	
		Student student_old = new Student(student.getName(), student.getCpf(), 
				student.getRegistration(), student.getPhone(), student.getEmail());
		
		student.setName(name);
		student.setCpf(cpf);
		student.setRegistration(registration);
		student.setPhone(phone);
		student.setEmail(email);
		StudentDAO.getInstance().alterate(student_old, student);
	}

	public void delete(Student student) throws SQLException, ClientException {
		
		StudentDAO.getInstance().delete(student);
		this.studentVector.remove(student);
	}

}
