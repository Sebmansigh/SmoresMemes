package com.cs316.smoresmemes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sebmansigh on 2/5/18.
 */

public final class MyHTTP {
    private MyHTTP() {
    }

    ;

    public static String POST(String data) {
        URL url = null;
        try {
            url = new URL("http://cs316smoresmemes.online/test.php");
        } catch (MalformedURLException e) {
            return e.getMessage();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write("path=" + data);
            wr.flush();
            wr.close();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }

            return content;
            /*
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();
            */
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
