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
			if(instance == null){
				instance = new ReserveStudentRoomDAO();
			}
			else{
				//nothing here
			}
			
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
					"reservedChairs = " + reserveStudentRoom.getReservedChairs();
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
					"reservedChairs = " + r.getReservedChairs();
		}
		private String insert(ReserveStudentRoom r){
			return "INSERT INTO " +
					"reserva_sala_aluno (id_aluno, id_sala, finalidade, hora, data, reservedChairs) " +
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
		if(this.dateInPast(r.getDate()))
			throw new ReserveException(DATE_IN_PAST);
		if(this.dateEquals(r.getDate()))
		{
			if(this.hourInPast(r.getHour()))
				throw new ReserveException(HOUR_IN_PAST);
			else
				super.executeQuery(this.insert(r));
		}
		else
			super.executeQuery(this.insert(r));
	}
	
	public void alterate(ReserveStudentRoom reserveStudentRoom,
						 ReserveStudentRoom newReserveStudentRoom) throws ReserveException, SQLException, 
						 												  ClientException, PatrimonyException{
		if(reserveStudentRoom == null)
			throw new ReserveException(NULL);
		else if(newReserveStudentRoom == null)
			throw new ReserveException(NULL);
		
		else if(!this.reserveInDB(reserveStudentRoom))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else if(this.reserveInDB(newReserveStudentRoom))
			throw new ReserveException(RESERVE_EXISTING);
		else if(!this.studentInDB(newReserveStudentRoom.getStudent()))
			throw new ReserveException(STUDENT_NOT_EXISTING);
		else if(!this.roomInDB(newReserveStudentRoom.getRoom()))
			throw new ReserveException(ROOM_NOT_EXISTING);
		else if(!reserveStudentRoom.getDate().equals(newReserveStudentRoom.getDate()) || !reserveStudentRoom.getHour().equals(newReserveStudentRoom.getHour())){
			if(this.studentInReserveDB(newReserveStudentRoom.getStudent(), newReserveStudentRoom.getDate(), newReserveStudentRoom.getHour()))
				throw new ReserveException(STUDENT_UNAVAILABLE);
			else if(this.roomInTeachersReserveDB(newReserveStudentRoom.getRoom(), newReserveStudentRoom.getDate(), newReserveStudentRoom.getHour()))
				throw new ReserveException(ROOM_UNAVAILABLE);
		}
		if(!this.thereAreChairs(""+(Integer.parseInt(newReserveStudentRoom.getReservedChairs()) - 
				Integer.parseInt(reserveStudentRoom.getReservedChairs())), newReserveStudentRoom.getRoom(), 
				newReserveStudentRoom.getDate(), newReserveStudentRoom.getHour()))
			throw new ReserveException(CHAIRS_UNAVAILABLE);
		if(this.dateInPast(newReserveStudentRoom.getDate()))
			throw new ReserveException(DATE_IN_PAST);
		if(this.hourInPast(newReserveStudentRoom.getHour()) && this.dateEquals(newReserveStudentRoom.getDate()))
			throw new ReserveException(HOUR_IN_PAST);
		else
			super.updateQuery(this.update(reserveStudentRoom, newReserveStudentRoom));
			
	}
	
	public void delete(ReserveStudentRoom reserveStudentRoom) throws ReserveException, SQLException {
		if(reserveStudentRoom == null)
			throw new ReserveException(NULL);
		else if(!this.reserveInDB(reserveStudentRoom))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else
			super.executeQuery(this.deleteFrom(reserveStudentRoom));
	}
	
	public Vector<ReserveStudentRoom> findAll() throws SQLException, ClientException, PatrimonyException, ReserveException{
		return super.find("SELECT * FROM reserva_sala_aluno " +
						  "INNER JOIN room ON room.id_sala = reserva_sala_aluno.id_sala " +
						  "INNER JOIN student ON student.id_aluno = reserva_sala_aluno.id_aluno;");
	}
	public Vector<ReserveStudentRoom> findByDate(String date) throws SQLException, ClientException, PatrimonyException, ReserveException{
		date = this.padronizateDate(date);
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

	
	public int availableChairs(Room room, String date, 
							   String hour) throws SQLException, PatrimonyException, ClientException, ReserveException{
		date = this.padronizateDate(date);
		hour = this.padronizeHour(hour);
		Vector<ReserveStudentRoom> vet = this.findAll();
		Iterator<ReserveStudentRoom> iterator =  vet.iterator();
		int total = Integer.parseInt(room.getCapacity());
		while(iterator.hasNext()){
			ReserveStudentRoom r = iterator.next();
			if(r.getRoom().equals(room) && r.getDate().equals(date) && r.getHour().equals(hour))
				total -= Integer.parseInt(r.getReservedChairs());
		}
		return total;
	}
	
	
	private boolean thereAreChairs(String reservedChairs, Room room,
								   String date, String hour) throws SQLException, ClientException, PatrimonyException, ReserveException {
		if(this.availableChairs(room, date, hour) >= Integer.parseInt(reservedChairs))
			return true;
		return false;
	}
	
	@Override
	protected Object fetch(ResultSet resultSet) throws SQLException, ClientException, PatrimonyException, ReserveException {
		Student student = new Student(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("matricula"),
				resultSet.getString("telefone"), resultSet.getString("email"));
		
		Room s = new Room(resultSet.getString("codigo"), resultSet.getString("descricao"), resultSet.getString("capacidade"));
		
		ReserveStudentRoom reserveStudentRoom = new ReserveStudentRoom(resultSet.getString("data"),resultSet.getString("hora"),
				s ,resultSet.getString("finalidade"),resultSet.getString("reservedChairs"), student);
		
		return reserveStudentRoom;
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
								 "sala.capacidade = " + room.getCapacity() + ";");
	}
	
	private boolean studentInReserveDB(Student student, String date, String hour) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
								 "data = \"" + date + "\" and " +
								 "hora = \"" + hour + "\" and " +
								 "id_aluno = (SELECT id_aluno FROM student WHERE " +
								 "aluno.nome = \"" + student.getName() + "\" and " +
								 "aluno.cpf = \"" + student.getCpf() + "\" and " +
							 	 "aluno.telefone = \"" + student.getPhone() + "\" and " +
								 "aluno.email = \"" + student.getEmail() + "\" and " +
								 "aluno.matricula = \"" + student.getRegistration() + "\");");
	}
	private boolean roomInTeachersReserveDB(Room room, String date, String hour) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_sala_professor WHERE " +
								 "data = \"" + this.padronizateDate(date) + "\" and " +
								 "hora = \"" + this.padronizeHour(hour) + "\" and " +
								 "id_sala = (SELECT id_sala FROM room WHERE " +
								 "room.codigo = \"" + room.getCode() + "\" and " +
								 "room.descricao = \"" + room.getDescription() +  "\" and " +
								 "room.capacidade = " + room.getCapacity() +" );");
	}
	
	private boolean reserveInDB(ReserveStudentRoom reserveStudentRoom) throws SQLException {
		
		return super.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
								 "id_aluno = (SELECT id_aluno FROM student WHERE " +
								 "student.nome = \"" + reserveStudentRoom.getStudent().getName() + "\" and " +
								 "student.cpf = \"" + reserveStudentRoom.getStudent().getCpf() + "\" and " +
								 "student.telefone = \"" + reserveStudentRoom.getStudent().getPhone() + "\" and " +
								 "student.email = \"" + reserveStudentRoom.getStudent().getEmail() + "\" and " +
								 "student.matricula = \"" + reserveStudentRoom.getStudent().getRegistration() + "\") and " +
								 "id_sala = (SELECT id_sala FROM room WHERE " +
								 "room.codigo = \"" + reserveStudentRoom.getRoom().getCode() + "\" and " +
								 "room.descricao = \"" + reserveStudentRoom.getRoom().getDescription() +  "\" and " +
								 "room.capacidade = " + reserveStudentRoom.getRoom().getCapacity() +" ) and " +
								 "finalidade = \"" + reserveStudentRoom.getFinality() + "\" and " +
								 "hora = \"" + reserveStudentRoom.getHour() + "\" and " +
								 "data = \"" + reserveStudentRoom.getDate() + "\" and " +
								 "reservedChairs = " + reserveStudentRoom.getReservedChairs() + ";");
	}

	private String actualDate(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		return formater.format(date);
	}
	
	private String actualHour(){
		Date date = new Date(System.currentTimeMillis());
		return date.toString().substring(11, 16);
	}
	
	private boolean dateInPast(String d){
		String now[] = this.actualDate().split("[./-]");
		String date[] = d.split("[./-]");
		
		int dif = now[2].length() - date[2].length();
		date[2] = now[2].substring(0, dif) + date[2];
		
		if(Integer.parseInt(now[2]) > Integer.parseInt(date[2]))
			return true;
		
		dif = now[1].length() - date[1].length();
		date[1] = now[1].substring(0, dif) + date[1];
		
		if(Integer.parseInt(now[1]) > Integer.parseInt(date[1]))
			return true;
		else if(Integer.parseInt(now[1]) == Integer.parseInt(date[1])){
			dif = now[0].length() - date[0].length();
			date[0] = now[0].substring(0, dif) + date[0];
			
			if(Integer.parseInt(now[0]) > Integer.parseInt(date[0]))
				return true;
		}
		return false;
	}
	
	public boolean dateEquals(String d){
		d = this.padronizateDate(d);
		String now[] = this.actualDate().split("[./-]");
		String date[] = d.split("[./-]");
		
		if(now[0].equals(date[0]) && now[1].equals(date[1]) && now[2].equals(date[2]))
			return true;
		return false;
	}
	
	private boolean hourInPast(String hour){
		String now = this.actualHour();
		if(hour.length() == 4)
			hour = "0" + hour;
		if(Integer.parseInt(now.substring(0, 2)) > Integer.parseInt(hour.substring(0, 2)))
			return true;
		else if(Integer.parseInt(now.substring(0, 2)) == Integer.parseInt(hour.substring(0, 2))){
			if(Integer.parseInt(now.substring(3, 5)) > Integer.parseInt(hour.substring(3, 5)))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	private String padronizateDate(String d){
		String now[] = actualDate().split("[./-]");
		String parts[] = d.split("[./-]");
		String datePattern = "";
		
		for(int i = 0; i < 3; i++){
			if(i == 0)
				datePattern += now[i].substring(0, 
						now[i].length() - parts[i].length()) + parts[i];
			else
				datePattern +=  "/" + now[i].substring(0, 
						now[i].length() - parts[i].length()) + parts[i];
				
		}
		
		return datePattern;
	}
	
	private String padronizeHour(String hour){
		if(hour.length() == 4)
			return "0" + hour;
		return hour;
	}
	
}
