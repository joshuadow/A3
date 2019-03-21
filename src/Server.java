import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private Scanner in;
    public Server(){}

    public Object start(int port) throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            // Connected to client
             out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
             in = new Scanner(new InputStreamReader(
                    socket.getInputStream()));
            // Respond to messages from the client
            while (true) {
                String s = in.nextLine();

                out.println(s);
                out.flush();
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return new Object();
    }

    public void shutdown() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}
