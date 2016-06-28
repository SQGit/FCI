package fci.com.fci;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
ArrayList<String>fetchList=new ArrayList<String>();
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
                Intent i = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(i);
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
        String[] plants = new String[]{"Staff Name",
                "Ramya",
                "Tulasi",
                "Anand",
                "Hari",
                "Arun"
        };


        /*final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view;
                tv.setTypeface(tf, 1);
                tv.setTextColor(getResources().getColor(R.color.textColorGray));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(tf, 1);
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textColorGray));
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spn_staffname.setAdapter(spinnerArrayAdapter);
*/
        spn_staffname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            baL = new ArrayList<StaffFetchList>();
            boardlist=new ArrayList<>();

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
                        bl.setName(jsonobject.optString("phone"));
                        bl.setName(jsonobject.optString("password"));

                        baL.add(bl);
                        boardlist.add(jsonobject.optString("name"));
                        Log.d("tag", "<----worldlist----->" + boardlist);
                    }

                    final CustomAdapter arrayAdapter = new CustomAdapter(getApplicationContext(), R.layout.custom_spinner, boardlist) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                          //  TextView tv = (TextView) view;
                           // tv.setTypeface(tf);

                            return view;
                        }
                    };
                    spn_staffname.setAdapter(arrayAdapter);

                    spn_staffname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > -1) {
                                String password = baL.get(position).getPassword();
                                Log.e("tag","password"+password);
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

}
