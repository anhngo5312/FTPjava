package Core;

import Model.MyFile;
import Model.Tree;
import Model.MyTableModel;
import Model.User;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author ngocanh
 */
public class View extends javax.swing.JFrame implements Runnable {

    public Desktop getDesktop() {
        return Desktop;
    }

    private Tree Tree;

    private Desktop Desktop;

    private File CurentFile;

    public View() throws InterruptedException {
        this.Desktop = Desktop.getDesktop();
        //<editor-fold defaultstate="collapsed" desc="Set windows">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        this.setTitle("FTP Client");
        this.initComponents();
        this.setLocation(350, 150);
        this.setVisible(true);
        this.setModeLogout();
    }

    public void addMessage(String Message) {
        JOptionPane.showMessageDialog(this, Message);
    }

    public User getUser() {
        User User = new User();
        User.setUsername(txtUsername.getText());
        User.setPassword(new String(txtPassword.getPassword()));
        User.setIp(getTxtIp().getText());
        User.setPort(Integer.parseInt(getTxtPort().getText()));
        return User;
    }

    // <editor-fold defaultstate="collapsed" desc="initTree">
    public void initTree() throws InterruptedException {
        DefaultTreeModel TreeModel = new DefaultTreeModel(Tree.getRoot());
        JTree = new JTree(TreeModel);
        DefaultTreeCellRenderer Renderer = new DefaultTreeCellRenderer();
        Renderer.setLeafIcon(Renderer.getOpenIcon());
        JTree.setCellRenderer(Renderer);
        jScrollPane1.setViewportView(JTree);
        JTree.setEnabled(true);
        JTree.expandRow(0);
        JTree.setVisibleRowCount(20);
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Set action">  
    public void addJtreeClick(MouseListener log) {
        JTree.addMouseListener(log);
    }

    public void addtblMouseClicked(MouseListener log) {
        getTbl().addMouseListener(log);
    }

    public void addUploadClicked(ActionListener log) {
        btlupload.addActionListener(log);
    }

    public void addDownloadListener(ActionListener log) {
        btldownload.addActionListener(log);
    }

    public void addLoginListener(ActionListener log) {
        btnLogin.addActionListener(log);
    }

    public void addLogoutListener(ActionListener log) {
        btnLogout.addActionListener(log);
    }

    public void addRegisterListener(ActionListener log) {
        btnRegister.addActionListener(log);
    }

    public void addRenameListener(ActionListener log) {
        btnRename.addActionListener(log);
    }

    public void addDeleteListener(ActionListener log) {
        btnDelete.addActionListener(log);
    }

    public void addNewListener(ActionListener log) {
        btnNew.addActionListener(log);
    }

    public void addOpenListener(ActionListener log) {
        btnOpenfile.addActionListener(log);
    }

    public void addPrintListener(ActionListener log) {
        btnPrintfile.addActionListener(log);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="builtTable">  
    public void builtTable(ArrayList<MyFile> ListFile) {
        MyTableModel TableModel = new MyTableModel(ListFile);
        getTbl().setModel(TableModel);
        getTbl().setAutoCreateRowSorter(true);
        getTbl().setShowVerticalLines(false);
        getTbl().setShowHorizontalLines(false);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        ((DefaultTableCellRenderer) getTbl().getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        getTbl().getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        getTbl().getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        getTbl().setRowHeight(14 + 6);
        setColumnWidth(0, -1);
        setColumnWidth(1, 260);
        setColumnWidth(2, 125);
        setColumnWidth(3, 160);
    }

    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = getTbl().getColumnModel().getColumn(column);
        if (width < 0) {
            JLabel label = new JLabel((String) tableColumn.getHeaderValue());
            Dimension preferred = label.getPreferredSize();
            width = (int) preferred.getWidth() + 14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setModeLogout">
    public void setModeLogout() {
        btnLogout.setEnabled(false);
        btlupload.setEnabled(false);
        btlupload.setVisible(false);
        btldownload.setEnabled(false);
        btldownload.setVisible(false);
        btnLogin.setEnabled(true);
        btnRegister.setEnabled(true);
        progressBar.setVisible(false);
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("NOT LOGIN");
        JTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        JTree.setEnabled(false);
        tbl.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{}
        ));
        tbl.setEnabled(false);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtPassword.setText("");
        getTxtIp().setEnabled(true);
        getTxtPort().setEnabled(true);
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setModeLogon">
    public void setModeLogon() {
        btnLogout.setEnabled(true);
        btlupload.setEnabled(true);
        btlupload.setVisible(true);
        btldownload.setVisible(true);
        btldownload.setEnabled(true);
        tbl.setEnabled(true);
        JTree.setEnabled(true);
        btnRegister.setEnabled(false);
        btnLogin.setEnabled(false);
        txtUsername.setEnabled(false);
        txtPassword.setEnabled(false);
        getTxtIp().setEnabled(false);
        getTxtPort().setEnabled(false);

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="get + set">
    public JTable getTbl() {
        return tbl;
    }

    public Tree getTree() {
        return Tree;
    }

    public void setTree(Tree Tree) {
        this.Tree = Tree;
    }

    public JTree getJTree() {
        return JTree;
    }

    public javax.swing.JTextField getTxtIp() {
        return txtIp;
    }

    public javax.swing.JTextField getTxtPort() {
        return txtPort;
    }

    public javax.swing.JProgressBar getProgressBar() {
        return progressBar;
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getRegister">
    public User getRegister() {
        JTextField Username = new JTextField();
        JPasswordField Password = new JPasswordField();
        JPasswordField Password2 = new JPasswordField();
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Username"),
            Username,
            new JLabel("Password"),
            Password,
            new JLabel("Retype pasword"),
            Password2,};
        int result = JOptionPane.showConfirmDialog(this, inputs, "Form register", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String User = Username.getText();
            String Pass = new String(Password.getPassword());
            String Pass2 = new String(Password2.getPassword());
            if (Pass.equals(Pass2)) {
                User Newuser = new User(User, Pass);
                return Newuser;
            } else {
                addMessage("Mat khau nhap khong trung nhau");
            }
        } else {
            addMessage("Ban chua dang ky xong");
        }
        return null;
    } //</editor-fold>

    public void FileDetail(MyFile File, String Path) {
        Icon Icon = File.getIcon();
        txtFilename.setText(Path + "\\" + File.getName());
        lbFiletype.setIcon(Icon);
        lbFiletype.setText(File.getIcon().toString());
        lbSize.setText(File.getSize());
        lbLastmodified.setText(new Date(File.getLastmodified()).toString());
    }

    public String getInput(String St) {
        String a = JOptionPane.showInputDialog(this, St);
        return a;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTree = new javax.swing.JTree();
        progressBar = new javax.swing.JProgressBar();
        btldownload = new javax.swing.JButton();
        btlupload = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        txtIp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpenfile = new javax.swing.JButton();
        btnPrintfile = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnRename = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnNew = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtFilename = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbFiletype = new javax.swing.JLabel();
        lbSize = new javax.swing.JLabel();
        lbLastmodified = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("NOT LOGIN");
        JTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(JTree);

        btldownload.setText("Download");

        btlupload.setText("Upload");

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Username");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Password");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("IP");

        txtIp.setText("127.0.0.1");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Port");

        txtPort.setText("9000");

        btnLogin.setText("Login");

        btnLogout.setText("Logout");

        btnRegister.setText("Register ");

        jToolBar1.setRollover(true);

        btnOpenfile.setText("Open");
        btnOpenfile.setFocusable(false);
        btnOpenfile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenfile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnOpenfile);

        btnPrintfile.setText("Print");
        btnPrintfile.setFocusable(false);
        btnPrintfile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrintfile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnPrintfile);
        jToolBar1.add(jSeparator1);

        btnRename.setText("Rename");
        btnRename.setFocusable(false);
        btnRename.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRename.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnRename);

        btnDelete.setText("Delete");
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnDelete);
        jToolBar1.add(jSeparator2);

        btnNew.setText("New folder");
        btnNew.setFocusable(false);
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnNew);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("File");

        txtFilename.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("File Type");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Size");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Last Modified");

        lbFiletype.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lbSize.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lbLastmodified.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbFiletype)
                            .addComponent(txtFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbLastmodified)
                            .addComponent(lbSize))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtFilename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbFiletype))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lbSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lbLastmodified))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel6.getAccessibleContext().setAccessibleName("File:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnLogin)
                        .addGap(34, 34, 34)
                        .addComponent(btnLogout)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRegister)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btlupload)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btldownload))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin)
                    .addComponent(btnLogout))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btldownload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btlupload)
                        .addComponent(btnRegister))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree JTree;
    private javax.swing.JButton btldownload;
    private javax.swing.JButton btlupload;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOpenfile;
    private javax.swing.JButton btnPrintfile;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRename;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbFiletype;
    private javax.swing.JLabel lbLastmodified;
    private javax.swing.JLabel lbSize;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTable tbl;
    private javax.swing.JTextField txtFilename;
    private javax.swing.JTextField txtIp;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
    }

}
