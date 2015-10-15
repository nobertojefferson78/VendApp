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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.Produto;

public class ExibirProdutosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String PAR_PRODUTO = "PRODUTO";

    private EditText edtPesquisa;
    private ListView lstProdutos;
    private ArrayAdapter<Produto> adpProdutos;
    private DataBase dataBase;
    private SQLiteDatabase connection;
    private RepositorioProduto repositorioProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_produtos);

        edtPesquisa = (EditText) findViewById(R.id.cpPesquisarProdutos);
        lstProdutos = (ListView) findViewById(R.id.lstProduto);

        lstProdutos.setOnItemClickListener(this);

        try {
            dataBase = new DataBase(this);
            connection = dataBase.getWritableDatabase();
            repositorioProduto = new RepositorioProduto(connection);

            adpProdutos = repositorioProduto.buscarProdutos(this);
            lstProdutos.setAdapter(adpProdutos);

            FiltraDados filtraDados = new FiltraDados(adpProdutos);
            edtPesquisa.addTextChangedListener(filtraDados);
        }catch(SQLException e) {
            MensageBox.show(this, "Erro", "Erro ao criar o banco: " + e.getMessage());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null){
            connection.close();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        atualizarLista();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        final Produto produto = adpProdutos.getItem(position);

        new AlertDialog.Builder(this).setMessage(R.string.mensagem_pergunta_editar).setCancelable(true)
                .setNegativeButton(getString(R.string.mensagem_editar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editarProduto(view, produto);
                    }

                })
                .setPositiveButton(getString(R.string.mensagem_excluir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirProduto(produto);
                    }

                })
                .show();
    }

    public void editarProduto(View view, Produto produto) {
        Intent it = new Intent(this, FormProdutoActivity.class);
        it.putExtra(PAR_PRODUTO, produto);
        startActivityForResult(it, 0);
    }
    public void excluirProduto(Produto produto) {
        repositorioProduto.excluir(produto.getId());
        atualizarLista();
    }

    public void atualizarLista() {
        adpProdutos = repositorioProduto.buscarProdutos(this);
        lstProdutos.setAdapter(adpProdutos);
    }
    public void atualizarProduto(View view, Produto produto) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra(PAR_PRODUTO, produto);
        startActivityForResult(intent, 0);
    }
    private class FiltraDados implements TextWatcher {
        private ArrayAdapter<Produto> arrayAdapter;
        private FiltraDados(ArrayAdapter<Produto> arrayAdapter) {
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
