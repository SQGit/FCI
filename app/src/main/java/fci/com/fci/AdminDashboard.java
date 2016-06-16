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
public class AdminDashboard extends AppCompatActivity {
    EditText createstaff, editstaff, createcompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admindashboard);
        createstaff = (EditText) findViewById(R.id.createstaff_et);
        editstaff = (EditText) findViewById(R.id.editstaff_id);
        createcompany = (EditText) findViewById(R.id.createcompany_id);
        createstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),StaffCreate.class);
                startActivity(i);
            }
        });
        createcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),CreateCompany.class);
                startActivity(i);
            }
        });

        editstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),StaffEdit.class);
                startActivity(i);

            }
        });

    }
}
