package fci.com.fci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Salman on 8/6/2016.
 */
public class Staff_New extends Activity {

    TextView tv_logout, tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_vinno, tv_make, tv_startgug, tv_endgug, tv_save, tv_note, tv_add_another;
    Typeface tf;
    ListView lv_entries;
    AdapterAddEntry staff_adapter;
    public HashMap<Integer, String> vin_make = new HashMap<>();
    public HashMap<Integer, String> name = new HashMap<>();
    public ArrayList<Integer> v_pos = new ArrayList<>();
    public ArrayList<String> v_mk = new ArrayList<>();
    static int siz_da =3;
    DbHelper dbclass;
    Context context = this;
    String managername, managerphone, companyname, staffname, dateFrom, staffphone;

    public ArrayList<String> vin_positions = new ArrayList<>();
    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_addentry);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        Calendar ca = Calendar.getInstance();
        int m = ca.get(Calendar.MONTH);
        int y = ca.get(Calendar.YEAR);
        int cHour = ca.get(Calendar.HOUR);
        int cMinute = ca.get(Calendar.MINUTE);
        int cSecond = ca.get(Calendar.SECOND);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dateFrom = format.format(ca.getTime());
            Log.e("tag", "<---from---->" + dateFrom + "<----to-->" + dateFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());


        companyname = sharedPreferences.getString("companyname", "");
        managername = sharedPreferences.getString("managername", "");
        managerphone = sharedPreferences.getString("managerphone", "");
        staffname = sharedPreferences.getString("staffname", "");
        staffphone = sharedPreferences.getString("phoneno", "");

        dbclass = new DbHelper(context);
        tv_logout = (TextView) findViewById(R.id.logout_tv);
        lv_entries = (ListView) findViewById(R.id.listview);
        tv_staff = (TextView) findViewById(R.id.staff_id);
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
        tv_time.setText(cHour + ":" + cMinute);

        tv_header.setTypeface(tf, 1);
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
        tv_staff.setTypeface(tf);
        //  tv_add_another.setTypeface(tf);
        tv_logout.setTypeface(tf);
        tv_comp_name.setText(companyname);
        tv_manag_name.setText(managername);
        tv_staff.setText("Hi " + staffname);
        tv_date.setText(dateFrom);


        tv_add_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siz_da = siz_da + 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("size",String.valueOf(siz_da));
                editor.commit();

                staff_adapter.notifyDataSetChanged();

            }
        });


        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbclass.deleteDb();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("size",String.valueOf(3));
                editor.commit();



                Intent goStf = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(goStf);
                finish();
            }
        });






    }
}
