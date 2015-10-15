package com.noberto.br.ufrn.vendapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.Produto;


public class FormProdutoActivity extends AppCompatActivity {

    private EditText cpProdReferencia, cpProdNome, cpProdValor, cpProdEstoque;
    private Button btSalvarProduto, btCancelarProduto;
    private DataBase dataBase;
    private SQLiteDatabase connection;
    private RepositorioProduto repositorioProduto;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);
        conectarInterface();
        Bundle bundle = getIntent().getExtras();
        if((bundle != null) && (bundle.containsKey(ExibirProdutosActivity.PAR_PRODUTO))){

            this.produto = (Produto)bundle.getSerializable(ExibirProdutosActivity.PAR_PRODUTO);
            preencheDados();

        }else  produto = new Produto();

        try {
            dataBase = new DataBase(this);
            connection = dataBase.getWritableDatabase();
            repositorioProduto = new RepositorioProduto(connection);
        }catch (SQLiteException e){
            MensageBox.show(this, "Erro no banco: " + e.getMessage(), "ERRO!");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection != null){
            connection.close();
        }
    }

    public void conectarInterface() {
        cpProdReferencia = (EditText) findViewById(R.id.cpProdReferencia);
        cpProdNome = (EditText) findViewById(R.id.cpProdNome);
        cpProdValor = (EditText) findViewById(R.id.cpProdValor);
        cpProdEstoque = (EditText) findViewById(R.id.cpProdEstoque);

        btSalvarProduto = (Button) findViewById(R.id.btSalvarProduto);
        btSalvarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
                finish();
            }
        });

        btCancelarProduto = (Button) findViewById(R.id.btCancelarProduto);
        btCancelarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }
    public void preencheDados(){
        cpProdReferencia.setText(this.produto.getReferencia());
        cpProdNome.setText(this.produto.getNome());
        cpProdValor.setText(this.produto.getValor()+"");
        cpProdEstoque.setText(this.produto.getEstoque()+"");
    }
    public void salvar() {
        try {
            produto.setReferencia(cpProdReferencia.getText().toString());
            produto.setNome(cpProdNome.getText().toString());
            produto.setValor(Double.parseDouble(cpProdValor.getText().toString()));
            produto.setEstoque(Integer.parseInt(cpProdEstoque.getText().toString()));

            if(produto.getId() == 0) {
                repositorioProduto.inserir(produto);
            }else{
                repositorioProduto.alterar(produto);
            }
            MensageBox.show(this,"Estou salvando o condenado: " + produto.getNome(), "Perfeito");
        } catch(Exception e) {
            MensageBox.show(this, "Erro ao inserir produto: " + e.getMessage(), "ERRO!");
        }
    }
}
