package fci.com.fci;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class StaffLogin extends AppCompatActivity {
    ImageView submit;
    Spinner spn_staffname;
    Typeface tf;
    EditText et_phone, et_pass;
    TextView tv_header, tv_footer, tv_repass;
    ArrayList<StaffFetchList> baL;
    ArrayList<String> boardlist;
    public String URL = Data_Service.SERVICE_URL_NEW + "staff/fetch";
    public String URL_LOGIN = Data_Service.SERVICE_URL_NEW + "staff/login";
    String phone, password, staffname,phoneno;
    ArrayList<String> fetchList = new ArrayList<String>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staff_login_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        spn_staffname = (Spinner) findViewById(R.id.spn_staff_name);
        tv_header = (TextView) findViewById(R.id.tv_header);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pass = (EditText) findViewById(R.id.et_password);
        tv_repass = (TextView) findViewById(R.id.tv_repassword);
        tv_footer = (TextView) findViewById(R.id.tv_terms);
        submit = (ImageView) findViewById(R.id.submit_iv);
        tv_header.setTypeface(tf);
        et_phone.setTypeface(tf);
        et_pass.setTypeface(tf);
        tv_repass.setTypeface(tf);
        tv_footer.setTypeface(tf);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString();
                password = et_pass.getText().toString();
                if (Util.Operations.isOnline(StaffLogin.this)) {

                    if (!phone.isEmpty() && !password.isEmpty()) {
                        new staffLogin_Task().execute();
                    } else {
                        new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("Enter All Details..")

                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();

                    }
                } else {
                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Internet Connectivity")
                            .show();
                }


            }
        });
        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                finish();
            }
        });
        new staffFetch_Task().execute();

        et_phone.setTypeface(tf, 1);
        et_pass.setTypeface(tf, 1);


    }

    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            baL = new ArrayList<StaffFetchList>();
            boardlist = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(URL, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----result---->" + s);
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("success")) {
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

                    final CustomAdapter arrayAdapter = new CustomAdapter(getApplicationContext(), R.layout.custom_spinner, boardlist) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;}
                            else
                            {return true;}
                        }



                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            return view;
                        }
                    };
                    spn_staffname.setAdapter(arrayAdapter);

                    spn_staffname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > -1)
                            {
                                String password = baL.get(position).getPassword();
                                staffname = baL.get(position).getName();
                                phoneno = baL.get(position).getPhone();

                                Log.e("tag", "password" + password);
                                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(StaffLogin.this);
                                SharedPreferences.Editor edit = s_pref.edit();
                                edit.putString("staffname", staffname);
                                edit.putString("phoneno", phoneno);
                                edit.commit();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    class staffLogin_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            baL = new ArrayList<StaffFetchList>();
            boardlist = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Phone", phone);
                jsonObject.accumulate("Password", password);
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(URL_LOGIN, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----resultlogin---->" + s);
            super.onPostExecute(s);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                if (status.equals("success")) {
                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText("Login successfully..")

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }


        }

    }


}
