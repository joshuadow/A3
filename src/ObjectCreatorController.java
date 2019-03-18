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
        File[] classFile = ObjectCreatorReflective.getClasses();
        for(File f : classFile){
            String s = f.getName().replaceFirst(".class", "");
            if(s.equals("ObjectCreator") || s.equals("Serializer") || s.equals("Visualizer") || s.equals("Deserializer") ||
            s.contains("ObjectCreatorController")){ continue;}
            classBox.getItems().add(s);
        }
        objDisplay.setText("Objects:");
        objDisplay.setEditable(false);
        objDisplayData.setEditable(false);
        //classBox.getItems().add("primitivesOnly");
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
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void recurseFields(String newValue) throws NoSuchFieldException, IllegalAccessException {
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
            ObjectCreatorReflective.setPARENT_OBJ(obj.getClass().toString());
            ObjectCreatorReflective.notPrimitive(field, obj);
        }
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
                ObjectCreatorReflective.throwRangeError(field);
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
            ObjectCreatorReflective.throwExistError();
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
                ObjectCreatorReflective.throwIndexError();
            }
        }
    }
}
