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

        db.execSQL("CREATE TABLE IF NOT EXISTS fci_entry(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,pos VARCHAR, vinno VARCHAR, make VARCHAR);");
    }

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


    public void deletedata(String qry2, String txt) {

        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            //sdb1.rawQuery(qry2,null);
            String query = "DELETE FROM cart WHERE id = " + txt;

            // sdb1.delete("cart","size =?", new String[] { txt });

            sdb1.execSQL(query);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

    }


    public void del_last_row() {

        try {
            SQLiteDatabase sdb1;
            sdb1 = getWritableDatabase();
            //sdb1.rawQuery(qry2,null);
            String query = "DELETE FROM cart WHERE id = (SELECT MAX(id) FROM cart);";

            // DELETE FROM test WHERE id = (SELECT MAX(id) FROM test);
            // sdb1.delete("cart","size =?", new String[] { txt });

            sdb1.execSQL(query);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);

        }

    }


    protected void insertIntoDB(int a, String b, String c) {
        Log.d("tag", "insertdb " + a + b + c);

        SQLiteDatabase sdb1;
        sdb1 = getWritableDatabase();

        String query = "INSERT INTO fci_entry (pos,vinno,make) VALUES('" + a + "', '" + b + "', '" + c + "');";
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


}
