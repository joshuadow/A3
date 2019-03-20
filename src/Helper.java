
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;

public class Helper {
    private static String myDir = "./out/production/A3/";
    private static String fileInMyDir = "./out/production/A3/Helper.class";
    public static ArrayList<File> getClasses(){

        File currentDir = new File(myDir);
        File[] thing = currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0) {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');
                    // get extension
                    String str = name.substring(lastIndex);
                    // match path name extension
                    if(str.equals(".class")) {
                        return true;
                    }
                }
                return false;
            }

        });
        ArrayList<File> toReturn = new ArrayList<>();
        for(File f : thing) {
            toReturn.add(f);
        }
        return toReturn;
    }

    public static String getMyDir(){
        return myDir;
    }

    public static String getFileInMyDir(){
        return fileInMyDir;
    }

    public static String getArr(String arrClassName) throws ClassNotFoundException {
        String type = arrClassName.replace("[", "");
        switch(type){
            case "B":
                return "java.lang.Byte";
            case "C":
                return "java.lang.Character";
            case "D":
                return "java.lang.Double";
            case "F":
                return "java.lang.Float";
            case "I":
                return "int";
            case "J":
                return "java.lang.Long";
            case "S":
                return "java.lang.Short";
            case "Z":
                return "java.lang.Boolean";
            case "java.lang.String":
                return "java.lang.String";
            default:
                return type.replace("L" ,"");
        }
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static Class<?> guessComponents(List<?> copy) {
        Class<?> guess = null;
        for (Object o : copy)
        {
            if (o != null)
            {
                if (guess == null)
                {
                    guess = o.getClass();
                }
                else if (guess != o.getClass())
                {
                    guess = lowestCommonSuper(guess, o.getClass());
                }
            }
        }
        return guess;
    }

    private static Class<?> lowestCommonSuper(Class<?> a, Class<?> b) {
        Set<Class<?>> aSupers = getsupers(a);
        while (!aSupers.contains(b))
        {
            b = b.getSuperclass();
        }
        return b;
    }

    private static Set<Class<?>> getsupers(Class<?> c){
        if (c == null) return new HashSet<Class<?>>();

        Set<Class<?>> s = getsupers(c.getSuperclass());
        s.add(c);
        return s;
    }
}
