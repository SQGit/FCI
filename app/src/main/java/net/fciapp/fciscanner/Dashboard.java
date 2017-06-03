package net.fciapp.fciscanner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 10-06-2016.
 */
public class Dashboard extends AppCompatActivity {
    LinearLayout admin, staff;
    TextView tv_admin, tv_staff;

    PendingIntent mPermissionIntent;
    UsbDevice device;
    UsbManager um;
    UsbInterface ui = null;
    UsbDeviceConnection con = null;

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard);
        admin = (LinearLayout) findViewById(R.id.admin_lv);
        staff = (LinearLayout) findViewById(R.id.staff_lv);

        tv_admin = (TextView) findViewById(R.id.tv_admin);
        tv_staff = (TextView) findViewById(R.id.tv_staff);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        tv_admin.setTypeface(tf);
        tv_staff.setTypeface(tf);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(i);
            }
        });
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StaffLogin.class);
                startActivity(i);

/*
                UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                while(deviceIterator.hasNext()){
                    UsbDevice devices = deviceIterator.next();
                    Log.e("tag", "device: " + devices.getDeviceName());
                    Log.e("tag", "devicemnf: " + devices.getManufacturerName());
                    Log.e("tag", "deviceid: " + devices.getDeviceId());
                    //your code

                    tv_admin.setText("device: " + devices.getDeviceName()+" devicemnf: " + devices.getManufacturerName()+" deviceid: " + devices.getDeviceId());

                    mPermissionIntent = PendingIntent.getBroadcast(Dashboard.this, 0, new Intent(
                            ACTION_USB_PERMISSION), 0);
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    registerReceiver(mUsbReceiver1, filter);

                    // desired interface for desired device
                   // ui = device.getInterface(0);
                  //  con.releaseInterface(ui);
                    // desired connection
                  //  con.close();

                    ui = devices.getInterface(0);
                    // desired connection
                    con = um.openDevice(devices);
            *//*        con.close();
                    con.claimInterface(ui, true);*//*
                    con.releaseInterface(ui);

                }*/


                //checkInfo();




            }
        });







/*
          um = (UsbManager) getSystemService(Context.USB_SERVICE);
        // hashmap will tel list of connected usb's


        final HashMap<String, UsbDevice> mydevicelist = um.getDeviceList();
        // iterator is made of usbDevices .. it one by one iterates through the list pf usb in loop
        Iterator<UsbDevice> myiterator = mydevicelist.values().iterator();

        while(myiterator.hasNext()){

            device = myiterator.next();
            // showing name of usb
            Toast.makeText(Dashboard.this,"NAME OF MY USB IS :" +device.getDeviceName(), Toast.LENGTH_SHORT).show();


        }
        // OR SIMPLY GET
        UsbDevice mydevice =mydevicelist.get("Devicename");
        // this is used to check whther permission is given to the attached device or not


        final String permission ="com.example.bsef10m057.USB_PERMISSION";
        final UsbInterface ui;
        final UsbDeviceConnection con;
        final BroadcastReceiver abcd = new BroadcastReceiver() {

            public void onReceive(Context context, Intent myintent) {
                String mypermission = myintent.getAction();
                if (permission.equals(mypermission)) {
                    synchronized (this) {
                        UsbDevice mydevice = (UsbDevice)myintent.getParcelableExtra(um.EXTRA_DEVICE);



                        //PERMISSION given
                        if (myintent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if(mydevice != null){


                                //Communication or data is being transferred

                                final byte[] abc={'r','a','a','a','p'};
                                final   UsbInterface ui;
                                final   UsbDeviceConnection con;

                                // desired interface for desired device
                                ui = mydevice.getInterface(0);
                                UsbEndpoint ep = ui.getEndpoint(0);

                                // desired connection
                                con = um.openDevice(mydevice);
                                con.claimInterface(ui, true);

                                // data transfer
                                con.bulkTransfer(ep, abc, abc.length,0);

                                Toast.makeText(Dashboard.this,"connected usb :" +device.getDeviceName()+ui.getId(), Toast.LENGTH_SHORT).show();
                                tv_admin.setText("device: "+mydevice);

                                BroadcastReceiver mUsbReceiver2 = new BroadcastReceiver()
                                {
                                    public void onReceive(Context context, Intent myintent)
                                    {
                                        String close = myintent.getAction();

                                        if (um.ACTION_USB_DEVICE_DETACHED.equals(close))
                                        {
                                            UsbDevice device = (UsbDevice)myintent.getParcelableExtra(um.EXTRA_DEVICE);
                                            if (device != null) {
                                                // call your method that cleans up and closes communication with the device


                                            }
                                        }
                                    }
                                };

                            }
                        }
                        else { // if user says no
                            tv_admin.setText("SORY!! no permission :( ");
                        }
                    }
                }
            }
        };                       // now in order to register broadcast receiver this is syntax
        PendingIntent  pi = PendingIntent.getBroadcast(Dashboard.this, 0, new Intent(permission), 0);
        IntentFilter iff = new IntentFilter(permission);
       // registerReceiver(abcd, iff);

        try {
            um.requestPermission(mydevice, pi) ;
        }
        catch (Exception e){
            tv_admin.setText("null point");
        }

        // to terminate established connection
        BroadcastReceiver efgh = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent myintent)
            {
                String close = myintent.getAction();

                if (um.ACTION_USB_DEVICE_DETACHED.equals(close))
                {
                    UsbDevice device = (UsbDevice)myintent.getParcelableExtra(um.EXTRA_DEVICE);
                    if (device != null) {
                        // call your method that cleans up and closes communication with the device
                        System.exit(0);

                        Toast.makeText(Dashboard.this,"detached:" +device.getDeviceName(), Toast.LENGTH_SHORT).show();
                        tv_admin.setText("device: "+device.getDeviceId());

                    }
                    else{
                        Toast.makeText(Dashboard.this,"device detached", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        // now in order to register broadcast receiver this is syntax
        pi = PendingIntent.getBroadcast(Dashboard.this, 0, new Intent(permission), 0);
        iff = new IntentFilter(permission);
        //registerReceiver(efgh, iff);


        try {
            um.requestPermission(mydevice, pi) ;
        }
        catch (Exception e){
            tv_admin.setText("null point");
        }


























*//*
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);


        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);*//*

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver1, filter);*/

    }


    /*private void checkInfo() {
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        *//*
         * this block required if you need to communicate to USB devices it's
         * take permission to device
         * if you want than you can set this to which device you want to communicate
         *//*
        // ------------------------------------------------------------------

        // -------------------------------------------------------------------
        HashMap<String , UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        String i = "";
        while (deviceIterator.hasNext()) {
            device = deviceIterator.next();
            manager.requestPermission(device, mPermissionIntent);
            i += "\n" + "DeviceID: " + device.getDeviceId() + "\n"
                    + "DeviceName: " + device.getDeviceName() + "\n"
                    + "DeviceClass: " + device.getDeviceClass() + " - "
                    + "DeviceSubClass: " + device.getDeviceSubclass() + "\n"
                    + "VendorID: " + device.getVendorId() + "\n"
                    + "ProductID: " + device.getProductId() + "\n";
        }

        tv_admin.setText(i);
    }*/


    private final BroadcastReceiver mUsbReceiver1 = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication
                            tv_admin.setText("permission for device " + device);
                            //checkInfo();
                        }
                    } else {
                        tv_admin.setText("ERROR permission denied for device " + device);
                    }
                }
            }
        }
    };


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        new SweetAlertDialog(Dashboard.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to exit the\nFCI?")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent i1 = new Intent(Intent.ACTION_MAIN);
                        i1.setAction(Intent.ACTION_MAIN);
                        i1.addCategory(Intent.CATEGORY_HOME);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);
                        Dashboard.this.finish();
                        ActivityCompat.finishAffinity(Dashboard.this);
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



    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                }
            }
        }
    };


}
