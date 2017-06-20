package Core;

import Model.MyFile;
import Model.Tree;
import Model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.Icon;

public class ServiceThread extends Thread {

    private final int ClientNumber;
    private final Socket Client;
    private User User;
    private Connection con;
    private final Serverview View;
    private final String FRoot = "D:\\FTP\\";

    public ServiceThread(Socket Client, int ClientNumber, Serverview View) {
        this.ClientNumber = ClientNumber;
        this.Client = Client;
        this.View = View;
    }

    @Override
    public void run() {
        System.out.println("Connected!");
        View.addLog("Connected!\n");
        System.out.println("Phuc vu Thread thu " + ClientNumber);
        View.addLog("Phuc vu client thu " + ClientNumber + "\n");
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(Client.getInputStream());
            out = new ObjectOutputStream(Client.getOutputStream());
            while (!(Client.isClosed())) {
                Object temp;
                String Command;
                byte[] buffer;
                boolean isconnect;
                Command = (String) in.readObject();
                switch (Command) {
                    case "dir":
                        String link = FRoot + (String) in.readObject();
                        File File = new File(link);
                        File[] Files = File.listFiles();
                        ArrayList<MyFile> ListFile = new ArrayList();
                        for (File file : Files) {
                            FileSystemView fileSystemView = FileSystemView.getFileSystemView();
                            Icon Icon = fileSystemView.getSystemIcon(file);
                            String Name = file.getName();
                            String Size = "";
                            if (file.isFile()) {
                                Size = Long.toString(file.length() / 1024) + " KB";
                            }
                            long Lastmodified = file.lastModified();
                            MyFile Myfile = new MyFile(Icon, Name, Size, Lastmodified);
                            Myfile.setFile(file.isFile());
                            ListFile.add(Myfile);
                        }
                        out.writeObject(ListFile);
                        System.out.println(User.getUsername() + ": truy cap " + link);
                        View.addLog(User.getUsername() + ": truy cap " + link + "\n");
                        break;
                    case "up":
                        String UrlUp = (String) in.readObject();
                        String FileName = (String) in.readObject();
                        buffer = (byte[]) in.readObject();
                        File UpFile = new File(FRoot + UrlUp + "\\" + FileName);
                        if (!UpFile.exists()) {
                            FileOutputStream Fos = new FileOutputStream(UpFile);
                            Fos.write(buffer);
                            out.writeObject("upload_done");
                            System.out.println(User.getUsername() + ": upload file thanh cong");
                            View.addLog(User.getUsername() + ": upload file thanh cong\n");
                        }
                        break;
                    case "down":
                        String UrlDown = FRoot + (String) in.readObject();
                        File FileDown = new File(UrlDown);
                        FileInputStream Fis = new FileInputStream(FileDown);
                        buffer = new byte[Fis.available()];
                        Fis.read(buffer, 0, Fis.available());
                        out.writeObject(buffer);
                        Fis.close();
                        System.out.println(User.getUsername() + ": tai file thanh cong");
                        View.addLog(User.getUsername() + ": tai file thanh cong\n");
                        break;
                    case "delete":
                        String Path = (String) in.readObject();
                        File delFile = new File(FRoot + Path);
                        out.writeObject(delFile.delete());
                        File FileRoot2 = new File(FRoot + User.getUsername());
                        DefaultMutableTreeNode Root2 = new DefaultMutableTreeNode(User.getUsername());
                        Tree Tree2 = new Tree(FileRoot2, Root2);
                        out.writeObject(Tree2);
                        break;
                    case "rename":
                        String OldPath = (String) in.readObject();
                        String NewPath = (String) in.readObject();
                        System.out.println(OldPath);
                        System.out.println(NewPath);
                        File OldFile = new File(FRoot + OldPath);
                        File NewFile = new File(FRoot + NewPath);
                        out.writeObject(OldFile.renameTo(NewFile));
                        break;
                    case "mkdir":
                        String Link = (String) in.readObject();
                        String Folder = (String) in.readObject();
                        File file = new File(FRoot + Link + "\\" + Folder);
                        out.writeObject(file.mkdir());
                        File FileRoot1 = new File(FRoot + User.getUsername());
                        DefaultMutableTreeNode Root1 = new DefaultMutableTreeNode(User.getUsername());
                        Tree Tree1 = new Tree(FileRoot1, Root1);
                        out.writeObject(Tree1);
                        break;
                    case "login":
                        isconnect = getDBConnection("logindb", "root", "");
                        if (isconnect) {
                            temp = in.readObject();
                            if (temp instanceof User) {
                                User = (User) temp;
                                boolean check_login = checkUser(User.getUsername(), User.getPassword());
                                out.writeObject(check_login);
                                if (check_login) {
                                    System.out.println(User.getUsername() + ": da dang nhap vao he thong");
                                    View.addLog(User.getUsername() + ": da dang nhap vao he thong\n");
                                    File FileRoot = new File(FRoot + User.getUsername());
                                    DefaultMutableTreeNode Root = new DefaultMutableTreeNode(User.getUsername());
                                    Tree Tree = new Tree(FileRoot, Root);
                                    out.writeObject(Tree);
                                }
                            }
                        }
                        break;
                    case "register":
                        isconnect = getDBConnection("logindb", "root", "");
                        if (isconnect) {
                            temp = in.readObject();
                            if (temp instanceof User) {
                                User user = (User) temp;
                                System.out.println(user.getUsername() + " " + user.getPassword());
                                boolean check_res = registerUser(user.getUsername(), user.getPassword());
                                out.writeObject(check_res);
                                if (check_res) {
                                    File makeFile = new File(FRoot + user.getUsername());
                                    System.out.println(makeFile.getName());
                                    System.out.println(makeFile.getAbsolutePath());
                                    makeFile.mkdir();
                                    System.out.println("dang ky tai khoan " + user.getUsername() + " thanh cong");
                                    View.addLog("dang ky tai khoan " + user.getUsername() + " thanh cong\n");
                                }
                            }

                        }

                        break;
                }
            }
            if (User == null) {
                System.out.println(User.getUsername() + "da thoat");
                View.addLog(User.getUsername() + "da thoat\n");
            }
        } catch (IOException | ClassNotFoundException e) {
        } catch (Exception ex) {
            Logger.getLogger(Servercontrol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Dir() throws IOException, ClassNotFoundException {

    }

    // <editor-fold defaultstate="collapsed" desc="SQL">  
    private boolean checkUser(String user, String pass) throws Exception {
        String query = "Select * FROM users WHERE username ='"
                + user
                + "' AND password ='" + pass + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;

    }

    private boolean registerUser(String Username, String Password) {
        String sql = "INSERT INTO users (username, password) VALUES ('" + Username + "', '" + Password + "')";
        try {
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate(sql);
            if (result == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Servercontrol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean getDBConnection(String myDBName, String admin, String string) {
        String dbURL = "jdbc:mysql://localhost/" + myDBName;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, admin, string);
        } catch (SQLException ex) {
            Logger.getLogger(Servercontrol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servercontrol.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (con != null) {
            return true;
        } else {
            return false;
        }
    }// </editor-fold>
}
