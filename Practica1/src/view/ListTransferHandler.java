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
        // Para solo poder arrastrar archivos
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
        List<File> dropppedFiles = null;
        try {
            dropppedFiles = (List<File>) support.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            for (File file : dropppedFiles) {
                model.addElement(file.getName());
                if (file.isDirectory())
                        socketEnvio.enviarCarpetas(file, ""); // Manda las carpetas recursivamente
                    else
                        socketEnvio.enviarArchivo(file, ""); // Manda un solo archivo
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}