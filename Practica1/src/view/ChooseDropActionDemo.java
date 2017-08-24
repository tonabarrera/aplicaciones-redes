package view;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChooseDropActionDemo extends JFrame {
    private ChooseDropActionDemo() {
        super("ChooseDropActionDemo");

        DefaultListModel<String> copy = new DefaultListModel<>();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel label;
        JScrollPane sp = new JScrollPane();

        JList<String> copyTo = new JList<>(copy);
        copyTo.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        copyTo.setDropMode(DropMode.INSERT);

        p = new JPanel();
        label = new JLabel("Drop to MOVE to here:");
        label.setAlignmentX(0f);
        p.add(label);
        sp = new JScrollPane(copyTo);
        sp.setAlignmentX(0f);
        p.add(sp);
        p.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        add(p, BorderLayout.CENTER);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        getContentPane().setPreferredSize(new Dimension(320, 315));
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        ChooseDropActionDemo test = new ChooseDropActionDemo();
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Display the window.
        test.pack();
        test.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createAndShowGUI();
        });
    }

    class ToTransferHandler extends TransferHandler {
        int action;

        ToTransferHandler(int action) {
            this.action = action;
        }

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
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }


            assert dropppedFiles != null;
            for (File f : dropppedFiles)
                System.out.println(f.getName());

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
}