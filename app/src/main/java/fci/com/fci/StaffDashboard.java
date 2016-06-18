package fci.com.fci;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ramya on 10-06-2016.
 */
public class StaffDashboard extends AppCompatActivity {
    TextView addentry,viewentry,reports,setings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.staff_dashboard_1);
        addentry = (TextView) findViewById(R.id.addentry_et);
        viewentry = (TextView) findViewById(R.id.viewentry_et);
        reports = (TextView) findViewById(R.id.reports_et);
        setings = (TextView) findViewById(R.id.settings_et);

       /* addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),StaffCreate.class);
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
