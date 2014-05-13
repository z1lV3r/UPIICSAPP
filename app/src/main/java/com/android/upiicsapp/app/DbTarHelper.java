package com.android.upiicsapp.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Administrador on 10/05/14.
 */
public class DbTarHelper extends SQLiteOpenHelper {

    private static  final String DB_NAME = "tasks.sqlite";
    private  static  final  int DB_SCHEME_VERSION = 1;

    public DbTarHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbTarManager.CREATE_TABLE_TAREAS);
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
