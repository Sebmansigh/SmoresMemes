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
import java.util.Map;

/**
 * Created by sebmansigh on 2/5/18.
 */

public final class MyHTTP {
    private MyHTTP() {
    }

    public static String Encode(String data) {
        return data.replace("!", "%21")
                .replace("#", "%23")
                .replace("$", "%24")
                .replace("&", "%26")
                .replace("'", "%27")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace("*", "%2A")
                .replace("+", "%2B")
                .replace(",", "%2C")
                .replace("/", "%2F")
                .replace(":", "%3A")
                .replace(";", "%3B")
                .replace("=", "%3D")
                .replace("?", "%3F")
                .replace("@", "%40")
                .replace("[", "%5B")
                .replace("]", "%5D");
    }

    /*!	#	$	&	'	(	)	*	+	,	/	:	;	=	?	@	[	]
    %21	%23	%24	%26	%27	%28	%29	%2A	%2B	%2C	%2F	%3A	%3B	%3D	%3F	%40	%5B	%5D
    */
    public static String POST(String task, Map<String, String> args) {
        URL url = null;
        try {
            url = new URL("http://cs316smoresmemes.online/" + task + ".php");
        } catch (MalformedURLException e) {
            return e.getClass().getName() + "1: " + e.getMessage() + "\n";
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            System.out.println("Connected.");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            for (Map.Entry<String, String> arg : args.entrySet()) {
                String qstr = Encode(arg.getKey()) + "=" + Encode(arg.getValue()) + "&";
                wr.write(qstr);
            }
            wr.flush();
            wr.close();

            System.out.println("Pushed args.");
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String content = "", line;
                while ((line = rd.readLine()) != null) {
                    content += line + "\n";
                }

                System.out.println("Acquired content.");

                return content;
            } catch (Exception e) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String error = "", line;
                while ((line = rd.readLine()) != null) {
                    error += line + "\n";
                }

                System.out.println("Acquired error");

                throw new PHPErrorException(error);
            }
        } catch (Exception e) {

            return e.getClass().getName() + "2: " + e.getMessage();
        }
    }
}

class PHPErrorException extends Exception {
    public PHPErrorException(String Message) {
        super(Message);
    }
};