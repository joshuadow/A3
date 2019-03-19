import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ObjectCreatorController {

    @FXML
    private ComboBox<String> classBox;

    @FXML
    private ComboBox<String> arrayTypeBox;

    @FXML
    private ComboBox<String> fieldBox;

    @FXML
    private TextField fieldText;

    @FXML
    private TextArea displayInfo;

    @FXML
    private TextArea objDisplay;

    @FXML
    private TextArea objDisplayData;

    @FXML
    private ProgressBar progBar;

    @FXML
    private TextField fieldText2;

    private Class classObj;
    private Object obj;
    private ArrayList<Object> objArr = new ArrayList<Object>();
    private Object parentObj;

    public void initialize(){
        File[] classFile = Helper.getClasses();
        for(File f : classFile){
            String s = f.getName().replaceFirst(".class", "");
            if(s.equals("ObjectCreator") || s.equals("Serializer") || s.equals("Visualizer") || s.equals("Deserializer") ||
            s.contains("ObjectCreatorController") || s.contains("ObjectCreatorReflective") || s.contains("Helper")){ continue;}
            classBox.getItems().add(s);
        }
        objDisplay.setText("Objects:");
        objDisplay.setEditable(false);
        objDisplayData.setEditable(false);
        //classBox.getItems().add("PrimitivesOnly");
        classBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                if(newValue.equals(classBox.getPromptText())) { return;}
                progBar.setProgress(0);
                displayInfo.setText("Fields:\n");
                fieldBox.getItems().clear();
                try {
                    classObj = Class.forName(classBox.getValue().toString());
                    obj = classObj.getConstructor(new Class[] {}).newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    System.out.println("Could not find class");

                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                Field[] fieldarr = classObj.getDeclaredFields();
                for (Field field : fieldarr) {
                    field.setAccessible(true);
                    fieldBox.getItems().add(field.getName());
                }
                updateDisplayArea(classObj, obj);

            }
        });
        fieldBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(fieldBox.getPromptText())){ return;}
                    fieldText.setText("");
                try {
                    classObj = Class.forName(classBox.getValue().toString());
                } catch (ClassNotFoundException e) {
                    System.out.println("Could not find class");
                }
                try {
                    recurseFields(newValue);
                } catch (NoSuchFieldException | IllegalAccessException |
                        NoSuchMethodException | InvocationTargetException |
                        InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void recurseFields(String newValue) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Field field = classObj.getDeclaredField(newValue);
        field.setAccessible(true);
        if(field.getType().isPrimitive()) {
            if (field.get(obj) == null) {
                fieldText.setText("null");
            } else
                fieldText.setText(field.get(obj).toString());
        }
        else if(field.getType().isArray()){

        }
        else if(!field.getType().isPrimitive()){
            ObjectCreatorReflective.notPrimitive(field, obj);
        }
    }

    public static void createPopUp(Field f, Object field){
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter a value for: " + f.getType() + " " + f.getName());
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter a value for: " + f.getName());
        Button submit = new Button();
        submit.setText("Enter Field");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 300, 300);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ADD VALUES");
        stage.show();

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (textArea.getText().equals("")) {
                        return;
                    }
                    //primitiveCheck
                    if (f.getType().isPrimitive()) {
                        ObjectCreatorReflective.parseFields(f, field, textArea.getText());
                    } else if (f.getType().isArray()) {

                    } else if (!f.getType().isPrimitive()) {

                    }
                    stage.close();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void updateDisplayArea(Class classObj, Object obj){
        displayInfo.setText("Fields:\n");
        displayInfo.setEditable(true);
        Field[] fieldarr = classObj.getDeclaredFields();
        for(int i = 0; i < fieldarr.length; i++){
            Field field = fieldarr[i];
            field.setAccessible(true);
            try {
                displayInfo.appendText(field.getType() + " " + field.getName() + " : " + field.get(obj) + "\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        displayInfo.setEditable(false);
    }

    public void saveField(ActionEvent actionEvent) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        String classStr = classBox.getValue();
        String fieldStr = fieldBox.getValue();
        String newStr = fieldText.getText();
        Field field = obj.getClass().getDeclaredField(fieldStr);
        field.setAccessible(true);
        try {
            ObjectCreatorReflective.parseFields(field, obj, newStr);
        }
            catch(IllegalAccessException e){
                System.out.println("Could not access field");
            }
            catch (IllegalArgumentException | UnsupportedOperationException c) {
                throwRangeError(field);
                fieldText.setText(field.get(classObj).toString());
            }
        updateDisplayArea(classObj, obj);
    }

    public void createObj(ActionEvent actionEvent) throws InterruptedException {
        if(classBox.getValue().equals(classBox.getPromptText())){ return;}
        if(!objArr.contains(obj) || !objDisplay.getText().contains(fieldText2.getText())) {
            for(double i = 0; i < 1; i=i+0.1){
                progBar.setProgress(i);
            }
            objArr.add(obj);
            if(fieldText2.getText().equals(""))
                objDisplay.appendText("\n" + obj.getClass().getName());
            else
                objDisplay.appendText("\n" + fieldText2.getText());
            classBox.setValue(classBox.getPromptText());
            fieldBox.setValue(fieldBox.getPromptText());
            fieldText.setText("");
            displayInfo.setText("");
            fieldText2.setText("");
        }
        else{
            throwExistError();
        }

    }

    public void deleteObject(ActionEvent actionEvent) throws ClassNotFoundException {
        String[] differentName = objDisplay.getText().split("\n");
        int index = -1;
        String toRemove = objDisplay.getSelectedText();
        for(int i = 0; i < differentName.length; i++){
            if(differentName[i].contains(toRemove)) {
                index = i;
                break;
            }
        }
        if(toRemove.equals("Objects") || toRemove.equals("Objects:")){ return;}
        objDisplay.setText(objDisplay.getText().replace("\n"+toRemove, ""));
        objArr.remove(index - 1);
        objDisplayData.setText("");
        classBox.setValue(classBox.getPromptText());
    }

    public void displayObjInfo(MouseEvent mouseEvent) throws ClassNotFoundException, IllegalAccessException {
        if(mouseEvent.getClickCount() == 2) {
            double mind = mouseEvent.getY();
            int mindy = (int) (mind / 20);
            if (mindy < 1) {
                return;
            }
            Object ohj = null;
            try {
                ohj = objArr.get(mindy - 1);
                objDisplayData.setText("Object properties:\n");
                objDisplayData.setText("Object: " + ohj.getClass().getName() + "\n");
                for (Field field : ohj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    objDisplayData.appendText(field.getType() + " " + field.getName() + " : " + field.get(ohj) + "\n");
                }
            } catch (IndexOutOfBoundsException e) {
                throwIndexError();
            }
        }
    }

    public static void throwRangeError(Field field) {
        BorderPane borderPane = new BorderPane();
        Text warning = new Text("\n\nValue out of range for type: " + field.getType());
        warning.setFont(Font.font(24));
        warning.setFont(Font.font("Comic Sans"));;
        warning.setTextAlignment(TextAlignment.CENTER);
        borderPane.getChildren().add(warning);
        Scene scene = new Scene(borderPane, 230, 50);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ERROR");
        stage.show();

    }

    public static void throwExistError(){
        BorderPane borderPane = new BorderPane();
        Text warning = new Text("\n\nObject already exists!");
        warning.setFont(Font.font(24));
        warning.setFont(Font.font("Comic Sans"));;
        warning.setTextAlignment(TextAlignment.CENTER);
        borderPane.getChildren().add(warning);
        Scene scene = new Scene(borderPane, 230, 50);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ERROR");
        stage.show();
    }

    public static void throwIndexError() {
        BorderPane borderPane = new BorderPane();
        Text warning = new Text("\n\nYou've been clicking out of bounds");
        warning.setFont(Font.font(24));
        warning.setFont(Font.font("Comic Sans"));
        warning.setTextAlignment(TextAlignment.CENTER);
        borderPane.getChildren().add(warning);
        Scene scene = new Scene(borderPane, 230, 50);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ERROR");
        stage.show();
    }
}
