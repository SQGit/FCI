package fci.com.fci;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Salman on 6/16/2016.
 */
public class StaffEdit extends Activity {

    ListView lv_staffsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staffedit);

        lv_staffsList = (ListView) findViewById(R.id.listview);

        AdapterStaffEdit staff_adapter = new AdapterStaffEdit(StaffEdit.this);

        lv_staffsList.setAdapter(staff_adapter);

    }
}
