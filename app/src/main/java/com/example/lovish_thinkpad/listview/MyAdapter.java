package com.example.lovish_thinkpad.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lovish-Thinkpad on 31-10-2015.
 */
public class MyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;


    public MyAdapter(Context context, ArrayList<HashMap<String, String>> oslist) {
        this.context = context;
        this.data = oslist;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class Holder {
        TextView title;
        TextView genre;
        TextView username;
        ImageView img;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View iView = convertView;
        Holder holder;
        if (convertView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            iView = inflater.inflate(R.layout.list_child, null);
            holder = new Holder();
            holder.title = (TextView) iView.findViewById(R.id.name);
            holder.genre = (TextView) iView.findViewById(R.id.vers);
            holder.username = (TextView) iView.findViewById(R.id.api);
            holder.img = (ImageView) iView.findViewById(R.id.image);
            //holder.title.setText(data[position]);

            iView.setTag(holder);
        } else {
            holder = (Holder) iView.getTag();
        }
        HashMap<String, String> resultp = new HashMap<String, String>();
        resultp = data.get(position);
        holder.title.setText(resultp.get(MainActivity.TAG_NAME));
        holder.genre.setText(resultp.get(MainActivity.GENRE));
        holder.username.setText(resultp.get(MainActivity.U_NAME));

        if (MainActivity.ART_URL=="null"){
            Picasso.with(this.context)
                    .load(resultp.get(MainActivity.AVATR_URL))
                    .into(holder.img);
        }else {
            Picasso.with(this.context)
                    .load(resultp.get(MainActivity.ART_URL))
                    .into(holder.img);
        }
        return iView;
    }
}
