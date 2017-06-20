
package Model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;


public class MyTableModel extends AbstractTableModel {

        private ArrayList<MyFile> ListFile;
        private String[] columns = {
            "Icon",
            "Name",
            "Size",
            "Date Modified",};

        public MyTableModel() {
            this.ListFile = null;
        }
        
        public MyTableModel(ArrayList<MyFile> ListFile) {
            this.ListFile = ListFile;
        }

        @Override
        public int getRowCount() {
            return ListFile.size();
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MyFile file = ListFile.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return file.getIcon();
                case 1:
                    return file.getName();
                case 2:
                    return file.getSize();
                case 3:
                    SimpleDateFormat DateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    return DateFormat.format(file.getLastmodified());
                default:
                    System.err.println("Logic Error");
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
                case 0: 
                    return ImageIcon.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        public MyFile getFile(int index) {
            return ListFile.get(index);
        }

    }