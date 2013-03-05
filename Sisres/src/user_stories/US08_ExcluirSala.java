package user_stories;

import java.awt.Dimension;
import java.sql.SQLException;

import model.Aluno;
import model.Room;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import persistence.AlunoDAO;
import persistence.RoomDAO;
import view.Main2;
import exception.ClienteException;
import exception.PatrimonyException;

/**
 * US8 T�tulo: Excluir room. Como usu�rio Eu quero excluir uma room Para que a
 * mesma seja indisponibilizada para reserva.
 * 
 * Cen�rio 1: Existe room cadastrada. Dado que a room est� cadastrada; Quando o
 * usu�rio solicita a exclus�o; Ent�o o sistema deve eliminar os registros da
 * room, E informar o sucesso da exclus�o.
 * 
 * Cen�rio 2: N�o existe room cadastrada. Dado que n�o existe o registro da
 * room; Quando o usu�rio solicita exclus�o; Ent�o o sistema n�o exclui nenhum
 * registro de room, E informa que n�o h� o registro.
 */

public class US08_ExcluirSala {

    private FrameFixture window;
    private Robot robot;
    private Room room;
    private DialogFixture dialog;
    private int index;

    @Before public void setUp() throws PatrimonyException, SQLException {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(5);

        window = new FrameFixture(robot, new Main2());
        window.show(new Dimension(900, 500)); // shows the frame to test

        room = new Room("code", "Room para testes de aceitacao", "123");
        RoomDAO.getInstance().incluir(room);

        index = RoomDAO.getInstance().buscarTodos().size() - 1;

        window.button("Room").click();
        dialog = window.dialog("RoomView");

    }

    @After public void tearDown() throws SQLException, PatrimonyException {
        if (room != null)
            RoomDAO.getInstance().excluir(room);
        window.cleanUp();
    }

    public void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testCenario1() throws SQLException, ClienteException{
        dialog.table("tabelaPatrimonio").selectRows(index);
        dialog.button("Excluir").click();
        dialog.optionPane().requireMessage("Deseja mesmo excluir Room: " + room.getDescription() + "?");
        sleep();
        dialog.optionPane().yesButton().click();
        sleep();
        dialog.optionPane().requireMessage("Room excluida com sucesso");
        
        room = null;
    }
    
    @Test
    public void testCenario2() throws SQLException, ClienteException{
        
        dialog.button("Excluir").click();
        dialog.optionPane().requireMessage("Selecione uma linha!");
        sleep();
        dialog.optionPane().okButton().click();
    }
    
   }
