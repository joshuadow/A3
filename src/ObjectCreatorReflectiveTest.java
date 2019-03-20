import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

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
    public void test4DArr(){
        int[][][][] tensor = new int[5][4][3][2];
        int[] dimArr = {5,4,3,2};
        int currentDim = 0;
        int[] state = {0,0,0,0};
        recurseArr(tensor, dimArr, currentDim, state);

    }
    public void recurseArr(Object tensor, int[] dimArr, int currentDim,int[] state) {
        if(currentDim == dimArr.length-1){
            System.out.print("Enter an int");
            for(int j = 0; j < state.length; j++) {
               System.out.print(state[j] + ",");
            }

            int i = input.nextInt();
        }
        else{
            currentDim++;
            state[currentDim] = state[currentDim] + 1;
            recurseArr(tensor, dimArr, currentDim, state);
        }
    }
}