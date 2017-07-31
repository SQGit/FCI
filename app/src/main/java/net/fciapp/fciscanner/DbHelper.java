package net.fciapp.fciscanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SMK on 6/25/2016.
 */
public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, "fci", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("tagdb","dbcreated");
        db.execSQL("CREATE TABLE IF NOT EXISTS fci_data_entry(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,pos VARCHAR, vinno VARCHAR, make VARCHAR, st_g VARCHAR, ed_g VARCHAR, mva VARCHAR, mva_barcode VARCHAR, start VARCHAR, end VARCHAR  );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    protected void insertIntoDB2(int a, int b) {
        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        //String query = "INSERT INTO fci_data_entry (st_g) VALUES(\"" + b + "\") Where pos ="+a+";";
        String query = "UPDATE fci_data_entry set st_g = \"" + b + "\" where pos = " + a;
        Log.e("tag", "insertdb_start_guage  " + query);
        sdb1.execSQL(query);
    }


    protected void insertIntoDB3(int a, int b) {
        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        //String query = "INSERT INTO fci_data_entry (st_g) VALUES(\"" + b + "\") Where pos ="+a+";";
        String query = "UPDATE fci_data_entry set ed_g = \"" + b + "\" where pos = " + a;
        Log.e("tag", "insertdb_end_guage  " + query);
        sdb1.execSQL(query);
    }

    protected void insertIntoDB4(int a, int b) {
        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        try {
            String query = "UPDATE fci_data_entry set mva = \"" + b + "\" where pos = " + a;
            Log.e("tag", "insertdb_mva  " + query);
            sdb1.execSQL(query);
        }
        catch (Exception e){
            System.out.println("DATABASE ERROR " + e);
            if(e.toString().contains("no such column: mva (code 1):")){
                sdb1.execSQL("ALTER TABLE fci_data_entry ADD COLUMN mva VARCHAR");
                Log.e("tag", "ALTER TABLE");
            }
        }
    }


    public void update_mva(int position, String mva_barcode) {
        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            String query = "UPDATE fci_data_entry set  mva_barcode = \"" + mva_barcode + "\" where pos = " + position;
            Log.e("tag", "update mva  " + query);
            sdb1.execSQL(query);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
    }

    protected void insert_mva(int position, String mva_barcode) {
        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            String query = "INSERT INTO fci_data_entry (pos,mva_barcode) VALUES('" + position + "','" + mva_barcode + "');";
            Log.e("tag", "insert mva  " + query);
            sdb1.execSQL(query);
        } catch (Exception ee) {
            System.out.println("DATABASE ERROR " + ee);
        }
    }


    public void updateIntoDB(int position, String vin_no, String make, String start, String end, String s2, String s3, String mva) {

        //(pos,vinno,make,st_g,ed_g,start,end) VALUES('" +
        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            //String query = "INSERT INTO fci_data_entry (st_g) VALUES(\"" + b + "\") Where pos ="+a+";";
            String query = "UPDATE fci_data_entry set  vinno = \"" + vin_no + "\", make = \"" + make + "\", st_g = \"" + start + "\", ed_g = \"" + end + "\", mva = \"" + mva + "\" where pos = " + position;
            Log.e("tag", "insertdb_end_guage  " + query);
            sdb1.execSQL(query);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }

    }


    public void deletedata() {

        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            String query = "DELETE FROM fci_data_entry";

            sdb1.execSQL(query);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

    }


    protected void insertIntoDB(int a, String b, String c, String d, String e, String f, String g, String h) {
        Log.d("tag", "insertdb " + a + b + c + d + e + f + g);


        try {

            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();

            String query = "INSERT INTO fci_data_entry (pos,vinno,make,st_g,ed_g,start,end,mva) VALUES('" + a + "', '" + b + "', '" + c + "', '" + d + "', '" + e + "', '" + f + "', '" + g + "', '" + h + "');";
            Log.e("tag", "insertdb_end_guage  " + query);
            sdb1.execSQL(query);

        } catch (Exception ee) {
            System.out.println("DATABASE ERROR " + ee);

        }
    }


    public Cursor getFromDb() {
        String query = " SELECT distinct* FROM fci_data_entry";
        Cursor cursor = null;
        try {
            SQLiteDatabase sdb1;
            sdb1 = getReadableDatabase();
            cursor = sdb1.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

        return cursor;

    }


    protected void deleteDb() {
        Log.d("tag", "insertdb ");

        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        String query = " DELETE from fci_data_entry ";
        sdb1.execSQL(query);
    }


}
