import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;

public class ObjectCreatorReflective {

    public ObjectCreatorReflective(){}

    public static File[] getClasses(){
        File currentDir = new File("./src/");
        return currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0) {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');
                    // get extension
                    String str = name.substring(lastIndex);
                    // match path name extension
                    if(str.equals(".class")) {
                        return true;
                    }
                }
                return false;
            }

        });
    }

    public static void notPrimitive(Field field, Object obj){
        for(Field f : field.getType().getDeclaredFields()) {
            f.setAccessible(true);
            if(!f.getType().isPrimitive()){

            }
            else {
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
                                parseFields(f, field.get(obj), textArea.getText());
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
        }
    }

    public static void parseFields(Field field, Object obj, String newStr) throws IllegalAccessException {
        Class t = field.getType();
        if (t.getName().equals("int"))
            field.setInt(obj, Integer.parseInt(newStr));
        else if (t.getName().equals("byte"))
            field.setByte(obj, Byte.parseByte(newStr));
        else if (t.getName().equals("short"))
            field.setShort(obj, Short.parseShort(newStr));
        else if (t.getName().equals("double"))
            field.setDouble(obj, Double.parseDouble(newStr));
        else if (t.getName().equals("float"))
            field.setFloat(obj, Float.parseFloat(newStr));
        else if (t.getName().equals("char")) {
            if (newStr.length() > 1) {
                throw new UnsupportedOperationException();
            }
            field.setChar(obj, newStr.charAt(0));
        } else if (t.getName().equals("boolean")) {
            if (newStr.equals("1")) {
                newStr = "true";
            }
            if (newStr.equals("0")) {
                newStr = "false";
            }
            if (!newStr.equals("1") && !newStr.equals("0") && !newStr.equals("false") && !newStr.equals("true")) {
                throw new IllegalArgumentException();
            }
            field.setBoolean(obj, Boolean.parseBoolean(newStr));
        } else if (t.getName().equals("long"))
            field.setLong(obj, Long.parseLong(newStr));

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
