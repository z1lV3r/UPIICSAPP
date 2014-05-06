package com.android.upiicsapp.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by ruben on 13/04/2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static  final String DB_NAME = "users.sqlite";
    private  static  final  int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbAluManager.CREATE_TABLE_ALUM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    public static boolean existDB(Context context){
        String sFichero = context.getDatabasePath(DB_NAME).toString();
        File fichero = new File(sFichero);
        if (fichero.exists()) return true;
        return false;
    }
}
