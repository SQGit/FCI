package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class StaffLogin extends AppCompatActivity {
    public String URL = Data_Service.SERVICE_URL_NEW + "staff/fetch";

    //http://ec2-54-174-246-193.compute-1.amazonaws.com/staff/fetch
    public String URL_LOGIN = Data_Service.SERVICE_URL_NEW + "staff/login";
    ImageView submit;
    Spinner spn_staffname;
    Typeface tf;
    EditText et_phone, et_pass;
    TextView tv_header, tv_footer, tv_repass;
    ArrayList<StaffFetchList> baL;
    ArrayList<String> boardlist;
    ArrayList<String> phones = new ArrayList<>();
    String phone, password, staffname, phoneno;
    ArrayList<String> fetchList = new ArrayList<String>();
    SweetAlertDialog sweetDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staff_login_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StaffLogin.this);

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

        tv_repass.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString().trim();
                password = et_pass.getText().toString().trim();
                Log.e("tag", "ph+ " + sharedPreferences.getString("phoneno", ""));
                Log.e("tag", "ph+ " + sharedPreferences.getString("phoneno", ""));
                Log.e("tag", "ph+ " + phone);
                if (Util.Operations.isOnline(StaffLogin.this)) {
                    if (!phone.isEmpty()) {
                        if (phone.length() > 4) {
                            if (phone.equals(sharedPreferences.getString("phoneno", ""))) {
                                if (!password.isEmpty()) {
                                    if (password.length() == 4) {

                                        if (password.equals(sharedPreferences.getString("password", ""))) {
                                            //  new staffLogin_Task().execute();

                                            Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                                            startActivity(i);
                                            StaffLogin.this.finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Password Not Match", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password Should be 4 digit character", Toast.LENGTH_SHORT).show();
                                        et_pass.requestFocus();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                                    et_pass.requestFocus();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Phone Number Not Match With Staff Name.", Toast.LENGTH_SHORT).show();
                                et_phone.requestFocus();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                            et_phone.requestFocus();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        et_phone.requestFocus();
                    }
                } else {
                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText("No Internet Connectivity")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(i);
                                    StaffLogin.this.finish();
                                }
                            })
                            .show();


                }


            }
        });
        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                StaffLogin.this.finish();



            }
        });


        setupUI(findViewById(R.id.top));

        if (Util.Operations.isOnline(StaffLogin.this)) {
            new staffFetch_Task().execute();
        } else {
            new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(i);
                            StaffLogin.this.finish();
                        }
                    })
                    .show();


        }


        et_phone.setTypeface(tf, 1);
        et_pass.setTypeface(tf, 1);


        spn_staffname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > -1) {
                    password = baL.get(position).getPassword();
                    staffname = baL.get(position).getName();
                    phoneno = baL.get(position).getPhone();

                    Log.e("tag", "password" + password + "__ " + phoneno + " _ " + staffname);

                    editor = sharedPreferences.edit();
                    editor.putString("staffname", staffname);
                    editor.putString("phoneno", phoneno);
                    editor.putString("password", password);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(StaffLogin.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        StaffLogin.this.finish();
    }

    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            baL = new ArrayList<StaffFetchList>();
            boardlist = new ArrayList<>();

            try {

                Log.e("tag_", "started");
                JSONObject jsonobject = PostService.getData(URL);

                Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
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


                        JSONArray jsonarray = jo.getJSONArray("staff");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            StaffFetchList bl = new StaffFetchList();
                            bl.setName(jsonobject.optString("name"));
                            bl.setPhone(jsonobject.optString("phone"));
                            bl.setPassword(jsonobject.optString("password"));
                            baL.add(bl);
                            boardlist.add(jsonobject.optString("name"));
                            phones.add(jsonobject.optString("phone"));
                            Log.d("tag", "<----worldlist----->" + boardlist);
                        }

                        final CustomAdapter arrayAdapter = new CustomAdapter(getApplicationContext(), R.layout.list, boardlist) {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);

                                TextView staff = (TextView) view.findViewById(R.id.text1);
                                staff.setTypeface(tf, 1);

                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);

                                TextView staff_dropdown = (TextView) view.findViewById(R.id.text1);
                                staff_dropdown.setTypeface(tf);
                                view.setBackgroundColor(getResources().getColor(R.color.bg2));

                                return view;
                            }
                        };


                        spn_staffname.setAdapter(arrayAdapter);
                    } else {
                        new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No Data Found")
                                .setContentText("Staff not created. Please Add Staff")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                        startActivity(i);
                                        StaffLogin.this.finish();
                                    }
                                })
                                .show();


                    }


                } else {

                    Log.e("tag_", "error");

                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Network!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(i);
                                    StaffLogin.this.finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();


                Log.e("tag_", "" + e.toString());

                new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                StaffLogin.this.finish();
                            }
                        })
                        .show();
            }
        }

    }

    class staffLogin_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();


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
            sweetDialog.dismiss();
            super.onPostExecute(s);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                if (status.equals("success")) {

                    Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                    startActivity(i);
                    finish();

                } else {

                    new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Network!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(i);
                                    StaffLogin.this.finish();
                                }
                            })
                            .show();
                }
            } catch (Exception e) {

                new SweetAlertDialog(StaffLogin.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                StaffLogin.this.finish();
                            }
                        })
                        .show();

            }


        }

    }
}
