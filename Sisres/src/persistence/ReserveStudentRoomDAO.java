package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

import model.Student;
import model.ReserveStudentRoom;
import model.Room;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReserveStudentRoomDAO extends DAO{
	

		private final String NULL = "Termo nulo.";
		private final String STUDENT_UNAVAILABLE = "O student possui uma reserva no mesmo dia e horario.";
		private final String ROOM_UNAVAILABLE = "A Room esta reservada no mesmo dia e horario.";
		private final String STUDENT_NOT_EXISTING = "Student inexistente.";
		private final String ROOM_NOT_EXISTING = "Room inexistente";
		private final String RESERVE_NOT_EXISTING = "Reserva inexistente";
		private final String RESERVE_EXISTING = "A reserva ja existe.";
		private final String CHAIRS_UNAVAILABLE = "O numero de cadeiras reservadas esta indisponivel para esta room.";
		private final String DATE_IN_PAST = "A data escolhida ja passou.";
		private final String HOUR_IN_PAST = "A hora escolhida ja passou.";

	
		private static ReserveStudentRoomDAO instance;
		private ReserveStudentRoomDAO(){
		}
		public static ReserveStudentRoomDAO getInstance(){
			if(instance == null)
				instance = new ReserveStudentRoomDAO();
			return instance;
		}

		private String findStudentById(Student student){
			return "SELECT id_aluno FROM student WHERE " +
					"student.nome = \"" + student.getName() + "\" and " +
					"student.cpf = \"" + student.getCpf() + "\" and " +
					"student.telefone = \"" + student.getPhone() + "\" and " +
					"student.email = \"" + student.getEmail() + "\" and " +
					"student.matricula = \"" + student.getRegistration() + "\"";
		}
		private String findRoomById(Room room){
			return "SELECT id_sala FROM room WHERE " +
					"room.codigo = \"" + room.getCode() + "\" and " +
					"room.descricao = \"" + room.getDescription() +  "\" and " +
					"room.capacidade = " + room.getCapacity();
		}
		private String findReserveByRoomAndStudent(ReserveStudentRoom reserveStudentRoom){
			return " WHERE " +
			"id_aluno = ( " + findStudentById(reserveStudentRoom.getStudent()) + " ) and " +
			"id_sala = ( " + findRoomById(reserveStudentRoom.getRoom()) + " ) and " +
			"finalidade = \"" + reserveStudentRoom.getFinality() + "\" and " +
			"hora = \"" + reserveStudentRoom.getHour() + "\" and " +
			"data = \"" + reserveStudentRoom.getDate() + "\" and " +
			"cadeiras_reservadas = " + reserveStudentRoom.getReservedChairs();
		}
		private String findValuesOfReserve(ReserveStudentRoom r){
			return "( " + findStudentById(r.getStudent()) + " ), " +
			"( " + findRoomById(r.getRoom()) + " ), " +
			"\"" + r.getFinality() + "\", " +
			"\"" + r.getHour() + "\", " +
			"\"" + r.getDate() + "\", " +
			r.getReservedChairs();
		}
		private String findAtributesValueByRoomAndStudent(ReserveStudentRoom r){
			return "id_aluno = ( " + findStudentById(r.getStudent()) + " ), " +
			"id_sala = ( " + findRoomById(r.getRoom()) + " ), " +
			"finalidade = \"" + r.getFinality() + "\", " +
			"hora = \"" + r.getHour() + "\", " +
			"data = \"" + r.getDate() + "\", " +
			"cadeiras_reservadas = " + r.getReservedChairs();
		}
		private String insert(ReserveStudentRoom r){
			return "INSERT INTO " +
					"reserva_sala_aluno (id_aluno, id_sala, finalidade, hora, data, cadeiras_reservadas) " +
					"VALUES ( " + findValuesOfReserve(r) + " );";
		}
		private String update(ReserveStudentRoom r, ReserveStudentRoom r2){
			return "UPDATE reserva_sala_aluno SET " + 
					this.findAtributesValueByRoomAndStudent(r2) +
					this.findReserveByRoomAndStudent(r) + " ;";
		}
		private String deleteFrom(ReserveStudentRoom r){
			return "DELETE FROM reserva_sala_aluno " + this.findReserveByRoomAndStudent(r) + " ;";
		}

		
		
	public void include(ReserveStudentRoom r) throws ReserveException, SQLException, ClientException, PatrimonyException {
		if(r == null)
			throw new ReserveException(NULL);
		else if(!this.studentInDB(r.getStudent()))
			throw new ReserveException(STUDENT_NOT_EXISTING);
		else if(!this.roomInDB(r.getRoom()))
			throw new ReserveException(ROOM_NOT_EXISTING);
		else if(this.roomInTeachersReserveDB(r.getRoom(), r.getDate(), r.getHour()))
			throw new ReserveException(ROOM_UNAVAILABLE);
		else if(this.studentInReserveDB(r.getStudent(), r.getDate(), r.getHour()))
			throw new ReserveException(STUDENT_UNAVAILABLE);
		else if(!this.thereAreChairs(r.getReservedChairs(), r.getRoom(), r.getDate(), r.getHour()))
			throw new ReserveException(CHAIRS_UNAVAILABLE);
		if(this.dataPassou(r.getDate()))
			throw new ReserveException(DATE_IN_PAST);
		if(this.dataIgual(r.getDate()))
		{
			if(this.horaPassou(r.getHour()))
				throw new ReserveException(HOUR_IN_PAST);
			else
				super.executeQuery(this.insert(r));
		}
		else
			super.executeQuery(this.insert(r));
	}
	
	public void alterate(ReserveStudentRoom r, ReserveStudentRoom r_new) throws ReserveException, SQLException, ClientException, PatrimonyException{
		if(r == null)
			throw new ReserveException(NULL);
		else if(r_new == null)
			throw new ReserveException(NULL);
		
		else if(!this.reserveInDB(r))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else if(this.reserveInDB(r_new))
			throw new ReserveException(RESERVE_EXISTING);
		else if(!this.studentInDB(r_new.getStudent()))
			throw new ReserveException(STUDENT_NOT_EXISTING);
		else if(!this.roomInDB(r_new.getRoom()))
			throw new ReserveException(ROOM_NOT_EXISTING);
		else if(!r.getDate().equals(r_new.getDate()) || !r.getHour().equals(r_new.getHour())){
			if(this.studentInReserveDB(r_new.getStudent(), r_new.getDate(), r_new.getHour()))
				throw new ReserveException(STUDENT_UNAVAILABLE);
			else if(this.roomInTeachersReserveDB(r_new.getRoom(), r_new.getDate(), r_new.getHour()))
				throw new ReserveException(ROOM_UNAVAILABLE);
		}
		if(!this.thereAreChairs(""+(Integer.parseInt(r_new.getReservedChairs()) - 
				Integer.parseInt(r.getReservedChairs())), r_new.getRoom(), 
				r_new.getDate(), r_new.getHour()))
			throw new ReserveException(CHAIRS_UNAVAILABLE);
		if(this.dataPassou(r_new.getDate()))
			throw new ReserveException(DATE_IN_PAST);
		if(this.horaPassou(r_new.getHour()) && this.dataIgual(r_new.getDate()))
			throw new ReserveException(HOUR_IN_PAST);
		else
			super.updateQuery(this.update(r, r_new));
			
	}
	
	public void delete(ReserveStudentRoom r) throws ReserveException, SQLException {
		if(r == null)
			throw new ReserveException(NULL);
		else if(!this.reserveInDB(r))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else
			super.executeQuery(this.deleteFrom(r));
	}
	
	public Vector<ReserveStudentRoom> findAll() throws SQLException, ClientException, PatrimonyException, ReserveException{
		return super.find("SELECT * FROM reserva_sala_aluno " +
				"INNER JOIN room ON room.id_sala = reserva_sala_aluno.id_sala " +
				"INNER JOIN student ON student.id_aluno = reserva_sala_aluno.id_aluno;");
	}
	public Vector<ReserveStudentRoom> findByDate(String date) throws SQLException, ClientException, PatrimonyException, ReserveException{
		date = this.padronizeDate(date);
		return super.find("SELECT * FROM reserva_sala_aluno " +
				"INNER JOIN room ON room.id_sala = reserva_sala_aluno.id_sala " +
				"INNER JOIN student ON student.id_aluno = reserva_sala_aluno.id_aluno " +
				"WHERE data = \""+ date + "\";");
	}
	public Vector<ReserveStudentRoom> findByHour(String hour) 
			throws SQLException, ClientException, PatrimonyException, ReserveException{
		hour = this.padronizeHour(hour);
		return super.find("SELECT * FROM reserva_sala_aluno " +
				"INNER JOIN room ON room.id_sala = reserva_sala_aluno.id_sala " +
				"INNER JOIN student ON student.id_aluno = reserva_sala_aluno.id_aluno " +
				" WHERE hora = \"" + hour +"\";");
	}

	
	public int availableChairs(Room room, String data, String hora) 
			throws SQLException, PatrimonyException, ClientException, ReserveException{
		data = this.padronizeDate(data);
		hora = this.padronizeHour(hora);
		Vector<ReserveStudentRoom> vet = this.findAll();
		Iterator<ReserveStudentRoom> iterator =  vet.iterator();
		int total = Integer.parseInt(room.getCapacity());
		while(iterator.hasNext()){
			ReserveStudentRoom r = iterator.next();
			if(r.getRoom().equals(room) && r.getDate().equals(data) && r.getHour().equals(hora))
				total -= Integer.parseInt(r.getReservedChairs());
		}
		return total;
	}
	
	
	private boolean thereAreChairs(String cadeiras_reservadas, Room room, String data, String hora) 
			throws SQLException, ClientException, PatrimonyException, ReserveException {
		if(this.availableChairs(room, data, hora) >= Integer.parseInt(cadeiras_reservadas))
			return true;
		return false;
	}
	
	@Override
	protected Object fetch(ResultSet rs) throws SQLException, ClientException, PatrimonyException, ReserveException {
		Student student = new Student(rs.getString("nome"), rs.getString("cpf"), rs.getString("matricula"),
				rs.getString("telefone"), rs.getString("email"));
		
		Room s = new Room(rs.getString("codigo"), rs.getString("descricao"), rs.getString("capacidade"));
		
		ReserveStudentRoom r = new ReserveStudentRoom(rs.getString("data"),rs.getString("hora"),
				s ,rs.getString("finalidade"),rs.getString("cadeiras_reservadas"), student);
		
		return r;
	}
	
	private boolean studentInDB(Student student) throws SQLException{
		return super.inDBGeneric("SELECT * FROM student WHERE " +
				"aluno.nome = \"" + student.getName() + "\" and " +
				"aluno.cpf = \"" + student.getCpf() + "\" and " +
				"aluno.telefone = \"" + student.getPhone() + "\" and " +
				"aluno.email = \"" + student.getEmail() + "\" and " +
				"aluno.matricula = \"" + student.getRegistration() + "\";");
	}
	
	private boolean roomInDB(Room room) throws SQLException{
		return super.inDBGeneric("SELECT * FROM room WHERE " +
				"sala.codigo = \"" + room.getCode() + "\" and " +
				"sala.descricao = \"" + room.getDescription() + "\" and " +
				"sala.capacidade = " + room.getCapacity() +
				";");
	}
	
	private boolean studentInReserveDB(Student student, String data, String hora) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
				"data = \"" + data + "\" and " +
				"hora = \"" + hora + "\" and " +
				"id_aluno = (SELECT id_aluno FROM student WHERE " +
				"aluno.nome = \"" + student.getName() + "\" and " +
				"aluno.cpf = \"" + student.getCpf() + "\" and " +
				"aluno.telefone = \"" + student.getPhone() + "\" and " +
				"aluno.email = \"" + student.getEmail() + "\" and " +
				"aluno.matricula = \"" + student.getRegistration() + "\");");
	}
	private boolean roomInTeachersReserveDB(Room room, String data, String hora) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_sala_professor WHERE " +
				"data = \"" + this.padronizeDate(data) + "\" and " +
				"hora = \"" + this.padronizeHour(hora) + "\" and " +
				"id_sala = (SELECT id_sala FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity() +" );");
	}
	
	private boolean reserveInDB(ReserveStudentRoom r) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
					"id_aluno = (SELECT id_aluno FROM student WHERE " +
							"student.nome = \"" + r.getStudent().getName() + "\" and " +
							"student.cpf = \"" + r.getStudent().getCpf() + "\" and " +
							"student.telefone = \"" + r.getStudent().getPhone() + "\" and " +
							"student.email = \"" + r.getStudent().getEmail() + "\" and " +
							"student.matricula = \"" + r.getStudent().getRegistration() + "\") and " +
					"id_sala = (SELECT id_sala FROM room WHERE " +
									"room.codigo = \"" + r.getRoom().getCode() + "\" and " +
									"room.descricao = \"" + r.getRoom().getDescription() +  "\" and " +
									"room.capacidade = " + r.getRoom().getCapacity() +" ) and " +
					"finalidade = \"" + r.getFinality() + "\" and " +
					"hora = \"" + r.getHour() + "\" and " +
					"data = \"" + r.getDate() + "\" and " +
					"cadeiras_reservadas = " + r.getReservedChairs() + ";");
	}

	private String actualDate(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		return formatador.format(date);
	}
	
	private String actualHour(){
		Date date = new Date(System.currentTimeMillis());
		return date.toString().substring(11, 16);
	}
	
	private boolean dataPassou(String d){
		String agora[] = this.actualDate().split("[./-]");
		String data[] = d.split("[./-]");
		
		int dif = agora[2].length() - data[2].length();
		data[2] = agora[2].substring(0, dif) + data[2];
		
		if(Integer.parseInt(agora[2]) > Integer.parseInt(data[2]))
			return true;
		
		dif = agora[1].length() - data[1].length();
		data[1] = agora[1].substring(0, dif) + data[1];
		
		if(Integer.parseInt(agora[1]) > Integer.parseInt(data[1]))
			return true;
		else if(Integer.parseInt(agora[1]) == Integer.parseInt(data[1])){
			dif = agora[0].length() - data[0].length();
			data[0] = agora[0].substring(0, dif) + data[0];
			
			if(Integer.parseInt(agora[0]) > Integer.parseInt(data[0]))
				return true;
		}
		return false;
	}
	
	public boolean dataIgual(String d){
		d = this.padronizeDate(d);
		String agora[] = this.actualDate().split("[./-]");
		String data[] = d.split("[./-]");
		
		if(agora[0].equals(data[0]) && agora[1].equals(data[1]) && agora[2].equals(data[2]))
			return true;
		return false;
	}
	
	private boolean horaPassou(String hora){
		String agora = this.actualHour();
		if(hora.length() == 4)
			hora = "0" + hora;
		if(Integer.parseInt(agora.substring(0, 2)) > Integer.parseInt(hora.substring(0, 2)))
			return true;
		else if(Integer.parseInt(agora.substring(0, 2)) == Integer.parseInt(hora.substring(0, 2))){
			if(Integer.parseInt(agora.substring(3, 5)) > Integer.parseInt(hora.substring(3, 5)))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	private String padronizeDate(String data){
		String agora[] = actualDate().split("[./-]");
		String partes[] = data.split("[./-]");
		String dataNoPadrao = "";
		
		for(int i = 0; i < 3; i++){
			if(i == 0)
				dataNoPadrao += agora[i].substring(0, 
						agora[i].length() - partes[i].length()) + partes[i];
			else
				dataNoPadrao +=  "/" + agora[i].substring(0, 
						agora[i].length() - partes[i].length()) + partes[i];
				
		}
		
		return dataNoPadrao;
	}
	
	private String padronizeHour(String hora){
		if(hora.length() == 4)
			return "0" + hora;
		return hora;
	}
	
}
