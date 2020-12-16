package ir.allahverdi.digiapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.entity.Product;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "product_db";
    private static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Log.e(Const.TAG_DB, "Constructor()");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(Const.TAG_DB, "OnCreate()");

        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(Const.TAG_DB, "onUpgrade()");

        onCreate(db);

    }

    private void createTable(SQLiteDatabase db) {

        try {

            String query = "CREATE TABLE " + Product.class.getSimpleName() + "(\n" +
                    "   id INTEGER PRIMARY KEY,\n" +
                    "   name VARCHAR(255),\n" +
                    "   price INTEGER,\n" +
                    "   imgId VARCHAR(255),\n" +
                    "   model VARCHAR(255),\n" +
                    "   score REAL\n" +
                    ");";

            db.execSQL(query);

        } catch (Exception ex) {
        }

    /*
        just for test VERSION 2:

        try {
            String query = "ALTER TABLE "+Product.class.getSimpleName()+" ADD seller VARCHAR(50)";
            db.execSQL(query);
        } catch (Exception ex) {
        }
    */

    }// end createTable()
}
