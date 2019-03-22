import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import javax.print.Doc;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public Server(){}

    public ArrayList<Document> start(int port) throws IOException {
        ArrayList<Document> docList = null;
        try{
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            // Connected to client
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            docList = (ArrayList<Document>) objectInputStream.readObject();
            serverSocket.close();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return docList;
    }

    public void shutdown() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
