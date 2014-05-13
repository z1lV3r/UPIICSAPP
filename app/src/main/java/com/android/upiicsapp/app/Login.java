package com.android.upiicsapp.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.nodes.Document;


public class Login extends ActionBarActivity {

    EditText boleta;
    EditText pass;
    Button login;
    Cursor alumno;
    TextView tvBoleta;
    TextView tvContra;
    Thread thread;
    Button retry;
    ProgressBar progressBar;
    TextView tvProgress;
    boolean existDB;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();

        boleta = (EditText) findViewById(R.id.etBoleta);
        pass = (EditText) findViewById(R.id.etPass);
        login = (Button) findViewById(R.id.btLogin);
        retry = (Button) findViewById(R.id.btRetry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvBoleta = (TextView) findViewById(R.id.tvBoleta);
        tvContra = (TextView) findViewById(R.id.tvContra);

        progressBar.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);

        existDB=extras.getBoolean("existDB");
        if(extras.getBoolean("isOnline") || existDB){
            retry.setVisibility(View.GONE);
        }
        enableFocusable(extras.getBoolean("isOnline") || existDB);


    }

    private Thread inicializar(){
        thread = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(20);
                        tvProgress.setText("Pidiendo respuesta del servidor");
                    }
                });
                Scraper.breakSSL();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(30);
                        tvProgress.setText("Validando seguridad SSL");
                    }
                });
                Scraper scraper = new Scraper("https://www.saes.upiicsa.ipn.mx/default.aspx");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(50);
                        tvProgress.setText("Estableciendo conexion");
                    }
                });
                scraper.login(boleta.getText().toString(),pass.getText().toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(70);
                        tvProgress.setText("Enviando datos");
                    }
                });
                doc=scraper.sraping_to("https://www.saes.upiicsa.ipn.mx/alumnos/default.aspx");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(80);
                        tvProgress.setText("Comprobando informacion");
                    }
                });
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (doc.title().equals("Menú principal de alumnos")){
                            progressBar.setProgress(100);
                            tvProgress.setText("Verificacion exitosa");
                            DbAluManager dbAluManager = new DbAluManager(getApplicationContext());
                            dbAluManager.insertar(boleta.getText().toString(),pass.getText().toString());
                            Intent intent = new Intent("com.android.upiicsapp.app.Main");
                            intent.putExtra("boleta",boleta.getText().toString());
                            intent.putExtra("contra",pass.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            enableFocusable(Scraper.isOnline(getApplicationContext()));
                            Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                            tvContra.setVisibility(View.VISIBLE);
                            tvBoleta.setVisibility(View.VISIBLE);
                            login.setVisibility(View.VISIBLE);
                            boleta.setVisibility(View.VISIBLE);
                            pass.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);
                            tvProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        return thread;
    }

    private void enableFocusable(boolean activar){
        if (activar){
            boleta.setFocusableInTouchMode(true);
            pass.setFocusableInTouchMode(true);
            login.setClickable(true);
        }
        else {
            boleta.setFocusable(false);
            pass.setFocusable(false);
            login.setClickable(false);
            Toast.makeText(this,"Se necesita conexion a interntet para primera sincronizacion",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Document doc;
    public void clickLogin(View view){
        tvContra.setVisibility(View.GONE);
        tvBoleta.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        boleta.setVisibility(View.GONE);
        pass.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.VISIBLE);

        if (!existDB) {
            if (pass.getText().length() > 0 && boleta.getText().length() == 10) {
                if (Scraper.isOnline(this)) {
                    thread = inicializar();
                    thread.start();
                }
                else {
                    enableFocusable(Scraper.isOnline(this));
                    Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                    tvContra.setVisibility(View.VISIBLE);
                    tvBoleta.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);
                    boleta.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);
                    tvProgress.setVisibility(View.GONE);
                }
            }
            else {
                enableFocusable(Scraper.isOnline(this));
                Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                tvContra.setVisibility(View.VISIBLE);
                tvBoleta.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                boleta.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);
            }
        }
        else {
            if (pass.getText().length() > 0 && boleta.getText().length() == 10) {
                DbAluManager db = new DbAluManager(this);
                progressBar.setProgress(10);
                tvProgress.setText("Verificando en base de datos local");
                alumno = db.consultar(boleta.getText().toString());
                if (alumno.moveToFirst()) {
                    if (alumno.getString(alumno.getColumnIndex(DbAluManager.CN_PASS)).equals(pass.getText().toString())) {
                        Intent intent = new Intent("com.android.upiicsapp.app.Main");
                        intent.putExtra("boleta",boleta.getText().toString());
                        intent.putExtra("contra",pass.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        if (Scraper.isOnline(this)) {
                            thread = inicializar();
                            thread.start();
                        }
                        else {
                            enableFocusable(Scraper.isOnline(this));
                            Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                            tvContra.setVisibility(View.VISIBLE);
                            tvBoleta.setVisibility(View.VISIBLE);
                            login.setVisibility(View.VISIBLE);
                            boleta.setVisibility(View.VISIBLE);
                            pass.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);
                            tvProgress.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (Scraper.isOnline(this)) {
                        thread = inicializar();
                        thread.start();
                    }
                    else {
                        enableFocusable(Scraper.isOnline(this));
                        Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                        tvContra.setVisibility(View.VISIBLE);
                        tvBoleta.setVisibility(View.VISIBLE);
                        login.setVisibility(View.VISIBLE);
                        boleta.setVisibility(View.VISIBLE);
                        pass.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);
                        tvProgress.setVisibility(View.GONE);
                    }
                }
            }
            else{
                enableFocusable(Scraper.isOnline(this));
                Toast.makeText(getApplicationContext(),"ERROR de autentificación",Toast.LENGTH_SHORT).show();
                tvContra.setVisibility(View.VISIBLE);
                tvBoleta.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                boleta.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);
            }
        }
    }

    public void clickRetry(View view){
        enableFocusable(Scraper.isOnline(this));
    }
}
