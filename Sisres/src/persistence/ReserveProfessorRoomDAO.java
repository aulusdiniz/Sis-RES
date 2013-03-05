package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import model.Professor;
import model.ReserveRoomProfessor;
import model.Room;

import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveProfessorRoomDAO extends DAO{

	private final String NULL = "Termo nulo.";
	private final String ROOM_UNAVAILABLE = "A Room esta reservada no mesmo dia e horario.";
	private final String PROFESSOR_NOT_EXISTING = "Professor inexistente.";
	private final String ROOM_NOT_EXISTING = "Room inexistente";
	private final String RESERVE_NOT_EXISTING = "Reserva inexistente";
	private final String RESERVE_EXISTING = "A reserva ja existe.";
	private final String DATE_IN_PAST = "A data escolhida ja passou.";
	private final String HOUR_IN_PAST = "A hora escolhida ja passou.";
	
	
		private static ReserveProfessorRoomDAO instance;
		private ReserveProfessorRoomDAO(){
		}
		public static ReserveProfessorRoomDAO getInstance(){
			if(instance == null)
				instance = new ReserveProfessorRoomDAO();
			return instance;
		}

			private String findProfessorById(Professor p){
				return "SELECT id_professor FROM professor WHERE " +
						"professor.nome = \"" + p.getName() + "\" and " +
						"professor.cpf = \"" + p.getCpf() + "\" and " +
						"professor.telefone = \"" + p.getPhone() + "\" and " +
						"professor.email = \"" + p.getEmail() + "\" and " +
						"professor.matricula = \"" + p.getRegistration() + "\"";
			}
			private String findRoomById(Room room){
				return "SELECT id_room FROM room WHERE " +
						"room.codigo = \"" + room.getCode() + "\" and " +
						"room.descricao = \"" + room.getDescription() +  "\" and " +
						"room.capacidade = " + room.getCapacity();
			}
			private String findReserveByRoomAndProfessor(ReserveRoomProfessor r){
				return " WHERE " +
				"id_professor = ( " + findProfessorById(r.getProfessor()) + " ) and " +
				"id_room = ( " + findRoomById(r.getRoom()) + " ) and " +
				"finalidade = \"" + r.getFinality() + "\" and " +
				"hora = \"" + r.getHour() + "\" and " +
				"data = \"" + r.getDate() + "\"";
			}
			private String findValuesOfReserve(ReserveRoomProfessor r){
				return "( " + findProfessorById(r.getProfessor()) + " ), " +
				"( " + findRoomById(r.getRoom()) + " ), " +
				"\"" + r.getFinality() + "\", " +
				"\"" + r.getHour() + "\", " +
				"\"" + r.getDate() + "\"";
			}
			private String findAtributesValueByRoomAndProfessor(ReserveRoomProfessor r){
				return "id_professor = ( " + findProfessorById(r.getProfessor()) + " ), " +
				"id_room = ( " + findRoomById(r.getRoom()) + " ), " +
				"finalidade = \"" + r.getFinality() + "\", " +
				"hora = \"" + r.getHour() + "\", " +
				"data = \"" + r.getDate() + "\"";
			}
		
			private String insert(ReserveRoomProfessor r){
				return "INSERT INTO " +
						"reserva_room_professor (id_professor, id_room, finalidade, hora, data) " +
						"VALUES ( " + findValuesOfReserve(r) + " );";
			}
			
			private String deleteFromProfessor(ReserveRoomProfessor r){
				return "DELETE FROM reserva_room_professor " + this.findReserveByRoomAndProfessor(r) + " ;";
			}
			
			private String deleteFromStudent(ReserveRoomProfessor r){
				return "DELETE FROM reserva_room_aluno WHERE " +
						"hora = \"" + r.getHour() + "\" and " +
						"data = \"" + r.getDate() +  "\" ;";
			}
			
			private String update(ReserveRoomProfessor r, ReserveRoomProfessor r2){
				return "UPDATE reserva_room_professor SET " + 
						this.findAtributesValueByRoomAndProfessor(r2) +
						this.findReserveByRoomAndProfessor(r) + " ;";
			}
			
	public void include(ReserveRoomProfessor r) throws ReserveException, SQLException {
		if(r == null)
			throw new ReserveException(NULL);
		else if(!this.professorInDB(r.getProfessor()))
			throw new ReserveException(PROFESSOR_NOT_EXISTING);
		else if(!this.roomInDB(r.getRoom()))
			throw new ReserveException(ROOM_NOT_EXISTING);
		else if(this.roomInReserveDB(r.getRoom(), r.getDate(), r.getHour()))
			throw new ReserveException(ROOM_UNAVAILABLE);
		else if(this.reserveInDB(r))
			throw new ReserveException(RESERVE_EXISTING);
		else if(this.studentInReserveDB(r.getDate(), r.getHour()))
				super.executeQuery(this.deleteFromStudent(r));
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
	
	public void alterate(ReserveRoomProfessor r, ReserveRoomProfessor r_new) throws ReserveException, SQLException {
		if(r == null)
			throw new ReserveException(NULL);
		else if(r_new == null)
			throw new ReserveException(NULL);
		
		else if(!this.reserveInDB(r))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else if(this.reserveInDB(r_new))
			throw new ReserveException(RESERVE_EXISTING);
		else if(!this.professorInDB(r_new.getProfessor()))
			throw new ReserveException(PROFESSOR_NOT_EXISTING);
		else if(!this.roomInDB(r_new.getRoom()))
			throw new ReserveException(ROOM_NOT_EXISTING);
		else if(!r.getDate().equals(r_new.getDate()) || !r.getHour().equals(r_new.getHour())) {
			 if(this.roomInReserveDB(r_new.getRoom(), r_new.getDate(), r_new.getHour()))
				throw new ReserveException(ROOM_UNAVAILABLE);
		}		
		if(this.dataPassou(r_new.getDate()))
			throw new ReserveException(DATE_IN_PAST);
		if(this.horaPassou(r_new.getHour()) && this.dataIgual(r_new.getDate()))
			throw new ReserveException(HOUR_IN_PAST);
		else
			super.updateQuery(this.update(r, r_new));
	}
	
	public void delete(ReserveRoomProfessor r) throws ReserveException, SQLException {
		if(r == null)
			throw new ReserveException(NULL);
		else if(!this.reserveInDB(r))
			throw new ReserveException(RESERVE_NOT_EXISTING);
		else{
			super.executeQuery(this.deleteFromProfessor(r));
		}
	}

	@SuppressWarnings("unchecked")
	public Vector<ReserveRoomProfessor> findAll() throws SQLException, ClientException, PatrimonyException, ReserveException{
		return super.find("SELECT * FROM reserva_room_professor " +
				"INNER JOIN room ON room.id_room = reserva_room_professor.id_room " +
				"INNER JOIN professor ON professor.id_professor = reserva_room_professor.id_professor;");
	}

	
	@SuppressWarnings("unchecked")
	public Vector<ReserveRoomProfessor> findByDate(String data) throws SQLException, ClientException, PatrimonyException, ReserveException{
		return super.find("SELECT * FROM reserva_room_professor " +
				"INNER JOIN room ON room.id_room = reserva_room_professor.id_room " +
				"INNER JOIN professor ON professor.id_professor = reserva_room_professor.id_professor" +
				" WHERE data = \"" + this.padronizeDate(data) + "\";");
	} 
	
	
	@Override
	protected Object fetch(ResultSet rs) throws SQLException, ClientException, PatrimonyException, ReserveException {
		Professor p = new Professor(rs.getString("nome"), rs.getString("cpf"), rs.getString("matricula"),
				rs.getString("telefone"), rs.getString("email"));
		
		Room s = new Room(rs.getString("codigo"), rs.getString("descricao"), rs.getString("capacidade"));
		
		ReserveRoomProfessor r = new ReserveRoomProfessor(rs.getString("data"),rs.getString("hora"),
				s ,rs.getString("finalidade"), p);
		
		return r;
	}
	
	private boolean professorInDB(Professor professor) throws SQLException{
		return super.inDBGeneric("SELECT * FROM professor WHERE " +
				"professor.nome = \"" + professor.getName() + "\" and " +
				"professor.cpf = \"" + professor.getCpf() + "\" and " +
				"professor.telefone = \"" + professor.getPhone() + "\" and " +
				"professor.email = \"" + professor.getEmail() + "\" and " +
				"professor.matricula = \"" + professor.getRegistration() + "\";");
	}
	
	private boolean roomInDB(Room room) throws SQLException{
		return super.inDBGeneric("SELECT * FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() + "\" and " +
				"room.capacidade = " + room.getCapacity() +
				";");
	}
	
	private boolean roomInReserveDB(Room room, String data, String hora) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_room_professor WHERE " +
				"data = \"" + data + "\" and " +
				"hora = \"" + hora + "\" and " +
				"id_room = (SELECT id_room FROM room WHERE " +
				"room.codigo = \"" + room.getCode() + "\" and " +
				"room.descricao = \"" + room.getDescription() +  "\" and " +
				"room.capacidade = " + room.getCapacity() +" );");
	}
	
	private boolean reserveInDB(ReserveRoomProfessor r) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_room_professor WHERE " +
					"id_professor = (SELECT id_professor FROM professor WHERE " +
							"professor.nome = \"" + r.getProfessor().getName() + "\" and " +
							"professor.cpf = \"" + r.getProfessor().getCpf() + "\" and " +
							"professor.telefone = \"" + r.getProfessor().getPhone() + "\" and " +
							"professor.email = \"" + r.getProfessor().getEmail() + "\" and " +
							"professor.matricula = \"" + r.getProfessor().getRegistration() + "\") and " +
					"id_room = (SELECT id_room FROM room WHERE " +
									"room.codigo = \"" + r.getRoom().getCode() + "\" and " +
									"room.descricao = \"" + r.getRoom().getDescription() +  "\" and " +
									"room.capacidade = " + r.getRoom().getCapacity() +" ) and " +
					"finalidade = \"" + r.getFinality() + "\" and " +
					"hora = \"" + r.getHour() + "\" and " +
					"data = \"" + r.getDate() + "\";");
	}
	private boolean studentInReserveDB(String data, String hora) throws SQLException {
		return super.inDBGeneric("SELECT * FROM reserva_room_aluno WHERE " +
				"data = \"" + data + "\" and " +
				"hora = \"" + hora + "\";");
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
	/*
	private String padronizarHora(String hora){
		if(hora.length() == 4)
			return "0" + hora;
		return hora;
	}*/
}