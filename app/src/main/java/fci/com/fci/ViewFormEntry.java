package fci.com.fci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/22/2016.
 */
public class ViewFormEntry extends Activity {

    TextView tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_formno, tv_staffname, tv_tot_gal, tv_view;
    Typeface tf;
    ListView lv_entries;
    AdapterViewFormEntry avw;
    ArrayList<HashMap<String, String>> contactList;
    String formId, staffname;
    static String FCI_FORM = "fci_entry_form_id";
    static String VIN_NO = "vin_no";
    static String MAKE_MODEL = "make_model";
    static String START_GUAGE = "start_gauge";
    static String END_GAUGE = "end_gauge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewformentry);
        contactList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        formId = sharedPreferences.getString("viewformId", "");
        staffname = sharedPreferences.getString("staffname", "");
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
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
        tv_formno = (TextView) findViewById(R.id.tv_formno);
        tv_staffname = (TextView) findViewById(R.id.tv_staff);
        tv_tot_gal = (TextView) findViewById(R.id.tv_total);
        tv_view = (TextView) findViewById(R.id.tv_view);
        tv_header.setTypeface(tf, 1);
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
        tv_staff.setTypeface(tf);
        tv_staff.setText("Hi " + staffname);
        new staffViewEntry_Task().execute();

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goStf = new Intent(getApplicationContext(), StaffViewEntry.class);
                startActivity(goStf);
            }
        });

    }


    class staffViewEntry_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {

                String virtual_url = Data_Service.SERVICE_URL_NEW + "fetchentrydetail";
                JSONObject jsonobject = PostService.getStaffs(virtual_url);
                jsonobject.accumulate("entry_form_id", formId);
                json = jsonobject.toString();
                return jsonStr = PostService.makeRequest(virtual_url, json);

            } catch (Exception e) {
                Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                String count = jo.getString("count");


                if (status.equals("success")) {
                    JSONArray staff_datas = jo.getJSONArray("entrydetail");
                    Log.d("tag", "<-----staff_datas----->" + "" + staff_datas);

                    for (int i = 0; i < staff_datas.length(); i++) {
                        JSONObject datas = staff_datas.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("fci_entry_form_id", datas.getString("fci_entry_form_id"));
                        map.put("vin_no", datas.getString("vin_no"));
                        map.put("make_model", datas.getString("make_model"));
                        map.put("start_gauge", datas.getString("start_gauge"));
                        map.put("end_gauge", datas.getString("end_gauge"));
                        contactList.add(map);
                    }
                    avw = new AdapterViewFormEntry(ViewFormEntry.this, contactList);
                    lv_entries.setAdapter(avw);

                } else {
                    new SweetAlertDialog(ViewFormEntry.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();

                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}
