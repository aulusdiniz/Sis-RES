/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.horariosReservas;

import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.Patrimonio;
import model.ReserveStudentRoom;
import model.ReserveRoomProfessor;
import model.Room;
import view.reservasSalas.AlterarReservaAlunoSalaView;
import view.reservasSalas.AlterarReservaProfSalaView;
import view.reservasSalas.FazerReservaSalaView;
import view.reservasSalas.ReservaSalaView;
import control.ReserveStudentRoomController;
import control.ReserveRoomProfessorController;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

/**
 * 
 * @author Parley
 */
public class HorariosReservaSala extends HorariosReservaPatrimonio {

    ReserveStudentRoomController instanceAluno;
    ReserveRoomProfessorController instanceProf;
    Room room;

    public HorariosReservaSala(java.awt.Frame parent, boolean modal, String data, Room room) {
        super(parent, modal, data, room);
        this.room = room;
        this.setName("HorarioReservaSala");
    }

    protected Vector<String> fillDataVector(Object o, int index) {
        Vector<String> nomesTabela = new Vector<String>();
        if (o instanceof ReserveStudentRoom) {
            ReserveStudentRoom r = (ReserveStudentRoom) o;
            if (this.room != null && (r.getRoom().equals(this.room))) {
                nomesTabela.add(String.valueOf(index));
                nomesTabela.add("Aluno");
                nomesTabela.add(r.getHour());
                nomesTabela.add(r.getStudent().getName());
                nomesTabela.add(r.getStudent().getRegistration());
                nomesTabela.add(r.getFinality());
                nomesTabela.add(r.getRoom().getCode());
                nomesTabela.add(r.getRoom().getDescription());
                nomesTabela.add(r.getReservedChairs());
                nomesTabela.add(r.getRoom().getCapacity());
            }
        } else if (o instanceof ReserveRoomProfessor) {
            ReserveRoomProfessor r = (ReserveRoomProfessor) o;
            if (this.room != null && (r.getRoom().equals(this.room))) {

                nomesTabela.add(String.valueOf(index));
                nomesTabela.add("Professor");
                nomesTabela.add(r.getHour());
                nomesTabela.add(r.getProfessor().getName());
                nomesTabela.add(r.getProfessor().getRegistration());
                nomesTabela.add(r.getFinality());
                nomesTabela.add(r.getRoom().getCode());
                nomesTabela.add(r.getRoom().getDescription());
                nomesTabela.add(r.getRoom().getCapacity());
                nomesTabela.add(r.getRoom().getCapacity());
            }
        }

        return nomesTabela;

    }

    @Override protected DefaultTableModel fillTable(Patrimonio sala) {
        this.room = (Room) sala;
        DefaultTableModel table = new DefaultTableModel();
        instanceAluno = ReserveStudentRoomController.getInstance();
        instanceProf = ReserveRoomProfessorController.getInstance();
        table.addColumn("");
        table.addColumn("Tipo:");
        table.addColumn("Hora:");
        table.addColumn("Nome");
        table.addColumn("Matricula");
        table.addColumn("Finalidade");
        table.addColumn("Codigo da Room");
        table.addColumn("Descricao da Room");
        table.addColumn("Reservadas");
        table.addColumn("Capacidade");

        this.mes = Integer.parseInt(this.data.substring(3, 5));

        try {
            Vector v = instanceProf.buscarPorData(this.data);

            if (v != null)
                for (int i = 0; i < v.size(); i++) {
                    Vector<String> linha = fillDataVector(v.get(i), i);
                    if (!linha.isEmpty())
                        table.addRow(linha);

                }
            v.clear();

            v = instanceAluno.getReserveMonth(this.data);
            if (v != null)
                for (int i = 0; i < v.size(); i++) {
                    Vector<String> linha = fillDataVector(v.get(i), i);
                    if (!linha.isEmpty())
                        table.addRow(linha);

                }

        } catch (SQLException ex) {
            Logger.getLogger(HorariosReservaPatrimonio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PatrimonyException ex) {
            Logger.getLogger(HorariosReservaPatrimonio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientException ex) {
            Logger.getLogger(HorariosReservaPatrimonio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReserveException ex) {
            Logger.getLogger(HorariosReservaPatrimonio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(HorariosReservaPatrimonio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return table;

    }

    @Override protected void cancelarReservaAction(int index) {
        try {
            String tipoCliente = (String) this.reservasTable.getModel().getValueAt(index, 1);
            index = Integer.parseInt((String) this.reservasTable.getModel().getValueAt(index, 0));
            if (tipoCliente.equals("Aluno")) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja mesmo excluir Reserva?\n" + instanceAluno.getReserveMonth(data).get(index).toString(), "Excluir",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    this.instanceAluno.delete(instanceAluno.getReserveMonth(data).get(index));
                    JOptionPane.showMessageDialog(this, "Reserva excluida com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                            null);
                }
            } else if (tipoCliente.equals("Professor")) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja mesmo excluir Reserva?\n" + instanceProf.buscarPorData(data).get(index).toString(), "Excluir",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    this.instanceProf.excluir(instanceProf.buscarPorData(data).get(index));
                    JOptionPane.showMessageDialog(this, "Reserva excluida com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                            null);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ReserveException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override protected void reservarAction() {
        try {
            ReservaSalaView reserva = new FazerReservaSalaView(new JFrame(), true, room, this.data);
            reserva.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ReserveException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override protected void alterarAction(int index) {
        try {
            String tipoCliente = (String) this.reservasTable.getModel().getValueAt(index, 1);
            index = Integer.parseInt((String) this.reservasTable.getModel().getValueAt(index, 0));
            if (tipoCliente.equals("Aluno")) {
                ReservaSalaView reserva = new AlterarReservaAlunoSalaView(new JFrame(), true, index, this.data);
                reserva.setVisible(true);
            } else if (tipoCliente.equals("Professor")) {
                ReservaSalaView reserva = new AlterarReservaProfSalaView(new JFrame(), true, index, this.data);
                reserva.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ReserveException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
}