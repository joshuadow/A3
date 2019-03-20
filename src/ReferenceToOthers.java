public class ReferenceToOthers {
    private PrimitivesOnly p;

    public ReferenceToOthers(){
    }

    @Override
    public String toString() {
        return "\nReferenceToOthers: " +
                "\n\tp=" + p;
    }
}
