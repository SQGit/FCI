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
public class StaffViewEntry extends Activity {

    static String FCI_FORM = "fci_entry_form_id";
    static String COMPANY_NAME = "company_name";
    static String MANAGER_NAME = "company_mgr";
    static String STAFF_NO = "use_phone";
    static String STAFF_NAME = "use_name";
    static String CREATE_DATE = "create_date";
    static String REVIEW_DATE = "review_date";
    static String TOTAL_GALOONS = "total_gallons";
    static String ENTRY_STATUS = "entry_status";
    static String MANAGER_SIGN = "mgr_sign_url";
    TextView tv_logout, tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_formno, tv_staffname, tv_tot_gal, tv_view, tv_status,tv_total_veh;
    Typeface tf;
    ListView lv_entries;
    AdapterViewEntry avw;
    ArrayList<HashMap<String, String>> contactList;
    String phone, staffname;
    String dateFrom;
    SweetAlertDialog sweetDialog;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewentry);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        phone = sharedPreferences.getString("phoneno", "");
        staffname = sharedPreferences.getString("staffname", "");
        contactList = new ArrayList<>();
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");

        //String currentDateandTime = datef.format(new Date(date));
        Calendar ca = Calendar.getInstance();


        Calendar ccc = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
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
        Log.e("tag", "hour" + hour);
        Log.e("tag", "hour" + hourofday);


        DateFormat date = new SimpleDateFormat("HH:MM aa");
        Calendar caa = date.getCalendar();
        int m = ca.get(Calendar.MONTH);
        int y = ca.get(Calendar.YEAR);
        int cHour = caa.get(Calendar.HOUR_OF_DAY);
        int cMinute = ca.get(Calendar.MINUTE);
        int cSecond = ca.get(Calendar.SECOND);
        int c = ca.get(Calendar.AM_PM);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dateFrom = format.format(ca.getTime());
            //   Log.e("tag", "<---from---->" + dateFrom + "<----to-->" + cHour + cMinute + "\t");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tv_staff = (TextView) findViewById(R.id.staff_id);
        tv_logout = (TextView) findViewById(R.id.logout_tv);
        tv_total_veh = (TextView) findViewById(R.id.tv_tot);

        tv_logout.setTypeface(tf);

        tv_date.setText(dateFrom);
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
        tv_status.setTypeface(tf);
        tv_staff.setTypeface(tf);
        tv_total_veh.setTypeface(tf);
        tv_staff.setText("Hi " + staffname);


        String shour = String.format("%02d", hour);
        String smin = String.format("%02d", cMinute);

        if (c == 0) tv_time.setText(shour + " : " + smin + " AM");
        else tv_time.setText(shour + " : " + smin + " PM");


        if (Util.Operations.isOnline(StaffViewEntry.this)) {
            new staffViewEntry_Task().execute();
        } else {
            new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .show();
        }


        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.WARNING_TYPE)
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
        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goStf = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(goStf);
                StaffViewEntry.this.finish();
            }
        });

    }


    class staffViewEntry_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {
                String virtual_url = Data_Service.SERVICE_URL_NEW + "fetchentry";
                JSONObject jsonobject = new JSONObject();
                jsonobject.accumulate("use_phone", phone);
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
            super.onPostExecute(jsonStr);
            sweetDialog.dismiss();
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                String count = jo.getString("count");
                if (status.equals("success")) {

                    if (Integer.valueOf(count) > 0) {


                        JSONArray staff_datas = jo.getJSONArray("entry");
                        Log.d("tag", "<-----staff_datas----->" + "" + staff_datas);
                        for (int i = 0; i < staff_datas.length(); i++) {

                            JSONObject datas = staff_datas.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("fci_entry_form_id", datas.getString("fci_entry_form_id"));
                            map.put("company_name", datas.getString("company_name"));
                            map.put("company_mgr", datas.getString("company_mgr"));
                            map.put("mgr_phone", datas.getString("mgr_phone"));
                            map.put("use_phone", datas.getString("use_phone"));
                            map.put("use_name", datas.getString("use_name"));
                            map.put("total_gallons", datas.getString("total_gallons"));
                            map.put("create_date", datas.getString("create_date"));
                            map.put("review_date", datas.getString("review_date"));
                            map.put("entry_status", datas.getString("entry_status"));
                            map.put("total_vehicles",datas.getString("total_vehicles"));
                            map.put("assist",datas.getString("assist"));
                            contactList.add(map);

                        }


                        avw = new AdapterViewEntry(StaffViewEntry.this, contactList);
                        lv_entries.setAdapter(avw);
                    } else {

                        new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("No Forms Associated with this UserName")
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


                } else {

                    new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.WARNING_TYPE)
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


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                Log.e("tag_", "" + e.toString());

                new SweetAlertDialog(StaffViewEntry.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                                startActivity(i);
                                StaffViewEntry.this.finish();
                            }
                        })
                        .show();

            }

        }

    }

}
