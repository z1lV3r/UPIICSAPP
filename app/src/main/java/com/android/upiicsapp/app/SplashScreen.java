package com.android.upiicsapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.android.upiicsapp.app.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Context context = this;
        final Intent intent = new Intent("com.android.upiicsapp.app.Login");

        Thread verificar = new Thread(){
            public void run(){
                intent.putExtra("isOnline",Scraper.isOnline(context));
                intent.putExtra("existDB",DbHelper.existDB(context));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                finish();
            }
        };
        verificar.start();
    }
}
