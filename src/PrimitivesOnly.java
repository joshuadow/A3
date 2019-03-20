public class PrimitivesOnly {
    private int anInt;
    private char aChar;
    private byte aByte;
    private short aShort;
    private long aLong;
    private float aFloat;
    private double aDouble;
    private boolean aBoolean;
    private String aString;

    public PrimitivesOnly(){
        aDouble = 20.19;
        anInt = 0;
        aChar = 'J';
        aByte = 4;
        aShort = 15;
        aLong = 100000000;
        aFloat = 999999999;
        aBoolean = true;
        aString = "josh";

    }

    public PrimitivesOnly(int i, char c, byte b, short s, long l, float f, double d, boolean bo){
        this.anInt = i;
        this.aChar = c;
        this.aByte = b;
        this.aShort = s;
        this.aLong = l;
        this.aFloat = f;
        this.aDouble = d;
        this.aBoolean = bo;
    }

    public int getAnInt() {
        return this.anInt;
    }
    public void setAnInt(int i){
        this.anInt = i;
    }

    public double getaDouble() {
        return this.aDouble;
    }
    public void setaDouble(double d){
        this.aDouble = d;
    }

    public char getaChar() {
        return this.aChar;
    }
    public void setaChar(char c){
        this.aChar = c;
    }

    public byte getaByte() {
        return this.aByte;
    }
    public void setaByte(byte b){
        this.aByte = b;
    }

    public float getaFloat() {
        return this.aFloat;
    }
    public void setaFloat(float f){
        this.aFloat = f;
    }

    public long getaLong() {
        return this.aLong;
    }
    public void setaLong(long l){
        this.aLong = l;
    }

    public short getaShort() {
        return this.aShort;
    }
    public void setaShort(short s){
        this.aShort = s;
    }

    public boolean getaBoolean(){
        return this.aBoolean;
    }
    public void setaBoolean(boolean b){
        this.aBoolean = b;
    }

    @Override
    public String toString() {
        return "PrimitivesOnly: \n" +
                "\tanInt=" + anInt +
                "\n\taChar=" + aChar +
                "\n\taByte=" + aByte +
                "\n\taShort=" + aShort +
                "\n\taLong=" + aLong +
                "\n\taFloat=" + aFloat +
                "\n\taDouble=" + aDouble +
                "\n\taBoolean=" + aBoolean +
                "\n\taString=" + aString;
    }
}
