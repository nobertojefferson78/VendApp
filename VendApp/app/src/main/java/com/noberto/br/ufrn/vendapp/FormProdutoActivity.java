package com.noberto.br.ufrn.vendapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBar;
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

    private ActionBar ab;
    private MenuItem m1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);
        ab = getSupportActionBar();
        ab.setTitle("Produtos");
        ab.setSubtitle("Cadastrar");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);


        conectarInterface();
        Bundle bundle = getIntent().getExtras();
        if((bundle != null) && (bundle.containsKey(ExibirProdutosActivity.PAR_PRODUTO))){

            this.produto = (Produto)bundle.getSerializable(ExibirProdutosActivity.PAR_PRODUTO);
            ab.setSubtitle("Editar");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        m1 = menu.add(0, 0, 0, "Salvar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m1.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exibir_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
