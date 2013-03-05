/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.alteracoes;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import view.cadastros.CadastroPatrimonio;
import control.RoomController;
import exception.PatrimonyException;

/**
 * @author Parley
 * @editor Aulus & Arthur
 */
public class AlterarSala extends CadastroPatrimonio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5027264511485201760L;
	private int index2 = 0;

    public AlterarSala(java.awt.Frame parent, boolean modal, int index) {
        super(parent, modal);
        this.setTitle("Alterar");
        this.setName("AlterarSala");
        this.cadastroBtn.setText("Alterar");
        this.cadastroBtn.setName("Alterar");
        index2 = index;

        try {

            this.codigoTxtField.setText(RoomController.getInstance().getSalas_vet().get(index).getCode());
            this.capacidadeTxtField.setText(RoomController.getInstance().getSalas_vet().get(index).getCapacity());
            this.descricaoTextArea.setText(RoomController.getInstance().getSalas_vet().get(index).getDescription());
            this.index2 = index;

        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }

    }

    @Override protected void cadastroAction() {
        try {

            RoomController.getInstance().alterar(codigoTxtField.getText(), descricaoTextArea.getText(), capacidadeTxtField.getText(),
                    RoomController.getInstance().getSalas_vet().get(index2));

            JOptionPane.showMessageDialog(this, "Room Alterada com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE, null);
            this.setVisible(false);

        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }

}
