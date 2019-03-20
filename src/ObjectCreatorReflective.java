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


    public static void notPrimitive(Field field, Object obj, int counter) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        if(field.getType().isArray()) {
            isAnArray(field, obj);
            return;
        }
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
                    notPrimitive(f, newField, counter);
                }
            }
            else if(f.getType().isArray()){
                isAnArray(f, newField);
            }
            else if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
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
                ObjectCreatorController.throwRangeError(field);
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
                ObjectCreatorController.throwRangeError(field);
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
        int length = ObjectCreatorController.getTopLevelElements(1, field);
        Object object = null;
        if(dimensions == 1) {
            arrClassName = arrClassName.substring(1);
            if (arrClassName.equals("I"))
                object = Array.newInstance(int.class, length);
            else if (arrClassName.equals("B"))
                object = Array.newInstance(byte.class, length);
            else if (arrClassName.equals("C"))
                object = Array.newInstance(char.class, length);
            else if (arrClassName.equals("J"))
                object = Array.newInstance(long.class, length);
            else if (arrClassName.equals("S"))
                object = Array.newInstance(short.class, length);
            else if (arrClassName.equals("Z"))
                object = Array.newInstance(boolean.class, length);
            else if (arrClassName.equals("D"))
                object = Array.newInstance(double.class, length);
            else if (arrClassName.equals("F"))
                object = Array.newInstance(float.class, length);
            else if (arrClassName.equals("Ljava.lang.String;"))
                object = Array.newInstance(String.class, length);
            else{
                object = Array.newInstance(Class.forName(arrClassName.replace(";","").replace("L","")
                .replace("[","")), length);

            }

            for (int i = 0; i < length; i++) {
                if (arrClassName.equals("I")) {
                    String j = ObjectCreatorController.getDimensionPopup(0, i, field);
                    Array.set(object, i, Integer.parseInt(j));
                } else if (arrClassName.equals("B")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Byte.parseByte(j));
                }
                else if (arrClassName.equals("C")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, j.charAt(0));
                }
                else if (arrClassName.equals("J")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Long.parseLong(j));
                }
                else if (arrClassName.equals("S")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Short.parseShort(j));
                }
                else if (arrClassName.equals("Z")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Boolean.parseBoolean(j));
                }
                else if (arrClassName.equals("D")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Double.parseDouble(j));
                }
                else if (arrClassName.equals("F")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, Float.parseFloat(j));
                }
                else if (arrClassName.equals("Ljava.lang.String;")){
                    String j = ObjectCreatorController.getDimensionPopup(0,i,field);
                    Array.set(object, i, j);
                }
                else{
                    Class cls = Class.forName(arrClassName.replace("[","").replace("L","")
                            .replace(";",""));
                    Object objInArr = cls.getConstructor(new Class[] {}).newInstance();
                    for(Field f : cls.getDeclaredFields()){
                        f.setAccessible(true);
                        if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String"))
                            ObjectCreatorController.getPrimitiveValue(f, objInArr);
                        else if(f.getType().isArray())
                            isAnArray(f, objInArr);
                        else {
                            notPrimitive(f, objInArr, 0);
                        }
                    }
                    Array.set(object, i, objInArr);
                }
            }
        }
        else if (dimensions == 2){
            int length2 = ObjectCreatorController.getTopLevelElements(2,field);
            if (arrClassName.equals("[[I"))
                object = Array.newInstance(int.class, length, length2);
            else if (arrClassName.equals("[[B"))
                object = Array.newInstance(byte.class, length, length2);
            else if (arrClassName.equals("[[C"))
                object = Array.newInstance(char.class, length, length2);
            else if (arrClassName.equals("[[J"))
                object = Array.newInstance(long.class, length, length2);
            else if (arrClassName.equals("[[S"))
                object = Array.newInstance(short.class, length, length2);
            else if (arrClassName.equals("[[Z"))
                object = Array.newInstance(boolean.class, length, length2);
            else if (arrClassName.equals("[[D"))
                object = Array.newInstance(double.class, length, length2);
            else if (arrClassName.equals("[[F"))
                object = Array.newInstance(float.class, length, length2);
            else if(arrClassName.equals("[[Ljava.lang.String;"))
                object = Array.newInstance(String.class, length, length2);
            else
                object = Array.newInstance(field.getType(), length, length2);
            for(int i = 0; i < length; i++){
                Object object2 = null;
                if (arrClassName.equals("[[I"))
                    object2 = Array.newInstance(int.class, length2);
                else if (arrClassName.equals("[[B"))
                    object2 = Array.newInstance(byte.class, length2);
                else if (arrClassName.equals("[[C"))
                    object2 = Array.newInstance(char.class, length2);
                else if (arrClassName.equals("[[J"))
                    object2 = Array.newInstance(long.class, length2);
                else if (arrClassName.equals("[[S"))
                    object2 = Array.newInstance(short.class, length2);
                else if (arrClassName.equals("[[Z"))
                    object2 = Array.newInstance(boolean.class, length2);
                else if (arrClassName.equals("[[D"))
                    object2 = Array.newInstance(double.class, length2);
                else if (arrClassName.equals("[[F"))
                    object2 = Array.newInstance(float.class, length2);
                else if (arrClassName.equals("[[Ljava.lang.String;"))
                    object2 = Array.newInstance(String.class, length2);
                else
                    object2 = Array.newInstance(field.getType(), length2);
                for(int j = 0; j < length2; j++){
                    String result = ObjectCreatorController.getDimensionPopup(i,j,field);
                    if (arrClassName.equals("[[I"))
                        Array.set(object2, j, Integer.parseInt(result));
                    else if (arrClassName.equals("[[B"))
                        Array.set(object2, j, Byte.parseByte(result));
                    else if (arrClassName.equals("[[C"))
                        Array.set(object2, j, result.charAt(0));
                    else if (arrClassName.equals("[[J"))
                        Array.set(object2, j, Long.parseLong(result));
                    else if (arrClassName.equals("[[S"))
                        Array.set(object2, j, Short.parseShort(result));
                    else if (arrClassName.equals("[[Z"))
                        Array.set(object2, j, Boolean.parseBoolean(result));
                    else if (arrClassName.equals("[[D"))
                        Array.set(object2, j, Double.parseDouble(result));
                    else if (arrClassName.equals("[[F"))
                        Array.set(object2, j, Float.parseFloat(result));
                    else if (arrClassName.equals("[[Ljava.lang.String;"))
                        Array.set(object2, j, result);
                    else
                        Array.set(object2, j, (Object) result);
                }
                Array.set(object, i, object2);
            }
        }
        field.set(obj, object);
    }

    /**
     *
     * @param arrClassName
     * @param dimArr  contains size of each dimension per index i
     * @param currentDim contains the current dimension we are looking at
     * @return
     */
    /*private static Object getRecursiveArray(String arrClassName, int[] dimArr, int currentDim) {
        //int[][][][] -> int[a][][][] -> int[][b][][] -> ... -> int[][][][d] -> here is where we prompt
        Object toReturn;
        for(int i = 0; i < dimArr[currentDim]; i++){

        }
    }*/
}
