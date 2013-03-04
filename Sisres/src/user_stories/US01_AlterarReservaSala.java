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
import exception.ClienteException;
import exception.PatrimonyException;
import exception.ReserveException;

public class US01_AlterarReservaSala {
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

        AlunoDAO.getInstance().include(aluno);

        reservaAluno = new ReservaSalaAluno(data, "23:59", room, "abc", "100", aluno);
        ReserveStudentRoomDAO.getInstance().incluir(reservaAluno);

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


}
