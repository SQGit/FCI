package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdminCompanyEdit extends Activity {

    ListView lv_staffsList;
    SweetAlertDialog sweetDialog;
    ArrayList<String> ar_com_name = new ArrayList<>();
    ArrayList<String> ar_com_loc = new ArrayList<>();
    ArrayList<String> ar_com_phone = new ArrayList<>();
    ArrayList<String> ar_com_email = new ArrayList<>();
    ArrayList<String> ar_com_managerName = new ArrayList<>();
    ArrayList<String> alt_managerName = new ArrayList<>();
    ArrayList<String> alt_phone = new ArrayList<>();

    Typeface tf;
    LinearLayout lt_logout;
    TextView tv_header, tv_name_txt, tv_phone_txt, tv_pass_txt, tv_edit_txt, tv_logout, tv_admin_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.admin_companyedit);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        lv_staffsList = (ListView) findViewById(R.id.listview);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_name_txt = (TextView) findViewById(R.id.tv_name);
        tv_phone_txt = (TextView) findViewById(R.id.tv_phone);
        tv_pass_txt = (TextView) findViewById(R.id.tv_password);
        tv_edit_txt = (TextView) findViewById(R.id.tv_edit);

        tv_admin_txt = (TextView) findViewById(R.id.tv_admin);
        tv_logout = (TextView) findViewById(R.id.tv_logout);

        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        tv_header.setTypeface(tf);
        tv_name_txt.setTypeface(tf);
        tv_phone_txt.setTypeface(tf);
        tv_pass_txt.setTypeface(tf);
        tv_edit_txt.setTypeface(tf);

        tv_admin_txt.setTypeface(tf);
        tv_logout.setTypeface(tf);


        if (Util.Operations.isOnline(AdminCompanyEdit.this)) {
            new companyEdit_Task().execute();
        } else {
            new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            AdminCompanyEdit.this.finish();
                        }
                    })
                    .show();
        }




        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(i);
                AdminCompanyEdit.this.finish();
            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminCompanyEdit.this.finish();
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


    class companyEdit_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.PROGRESS_TYPE);
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

                Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                    Log.e("tag_", "1" + jsonobject.toString());
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
                String count = jo.getString("count");


                if (status.equals("success")) {


                    if(Integer.valueOf(count)>0) {


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

                            ar_com_name.add(i, datas.getString("company_name"));
                            ar_com_loc.add(i, datas.getString("company_location"));
                            ar_com_phone.add(i, datas.getString("mgr_phone"));
                            ar_com_email.add(i, datas.getString("company_email"));
                            ar_com_managerName.add(i, datas.getString("mgr_name"));
                            alt_managerName.add(i, datas.getString("alt_mgr_name"));
                            alt_phone.add(i, datas.getString("alt_mgr_phone"));

                            Log.e("tag", "" + alt_managerName + alt_phone);
                        }

                        AdapterCompanyEdit staff_adapter = new AdapterCompanyEdit(AdminCompanyEdit.this, ar_com_name, ar_com_loc, ar_com_email, ar_com_phone, ar_com_managerName, alt_managerName, alt_phone);
                        lv_staffsList.setAdapter(staff_adapter);

                    }
                    else{
                        new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No Data Found")
                                .setContentText("Companies not Added,Please Add Company")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminCompanyEdit.this.finish();
                                    }
                                })
                                .show();

                    }


                }
                else {

                    new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();

                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminCompanyEdit.this.finish();
                            }
                        })
                        .show();


            }

        }

    }


}
