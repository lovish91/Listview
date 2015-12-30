package com.example.lovish_thinkpad.listview;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

/**
 * Created by Lovish-Thinkpad on 08-10-2015.
 */
public class JsonParser {

    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(25000);
            con.setDoInput(true);
            con.connect();
            InputStream is = con.getInputStream();
            reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            String data = null;
            String webPage = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            return webPage;

        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            //e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("fail 2", e.toString());
                    //e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
