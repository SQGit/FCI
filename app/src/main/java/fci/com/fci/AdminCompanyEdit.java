package fci.com.fci;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
 * Created by Salman on 6/16/2016.
 */
public class AdminCompanyEdit extends Activity {

    ListView lv_staffsList;
    SweetAlertDialog sweetDialog;
    ArrayList<String> ar_com_name = new ArrayList<>();
    ArrayList<String> ar_com_loc = new ArrayList<>();
    ArrayList<String> ar_com_phone = new ArrayList<>();
    ArrayList<String> ar_com_email = new ArrayList<>();

    Typeface tf;

    TextView tv_header,tv_name_txt,tv_phone_txt,tv_pass_txt,tv_edit_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_companyedit);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        lv_staffsList = (ListView) findViewById(R.id.listview);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_name_txt = (TextView) findViewById(R.id.tv_name);
        tv_phone_txt = (TextView) findViewById(R.id.tv_phone);
        tv_pass_txt = (TextView) findViewById(R.id.tv_password);
        tv_edit_txt = (TextView) findViewById(R.id.tv_edit);

        tv_header.setTypeface(tf);
        tv_name_txt.setTypeface(tf);
        tv_phone_txt.setTypeface(tf);
        tv_pass_txt.setTypeface(tf);
        tv_edit_txt.setTypeface(tf);



        if (!Data_Service.isNetworkAvailable(AdminCompanyEdit.this)) {
            new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops!")
                    .setContentText("No network Available!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.setCancelable(false);
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .show();
        } else {
            new staffEdit_Task().execute();
        }




        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });

    }


    class staffEdit_Task extends AsyncTask<String, Void, String> {
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

                Log.d("tag", "" + jsonobject.toString());

                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(AdminCompanyEdit.this, SweetAlertDialog.WARNING_TYPE)
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

                 //   company_name, company_location, company_email, mgr_name, mgr_phone, alt_mgr_name, alt_mgr_phone

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

                        ar_com_name.add(i,datas.getString("company_name"));
                        ar_com_loc.add(i,datas.getString("company_location"));
                        ar_com_phone.add(i,datas.getString("mgr_phone"));
                        ar_com_email.add(i,datas.getString("company_email"));

                    }


                    AdapterCompanyEdit staff_adapter = new AdapterCompanyEdit(AdminCompanyEdit.this,ar_com_name,ar_com_loc,ar_com_email,ar_com_phone);

                    lv_staffsList.setAdapter(staff_adapter);


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
            }

        }

    }


}
