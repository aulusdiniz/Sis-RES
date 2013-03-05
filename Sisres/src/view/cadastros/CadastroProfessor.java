/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.cadastros;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import control.ProfessorController;
import exception.ClientException;

/**
 * 
 * @author Arthur
 */
public class CadastroProfessor extends CreateClient {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4223711910136839110L;

	public CadastroProfessor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setName("CadastroProfessor");

    }

    @Override public void cadastroAction() {
        try {
            if (cadastroBtn.getText().equals("Cadastrar")) {
                ProfessorController.getInstance().insert(nomeTxtField.getText(), cpfTxtField.getText(), matriculaTxtField.getText(),
                        telefoneTxtField.getText(), emailTxtField.getText());

                JOptionPane.showMessageDialog(this, "Professor Cadastrado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                        null);

                this.setVisible(false);
            }
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

}
