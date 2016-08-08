package fci.com.fci;

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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/22/2016.
 */
public class
StaffAddEntry extends Activity {
    static int siz_da = 3;
    public HashMap<Integer, String> vin_make = new HashMap<>();
    public HashMap<Integer, String> name = new HashMap<>();
    public ArrayList<Integer> v_pos = new ArrayList<>();
    public ArrayList<String> v_mk = new ArrayList<>();
    public ArrayList<String> vin_positions = new ArrayList<>();
    TextView tv_logout, tv_staff, tv_header, tv_comp_namtxt, tv_comp_name, tv_manag_namtxt, tv_manag_name, tv_datetxt, tv_date, tv_timetxt, tv_time, tv_vinno, tv_make, tv_startgug, tv_endgug, tv_save, tv_note, tv_add_another;
    Typeface tf;
    ListView lv_entries;
    AdapterAddEntry staff_adapter;
    DbHelper dbclass;
    Context context = this;
    String managername, managerphone, companyname, staffname, dateFrom, staffphone;
    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();

    Adapter_Add staff_add_adapter;
    SweetAlertDialog sweetDialog;


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
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

/*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("size","3");
        editor.commit();*/


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


        getFromDb();
        staff_adapter = new AdapterAddEntry(StaffAddEntry.this, StaffAddEntry.this, vin_make, v_pos, v_mk, siz_da);
        lv_entries.setAdapter(staff_adapter);

      /* staff_add_adapter = new Adapter_Add(StaffAddEntry.this,getApplicationContext(),siz_da);
        lv_entries.setAdapter(staff_add_adapter);*/
        Log.e("tag", "" + siz_da);


        tv_add_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siz_da = siz_da + 1;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("size", String.valueOf(siz_da));
                // editor.putString("vv_make" + pos_view, holder.tv_make.getText().toString());
                editor.commit();

                getFromDb();
                //staff_add_adapter.notifyDataSetChanged();
                staff_adapter = new AdapterAddEntry(StaffAddEntry.this, StaffAddEntry.this, vin_make, v_pos, v_mk, siz_da);
                lv_entries.setAdapter(staff_adapter);
                staff_adapter.notifyDataSetChanged();

                Intent intent = getIntent();
                startActivity(intent);
                finish();
            }
        });


        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(StaffAddEntry.this, SweetAlertDialog.WARNING_TYPE)
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


                dbclass.deleteDb();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("size", String.valueOf(3));
                editor.commit();


                Intent goStf = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(goStf);
                finish();
            }
        });


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFromDb();
                new staff_AddEntry().execute();

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag_", "restart");

        // getFromDb();
        /*staff_adapter = new AdapterAddEntry(StaffAddEntry.this, StaffAddEntry.this, vin_make, v_pos, v_mk, siz_da);
        lv_entries.setAdapter(staff_adapter);*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag_", "resume");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e("tag_", "postcreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("tag", "onstart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("tag", "onporstresume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tag_", "pause");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("tag_", "restore");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("tag_", "saveinst");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e("tag_", "saveInst2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag_", "stop");
    }

    private void getFromDb() {

        vin_positions.clear();
        vin_no.clear();
        vin_makemodel.clear();
        vin_start_guage.clear();
        vin_end_guage.clear();
        Log.e("tag", "getfromdb_class");
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

        Log.e("tag", "length: " + vin_positions.size());

        //   Log.e("tag", "getfromdb" + vin_positions.size() + vin_no.size() + vin_makemodel.size());
        // Log.e("tag", "starttt" + getStart.size() + getStart);
    }


    class staff_AddEntry extends AsyncTask<String, Void, String> {
        String adr = "http://androidtesting.newlogics.in/newentry";

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(StaffAddEntry.this, SweetAlertDialog.PROGRESS_TYPE);
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
                    if (vin_end_guage.get(i).equals("0")) {
                        end_gauge = "1/2";
                    } else if (vin_end_guage.get(i).equals("1")) {
                        end_gauge = "1/4";
                    } else if (vin_end_guage.get(i).equals("2")) {
                        end_gauge = "3/4";
                    }


                    if (vin_start_guage.get(i).equals("0")) {
                        start_gauge = "1/2";
                    } else if (vin_start_guage.get(i).equals("1")) {
                        start_gauge = "1/4";
                    } else if (vin_start_guage.get(i).equals("2")) {
                        start_gauge = "3/4";
                    }

                    Log.e("tag", "endbh " + end_gauge);

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.accumulate("vin_no", vin_no.get(i));
                    jsonObject1.accumulate("make_model", vin_makemodel.get(i));
                    jsonObject1.accumulate("start_gauge", start_gauge);
                    jsonObject1.accumulate("end_gauge", end_gauge);

                    jsonArray.put(jsonObject1);


                }
                asd = jsonArray.toString();
                Log.e("tag", "valueee" + asd);


                jsonObject.accumulate("company_name", companyname);
                jsonObject.accumulate("company_mgr", managername);
                jsonObject.accumulate("mgr_phone", managerphone);
                jsonObject.accumulate("staff_phone", staffphone);
                jsonObject.accumulate("staff_name", staffname);
                jsonObject.accumulate("total_gallons", "240");
                jsonObject.accumulate("entrydetail", jsonArray);


                json = jsonObject.toString();
                Log.d("tag", "" + json);
                return jsonStr = PostService.makeRequest(adr, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----rerseres---->" + s);

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
                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(StaffAddEntry.this);
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("entry_form_id", entry_form_id);
                    edit.putString("size", "3");
                    edit.commit();
                    Log.d("tag", "<-----msg----->" + msg);
                    new SweetAlertDialog(StaffAddEntry.this, SweetAlertDialog.SUCCESS_TYPE)
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
                    new SweetAlertDialog(StaffAddEntry.this, SweetAlertDialog.WARNING_TYPE)
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

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
