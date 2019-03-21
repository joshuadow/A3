import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;
    private String ip = "";
    private int port = 0;
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect(Document doc){
        try {
            this.socket = new Socket(ip, port);
            input  = new DataInputStream(System.in);
            out    = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());

            try {
                xout.output(doc, out);
            } catch (IOException e) {
                System.out.println("Could not send XML");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
