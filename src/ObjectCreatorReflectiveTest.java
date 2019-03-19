import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCreatorReflectiveTest {

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
}