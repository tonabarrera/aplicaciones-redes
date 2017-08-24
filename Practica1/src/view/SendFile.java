/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import sockets.SocketEnvio;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tona
 */
public class SendFile extends javax.swing.JFrame {
    private SocketEnvio socketEnvio;
    private String host = "localhost";
    private int port = 9999;

    /**
     * Creates new form EnviarArchivo
     */
    public SendFile() {
        initComponents();
        myInitComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListFiles = new javax.swing.JList<>();
        btnChooser = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaOutput = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTitle.setText("Arrastra y suelta tus archivos");

        jListFiles.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize() { return strings.length; }

            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListFiles);

        btnChooser.setText("Abrir Archivos");
        btnChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooserActionPerformed(evt);
            }
        });

        txtAreaOutput.setColumns(20);
        txtAreaOutput.setRows(5);
        jScrollPane2.setViewportView(txtAreaOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addContainerGap()
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                        layout.createSequentialGroup().addComponent(labelTitle)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                layout.createSequentialGroup().addComponent(
                                                        jScrollPane2,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 271,
                                                        Short.MAX_VALUE).addPreferredGap(
                                                        javax.swing.LayoutStyle
                                                                .ComponentPlacement.RELATED)
                                                        .addComponent(btnChooser).addGap(4, 4, 4)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
                                labelTitle).addPreferredGap(
                                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.TRAILING).addGroup(
                                        layout.createSequentialGroup().addGap(18, 18, 18)
                                                .addComponent(jScrollPane2,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup().addGap(28, 28, 28)
                                                .addComponent(btnChooser)))
                                .addContainerGap(25, Short.MAX_VALUE)));

        labelTitle.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooserActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jf = new JFileChooser();
        jf.setMultiSelectionEnabled(true);
        int r = jf.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File files[] = jf.getSelectedFiles();
            for (File file : files) {
                try {
                    socketEnvio.sendFile(file);
                } catch (IOException ex) {
                    Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnChooserActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and
        feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                    .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SendFile().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooser;
    private javax.swing.JList<String> jListFiles;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextArea txtAreaOutput;
    // End of variables declaration//GEN-END:variables

    private void myInitComponents() {
        socketEnvio = new SocketEnvio(host, port);
        jListFiles.setTransferHandler(new ListTransferHandler(TransferHandler.COPY, socketEnvio));
        jListFiles.setDropMode(DropMode.INSERT);
    }
}
