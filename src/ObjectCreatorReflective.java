import java.lang.reflect.*;
import java.util.*;

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

    public static void isACollection(Field field, Object obj) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchFieldException {
        /*Class classObj = field.getType();
        Object object;
        if(field.getType().isInterface()) {
            System.out.println(field.getType());
            String name = field.getType().getName();
            String selectedImplementation = ObjectCreatorController.getImplementationChoice(name);
            classObj = Class.forName("java.util."+selectedImplementation);
            object = classObj.getConstructor(new Class[] {}).newInstance();
        }*/

        String ref = field.getType().getName();
        Class c = field.get(obj).getClass();
        field.setAccessible(true);
        int size = ObjectCreatorController.getCollectionSize(0, c);
        if(ref.equals("java.util.List") || ref.equals("java.util.ArrayList") || ref.equals("java.util.LinkedList")){
            List copy = null;
            if(c.getName().equals("java.util.ArrayList")){
                copy = (ArrayList<Object>) field.get(obj);
            }
            else if(c.getName().equals("java.util.LinkedList")){
                copy = (LinkedList<Object>) field.get(obj);
            }
            else {
                copy = (List<Object>) field.get(obj);
            }
            Object object = null;
            Class component = Helper.guessComponents(copy);
            for(int i = 0; i < size; i++){
                if(checkPrimitive(component)[0].equals(true)){
                    object = getPrimitive(component);
                }
                else {
                    object = component.getConstructor(new Class[]{}).newInstance();
                    for (Field f : object.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if (!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")) {
                            notPrimitive(f, object, 0);
                        } else if (f.getType().isArray()) {
                            isAnArray(f, object);
                        } else if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, object, obj);
                        }
                    }
                }
                copy.set(i, object);
            }
            field.set(obj, copy);
        }
        else if(ref.equals("java.util.Map") || ref.equals("java.util.HashMap") || ref.equals("java.util.LinkedHashMap") || ref
        .equals("java.util.TreeMap")) {
            Map copy;
            if (c.getName().equals("java.util.HashMap")) {
                copy = (HashMap<Object, Object>) field.get(obj);
            } else if (c.getName().equals("java.util.TreeMap")) {
                copy = (TreeMap<Object, Object>) field.get(obj);
            } else if (c.getName().equals("java.util.LinkedHashMap")) {
                copy = (LinkedHashMap<Object, Object>) field.get(obj);
            } else {
                copy = (Map<Object, Object>) field.get(obj);
            }
            Class keys = Helper.guessKeyComponents(copy);
            Class values = Helper.guessValueComponents(copy);
            for(int i = 0; i < size; i++){
                Object key = null;
                if(checkPrimitive(keys)[0].equals(true)) {
                    key = getPrimitive(keys);
                }
                else{
                    key = keys.getConstructor(new Class[]{}).newInstance();
                    for(Field f : key.getClass().getDeclaredFields()){
                        f.setAccessible(true);
                        if(!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")){
                            notPrimitive(f, key, 0);
                        }
                        else if(f.getType().isArray()){
                            isAnArray(f, key);
                        }
                        else if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, key, obj);
                        }
                    }
                }
                Object value = null;
                if(checkPrimitive(values)[0].equals(true)){
                    value = getPrimitive(values);
                }
                else{
                    value = values.getConstructor(new Class[] {}).newInstance();
                    for(Field f : value.getClass().getDeclaredFields()){
                        f.setAccessible(true);
                        if(!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")){
                            notPrimitive(f, value, 0);
                        }
                        else if(f.getType().isArray()){
                            isAnArray(f, value);
                        }
                        else if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, value, obj);
                        }
                    }
                }
                copy.put(key,value);
            }
            field.set(obj, copy);
        }
        else if(ref.equals("java.util.Queue")){
            Queue<Object> copy = new Queue<>() {
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
            };
            Class type = Helper.guessQueueComponents(copy);
            Object key = null;
            for(int i = 0; i < size; i++) {
                if(checkPrimitive(type)[0].equals(true)){
                    key = getPrimitive(type);
                }
                else {
                    key = type.getConstructor(new Class[]{}).newInstance();
                    for (Field f : key.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if (!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")) {
                            notPrimitive(f, key, 0);
                        } else if (f.getType().isArray()) {
                            isAnArray(f, key);
                        } else if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, key, obj);
                        }
                    }
                }
                copy.add(key);
            }
            field.set(obj, copy);
        }
        else if(ref.equals("java.util.Deque") || ref.equals("java.util.ArrayDeque")){
            Deque copy;
            if (c.getName().equals("java.util.ArrayDeque")) {
                copy = (ArrayDeque<Object>) field.get(obj);
            } else if (c.getName().equals("java.util.LinkedList")) {
                copy = (LinkedList<Object>) field.get(obj);
            } else {
                copy = (Deque) field.get(obj);
            }
            Class type = Helper.guessQueueComponents(copy);
            Object key = null;
            for(int i = 0; i < size; i++) {
                if(checkPrimitive(type)[0].equals(true)){
                    key = getPrimitive(type);
                }
                else {
                    key = type.getConstructor(new Class[]{}).newInstance();
                    for (Field f : key.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if (!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")) {
                            notPrimitive(f, key, 0);
                        } else if (f.getType().isArray()) {
                            isAnArray(f, key);
                        } else if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, key, obj);
                        }
                    }
                }
                copy.add(key);
            }
            field.set(obj, copy);
        }
        else if(ref.equals("java.util.Set") || ref.equals("java.util.HashSet") || ref.equals("java.util.TreeSet") ||
        ref.equals("LinkedHashSet")){
            Set copy;
            if(c.getName().equals("java.util.HashSet")){
                copy = (HashSet<Object>) field.get(obj);
            }
            else if(c.getName().equals("java.util.TreeSet")){
                copy = (TreeSet<Object>) field.get(obj);
            }
            else if(c.getName().equals("LinkedHashSet")){
                copy = (LinkedHashSet<Object>) field.get(obj);
            }
            else
                copy = (Set<Object>) field.get(obj);
            Class type = Helper.guessSetComponents(copy);
            Object key = null;
            for(int i = 0; i < size; i++) {
                if(checkPrimitive(type)[0].equals(true)){
                    key = getPrimitive(type);
                }
                else {
                    key = type.getConstructor(new Class[]{}).newInstance();
                    for (Field f : key.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if (!f.getType().isPrimitive() && !f.getType().getName().equals("java.lang.String")) {
                            notPrimitive(f, key, 0);
                        } else if (f.getType().isArray()) {
                            isAnArray(f, key);
                        } else if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            ObjectCreatorController.createPopUp(f, key, obj);
                        }
                    }
                }
                copy.add(key);
            }
            field.set(obj, copy);
        }

    }

    private static Object[] checkPrimitive(Class cls) {
        Object[] objArr = {null, null};
        if(cls.getName().equals("java.lang.Integer")) {
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Byte")) {
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Double")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Character")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Long")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Float")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Short")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.Boolean")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else if(cls.getName().equals("java.lang.String")){
            objArr[0] = true;
            objArr[1] = cls.getName();
        }
        else{
            objArr[0] = false;
            objArr[1] = cls.getName();
        }
        return objArr;
    }

    private static Object getPrimitive(Class cls){
        Object toReturn = null;
        if(cls.getName().equals("java.lang.Integer")) {
            int i = Integer.parseInt(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = i;
        }
        else if(cls.getName().equals("java.lang.Byte")) {
            byte b = Byte.parseByte(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = b;
        }
        else if(cls.getName().equals("java.lang.Double")){
            double d = Double.parseDouble(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = d;
        }
        else if(cls.getName().equals("java.lang.Character")){
            char c = ObjectCreatorController.getObjectPrimitive(cls).charAt(0);
            toReturn = c;
        }
        else if(cls.getName().equals("java.lang.Long")){
            long l = Long.parseLong(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = l;
        }
        else if(cls.getName().equals("java.lang.Float")){
            float f = Float.parseFloat(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = f;
        }
        else if(cls.getName().equals("java.lang.Short")){
            short s = Short.parseShort(ObjectCreatorController.getObjectPrimitive(cls));
            toReturn = s;
        }
        else if(cls.getName().equals("java.lang.Boolean")){
            String s = ObjectCreatorController.getObjectPrimitive(cls);
            if (s.equals("1")) {
                s = "true";
            }
            if (s.equals("0")) {
                s = "false";
            }
            boolean b = Boolean.parseBoolean(s);
            toReturn = b;
        }
        else if(cls.getName().equals("java.lang.String")){
            String s = ObjectCreatorController.getObjectPrimitive(cls);
            toReturn = s;
        }
        return toReturn;

    }
}
