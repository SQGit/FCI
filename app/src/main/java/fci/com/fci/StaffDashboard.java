package fci.com.fci;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Ramya on 10-06-2016.
 */
public class StaffDashboard extends AppCompatActivity {
    EditText addentry,viewentry,reports,setings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staffdashboard);
        addentry = (EditText) findViewById(R.id.addentry_et);
        viewentry = (EditText) findViewById(R.id.viewentry_et);
        reports = (EditText) findViewById(R.id.reports_et);
        setings = (EditText) findViewById(R.id.settings_et);

       /* addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),CreateStaff.class);
                startActivity(i);
            }
        });
        viewentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),CreateCompany.class);
                startActivity(i);
            }
        });
*/
    }
}
