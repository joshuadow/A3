import java.lang.reflect.Array;
import java.util.Arrays;

public class PrimitiveArray {
    private int[] intArr = {1,2,3};
    private int[][] int2DArr = {{1,2},{3,4}};
    private PrimitivesOnly po = new PrimitivesOnly();
    private PrimitivesOnly po2 = new PrimitivesOnly();
    private PrimitivesOnly[] primOnlyArr = {po, po2};
    private PrimitivesOnly[] p2o = {new PrimitivesOnly(),new PrimitivesOnly()};

    public PrimitiveArray(){}
    @Override
    public String toString() {
        return "\nPrimitiveArray :" +
                "\n\tintArr=" + Arrays.toString(intArr) +
                "\n\tint2DArr=" + Arrays.deepToString(int2DArr) + '\'' +
                "\n\tprimOnlyArr=" + Arrays.deepToString(primOnlyArr) +
                "\n\tpo=" + po.toString() +
                "\n\tpo2=" + po2.toString() +
                "\n\tp2o=" + Arrays.deepToString(p2o);
    }
}
