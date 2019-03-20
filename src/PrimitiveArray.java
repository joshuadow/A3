import java.util.Arrays;

public class PrimitiveArray {
    private int[] intArr = {1,2,3};
    private int[][] int2DArr = {{1,2},{3,4}};
    PrimitivesOnly p0;
    private String[] strArr = {"test", "test2"};
    private String[][] str2DArr;
    private PrimitivesOnly[] primOnlyArr = {p0, p0};
    private String aString = "josh";
    private char[] charArr = {'d','o','w'};

    @Override
    public String toString() {
        return "PrimitiveArray :\n" +
                "\tintArr=" + Arrays.toString(intArr) +
                "\n\taString='" + aString + '\'' +
                "\n\tcharArr=" + Arrays.toString(charArr);
    }
}
