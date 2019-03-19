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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ObjectCreatorReflective {

    public static ArrayList<Object> getObjChecked() {
        return OBJ_CHECKED;
    }

    public static void setObjChecked(ArrayList<Object> objChecked) {
        OBJ_CHECKED = objChecked;
    }

    private static ArrayList<Object> OBJ_CHECKED = new ArrayList<>();


    public ObjectCreatorReflective(){}


    public static void notPrimitive(Field field, Object obj, Object parentObj, int counter) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class newCO = Class.forName(field.getType().getName());
        Object newField = newCO.getConstructor(new Class[] {}).newInstance();
        for(Field f : newField.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(!f.getType().isPrimitive()){
                String fieldType = field.getType().getName();
                String fType = f.getType().getName();
                String objType = obj.getClass().getTypeName();
                if(counter != 0 && fType.equals(objType))
                    continue;
                else {
                    counter++;
                    notPrimitive(f, newField, parentObj, counter);
                }
            }
            else {
                ObjectCreatorController.createPopUp(f, newField, obj);
            }
        }
        field.set(obj, newField);
    }

    public static boolean fieldNamesEqual(Object obj, Object parentObj) {
        Field[] objField = obj.getClass().getDeclaredFields();
        Field[] parentObjField = parentObj.getClass().getDeclaredFields();
        boolean result = false;
        if(objField.length == parentObjField.length){
            for(int i = 0; i < objField.length; i++){
                if(objField[i].getName().equals(parentObjField[i].getName()))
                    result = true;
                else
                    result = false;
            }
        }
        return result;
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

}
