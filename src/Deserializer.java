import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Deserializer {


    public Object deserialize(Document doc) throws InvocationTargetException, ClassNotFoundException,
            InstantiationException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {

        List<Element> myList = doc.getRootElement().getChildren();
        HashMap myMap = new HashMap();
        createInstances(myMap, myList);
        assignValues(myMap,myList);
        Object newOb = myMap.get("0");
        return newOb;
    }
    public ArrayList<Object> recreateObject(ArrayList<Document> newDoc) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {


        int count = 0;
        ArrayList<Object> obj = new ArrayList<>();
        for(Document d : newDoc){
            Object ogh = deserialize(d);
            obj.add(ogh);
        }
        return obj;
    }

    public static void createInstances(HashMap myMap, List<Element> myList) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
        for (int i=0; i< myList.size(); i++) {
            Element listEle = myList.get(i);
            Class cls = Class.forName(listEle.getAttributeValue("class"));
            Object instance = null;
            if(isCollection(cls)){
                instance = getNewCollectionInstance(cls);
            }
            else if (!cls.isArray()) {
                instance = cls.getDeclaredConstructor(null).newInstance();
            }else
                instance = Array.newInstance(cls.getComponentType(), Integer.parseInt(listEle.getAttributeValue("length")));
            myMap.put(listEle.getAttributeValue("id"), instance);
        }
    }

    private static Object getNewCollectionInstance(Class cls) {
        Class c = cls;
        String ref = c.getTypeName();
        Object toReturn = null;
        if(ref.equals("java.util.List") || ref.equals("java.util.ArrayList") || ref.equals("java.util.LinkedList")){
            if(ref.equals("java.util.List")){
                toReturn = new List<>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }

                    @Override
                    public Iterator<Object> iterator() {
                        return null;
                    }

                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @Override
                    public <T> T[] toArray(T[] a) {
                        return null;
                    }

                    @Override
                    public boolean add(Object o) {
                        return false;
                    }

                    @Override
                    public boolean remove(Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(int index, Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public Object get(int index) {
                        return null;
                    }

                    @Override
                    public Object set(int index, Object element) {
                        return null;
                    }

                    @Override
                    public void add(int index, Object element) {

                    }

                    @Override
                    public Object remove(int index) {
                        return null;
                    }

                    @Override
                    public int indexOf(Object o) {
                        return 0;
                    }

                    @Override
                    public int lastIndexOf(Object o) {
                        return 0;
                    }

                    @Override
                    public ListIterator<Object> listIterator() {
                        return null;
                    }

                    @Override
                    public ListIterator<Object> listIterator(int index) {
                        return null;
                    }

                    @Override
                    public List<Object> subList(int fromIndex, int toIndex) {
                        return null;
                    }
                };
            }
            else if(ref.equals("java.util.ArrayList")){
                toReturn = new ArrayList<>();
            }
            else if(ref.equals("java.util.LinkedList")){
                toReturn = new LinkedList<>();
            }
        }
        else if(ref.equals("java.util.Map") || ref.equals("java.util.HashMap") || ref.equals("java.util.LinkedHashMap") || ref
                .equals("java.util.TreeMap")) {
            if(ref.equals("java.util.Map")){
                toReturn = new Map<>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean containsKey(Object key) {
                        return false;
                    }

                    @Override
                    public boolean containsValue(Object value) {
                        return false;
                    }

                    @Override
                    public Object get(Object key) {
                        return null;
                    }

                    @Override
                    public Object put(Object key, Object value) {
                        return null;
                    }

                    @Override
                    public Object remove(Object key) {
                        return null;
                    }

                    @Override
                    public void putAll(Map<?, ?> m) {

                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public Set<Object> keySet() {
                        return null;
                    }

                    @Override
                    public Collection<Object> values() {
                        return null;
                    }

                    @Override
                    public Set<Entry<Object, Object>> entrySet() {
                        return null;
                    }
                };
            }
            else if(ref.equals("java.util.HashMap")){
                toReturn = new HashMap<>();
            }
            else if(ref.equals("java.util.LinkedHashMap")){
                toReturn = new LinkedHashMap<>();
            }
        }
        else if(ref.equals("java.util.Queue")){
            toReturn = new Queue<>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @Override
                public Iterator<Object> iterator() {
                    return null;
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return null;
                }

                @Override
                public boolean add(Object o) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public boolean offer(Object o) {
                    return false;
                }

                @Override
                public Object remove() {
                    return null;
                }

                @Override
                public Object poll() {
                    return null;
                }

                @Override
                public Object element() {
                    return null;
                }

                @Override
                public Object peek() {
                    return null;
                }
            };
        }
        else if(ref.equals("java.util.Deque") || ref.equals("java.util.ArrayDeque")){
            if(ref.equals("java.util.Deque")){
                toReturn = new Deque<>() {
                    @Override
                    public void addFirst(Object o) {

                    }

                    @Override
                    public void addLast(Object o) {

                    }

                    @Override
                    public boolean offerFirst(Object o) {
                        return false;
                    }

                    @Override
                    public boolean offerLast(Object o) {
                        return false;
                    }

                    @Override
                    public Object removeFirst() {
                        return null;
                    }

                    @Override
                    public Object removeLast() {
                        return null;
                    }

                    @Override
                    public Object pollFirst() {
                        return null;
                    }

                    @Override
                    public Object pollLast() {
                        return null;
                    }

                    @Override
                    public Object getFirst() {
                        return null;
                    }

                    @Override
                    public Object getLast() {
                        return null;
                    }

                    @Override
                    public Object peekFirst() {
                        return null;
                    }

                    @Override
                    public Object peekLast() {
                        return null;
                    }

                    @Override
                    public boolean removeFirstOccurrence(Object o) {
                        return false;
                    }

                    @Override
                    public boolean removeLastOccurrence(Object o) {
                        return false;
                    }

                    @Override
                    public boolean add(Object o) {
                        return false;
                    }

                    @Override
                    public boolean offer(Object o) {
                        return false;
                    }

                    @Override
                    public Object remove() {
                        return null;
                    }

                    @Override
                    public Object poll() {
                        return null;
                    }

                    @Override
                    public Object element() {
                        return null;
                    }

                    @Override
                    public Object peek() {
                        return null;
                    }

                    @Override
                    public boolean addAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public void push(Object o) {

                    }

                    @Override
                    public Object pop() {
                        return null;
                    }

                    @Override
                    public boolean remove(Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }

                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public Iterator<Object> iterator() {
                        return null;
                    }

                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @Override
                    public <T> T[] toArray(T[] a) {
                        return null;
                    }

                    @Override
                    public Iterator<Object> descendingIterator() {
                        return null;
                    }
                };
            }
            else if(ref.equals("java.util.ArrayDeque")){
                toReturn = new ArrayDeque<>();
            }
        }
        else if(ref.equals("java.util.Set") || ref.equals("java.util.HashSet") || ref.equals("java.util.TreeSet") ||
                ref.equals("LinkedHashSet")){
            if(ref.equals("java.util.Set")){
                toReturn = new Set<>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }

                    @Override
                    public Iterator<Object> iterator() {
                        return null;
                    }

                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @Override
                    public <T> T[] toArray(T[] a) {
                        return null;
                    }

                    @Override
                    public boolean add(Object o) {
                        return false;
                    }

                    @Override
                    public boolean remove(Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }
                };
            }
            else if(ref.equals("java.util.HashSet")){
                toReturn = new HashSet<>();
            }
            else if(ref.equals("java.util.TreeSet")){
                toReturn = new TreeSet<>();
            }
            else if(ref.equals("java.util.LinkedHashSet")){
                toReturn = new LinkedHashSet<>();
            }
        }
        return toReturn;
    }

    private static boolean isCollection(Class cls) {
        Class c = cls;
        String ref = c.getTypeName();
        if(ref.equals("java.util.List") || ref.equals("java.util.ArrayList") || ref.equals("java.util.LinkedList")){
            return true;
        }
        else if(ref.equals("java.util.Map") || ref.equals("java.util.HashMap") || ref.equals("java.util.LinkedHashMap") || ref
                .equals("java.util.TreeMap")) {
            return true;
        }
        else if(ref.equals("java.util.Queue")){
            return true;
        }
        else if(ref.equals("java.util.Deque") || ref.equals("java.util.ArrayDeque")){
            return true;
        }
        else if(ref.equals("java.util.Set") || ref.equals("java.util.HashSet") || ref.equals("java.util.TreeSet") ||
                ref.equals("LinkedHashSet")){
            return true;
        }
        else{
            return false;
        }
    }

    public static void assignValues(HashMap myMap, List<Element> myList) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        for (int i=0; i < myList.size(); i++) {
            Element listEle = (Element) myList.get(i);
            Object instance = myMap.get(listEle.getAttributeValue("id"));
            List<Element> children = listEle.getChildren();
            if(isCollection(instance.getClass())){
                String ref = instance.getClass().getName();
                for(Element child : children) {
                    Object copy = null;
                    List<Content> content = child.getContent();
                    if (ref.equals("java.util.List") || ref.equals("java.util.ArrayList") || ref.equals("java.util.LinkedList")) {
                        assert (instance instanceof List);
                        if(child.getName().equals("reference")) {
                            ((List) instance).add(myMap.get(content.get(0).getValue()));
                            myMap.put(listEle.getAttributeValue("id"), instance);
                        }
                        else {
                            ((List) instance).add(content.get(0).getValue());
                            myMap.put(listEle.getAttributeValue("id"), instance);
                        }
                    } else if (ref.equals("java.util.Map") || ref.equals("java.util.HashMap") || ref.equals("java.util.LinkedHashMap") || ref
                            .equals("java.util.TreeMap")) {
                            assert (instance instanceof Map);
                            String sd = content.get(0).getValue();
                            String dj = content.get(0).getCType().toString();
                            String jg = content.get(2).getValue();
                            String test = content.get(2).getCType().toString();
                            Object c1 = "";
                            Object c2 = "";
                            if(dj.equals("Element")){
                                c1 = myMap.get(sd);
                            }
                            else
                                c1 = sd;
                            if(test.equals("Element")){
                                c2 = myMap.get(jg);
                            }
                            else
                                c2 = jg;
                            ((Map) instance).put(sd,jg);
                            myMap.put(listEle.getAttributeValue("id"), instance);
                    } else if (ref.equals("java.util.Queue")) {
                        assert (instance instanceof Queue);
                        if(child.getName().equals("reference"))
                            ((Queue) instance).add(myMap.get(content.get(0).getValue()));
                        else
                            ((Queue) instance).add(content.get(0).getValue());
                        myMap.put(listEle.getAttributeValue("id"), instance);
                    } else if (ref.equals("java.util.Deque") || ref.equals("java.util.ArrayDeque")) {
                        assert (instance instanceof Deque);
                        if(child.getName().equals("reference"))
                            ((Deque) instance).add(myMap.get(content.get(0).getValue()));
                        else
                            ((Deque) instance).add(content.get(0).getValue());
                        myMap.put(listEle.getAttributeValue("id"), instance);
                    } else if (ref.equals("java.util.Set") || ref.equals("java.util.HashSet") || ref.equals("java.util.TreeSet") ||
                            ref.equals("LinkedHashSet")) {
                        assert (instance instanceof Set);
                        if(child.getName().equals("reference"))
                            ((Set) instance).add(myMap.get(content.get(0).getValue()));
                        else
                            ((Set) instance).add(content.get(0).getValue());
                        myMap.put(listEle.getAttributeValue("id"), instance);
                    }
                }

            }
            else if (!instance.getClass().isArray()) {
                for (int j=0; j<children.size(); j++) {
                    Element fElt = children.get(j);
                    if(fElt.getName().equals("reference")){
                        String val = fElt.getContent(0).getValue();
                        Object hashValue = myMap.get(val);
                        for(Field f : instance.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(isCollection(f.getType())){
                                continue;
                            }
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
                        if(fElt.getChildren().size() != 0) {
                            Element vElt = (Element) fElt.getChildren().get(0);
                            f.set(instance, deserializeValue(vElt, f.getType(), myMap));
                        }
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
