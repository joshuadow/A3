import java.util.Arrays;

public class PrimitiveArray {
    private int[] intArr = {1,2,3};
    private int[][][] threeDArr;
    private String aString = "josh";
    private char[] charArr = {'d','o','w'};

    @Override
    public String toString() {
        return "PrimitiveArray{" +
                "intArr=" + Arrays.toString(intArr) +
                ", aString='" + aString + '\'' +
                ", charArr=" + Arrays.toString(charArr) +
                '}';
    }
}
