package net.fciapp.fciscanner;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 10-06-2016.
 */
public class Dashboard extends AppCompatActivity {
    LinearLayout admin,staff;
    TextView tv_admin,tv_staff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard);
        admin=(LinearLayout) findViewById(R.id.admin_lv);
        staff=(LinearLayout) findViewById(R.id.staff_lv);

        tv_admin = (TextView) findViewById(R.id.tv_admin);
        tv_staff = (TextView) findViewById(R.id.tv_staff);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        tv_admin.setTypeface(tf);
        tv_staff.setTypeface(tf);

        admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(i);
            }
        });
        staff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),StaffLogin.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        new SweetAlertDialog(Dashboard.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to exit the\nFCI?")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent i1 = new Intent(Intent.ACTION_MAIN);
                        i1.setAction(Intent.ACTION_MAIN);
                        i1.addCategory(Intent.CATEGORY_HOME);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);
                        Dashboard.this.finish();
                        ActivityCompat.finishAffinity(Dashboard.this);
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
