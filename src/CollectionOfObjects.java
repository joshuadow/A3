import java.util.*;

public class CollectionOfObjects {
    private List<PrimitivesOnly> list = new ArrayList<>();
    private HashMap<Integer, PrimitivesOnly> hashMap = new HashMap();
    public CollectionOfObjects(){
        list.add(new PrimitivesOnly());
        hashMap.put(6, new PrimitivesOnly());
    }

    @Override
    public String toString() {
        return "CollectionOfObjects{" +
                "\n\tlist=" + list +
                "\n\thashMap=" + hashMap +
                '}';
    }
}
