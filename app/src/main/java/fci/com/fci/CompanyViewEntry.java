package fci.com.fci;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Salman on 6/22/2016.
 */
public class CompanyViewEntry extends Activity{
    TextView tv_header,tv_comp_namtxt,tv_comp_name,tv_manag_namtxt,tv_manag_name,tv_datetxt,tv_date,tv_timetxt,tv_time, tv_formno, tv_staffname, tv_tot_gal, tv_view,tv_status;
    Typeface tf;
    ListView lv_entries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewentry);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        lv_entries = (ListView) findViewById(R.id.listview);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_comp_namtxt = (TextView) findViewById(R.id.tv_comp);
        tv_comp_name = (TextView) findViewById(R.id.tv_comp1);
        tv_manag_namtxt = (TextView) findViewById(R.id.tv_manager);
        tv_manag_name = (TextView) findViewById(R.id.tv_manager1);
        tv_datetxt = (TextView) findViewById(R.id.tv_date);
        tv_date = (TextView) findViewById(R.id.tv_date1);
        tv_timetxt = (TextView) findViewById(R.id.tv_time);
        tv_time = (TextView) findViewById(R.id.tv_time1);
        tv_formno = (TextView) findViewById(R.id.tv_formno);
        tv_staffname = (TextView) findViewById(R.id.tv_staff);
        tv_tot_gal = (TextView) findViewById(R.id.tv_total);
        tv_view = (TextView) findViewById(R.id.tv_view);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_header.setTypeface(tf,1);
        tv_comp_namtxt.setTypeface(tf);
        tv_comp_name.setTypeface(tf);
        tv_manag_namtxt.setTypeface(tf);
        tv_manag_name.setTypeface(tf);
        tv_datetxt.setTypeface(tf);
        tv_date.setTypeface(tf);
        tv_timetxt.setTypeface(tf);
        tv_time.setTypeface(tf);
        tv_formno.setTypeface(tf);
        tv_staffname.setTypeface(tf);
        tv_tot_gal.setTypeface(tf);
        tv_view.setTypeface(tf);
        tv_status.setTypeface(tf);

        AdapterViewEntry staff_adapter = new AdapterViewEntry(CompanyViewEntry.this);
        lv_entries.setAdapter(staff_adapter);

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goStf = new Intent(getApplicationContext(),StaffDashboard.class);
                startActivity(goStf);
            }
        });



    }


}
