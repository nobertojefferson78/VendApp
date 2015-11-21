package com.noberto.br.ufrn.vendapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noberto.br.ufrn.vendapp.app.Mask;
import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.Produto;


public class FormProdutoActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

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
        m1.setOnMenuItemClickListener(this);

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


    }
    public void preencheDados(){
        cpProdReferencia.setText(this.produto.getReferencia());
        cpProdNome.setText(this.produto.getNome());
        cpProdValor.setText(this.produto.getValor() + "");
        cpProdEstoque.setText(this.produto.getEstoque()+"");
    }

    private boolean validarCampos(){

        if(TextUtils.isEmpty(cpProdReferencia.getText().toString())){
            MensageBox.show(this, "Erro", "Campo Referencia vazio");
            return false;
        }

        if(TextUtils.isEmpty(cpProdNome.getText().toString())) {
            MensageBox.show(this, "Erro", "Campo nome vazio");
            return false;
        }

        if (TextUtils.isEmpty(cpProdEstoque.getText().toString())) {
            MensageBox.show(this, "Erro", "Campo Estoque vazio");
            return false;
        }

        if (TextUtils.isEmpty(cpProdValor.getText().toString())) {
            MensageBox.show(this, "Erro", "Campo Valor vazio");
            return false;
        }

        return true;
    }

    public void salvar() {
        if(validarCampos()) {
            try {
                produto.setReferencia(cpProdReferencia.getText().toString());
                produto.setNome(cpProdNome.getText().toString());
                produto.setValor(Double.parseDouble(cpProdValor.getText().toString()));
                produto.setEstoque(Integer.parseInt(cpProdEstoque.getText().toString()));

                if (produto.getId() == 0) {
                    repositorioProduto.inserir(produto);
                } else {
                    repositorioProduto.alterar(produto);
                }

            } catch (Exception e) {
                MensageBox.show(this, "Erro ao inserir produto: " + e.getMessage(), "ERRO!");
            }
            this.finish();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        salvar();
        return false;
    }
}
