/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.cadastros;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import control.EquipamentController;
import exception.PatrimonyException;

/**
 * @author Parley
 * @editor Aulus & Arthur
 */
public class CadastroEquipamento extends CadastroPatrimonio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5990544619563203493L;

	public CadastroEquipamento(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setName("CadastroEquipamento");
        this.capacidadeLbl.setVisible(false);
        this.capacidadeTxtField.setVisible(false);
    }

    @Override protected void cadastroAction() {

        try {
            EquipamentController.getInstance().insert(codigoTxtField.getText(), descricaoTextArea.getText());
            JOptionPane.showMessageDialog(this, "Equipamento Cadastrado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                    null);
            this.setVisible(false);

        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }

    }
}
