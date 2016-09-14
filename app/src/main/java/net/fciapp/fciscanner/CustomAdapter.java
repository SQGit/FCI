package net.fciapp.fciscanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ramya on 16-02-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    List<String> data;
    private Context context;
    private int layout;


    public CustomAdapter(Context context, int layout , List<String> source) {
        super(context, layout, source);
        this.context = context;
        this.data = source;
        this.layout = layout;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);


        TextView suggestion = (TextView) view.findViewById(R.id.text1);
        //suggestion.setTextSize(30);


        return view;
    }
}