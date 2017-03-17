package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
    TextView form_no_tv, staff_name_tv, total_galoons_tv, view_tv, status_tv,tv_tot;

    Typeface tf;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    LinearLayout ll, llt;

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
        // view_tv = (TextView) convertView.findViewById(R.id.tv_endgug);
        status_tv = (TextView) convertView.findViewById(R.id.tv_endgug2);

        tv_tot = (TextView) convertView.findViewById(R.id.tv_tot_veh);

        llt = (LinearLayout) convertView.findViewById(R.id.llayout);

        ll = (LinearLayout) convertView.findViewById(R.id.ll);

        form_no_tv.setTypeface(tf);
        status_tv.setTypeface(tf, 1);
        staff_name_tv.setTypeface(tf);
        total_galoons_tv.setTypeface(tf);
        tv_tot.setTypeface(tf);
//        view_tv.setTypeface(tf);

        form_no_tv.setText(resultp.get(StaffViewEntry.FCI_FORM));
        staff_name_tv.setText(resultp.get(StaffViewEntry.STAFF_NAME));
        total_galoons_tv.setText(resultp.get(StaffViewEntry.TOTAL_GALOONS));
        status_tv.setText(resultp.get(StaffViewEntry.ENTRY_STATUS));

        tv_tot.setText(resultp.get("total_vehicles"));


        String status = status_tv.getText().toString();
        if (status.equals("APPROVED")) {
            llt.setBackgroundResource(R.drawable.aprov_dr);
            status_tv.setEnabled(false);

        } else if (status.equals("PENDING")) {
            llt.setBackgroundResource(R.drawable.pending_dra);
            status_tv.setEnabled(true);

        } else {
            llt.setBackgroundColor(Color.parseColor("#EB1543"));

            llt.setBackgroundResource(R.drawable.reject_dr);
            status_tv.setEnabled(false);

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
                cdd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                cdd.show();

                WindowManager.LayoutParams lp = cdd.getWindow().getAttributes();
                lp.dimAmount = 1.90f;
                cdd.getWindow().setAttributes(lp);
                cdd.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


            }
        });
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                String form = resultp.get(StaffViewEntry.FCI_FORM);
                String cr_date = resultp.get(StaffViewEntry.CREATE_DATE);
                String rv_date = resultp.get(StaffViewEntry.REVIEW_DATE);
                String mgr = resultp.get(StaffViewEntry.MANAGER_NAME);
                String comp = resultp.get(StaffViewEntry.COMPANY_NAME);
                String assis = resultp.get("assist");
                Log.e("tag","asdr :"+assis);
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("viewformId", form);
                edit.putString("cr_date", cr_date);
                edit.putString("rv_date", rv_date);
                edit.putString("mgr", mgr);
                edit.putString("comp", comp);
                edit.putString("assist", assis);

                edit.commit();
                Intent intent = new Intent(context, ViewFormEntry.class);
                context.startActivity(intent);

            }
        });


        return convertView;
    }
}
