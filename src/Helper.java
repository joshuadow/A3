
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;

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
}
