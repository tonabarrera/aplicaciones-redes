package view;

import sockets.SocketEnvio;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListTransferHandler extends TransferHandler {
    int action;
    private final SocketEnvio socketEnvio;

    ListTransferHandler(int action, SocketEnvio socketEnvio) {
        this.action = action;
        this.socketEnvio = socketEnvio;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        // for the demo, we'll only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            System.out.println("NO FILE");
            return false;
        }


        boolean actionSupported = (action & support.getSourceDropActions()) == action;
        if (actionSupported) {
            support.setDropAction(action);
            return true;
        }
        System.out.println("AQUI");
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
        System.out.println("AQUI2");

        //DefaultListModel model = (DefaultListModel) list.getModel();

        List<File> dropppedFiles = null;
        System.out.println("AQUI");
        try {        
            System.out.println("AQUI");

            dropppedFiles = (List<File>) support.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            for (File file : dropppedFiles) {
                socketEnvio.sendFile(file);
                //model.addElement(file.getName());
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}