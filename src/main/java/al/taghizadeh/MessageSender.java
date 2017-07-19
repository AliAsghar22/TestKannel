package al.taghizadeh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ali on 7/17/17.
 */
public class MessageSender {
    private static String smsBoxUrl = "localhost";
    private static String smsBoxPort = "13013";
    private static String dlrUrl = "http://localhost:8080/kannel/dlr?id=xxx&type=%d";
    private static String messge = "test ";
    private static CharsetUtil charsetUtil = new CharsetUtil();

    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    String getDlrUrl(String messageId){
        return dlrUrl.replace("xxx", messageId);
    }

    void send(String smsBoxUrl, String smsBoxPort, String receiver, String message, int priority, String dlrURL, String smscId, int simCount) throws UnsupportedEncodingException{
        StringBuilder param = new StringBuilder();
        StringBuilder response = new StringBuilder();
        param.append("http://").append(smsBoxUrl).append(":").append(smsBoxPort).append("/cgi-bin/sendsms?");
        param.append(URLEncoder.encode("username","UTF-8")).append("=").append(URLEncoder.encode("kannel","UTF-8"));
        param.append("&").append(URLEncoder.encode("password","UTF-8")).append("=").append(URLEncoder.encode("kannel123","UTF-8"));
//        param.append("&").append(URLEncoder.encode("from","UTF-8")).append("=").append(URLEncoder.encode("wavecom0","UTF-8"));
//        param.append("&").append(URLEncoder.encode("smsc","UTF-8")).append("=").append(URLEncoder.encode(smscId,"UTF-8"));
        param.append("&").append(URLEncoder.encode("to","UTF-8")).append("=").append(URLEncoder.encode(receiver,"UTF-8"));
        if(charsetUtil.getCharset(message)==CharsetUtil.GSM_CHARSET_UNICODE){
            param.append("&").append(URLEncoder.encode("coding","UTF-8")).append("=").append(URLEncoder.encode("2","UTF-8"));
            param.append("&").append(URLEncoder.encode("charset", "UTF-8")).append("=").append(URLEncoder.encode("UTF-8","UTF-8"));
        }
        param.append("&").append(URLEncoder.encode("text","UTF-8")).append("=").append(URLEncoder.encode(message,"UTF-8"));
        param.append("&").append(URLEncoder.encode("priority","UTF-8")).append("=").append(URLEncoder.encode(""+priority,"UTF-8"));
        param.append("&").append(URLEncoder.encode("dlr-mask","UTF-8")).append("=").append(URLEncoder.encode("31","UTF-8"));
        param.append("&").append(URLEncoder.encode("dlr-url","UTF-8")).append("=").append(URLEncoder.encode(dlrURL,"UTF-8"));
        try{
            URL url = new URL(param.toString());
            logger.info("Opening connection ");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            logger.info("Connection openned");
            BufferedReader input = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = input.readLine()) != null)
                response.append(inputLine);
            input.close();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        logger.info("response : "+ response.toString());
        Thread.currentThread();
        try {
            Thread.sleep(charsetUtil.getPartCount(message) * 10000/simCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        MessageSender sender = new MessageSender();
        sender.send(smsBoxUrl, smsBoxPort, "09146214039", messge, 0, sender.getDlrUrl("hello"), "polygator", 1);
    }

}
