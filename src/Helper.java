import java.io.File;
import java.io.FilenameFilter;

public class Helper {
    public static File[] getClasses(){

        File currentDir = new File("./out/production/A3/");
        return currentDir.listFiles(new FilenameFilter() {
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
    }
}
