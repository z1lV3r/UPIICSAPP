package com.android.upiicsapp.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Administrador on 4/05/14.
 */
public class DbTarManager {
    public static final String TABLE_NAME = "tareas";
    public static final String CN_ID = "_id";
    public static final String CN_TITLE = "titulo";
    public static final String CN_DESC = "descripcion";
    public static final String CN_DATE = "fecha";

    public static final String CREATE_TABLE_TAREAS = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement, "
            + CN_TITLE + " text not null, "
            + CN_DESC + " text,"
            + CN_DATE + " text not null);";

    private ContentValues generarContnentValues(String titulo, String desc, String fecha) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TITLE, titulo);
        valores.put(CN_DESC, desc);
        valores.put(CN_DATE, fecha);
        return valores;
    }

    private DbTarHelper helper;
    private SQLiteDatabase db, dbr;

    public DbTarManager(Context context) {
        helper = new DbTarHelper(context);
        db = helper.getWritableDatabase();
        dbr = helper.getReadableDatabase();
    }

    public void insertar(String titulo, String desc, String fecha) {
        db.insert(TABLE_NAME, null, generarContnentValues(titulo, desc, fecha));
    }

    public void eliminar(String titulo,String fecha) {
        db.delete(TABLE_NAME, CN_TITLE + "=? AND "+ CN_DATE + "=?", new String[]{titulo,fecha});
    }

    public void modificar(String titulo, String fecha,String nuevoTitulo,String descripcion, String nuevaFecha){
        ContentValues valores = new ContentValues();
        valores.put(CN_TITLE, nuevoTitulo);
        valores.put(CN_DESC, descripcion);
        valores.put(CN_DATE, nuevaFecha);
        db.update(TABLE_NAME, valores,CN_TITLE+"=? AND "+ CN_DATE + "=?",new String[]{titulo,fecha});
    }

    public Cursor obtenerFechas(){
        return dbr.query(TABLE_NAME,new String[]{CN_ID,CN_DATE},null,null,CN_DATE,null,CN_DATE+" ASC",null);
    }

    public Cursor obtenerTareas(String fecha){
        return dbr.query(TABLE_NAME,new String[]{CN_ID,CN_TITLE,CN_DESC},CN_DATE+"=?",new String[]{fecha},null,null,null,null);
    }
}
