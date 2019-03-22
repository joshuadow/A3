import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private Socket socket = null;
    private BufferedReader input = null;
    private PrintWriter out = null;
    private String ip = "";
    private int port = 0;
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect(ArrayList<Document> docList){
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            try {
                objectOutputStream.writeObject(docList);
            } catch (IOException e) {
                System.out.println("Could not send XML");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop() throws IOException {
        input.close();
        out.close();
        socket.close();
    }

}
