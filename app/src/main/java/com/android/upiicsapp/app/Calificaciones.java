package com.android.upiicsapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.nodes.Document;

/**
 * Created by Administrador on 1/05/14.
 */
public class Calificaciones extends Fragment{

    private String title;
    //private TextView tit;*/
    private TextView text;
    GridView grid;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calificaciones,container,false);

        text = (TextView)view.findViewById(R.id.text);
        grid = (GridView)view.findViewById(R.id.gridView);
        if(Scraper.isOnline(getActivity())){
            new Thread(new Runnable() {
                public void run() {
                    //En esta sección realizar_todo el trabajo pesado, ya que es el comienzo de un nuevo hilo creado
                    Document doc;
                    Scraper.breakSSL();
                    Scraper scraper = new Scraper("https://www.saes.upiicsa.ipn.mx/default.aspx");
                    scraper.login("2014601987","159753");
                    doc=scraper.sraping_to("https://www.saes.upiicsa.ipn.mx/Alumnos/Informacion_semestral/calificaciones_sem.aspx");
                    title=doc.title();
                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,scraper.getCalficaciones(doc));

                    //Mostrar los resultados.
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Como ya se vincularon los componentes, se podrá utilizar sin problemas.
                            //En esta parte siempre mostrar los resultados.
                            text.setText(title);
                            grid.setAdapter(adapter);
                            Toast.makeText(getActivity(), "Tarea finalizada!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }
        else {
            Toast.makeText(getActivity(), "Verificar internet!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

}
