/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import logica.Enviar;
import logica.Mensaje;
import logica.MulticastUtilidades;
import logica.Recibir;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tona
 */
public class Chat extends javax.swing.JFrame implements MulticastUtilidades {
    private static Element body = null;
    private static HTMLDocument doc;
    private File imagen;
    private Enviar enviar;
    private static String nickname;
    private static DefaultListModel modelo;
    private static String dest;

    /**
     * Creates new form Chat
     */
    public Chat() {
        initComponents();
        modelo = new DefaultListModel();
        listUsuarios.setModel(modelo);
        obtenerBody();
    }

    public Chat(String nickname) {
        this();
        Chat.nickname = nickname;
        crearSocket();
        enviar.enviarAnuncio(Chat.nickname);
        try {
            ponerIconos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ponerIconos() throws IOException {
        int dimension = btnCaquita.getHeight() - 10;
        Image iconoBoton = ImageIO.read(Mensaje.SADNESS);
        iconoBoton = iconoBoton.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
        btnTriste.setIcon(new ImageIcon(iconoBoton));
        btnTriste.setText("");

        iconoBoton = ImageIO.read(Mensaje.ANGER);
        iconoBoton = iconoBoton.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
        btnEnojado.setIcon(new ImageIcon(iconoBoton));
        btnEnojado.setText("");

        iconoBoton = ImageIO.read(Mensaje.HEART);
        iconoBoton = iconoBoton.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
        btnCorazon.setIcon(new ImageIcon(iconoBoton));
        btnCorazon.setText("");

        iconoBoton = ImageIO.read(Mensaje.POOP);
        iconoBoton = iconoBoton.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
        btnCaquita.setIcon(new ImageIcon(iconoBoton));
        btnCaquita.setText("");

        iconoBoton = ImageIO.read(Mensaje.SMILE);
        iconoBoton = iconoBoton.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
        btnFeliz.setIcon(new ImageIcon(iconoBoton));
        btnFeliz.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelMensajes = new javax.swing.JEditorPane();
        btnCargar = new javax.swing.JButton();
        btnCaquita = new javax.swing.JButton();
        btnFeliz = new javax.swing.JButton();
        btnEnojado = new javax.swing.JButton();
        btnCorazon = new javax.swing.JButton();
        btnTriste = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaMensaje = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listUsuarios = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Super Chat Grupal");
        setResizable(false);

        jLabel1.setText("Super chat grupal");

        panelMensajes.setEditable(false);
        panelMensajes.setContentType("text/html"); // NOI18N
        panelMensajes.setEditorKit(new HTMLEditorKit());
        panelMensajes.setText("<!DOCTYPE html>\n<html>\n  <head>\n\n  </head>\n  <body></body>\n</html>\n");
        panelMensajes.setAutoscrolls(false);
        jScrollPane2.setViewportView(panelMensajes);

        btnCargar.setText("Cargar Imagen");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        btnCaquita.setText("<:3");
        btnCaquita.setToolTipText("");
        btnCaquita.setMaximumSize(new java.awt.Dimension(31, 31));
        btnCaquita.setMinimumSize(new java.awt.Dimension(31, 31));
        btnCaquita.setPreferredSize(new java.awt.Dimension(31, 31));
        btnCaquita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaquitaActionPerformed(evt);
            }
        });

        btnFeliz.setText("=)");
        btnFeliz.setMaximumSize(new java.awt.Dimension(31, 31));
        btnFeliz.setMinimumSize(new java.awt.Dimension(31, 31));
        btnFeliz.setPreferredSize(new java.awt.Dimension(31, 31));
        btnFeliz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFelizActionPerformed(evt);
            }
        });

        btnEnojado.setText(">=|");
        btnEnojado.setMaximumSize(new java.awt.Dimension(31, 31));
        btnEnojado.setMinimumSize(new java.awt.Dimension(31, 31));
        btnEnojado.setPreferredSize(new java.awt.Dimension(31, 31));
        btnEnojado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnojadoActionPerformed(evt);
            }
        });

        btnCorazon.setText("<3");
        btnCorazon.setMaximumSize(new java.awt.Dimension(31, 31));
        btnCorazon.setMinimumSize(new java.awt.Dimension(31, 31));
        btnCorazon.setPreferredSize(new java.awt.Dimension(31, 31));
        btnCorazon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorazonActionPerformed(evt);
            }
        });

        btnTriste.setText("='(");
        btnTriste.setMaximumSize(new java.awt.Dimension(31, 31));
        btnTriste.setMinimumSize(new java.awt.Dimension(31, 31));
        btnTriste.setPreferredSize(new java.awt.Dimension(31, 31));
        btnTriste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTristeActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        txtAreaMensaje.setColumns(10);
        txtAreaMensaje.setLineWrap(true);
        txtAreaMensaje.setRows(5);
        txtAreaMensaje.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtAreaMensaje);

        jLabel2.setText("Usuarios conectados");

        jLabel3.setText("Escribe un mensaje");

        btnVolver.setText("Volver al chat grupal");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        listUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listUsuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCargar, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCaquita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFeliz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEnojado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCorazon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTriste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(384, 384, 384)
                        .addComponent(btnCargar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(btnEnviar))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolver)
                    .addComponent(btnTriste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCorazon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnojado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFeliz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCaquita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

//GEN-FIRST:event_btnCargarActionPerformed
    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Selecciona una imagen a enviar");
        if (jfc.showDialog(this, "Seleccionar") == JFileChooser.APPROVE_OPTION) {
            imagen = jfc.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Imagen lista para enviar");
            btnCargar.setText("Imagen en espera");
        }
    }//GEN-LAST:event_btnCargarActionPerformed

    //GEN-FIRST:event_btnVolverActionPerformed
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        listUsuarios.clearSelection();
        dest = null;
    }//GEN-LAST:event_btnVolverActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Emoji">//GEN-BEGIN:emojibuttons
    //GEN-FIRST:event_btnCorazonActionPerformed
    private void btnCorazonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        agregarEmoji(obtenerMensaje() + " <3 ");
    }//GEN-LAST:event_btnCorazonActionPerformed

    //GEN-FIRST:event_btnCaquitaActionPerformed
    private void btnCaquitaActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        agregarEmoji(obtenerMensaje() + " :poop: ");
    }//GEN-LAST:event_btnCaquitaActionPerformed

//GEN-FIRST:event_btnFelizActionPerformed
    private void btnFelizActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        agregarEmoji(obtenerMensaje() + " =) ");
    }//GEN-LAST:event_btnFelizActionPerformed

//GEN-FIRST:event_btnEnojadoActionPerformed
    private void btnEnojadoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        agregarEmoji(obtenerMensaje() + " >=| ");
    }//GEN-LAST:event_btnEnojadoActionPerformed

//GEN-FIRST:event_btnTristeActionPerformed
    private void btnTristeActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        agregarEmoji(obtenerMensaje() + " ='( ");
    }//GEN-LAST:event_btnTristeActionPerformed
    // </editor-fold>//GEN-END:emojiButtons
//GEN-FIRST:event_btnEnviarActionPerformed
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // TODO add your handling code here:
            Mensaje msj = new Mensaje();
            msj.setUsuario(nickname);
            msj.setMensaje(obtenerMensaje());
            if (dest != null)
                msj.setDestinatario(dest);
            if (imagen != null) {
                msj.setTipoMensaje(Mensaje.IMAGEN);
                btnCargar.setText("Cargar Imagen");
                enviar.enviarImagen(imagen, msj);
                imagen = null;
            } else {
                enviar.enviarMensaje(msj);
            }
            txtAreaMensaje.setText("");
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEnviarActionPerformed

    //GEN-FIRST:event_listUsuariosMouseClicked
    private void listUsuariosMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        JList jlist = (JList) evt.getSource();
        if (evt.getClickCount() == 1) {
            int index = jlist.locationToIndex(evt.getPoint());
            System.out.println("Destinatario: "+ modelo.get(index));
            dest = (String) modelo.get(index);
        }
    }//GEN-LAST:event_listUsuariosMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCaquita;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCorazon;
    private javax.swing.JButton btnEnojado;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnFeliz;
    private javax.swing.JButton btnTriste;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listUsuarios;
    private javax.swing.JEditorPane panelMensajes;
    private javax.swing.JTextArea txtAreaMensaje;
    // End of variables declaration//GEN-END:variables

    private String obtenerMensaje() {
        return txtAreaMensaje.getText();
    }

    private void obtenerBody() {
        StyleSheet styleSheet = new StyleSheet();
        HTMLEditorKit kit = (HTMLEditorKit) panelMensajes.getEditorKit();
        styleSheet.addRule("div {max-width:295px; word-wrap:break-word; overflow: hidden; width:295px;}");
        kit.setStyleSheet(styleSheet);
        panelMensajes.setEditorKit(kit);
        doc = (HTMLDocument) kit.createDefaultDocument();
        panelMensajes.setDocument(doc);
        Element[] roots = doc.getRootElements();

        for (int i = 0; i < roots[0].getElementCount(); i++) {
            Element element = roots[0].getElement(i);
            System.out.println(element.getAttributes().getAttribute(StyleConstants.NameAttribute));
            if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                body = element;
                break;
            }
        }
    }

    private void crearSocket() {
        try {
            InetAddress grupo = InetAddress.getByName(DIRECCION);
            MulticastSocket multicastSocket = new MulticastSocket(PUERTO);
            multicastSocket.joinGroup(grupo);
            multicastSocket.setReuseAddress(true);

            Recibir recibir = new Recibir(multicastSocket, nickname);
            enviar = new Enviar(multicastSocket, grupo, PUERTO);

            new Thread(recibir).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void agregarMensaje(Mensaje msj) throws IOException, BadLocationException {
        if (Objects.equals(msj.getDestinatario(), dest) || msj.getDestinatario() == null || Objects.equals(msj.getDestinatario(), nickname) )
            doc.insertBeforeEnd(body, msj.construirMensaje());
    }

    private void agregarEmoji(String msj) {
        txtAreaMensaje.setText(msj);
    }

    public static void agregarUsuarioLista(String usuario) {
        modelo.addElement(usuario);
    }

    public static void cargarListaConectados(ArrayList<String> conectados) {
        modelo.clear();
        for (String usuario : conectados)
            agregarUsuarioLista(usuario);
    }
}
