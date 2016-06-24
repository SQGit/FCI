package fci.com.fci;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterViewEntry extends BaseAdapter {

    Context context;
    ArrayList<UserData> arrayList;
    TextView tv_vinno, tv_make, tv_startgug, tv_endgug,tv_endgug2;
    LinearLayout lt_bg;
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;


    AdapterViewEntry(Context c1) {
        this.context = c1;
       /* this.ar_name = name;
        this.ar_phone = phone;
        this.ar_pass = pass;*/
    }


    @Override
    public int getCount() {
        return 3;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_viewentry, null);

        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");

        tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
        tv_make = (TextView) convertView.findViewById(R.id.tv_make);
        tv_startgug = (TextView) convertView.findViewById(R.id.tv_startgug);
        tv_endgug = (TextView) convertView.findViewById(R.id.tv_endgug);
        tv_endgug2 = (TextView) convertView.findViewById(R.id.tv_endgug2);


        tv_vinno.setTypeface(tf);
        tv_make.setTypeface(tf);
        tv_startgug.setTypeface(tf);
        tv_endgug.setTypeface(tf);
        tv_endgug2.setTypeface(tf);





        return convertView;
    }
}
