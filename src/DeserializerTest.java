import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
        Document newDoc = s.serialize(pa);
        ArrayList<Document> doc = new ArrayList<Document>();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(xmlOutputter.outputString(newDoc));
        doc.add(newDoc);
        Deserializer.recreateObject(doc);
    }
}