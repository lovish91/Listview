package com.example.lovish_thinkpad.listview;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static final String Base_url = "https://api-v2.soundcloud.com/";
    static final String Client_id ="&client_id=994971d344c2db865817b63014907a09";
    static final String ART_URL = "artwork_url";
    static final String AVATR_URL = "avatar_url";
    static final String TAG_NAME = "title";
    static final String STREAM_URL = "stream_url";
    static final String U_NAME = "username";
    private static final String T_USER = "user";
    static final String GENRE = "genre";
    private static final String TRACK = "tracks";
    static int x= 0;
    static String music_type="popular%20music";
    int k;
    ListView list;
    Button getdata;
    TextView nme;
    ImageButton paus;
    ImageButton splay;
    ImageView imgbg;
    ImageView smldp;
    MyAdapter adapter;
    LinearLayout bigbg;
    Toolbar myToolbar;

    boolean mBounded;
    Boolean ab = false;
    boolean flag = false;
    String Surl;
    String ver;
    String name;
    String username;
    static String imgurl;
    Intent mIntent;
    static String avatar;
    ProgressBar progressBar;
    Spinner spinner;
    MyService myService;
    FloatingActionButton fab;
    SlidingUpPanelLayout slider;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ConnectionDetector cd;
    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            myService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MyService.LocalBinder mLocalBinder = (MyService.LocalBinder) service;
            myService = mLocalBinder.getService();
        }
    };
    Boolean isInternetPresent = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        getdata = (Button) findViewById(R.id.getdata);
        //paus = (ImageButton) findViewById(R.id.paus);
        splay = (ImageButton) findViewById(R.id.btn_hide);
        imgbg = (ImageView) findViewById(R.id.bgdp);
        smldp = (ImageView) findViewById(R.id.small_dp);
        bigbg = (LinearLayout) findViewById(R.id.big_bg);
        fab =(FloatingActionButton)findViewById(R.id.fab);
        slider=(SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        progressBar = (ProgressBar)findViewById(R.id.prog);
        nme = (TextView) findViewById(R.id.name);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        addtospinner();
        //contection checking
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent){
            getdata.setVisibility(View.INVISIBLE);
            new JSONParse().execute();
            //x+=10;
        }else {
            getdata.setVisibility(View.INVISIBLE);

        }

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
//                    paus.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    splay.setBackgroundResource(R.drawable.ic_play_arrow_white_36dp);
                    myService.stopMedia();
                    ab = true;
                } else {
                    if (ab) {
//                        paus.setImageResource(R.drawable.ic_pause_white_24dp);
                        splay.setBackgroundResource(R.drawable.ic_pause_white_24dp);
                        myService.playMedia();
                        ab = false;
                    }
                }
            }
        });
        fab.setOnClickListener(onExpand());
        int color = 0xFF0033CC;
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }
    public void addtospinner(){
        ArrayList<String> list = new ArrayList<String>();


        ArrayAdapter<CharSequence> adp3= ArrayAdapter.createFromResource(this,
                R.array.str2, R.layout.spinner_item);

        adp3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adp3);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                String ss = spinner.getSelectedItem().toString();
                x=0;
                oslist.clear();
                music_type = ss;
                Toast.makeText(getBaseContext(), ss, Toast.LENGTH_SHORT).show();
                new JSONParse().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                // TODO Auto-generated method stub

            }
        });
    }
    private View.OnClickListener onExpand(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show sliding layout in bottom of screen (not expand it)
                //slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                //btnShow.setVisibility(View.GONE);
                x+=10;
                new JSONParse().execute();

            }
        };
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    private class JSONParse extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
           // pDialog.show();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... args) {
            String url = "https://api-v2.soundcloud.com/explore/"+music_type+"?limit=10&offset="+x+"&linked_partitioning=1&client_id=994971d344c2db865817b63014907a09";

            String content = JsonParser.getData(url);
            try {
                flag = true;
                JSONObject Json = new JSONObject(content);
                // Getting JSON Object from URL Content
                JSONArray json = Json.optJSONArray(TRACK);
                k = json.length();
                //JSONArray jsonArray = json.getJSONArray(content);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = json.getJSONObject(i);
                    // Storing JSON item in a Variable
                    name = c.getString(TAG_NAME);
                    ver = c.getString(GENRE);
                    Surl = c.getString(STREAM_URL);
                    imgurl = c.getString(ART_URL);


                    JSONObject b = c.getJSONObject(T_USER);
                    username = b.getString(U_NAME);
                    avatar = b.getString(AVATR_URL);



                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(GENRE, ver);
                    map.put(TAG_NAME, name);
                    map.put(STREAM_URL, Surl);
                    map.put(U_NAME, username);
                    map.put(ART_URL, imgurl);
                    map.put(AVATR_URL, avatar);
                    //if(count<jsonArray.length())
                    //{
                    oslist.add(map);
                    //}
                    //count++;

                }
            } catch (JSONException e) {
                Log.e("fail 4", e.toString());
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

            //pDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(
                    MainActivity.this, "length" + k, Toast.LENGTH_SHORT).show();
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    HashMap<String, String> info = oslist.get(position);
                    String link = info.get(STREAM_URL);
                    String text = info.get(TAG_NAME);
                    String icon = info.get(ART_URL);
                    nme.setText(text);
                   // Picasso.with(MainActivity.this).load(icon).into(smldp);

                    Picasso.with(MainActivity.this)
                            .load(icon.replaceFirst("large", "crop"))
                            .priority(Picasso.Priority.HIGH)
                            //.centerInside()
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    bigbg.setBackground(new BitmapDrawable(bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            });

                    ab = false;
                    splay.setBackgroundResource(R.drawable.ic_pause_white_24dp);
                    Toast.makeText(MainActivity.this, "You Clicked at " + icon,
                            Toast.LENGTH_SHORT).show();

                    Toast.makeText(MainActivity.this, "You Clicked at " + position,
                            Toast.LENGTH_SHORT).show();

                    mIntent.putExtra("sntAudioLink", link);
                    try

                    {
                        startService(mIntent);
                    } catch (
                            Exception e
                            )

                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                e.getClass().getName() + " " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

