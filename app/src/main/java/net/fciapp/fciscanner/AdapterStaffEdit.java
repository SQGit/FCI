package net.fciapp.fciscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterStaffEdit extends BaseAdapter {

    Context context;
    ArrayList<UserData> arrayList;
    TextView tv_name, tv_phone, tv_password, tv_editview;
    LinearLayout lt_bg;
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;
    SweetAlertDialog sweetDialog;

    AdapterStaffEdit(Context c1, ArrayList<String> name, ArrayList<String> phone, ArrayList<String> pass) {
        this.context = c1;
        this.ar_name = name;
        this.ar_phone = phone;
        this.ar_pass = pass;
    }


    @Override
    public int getCount() {
        return ar_name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_staffedit, null);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        tv_password = (TextView) convertView.findViewById(R.id.tv_password);
        tv_editview = (TextView) convertView.findViewById(R.id.tv_editview);
        lt_bg = (LinearLayout) convertView.findViewById(R.id.layout_bg);

        tv_name.setTypeface(tf);
        tv_phone.setTypeface(tf);
        tv_password.setTypeface(tf);
        tv_editview.setTypeface(tf);


        if (position % 2 == 0) {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg1));


        } else {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        }


        tv_name.setText(ar_name.get(position));
        tv_phone.setText(ar_phone.get(position));
        tv_password.setText(ar_pass.get(position));


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("tag", "-" + ar_phone.get(position));
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you Sure want to Delete?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                new staffDelete_Task(ar_phone.get(position)).execute();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

                return false;
            }
        });

        tv_editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "-" + ar_phone.get(position) + "-" + ar_name.get(position) + "-" + ar_pass.get(position));

                Intent goupdate = new Intent(context, AdminStaffCreate1.class);
                goupdate.putExtra("sts",1);
                goupdate.putExtra("name",ar_name.get(position));
                goupdate.putExtra("phone",ar_phone.get(position) );
                goupdate.putExtra("pass",ar_pass.get(position));
                context.startActivity(goupdate);
            }
        });


        return convertView;
    }


    class staffDelete_Task extends AsyncTask<String, Void, String> {

        String phone;
        public staffDelete_Task(String ph) {
            this.phone = ph;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
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
                jsonObject.accumulate("phone", phone);
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(Data_Service.SERVICE_URL_NEW + "staff/delete", json);
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
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent goDash = new Intent(context, AdminStaffEdit.class);
                                    context.startActivity(goDash);

                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(context, AdminDashboard.class);
                                context.startActivity(i);
                            }
                        })
                        .show();
            }

        }

    }






}
