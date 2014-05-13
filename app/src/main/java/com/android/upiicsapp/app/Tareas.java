package com.android.upiicsapp.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Created by Administrador on 1/05/14.
 */
public class Tareas extends Fragment {

    Spinner spFechas;
    ListView lvTareas;
    ImageButton ibAdd;
    DbTarManager dbTarManager;

    SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas,container,false);
        spFechas = (Spinner) view.findViewById(R.id.spFechas);
        lvTareas = (ListView) view.findViewById(R.id.lvTareas);
        ibAdd = (ImageButton) view.findViewById(R.id.ibAdd);

        if(!DbHelper.existDB(getActivity().getApplicationContext())) {
            dbTarManager = new DbTarManager(getActivity().getApplicationContext());
            dbTarManager.insertar("Exposición de sistemas", "En rotafolio", "2014-05-11");
            dbTarManager.insertar("Ensayo de software", "Ensayo de la situación del software en México", "2014-05-11");
            dbTarManager.insertar("Ejercicios de proba", "Ejercicios 209-215 pag 105", "2014-05-14");
            dbTarManager.insertar("Estudiar estructuras", "estructuras basicas (for,while,if,switch...)", "2014-05-14");
            dbTarManager.insertar("Desarrollo de la creatividad", "Resumen de la unidad 3 del libro", "2014-05-14");
            dbTarManager.insertar("Exposición de software", "Instituto Iberoamericano", "2014-05-20");
            dbTarManager.insertar("Aplicación de sistemas", "Armado de circuitos con reloj y cuestionario", "2014-05-20");
            dbTarManager.insertar("Repaso de proba", "Integrales y diferenciales", "2014-05-21");
            dbTarManager.insertar("Investigar de POO", "Polimorfismo, clases, objetos", "2014-05-25");
            dbTarManager.insertar("Sociedad", "Leer primer capitulo del libro Ética", "2014-05-25");
            dbTarManager.insertar("POO", "Estudiar torres de hanoi", "2014-05-23");
            dbTarManager.insertar("Contabilidad", "Ir al museo MIDE", "2014-05-23");
            dbTarManager.insertar("Practica de discretas", null, "2014-05-28");
            dbTarManager.insertar("Proba", "Examen", "2014-05-28");
            dbTarManager.insertar("Comunicación", "Entregar ensayo de valor", "2014-05-29");
        }
        else {
            dbTarManager = new DbTarManager(getActivity().getApplicationContext());
        }
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdd(view);
            }
        });

        Cursor cursor = dbTarManager.obtenerFechas();
        if (cursor.moveToFirst()) {
            adapter = new SimpleCursorAdapter(
                    getActivity().getApplicationContext(),
                    R.layout.simple_sppiner_item,
                    cursor,
                    new String[]{DbTarManager.CN_DATE},
                    new int[]{android.R.id.text1});
            spFechas.setAdapter(adapter);
        }

        spFechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor= (Cursor) spFechas.getSelectedItem();
                int row=0;
                cursor.moveToFirst();
                while(row<i){
                    row++;
                    cursor.moveToNext();
                }
                String date = cursor.getString(cursor.getColumnIndex(DbTarManager.CN_DATE));
                Cursor cursorLv = dbTarManager.obtenerTareas(date);
                if (cursorLv.moveToFirst()) {
                    SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                            getActivity().getApplicationContext(),
                            R.layout.simple_list_item_2,
                            cursorLv,
                            new String[]{DbTarManager.CN_TITLE, DbTarManager.CN_DESC},
                            new int[]{android.R.id.text1, android.R.id.text2}
                    );
                    lvTareas.setAdapter(cursorAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickLvTareas(adapterView,view,i,l);
            }
        });

        return view;
    }

    public void clickLvTareas(AdapterView<?> adapterView, View view, int i, long l){
    }

    public void clickAdd(View view){
    }
}
