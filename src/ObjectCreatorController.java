import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<File> classesToLoad;

    public static Integer getTopLevelElements(int i, Field f){
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter the length of the " + Helper.ordinal(i) + " dimension of" + f.getType().getName()+ " \n");
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter an integer: ");
        textArea.setMaxHeight(25);
        Button submit = new Button();
        submit.setText("Enter Integer");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 600, 150);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("GETTING LENGTH OF DEPTH: " + i + " for " + f.getType().getName());
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
    public static String getDimensionPopup(int i, int j, Field f) {
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter what you want index: " + i +"," + j + " of " + f.getType().getName()+ " to be\n");
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter field: " );
        textArea.setMaxHeight(25);
        Button submit = new Button();
        submit.setText("Enter Field");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 600, 150);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("GETTING VALUE FOR: " + f.getType().getName());
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        stage.showAndWait();
        return textArea.getText();
    }

    public static void throwArrayLengthError() {
    }

    public static void getPrimitiveValue(Field f, Object obj) {
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter a value for: " + f.getType() + " " + f.getName());
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter a value for: " + f.getName());
        textArea.setMaxHeight(25);
        Button submit = new Button();
        submit.setText("Enter Field");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 600, 150);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ADDING VALUES FOR: " + obj.getClass().getName());

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (textArea.getText().equals("")) {
                        return;
                    }
                    //primitiveCheck
                    if (f.getType().isPrimitive()) {
                        ObjectCreatorReflective.parseFields(f, obj, textArea.getText());
                    }
                    stage.close();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.showAndWait();


    }

    public void initialize(){
        createHelpDialog();
        classesToLoad = Helper.getClasses();
        updateClassBox(classesToLoad);
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
                fieldBox.setValue(fieldBox.getPromptText());
                fieldBox.getItems().clear();
                fieldText.setText("");
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

    private void updateClassBox(ArrayList<File> classesToLoad) {
        classBox.getItems().clear();
        for(File f : classesToLoad){
            String s = f.getName().replaceFirst(".class", "");
            if(s.equals("ObjectCreator") || s.equals("Serializer") || s.equals("Visualizer") || s.equals("Deserializer") ||
                    s.contains("ObjectCreatorController") || s.contains("ObjectCreatorReflective") || s.contains("Helper")){ continue;}
            classBox.getItems().add(s);
        }
    }

    public void createHelpDialog() {
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nWelcome To The Serializer/Deserializer Program!\n" +
                "I am a help dialog designed to guide you through the steps of working this program\n\n" +
                "Firstly, select your class from the first ComboBox. Note this program can take custom classes\n" +
                "\tYou can import classes by clicking File -> Import Class\n\n" +
                "You have the option of naming your object in the text area\n" +
                "Select the field that you want to alter/set\n" +
                "\tIf the field is a primitive just simply set the value in the text area below\n" +
                "\tIf it is not a primitive, the program will prompt you with text boxes to enter values\n\n" +
                "NOTE: IT IS CRUCIAL YOU CLICK SAVE FIELD IF YOU WANT CHANGES TO BE ACTIVATED\n\n" +
                "Once all the fields you want to set are complete, click Create Object\n" +
                "You have the option of inspecting already created object by double clicking on them in the top right most pane\n" +
                "The area directly below this will display the information you are looking for\n" +
                "To delete an already created object, simply double click on the object name and press the delete object button\n" +
                "\n\nYou can recreate this dialog anytime by clicking Help -> Show Help");
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        Button submit = new Button();
        submit.setAlignment(Pos.BASELINE_CENTER);
        submit.setText("START");
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 700, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WELCOME");
        stage.show();
        stage.setAlwaysOnTop(true);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    stage.close();
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
        else if(field.getType().getName().equals("java.lang.String")){
            fieldText.setText(field.get(obj).toString());
        }
        else if(field.getType().isArray()){
            ObjectCreatorReflective.isAnArray(field, obj);
        }
        else if(!field.getType().isPrimitive()){
            ObjectCreatorReflective.notPrimitive(field, obj, 0);
        }
    }

    public static void createPopUp(Field f, Object field, Object obj){
        VBox vb = new VBox();
        Text getInfo = new Text("\n\nPlease enter a value for: " + f.getType() + " " + f.getName());
        getInfo.setFont(Font.font(24));
        getInfo.setFont(Font.font("Comic Sans"));
        getInfo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().add(getInfo);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter a value for: " + f.getName());
        textArea.setMaxHeight(25);
        Button submit = new Button();
        submit.setText("Enter Field");
        vb.getChildren().add(textArea);
        vb.getChildren().add(submit);

        Scene scene = new Scene(vb, 600, 150);
        scene.setFill(Color.RED);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("ADDING VALUES FOR: " + obj.getClass().getName() + " " + field.getClass().getName());


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
                    }
                    stage.close();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.showAndWait();
    }


    public void updateDisplayArea(Class classObj, Object obj){
        displayInfo.setText("Fields:\n");
        displayInfo.setEditable(true);
        Field[] fieldarr = classObj.getDeclaredFields();
        for(int i = 0; i < fieldarr.length; i++){
            Field field = fieldarr[i];
            field.setAccessible(true);
            try {
                if(!field.getType().isArray())
                    displayInfo.appendText(field.getType() + " " + field.getName() + " : " + field.get(obj) + "\n");
                else {
                    String name = field.getType().getName();
                    if(name.equals("[I"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((int[])field.get(obj))+ "\n");
                    else if(name.equals("[D"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((double[])field.get(obj))+ "\n");
                    else if(name.equals("[S"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((short[])field.get(obj))+ "\n");
                    else if(name.equals("[J"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((long[])field.get(obj))+ "\n");
                    else if(name.equals("[B"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((byte[])field.get(obj))+ "\n");
                    else if(name.equals("[C"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((char[])field.get(obj))+ "\n");
                    else if(name.equals("[F"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((float[])field.get(obj))+ "\n");
                    else if(name.equals("[Z"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((boolean[])field.get(obj))+ "\n");
                    else if(name.equals("[[I"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((int[][])field.get(obj))+ "\n");
                    else if(name.equals("[[D"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((double[][])field.get(obj))+ "\n");
                    else if(name.equals("[[S"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((short[][])field.get(obj))+ "\n");
                    else if(name.equals("[[J"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((long[][])field.get(obj))+ "\n");
                    else if(name.equals("[[B"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((byte[][])field.get(obj))+ "\n");
                    else if(name.equals("[[C"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((char[][])field.get(obj))+ "\n");
                    else if(name.equals("[[F"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((float[][])field.get(obj))+ "\n");
                    else if(name.equals("[[Z"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((boolean[][])field.get(obj))+ "\n");
                    else if(name.equals("[Ljava.lang.String;"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.toString((String[])field.get(obj))+ "\n");
                    else if(name.equals("[[Ljava.lang.String;"))
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((String[][])field.get(obj))+ "\n");
                    else
                        displayInfo.appendText(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((Object[])field.get(obj)) + "\n");






                }
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

    public void importClass(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        Path text = null;

        if (file != null) {
            text = file.toPath();
        }
        if(!text.toString().contains(".class")) { importClass(actionEvent);}
        int index = text.toString().lastIndexOf("/");
        String fileName = text.toString().substring(index+1);
        Path temp = Files.copy(text,Paths.get(Helper.getMyDir()+fileName), StandardCopyOption.REPLACE_EXISTING);
        classesToLoad.add(new File(fileName));
        updateClassBox(classesToLoad);
    }
}
