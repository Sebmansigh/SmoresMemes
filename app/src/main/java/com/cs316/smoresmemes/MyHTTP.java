package com.cs316.smoresmemes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

final class MyHTTP {
    private MyHTTP() {
    }

    public static String Encode(String data) {

        return data
                .replace("%", "%25") //Encode % First.
                .replace("!", "%21")
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
                .replace("]", "%5D")
                .replace(new String(new char[]{(char) 10}), "%0A") //Encode LF
                .replace(new String(new char[]{(char) 13}), "%0D"); //Encode CR
    }


    /*!	#	$	&	'	(	)	*	+	,	/	:	;	=	?	@	[	]
    %21	%23	%24	%26	%27	%28	%29	%2A	%2B	%2C	%2F	%3A	%3B	%3D	%3F	%40	%5B	%5D
    */
    public static String POST(String task, Map<String, String> args) {
        URL url;
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

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            for (Map.Entry<String, String> arg : args.entrySet()) {
                String qStr = Encode(arg.getKey()) + "=" + Encode(arg.getValue()) + "&";
                wr.write(qStr);
            }
            wr.flush();
            wr.close();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }


                return content.toString();
            } catch (Exception e) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    error.append(line);
                    error.append("\n");
                }

                throw new PHPErrorException(error.toString());
            } finally {
                connection.disconnect();
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
}