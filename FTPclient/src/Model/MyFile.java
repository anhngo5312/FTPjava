package Model;

import java.io.File;
import java.io.Serializable;
import javax.swing.Icon;

public class MyFile implements Serializable {

    public boolean isFile() {
        return File;
    }

    public void setFile(boolean File) {
        this.File = File;
    }

    public MyFile(Icon Icon, String Name, String Size, long Lastmodified) {
        this.Icon = Icon;
        this.Name = Name;
        this.Size = Size;
        this.Lastmodified = Lastmodified;
    }

    public Icon getIcon() {
        return Icon;
    }

    public void setIcon(Icon Icon) {
        this.Icon = Icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }

    public long getLastmodified() {
        return Lastmodified;
    }

    public void setLastmodified(long Lastmodified) {
        this.Lastmodified = Lastmodified;
    }
    private Icon Icon;
    private String Name;
    private String Size;
    private long Lastmodified;
    private boolean File;
    
  
    

}
