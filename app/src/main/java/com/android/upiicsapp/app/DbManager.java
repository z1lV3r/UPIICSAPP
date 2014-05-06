package com.android.upiicsapp.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ruben on 13/04/2014.
 */
public class DbManager {
    public static final String TABLE_NAME = "contactos";
    public static final String CN_ID = "_id";
    public static final String CN_NAME = "nombre";
    public static final String CN_PHONE = "telefono";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NAME + " text not null,"
            + CN_PHONE + " text);";

    private DbHelper helper;
    private SQLiteDatabase db, dbr;

    public DbManager(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
        dbr = helper.getWritableDatabase();
    }

    private ContentValues generarContnentValues(String nombre, String telefono) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NAME, nombre);
        valores.put(CN_PHONE, telefono);
        return valores;
    }

    public void insertar(String nombre, String telefono) {
        db.insert(TABLE_NAME, null, generarContnentValues(nombre, telefono));
    }

    public void eliminar(String nombre) {
        db.delete(TABLE_NAME, CN_NAME + "=?", new String[]{nombre});
    }

    public void modificar(String nombre, String nuevoTelefono){
        db.update(TABLE_NAME, generarContnentValues(nombre,nuevoTelefono),CN_NAME+"=?",new String[]{nombre});
    }

    public void consultar(String nombre){
        dbr.execSQL("select "+CN_PHONE+" from "+TABLE_NAME+" where "+CN_NAME+"="+nombre+";");
    }
}