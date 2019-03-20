public class CircularOne {
    private PrimitivesOnly po;
    private ReferenceToOthers rto;
    private CircularTwo c2;

    @Override
    public String toString() {
        return "CircularOne{" +
                "po=" + po +
                ", rto=" + rto +
                ", c2=" + c2 +
                '}';
    }
}
