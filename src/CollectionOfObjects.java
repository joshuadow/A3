import java.util.*;

public class CollectionOfObjects {
    private ArrayList<PrimitivesOnly> list = new ArrayList<>();
    private ArrayList<Integer> intList = new ArrayList<>();
    private HashMap<Integer, PrimitivesOnly> hashMap = new HashMap();
    public CollectionOfObjects(){
        PrimitivesOnly po = new PrimitivesOnly();
        intList.add(1);
        list.add(po);
        hashMap.put(6, po);
    }

    @Override
    public String toString() {
        return "CollectionOfObjects{" +
                "\n\tlist=" + list +
                "\n\tintList=" + intList +
                "\n\thashMap=" + hashMap +
                '}';
    }
}
