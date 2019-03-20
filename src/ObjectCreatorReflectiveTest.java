import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCreatorReflectiveTest {

    Scanner input = new Scanner(System.in);
    @Test
    public void testReference() throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

      Class classObj = Class.forName("ReferenceToOthers");
      Object obj = classObj.getConstructor(new Class[] {}).newInstance();
      Field field = classObj.getDeclaredField("p");
      Object newObj = Class.forName(field.getType().getName()).getConstructor(new Class[] {}).newInstance();
      field.setAccessible(true);
      Field f = newObj.getClass().getDeclaredField("aBoolean");
      f.setAccessible(true);

      ObjectCreatorReflective.parseFields(f, newObj, "true");
      field.set(obj, newObj);
      assertTrue(true);

    }

    @Test
    public void testCircularReference() throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

        Class classObj = Class.forName("CircularTwo");
        Object obj = classObj.getConstructor(new Class[] {}).newInstance();
        Field cone = classObj.getDeclaredField("c1");
        cone.setAccessible(true);
        Class classObj2 = Class.forName(cone.getType().getName());
        Object obj2 = classObj2.getConstructor(new Class[] {}).newInstance();
        Field f = classObj2.getDeclaredField("c2");
        f.setAccessible(true);
        cone.set(obj, obj2);
        assertTrue(true);
    }

    @Test
    public void testFieldNamesEqual(){
        int i = 0;
        int j = 0;
        byte b = 0;
        assertFalse(ObjectCreatorReflective.fieldNamesEqual(i,b));
        assertTrue(ObjectCreatorReflective.fieldNamesEqual(i,j));
    }

    @Test
    public void testPrimitiveArray() throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class classobj = Class.forName("PrimitiveArray");
        Object obj = classobj.getConstructor(new Class[] {}).newInstance();
        Field field = classobj.getDeclaredField("intArr");
        field.setAccessible(true);
        String arrClassName = field.getType().getName();
        int dimensions = 0;
        String arrayClass = "I";
        for(int i = 0; i < arrClassName.length(); i++){
            if(arrClassName.charAt(i) == '[')
                arrayClass = "[" + arrayClass;
        }
        Class arrcls = Class.forName(arrayClass);
        int length = 4;
        Object object = Array.newInstance(int.class, length);
        for(int i = 0; i < length; i++){
            Array.set(object, i, i*2);
        }
        field.set(obj, object);
    }

    @Test
    public void testCollections() {
        Queue<Integer> queue = new Queue<Integer>() {
            @Override
            public boolean add(Integer integer) {
                return false;
            }

            @Override
            public boolean offer(Integer integer) {
                return false;
            }

            @Override
            public Integer remove() {
                return null;
            }

            @Override
            public Integer poll() {
                return null;
            }

            @Override
            public Integer element() {
                return null;
            }

            @Override
            public Integer peek() {
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
            public Iterator<Integer> iterator() {
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
            public boolean addAll(Collection<? extends Integer> c) {
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
        System.out.println("Super class: " + queue.getClass().getGenericSuperclass());
        System.out.println("Name: " + queue.getClass());
        System.out.println("Type Name: " + Arrays.deepToString(queue.getClass().getGenericInterfaces()));
        System.out.println("------------------------------------");
        Collection<Integer> collection = new Collection<Integer>() {
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
            public Iterator<Integer> iterator() {
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
            public boolean add(Integer integer) {
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
            public boolean addAll(Collection<? extends Integer> c) {
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
        System.out.println("Super class: " + collection.getClass().getGenericSuperclass());
        System.out.println("Name: " + collection.getClass().getName());
        System.out.println("Type Name: " + Arrays.deepToString(collection.getClass().getGenericInterfaces()));
        System.out.println("------------------------------------");
        List<Integer> list = new List<Integer>() {
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
            public Iterator<Integer> iterator() {
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
            public boolean add(Integer integer) {
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
            public boolean addAll(Collection<? extends Integer> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Integer> c) {
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
            public Integer get(int index) {
                return null;
            }

            @Override
            public Integer set(int index, Integer element) {
                return null;
            }

            @Override
            public void add(int index, Integer element) {

            }

            @Override
            public Integer remove(int index) {
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
            public ListIterator<Integer> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Integer> listIterator(int index) {
                return null;
            }

            @Override
            public List<Integer> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        System.out.println("Super class: " + list.getClass().getGenericSuperclass());
        System.out.println("Name: " + list.getClass().getName());
        System.out.println("Type Name: " + Arrays.deepToString(list.getClass().getGenericInterfaces()));
        System.out.println("------------------------------------");
        Map<Integer, Integer> map = new Map<Integer, Integer>() {
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
            public Integer get(Object key) {
                return null;
            }

            @Override
            public Integer put(Integer key, Integer value) {
                return null;
            }

            @Override
            public Integer remove(Object key) {
                return null;
            }

            @Override
            public void putAll(Map<? extends Integer, ? extends Integer> m) {

            }

            @Override
            public void clear() {

            }

            @Override
            public Set<Integer> keySet() {
                return null;
            }

            @Override
            public Collection<Integer> values() {
                return null;
            }

            @Override
            public Set<Entry<Integer, Integer>> entrySet() {
                return null;
            }
        };
        System.out.println("Super class: " + map.getClass().getGenericSuperclass());
        System.out.println("Name: " + map.getClass().getName());
        System.out.println("Type Name: " + Arrays.deepToString(map.getClass().getGenericInterfaces()));
        System.out.println("------------------------------------");
    }
}