/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import sockets.Envio;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author tona
 */


public class ListTransferHandler extends TransferHandler {

    private int action;

    private final Envio socketEnvio;


    ListTransferHandler(int action, Envio socketEnvio) {

        this.action = action;

        this.socketEnvio = socketEnvio;

    }


    @Override

    public boolean canImport(TransferHandler.TransferSupport support) {

        // for the demo, we'll only support drops (not clipboard paste)

        if (!support.isDrop()) {

            return false;

        }

        // Para solo poder arrastrar archivos/carpetas

        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

            System.out.println("NO FILE");

            return false;

        }


        boolean actionSupported = (action & support.getSourceDropActions()) == action;

        if (actionSupported) {

            support.setDropAction(action);

            return true;

        }

        return false;

    }


    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        // Si no se puede importar el archivo se termina la accion
        if (!canImport(support)) {
            System.out.println("No se soporta la informacion");
            return false;
        }
        // Obtenemos el componente que utiliza drag and drop
        JList jList = (JList) support.getComponent();
        DefaultListModel model = new DefaultListModel();
        jList.setModel(model);
        List<File> archivos = null;
        try {
            // Obtenemos los elmeentos arrastrados
            archivos = (List<File>) support.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            // Los mandamos a su respectivo metodo para ser enviados
            for (File file : archivos) {
                model.addElement(file.getName());
                model.removeElement(file.getName());
                // Manda las carpetas recursivamente
                if (file.isDirectory()) socketEnvio.enviarCarpetas(file, "");
                else socketEnvio.enviarArchivo(file, ""); // Manda un solo archivo
                model.removeElement(file.getName());
                model.addElement(file.getName() + "(enviado)");

            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return true;

    }

}