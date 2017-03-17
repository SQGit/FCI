package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by SMK on 8/6/2016.
 */
public class __Staff extends Activity {

    public int count = 3;
    ListView lview;
    TextView tv_logout, tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_vinno, tv_make, tv_startgug, tv_endgug, tv_save, tv_note, tv_add_another, tv_gallon;
    DbHelper dbclass;
    Context context = this;
    ArrayList myList = new ArrayList();
    String[] ar_vin_no = new String[]{"Scan VinNo", "Scan VinNo", "Scan VinNo"};
    String[] ar_vin_make = new String[]{"", "", ""};
    int[] ar_start = new int[]{0, 0, 0};
    int[] ar_end = new int[]{0, 0, 0};
    SweetAlertDialog sweetDialog;

    ArrayList<String> vin_positions = new ArrayList<>();
    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();
    Typeface tf;
    String managername, managerphone, companyname, staffname, dateFrom, staffphone, total_gallon, alt_mgr, alt_phone,purchase_order,staffname2 ="Choose Assist";
    EditText et_gallon,et_purchase;
    LinearLayout lt_logout;

    __StaffAdapter adapter;
    ArrayList<StaffFetchList> baL;


    Spinner spin,spin2;

    ArrayList<String> asd = new ArrayList<>();
    String choosen;
    SharedPreferences.Editor editor;

    ArrayList<String> boardlist;
    public String URL = Data_Service.SERVICE_URL_NEW + "staff/fetch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staff);

        Log.e("tag_class", "oncreate");


        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        Calendar ca = Calendar.getInstance();
        int m = ca.get(Calendar.MONTH);
        int y = ca.get(Calendar.YEAR);
        int cHour = ca.get(Calendar.HOUR_OF_DAY);
        int c = ca.get(Calendar.AM_PM);
        int cMinute = ca.get(Calendar.MINUTE);
        int cSecond = ca.get(Calendar.SECOND);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


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

        if (Util.Operations.isOnline(__Staff.this)) {
            new staffFetch_Task().execute();
        } else {
            new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            __Staff.this.finish();
                        }
                    })
                    .show();


        }


        Log.e("tag", "time" + millisecond + "cc" + second + "cc" + minute + "cc" + hour % 12 + "cc" + hourofday);

        hour = hour % 12;
        if (hour == 0) {
            hour = 12;
        }


        try {
            dateFrom = format.format(ca.getTime());

            Log.e("tag", c + "<---from---->" + dateFrom + "<----to-->" + dateFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        editor = sharedPreferences.edit();


        companyname = sharedPreferences.getString("companyname", "");
        managername = sharedPreferences.getString("managername", "");
        managerphone = sharedPreferences.getString("managerphone", "");
        staffname = sharedPreferences.getString("staffname", "");
        staffphone = sharedPreferences.getString("phoneno", "");
        alt_mgr = sharedPreferences.getString("alt_managername", "");
        alt_phone = sharedPreferences.getString("alt_managerphone", "");

        dbclass = new DbHelper(context);
        tv_logout = (TextView) findViewById(R.id.logout_tv);
        tv_staff = (TextView) findViewById(R.id.staff_id);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_comp_namtxt = (TextView) findViewById(R.id.tv_comp);
        tv_comp_name = (TextView) findViewById(R.id.tv_comp1);
        tv_manag_namtxt = (TextView) findViewById(R.id.tv_manager);
        // tv_manag_name = (TextView) findViewById(R.id.tv_manager1);
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

        tv_gallon = (TextView) findViewById(R.id.tv_gallon);
        et_gallon = (EditText) findViewById(R.id.editText);

        //tv_purchase_txt = (TextView) findViewById(R.id.tv_purchase);
        et_purchase = (EditText) findViewById(R.id.et_po);

        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        spin = (Spinner) findViewById(R.id.spinn);
        spin2 = (Spinner) findViewById(R.id.spinn2);


        String shour = String.format("%02d", hour);
        String smin = String.format("%02d", cMinute);

        if (c == 0) tv_time.setText(shour + " : " + smin + " AM");
        else tv_time.setText(shour + " : " + smin + " PM");


        dbclass = new DbHelper(context);

        lview = (ListView) findViewById(R.id.listview);
        tv_header = (TextView) findViewById(R.id.tv_header);

        tv_add_another = (TextView) findViewById(R.id.tv_add_another);

        tv_header.setTypeface(tf, 1);
        tv_comp_namtxt.setTypeface(tf);
        tv_comp_name.setTypeface(tf);
        tv_manag_namtxt.setTypeface(tf);
        // tv_manag_name.setTypeface(tf);
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
        tv_add_another.setTypeface(tf);
        tv_logout.setTypeface(tf);
        tv_gallon.setTypeface(tf);
        et_gallon.setTypeface(tf);
       // tv_purchase_txt.setTypeface(tf);
        et_purchase.setTypeface(tf);

        tv_comp_name.setText(companyname);
        // tv_manag_name.setText(managername);
        tv_staff.setText("Hi " + staffname);
        tv_date.setText(dateFrom);


        Log.e("tag_class", "" + alt_mgr);


        if (alt_mgr == null || alt_mgr.isEmpty() || alt_mgr.contains("null")) {
            asd.add(managername);
            Log.e("tag", "worked");
        } else {
            asd.add(managername);
            asd.add(alt_mgr);
        }

        if (sharedPreferences.getString("max_gallon", "") == "") {
            editor.putString("max_gallon", "240");
            editor.commit();
            total_gallon = "0";
        } else {
            total_gallon = sharedPreferences.getString("max_gallon", "");
            Log.e("tag", "1" + total_gallon);
        }

        //et_gallon.setText("");
        //  et_gallon.setEnabled(false);


        final CustomAdapter arrayAdapter = new CustomAdapter(context, R.layout.list2, asd) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView staff = (TextView) view.findViewById(R.id.text1);
                staff.setTypeface(tf, 1);
                staff.setTextSize(14);

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView staff_dropdown = (TextView) view.findViewById(R.id.text1);
                staff_dropdown.setTypeface(tf);
                staff_dropdown.setTextSize(14);

                return view;
            }
        };

        spin.setAdapter(arrayAdapter);


        getDataInList();

        adapter = new __StaffAdapter(context, myList, count, myList.size());
        lview.setAdapter(adapter);


        spin2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0) {
                   // staffname2 = baL.get(position).getName();
                    staffname2 = spin2.getSelectedItem().toString();
                    Log.e("tag","s:"+staffname2);
                }
                else{
                    staffname2 = "Choose Assist";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbclass.deleteDb();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("size", String.valueOf(3));
                editor.commit();
                Intent goStf = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(goStf);
                __Staff.this.finish();


            }
        });


        tv_add_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count + 1;
                adapter = new __StaffAdapter(context, myList, count, myList.size());
                lview.setAdapter(adapter);

                Toast.makeText(getApplicationContext(), "1 Row Added", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
                // Log.e("tag_class", "add_click" + count);

            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(et_gallon.getText().toString().trim().isEmpty())) {

                    getFromDb();
                    total_gallon = et_gallon.getText().toString().trim();
                    if (Integer.valueOf(total_gallon) > Integer.valueOf(sharedPreferences.getString("max_gallon", ""))) {
                        // Toast.makeText(getApplicationContext(), "Total Gallons should not exceed max capacity", Toast.LENGTH_SHORT).show();

                        new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Total Gallons should not exceed max capacity limit. \n Please Check in Settings.")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();

                    } else if (Integer.valueOf(total_gallon) == 0) {
                        //Toast.makeText(getApplicationContext(), "Total Gallons should not be zero", Toast.LENGTH_SHORT).show();

                        new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Total Gallons should not be Zero")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();


                    } else {



                        if ((!(et_purchase.getText().toString().trim().isEmpty()))  && et_purchase.getText().toString().trim().length()>3 ) {
                            purchase_order = et_purchase.getText().toString().trim();

                            purchase_order = "PO"+purchase_order;
                        }
                        else {
                            Random r = new Random();
                            int Low = 9999;
                            int High = 79999;
                            int Result = r.nextInt(High-Low) + Low;
                            purchase_order = "PO"+String.valueOf(Result);
                        }


                                editor.putString("gallon", et_gallon.getText().toString());
                                editor.commit();

                                choosen = spin.getSelectedItem().toString();



                                Log.e("tag", "or " + choosen+ purchase_order);

                                new staff_AddEntry().execute();
                            }

                } else {
                    // Toast.makeText(getApplicationContext(), "Enter Total Gallons", Toast.LENGTH_SHORT).show();

                    new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("No Input Found, Enter Total Gallons")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }


            }
        });

        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
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

    private void getDataInList() {

        getFromDb();

        //  Log.e("tag_class", "db_" + vin_positions.size());

        if (vin_positions.size() == 0) {
            myList.clear();
            for (int i = 0; i < ar_vin_no.length; i++) {
                __StaffData ld = new __StaffData();
                ld.setVin_no(ar_vin_no[i]);
                ld.setVin_make(ar_vin_make[i]);
                ld.setVin_start(ar_start[i]);
                ld.setVin_end(ar_end[i]);
                myList.add(ld);
            }
        } else if (vin_positions.size() == 1) {
            myList.clear();
            for (int i = 0; i < ar_vin_no.length; i++) {
                __StaffData ld = new __StaffData();
                if (i == 0) {
                    ld.setVin_no(vin_no.get(0));
                    ld.setVin_make(vin_makemodel.get(0));
                    ld.setVin_start(Integer.valueOf(vin_start_guage.get(0)));
                    ld.setVin_end(Integer.valueOf(vin_end_guage.get(0)));

                } else {
                    ld.setVin_no(ar_vin_no[i]);
                    ld.setVin_make(ar_vin_make[i]);
                    ld.setVin_start(ar_start[i]);
                    ld.setVin_end(ar_end[i]);
                }
                myList.add(ld);
            }
        } else if (vin_positions.size() == 2) {
            myList.clear();
            for (int i = 0; i < ar_vin_no.length; i++) {

                __StaffData ld = new __StaffData();
                if (i == 0) {
                    ld.setVin_no(vin_no.get(0));
                    ld.setVin_make(vin_makemodel.get(0));
                    ld.setVin_start(Integer.valueOf(vin_start_guage.get(0)));
                    ld.setVin_end(Integer.valueOf(vin_end_guage.get(0)));

                }
                if (i == 1) {
                    ld.setVin_no(vin_no.get(1));
                    ld.setVin_make(vin_makemodel.get(1));
                    ld.setVin_start(Integer.valueOf(vin_start_guage.get(1)));
                    ld.setVin_end(Integer.valueOf(vin_end_guage.get(1)));

                }
                if (i == 2) {
                    ld.setVin_no("Scan Vin");
                    ld.setVin_make("");
                    ld.setVin_start(0);
                    ld.setVin_end(0);
                }
                myList.add(ld);
            }
        } else {
            myList.clear();
            for (int i = 0; i < vin_positions.size(); i++) {
                __StaffData ld = new __StaffData();
                ld.setVin_no(vin_no.get(i));
                ld.setVin_make(vin_makemodel.get(i));
                ld.setVin_start(Integer.valueOf(vin_start_guage.get(i)));
                ld.setVin_end(Integer.valueOf(vin_end_guage.get(i)));
                myList.add(ld);
            }

        }


    }


    private void getFromDb() {

        vin_positions.clear();
        vin_no.clear();
        vin_makemodel.clear();
        vin_start_guage.clear();
        vin_end_guage.clear();
        // Log.e("tag_class", "getfromdb_class");
        Cursor cursor = dbclass.getFromDb();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex("pos"));
                    String vin_nos = cursor.getString(cursor.getColumnIndex("vinno"));
                    String make = cursor.getString(cursor.getColumnIndex("make"));
                    String start = cursor.getString(cursor.getColumnIndex("st_g"));
                    String end = cursor.getString(cursor.getColumnIndex("ed_g"));
                    vin_positions.add(id);
                    vin_no.add(vin_nos);
                    vin_makemodel.add(make);
                    vin_start_guage.add(start);
                    vin_end_guage.add(end);

                } while (cursor.moveToNext());

            }
        }

        //Log.e("tag_class", "length: " + vin_positions.size());
        showlog();
    }

    private void showlog() {

        if (vin_positions.size() != 0) {

            for (int i = 0; i < vin_positions.size(); i++) {
                Log.e("tag", i + " vin_" + vin_no.get(i) + "make_" + vin_makemodel.get(i) + "start_" + vin_start_guage.get(i) + "end_" + vin_end_guage.get(i));
            }
        }


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e("tag_class", "onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag_class", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tag_class", "onPause");
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.e("tag_class", "onCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag_class", "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("tag_class", "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag_class", "onRestart");

        if (count < 25) {
            count = count + 1;
        }

        getDataInList();
        adapter = new __StaffAdapter(context, myList, count, myList.size());
        lview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.e("tag_class", "restart_count" + count);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("tag_class", "onPostResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("tag_class", "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("tag_class", "onRestoreInstanceState");
    }


    class staff_AddEntry extends AsyncTask<String, Void, String> {
        String adr = "http://androidtesting.newlogics.in/newentry";

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(__Staff.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String start_gauge = "1/3", end_gauge = "1/3";
            try {
                JSONObject jsonObject = new JSONObject();


                JSONArray jsonArray = new JSONArray();
                String asd;
                for (int i = 0; i < vin_positions.size(); i++) {
                    Log.e("tag", "valueee" + vin_end_guage.get(i));


                    if (vin_start_guage.get(i).equals("0")) {
                        start_gauge = "1/8 Tank";
                    } else if (vin_start_guage.get(i).equals("1")) {
                        start_gauge = "1/4 Tank";
                    } else if (vin_start_guage.get(i).equals("2")) {
                        start_gauge = "1/2 Tank";
                    } else if (vin_start_guage.get(i).equals("3")) {
                        start_gauge = "3/4 Tank";
                    } else if (vin_start_guage.get(i).equals("4")) {
                        start_gauge = "Empty Tank";
                    } else if (vin_start_guage.get(i).equals("5")) {
                        start_gauge = "Full Tank";
                    }


                    if (vin_end_guage.get(i).equals("0")) {
                        end_gauge = "1/8 Tank";
                    } else if (vin_end_guage.get(i).equals("1")) {
                        end_gauge = "1/4 Tank";
                    } else if (vin_end_guage.get(i).equals("2")) {
                        end_gauge = "1/2 Tank";
                    } else if (vin_end_guage.get(i).equals("3")) {
                        end_gauge = "3/4 Tank";
                    } else if (vin_end_guage.get(i).equals("4")) {
                        end_gauge = "Empty Tank";
                    } else if (vin_end_guage.get(i).equals("5")) {
                        end_gauge = "Full Tank";
                    }

                    Log.e("tag", "endbh " + end_gauge);

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.accumulate("vin_no", vin_no.get(i));
                    jsonObject1.accumulate("make_model", vin_makemodel.get(i));
                    jsonObject1.accumulate("start_gauge", start_gauge);
                    jsonObject1.accumulate("end_gauge", end_gauge);

                    jsonArray.put(jsonObject1);
                    // }

                }
                asd = jsonArray.toString();
                Log.e("tag", "valueee" + asd);

                jsonObject.accumulate("company_name", companyname);
                jsonObject.accumulate("company_mgr", choosen);
                jsonObject.accumulate("mgr_phone", managerphone);
                Log.e("tag", "purchase order" + purchase_order);
                jsonObject.accumulate("staff_phone", staffphone);
                jsonObject.accumulate("staff_name", staffname);
                jsonObject.accumulate("purchase_order", purchase_order);
                jsonObject.accumulate("total_gallons", total_gallon);
                jsonObject.accumulate("entrydetail", jsonArray);

                    if(staffname2 != "Choose Assist") {
                        Log.e("tag",staffname2);

                        jsonObject.accumulate("assist", staffname2);
                    }



                json = jsonObject.toString();
                Log.e("tag", "" + json);
                return jsonStr = PostService.makeRequest(Data_Service.SERVICE_URL_NEW + "newentry", json);



            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                sweetDialog.dismiss();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("tag", "<-----rerseres---->" + s);
            sweetDialog.dismiss();
            super.onPostExecute(s);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("success")) {
                    dbclass.deleteDb();

                    String entry_form_id = jo.getString("fci_entry_form_id");
                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(__Staff.this);
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("entry_form_id", entry_form_id);
                    edit.putString("size", "3");
                    edit.commit();
                    Log.d("tag", "<-----msg----->" + msg);
                    new SweetAlertDialog(__Staff.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                    dbclass.deleteDb();

                                }
                            })
                            .show();
                } else {


                    if (msg.contains("Some fields are null")) {


                        new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Failed")
                                .setContentText("No Inputs Found, Please Scan VinNo.")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();


                    } else {
                        new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Failed")
                                .setContentText(msg)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Network Error.")
                        .setContentText("Try Again Later.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }


        }

    }


    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(__Staff.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();

            baL = new ArrayList<StaffFetchList>();
            boardlist = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {

                Log.e("tag_", "started");
                JSONObject jsonobject = PostService.getData(URL);

                Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                    Log.e("tag_", "1" + jsonobject.toString());
                }

                json = jsonobject.toString();

                return json;

            } catch (Exception e) {
                Log.e("InputStream", "2" + e.toString());
                jsonStr = "";
                sweetDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("tag", "<-----result---->" + s);
            sweetDialog.dismiss();
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String count = jo.getString("count");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("success")) {

                    if (Integer.valueOf(count) > 0) {


                        boardlist.add("Choose Assist");
                        JSONArray jsonarray = jo.getJSONArray("staff");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            StaffFetchList bl = new StaffFetchList();
                            bl.setName(jsonobject.optString("name"));
                            bl.setPhone(jsonobject.optString("phone"));
                            bl.setPassword(jsonobject.optString("password"));
                            baL.add(bl);
                            boardlist.add(jsonobject.optString("name"));
                            Log.d("tag", "<----worldlist----->" + boardlist);
                        }

                        final CustomAdapter arrayAdapter = new CustomAdapter(getApplicationContext(),R.layout.list3, boardlist) {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);

                                TextView staff = (TextView) view.findViewById(R.id.text1);
                                staff.setTextSize(15);
                                staff.setTypeface(tf,1);

                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);

                                TextView staff_dropdown = (TextView) view.findViewById(R.id.text1);
                                staff_dropdown.setTypeface(tf);
                                staff_dropdown.setTextSize(14);
                                staff_dropdown.setPadding(0,5,0,5);
                                view.setBackgroundColor(getResources().getColor(R.color.bg2));

                                return view;
                            }
                        };


                        spin2.setAdapter(arrayAdapter);
                    } else {
                        new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No Data Found")
                                .setContentText("Staff not created. Please Add Staff")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                        startActivity(i);
                                        __Staff.this.finish();
                                    }
                                })
                                .show();


                    }


                } else {

                    Log.e("tag_", "error");

                    new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Network!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(i);
                                    __Staff.this.finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();


                Log.e("tag_", "" + e.toString());

                new SweetAlertDialog(__Staff.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                __Staff.this.finish();
                            }
                        })
                        .show();
            }
        }

    }


}
