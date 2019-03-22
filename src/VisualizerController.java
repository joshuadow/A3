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
import org.jdom2.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class VisualizerController {

    @FXML
    private Button clientButton;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea objDisplayArea;

    public void initialize() throws IOException {
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
            ArrayList<Document> newDoc = server.start(myPort);
            server.shutdown();
            Deserializer deserializer = new Deserializer();
            ArrayList<Object> remadeObj = deserializer.recreateObject(newDoc);
            showNewObjects(remadeObj);
        } catch (IOException e) {
            System.out.println("Could not start server");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void showNewObjects(ArrayList<Object> remadeObj) throws IllegalAccessException {
        objDisplayArea.setText("Object properties:\n");
        for(Object ohj : remadeObj){
            try {
                objDisplayArea.appendText("Object: " + ohj.getClass().getName() + "\n");
                for (Field field : ohj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    ObjectCreatorController.updateDisplayDeep(ohj, field, objDisplayArea);
                }
            } catch (IndexOutOfBoundsException e) {
                ObjectCreatorController.throwIndexError();
            }
            objDisplayArea.appendText("########################################################");
        }
    }
}
