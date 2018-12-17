import java.util.*;


public class Data implements CFTXml {
    TreeMap<String, String> fieldData;

    Data(TreeMap<String, String> fieldData, String type) {

        this.fieldData = ( TreeMap<String, String>) fieldData.clone();
        switch (type){
            case TYPE_ACCOUNT:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_ACCOUNT);
               break;
            case TYPE_FIO:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_FIO);
                break;
            case TYPE_ADDRESS:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_ADDRESS);
                break;
            case TYPE_COUNTER:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_COUNTER);
                break;
            case TYPE_SUM:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_SUM);
                break;
            default:
                this.fieldData = sortByAccountType(fieldData, ATRIBUTES_TYPE_UNKNOWN);
                 break;
        }
    }


    @Override
    public String toString() {
        StringBuilder printText = new StringBuilder();
        if (fieldData != null) {
            for (Map.Entry<String, String> item : fieldData.entrySet()) {
                if (Objects.equals(item.getKey(), TYPE_TAG)){
                    printText.append("<").append(item.getValue());
                }
                else
                printText.append(" ").append(item.getKey()).append("=").append("\"").append(item.getValue()).append("\"");
            }
        }
        printText.append("/>");
        return printText.toString();
    }

    private TreeMap<String, String> sortByAccountType(TreeMap<String, String> startData, String[] accountTypeAtributes) {
        TreeMap<String, String> sortedData = new TreeMap<>( new fieldTypeComporator(accountTypeAtributes));
        if (startData != null) {
            for (Map.Entry<String, String> item : startData.entrySet()) {
                if (startData.get(TYPE_TAG).equals(TYPE_ADDRESS) && item.getKey().equals(VALUE_TAG)) {
                  String address = item.getValue();
                  String [] addressArray = address.split(ADDRESS_SPLITER);
                  for (int i = 0; i < ADDRESS_CONTAINS.length; i++) {
                     if(!addressArray[i].isEmpty())
                     sortedData.put(ADDRESS_CONTAINS[i],addressArray[i]);
                     else
                         sortedData.put(ADDRESS_CONTAINS[i],null);
                  }
                  sortedData.remove(VALUE_TAG);
                }
                else
                sortedData.put(item.getKey(), item.getValue());
            }
        }
        return sortedData;
    }


    static class fieldTypeComporator implements Comparator<String> {
        String [] accountTypeAtributes;
        fieldTypeComporator(String [] accountTypeAtributes){
            this.accountTypeAtributes = accountTypeAtributes;
        }

        @Override
        public int compare(String o1, String o2) {
            int a = 0;
            int b = 0;
            for (int i = 0; i < this.accountTypeAtributes.length; i++) {
                if (o1.equals(this.accountTypeAtributes[i])) {
                    a = i;
                    break;
                } else a = this.accountTypeAtributes.length;
            }
            for (int i = 0; i < this.accountTypeAtributes.length; i++) {
                if (o2.equals(this.accountTypeAtributes[i])) {
                    b = i;
                    break;
                } else
                    b = this.accountTypeAtributes.length;
            }
            return a - b;
        }
    }
}

