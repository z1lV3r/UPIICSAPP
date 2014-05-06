package com.android.upiicsapp.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrador on 4/05/14.
 */
public class DbAluManager {
    public static final String TABLE_NAME = "alumnos";
    public static final String CN_ID = "_id";
    public static final String CN_BOLETA = "boleta";
    public static final String CN_PASS = "contra";

    public static final String CREATE_TABLE_ALUM = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_BOLETA + " text not null,"
            + CN_PASS + " text not null);";

    private ContentValues generarContnentValues(String boleta, String contra) {
        ContentValues valores = new ContentValues();
        valores.put(CN_BOLETA, boleta);
        valores.put(CN_PASS, contra);
        return valores;
    }

    private DbHelper helper;
    private SQLiteDatabase db, dbr;

    public DbAluManager(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
        dbr = helper.getReadableDatabase();
    }

    public void insertar(String boleta, String contra) {
        db.insert(TABLE_NAME, null, generarContnentValues(boleta, contra));
    }

    /*public void eliminar(String boleta) {
        db.delete(TABLE_NAME, CN_BOLETA + "=?", new String[]{boleta});
    }*/

    public void modificar(String boleta, String nuevaContra){
        db.update(TABLE_NAME, generarContnentValues(boleta,nuevaContra),CN_BOLETA+"=?",new String[]{boleta});
    }

    public Cursor consultar(String boleta){
        return dbr.query(TABLE_NAME,new String[]{CN_ID,CN_BOLETA,CN_PASS},CN_BOLETA+"=?",new String[]{boleta},null,null,null,null);
    }
}
