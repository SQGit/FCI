package net.fciapp.fciscanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 10-06-2016.
 */
public class StaffDashboard extends AppCompatActivity {
    TextView tv_header, addentry, viewentry, reports, setings;

    RelativeLayout lt_addentry, lt_viewentry, lt_settings;
    Typeface tf;
    DbHelper dbclass;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staff_dashboard_1);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        tv_header = (TextView) findViewById(R.id.tv_header);
        addentry = (TextView) findViewById(R.id.tv_add_entry);
        viewentry = (TextView) findViewById(R.id.tv_view_entry);
        setings = (TextView) findViewById(R.id.tv_settings);
        lt_addentry = (RelativeLayout) findViewById(R.id.layout_addentry);
        lt_viewentry = (RelativeLayout) findViewById(R.id.layout_viewentry);
        lt_settings = (RelativeLayout) findViewById(R.id.layout_settings);

        tv_header.setTypeface(tf, 1);
        addentry.setTypeface(tf);
        viewentry.setTypeface(tf);
        setings.setTypeface(tf);

        dbclass = new DbHelper(context);


        lt_addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStoragePermissionGranted()) {

                    Dialog_ChooseCompany cdd = new Dialog_ChooseCompany(StaffDashboard.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();

                    dbclass.deleteDb();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("size", String.valueOf(3));
                    editor.commit();
                }

            }
        });

        lt_viewentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goEntry = new Intent(getApplicationContext(), StaffViewEntry.class);
                startActivity(goEntry);
                // StaffDashboard.this.finish();
            }
        });


        lt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSett = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(goSett);

            }
        });


        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(StaffDashboard.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent goStf = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(goStf);
                                StaffDashboard.this.finish();
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

        new SweetAlertDialog(StaffDashboard.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to Logout?")
                .setConfirmText("Yes!")
                .setCancelText("No")

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Intent goStf = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(goStf);
                        StaffDashboard.this.finish();
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


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (StaffDashboard.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("tag0", "Permission is granted for storage");
                if (StaffDashboard.this.checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.e("tag0", "Permission is granted for camera");
                    return true;
                } else {
                    Log.e("tag0f", "Permission is revoked camera");
                    ActivityCompat.requestPermissions(StaffDashboard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                    return false;
                }
            } else {
                Log.e("tag0f", "Permission is revoked all");
                ActivityCompat.requestPermissions(StaffDashboard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("tag1", "Permission is granted");
            return true;
        }


    }


    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (StaffDashboard.this.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("tag0", "Permission is granted");
                return true;
            } else {
                Log.e("tag0f", "Permission is revoked");
                ActivityCompat.requestPermissions(StaffDashboard.this, new String[]{Manifest.permission.CAMERA}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("tag1", "Permission is granted");
            return true;
        }


    }


    // final private int code = 123;

    private void insert() {
        List<String> permissin = new ArrayList<>();
        final List<String> permissionList = new ArrayList<>();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {

            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("tag11", "permissionGranted");

                    Dialog_ChooseCompany cdd = new Dialog_ChooseCompany(StaffDashboard.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();

                    dbclass.deleteDb();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("size", String.valueOf(3));
                    editor.commit();

                } else {
                    Log.e("tag00", "Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                Log.e("tag00", "default");

        }

    }
}
