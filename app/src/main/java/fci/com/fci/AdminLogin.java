package fci.com.fci;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminLogin extends AppCompatActivity {
    ImageView submit;
    TextView tv_header;
    EditText et_phone, et_pass;
    Typeface tf;
    String str_phone, str_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_login_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        submit = (ImageView) findViewById(R.id.submit_iv);
        tv_header = (TextView) findViewById(R.id.tv_header);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pass = (EditText) findViewById(R.id.et_password);
        tv_header.setTypeface(tf);
        et_phone.setTypeface(tf);
        et_pass.setTypeface(tf);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_phone = et_phone.getText().toString();
                str_pass = et_pass.getText().toString();
                if (!(str_phone.isEmpty())) {
                    if (!(str_pass.isEmpty())) {
                        if ((str_phone.matches("12345") && str_pass.matches("admin"))) {
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Password not empty", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Phone not empty", Toast.LENGTH_LONG).show();

                }


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


    }
}
