import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {
    @Override
    public boolean accept(File path) {
        return path.isFile() && path.getName().endsWith(".xml");
    }
}
