import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;

public class Serializer {
    private IdentityHashMap<Object, Integer> identityHashMap = new IdentityHashMap<Object, Integer>();
    private int counter = 0;
    public Document serialize(Object o) throws IllegalAccessException {

        Document document = new Document();
        XMLOutputter xOut = new XMLOutputter(Format.getPrettyFormat());
        Element root = new Element("serialized");
        document.addContent(root);
        String className = o.getClass().getName();
        identityHashMap.put(o, counter);
        counter++;
        Element obj = new Element("object");
        obj.setAttribute("class", className);
        obj.setAttribute("id", identityHashMap.get(o).toString());
        if(o.getClass().isArray()){
            obj.setAttribute("length", String.valueOf(Array.getLength(o)));
        }
        root.addContent(obj);
        Object[] oArr = ObjectCreatorReflective.checkPrimitive(o.getClass());
        recurseElements(o, oArr, obj, root);

        return document;
    }

    private void addReferenceObject(Field f, Object o, Element root) throws IllegalAccessException {
        Element obj = new Element("object");
        obj.setAttribute("class", f.getType().getName());
        obj.setAttribute("id", identityHashMap.get(f.get(o)).toString());
        if(checkArray(f)){
            obj.setAttribute("length", String.valueOf(Array.getLength(f.get(o))));
        }
        root.addContent(obj);
        Object fgeto = f.get(o);
        Object[] oArr = ObjectCreatorReflective.checkPrimitive(fgeto.getClass());
        recurseElements(fgeto, oArr, obj, root);
    }

    private boolean checkArray(Field f) {
        String arrClassName = f.getType().getName();
        int dimensions = 0;
        for(int i = 0; i < arrClassName.length(); i++){
            if(arrClassName.charAt(i) == '[')
                dimensions++;
        }
        if(dimensions > 0){
            return true;
        }
        else{
            return false;
        }
    }

    private int getDimensions(Object o){
        String arrClassName = o.getClass().getName();
        int dimensions = 0;
        for(int i = 0; i < arrClassName.length(); i++){
            if(arrClassName.charAt(i) == '[')
                dimensions++;
        }
        return dimensions;
    }

    public void recurseElements(Object o, Object[] oArr, Element obj, Element root) throws IllegalAccessException {
        if(o == null){
            Element nullEle = new Element("value");
            nullEle.addContent("null");
            obj.addContent(nullEle);
        }
        if(o.getClass().isPrimitive() || oArr[0].equals(true)){
            String primName = oArr[1].toString();
            Element primValue = new Element("value");
            if(primName.equals("java.lang.Integer")){
                int i = (int) o;
                primValue.addContent(String.valueOf(i));
            }
            else if(primName.equals("java.lang.Byte")){
                byte b = (byte) o;
                primValue.addContent(String.valueOf(b));
            }
            else if(primName.equals("java.lang.Double")){
                double d = (double) o;
                primValue.addContent(String.valueOf(d));
            }
            else if(primName.equals("java.lang.Short")){
                short s = (short) o;
                primValue.addContent(String.valueOf(s));
            }
            else if(primName.equals("java.lang.Long")){
                long l = (long) o;
                primValue.addContent(String.valueOf(l));
            }
            else if(primName.equals("java.lang.Float")){
                float f = (float) o;
                primValue.addContent(String.valueOf(f));
            }
            else if(primName.equals("java.lang.Boolean")){
                boolean b = (boolean) o;
                primValue.addContent(String.valueOf(b));
            }
            else if(primName.equals("java.lang.String")){
                String s = (String) o;
                primValue.addContent(s);
            }
            else if(primName.equals("java.lang.Character")){
                char c = (char) o;
                primValue.addContent(String.valueOf(c));
            }
            obj.addContent(primValue);
        }
        else if(o.getClass().isArray()){
            for(int i = 0; i < Array.getLength(o); i++){
                if(getDimensions(o) > 1){
                    Element obj2 = new Element("index");
                    Object a = Array.get(o, i);
                    Object[] dArr = ObjectCreatorReflective.checkPrimitive(a.getClass());
                    recurseElements(a, dArr, obj2, root);
                    obj.addContent(obj2);
                }
                else{
                    Object a = Array.get(o, i);
                    Object[] dArr = ObjectCreatorReflective.checkPrimitive(a.getClass());
                    recurseElements(a, dArr, obj, root);
                }

            }

        }
        else if(!o.getClass().isPrimitive()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                String fieldName = f.getName();
                String declaringClasss = f.getDeclaringClass().getName();
                Element field = new Element("field");
                field.setAttribute("name", fieldName);
                field.setAttribute("declaringclass", declaringClasss);
                if(f.get(o) == null){
                    field.addContent("null");
                    obj.addContent(field);
                    continue;
                }
                if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                    Element value = new Element("value");
                    value.addContent(f.get(o).toString());
                    field.addContent(value);
                    obj.addContent(field);
                }
                else {
                    Element reference = new Element("reference");
                    identityHashMap.put(f.get(o), counter);
                    reference.addContent(String.valueOf(counter));
                    obj.addContent(reference);
                    addReferenceObject(f, o, root);
                    counter++;

                }

            }
        }
    }

}
