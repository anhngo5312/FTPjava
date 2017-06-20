package Core;

import Model.MyFile;
import Model.MyTableModel;
import Model.Tree;
import Model.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreePath;

/**
 *
 * @author ngocanh
 */
public class Control {

    private final View View;
    private String link;
    private JFileChooser FileChoose;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket client;

    public Control(View View) throws IOException, ClassNotFoundException, InterruptedException {
        this.View = View;
        this.View.addRegisterListener((ActionListener) new RegisterListener());
        this.View.addLoginListener((ActionListener) new LoginListener());
        this.View.addJtreeClick((MouseListener) new JtreeListener());

    }

    // <editor-fold defaultstate="collapsed" desc="PrintListener"> 
    class PrintListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = View.getTbl().getSelectedRow();
            if (row != -1 && ((MyTableModel) View.getTbl().getModel()).getFile(row).isFile()) {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        View.getProgressBar().setVisible(true);
                        View.getProgressBar().setIndeterminate(true);
                        View.getProgressBar().setStringPainted(true);
                        View.getProgressBar().setString("Loading..");
                        out.writeObject("down");
                        String UrlDown = link + "\\" + View.getTbl().getValueAt(row, 1);
                        out.writeObject(UrlDown);
                        File TempFile = File.createTempFile("temp", ((String) View.getTbl().getValueAt(row, 1)), new File("D:"));
                        //TempFile.createNewFile();
                        TempFile.deleteOnExit();
                        byte[] buffer = (byte[]) in.readObject();
                        FileOutputStream Fos = new FileOutputStream(TempFile);
                        Fos.write(buffer);
                        Fos.close();
                        System.out.println("aaa");
                        View.getDesktop().print(TempFile);
                        return null;
                    }

                    @Override
                    protected void done() {
                        View.getProgressBar().setVisible(false);
                        View.getProgressBar().setIndeterminate(false);
                    }

                };
                worker.execute();
            }
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OpenListener"> 
    class OpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = View.getTbl().getSelectedRow();
            if (row != -1 && ((MyTableModel) View.getTbl().getModel()).getFile(row).isFile()) {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        View.getProgressBar().setVisible(true);
                        View.getProgressBar().setIndeterminate(true);
                        View.getProgressBar().setStringPainted(true);
                        View.getProgressBar().setString("Loading..");
                        out.writeObject("down");
                        String UrlDown = link + "\\" + View.getTbl().getValueAt(row, 1);
                        out.writeObject(UrlDown);
                        File TempFile = File.createTempFile("temp", ((String) View.getTbl().getValueAt(row, 1)), new File("D:"));
                        TempFile.deleteOnExit();
                        byte[] buffer = (byte[]) in.readObject();
                        System.out.println("abc");
                        FileOutputStream Fos = new FileOutputStream(TempFile);
                        Fos.write(buffer);
                        Fos.close();

                        View.getDesktop().open(TempFile);
                        return null;
                    }

                    @Override
                    protected void done() {
                        View.getProgressBar().setVisible(false);
                        View.getProgressBar().setIndeterminate(false);
                    }

                };
                worker.execute();
            }
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RenameListener"> 
    class RenameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                out.writeObject("rename");
                if (View.getTbl().getSelectedRow() != -1) {
                    String Path = link + "\\" + View.getTbl().getValueAt(View.getTbl().getSelectedRow(), 1);
                    out.writeObject(Path);
                }
                String Newname = View.getInput("Rename to");
                out.writeObject(link + "\\" + Newname);
                boolean check = (boolean) in.readObject();
                if (check) {
                    View.addMessage("Rename sucessful");
                } else {
                    View.addMessage("Rename fail");
                }
                out.writeObject("dir");
                out.writeObject(link);
                ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                View.builtTable(ListFile);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DeleteListener"> 
    class DeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                out.writeObject("delete");
                if (View.getTbl().getSelectedRow() != -1) {
                    String file = link + "\\" + View.getTbl().getValueAt(View.getTbl().getSelectedRow(), 1);
                    out.writeObject(file);
                }
                boolean check = (boolean) in.readObject();
                if (check) {
                    View.addMessage("delete sucessful");
                } else {
                    View.addMessage("delete fail");
                }
                Object Receive = in.readObject();
                if (Receive instanceof Tree) {
                    Tree Tree = (Tree) Receive;
                    View.setTree(Tree);
                    View.initTree();
                }
                out.writeObject("dir");
                out.writeObject(link);
                ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                View.builtTable(ListFile);
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NewListener"> 
    class NewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                out.writeObject("mkdir");
                out.writeObject(link);
                String Folder = View.getInput("Input name folder");
                out.writeObject(Folder);
                boolean check = (boolean) in.readObject();
                if (check) {
                    View.addMessage("Create folder sucessful");
                } else {
                    View.addMessage("Create folder fail");
                }
                Object Receive = in.readObject();
                if (Receive instanceof Tree) {
                    Tree Tree = (Tree) Receive;
                    View.setTree(Tree);
                    View.initTree();
                }
                out.writeObject("dir");
                out.writeObject(link);
                ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                View.builtTable(ListFile);
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LogoutListener"> 
    class LogoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            View.setModeLogout();
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LoginListener">  
    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            User User = View.getUser();
            try {
                client = new Socket(User.getIp(), User.getPort());
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                out.writeObject("login");
                out.writeObject(User);
                Object temp = in.readObject();
                if (temp instanceof Boolean) {
                    boolean check = (Boolean) temp;
                    if (check) {
                        View.addMessage("Dang nhap thanh cong");
                        SwingWorker worker = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                View.getProgressBar().setVisible(true);
                                View.getProgressBar().setIndeterminate(true);
                                View.getProgressBar().setStringPainted(true);
                                View.getProgressBar().setString("Loading..");
                                Object Receive = in.readObject();
                                if (Receive instanceof Tree) {
                                    Tree Tree = (Tree) Receive;
                                    View.setTree(Tree);
                                    View.initTree();
                                }
                                link = ((Tree) Receive).getRoot().toString();
                                System.out.println(link);
                                out.writeObject("dir");
                                out.writeObject(link);
                                ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                                View.builtTable(ListFile);
                                View.setModeLogon();
                                View.addJtreeClick((MouseListener) new JtreeListener());
                                View.addUploadClicked((ActionListener) new UploadListener());
                                View.addtblMouseClicked((MouseListener) new TableListener());
                                View.addDownloadListener((ActionListener) new DownloadListener());
                                View.addLogoutListener((ActionListener) new LogoutListener());
                                View.addRenameListener((ActionListener) new RenameListener());
                                View.addDeleteListener((ActionListener) new DeleteListener());
                                View.addNewListener((ActionListener) new NewListener());
                                View.addOpenListener((ActionListener) new OpenListener());
                                View.addPrintListener((ActionListener) new PrintListener());
                                return null;
                            }

                            @Override
                            protected void done() {
                                View.getProgressBar().setVisible(false);
                                View.getProgressBar().setIndeterminate(false);
                            }
                        };
                        worker.execute();
                    } else {
                        View.addMessage("Dang nhap khong thanh cong");
                        client.close();
                    }
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RegisterListener">  
    class RegisterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                client = new Socket(View.getTxtIp().getText(), Integer.parseInt(View.getTxtPort().getText()));
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                User User = View.getRegister();
                if (User != null) {
                    out.writeObject("register");
                    out.writeObject(User);
                    boolean CheckReg = (Boolean) in.readObject();
                    if (CheckReg) {
                        View.addMessage("Register sucessful");
                    } else {
                        View.addMessage("Register fail");
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UploadListener">  
    class UploadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            FileInputStream Fis = null;
            FileChoose = new JFileChooser();
            int Selected = FileChoose.showOpenDialog(View);
            if (Selected == JFileChooser.APPROVE_OPTION) {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        View.getProgressBar().setVisible(true);
                        View.getProgressBar().setIndeterminate(true);
                        View.getProgressBar().setStringPainted(true);
                        View.getProgressBar().setString("Uploading..");
                        File FileTemp = FileChoose.getSelectedFile();
                        FileInputStream Fis = new FileInputStream(FileTemp);
                        byte[] bufer = new byte[Fis.available()];
                        out.writeObject("up");
                        out.writeObject(link);
                        out.writeObject(FileTemp.getName());
                        Fis.read(bufer, 0, Fis.available());
                        out.writeObject(bufer);
                        String ok = (String) in.readObject();
                        out.writeObject("dir");
                        out.writeObject(link);
                        ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                        View.builtTable(ListFile);
                        View.addMessage("Upload success!");
                        return null;
                    }

                    @Override
                    protected void done() {
                        View.getProgressBar().setVisible(false);
                        View.getProgressBar().setIndeterminate(false);
                    }
                };
                worker.execute();
            }
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DownloadListener">  
    class DownloadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = View.getTbl().getSelectedRow();
            if (row != -1 && ((MyTableModel) View.getTbl().getModel()).getFile(row).isFile()) {
                String Filename = (String) View.getTbl().getValueAt(row, 1);
                FileChoose = new JFileChooser();
                FileChoose.setDialogTitle("Select file");
                FileChoose.setSelectedFile(new File(Filename));
                int Selected = FileChoose.showDialog(View, "Download");
                if (Selected == JFileChooser.APPROVE_OPTION) {
                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            View.getProgressBar().setVisible(true);
                            View.getProgressBar().setIndeterminate(true);
                            View.getProgressBar().setStringPainted(true);
                            View.getProgressBar().setString("Downloading..");
                            File FileTemp = FileChoose.getSelectedFile();
                            String Path = FileTemp.getAbsolutePath();
                            out.writeObject("down");
                            String UrlDown = link + "\\" + View.getTbl().getValueAt(row, 1);
                            System.out.println(UrlDown);
                            out.writeObject(UrlDown);
                            File SaveFile = new File(Path);
                            byte[] buffer = (byte[]) in.readObject();
                            if (!SaveFile.exists()) {
                                try (FileOutputStream Fos = new FileOutputStream(SaveFile)) {
                                    Fos.write(buffer);
                                    Fos.close();
                                }
                            }

                            View.addMessage("Download success!");
                            return null;
                        }

                        @Override
                        protected void done() {
                            View.getProgressBar().setVisible(false);
                            View.getProgressBar().setIndeterminate(false);
                        }

                    };
                    worker.execute();

                }

            }
        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TableListener">  
    class TableListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (View.getTbl().getSelectedRow() != -1) {
                MyFile File = ((MyTableModel) View.getTbl().getModel()).getFile(View.getTbl().getSelectedRow());
                View.FileDetail(File, link);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="JtreeListener">  
    class JtreeListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent evt) {
            TreePath TreePath = View.getJTree().getPathForLocation(evt.getX(), evt.getY());
            if (TreePath != null) {
                try {
                    link = "";
                    link = link + TreePath.getPathComponent(0);
                    for (int i = 1; i < TreePath.getPathCount(); i++) {
                        link = link + "\\" + TreePath.getPathComponent(i);
                    }
                    System.out.println(link);
                    out.writeObject("dir");
                    out.writeObject(link);
                    ArrayList<MyFile> ListFile = (ArrayList<MyFile>) in.readObject();
                    View.builtTable(ListFile);

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent evt) {

        }

        @Override
        public void mouseReleased(MouseEvent evt) {

        }

        @Override
        public void mouseEntered(MouseEvent evt) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }//</editor-fold>
}
