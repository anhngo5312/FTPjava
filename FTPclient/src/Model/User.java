
package Model;

import java.io.Serializable;


public class User implements Serializable {

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String Ip) {
        this.Ip = Ip;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }
    private String Username;
    private String Password;
    private String Ip;
    private int Port;

    public User(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;
    }

    public User() {
    }
   
}
