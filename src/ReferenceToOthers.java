public class ReferenceToOthers {
    private PrimitivesOnly p;

    public ReferenceToOthers(){
        p = new PrimitivesOnly();
    }

    @Override
    public String toString() {
        return "\nReferenceToOthers: " +
                "\n\tp=" + p;
    }
}
