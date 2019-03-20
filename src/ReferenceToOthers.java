public class ReferenceToOthers {
    private PrimitivesOnly p;

    public ReferenceToOthers(){
    }

    @Override
    public String toString() {
        return "ReferenceToOthers: \n" +
                "\tp=" + p;
    }
}
