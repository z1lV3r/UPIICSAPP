package com.android.upiicsapp.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.nodes.Document;

/**
 * Created by Administrador on 1/05/14.
 */
public class Horario extends Fragment{
	GridView HorarioG;
    ArrayAdapter<String> adapter;
    Handler handler = new Handler();
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario,container,false);
        HorarioG = (GridView)view.findViewById(R.id.GridHor);
        linearLayout = (LinearLayout) view.findViewById(R.id.llHor);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlHor);
        progressBar = (ProgressBar) view.findViewById(R.id.pbHorario);

        linearLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        if(Scraper.isOnline(getActivity())){
            Toast.makeText(getActivity().getApplicationContext(),"Obteniendo datos",Toast.LENGTH_LONG).show();
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
                    scraper.login("2014601810","250912");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(70);
                        }
                    });
                    doc=scraper.sraping_to("https://www.saes.upiicsa.ipn.mx/Alumnos/Informacion_semestral/Horario_Alumno.aspx");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(80);
                        }
                    });
                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,scraper.getHorario(doc));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(100);
                        }
                    });

                    //Mostrar los resultados.
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Como ya se vincularon los componentes, se podrá utilizar sin problemas.
                            //En esta parte siempre mostrar los resultados.
                            progressBar.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.VISIBLE);
                            HorarioG.setAdapter(adapter);
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
