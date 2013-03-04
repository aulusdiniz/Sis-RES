package view.alteracoes;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import view.cadastros.CadastroPatrimonio;
import control.EquipamentController;
import exception.PatrimonyException;

/**
 * @author Parley
 * @editor Aulus & Arthur
 */
public class AlterateEquipament extends CadastroPatrimonio {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1965502144129340786L;
	private int index2 = 0;

    public AlterateEquipament(java.awt.Frame parent, boolean modal, int index) {
        super(parent, modal);
        this.setTitle("Alterar");
        this.setName("AlterateEquipament");
        this.cadastroBtn.setText("Alterar");
        this.cadastroBtn.setName("Alterar");
        this.capacidadeLbl.setVisible(false);
        this.capacidadeTxtField.setVisible(false);
        index2 = index;

        try {

            this.codigoTxtField.setText(EquipamentController.getInstance().getEquipamentVector().get(index).getCode());
            this.descricaoTextArea.setText(EquipamentController.getInstance().getEquipamentVector().get(index).getDescription());
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

            EquipamentController.getInstance().alterate(codigoTxtField.getText(), descricaoTextArea.getText(),
                    EquipamentController.getInstance().getEquipamentVector().get(index2));

            JOptionPane.showMessageDialog(this, "Equipamento alterado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE,
                    null);
            this.setVisible(false);

        } catch (PatrimonyException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE, null);
        }
    }
}
