package user_stories;

import java.awt.Dimension;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Student;
import model.Professor;
import model.ReserveStudentRoom;
import model.ReserveRoomProfessor;
import model.Room;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.StudentDAO;
import persistence.ProfessorDAO;
import persistence.ReserveStudentRoomDAO;
import persistence.ReserveProfessorRoomDAO;
import persistence.RoomDAO;
import view.Main2;
import view.mainViews.StudentView;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

/**
 * US1 Título: Reservar room. Como cliente (aluno/professor), Eu quero reservar
 * uma room Para que eu possa usufruir, sempre que necessário, do espaço
 * disponível na FGA.
 * 
 * Cenário 1: Professor deseja reservar room disponível. Dado que o professor
 * está cadastrado, E a room está cadastrada, E a room está disponível, Quando o
 * usuário solicitar a reserva da room pelo professor, Então o sistema reserva a
 * room, E informar que a reserva foi realizada com sucesso.
 * 
 * Cenário 2: Student deseja reservar room disponível. Dado que um aluno possui
 * cadastro, E a room está cadastrada, E a room está disponível, Quando o
 * usuário solicitar a reserva pelo aluno, Então o sistema reserva a room, E
 * informar que a reserva foi realizada com sucesso.
 * 
 * Cenário 3: Professor deseja reservar room já reservada por aluno. Dado que o
 * professor está cadastrado, E a room está cadastrada, E a room está reservada
 * por um aluno, Quando o usuário solicitar a reserva da room pelo professor,
 * Então o sistema cancela a reserva feita pelo aluno, E o sistema reserva a
 * room pelo professor, E informar que a reserva foi realizada com sucesso.
 * 
 * 
 * Cenário 4: Professor deseja reservar room reservada por professor Dado que o
 * professor está cadastrado, E a room está cadastrada, E a room já está
 * reservada por um professor, Quando o usuário solicitar a reserva da room pelo
 * professor, Então o sistema deverá negar a reserva, E o sistema deve informar
 * que a room está indisponível para o dia e horário escolhido. E o sistema não
 * deve substituir a reserva.
 */

public class US01_ReservarSala {

    private FrameFixture window;
    private Robot robot;
    private Room room;
    private ReserveRoomProfessor reservaProf;
    private ReserveStudentRoom reservaStudent;
    private Student aluno;
    private Professor prof;
    private DialogFixture dialog;
    private int index;
    private int indexReserva;
    private String data;

    // private int index;

    private void dataAtual() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        this.data = formatador.format(date);
    }

    @Before public void setUp() throws PatrimonyException, SQLException, ClientException, ReserveException {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(5);

        window = new FrameFixture(robot, new Main2());
        window.show(new Dimension(900, 500)); // shows the frame to test

        room = new Room("code", "Room para testes de aceitacao", "123");
        RoomDAO.getInstance().include(room);

        prof = new Professor("Professor Teste", "658.535.144-40", "110038096", "9211-2144", "teste include repetido");
        ProfessorDAO.getInstance().include(prof);

        aluno = new Student("Student Teste", "658.535.144-40", "110038096", "9211-2144", "teste include repetido");
        StudentDAO.getInstance().include(aluno);

        dataAtual();

        index = RoomDAO.getInstance().findAll().size() - 1;
        indexReserva = ReserveProfessorRoomDAO.getInstance().findByDate(data).size() - 1;

        window.button("Room").click();
        dialog = window.dialog("RoomView");
    }

    @After public void tearDown() throws SQLException, PatrimonyException, ClientException, ReserveException {
        if (reservaProf != null)
            ReserveProfessorRoomDAO.getInstance().delete(reservaProf);
        if (reservaStudent != null)
            ReserveStudentRoomDAO.getInstance().delete(reservaStudent);
        if (room != null)
            RoomDAO.getInstance().delete(room);
        if (aluno != null)
            StudentDAO.getInstance().delete(aluno);
        if (prof != null)
            ProfessorDAO.getInstance().delete(prof);
        window.cleanUp();
    }

    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test public void testCenario1Professor() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("professorRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("23:59");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("Reserva feita com sucesso");
        fazerReservaSalaView.optionPane().okButton().click();

        indexReserva = ReserveProfessorRoomDAO.getInstance().findByDate(data).size() - 1;
        reservaProf = ReserveProfessorRoomDAO.getInstance().findByDate(data).get(indexReserva);
    }

    @Test public void testCenario1ProfessorCpfInvalido() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("professorRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("65853514440");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.optionPane().requireMessage(
                "Professor nao Cadastrado. Digite o CPF correto ou cadastre o professor desejado");
        fazerReservaSalaView.optionPane().okButton().click();
        reservaProf = null;
    }

    @Test public void testProfessorHoraAnterior() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("professorRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("00:00");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("A hora escolhida ja passou.");
        fazerReservaSalaView.optionPane().okButton().click();
        
    }

    @Test public void testCenario2Student() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("alunoRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("23:59");
        fazerReservaSalaView.button("VerificarCadeirasButton").click();
        fazerReservaSalaView.textBox("Quantidade de Cadeiras Reservadas").enterText("123");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("Reserva feita com sucesso");
        fazerReservaSalaView.optionPane().okButton().click();

        indexReserva = ReserveStudentRoomDAO.getInstance().findByDate(data).size() - 1;
        reservaStudent = ReserveStudentRoomDAO.getInstance().findAll().lastElement();
    }

    @Test public void testCenario2StudentCpfInvalido() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("alunoRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("65853514440");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.optionPane().requireMessage("Student nao Cadastrado. Digite o CPF correto ou cadastre o aluno desejado");
        fazerReservaSalaView.optionPane().okButton().click();
        reservaProf = null;
    }

    @Test public void testCenario2StudentHoraAnterior() throws SQLException, ClientException, PatrimonyException, ReserveException {

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("alunoRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("00:00");
        fazerReservaSalaView.button("VerificarCadeirasButton").click();
        fazerReservaSalaView.textBox("Quantidade de Cadeiras Reservadas").enterText("123");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("A hora escolhida ja passou.");
        fazerReservaSalaView.optionPane().okButton().click();

    }

    @Test public void testCenario2StudentCadeirasIndisponiveis() throws SQLException, ClientException, PatrimonyException, ReserveException {
                
        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("alunoRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("00:00");
        fazerReservaSalaView.button("VerificarCadeirasButton").click();
        fazerReservaSalaView.textBox("Quantidade de Cadeiras Reservadas").enterText("1234");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("A room nao possui este numero de cadeiras para reservar.");
        fazerReservaSalaView.optionPane().okButton().click();

    }

    
    @Test public void testCenario3() throws SQLException, ClientException, PatrimonyException, ReserveException {

        reservaStudent = new ReserveStudentRoom(data, "23:59", room, "abc", room.getCapacity(), aluno);
        ReserveStudentRoomDAO.getInstance().include(reservaStudent);

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("professorRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("23:59");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("Reserva feita com sucesso");
        fazerReservaSalaView.optionPane().okButton().click();

        indexReserva = ReserveProfessorRoomDAO.getInstance().findByDate(data).size() - 1;
        reservaProf = ReserveProfessorRoomDAO.getInstance().findByDate(data).get(indexReserva);
        reservaStudent = null;
    }

    
    @Test public void testCenario3StudentReserva() throws SQLException, ClientException, PatrimonyException, ReserveException {
        Student aluno2 = new Student("Student Teste", "382.808.446-00", "110", "", "abc");
        StudentDAO.getInstance().include(aluno2);

        ReserveStudentRoom reservaStudent2 = new ReserveStudentRoom(data, "23:59", room, "abc", "100", aluno2);
        ReserveStudentRoomDAO.getInstance().include(reservaStudent2);

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("alunoRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("23:59");
        fazerReservaSalaView.button("VerificarCadeirasButton").click();
        fazerReservaSalaView.textBox("Quantidade de Cadeiras Reservadas").enterText("23");
        fazerReservaSalaView.button("Reservar").click();

        fazerReservaSalaView.optionPane().requireMessage("Reserva feita com sucesso");
        fazerReservaSalaView.optionPane().okButton().click();

        indexReserva = ReserveStudentRoomDAO.getInstance().findByDate(data).size() - 1;
        reservaStudent = ReserveStudentRoomDAO.getInstance().findByDate(data).get(indexReserva);
        
        ReserveStudentRoomDAO.getInstance().delete(reservaStudent2);
        StudentDAO.getInstance().delete(aluno2);        
    }

    
    @Test public void testCenario4() throws SQLException, ClientException, ReserveException, PatrimonyException {

        reservaProf = new ReserveRoomProfessor(data, "23:59", room, "abc", prof);
        ReserveProfessorRoomDAO.getInstance().include(reservaProf);

        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Visualizar Horarios").click();

        DialogFixture diaReservaSala = dialog.dialog("DiaReservaSala");
        diaReservaSala.button("VisualizarButton").click();

        DialogFixture horarioReservaSala = dialog.dialog("HorarioReservaSala");
        horarioReservaSala.button("ReservarButton").click();

        DialogFixture fazerReservaSalaView = dialog.dialog("FazerReservaSalaView");
        fazerReservaSalaView.radioButton("professorRadioButton").click();
        fazerReservaSalaView.textBox("CPF").enterText("658.535.144-40");
        fazerReservaSalaView.button("BuscarCpfButton").click();
        fazerReservaSalaView.textBox("Finalidade").enterText("aula");
        fazerReservaSalaView.textBox("Hora").enterText("23:59");
        fazerReservaSalaView.button("Reservar").click();

        indexReserva = ReserveProfessorRoomDAO.getInstance().findByDate(data).size() - 1;
        reservaProf = ReserveProfessorRoomDAO.getInstance().findByDate(data).get(indexReserva);
        reservaStudent = null;

        fazerReservaSalaView.optionPane().requireMessage("A Room esta reservada no mesmo dia e horario.");
        fazerReservaSalaView.optionPane().okButton().click();

    }

}
