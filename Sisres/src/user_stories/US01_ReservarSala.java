package user_stories;

import java.awt.Dimension;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Aluno;
import model.Professor;
import model.ReservaSalaAluno;
import model.ReserveRoomProfessor;
import model.Room;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.AlunoDAO;
import persistence.ProfessorDAO;
import persistence.ReserveStudentRoomDAO;
import persistence.ResSalaProfessorDAO;
import persistence.SalaDAO;
import view.Main2;
import view.mainViews.AlunoView;
import exception.ClienteException;
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
 * Cenário 2: Aluno deseja reservar room disponível. Dado que um aluno possui
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
    private ReservaSalaAluno reservaAluno;
    private Aluno aluno;
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

    @Before public void setUp() throws PatrimonyException, SQLException, ClienteException, ReserveException {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(5);

        window = new FrameFixture(robot, new Main2());
        window.show(new Dimension(900, 500)); // shows the frame to test

        room = new Room("code", "Room para testes de aceitacao", "123");
        SalaDAO.getInstance().incluir(room);

        prof = new Professor("Professor Teste", "658.535.144-40", "110038096", "9211-2144", "teste incluir repetido");
        ProfessorDAO.getInstance().include(prof);

        aluno = new Aluno("Aluno Teste", "658.535.144-40", "110038096", "9211-2144", "teste incluir repetido");
        AlunoDAO.getInstance().include(aluno);

        dataAtual();

        index = SalaDAO.getInstance().buscarTodos().size() - 1;
        indexReserva = ResSalaProfessorDAO.getInstance().buscarPorData(data).size() - 1;

        window.button("Room").click();
        dialog = window.dialog("SalaView");
    }

    @After public void tearDown() throws SQLException, PatrimonyException, ClienteException, ReserveException {
        if (reservaProf != null)
            ResSalaProfessorDAO.getInstance().excluir(reservaProf);
        if (reservaAluno != null)
            ReserveStudentRoomDAO.getInstance().delete(reservaAluno);
        if (room != null)
            SalaDAO.getInstance().excluir(room);
        if (aluno != null)
            AlunoDAO.getInstance().delete(aluno);
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

    @Test public void testCenario1Professor() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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

        indexReserva = ResSalaProfessorDAO.getInstance().buscarPorData(data).size() - 1;
        reservaProf = ResSalaProfessorDAO.getInstance().buscarPorData(data).get(indexReserva);
    }

    @Test public void testCenario1ProfessorCpfInvalido() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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

    @Test public void testProfessorHoraAnterior() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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

    @Test public void testCenario2Aluno() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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
        reservaAluno = ReserveStudentRoomDAO.getInstance().buscarTodos().lastElement();
    }

    @Test public void testCenario2AlunoCpfInvalido() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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
        fazerReservaSalaView.optionPane().requireMessage("Aluno nao Cadastrado. Digite o CPF correto ou cadastre o aluno desejado");
        fazerReservaSalaView.optionPane().okButton().click();
        reservaProf = null;
    }

    @Test public void testCenario2AlunoHoraAnterior() throws SQLException, ClienteException, PatrimonyException, ReserveException {

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

    @Test public void testCenario2AlunoCadeirasIndisponiveis() throws SQLException, ClienteException, PatrimonyException, ReserveException {
                
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

    
    @Test public void testCenario3() throws SQLException, ClienteException, PatrimonyException, ReserveException {

        reservaAluno = new ReservaSalaAluno(data, "23:59", room, "abc", room.getCapacity(), aluno);
        ReserveStudentRoomDAO.getInstance().incluir(reservaAluno);

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

        indexReserva = ResSalaProfessorDAO.getInstance().buscarPorData(data).size() - 1;
        reservaProf = ResSalaProfessorDAO.getInstance().buscarPorData(data).get(indexReserva);
        reservaAluno = null;
    }

    
    @Test public void testCenario3AlunoReserva() throws SQLException, ClienteException, PatrimonyException, ReserveException {
        Aluno aluno2 = new Aluno("Aluno Teste", "382.808.446-00", "110", "", "abc");
        AlunoDAO.getInstance().include(aluno2);

        ReservaSalaAluno reservaAluno2 = new ReservaSalaAluno(data, "23:59", room, "abc", "100", aluno2);
        ReserveStudentRoomDAO.getInstance().incluir(reservaAluno2);

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
        reservaAluno = ReserveStudentRoomDAO.getInstance().findByDate(data).get(indexReserva);
        
        ReserveStudentRoomDAO.getInstance().delete(reservaAluno2);
        AlunoDAO.getInstance().delete(aluno2);        
    }

    
    @Test public void testCenario4() throws SQLException, ClienteException, ReserveException, PatrimonyException {

        reservaProf = new ReserveRoomProfessor(data, "23:59", room, "abc", prof);
        ResSalaProfessorDAO.getInstance().incluir(reservaProf);

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

        indexReserva = ResSalaProfessorDAO.getInstance().buscarPorData(data).size() - 1;
        reservaProf = ResSalaProfessorDAO.getInstance().buscarPorData(data).get(indexReserva);
        reservaAluno = null;

        fazerReservaSalaView.optionPane().requireMessage("A Room esta reservada no mesmo dia e horario.");
        fazerReservaSalaView.optionPane().okButton().click();

    }

}
