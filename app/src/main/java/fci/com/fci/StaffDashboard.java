package fci.com.fci;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ramya on 10-06-2016.
 */
public class StaffDashboard extends AppCompatActivity {
    TextView tv_header,addentry,viewentry,reports,setings;

    RelativeLayout lt_addentry,lt_viewentry,lt_settings;
    Typeface tf;

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

        tv_header.setTypeface(tf,1);
        addentry.setTypeface(tf);
        viewentry.setTypeface(tf);
        setings.setTypeface(tf);



        lt_addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dialog_ChooseCompany cdd = new Dialog_ChooseCompany(StaffDashboard.this);
               cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });

        lt_viewentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goEntry = new Intent(getApplicationContext(), StaffViewEntry.class);
                startActivity(goEntry);
            }
        });



    }
}
