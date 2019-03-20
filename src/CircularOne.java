public class CircularOne {
    private PrimitivesOnly po;
    private ReferenceToOthers rto;
    private CircularTwo c2;

    @Override
    public String toString() {
        return "CircularOne: \n" +
                "\tpo=" + po +
                "\n\trto=" + rto +
                "\n\tc2=" + c2;
    }
}
