package fci.com.fci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Salman on 6/22/2016.
 */
public class StaffAddEntry extends Activity{

    TextView tv_header,tv_comp_namtxt,tv_comp_name,tv_manag_namtxt,tv_manag_name,tv_datetxt,tv_date,tv_timetxt,tv_time,tv_vinno,tv_make,tv_startgug,tv_endgug,tv_save,tv_note,tv_add_another;
    Typeface tf;
    ListView lv_entries;
    AdapterAddEntry staff_adapter;
    public String asdfs;
    public HashMap<Integer,String> vin_make = new HashMap<>();
    public ArrayList<Integer> v_pos = new ArrayList<>();
    public ArrayList<String> v_mk = new ArrayList<>();
    TextView tv_addse;
    int siz_da;
    ImageView img_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_addentry);

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
        tv_vinno = (TextView) findViewById(R.id.tv_vinno);
        tv_make = (TextView) findViewById(R.id.tv_make);
        tv_startgug = (TextView) findViewById(R.id.tv_startgug);
        tv_endgug = (TextView) findViewById(R.id.tv_endgug);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_add_another = (TextView) findViewById(R.id.tv_add_another);
        img_save = (ImageView) findViewById(R.id.img_save);


        tv_header.setTypeface(tf,1);
        tv_comp_namtxt.setTypeface(tf);
        tv_comp_name.setTypeface(tf);
        tv_manag_namtxt.setTypeface(tf);
        tv_manag_name.setTypeface(tf);
        tv_datetxt.setTypeface(tf);
        tv_date.setTypeface(tf);
        tv_timetxt.setTypeface(tf);
        tv_time.setTypeface(tf);
        tv_vinno.setTypeface(tf);
        tv_make.setTypeface(tf);
        tv_startgug.setTypeface(tf);
        tv_endgug.setTypeface(tf);
        tv_save.setTypeface(tf);
        tv_note.setTypeface(tf);
        tv_add_another.setTypeface(tf);

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goStf = new Intent(getApplicationContext(),StaffDashboard.class);
                startActivity(goStf);
            }
        });

        siz_da =3;

        staff_adapter = new AdapterAddEntry(StaffAddEntry.this,vin_make,v_pos,v_mk,siz_da);

        lv_entries.setAdapter(staff_adapter);

        tv_add_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siz_da=siz_da+1;
                staff_adapter = new AdapterAddEntry(StaffAddEntry.this,vin_make,v_pos,v_mk,siz_da);

                lv_entries.setAdapter(staff_adapter);
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i =0;i<siz_da;i++){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("vv_vin"+i);
                    editor.remove("vv_make"+i);
                    editor.commit();
                }

                Intent goasdf = new Intent(getApplicationContext(),StaffDashboard.class);
                startActivity(goasdf);
            }
        });


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag","restart");
        Log.e("tag",""+v_pos.size());
        Log.e("tag",""+v_mk.size());
        staff_adapter = new AdapterAddEntry(StaffAddEntry.this,vin_make,v_pos,v_mk,siz_da);

        lv_entries.setAdapter(staff_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag","resume");
    }
}
