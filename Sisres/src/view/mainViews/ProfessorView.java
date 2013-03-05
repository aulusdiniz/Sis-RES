/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mainViews;

import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import view.alteracoes.AlterateProfessor;
import view.cadastros.CreateClient;
import view.cadastros.CadastroProfessor;
import control.ProfessorController;
import exception.ClientException;

/**
 * 
 * @author Parley
 */
public class ProfessorView extends ClientView {

    public ProfessorView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setName("ProfessorView");
    }

    public Iterator getIterator() {
        try {
            return ProfessorController.getInstance().getProfessorVector().iterator();

        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
        return null;
    }

    @Override public void cadastrarAction() {

        CreateClient cadastrar = new CadastroProfessor(new javax.swing.JFrame(), true);
        cadastrar.setResizable(false);
        cadastrar.setVisible(true);
        tabelaCliente.setModel(fillTable());

    }

    @Override public void alterarAction(int index) {

        AlterateProfessor alterar = new AlterateProfessor(new javax.swing.JFrame(), true, index);
        alterar.setResizable(false);
        alterar.setVisible(true);
        this.tabelaCliente.setModel(fillTable());
    }

    @Override public void excluirAction() {
        try {
            int index = this.tabelaCliente.getSelectedRow();
            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma linha!", "Erro", JOptionPane.ERROR_MESSAGE, null);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Deseja mesmo excluir Professor: "
                    + ProfessorController.getInstance().getProfessorVector().get(index).getName() + "?", "Excluir",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ProfessorController.getInstance().delete(ProfessorController.getInstance().getProfessorVector().get(index));
                JOptionPane.showMessageDialog(this, "Professor excluido com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                        null);
            }
            this.tabelaCliente.setModel(fillTable());

        } catch (ClientException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
}