package view;

import sockets.SocketEnvio;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListTransferHandler extends TransferHandler {
    private final SocketEnvio socketEnvio; // Socket que usamos en la transferencia
    private int action;

    ListTransferHandler(int action, SocketEnvio socketEnvio) {
        this.action = action;
        this.socketEnvio = socketEnvio;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        // Con esto solo se podran arrastar elementos
        if (!support.isDrop()) {
            return false;
        }
        // Para solo poder arrastrar archivos/carpetas
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            System.out.println("NO ES ARCHIVO NI CARPETA");
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
            archivos = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
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