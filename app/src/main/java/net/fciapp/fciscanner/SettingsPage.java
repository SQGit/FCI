package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 8/24/2016.
 */
public class SettingsPage extends Activity {
    TextView tv_header, tv_submit, tv_gallon,tv_sounds,tv_edit_fetch;
    ToggleButton tb_sound,tb_editfetch;
    EditText et_gallon;
    Typeface tf;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String total_gallon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_gallon = (TextView) findViewById(R.id.tv_gallon);
        et_gallon = (EditText) findViewById(R.id.et_gallon);

        tv_sounds = (TextView) findViewById(R.id.tv_sounds);
        tv_edit_fetch = (TextView) findViewById(R.id.tv_edit_fetch);
        tb_sound = (ToggleButton) findViewById(R.id.tog_sound);
        tb_editfetch = (ToggleButton) findViewById(R.id.tog_fetch_button);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();


        tv_header.setTypeface(tf);
        tv_submit.setTypeface(tf);
        tv_gallon.setTypeface(tf);
        et_gallon.setTypeface(tf);
        tv_sounds.setTypeface(tf);
        tv_edit_fetch.setTypeface(tf);
        tb_sound.setTypeface(tf);
        tb_editfetch.setTypeface(tf);


        if (sharedPreferences.getString("max_gallon", "") == "") {
            total_gallon = "240";
            Log.e("tag", "0" + total_gallon);
        } else {
            total_gallon = sharedPreferences.getString("max_gallon", "");
            Log.e("tag", "1" + total_gallon);
        }

        et_gallon.setText(total_gallon);

        int pos = et_gallon.getText().length();
        et_gallon.setSelection(pos);

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goStf = new Intent(getApplicationContext(), StaffDashboard.class);
                startActivity(goStf);
                SettingsPage.this.finish();

            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(et_gallon.getText().toString().trim().isEmpty())) {

                    int tdf = Integer.valueOf(et_gallon.getText().toString().trim());

                    if (tdf > 0) {
                        editor.putString("max_gallon", et_gallon.getText().toString());
                        editor.commit();
                        SettingsPage.this.finish();
                    } else {
                        // Toast.makeText(getApplicationContext(), "Maximum Gallon should not be zero", Toast.LENGTH_SHORT).show();

                        new SweetAlertDialog(SettingsPage.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Maximum Gallons Limit should not be Zero")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .show();

                    }
                } else {
                    // Toast.makeText(getApplicationContext(), "Enter Max Gallons", Toast.LENGTH_SHORT).show();

                    new SweetAlertDialog(SettingsPage.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("No Input Found, Enter Max Gallons Limit")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();

                }

            }
        });

    }
}
