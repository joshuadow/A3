import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

public class Visualizer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent thing = FXMLLoader.load(getClass().getResource("Visualizer.fxml"));

        primaryStage.setTitle("Object Getter");
        primaryStage.setScene(new Scene(thing, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args){
            launch(args);
        }
    }
