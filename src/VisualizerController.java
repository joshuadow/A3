import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class VisualizerController {

    @FXML
    private Button clientButton;

    @FXML
    private TextArea textArea;
    public void initialize() throws MalformedURLException {
        String externalIP = Helper.getMyIP();
        if(externalIP.equals("")){
            System.out.println("Could not get IP");
        }
        textArea.appendText("Please enter this IP into the Client: " + externalIP);
        clientButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startListening();
            }
        });
    }

    private int getMyPort() {
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter the port you want to listen on: \n");
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter an integer: ");
        textArea.setMaxHeight(25);
        Button submit = new Button();
        submit.setText("Enter Port");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 600, 150);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("GETTING PORT");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(textArea.getText().equals(""))
                    return;
                else
                    stage.close();
            }
        });
        stage.showAndWait();
        return Integer.parseInt(textArea.getText());
    }

    public void startListening(){
        try {
            int myPort = getMyPort();
            Server server = new Server();
            Object o = server.start(myPort);
            Scanner keyboard = new Scanner(System.in);
            while (!keyboard.next().equals("quit"));

            System.out.println();
            System.out.println("shutting down the server...");
            server.shutdown();
            System.out.println("server stopped");
        } catch (IOException e) {
            System.out.println("Could not start server");
        }
    }
}
