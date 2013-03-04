package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;


import model.Equipament;
import model.Professor;
import model.ReserveEquipamentProfessor;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public class ReserveEquipamentProfessorDAO extends DAO {


    private final String NULL = "Termo nulo.";
    private final String EQUIPAMENT_UNAVAILABLE = "O Equipamento esta reservada no mesmo dia e horario.";
    private final String PROFESSOR_NOT_EXISTING = "Professor inexistente.";
    private final String EQUIPAMENT_NOT_EXISTING = "Equipamento inexistente";
    private final String RESERVE_NOT_EXISTING = "Reserva inexistente";
    private final String RESERVE_EXISTING = "A reserva ja existe.";

    private static ReserveEquipamentProfessorDAO instance;

    private ReserveEquipamentProfessorDAO() {
    }

    public static ReserveEquipamentProfessorDAO getInstance() {
        if (instance == null)
            instance = new ReserveEquipamentProfessorDAO();
        return instance;
    }

    private String findProfessorbyId(Professor professor) {
        return "SELECT id_professor FROM professor WHERE " + "professor.nome = \"" + professor.getName() + "\" and " + "professor.cpf = \""
                + professor.getCpf() + "\" and " + "professor.telefone = \"" + professor.getPhone() + "\" and " + "professor.email = \""
                + professor.getEmail() + "\" and " + "professor.matricula = \"" + professor.getRegistration() + "\"";
    }

    private String findEquipamentById(Equipament equipament) {
        return "SELECT id_equipamento FROM equipamento WHERE " + "equipamento.codigo = \"" + equipament.getCode() + "\" and "
                + "equipamento.descricao = \"" + equipament.getDescription();
    }

    private String findByEquipamentAndProfessor(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return " WHERE " + "id_professor = ( " + findProfessorbyId(reserveEquipamentProfessor.getProfessor()) + " ) and " + "id_equipamento = ( "
                + findEquipamentById(reserveEquipamentProfessor.getEquipament()) + " ) and " + "hora = \"" + reserveEquipamentProfessor.getHour() + "\" and " + "data = \""
                + reserveEquipamentProfessor.getDate();
    }

    private String findReserveValues(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return "( " + findProfessorbyId(reserveEquipamentProfessor.getProfessor()) + " ), " + "( " + findEquipamentById(reserveEquipamentProfessor.getEquipament()) + " ), "
                + "\"" + reserveEquipamentProfessor.getHour() + "\", " + "\"" + reserveEquipamentProfessor.getDate();
    }

    private String findAtributeValues(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return "id_professor = ( " + findProfessorbyId(reserveEquipamentProfessor.getProfessor()) + " ), " + "id_equipamento = ( "
                + findEquipamentById(reserveEquipamentProfessor.getEquipament()) + " ), " + "hora = \"" + reserveEquipamentProfessor.getHour() + "\", " + "data = \""
                + reserveEquipamentProfessor.getDate();
    }

    private String insert(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return "INSERT INTO " + "reserva_equipamento_professor (id_professor, id_equipamento, hora, data) " + "VALUES ( "
                + findReserveValues(reserveEquipamentProfessor) + " );";
    }

    private String update(ReserveEquipamentProfessor reserveEquipamentProfessor, ReserveEquipamentProfessor r2) {
        return "UPDATE reserva_equipamento_professor SET " + this.findAtributeValues(r2)
                + this.findByEquipamentAndProfessor(reserveEquipamentProfessor) + " ;";
    }

    private String deleteFromProfessor(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return "DELETE FROM reserva_equipamento_professor " + this.findByEquipamentAndProfessor(reserveEquipamentProfessor) + " ;";
    }

    private String deleteFromStudent(ReserveEquipamentProfessor reserveEquipamentProfessor) {
        return "DELETE FROM reserva_equipamento_aluno WHERE " + "hora = \"" + reserveEquipamentProfessor.getHour() + "\" and " + "data = \"" + reserveEquipamentProfessor.getDate()
                + " ;";
    }

    public void include(ReserveEquipamentProfessor reserveEquipamentProfessor) throws ReserveException, SQLException {
        if (reserveEquipamentProfessor == null)
            throw new ReserveException(NULL);
        else if (!this.professorInDB(reserveEquipamentProfessor.getProfessor()))
            throw new ReserveException(PROFESSOR_NOT_EXISTING);
        else if (!this.equipamentInDB(reserveEquipamentProfessor.getEquipament()))
            throw new ReserveException(EQUIPAMENT_NOT_EXISTING);
        else if (this.equipamentInReserveDB(reserveEquipamentProfessor.getEquipament(), reserveEquipamentProfessor.getDate(), reserveEquipamentProfessor.getHour()))
            throw new ReserveException(EQUIPAMENT_UNAVAILABLE);
        else if (this.professorInReserveDB(reserveEquipamentProfessor.getProfessor(), reserveEquipamentProfessor.getDate(), reserveEquipamentProfessor.getHour()))
            throw new ReserveException(RESERVE_EXISTING);
        else {
            super.executeQuery(this.deleteFromStudent(reserveEquipamentProfessor));
            super.executeQuery(this.insert(reserveEquipamentProfessor));
        }

    }

    public void alterate(ReserveEquipamentProfessor reserveEquipamentProfessor, ReserveEquipamentProfessor r_new) throws ReserveException, SQLException {
        if (reserveEquipamentProfessor == null)
            throw new ReserveException(NULL);
        else if (r_new == null)
            throw new ReserveException(NULL);

        else if (!this.reserveInDB(reserveEquipamentProfessor))
            throw new ReserveException(RESERVE_NOT_EXISTING);
        else if (this.reserveInDB(r_new))
            throw new ReserveException(RESERVE_EXISTING);
        else if (!reserveEquipamentProfessor.getDate().equals(r_new.getDate()) || !reserveEquipamentProfessor.getHour().equals(r_new.getHour())) {
            if (this.professorInReserveDB(r_new.getProfessor(), r_new.getDate(), r_new.getHour()))
                throw new ReserveException(RESERVE_EXISTING);
            else if (this.equipamentInReserveDB(r_new.getEquipament(), r_new.getDate(), r_new.getHour()))
                throw new ReserveException(EQUIPAMENT_UNAVAILABLE);
        } else if (!this.professorInDB(r_new.getProfessor()))
            throw new ReserveException(PROFESSOR_NOT_EXISTING);
        else if (!this.equipamentInDB(r_new.getEquipament()))
            throw new ReserveException(EQUIPAMENT_NOT_EXISTING);
        else
            super.updateQuery(this.update(reserveEquipamentProfessor, r_new));
    }

    public void delete(ReserveEquipamentProfessor reserveEquipamentProfessor) throws ReserveException, SQLException {
        if (reserveEquipamentProfessor == null)
            throw new ReserveException(NULL);
        else if (!this.reserveInDB(reserveEquipamentProfessor))
            throw new ReserveException(RESERVE_NOT_EXISTING);
        else
            super.executeQuery(this.deleteFromProfessor(reserveEquipamentProfessor));
    }

    @SuppressWarnings("unchecked") public Vector<ReserveEquipamentProfessor> findAll() throws SQLException, ClientException, PatrimonyException,
            ReserveException {
        return super.find("SELECT * FROM reserva_sala_professor "
                + "INNER JOIN room ON room.id_sala = reserva_sala_professor.id_sala "
                + "INNER JOIN professor ON professor.id_professor = reserva_sala_professor.id_professor;");
    }

    @SuppressWarnings("unchecked") public Vector<ReserveEquipamentProfessor> findByMonth(int mes) throws SQLException,
            ClientException, PatrimonyException, ReserveException {
        Vector<ReserveEquipamentProfessor> reservas_prof_mes = super.find("SELECT * FROM reserva_equipamento_professor "
                + "INNER JOIN equipamento ON equipamento.id_equipamento = reserva_equipamento_professor.id_equipamento "
                + "INNER JOIN professor ON professor.id_professor = reserva_equipamento_professor.id_professor;");
        Iterator<ReserveEquipamentProfessor> it = reservas_prof_mes.iterator();
        while (it.hasNext()) {
            ReserveEquipamentProfessor obj = it.next();
            if (Integer.parseInt(obj.getDate().split("[./-]")[1]) != mes) {
                reservas_prof_mes.remove(obj);
            }
        }
        return reservas_prof_mes;
    }

    @SuppressWarnings("unchecked") public Vector<ReserveEquipamentProfessor> findByHour(String hora) throws SQLException,
            ClientException, PatrimonyException, ReserveException {
        String hora_a = "", hora_b = "";
        if (hora.length() == 4)
            hora_a = "0" + hora;
        if (hora.charAt(0) == '0')
            hora_b = hora.substring(1);
        return super.find("SELECT * FROM reserva_equipamento_professor "
                + "INNER JOIN equipamento ON equipamento.id_equipamento = reserva_equipamento_professor.id_equipamento "
                + "INNER JOIN professor ON professor.id_professor = reserva_equipamento_professor.id_professor "
                + " WHERE hora = \"" + hora + "\" or hora = \"" + hora_a + "\" or hora = \"" + hora_b + "\";");
    }

    @Override protected Object fetch(ResultSet rs) throws SQLException, ClientException, PatrimonyException, ReserveException {
        Professor p = new Professor(rs.getString("nome"), rs.getString("cpf"), rs.getString("matricula"), rs.getString("telefone"),
                rs.getString("email"));

        Equipament s = new Equipament(rs.getString("codigo"), rs.getString("descricao"));

        ReserveEquipamentProfessor reserveEquipamentProfessor = new ReserveEquipamentProfessor(rs.getString("data"), rs.getString("hora"), s, p);

        return reserveEquipamentProfessor;
    }

    private boolean professorInDB(Professor professor) throws SQLException {
        return super.inDBGeneric("SELECT * FROM professor WHERE " + "professor.nome = \"" + professor.getName() + "\" and "
                + "professor.cpf = \"" + professor.getCpf() + "\" and " + "professor.telefone = \"" + professor.getPhone()
                + "\" and " + "professor.email = \"" + professor.getEmail() + "\" and " + "professor.matricula = \""
                + professor.getRegistration() + "\";");
    }

    private boolean equipamentInDB(Equipament equipamento) throws SQLException {
        return super.inDBGeneric("SELECT * FROM equipamento WHERE " + "equipamento.codigo = \"" + equipamento.getCode()
                + "\" and " + "equipamento.descricao = \"" + equipamento.getDescription() + "\" and " + ";");
    }

    private boolean professorInReserveDB(Professor professor, String data, String hora) throws SQLException {
        return super.inDBGeneric("SELECT * FROM reserva_sala_professor WHERE " + "data = \"" + data + "\" and " + "hora = \""
                + hora + "\" and " + "id_professor = (SELECT id_professor FROM professor WHERE " + "professor.nome = \""
                + professor.getName() + "\" and " + "professor.cpf = \"" + professor.getCpf() + "\" and "
                + "professor.telefone = \"" + professor.getPhone() + "\" and " + "professor.email = \"" + professor.getEmail()
                + "\" and " + "professor.matricula = \"" + professor.getRegistration() + "\");");
    }

    private boolean equipamentInReserveDB(Equipament equipamento, String data, String hora) throws SQLException {
        return super.inDBGeneric("SELECT * FROM reserva_equipamento_professor WHERE " + "data = \"" + data + "\" and "
                + "hora = \"" + hora + "\" and " + "id_equipamento = (SELECT id_equipamento FROM equipamento WHERE "
                + "equipamento.codigo = \"" + equipamento.getCode() + "\" and " + "equipamento.descricao = \""
                + equipamento.getDescription() + "\" and " + ");");
    }

    private boolean reserveInDB(ReserveEquipamentProfessor reserveEquipamentProfessor) throws SQLException {
        return super.inDBGeneric("SELECT * FROM reserva_equipamento_professor WHERE "
                + "id_professor = (SELECT id_professor FROM professor WHERE " + "professor.nome = \""
                + reserveEquipamentProfessor.getProfessor().getName()
                + "\" and "
                + "professor.cpf = \""
                + reserveEquipamentProfessor.getProfessor().getCpf()
                + "\" and "
                + "professor.telefone = \""
                + reserveEquipamentProfessor.getProfessor().getPhone()
                + "\" and "
                + "professor.email = \""
                + reserveEquipamentProfessor.getProfessor().getEmail()
                + "\" and "
                + "professor.matricula = \""
                + reserveEquipamentProfessor.getProfessor().getRegistration()
                + "\") and "
                + "id_equipamento = (SELECT id_equipamento FROM equipamento WHERE "
                + "equipamento.codigo = \""
                + reserveEquipamentProfessor.getEquipament().getCode()
                + "\" and "
                + "equipamento.descricao = \""
                + reserveEquipamentProfessor.getEquipament().getDescription()
                + "\" and " + "hora = \"" + reserveEquipamentProfessor.getHour() + "\" and " + "data = \"" + reserveEquipamentProfessor.getDate() + ";");
    }

}
