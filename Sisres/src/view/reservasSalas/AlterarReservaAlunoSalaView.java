/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reservasSalas;

import java.awt.Color;
import java.awt.Frame;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.ReserveStudentRoom;
import model.ReserveRoomProfessor;
import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

/**
 * 
 * @author Parley
 */
public class AlterarReservaAlunoSalaView extends ReservaSalaView {

    int index;
    ReserveStudentRoom reservaAluno;
    ReserveRoomProfessor reservaProfessor;

    private void resetComponents() {
        this.reservarButton.setText("Alterar");
        this.reservarButton.setName("AlterarButton");
        this.studentRadioButton.setSelected(true);
        this.cpfLabel.setEnabled(false);
        studentRadioButtonAction();
    }

    public AlterarReservaAlunoSalaView(Frame parent, boolean modal, int index, String data) throws SQLException,
            PatrimonyException, PatrimonyException, ClientException, ReserveException {
        super(parent, modal);
        this.setName("AlterarReservaSalaView");
        this.reservaAluno = instanceAluno.getReserveMonth(data).get(index);
        resetComponents();

    }

    @Override protected void reservarAluno() {
        try {
            instanceAluno.alterate(this.finalidadeTextField.getText(), this.qntCadeirasReservadasTextField.getText(), reservaAluno);

            JOptionPane.showMessageDialog(this, "Reserva alterada com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE, null);

            this.setVisible(false);
        } catch (ReserveException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override protected void reservarProfessor() {

    }

    @Override protected void professorRadioButtonAction() {
    }

    @Override protected void studentRadioButtonAction() {
        this.instanceProf = null;
        this.professorRadioButton.setEnabled(false);
        this.cpfTextField.setBackground(new Color(200, 208, 254));
        this.cpfTextField.setEditable(false);
        this.qntCadeirasReservadasTextField.setEditable(true);
        this.qntCadeirasReservadasTextField.setBackground(Color.white);
        this.horaTextField.setBackground(new Color(200, 208, 254));
        this.horaTextField.setEditable(false);
        this.horaTextField.setText(reservaAluno.getHour());
        this.studentTextArea.setText(reservaAluno.getStudent().toString());
        this.salaTextArea.setText(reservaAluno.getRoom().toString());
        this.dataTextField.setText(reservaAluno.getDate());
        this.qntCadeirasTxtField.setText(reservaAluno.getRoom().getCapacity());
        this.qntCadeirasReservadasTextField.setText(reservaAluno.getReservedChairs());
        this.finalidadeTextField.setText(reservaAluno.getFinality());
    }

    @Override protected void verificarAction() {
        try {
            this.qntCadeirasTxtField.setText(String.valueOf(instanceAluno.availableChairs(room, this.dataTextField.getText(),
                    this.horaTextField.getText())));
        } catch (ReserveException ex) {
            
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (ClientException ex) {
            
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (NullPointerException ex) {
            
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
        
    }

}
