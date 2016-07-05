package fci.com.fci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterViewEntry extends BaseAdapter {

    Context context;
    TextView form_no_tv, staff_name_tv, total_galoons_tv, view_tv, status_tv;

    Typeface tf;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public AdapterViewEntry(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    AdapterViewEntry(Context c1) {
        this.context = c1;
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
        convertView = inflat.inflate(R.layout.adapter_viewentry, null);
        resultp = data.get(position);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        form_no_tv = (TextView) convertView.findViewById(R.id.tv_vinno);
        staff_name_tv = (TextView) convertView.findViewById(R.id.tv_make);
        total_galoons_tv = (TextView) convertView.findViewById(R.id.tv_startgug);
        view_tv = (TextView) convertView.findViewById(R.id.tv_endgug);
        status_tv = (TextView) convertView.findViewById(R.id.tv_endgug2);
        form_no_tv.setTypeface(tf);
        status_tv.setTypeface(tf);
        staff_name_tv.setTypeface(tf);
        total_galoons_tv.setTypeface(tf);
        view_tv.setTypeface(tf);

        form_no_tv.setText(resultp.get(StaffViewEntry.FCI_FORM));
        staff_name_tv.setText(resultp.get(StaffViewEntry.STAFF_NAME));
        total_galoons_tv.setText(resultp.get(StaffViewEntry.TOTAL_GALOONS));
        status_tv.setText(resultp.get(StaffViewEntry.ENTRY_STATUS));
        String status = status_tv.getText().toString();
        if (status.equals("APPROVED")) {
            status_tv.setTextColor(Color.parseColor("#5EB50D"));

        } else if (status.equals("PENDING")) {
            status_tv.setTextColor(Color.parseColor("#F7AE7C"));

        } else {
            status_tv.setTextColor(Color.parseColor("#EB1543"));

        }


        status_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                String form = resultp.get(StaffViewEntry.FCI_FORM);
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("formId", form);
                edit.commit();
                DialogSignature cdd = new DialogSignature((Activity) context);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();


            }
        });
        view_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                String form = resultp.get(StaffViewEntry.FCI_FORM);
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("viewformId", form);

                edit.commit();
                Intent intent = new Intent(context, ViewFormEntry.class);
                context.startActivity(intent);

            }
        });


        return convertView;
    }
}
