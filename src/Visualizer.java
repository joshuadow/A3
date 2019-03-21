import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Visualizer extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("Visualizer.fxml"));

            primaryStage.setTitle("Object Getter");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }

        public static void main(String[] args){
            launch(args);
        }
    }
