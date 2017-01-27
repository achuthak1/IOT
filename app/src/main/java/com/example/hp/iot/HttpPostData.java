package com.example.hp.iot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by HP on 21/01/2017.
 */

public class HttpPostData {
    public static String  postData(String status, String link)
    {
        // Create data variable for sent values to server

        String text = "";
        BufferedReader reader=null;
        // Send data
        try
        {
            // Defined URL  where to send data
          //  URL url = new URL(link+"&led="+status+"&motion_value=2");
            URL url = new URL(link+"led="+status);
            // Send POST data request
            URLConnection conn = url.openConnection();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
        }
        catch(Exception ex)
        {
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch(Exception ex) {}
        }
        // Show response on activity
        return text ;
    }
}
