import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

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
        String oName = o.getClass().getName();
        if(o.getClass().isPrimitive() || oArr[0].equals(true)){
            String primName = oArr[1].toString();
            Element primValue = new Element("value");
            primElements(primValue, primName, o);
            obj.addContent(primValue);
        }
        else if(o.getClass().isArray()){
            for(int i = 0; i < Array.getLength(o); i++){
                if(getDimensions(o) > 1 || !Array.get(o,i).getClass().isPrimitive()){
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
        else if(oName.equals("java.util.HashMap") || oName.equals("java.util.Map") ||
                oName.equals("java.util.LinkedHashMap") || oName.equals("java.util.TreeMap")){
            Map copy;
            if (oName.equals("java.util.HashMap")) {
                copy = (HashMap<Object, Object>) o;
            } else if (oName.equals("java.util.TreeMap")) {
                copy = (TreeMap<Object, Object>) o;
            } else if (oName.equals("java.util.LinkedHashMap")) {
                copy = (LinkedHashMap<Object, Object>) o;
            } else {
                copy = (Map<Object, Object>) o;
            }
            for(Object key : copy.keySet()){
                Object[] dArr = ObjectCreatorReflective.checkPrimitive(key.getClass());
                Element mapValue = new Element("value");
                if(dArr[0].equals(true)) {
                    primElements(mapValue, key.getClass().getName(), key);
                }
                else{
                    recurseElements(key, dArr, mapValue, root);
                }
                mapValue.addContent(",");
                dArr = ObjectCreatorReflective.checkPrimitive(copy.get(key).getClass());
                if(dArr[0].equals(true)){
                    primElements(mapValue, copy.get(key).getClass().getName(), copy.get(key));
                }
                else{
                    recurseElements(copy.get(key), dArr, mapValue, root);
                }
                obj.addContent(mapValue);
            }
        }
        else if(oName.equals("java.util.List") || oName.equals("java.util.ArrayList") || oName.equals("java.LinkedList")){
            List copy = null;
            if(oName.equals("java.util.ArrayList")){
                copy = (ArrayList<Object>) o;
            }
            else if(oName.equals("java.util.LinkedList")){
                copy = (LinkedList<Object>) o;
            }
            else {
                copy = (List<Object>) o;
            }
            for(int i = 0; i < copy.size(); i++){
                Object[] dArr = ObjectCreatorReflective.checkPrimitive(copy.get(i).getClass());
                Element listValue = new Element("value");
                if(dArr[0].equals(true)){
                    primElements(listValue, copy.get(i).getClass().getName(), copy.get(i));
                }
                else{
                    recurseElements(copy.get(i), dArr, listValue, root);
                }
            }
        }
        else if(oName.equals("java.util.Queue")){
            Queue<Object> copy = (Queue<Object>) o;
            for(Object gh : copy){
                Object[] dArr = ObjectCreatorReflective.checkPrimitive(gh.getClass());
                Element listValue = new Element("value");
                if(dArr[0].equals(true)){
                    primElements(listValue, gh.getClass().getName(), gh);
                }
                else{
                    recurseElements(gh, dArr, listValue, root);
                }
            }

        }
        else if(oName.equals("java.util.Deque") || oName.equals("java.util.ArrayDeque")){
            Deque<Object> copy = null;
            if(oName.equals("java.util.ArrayDeque")){
                copy = (ArrayDeque<Object>) o;
            }
            else{
                copy = (Deque<Object>) o;
            }
            for(Object dh : copy){
                Object[] dArr = ObjectCreatorReflective.checkPrimitive(dh.getClass());
                Element listValue = new Element("value");
                if(dArr[0].equals(true)){
                    primElements(listValue, dh.getClass().getName(), dh);
                }
                else{
                    recurseElements(dh, dArr, listValue, root);
                }
            }
        }
        else if(oName.equals("java.util.Set") || oName.equals("java.util.HashSet") || oName.equals("java.util.TreeSet")
        || oName.equals("java.util.LinkedHashSet")){

            Set copy;
            if(oName.equals("java.util.HashSet")){
                copy = (HashSet<Object>) o;
            }
            else if(oName.equals("java.util.TreeSet")){
                copy = (TreeSet<Object>) o;
            }
            else if(oName.equals("LinkedHashSet")){
                copy = (LinkedHashSet<Object>) o;
            }
            else
                copy = (Set<Object>) o;
            for(Object jh : copy){
                Object[] dArr = ObjectCreatorReflective.checkPrimitive(jh.getClass());
                Element listValue = new Element("value");
                if(dArr[0].equals(true)){
                    primElements(listValue, jh.getClass().getName(), jh);
                }
                else{
                    recurseElements(jh, dArr, listValue, root);
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

    public void primElements(Element primValue, String primName, Object o){
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
    }

}
