package com.noberto.br.ufrn.vendapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ActionBar ab = getActionBar();
        //ab.setTitle(R.string.titulo);
        //ab.setSubtitle(R.string.subtitulo);
        //ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        //ab.setIcon(R.mipmap.ic_launcher);
        //ab.setDisplayShowHomeEnabled(true);
        ConfigurarButoes();
    }

    private void ConfigurarButoes() {
        Button btCadastroCliente = (Button) findViewById(R.id.btCadastroCliente);
        btCadastroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarTelaCadastroCliente();
            }
        });

        Button btExibirCliente = (Button)findViewById(R.id.btRelatorioClientes);
        btExibirCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarTelaExibirCliente();
            }
        });
    }

    private void carregarTelaCadastroCliente() {
        Intent intent = new Intent(this, FormClienteActivity.class);
        startActivity(intent);
    }

    private void carregarTelaExibirCliente(){
        Intent it = new Intent(this, ExibirClientesActivity.class);
        startActivity(it);
    }
}
