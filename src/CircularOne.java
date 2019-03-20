public class CircularOne {
    private PrimitivesOnly po;
    private ReferenceToOthers rto;
    private CircularTwo c2;
    private PrimitiveArray[] paa;

    public CircularOne(){}
    @Override
    public String toString() {
        return "\nCircularOne: " +
                "\n\tpo=" + po +
                "\n\trto=" + rto +
                "\n\tc2=" + c2;
    }
}
