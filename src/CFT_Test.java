import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class CFT_Test {
    private static ArrayList<XMLFile> xmlFiles = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        new ConfigReader();
        String pathIn = ConfigReader.catalogPathIn;
        String pathOut = ConfigReader.catalogPathOut;
        String addToNewFileName = ConfigReader.addToNewFileName;
        String pathToNewFile = pathOut + System.getProperty("file.separator") + addToNewFileName;
        String url = ConfigReader.url;
        URLPost cftPost = new URLPost();

        try {
            Catalog catalog = new Catalog(pathIn);
            if (!catalog.listOfXmlFiles().isEmpty()) {
                catalog.print(catalog.listOfXmlFiles());
                for (int i = 0; i < catalog.listOfXmlFiles().size(); i++) {
                    File currentFile = catalog.listOfXmlFiles().get(i);
                    xmlFiles.add(new XMLFile(currentFile));
                    xmlFiles.get(i).XMLParser();
                    xmlFiles.get(i).XMLGenerate(pathToNewFile + currentFile.getName());
                    cftPost.sendPost(url, xmlFiles.get(i).getFullStringData());
                    System.out.println("\nSend HTTP request");
                }
            }
            else
                System.out.println("Xml file's not found");

            }catch(NullPointerException e){
                System.out.println("Folder path not found");
                e.printStackTrace();
            }
    }


    public static class ConfigReader {

        private final String PROPERTIES_FILE_NAME = "config.properties";
        private final String DIR_SEPARATOR = System.getProperty("file.separator");
        private final File currentDir = new File(".");
        static String catalogPathIn = null;
        static String catalogPathOut = null;
        static String addToNewFileName = "_";
        static String url = null;

        ConfigReader() {
            try {
                String sFilePath = currentDir.getCanonicalPath() + DIR_SEPARATOR + PROPERTIES_FILE_NAME;
                FileInputStream fileInputStream = new FileInputStream(sFilePath);
                Properties conf = new Properties();
                conf.load(fileInputStream);
                catalogPathIn = conf.getProperty("catalogPathIn");
                catalogPathOut = conf.getProperty("catalogPathOut");
                addToNewFileName = conf.getProperty("addToNewXmlFileName");
                url = conf.getProperty("url");

            } catch (FileNotFoundException e) {
                System.out.println("Configuration file not found");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                System.out.println("Configuration parameter not found");
                e.printStackTrace();
            }
        }
    }
}

