package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/22/2016.
 */
public class ViewFormEntry extends Activity {

    static String FCI_FORM = "fci_entry_form_id";
    static String VIN_NO = "vin_no";
    static String MAKE_MODEL = "make_model";
    static String START_GUAGE = "start_gauge";
    static String END_GAUGE = "end_gauge";
    TextView tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_formno, tv_staffname, tv_tot_gal, tv_view, tv_logout;
    Typeface tf;
    ListView lv_entries;
    AdapterViewFormEntry avw;
    ArrayList<HashMap<String, String>> contactList;
    String formId, staffname, dateFrom;
    SweetAlertDialog sweetDialog;
    LinearLayout lt_logout;
    View views;

    String mgr_name, comp_name, cr_date, rv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewformentry);
        contactList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        formId = sharedPreferences.getString("viewformId", "");
        staffname = sharedPreferences.getString("staffname", "");

        mgr_name = sharedPreferences.getString("mgr", "");
        comp_name = sharedPreferences.getString("comp", "");
        cr_date = sharedPreferences.getString("cr_date", "");
        rv_date = sharedPreferences.getString("rv_date", "");



        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        lv_entries = (ListView) findViewById(R.id.listview);
        tv_staff = (TextView) findViewById(R.id.staff_id);
        tv_header = (TextView) findViewById(R.id.tv_header);
      /*  tv_comp_namtxt = (TextView) findViewById(R.id.tv_comp);
        tv_comp_name = (TextView) findViewById(R.id.tv_comp1);
        tv_manag_namtxt = (TextView) findViewById(R.id.tv_manager);
        tv_manag_name = (TextView) findViewById(R.id.tv_manager1);*/
        tv_datetxt = (TextView) findViewById(R.id.tv_date);
        tv_date = (TextView) findViewById(R.id.tv_date1);
        tv_timetxt = (TextView) findViewById(R.id.tv_time);
        tv_time = (TextView) findViewById(R.id.tv_time1);
        tv_formno = (TextView) findViewById(R.id.tv_formno);
        tv_staffname = (TextView) findViewById(R.id.tv_staff);
        tv_tot_gal = (TextView) findViewById(R.id.tv_total);
        tv_view = (TextView) findViewById(R.id.tv_view);

        tv_logout = (TextView) findViewById(R.id.tv_logout);

        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        views = findViewById(R.id.views);

        tv_header.setTypeface(tf, 1);
      /*  tv_comp_namtxt.setTypeface(tf);
        tv_comp_name.setTypeface(tf);
        tv_manag_namtxt.setTypeface(tf);
        tv_manag_name.setTypeface(tf);*/
        tv_datetxt.setTypeface(tf);
        tv_date.setTypeface(tf);
        tv_timetxt.setTypeface(tf);
        tv_time.setTypeface(tf);
        tv_formno.setTypeface(tf);
        tv_staffname.setTypeface(tf);
        tv_tot_gal.setTypeface(tf);
        tv_view.setTypeface(tf);
        tv_staff.setTypeface(tf);
        tv_logout.setTypeface(tf);
        tv_staff.setText("Hi " + staffname);
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
        //String currentDateandTime = datef.format(new Date(date));
        Calendar ca = Calendar.getInstance();
        DateFormat date = new SimpleDateFormat("HH:MM");
        int m = ca.get(Calendar.MONTH);
        int y = ca.get(Calendar.YEAR);
        int cHour = ca.get(Calendar.HOUR_OF_DAY);
        int cMinute = ca.get(Calendar.MINUTE);
        int cSecond = ca.get(Calendar.SECOND);
        int c = ca.get(Calendar.AM_PM);


        Calendar ccc = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:MM:ss aa");
        String datetime = dateformat.format(ccc.getTime());
        System.out.println(datetime);

        int sd = ccc.get(Calendar.HOUR);

        int millisecond = ccc.get(Calendar.MILLISECOND);
        int second = ccc.get(Calendar.SECOND);
        int minute = ccc.get(Calendar.MINUTE);
        //12 hour format
        int hour = ccc.get(Calendar.HOUR);
        //24 hour format
        int hourofday = ccc.get(Calendar.HOUR_OF_DAY);


        Log.e("tag", "time" + millisecond + "cc" + second + "cc" + minute + "cc" + hour % 12 + "cc" + hourofday);

        hour = hour % 12;
        if (hour == 0) {
            hour = 12;
        }


        String shour = String.format("%02d", hour);
        String smin = String.format("%02d", cMinute);

     /*   if (c == 0) tv_time.setText(shour + " : " + smin + " AM");
        else tv_time.setText(shour + " : " + smin + " PM");*/

        Log.e("tag", "date :" + mgr_name + cr_date + comp_name + rv_date);

        tv_date.setText(cr_date);

        if (rv_date.equals(null) || rv_date == "null" || rv_date == null) {
            tv_time.setVisibility(View.GONE);
            tv_timetxt.setVisibility(View.GONE);
            views.setVisibility(View.GONE);
        } else {
            tv_time.setText(rv_date);
        }


/*        if (c == 0) tv_time.setText(hour + " : " + cMinute + " AM");
        else tv_time.setText(hour + " : " + cMinute + " PM");*/

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dateFrom = format.format(ca.getTime());
            Log.e("tag", "<---from---->" + dateFrom + "<----to-->" + cHour + cMinute);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //  tv_date.setText(dateFrom);
        //tv_time.setText(cHour + ":" + cMinute);


        if (Util.Operations.isOnline(ViewFormEntry.this)) {
            new staffViewEntry_Task().execute();
        } else {
            new SweetAlertDialog(ViewFormEntry.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .show();
        }


        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent goStf = new Intent(getApplicationContext(), StaffViewEntry.class);
                startActivity(goStf);
                ViewFormEntry.this.finish();*/
                onBackPressed();
            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ViewFormEntry.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(getApplicationContext(), StaffLogin.class);
                                startActivity(intent);
                                finish();
                                sDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });


    }


    class staffViewEntry_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(ViewFormEntry.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
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
                sweetDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            sweetDialog.dismiss();
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

                new SweetAlertDialog(ViewFormEntry.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                                startActivity(i);
                                ViewFormEntry.this.finish();
                            }
                        })
                        .show();
            }

        }

    }


}
