import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeserializerTest {

    @Test
    void recurseRebuild() throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        PrimitivesOnly po = new PrimitivesOnly();
        ReferenceToOthers ro = new ReferenceToOthers();
        PrimitiveArray pa = new PrimitiveArray();
        Serializer s = new Serializer();
        CollectionOfObjects coo = new CollectionOfObjects();
        CircularOne cooo = new CircularOne();
        Document newDoc = s.serialize(cooo);
        ArrayList<Document> doc = new ArrayList<Document>();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOutputter.outputString(newDoc));
        doc.add(newDoc);
        Deserializer deser = new Deserializer();
        ArrayList<Object> remadeObj = deser.recreateObject(doc);

        System.out.println("Object properties:\n");
        for(Object ohj : remadeObj){
            try {
                System.out.println("Object: " + ohj.getClass().getName() + "\n");
                for (Field field : ohj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if(!field.getType().isArray())
                        System.out.println(field.getType() + " " + field.getName() + " : " + field.get(ohj) + "\n");
                    else {
                        String name = field.getType().getName();
                        if(name.equals("[I"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((int[])field.get(ohj))+ "\n");
                        else if(name.equals("[D"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((double[])field.get(ohj))+ "\n");
                        else if(name.equals("[S"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((short[])field.get(ohj))+ "\n");
                        else if(name.equals("[J"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((long[])field.get(ohj))+ "\n");
                        else if(name.equals("[B"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((byte[])field.get(ohj))+ "\n");
                        else if(name.equals("[C"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((char[])field.get(ohj))+ "\n");
                        else if(name.equals("[F"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((float[])field.get(ohj))+ "\n");
                        else if(name.equals("[Z"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((boolean[])field.get(ohj))+ "\n");
                        else if(name.equals("[[I"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((int[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[D"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((double[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[S"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((short[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[J"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((long[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[B"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((byte[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[C"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((char[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[F"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((float[][])field.get(ohj))+ "\n");
                        else if(name.equals("[[Z"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((boolean[][])field.get(ohj))+ "\n");
                        else if(name.equals("[Ljava.lang.String;"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.toString((String[])field.get(ohj))+ "\n");
                        else if(name.equals("[[Ljava.lang.String;"))
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((String[][])field.get(ohj))+ "\n");
                        else
                            System.out.println(field.getType() + " " + field.getName() + " : " + Arrays.deepToString((Object[])field.get(ohj)) + "\n");

                    }
                }
            } catch (IndexOutOfBoundsException e) {
                ObjectCreatorController.throwIndexError();
            }
        }
    }
}