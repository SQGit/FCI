package net.fciapp.fciscanner;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 10-06-2016.
 */
public class AdminDashboard extends AppCompatActivity {
    TextView tv_header,tv_createstaff,tv_editstaff,tv_createcomp,tv_editcomp;
    RelativeLayout lt_stf_cr,lt_stf_ed,lt_comp_cr,lt_comp_ed;
    Typeface tf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_dashboard_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_createstaff = (TextView) findViewById(R.id.tv_create_staff);
        tv_editstaff = (TextView) findViewById(R.id.tv_edit_staff);
        tv_createcomp = (TextView) findViewById(R.id.tv_create_companey);
        tv_editcomp = (TextView) findViewById(R.id.tv_edit_companey);
        lt_stf_cr  = (RelativeLayout) findViewById(R.id.lt_create_staff);
        lt_stf_ed  = (RelativeLayout) findViewById(R.id.lt_edit_staff);
        lt_comp_cr  = (RelativeLayout) findViewById(R.id.lt_create_company);
        lt_comp_ed  = (RelativeLayout) findViewById(R.id.lt_edit_company);
        tv_header.setTypeface(tf);
        tv_createstaff.setTypeface(tf);
        tv_editstaff.setTypeface(tf);
        tv_createcomp.setTypeface(tf);
        tv_editcomp.setTypeface(tf);


        lt_stf_cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),AdminStaffCreate.class);
                i.putExtra("sts",0);
                startActivity(i);
                //  AdminDashboard.this.finish();
            }
        });
        lt_stf_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminStaffEdit.class);
                startActivity(i);
                //  AdminDashboard.this.finish();
            }
        });
        lt_comp_cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),AdminCreateCompany.class);
                startActivity(i);
                // AdminDashboard.this.finish();
            }
        });

        lt_comp_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),AdminCompanyEdit.class);
                startActivity(i);
                // AdminDashboard.this.finish();
            }
        });





        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new SweetAlertDialog(AdminDashboard.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                                startActivity(i);
                                AdminDashboard.this.finish();
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


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new SweetAlertDialog(AdminDashboard.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to Logout?")
                .setConfirmText("Yes!")
                .setCancelText("No")

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(i);
                        AdminDashboard.this.finish();
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
}
