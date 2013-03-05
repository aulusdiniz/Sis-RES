/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.diasReservas;

import java.awt.Frame;
import java.sql.SQLException;

import javax.swing.JFrame;

import model.Room;
import view.horariosReservas.HorariosReservaSala;
import control.RoomController;
import exception.PatrimonyException;

/**
 * 
 * @author Parley
 */
public class DiaReservaSala extends DiaReservaPatrimonio {

    Room room;

    public DiaReservaSala(Frame parent, boolean modal, int indexSala) throws SQLException, PatrimonyException {
        super(parent, modal);
        room = RoomController.getInstance().getSalas_vet().get(indexSala);
        this.setName("DiaReservaSala");
    }

    @Override protected void visualizarAction(String data) {
        HorariosReservaSala reserva = new HorariosReservaSala(new JFrame(), true, data, room);
        reserva.setVisible(true);
        reserva.setResizable(false);
    }

}
