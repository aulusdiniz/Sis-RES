/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.alteracoes;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import view.cadastros.CreateClient;
import control.ProfessorController;
import exception.ClientException;

/**
 * @author Parley
 * @editor Arthur & Paulo
 */
public class AlterateProfessor extends CreateClient {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6819554508080720082L;
	int index2 = 0;

    public AlterateProfessor(java.awt.Frame parent, boolean modal, int index) {
        super(parent, modal);
        this.setName("AlterateProfessor");
        this.cadastroBtn.setText("Alterar");
        this.cadastroBtn.setName("Alterar");
        this.index2 = index;

        try {
            this.nomeTxtField.setText(ProfessorController.getInstance().getProfessorVector().get(index).getName());
            this.emailTxtField.setText(ProfessorController.getInstance().getProfessorVector().get(index).getEmail());
            this.telefoneTxtField.setText(ProfessorController.getInstance().getProfessorVector().get(index).getPhone());
            this.matriculaTxtField.setText(ProfessorController.getInstance().getProfessorVector().get(index).getRegistration());
            this.cpfTxtField.setText(ProfessorController.getInstance().getProfessorVector().get(index).getCpf());

        } catch (ClientException clientException) {
            JOptionPane.showMessageDialog(this, clientException.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException clientException) {
            JOptionPane.showMessageDialog(this, clientException.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override public void cadastroAction() {
        try {
            ProfessorController.getInstance().alterate(nomeTxtField.getText(), cpfTxtField.getText(), matriculaTxtField.getText(),
                    telefoneTxtField.getText(), emailTxtField.getText(),
                    ProfessorController.getInstance().getProfessorVector().get(index2));

            JOptionPane.showMessageDialog(this, "Cadastro alterado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE, null);
            this.setVisible(false);
        } catch (ClientException clientException) {
            JOptionPane.showMessageDialog(this, clientException.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException clientException) {
            JOptionPane.showMessageDialog(this, clientException.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
}
