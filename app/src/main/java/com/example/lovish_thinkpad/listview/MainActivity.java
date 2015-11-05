package com.example.lovish_thinkpad.listview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView list;
    Button getdata;
    MyAdapter adapter;
    static int count = 0;
    static int k;
    boolean flag = false;
    JSONArray android = null;
    String Surl;
    String ver;
    String name;
    String username;
    String imgurl;
    Intent serviceIntent;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //private static String url = "http://serviceapi.skholingua.com/open-feeds/list_multipletext_json.php";
    private static String url = "https://api.soundcloud.com/tracks/?client_id=994971d344c2db865817b63014907a09";
    static final String IMG_URL = "avatar_url";
    static final String TAG_VER = "genre";
    static final String TAG_NAME = "title";
    static final String STREAM_URL = "stream_url";
    private static final String T_USER = "user";
    static final String U_NAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        getdata = (Button) findViewById(R.id.getdata);
        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });
        try {
            serviceIntent = new Intent(this, MyService.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class JSONParse extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... args) {

            String content = JsonParser.getData(url);
            try {
                flag = true;
                // Getting JSON Object from URL Content
                JSONArray json = new JSONArray(content);
                k = json.length();
                //JSONArray jsonArray = json.getJSONArray(content);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = json.getJSONObject(i);
                    // Storing JSON item in a Variable
                    name = c.getString(TAG_NAME);
                    ver = c.getString(TAG_VER);
                    Surl = c.getString(STREAM_URL);


                    JSONObject b = c.getJSONObject(T_USER);
                    username = b.getString(U_NAME);
                    imgurl = b.getString(IMG_URL);

                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);
                    map.put(STREAM_URL, Surl);
                    map.put(U_NAME, username);
                    map.put(IMG_URL, imgurl);
                    //if(count<jsonArray.length())
                    //{
                    oslist.add(map);
                    //}
                    //count++;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml

            // Pass the results into ListViewAdapter.java
            adapter = new MyAdapter(MainActivity.this, oslist);
            adapter.notifyDataSetChanged();
            // Set the adapter to the ListView
            list.setAdapter(adapter);
            // Close the progressdialog

            pDialog.dismiss();
            Toast.makeText(
                    MainActivity.this, "length" + k, Toast.LENGTH_SHORT).show();
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    HashMap<String, String> info = oslist.get(position);
                    String title = info.get(STREAM_URL);
                    Toast.makeText(
                            MainActivity.this,
                            "You Clicked at "
                                    + title,
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(
                            MainActivity.this,
                            "You Clicked at "
                                    + position,
                            Toast.LENGTH_SHORT).show();
                    serviceIntent.putExtra("sntAudioLink", title);
                    try {
                        startService(serviceIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                e.getClass().getName() + " " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

