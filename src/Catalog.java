import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Catalog {

    private String path;


    Catalog(String path)
    {
        this.path = path;
    }


    protected ArrayList<File> listOfFiles(){
        File file = new File(this.path);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }


    ArrayList<File> listOfXmlFiles(){
        MyFileFilter filter = new MyFileFilter();
        File file = new File(this.path);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles(filter))));
    }


    void print(ArrayList<File> listOfFiles){
        for (File listOfFile : listOfFiles) System.out.println(listOfFile);
    }
}
