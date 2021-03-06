/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mainViews;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.Client;

/**
 * 
 * @author Parley
 */
public abstract class ClientView extends javax.swing.JDialog {

    /**
     * Creates new form ClientView
     */
    public ClientView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public abstract Iterator getIterator();

    public abstract void cadastrarAction();

    public abstract void alterarAction(int index);

    public abstract void excluirAction();

    protected Vector<String> fillDataVector(Client client) {

        Vector<String> nomesTabela = new Vector<String>();

        if (client == null) {
            return null;
        }

        nomesTabela.add(client.getRegistration());
        nomesTabela.add(client.getName());
        nomesTabela.add(client.getPhone());
        nomesTabela.add(client.getCpf());
        nomesTabela.add(client.getEmail());

        return nomesTabela;

    }

    protected DefaultTableModel fillTable() {
        DefaultTableModel table = new DefaultTableModel();

        Iterator<Client> i = getIterator();

        table.addColumn("Matricula");
        table.addColumn("Nome");
        table.addColumn("Telefone");
        table.addColumn("CPF");
        table.addColumn("E-mail");

        while (i.hasNext()) {
            // int col, row = 0;
            Client client = i.next();
            table.addRow(fillDataVector(client));
        }

        return table;
    }

    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBotoes = new javax.swing.JPanel();
        cadastrarBtn = new javax.swing.JButton();
        alterarBtn = new javax.swing.JButton();
        excluirBtn = new javax.swing.JButton();
        panelLista = new javax.swing.JPanel();
        pesquisarLbl = new javax.swing.JLabel();
        pesquisarTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaCliente = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cliente");

        panelBotoes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cadastrarBtn.setText("Cadastrar");
        cadastrarBtn.setName("Cadastrar");
        cadastrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarBtnActionPerformed(evt);
            }
        });

        alterarBtn.setText("Alterar");
        alterarBtn.setName("Alterar");
        alterarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarBtnActionPerformed(evt);
            }
        });

        excluirBtn.setText("Excluir");
        excluirBtn.setName("Excluir");
        excluirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotoesLayout = new javax.swing.GroupLayout(panelBotoes);
        panelBotoes.setLayout(panelBotoesLayout);
        panelBotoesLayout.setHorizontalGroup(panelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        panelBotoesLayout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        panelBotoesLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(excluirBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(alterarBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cadastrarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 135,
                                                        Short.MAX_VALUE)).addContainerGap()));
        panelBotoesLayout.setVerticalGroup(panelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        panelBotoesLayout
                                .createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(cadastrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(alterarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 82,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(excluirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(78, Short.MAX_VALUE)));

        pesquisarLbl.setText("Digite a matricula desejada: ");

        pesquisarTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisarTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelListaLayout = new javax.swing.GroupLayout(panelLista);
        panelLista.setLayout(panelListaLayout);
        panelListaLayout.setHorizontalGroup(panelListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        panelListaLayout.createSequentialGroup().addContainerGap().addComponent(pesquisarLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pesquisarTextField)));
        panelListaLayout.setVerticalGroup(panelListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                panelListaLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                panelListaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(pesquisarLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pesquisarTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        tabelaCliente.setModel(fillTable());
        tabelaCliente.setName("tabelaCliente");
        jScrollPane1.setViewportView(tabelaCliente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelLista, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                layout.createSequentialGroup()
                                                        .addComponent(panelLista, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(panelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pesquisarTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pesquisarTextFieldActionPerformed
        String nome = this.pesquisarTextField.getText();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum texto digitado", "Erro", JOptionPane.ERROR_MESSAGE, null);
        } else {
            JOptionPane.showMessageDialog(this, "Funciona", "Teste", JOptionPane.WARNING_MESSAGE, null);
        }
    }// GEN-LAST:event_pesquisarTextFieldActionPerformed

    private void cadastrarBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cadastrarBtnActionPerformed
        // TODO add your handling code here:
        cadastrarAction();

    }// GEN-LAST:event_cadastrarBtnActionPerformed

    private void alterarBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_alterarBtnActionPerformed
        // TODO add your handling code here:

        int index = this.tabelaCliente.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha!", "Erro", JOptionPane.ERROR_MESSAGE, null);
            return;
        }

        alterarAction(index);

    }// GEN-LAST:event_alterarBtnActionPerformed

    private void excluirBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_excluirBtnActionPerformed
        excluirAction();

    }// GEN-LAST:event_excluirBtnActionPerformed
     // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JButton alterarBtn;
    private javax.swing.JButton cadastrarBtn;
    private javax.swing.JButton excluirBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBotoes;
    private javax.swing.JPanel panelLista;
    private javax.swing.JLabel pesquisarLbl;
    private javax.swing.JTextField pesquisarTextField;
    protected javax.swing.JTable tabelaCliente;
    // End of variables declaration//GEN-END:variables
}
