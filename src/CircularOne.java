public class CircularOne {
    private PrimitivesOnly po;
    private ReferenceToOthers rto;
    private CircularTwo c2;

    public CircularOne(){
        po = new PrimitivesOnly();
        rto = new ReferenceToOthers();
        c2 = new CircularTwo();
    }
    @Override
    public String toString() {
        return "\nCircularOne: " +
                "\n\tpo=" + po +
                "\n\trto=" + rto +
                "\n\tc2=" + c2;
    }
}
