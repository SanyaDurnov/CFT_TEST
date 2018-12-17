import java.io.*;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.HttpsURLConnection;


class URLPost {


        void sendPost(String url,String data) throws Exception {
             try {
                  URL obj = new URL(url);
                  HttpsURLConnection con = ( HttpsURLConnection ) obj.openConnection();
                  con.setRequestMethod("POST");
                  con.setDoOutput(true);
                  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                  wr.writeBytes(data);
                  wr.flush();
                  wr.close();
                  int responseCode = con.getResponseCode();
                  System.out.println("\nSending 'POST' request to URL : " + url);
                  System.out.println("Post parameters : " + data);
                  System.out.println("Response Code : " + responseCode);
                  BufferedReader in = new BufferedReader(
                          new InputStreamReader(con.getInputStream()));
                  String inputLine;
                  AtomicReference<StringBuilder> response = new AtomicReference<>(new StringBuilder());
                  while ((inputLine = in.readLine()) != null) {
                       response.get().append(inputLine);
                  }
                  in.close();
                  //System.out.println(response.toString());
             }catch (Exception e){
                  System.out.println("Unable to connect to URL");
                  throw e;
             }
        }
}
