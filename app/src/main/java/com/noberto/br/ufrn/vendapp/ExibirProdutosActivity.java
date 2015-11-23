package com.noberto.br.ufrn.vendapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Produto;

public class ExibirProdutosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    public static final String PAR_PRODUTO = "PRODUTO";

    //private EditText edtPesquisa;
    private ListView lstProdutos;
    private ArrayAdapter<Produto> adpProdutos;
    private DataBase dataBase;
    private SQLiteDatabase connection;
    private RepositorioProduto repositorioProduto;
    private ActionBar ab;
    private MenuItem m1, m2, editar, apagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_produtos);


        lstProdutos = (ListView) findViewById(R.id.lstProduto);
        registerForContextMenu(lstProdutos);

        //lstProdutos.setOnItemClickListener(this);

        ab = getSupportActionBar();
        ab.setTitle("Produtos");
        ab.setSubtitle("Lista");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);
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
    }

    public void editarProduto(Produto produto) {
        Intent it = new Intent(this, FormProdutoActivity.class);
        it.putExtra(PAR_PRODUTO, produto);
        startActivityForResult(it, 0);
    }
    public void excluirProduto(Produto produto) {
        repositorioProduto.excluir(produto.getId());
        atualizarLista();
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final Produto produto = adpProdutos.getItem(info.position);

        editar = menu.add("Editar");
        apagar = menu.add("Apagar");

        editar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //ExibirVendasActivity.this.someFunctionInYourActivity();
                editarProduto(produto);

                return true;
            }
        });
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //ExibirVendasActivity.this.someFunctionInYourActivity();
                excluir(produto);

                return true;
            }
        });;
    }

    public void excluir(final Produto produto){
        new AlertDialog.Builder(this).setMessage("Excluir " + produto.getNome() + "?").setCancelable(true)
                .setNegativeButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        excluirProduto(produto);
                    }

                })
                .setPositiveButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        conectarBanco();

        SearchFiltro searchFiltro = new SearchFiltro(adpProdutos);

        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(searchFiltro);

        m1 = menu.add(0, 0, 0, "Pesquisar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m1.setActionView(sv);
        m1.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cadastrarProduto) {
            Intent it = new Intent(this, FormProdutoActivity.class);
            startActivityForResult(it, 0);
            return true;
        } else if(id == R.id.produtoMaisVendido){
            return true;
        } else if(id == R.id.todosProdutos){
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent it = new Intent(this, FormProdutoActivity.class);
        startActivityForResult(it, 0);
        return false;
    }

    public void conectarBanco(){
        try {
            dataBase = new DataBase(this);
            connection = dataBase.getWritableDatabase();
            repositorioProduto = new RepositorioProduto(connection);

            adpProdutos = repositorioProduto.buscarProdutos(this);
            lstProdutos.setAdapter(adpProdutos);

        }catch(SQLException e) {
            MensageBox.show(this, "Erro", "Erro ao criar o banco: " + e.getMessage());
        }
    }

    private class SearchFiltro implements SearchView.OnQueryTextListener {

        private ArrayAdapter<Produto> arrayAdapter;

        //inicializa com o construtor recebendo  o parametro do array que utilizou
        private SearchFiltro(ArrayAdapter<Produto> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        public void setArrayAdapter(ArrayAdapter<Produto> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            arrayAdapter.getFilter().filter(newText);
            return false;
        }
    }
}
