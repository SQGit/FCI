package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminCreateCompany extends AppCompatActivity {
    ImageView submit;
    TextView tv_header, tv_add_manag, tv_admin, tv_logout;
    EditText et_comp_name, et_comp_loc, et_manag_name, et_manag_phone, et_comp_email, et_alt_manag_name, et_alt_manag_phone;
    Typeface tf;
    LinearLayout lt_add;
    SweetAlertDialog sweetDialog;
    LinearLayout lt_logout;
    String companyname, companylocation, companyemail, managername, managerphone, altermanagername, altermanagerphone, str_address;
    int sss;
    int asd = 0;
    RelativeLayout alt_mgr, alt_ph;
    ScrollView scroll;
    String email, phone;

    ArrayList<String> ar_com_phone = new ArrayList<>();
    ArrayList<String> ar_com_email = new ArrayList<>();

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
        setContentView(R.layout.admin_create_company_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_add_manag = (TextView) findViewById(R.id.tv_add_manag);
        et_comp_name = (EditText) findViewById(R.id.et_comp_name);
        et_comp_loc = (EditText) findViewById(R.id.et_comp_location);
        et_manag_name = (EditText) findViewById(R.id.et_manager_name);
        et_manag_phone = (EditText) findViewById(R.id.et_manager_phone);
        et_comp_email = (EditText) findViewById(R.id.et_comp_email);

        et_alt_manag_name = (EditText) findViewById(R.id.et_alt_manager_name);
        et_alt_manag_phone = (EditText) findViewById(R.id.et_alt_manager_phone);

        alt_mgr = (RelativeLayout) findViewById(R.id.alt_mgr0);
        alt_ph = (RelativeLayout) findViewById(R.id.alt_mgr1);

        scroll = (ScrollView) findViewById(R.id.scroll);

        alt_mgr.setVisibility(View.GONE);
        alt_ph.setVisibility(View.GONE);


        submit = (ImageView) findViewById(R.id.submit_iv);
        lt_add = (LinearLayout) findViewById(R.id.layout_add);

        tv_admin = (TextView) findViewById(R.id.tv_admin_txt);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        tv_header.setTypeface(tf);
        tv_add_manag.setTypeface(tf);
        et_comp_name.setTypeface(tf);
        et_comp_loc.setTypeface(tf);
        et_manag_name.setTypeface(tf);
        et_manag_phone.setTypeface(tf);
        et_comp_email.setTypeface(tf);

        et_alt_manag_name.setTypeface(tf);
        et_alt_manag_phone.setTypeface(tf);


        tv_admin.setTypeface(tf);
        tv_logout.setTypeface(tf);

        str_address = Data_Service.SERVICE_URL_NEW + "company/add";
        Intent get_intent = getIntent();
        sss = get_intent.getIntExtra("sts", 0);


        if (Util.Operations.isOnline(AdminCreateCompany.this)) {
            new companyEdit_Task().execute();
        } else {
            new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            AdminCreateCompany.this.finish();
                        }
                    })
                    .show();
        }


        if (sss == 0) {
            et_comp_loc.setEnabled(true);
            et_manag_name.setEnabled(true);
            et_comp_email.setEnabled(true);
            et_manag_phone.setEnabled(true);
            Log.d("tag", "fromdash");
            str_address = Data_Service.SERVICE_URL_NEW + "company/add";
            tv_header.setText("Create Company");

        } else if (sss == 1) {
            companyname = get_intent.getStringExtra("name");
            String location = get_intent.getStringExtra("location");
            phone = get_intent.getStringExtra("phone");
            email = get_intent.getStringExtra("email");
            String managerName = get_intent.getStringExtra("managerName");

            String alt_mgr = get_intent.getStringExtra("alt_manager");
            String alt_ph = get_intent.getStringExtra("alt_phone");


            str_address = Data_Service.SERVICE_URL_NEW + "company/update";
            Log.e("tag", "fromUpdate" + alt_mgr + "_" + alt_ph);
            et_comp_name.setText(companyname);
            et_comp_loc.setText(location);
            et_comp_email.setText(email);
            et_manag_name.setText(managerName);
            et_manag_phone.setText(phone);
            et_comp_name.setEnabled(false);
            et_comp_loc.requestFocus();
            tv_header.setText("Update Company");


            if (alt_mgr != null && !alt_mgr.isEmpty() && !alt_mgr.contains("null")) {
                this.alt_mgr.setVisibility(View.VISIBLE);
                this.alt_ph.setVisibility(View.VISIBLE);
                et_alt_manag_name.setText(alt_mgr);
                et_alt_manag_phone.setText(alt_ph);
                asd = 1;
                Log.e("tag", "worked");
            }


        }


        et_manag_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    lt_add.requestFocus();
                    Log.e("tag", "scroll");
                    //   scroll.fullScroll(View.FOCUS_DOWN);
                    scroll.scrollTo(0, scroll.getBottom());


                }
                return false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AdminCreateCompany.this);
                String code = sharedPreferences.getString("tag", "");
                if ((code == "")) {
                    managername = et_manag_name.getText().toString();
                    managerphone = et_manag_phone.getText().toString();
                } else {
                    altermanagername = et_manag_name.getText().toString();
                    altermanagerphone = et_manag_phone.getText().toString();
                }*/

                if (sss == 1) {

                }


                if (!(et_comp_name.getText().toString().trim().isEmpty())) {
                    if (!(et_comp_loc.getText().toString().trim().isEmpty())) {
                        if (!(et_comp_email.getText().toString().trim().isEmpty())) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(et_comp_email.getText().toString().trim()).matches()) {
                                if (!(ar_com_email.contains(et_comp_email.getText().toString().trim()))) {
                                    if (!(et_manag_name.getText().toString().trim().isEmpty())) {
                                        if (!(et_manag_phone.getText().toString().trim().isEmpty())) {
                                            if (!(et_manag_phone.getText().length() < 9)) {
                                                if (!(ar_com_phone.contains(et_manag_phone.getText().toString().trim()))) {

                                                    Log.e("tag", "" + asd);
                                                    if (asd == 0) {
                                                        companyname = et_comp_name.getText().toString().trim();
                                                        companylocation = et_comp_loc.getText().toString().trim();
                                                        companyemail = et_comp_email.getText().toString().trim();
                                                        managername = et_manag_name.getText().toString().trim();
                                                        managerphone = et_manag_phone.getText().toString().trim();


                                                        new createCompany_Task().execute();

                                                    } else {


                                                        if (!(et_alt_manag_name.getText().toString().trim().isEmpty())) {
                                                            if (!(et_alt_manag_phone.getText().toString().trim().isEmpty())) {
                                                                if (!(et_alt_manag_phone.getText().length() < 9)) {
                                                                    if (!(et_alt_manag_name.getText().toString().trim().equals((et_manag_name.getText().toString().trim())))) {
                                                                        if (!(et_alt_manag_phone.getText().toString().trim().equals((et_manag_phone.getText().toString().trim())))) {
                                                                            companyname = et_comp_name.getText().toString().trim();
                                                                            companylocation = et_comp_loc.getText().toString().trim();
                                                                            companyemail = et_comp_email.getText().toString().trim();
                                                                            managername = et_manag_name.getText().toString().trim();
                                                                            managerphone = et_manag_phone.getText().toString().trim();
                                                                            altermanagername = et_alt_manag_name.getText().toString().trim();
                                                                            altermanagerphone = et_alt_manag_phone.getText().toString().trim();

                                                                            new createCompany_Task().execute();
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "Phone Number already Entered by Manager.Please Try Another.", Toast.LENGTH_SHORT).show();
                                                                            et_alt_manag_phone.requestFocus();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "Manager Name already entered.Please Try another.", Toast.LENGTH_SHORT).show();
                                                                        et_alt_manag_name.requestFocus();
                                                                    }

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                                                                    et_alt_manag_phone.requestFocus();
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                                                                et_alt_manag_phone.requestFocus();
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Enter Alternate Manager Name", Toast.LENGTH_SHORT).show();
                                                            et_alt_manag_name.requestFocus();
                                                        }


                                                    }


                                                } else {
                                                    Toast.makeText(getApplicationContext(), "This Number is already in use.Please Try Another", Toast.LENGTH_SHORT).show();
                                                    et_manag_phone.requestFocus();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                                                et_manag_phone.requestFocus();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                                            et_manag_phone.requestFocus();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Enter Manager Name", Toast.LENGTH_SHORT).show();
                                        et_manag_name.requestFocus();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email already entered by anoter company.Please Try Another", Toast.LENGTH_SHORT).show();
                                    et_comp_email.requestFocus();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter Valid Mail", Toast.LENGTH_SHORT).show();
                                et_comp_email.requestFocus();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                            et_comp_email.requestFocus();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Company Location", Toast.LENGTH_SHORT).show();
                        et_comp_loc.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                    et_comp_name.requestFocus();
                }


            }
        });

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sss == 0) {
                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                    AdminCreateCompany.this.finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), AdminCompanyEdit.class);
                    startActivity(i);
                    AdminCreateCompany.this.finish();
                }


            }
        });

        lt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("tag", "1234");
                edit.commit();
                managername = et_manag_name.getText().toString();
                managerphone = et_manag_phone.getText().toString();
                et_manag_name.setText("");
                et_manag_phone.setText("");
                et_manag_name.requestFocus();*/


                if (asd == 0) {

                    Log.e("tag", "" + asd + "visibile");
                    alt_mgr.setVisibility(View.VISIBLE);
                    alt_ph.setVisibility(View.VISIBLE);
                    asd = 1;

                    tv_add_manag.setText("- No Alt.Manager");
                    et_alt_manag_name.requestFocus();
                } else if (asd == 1) {

                    Log.e("tag", "" + asd + "gone");
                    alt_mgr.setVisibility(View.GONE);
                    alt_ph.setVisibility(View.GONE);
                    asd = 0;
                    tv_add_manag.setText("+ Alt.Manager Details.");
                }


            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminCreateCompany.this.finish();
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

        setupUI(findViewById(R.id.top));


    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AdminCreateCompany.this);
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

    class createCompany_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#FFE64A19"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("company_name", companyname);
                jsonObject.accumulate("company_location", companylocation);
                jsonObject.accumulate("company_email", companyemail);
                jsonObject.accumulate("mgr_name", managername);
                jsonObject.accumulate("mgr_phone", managerphone);
                if (asd == 1) {
                    jsonObject.accumulate("alt_mgr_name", altermanagername);
                    jsonObject.accumulate("alt_mgr_phone", altermanagerphone);
                }
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(str_address, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                sweetDialog.dismiss();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----rerseres---->" + s);
            super.onPostExecute(s);

            sweetDialog.dismiss();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("success")) {
                    Log.d("tag", "<-----msg----->" + msg);
                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent goDash = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(goDash);
                                    AdminCreateCompany.this.finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
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

                new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING MESSAGE!!!")
                        .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminCreateCompany.this.finish();
                            }
                        })
                        .show();
            }

        }

    }


    class companyEdit_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#FFE64A19"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {

                String virtual_url = Data_Service.SERVICE_URL_NEW + "company/fetch";

                JSONObject jsonobject = PostService.getStaffs(virtual_url);

                Log.d("tag", "" + jsonobject.toString());

                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                }

                json = jsonobject.toString();

                return json;
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


                if (status.equals("success")) {
                    JSONArray staff_datas = jo.getJSONArray("company");
                    Log.d("tag", "<-----company----->" + "" + staff_datas);
                    for (int i = 0; i < staff_datas.length(); i++) {
                        JSONObject datas = staff_datas.getJSONObject(i);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("company_name", datas.getString("company_name"));
                        map.put("company_location", datas.getString("company_location"));
                        map.put("company_email", datas.getString("company_email"));
                        map.put("mgr_name", datas.getString("mgr_name"));
                        map.put("mgr_phone", datas.getString("mgr_phone"));
                        map.put("alt_mgr_name", datas.getString("alt_mgr_name"));
                        map.put("alt_mgr_phone", datas.getString("alt_mgr_phone"));

                        ar_com_phone.add(i, datas.getString("mgr_phone"));
                        ar_com_email.add(i, datas.getString("company_email"));

                    }


                    if (sss == 1) {
                        ar_com_email.remove(email);
                        ar_com_phone.remove(phone);
                    }


                } else {

                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();

                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminCreateCompany.this.finish();
                            }
                        })
                        .show();


            }

        }

    }

}
