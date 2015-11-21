package com.noberto.br.ufrn.vendapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Splash extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler tela = new Handler();
        tela.postDelayed((Runnable) this, Integer.parseInt(getString(R.string.tempoSplash)));
    }

    @Override
    public void run(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}