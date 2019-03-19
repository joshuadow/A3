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
}