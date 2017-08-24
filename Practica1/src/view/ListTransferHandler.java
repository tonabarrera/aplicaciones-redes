package view;

import sockets.SocketEnvio;

import javax.swing.*;
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
        System.out.println("Nana");
        DefaultListModel model = new DefaultListModel();
        System.out.println("Hola");
        list.setModel(model);
        System.out.println("Gaga");
        List<File> dropppedFiles = null;
        try {        
            dropppedFiles = (List<File>) support.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            for (File file : dropppedFiles) {
                model.addElement(file.getName());
                socketEnvio.sendFile(file);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}