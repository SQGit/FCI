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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminCreateCompany extends AppCompatActivity
{
    ImageView submit;
    TextView tv_header,tv_add_manag;
    EditText et_comp_name,et_comp_loc,et_manag_name,et_manag_phone;
    Typeface tf;
    LinearLayout lt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_create_company_1);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");



        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_add_manag = (TextView) findViewById(R.id.tv_add_manag);

        et_comp_name = (EditText) findViewById(R.id.et_comp_name);
        et_comp_loc = (EditText) findViewById(R.id.et_comp_location);
        et_manag_name = (EditText) findViewById(R.id.et_manager_name);
        et_manag_phone = (EditText) findViewById(R.id.et_manager_phone);

        submit=(ImageView) findViewById(R.id.submit_iv);
        lt_add = (LinearLayout) findViewById(R.id.layout_add);


        tv_header.setTypeface(tf);
        tv_add_manag.setTypeface(tf);
        et_comp_name.setTypeface(tf);
        et_comp_loc.setTypeface(tf);
        et_manag_name.setTypeface(tf);
        et_manag_phone.setTypeface(tf);






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(i);
            }
        });

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });

        lt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_manag_name.setText("");
                et_manag_phone.setText("");
                et_manag_name.requestFocus();
            }
        });


    }
}
