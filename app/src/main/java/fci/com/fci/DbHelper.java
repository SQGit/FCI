package fci.com.fci;

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

        db.execSQL("CREATE TABLE IF NOT EXISTS fci_entry(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,pos VARCHAR, vinno VARCHAR, make VARCHAR, st_g VARCHAR, ed_g VARCHAR,start VARCHAR,end VARCHAR );");    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor fetchdata(String qry2) {
        Cursor c2 = null;
        try {
            SQLiteDatabase sdb1;
            sdb1 = getReadableDatabase();
            c2 = sdb1.rawQuery(qry2, null);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

        return c2;
    }

    protected void insertIntoDB2(int a, int b,String value) {

        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();

        //String query = "INSERT INTO fci_entry (st_g) VALUES(\"" + b + "\") Where pos ="+a+";";
        Log.d("tag", "vall12335"+ value);
        String query = "UPDATE fci_entry set st_g = \""+b+"\" AND start = \""+value+"\"where pos = "+a;
        Log.e("tag", "insertdb_start_guage  " +query);
        sdb1.execSQL(query);
    }


    protected void insertIntoDB3(int a, int b,String value) {
       // Log.d("tag", "insertdb " + a + b);
        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        //String query = "INSERT INTO fci_entry (st_g) VALUES(\"" + b + "\") Where pos ="+a+";";
        String query = "UPDATE fci_entry set ed_g = \""+b+"\" AND end = \""+value+"\"where pos = "+a;
        Log.e("tag", "insertdb_end_guage  " +query);
        sdb1.execSQL(query);

    }



    public void deletedata() {

        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            //sdb1.rawQuery(qry2,null);
            String query = "DELETE FROM fci_entry";

            sdb1.execSQL(query);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

    }


    public void deleteTable() {

        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            String query = "DELETE FROM cart WHERE id = (SELECT MAX(id) FROM cart);";



            sdb1.execSQL(query);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

    }


    protected void insertIntoDB(int a, String b, String c,String d,String e,String f,String g) {
        Log.d("tag", "insertdb " + a + b + c +d +e + f +g);

        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();

        String query = "INSERT INTO fci_entry (pos,vinno,make,st_g,ed_g,start,end) VALUES('" + a + "', '" + b + "', '" + c + "', '" + d + "', '" + e + "', '" + f + "', '" + g + "');";
        sdb1.execSQL(query);
    }


    public Cursor getFromDb() {

        String query = " SELECT * FROM fci_entry";

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
        String query = " DELETE from fci_entry ";
        sdb1.execSQL(query);
    }


    protected void delete() {
        Log.d("tag", "insertdb ");

        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();
        String query = " DELETE from fci_entry ";
        sdb1.execSQL(query);
    }


}
