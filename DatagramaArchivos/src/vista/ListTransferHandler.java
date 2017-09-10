/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.TransferHandler;
import sockets.Envio;

/**
 *
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

        // if we can't handle the import, say so

        if (!canImport(support)) {

            System.out.println("No se pudo");

            return false;

        }



        JList list = (JList) support.getComponent();

        DefaultListModel model = new DefaultListModel();

        list.setModel(model);

        //List<File> dropppedFiles = null;

        //try {

            //dropppedFiles = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

            //for (File file : dropppedFiles) {

                //model.addElement(file.getName());

                // Manda las carpetas recursivamente

                //if (file.isDirectory()) socketEnvio.enviarCarpetas(file, "");

                //else socketEnvio.enviarArchivo(file, ""); // Manda un solo archivo

            //}

        //} catch (UnsupportedFlavorException | IOException e) {
        //    e.printStackTrace();
        //}
        return true;

    }

}