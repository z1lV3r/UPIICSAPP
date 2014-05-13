package com.android.upiicsapp.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.nodes.Document;

/**
 * Created by Administrador on 1/05/14.
 */
public class Calificaciones extends Fragment{

    private String title;
    private TextView text;
    String boleta;
    String contra;
    GridView grid;
    ArrayAdapter<String> adapter;
    Handler handler = new Handler();
    HorizontalScrollView horizontalScrollView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calificaciones,container,false);
        Bundle data = this.getArguments();
        boleta = data.getString("boleta");
        contra = data.getString("contra");
        text = (TextView)view.findViewById(R.id.text);
        grid = (GridView)view.findViewById(R.id.gridView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hsv);
        horizontalScrollView.setVisibility(View.GONE);
        text.setText("Obteniendo calificaciones");
        progressBar.setProgress(10);
        if(Scraper.isOnline(getActivity())){
            new Thread(new Runnable() {
                public void run() {
                    //En esta sección realizar_todo el trabajo pesado, ya que es el comienzo de un nuevo hilo creado
                    Document doc;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(20);
                        }
                    });
                    Scraper.breakSSL();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(30);
                        }
                    });
                    Scraper scraper = new Scraper("https://www.saes.upiicsa.ipn.mx/default.aspx");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(50);
                        }
                    });
                    scraper.login(boleta,contra);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(70);
                        }
                    });
                    doc=scraper.sraping_to("https://www.saes.upiicsa.ipn.mx/Alumnos/Informacion_semestral/calificaciones_sem.aspx");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(80);
                        }
                    });
                    title=doc.title();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(100);
                        }
                    });
                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,scraper.getCalficaciones(doc));

                    //Mostrar los resultados.
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Como ya se vincularon los componentes, se podrá utilizar sin problemas.
                            //En esta parte siempre mostrar los resultados.
                            progressBar.setVisibility(View.GONE);
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            text.setText(title);
                            grid.setAdapter(adapter);
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
