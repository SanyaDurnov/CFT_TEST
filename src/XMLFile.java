import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

class XMLFile implements CFTXml{

    private File path;
    private ArrayList<Data> fieldList = new ArrayList<>();
    private MyHandler myHandler = new MyHandler();
    private String fullStringData;


    void XMLParser() throws ParserConfigurationException, SAXException, IOException {

        File xmlFile = new File(String.valueOf(path));
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        InputStream inputStream= new FileInputStream(xmlFile);
        InputStreamReader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        InputSource inputSource = new InputSource(inputReader);
       // inputSource.setEncoding("UTF-8");
        try {
            saxParser.parse(inputSource, myHandler);
            inputReader.close();
            inputStream.close();
        }catch (SAXException e){
            System.out.println("Error in structure of xml file - " + xmlFile);
        }
    }


    static void Print(String fullData){
        System.out.println(fullData);
    }


    String getFullStringData() {
        String fullData;
        fullData = "<" + XML_FIRSTELEMNT + ">" + "\n";

        for(Data fields : fieldList)
            fullData = fullData + fields.toString() + "\n";

        fullData = fullData + "</" + XML_FIRSTELEMNT + ">";
        return fullData;
    }


    private class MyHandler extends DefaultHandler {

        String lastElementName;
        private Boolean inOurElement = false;
        private TreeMap<String, String> fieldData = new TreeMap<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals(FIELD_TAG)) {
                inOurElement = !inOurElement;
            }
            lastElementName = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            fullStringData = new String(ch, start, length);
            String infData = new String(ch, start, length);
            infData = infData.replace("\n", "").trim();
            if (!infData.isEmpty()) {
                if (inOurElement) {
                    infData = myHandler.isBoolean(infData);
                    fieldData.put(lastElementName, infData);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals(FIELD_TAG)) {
                if (!fieldData.isEmpty()) {
                    fieldList.add(new Data(fieldData, fieldData.get(TYPE_TAG)));
                    inOurElement = !inOurElement;
                    fieldData.clear();
                }
            }
        }


        private String isBoolean(String infData){
            for (String BOOLEAN_ATRIBUTE : BOOLEAN_ATRIBUTES) {
                if (this.lastElementName.equals(BOOLEAN_ATRIBUTE)) {
                    infData = infData.equals("0") ? "false" : "true";
                }
            }
            return infData;
        }
    }


        void XMLGenerate(String newFile) throws FileNotFoundException, XMLStreamException {

        try {
                OutputStream outputStream = new FileOutputStream(newFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                XMLOutputFactory factory = XMLOutputFactory.newFactory();
                XMLStreamWriter writer = factory.createXMLStreamWriter(outputStreamWriter);
                Charset.forName(XML_ENCODING).newEncoder();
                writer.writeStartDocument(XML_ENCODING, XML_VERSION);
                writer.writeCharacters("\n");
                writer.writeStartElement(XML_FIRSTELEMNT);
                writer.writeCharacters("\n");


            for (int i = 0; i < fieldList.size(); i++ ) {
                Data field = fieldList.get(i);
                writer.writeStartElement(field.fieldData.get(TYPE_TAG));
                for(Map.Entry<String, String> item : field.fieldData.entrySet()){
                    writer.writeAttribute(item.getKey(), item.getValue());
                }
                writer.writeEndElement();
                writer.writeCharacters("\n");
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();

        }catch (FileNotFoundException e){
            System.out.println("Folder for saving xml file's not found");
            throw e;
        }
        }


    XMLFile(File path) {
        this.path = path;

    }

}



