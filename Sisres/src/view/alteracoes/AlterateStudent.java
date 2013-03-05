package view.alteracoes;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import view.cadastros.CreateClient;
import control.StudentController;
import exception.ClientException;

/**
 * 
 * @author Arthur & Áulus
 */
public class AlterateStudent extends CreateClient {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4221721174352890124L;
	int index2 = 0;

    public AlterateStudent(java.awt.Frame parent, boolean modal, int index) {
        super(parent, modal);
        this.setTitle("Alterar");
        this.setName("Alterar Aluno");
        this.cadastroBtn.setText("Alterar");
        this.cadastroBtn.setName("Alterar");
        this.index2 = index;

        try {
            this.nomeTxtField.setText(StudentController.getInstance().getStudentVector().get(index).getName());
            this.emailTxtField.setText(StudentController.getInstance().getStudentVector().get(index).getEmail());
            this.telefoneTxtField.setText(StudentController.getInstance().getStudentVector().get(index).getPhone());
            this.matriculaTxtField.setText(StudentController.getInstance().getStudentVector().get(index).getRegistration());
            this.cpfTxtField.setText(StudentController.getInstance().getStudentVector().get(index).getCpf());

        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override public void cadastroAction() {
        try {
            StudentController.getInstance().alterate(nomeTxtField.getText(), cpfTxtField.getText(), matriculaTxtField.getText(),
                    telefoneTxtField.getText(), emailTxtField.getText(), StudentController.getInstance().getStudentVector().get(index2));

            JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE, null);
            this.setVisible(false);
        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
}
