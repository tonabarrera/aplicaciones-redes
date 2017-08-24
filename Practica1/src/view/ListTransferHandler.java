package view;

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.TransferHandler;
import sockets.SocketEnvio;

public class ListTransferHandler extends TransferHandler{
    int action;
    private SocketEnvio socketEnvio; 

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
            if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                System.out.println("NO FILE");


            boolean actionSupported = (action & support.getSourceDropActions()) == action;
            if (actionSupported) {
                support.setDropAction(action);
                return true;
            }

            return false;
        }

        @SuppressWarnings("unchecked")
    @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
                System.out.println("No se pudo");
                return false;
            }

            // fetch the drop location
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

            int index = dl.getIndex();
            List<File> dropppedFiles = null;
            System.out.println("Nana");
            try {
                dropppedFiles = (List<File>) support.getTransferable().getTransferData(
                        DataFlavor.javaFileListFlavor);
                for (File file : dropppedFiles)
                    socketEnvio.sendFile(file);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }

            // fetch the data and bail if this fails
            String data;
            try {
                data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | java.io.IOException e) {
                return false;
            }

            JList list = (JList) support.getComponent();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.insertElementAt(data, index);

            Rectangle rect = list.getCellBounds(index, index);
            list.scrollRectToVisible(rect);
            list.setSelectedIndex(index);
            list.requestFocusInWindow();
            return true;
        }
}