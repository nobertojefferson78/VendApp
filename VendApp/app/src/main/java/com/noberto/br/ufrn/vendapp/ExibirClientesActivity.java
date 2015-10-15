package com.noberto.br.ufrn.vendapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExibirClientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText edtPesquisa;
    private ListView lstClientes;
    private ArrayAdapter<Cliente> adpClientes;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioCliente repositorioCliente;

    public static final String PAR_CLIENTE = "CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_clientes);

        edtPesquisa  = (EditText)findViewById(R.id.cpPesquisarClientes);
        lstClientes  = (ListView)findViewById(R.id.lstCliente);

        lstClientes.setOnItemClickListener(this);

        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioCliente = new RepositorioCliente(conn);

            adpClientes = repositorioCliente.buscaClientes(this);

            lstClientes.setAdapter(adpClientes);

            FiltraDados filtraDados = new FiltraDados(adpClientes);
            edtPesquisa.addTextChangedListener(filtraDados);


        }catch(SQLException ex)
        {
            MensageBox.show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null){
            conn.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       atualizarLista();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        final Cliente cliente = adpClientes.getItem(position);

        new AlertDialog.Builder(this).setMessage(R.string.mensagem_pergunta_editar).setCancelable(true)
                .setNegativeButton(getString(R.string.mensagem_editar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        editarCliente(view, cliente);
                    }

                })
                .setPositiveButton(getString(R.string.mensagem_excluir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        excluirCliente(cliente);
                    }

                })
                .show();

    }

    public void editarCliente(View view, Cliente cliente){
        Intent it = new Intent(this, FormClienteActivity.class);

        it.putExtra(PAR_CLIENTE, cliente);

        startActivityForResult(it, 0);
    }

    public void excluirCliente(Cliente cliente){
        repositorioCliente.excluir(cliente.getId());
        atualizarLista();
    }

    public void atualizarLista(){
        adpClientes = repositorioCliente.buscaClientes(this);

        lstClientes.setAdapter(adpClientes);


    }


    private class FiltraDados implements TextWatcher
    {

        private ArrayAdapter<Cliente> arrayAdapter;

        private FiltraDados(ArrayAdapter<Cliente> arrayAdapter)
        {
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
