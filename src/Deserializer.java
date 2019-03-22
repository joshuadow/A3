import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Deserializer {

    public static void recreateObject(ArrayList<Document> newDoc) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {


        ArrayList<Object> obj = new ArrayList<>();
        int count = 0;
        for(Document d : newDoc){
            List<Element> myList = d.getRootElement().getChildren();
            HashMap myMap = new HashMap();
            createInstances(myMap, myList);
            assignValues(myMap,myList);
            obj.add(myMap.get("0"));
            System.out.println(obj.toString());
        }
    }

    public static void createInstances(HashMap myMap, List<Element> myList) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
        for (int i=0; i< myList.size(); i++) {
            Element listEle = myList.get(i);
            Class cls = Class.forName(listEle.getAttributeValue("class"));
            Object instance = null;
            if (!cls.isArray()) {
                instance = cls.getDeclaredConstructor(null).newInstance();
            } else
                instance = Array.newInstance(cls.getComponentType(), Integer.parseInt(listEle.getAttributeValue("length")));
            myMap.put(listEle.getAttributeValue("id"), instance);
        }
    }

    public static void assignValues(HashMap myMap, List<Element> myList) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        for (int i=0; i < myList.size(); i++) {
            Element listEle = (Element) myList.get(i);
            Object instance = myMap.get(listEle.getAttributeValue("id"));
            List<Element> children = listEle.getChildren();
            if (!instance.getClass().isArray()) {
                for (int j=0; j<children.size(); j++) {
                    Element fElt = children.get(j);
                    if(fElt.getName().equals("reference")){
                        String val = fElt.getContent(0).getValue();
                        Object hashValue = myMap.get(val);
                        for(Field f : instance.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(f.getType().getName().equals(hashValue.getClass().getName())){
                                f.set(instance, hashValue);
                            }
                        }
                    }
                    else {
                        String className = fElt.getAttributeValue("declaringclass");
                        Class fieldDC = Class.forName(className);
                        String fieldName = fElt.getAttributeValue("name");
                        Field f = fieldDC.getDeclaredField(fieldName);
                        f.setAccessible(true);
                        Element vElt = (Element) fElt.getChildren().get(0);
                        f.set(instance, deserializeValue(vElt, f.getType(), myMap));
                    }
                }
            } else {
                for (int j=0; j<children.size(); j++) {
                    Class comptype = instance.getClass().getComponentType();
                    Element idxOrValue = children.get(j);
                    int count;
                    if(comptype.getName().charAt(0) != '[') {
                        if(comptype.getName().charAt(comptype.getName().length() -1) == ';'){

                        }
                        else {
                            Array.set(instance, j, deserializeValue(idxOrValue, comptype, myMap));
                        }
                    }
                    else{
                        Object object = null;
                        int length2 = idxOrValue.getContentSize();
                        if (comptype.getName().equals("[I")) {
                            object = Array.newInstance(int.class, length2);
                            comptype = int.class;
                        }
                        else if (comptype.getName().equals("[B")) {
                            object = Array.newInstance(byte.class, length2);
                            comptype = byte.class;
                        }
                        else if (comptype.getName().equals("[C")) {
                            object = Array.newInstance(char.class, length2);
                            comptype = char.class;
                        }
                        else if (comptype.getName().equals("[J")) {
                            object = Array.newInstance(long.class, length2);
                            comptype = long.class;
                        }
                        else if (comptype.getName().equals("[S")) {
                            object = Array.newInstance(short.class, length2);
                            comptype = short.class;
                        }
                        else if (comptype.getName().equals("[Z")) {
                            object = Array.newInstance(boolean.class, length2);
                            comptype = boolean.class;
                        }
                        else if (comptype.getName().equals("[D")) {
                            object = Array.newInstance(double.class, length2);
                            comptype = double.class;
                        }
                        else if (comptype.getName().equals("[F")) {
                            object = Array.newInstance(float.class, length2);
                            comptype = float.class;
                        }
                        else if(comptype.getName().equals("[Ljava.lang.String;")) {
                            object = Array.newInstance(String.class, length2);
                            comptype = String.class;
                        }
                        else {
                            object = Array.newInstance(comptype, length2);
                        }

                        for(int k = 0; k < idxOrValue.getContentSize(); k++){
                            Element sub = idxOrValue.getChildren().get(k);
                            Object idxValue = deserializeValue(sub, comptype, myMap);
                            Array.set(object, k, idxValue);
                        }
                        Array.set(instance, j, object);

                    }
                }

            }
        }

    }

    private static Object deserializeValue(Element vElt, Class<?> type, HashMap myMap) {
        String valtype = vElt.getName();
        if (valtype.equals("null"))
            return null;
        else if (valtype.equals("reference"))
            return myMap.get(vElt.getText());
        else {
            if (type.equals(boolean.class)) {
                if (vElt.getText().equals("true"))
                    return Boolean.TRUE;
                else
                    return Boolean.FALSE;
            }
            else if (type.equals(byte.class))
                return Byte.valueOf(vElt.getText());
            else if (type.equals(short.class))
                return Short.valueOf(vElt.getText());
            else if (type.equals(int.class))
                return Integer.valueOf(vElt.getText());
            else if (type.equals(long.class))
                return Long.valueOf(vElt.getText());
            else if (type.equals(float.class))
                return Float.valueOf(vElt.getText());
            else if (type.equals(double.class))
                return Double.valueOf(vElt.getText());
            else if (type.equals(char.class))
                return vElt.getText().charAt(0);
            else
                return vElt.getText();

        }
    }

    /*public static Object recurseRebuild(Object obj, Element ele) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Object toReturn = null;
        Object[] oArr = ObjectCreatorReflective.checkPrimitive(obj.getClass());
        if(oArr[0].equals(true)){
            if(oArr[1].equals("java.lang.Integer")){
                int i;
                toReturn = Integer.parseInt(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Double")){
                double d;
                toReturn = Double.parseDouble(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Short")){
                short s;
                toReturn = Short.parseShort(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Float")){
                float f;
                toReturn = Float.parseFloat(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Long")){
                long l;
                toReturn = Long.parseLong(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Byte")){
                byte b;
                toReturn = Byte.parseByte(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.Char")){
                char c;
                toReturn = ele.getChild("value").getContent(0).getValue().charAt(0);
            }
            else if(oArr[1].equals("java.lang.Boolean")){
                boolean b;
                toReturn = Boolean.parseBoolean(ele.getChild("value").getContent(0).getValue());
            }
            else if(oArr[1].equals("java.lang.String")){
                String s;
                toReturn = ele.getChild("value").getContent(0).getValue();
            }
        }
        /*else if(cls.isArray()){

        }
        else if(!cls.isPrimitive()){
            for(Field f : obj.getClass().getDeclaredFields()){
                f.setAccessible(true);
                Object value = null;
                List<Element> newList = e.getChildren();
                if(f.getType().isPrimitive()){
                    for(Element v : newList){
                        String s = v.getAttribute("field").getValue();
                        if(s.equals(f.getName())){
                            f.set(v, s);
                        }
                    }
                }
                else if(f.getType().isArray()){

                }
                else{

                }

                for(Element g : newList) {
                    value = recurseRebuild(obj, g);
                }
                f.set(obj, value);
            }

        }
        return toReturn;
    }*/
}
