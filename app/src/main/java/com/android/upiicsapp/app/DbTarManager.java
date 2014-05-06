package com.android.upiicsapp.app;

/**
 * Created by Administrador on 4/05/14.
 */
public class DbTarManager {
    public static final String TABLE_TAREAS = "tareas";
    public static final String CNT_ID = "_id";
    public static final String CNT_IDA = "id_alumno";
    public static final String CNT_TITLE = "titulo";
    public static final String CNT_DESC = "descripcion";
    public static final String FK_ALUM = "FOREIGN KEY (id_alumno) REFERENCES alumnos (_id)";

    public static final String CREATE_TABLE_TAREAS = "create table " + TABLE_TAREAS + " ("
            + CNT_ID + " integer primary key autoincrement, "
            + CNT_IDA + " integer not null, "
            + CNT_TITLE + " text not null, "
            + CNT_DESC + " text, "
            + FK_ALUM + " );";
}
