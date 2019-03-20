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
import java.lang.reflect.Array;
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
            if(!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")){
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
        else if (t.getName().equals("java.lang.String"))
            field.set(obj, newStr);

    }

    public static void isAnArray(Field field, Object obj) throws ClassNotFoundException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {

        field.setAccessible(true);
        String arrClassName = field.getType().getName();
        int dimensions = 0;
        for(int i = 0; i < arrClassName.length(); i++){
            if(arrClassName.charAt(i) == '[')
                dimensions++;
        }
        arrClassName = arrClassName.substring(1);
        int length = ObjectCreatorController.getTopLevelElements();
        Object object = null;
        if(arrClassName.length() == 1){
            if(arrClassName.equals("I"))
                object = Array.newInstance(int.class, length);
            else if(arrClassName.equals("B"))
                object = Array.newInstance(byte.class, length);
            else if(arrClassName.equals("C"))
                object = Array.newInstance(char.class, length);
            else if(arrClassName.equals("J"))
                object = Array.newInstance(long.class, length);
            else if(arrClassName.equals("S"))
                object = Array.newInstance(short.class, length);
            else if(arrClassName.equals("Z"))
                object = Array.newInstance(boolean.class, length);
            else if(arrClassName.equals("D"))
                object = Array.newInstance(double.class, length);
            else if(arrClassName.equals("F"))
                object = Array.newInstance(float.class, length);
            else
                object = Array.newInstance(field.getType(), length);

            for(int i = 0; i < length; i++){
                String j = ObjectCreatorController.getDimensionPopup(i);
                if(arrClassName.equals("I"))
                    Array.set(object, i, Integer.parseInt(j));
                else if(arrClassName.equals("B"))
                    Array.set(object, i, Byte.parseByte(j));
                else if(arrClassName.equals("C"))
                    Array.set(object, i, j.charAt(0));
                else if(arrClassName.equals("J"))
                    Array.set(object, i, Long.parseLong(j));
                else if(arrClassName.equals("S"))
                    Array.set(object, i, Short.parseShort(j));
                else if(arrClassName.equals("Z"))
                    Array.set(object, i, Boolean.parseBoolean(j));
                else if(arrClassName.equals("D"))
                    Array.set(object, i, Double.parseDouble(j));
                else if(arrClassName.equals("F"))
                    Array.set(object, i, Float.parseFloat(j));
                else if(arrClassName.equals("java.lang.String"))
                    Array.set(object, i, j);
                else
                    Array.set(object, i, (Object) j);
            }
        }
        /*else{
            Class arrcls = Class.forName(arrClassName);
            object = Array.newInstance(arrcls, length);
            int[] dimArr = new int[dimensions];
            //4 dimensions
            //arrCls -> int[][][].class -> [[[[I
            //Array.newInstance([[[[I, new int[] {1,2,3,4});
            for(int i = 0; i < dimensions; i++) {
                dimArr[i] = ObjectCreatorController.getTopLevelElements();
            }
            object = Array.newInstance(arrcls, dimArr);
            for(int i = 0; i < dimensions; i++){
                Array.set(object, i, getRecursiveArray());
            }
        }*/
        //Object object = Array.newInstance(int.class, length);

        for(int i = 0; i < length; i++){
            System.out.println(Array.get(object, i));
        }
        field.set(obj, object);
    }
}
