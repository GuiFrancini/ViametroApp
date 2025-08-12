package com.example.viametroone;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


public class Splash extends AppCompatActivity {
    private static final int SPLASH_TIME = 2500; // 2500 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(SPLASH_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Opcional: você pode atualizar algo na UI aqui
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }.start();

    }
}