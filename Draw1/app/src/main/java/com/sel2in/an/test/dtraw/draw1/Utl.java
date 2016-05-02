package com.sel2in.an.test.dtraw.draw1;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by t on 24-04-2016.
 */
public class Utl {

    public static void main(String []args){
        System.out.println(getSmallHtml("http://sel2in.com/up6.php"));
    }

    /**
     * All ASCII small page read in a chunk to string
     * */
    public static String getSmallHtml(String urls){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urls);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setDoOutput(true);
            //urlConnection.setChunkedStreamingMode(0);

            //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
            StringBuilder sb = new StringBuilder();
            while(in != null && in.available() > 0){
                sb.append((char)in.read());
            }
            return sb.toString();
        }catch(Throwable e) {
            Log.w("Utl read small page", "err " + e, e);
            //System.out.println(e);
            //e.printStackTrace();
        }finally {
                urlConnection.disconnect();
        }
        return null;

    }
}
