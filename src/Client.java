import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    public void connect(){
        try {
            this.socket = new Socket(ip, port);
            System.out.println("Connected");
            input  = new DataInputStream(System.in);
            out    = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Document doc){
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        try {
            xout.output(doc, out);
        } catch (IOException e) {
            System.out.println("Could not send XML");
        }
    }

}
