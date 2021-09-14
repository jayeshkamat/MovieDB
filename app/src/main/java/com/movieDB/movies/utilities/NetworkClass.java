package com.movieDB.movies.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkClass {

    private int responseCode;

    public String makeServiceCall(String url) {
        Log.v(" test "," url being received "+url);
        HttpURLConnection connection = null;
        String response = null;
        InputStream is = null;

        try {
            URL imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
            is = connection.getInputStream();
            response = readInputStream(is);
            Log.v(" test "," response in network class "+response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    connection.disconnect();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private String readInputStream(InputStream iStream) throws Exception {
        String singleLine = "";
        StringBuilder totalLines = new StringBuilder(iStream.available());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(iStream));

            while ((singleLine = reader.readLine()) != null) {
                totalLines.append(singleLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalLines.toString();
    }

}