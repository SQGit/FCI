package net.fciapp.fciscanner;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterViewFormEntry extends BaseAdapter {

    Context context;
    TextView form_no_tv, staff_name_tv, total_galoons_tv, view_tv;

    Typeface tf;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public AdapterViewFormEntry(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }




    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_viewformentry, null);
        resultp = data.get(position);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        form_no_tv = (TextView) convertView.findViewById(R.id.tv_vinno);
        staff_name_tv = (TextView) convertView.findViewById(R.id.tv_make);
        total_galoons_tv = (TextView) convertView.findViewById(R.id.tv_startgug);
        view_tv = (TextView) convertView.findViewById(R.id.tv_endgug);

        form_no_tv.setTypeface(tf);
        staff_name_tv.setTypeface(tf);
        total_galoons_tv.setTypeface(tf);
        view_tv.setTypeface(tf);

        form_no_tv.setText(resultp.get(ViewFormEntry.VIN_NO));
        staff_name_tv.setText(resultp.get(ViewFormEntry.MAKE_MODEL));
        total_galoons_tv.setText(resultp.get(ViewFormEntry.START_GUAGE));
        view_tv.setText(resultp.get(ViewFormEntry.END_GAUGE));





        return convertView;
    }
}
