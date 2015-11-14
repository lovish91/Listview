package com.example.lovish_thinkpad.listview;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView list;
    Button getdata;
    TextView nme;
    ImageButton paus,play;
    ImageButton prev;
    ImageButton next;
    ImageButton splay;
    ImageView imgbg;
    ImageView smldp;
    MyAdapter adapter;
    int k;
    static int count = 0;
    boolean mBounded;
    Boolean ab=false;
    boolean flag = false;
    JSONArray android = null;
    String Surl;
    String ver;
    String name;
    String username;
    String imgurl;
    Intent mIntent;
    MyService myService;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //private static String url = "http://serviceapi.skholingua.com/open-feeds/list_multipletext_json.php";
    private static String url = "https://api-v2.soundcloud.com/explore/popular%20music?limit=50&offset=0&linked_partitioning=1&client_id=994971d344c2db865817b63014907a09";
    static final String IMG_URL = "artwork_url";
    static final String TAG_VER = "genre";
    static final String TAG_NAME = "title";
    static final String STREAM_URL = "stream_url";
    private static final String T_USER = "user";
    private static final String TRACK = "tracks";
    static final String U_NAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            list = (ListView) findViewById(R.id.list);
            getdata = (Button) findViewById(R.id.getdata);
            paus = (ImageButton)findViewById(R.id.paus);
            prev=(ImageButton)findViewById(R.id.prev);
            next=(ImageButton)findViewById(R.id.next);
            play=(ImageButton)findViewById(R.id.play);
            splay=(ImageButton)findViewById(R.id.btn_hide);
            imgbg=(ImageView)findViewById(R.id.bgdp);
            smldp=(ImageView)findViewById(R.id.small_dp);
            nme =(TextView)findViewById(R.id.name);
            getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });
        try {
             mIntent = new Intent(this, MyService.class);
            bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        splay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // check for already playing
                if (!ab) {
                    paus.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    splay.setBackgroundResource(R.drawable.ic_play_arrow_white_36dp);
                    myService.stopMedia();
                    ab = true;
                } else {
                    if (ab) {
                        paus.setImageResource(R.drawable.ic_pause_white_24dp);
                        splay.setBackgroundResource(R.drawable.ic_pause_white_24dp);
                        myService.playMedia();
                        ab = false;
                    }
                }

            }
        });
        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                //myService.playMedia();

            }
        });
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
                JSONObject  Json = new JSONObject(content);
                // Getting JSON Object from URL Content
                JSONArray json = Json.optJSONArray(TRACK);
                k = json.length();
                //JSONArray jsonArray = json.getJSONArray(content);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = json.getJSONObject(i);
                    // Storing JSON item in a Variable
                    name = c.getString(TAG_NAME);
                    ver = c.getString(TAG_VER);
                    Surl = c.getString(STREAM_URL);
                    imgurl = c.getString(IMG_URL);


                    JSONObject b = c.getJSONObject(T_USER);
                    username = b.getString(U_NAME);


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
                    String link = info.get(STREAM_URL);
                    String text = info.get(TAG_NAME);
                    String icon  = info.get(IMG_URL);
                    nme.setText(text);
                    Picasso.with(MainActivity.this)
                    .load(icon).into(smldp);
                    Picasso.with(MainActivity.this)
                            .load(icon.replaceFirst("large","t500x500")).resize(600, 600)
                            .centerCrop().into(imgbg);
                    ab=false;
                    splay.setBackgroundResource(R.drawable.ic_pause_white_24dp);
                    Toast.makeText(MainActivity.this, "You Clicked at "+ link,
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "You Clicked at " + position,
                            Toast.LENGTH_SHORT).show();
                    mIntent.putExtra("sntAudioLink", link);
                    try {
                        startService(mIntent);
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
    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            myService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MyService.LocalBinder mLocalBinder = (MyService.LocalBinder)service;
            myService = mLocalBinder.getService();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }
}

