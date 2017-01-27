package com.example.hp.iot;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sumanth-Achutha-Uday on 21/01/2017.
 */

public class HttpGetData {
    private static final String TAG = "MainActivity";
    public static byte[] GetText(String link) throws IOException{
        URL url=new URL(link);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage()+": with "+link);
            }
            int bytesRead=0;
            byte[] buffer=new byte[1024];
            while((bytesRead=in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String link) throws IOException
    {
        return  new String(GetText(link));
    }
    public String fetchItems() {
        String csvString="";
        try {
           // String url = Uri.parse("http://data.sparkfun.com/output/AJE3QqanMwfYV20dJdOj.csv")
            String url = Uri.parse("http://iotelectricity.000webhostapp.com/disp.php")
                    .buildUpon()
                    .build().toString();
            csvString = getUrlString(url);
            Log.i(TAG, "Recieved csv: " + csvString);
        }catch(IOException e){
            Log.e(TAG,"Error: ",e);
        }
        return csvString;
    }
}
