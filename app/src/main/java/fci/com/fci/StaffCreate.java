package fci.com.fci;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ramya on 11-06-2016.
 */
public class StaffCreate extends AppCompatActivity {
    ImageView submit;
    EditText et_name,et_phone,et_pin1, et_pin2, et_pin3, et_pin4, et_repin1, et_repin2, et_repin3, et_repin4;
    String str_pin,str_repin,str_name,str_phone;
    LinearLayout lt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_createstaff);

        submit = (ImageView) findViewById(R.id.submit_iv);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pin1 = (EditText) findViewById(R.id.et_pin1);
        et_pin2 = (EditText) findViewById(R.id.et_pin2);
        et_pin3 = (EditText) findViewById(R.id.et_pin3);
        et_pin4 = (EditText) findViewById(R.id.et_pin4);
        et_repin1 = (EditText) findViewById(R.id.et_repin1);
        et_repin2 = (EditText) findViewById(R.id.et_repin2);
        et_repin3 = (EditText) findViewById(R.id.et_repin3);
        et_repin4 = (EditText) findViewById(R.id.et_repin4);
        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag","logout");
            }
        });


       /* et_pin1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_pin2.requestFocus();
                    if (et_pin1.getText().length() == 1) {
                        et_pin2.requestFocus();
                    } else {
                        et_pin1.requestFocus();
                    }
                }
                return false;
            }
        });*/


        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_pin1.requestFocus();
                }
                return false;
            }
        });



        et_pin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin1.getText().length() == 1) {
                    et_pin2.requestFocus();
                } else {
                    et_pin1.requestFocus();
                }
                return false;
            }
        });

        et_pin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin2.getText().length() == 1) {
                    et_pin3.requestFocus();
                } else {
                    et_pin2.requestFocus();
                }
                return false;
            }
        });

        et_pin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin3.getText().length() == 1) {
                    et_pin4.requestFocus();
                } else {
                    et_pin3.requestFocus();
                }
                return false;
            }
        });

        et_pin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin4.getText().length() == 1) {
                    // pin = pin1.getText().toString()+pin2.getText().toString()+pin3.getText().toString()+pin4.getText().toString();
                    //Toast.makeText(getApplicationContext(),pin,Toast.LENGTH_LONG).show();
                    et_repin1.requestFocus();
                } else {
                    et_pin4.requestFocus();
                }
                return false;
            }
        });


        et_repin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin1.getText().length() == 1) {
                    et_repin2.requestFocus();
                } else {
                    et_repin1.requestFocus();
                }
                return false;
            }
        });

        et_repin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin2.getText().length() == 1) {
                    et_repin3.requestFocus();
                } else {
                    et_repin2.requestFocus();
                }
                return false;
            }
        });

        et_repin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin3.getText().length() == 1) {
                    et_repin4.requestFocus();
                } else {
                    et_repin3.requestFocus();
                }
                return false;
            }
        });

        et_repin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin4.getText().length() == 1 ) {
                     str_pin = et_pin1.getText().toString()+et_pin2.getText().toString()+et_pin3.getText().toString()+et_pin4.getText().toString();
                    str_repin = et_repin1.getText().toString()+et_repin2.getText().toString()+et_repin3.getText().toString()+et_repin4.getText().toString();
                    //Toast.makeText(getApplicationContext(),pin,Toast.LENGTH_LONG).show();
                    Log.d("tag",""+str_pin + str_repin);

                    submit.requestFocus();

                } else {
                    et_repin4.requestFocus();
                }
                return false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_pin == str_repin) {
                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                }
                else {
                    et_pin1.requestFocus();
                    Log.d("tag",str_pin + str_repin);
                }
            }
        });
    }
}
