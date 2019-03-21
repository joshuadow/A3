import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    public void testPrim(){
        int i = 8;
        testSerialPrim(i);
    }

    public void testSerialPrim(Object o){
        Document document = new Document();
        XMLOutputter xOut = new XMLOutputter(Format.getPrettyFormat());
        Element root = new Element("serialized");
        document.addContent(root);
        String className = o.getClass().getName();
        Element obj = new Element("object");
        obj.setAttribute("class", className);
        obj.setAttribute("id", "0");
        root.addContent(obj);
        Object[] oArr = ObjectCreatorReflective.checkPrimitive(o.getClass());
        if(o.getClass().isPrimitive() || oArr[0].equals(true)){
            Object newO = ObjectCreatorReflective.getPrimitive(o.getClass());
            Element primValue = new Element("value");

        }
    }
}