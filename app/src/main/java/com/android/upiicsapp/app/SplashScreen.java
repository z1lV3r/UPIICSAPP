package com.android.upiicsapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
        Thread timer = new Thread(){

            public void run(){
                try {
                    sleep(10000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent("com.android.upiicsapp.app.Main");
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
