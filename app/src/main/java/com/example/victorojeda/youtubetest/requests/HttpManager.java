package com.example.victorojeda.youtubetest.requests;

import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by victorojeda on 12/8/14.
 */
public class HttpManager {

    public static String getDataHttpURL(String uri)
    {
        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection con = null;

            con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String getDataHttpURL(String uri, String username, String password)
    {
        BufferedReader reader = null;

        byte[] loginBytes = (username + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.addRequestProperty("Authorization", loginBuilder.toString());

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            Log.d("HTTPManager", "response: " + sb.toString());
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String getDataHttpURL(RequestPackage p)
    {
        BufferedReader reader = null;
        String uri = p.getUri();

        if ("GET".equals(p.getMethod())) {
            uri += "?" + p.getEncodedParams();
        }


        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());

            JSONObject json = new JSONObject(p.getParams());
            String params = "params=" + json.toString();

            if ("POST".equals(p.getMethod())) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params); //p.getEncodedParams()
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
