
package Core;

import java.io.IOException;


public class Run {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        View View = new View();
        Control client = new Control(View);
    }
}
