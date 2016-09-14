package net.fciapp.fciscanner;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salman on 8/11/2016.
 */

public class Spinner_Adapter extends ArrayAdapter<String> {


    Context context;
    ArrayList<String> datas;
    int resource;
    Typeface tf;

    public Spinner_Adapter(Context context, int resource, ArrayList<String> data) {
        super(context, resource, data);
        this.context = context;
        this.datas = data;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  convertView = inflater.inflate(resource, parent, false);

        // tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        View view = super.getView(position, convertView, parent);

        TextView label = (TextView) view.findViewById(R.id.text1);
        label.setTypeface(tf);
        label.setText(datas.get(position));
        // label.setTextColor(context.getResources().getColor(R.color.textColorDark));
        //convertView.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // convertView = inflater.inflate(resource, parent, false);
        // tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        View view = super.getView(position, convertView, parent);

        TextView label = (TextView) view.findViewById(R.id.text1);
        label.setTypeface(tf);
        label.setText(datas.get(position));
        // label.setTextColor(context.getResources().getColor(R.color.textColorDark));
        // view.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        return view;
    }
}
