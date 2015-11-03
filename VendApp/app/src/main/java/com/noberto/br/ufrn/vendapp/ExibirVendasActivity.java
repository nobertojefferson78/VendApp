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
import com.noberto.br.ufrn.vendapp.dominio.RepositorioItemVenda;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Produto;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

public class ExibirVendasActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    /*public static final String PAR_VENDAS = "VENDAS";

    //private EditText edtPesquisa;
    private ListView lstVendas;
    private ArrayAdapter<Produto> adpVendas;
    private DataBase dataBase;
    private SQLiteDatabase connection;
    private RepositorioItemVenda repositorioVenda;
    */

    private ActionBar ab;
    private MenuItem m1, m2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_vendas);

        //  edtPesquisa = (EditText) findViewById(R.id.cpPesquisarProdutos);
        //lstVendas = (ListView) findViewById(R.id.lstVendas);

        //lstVendas.setOnItemClickListener(this);

        ab = getSupportActionBar();
        //ab.setTitle("Vendas");
        //ab.setSubtitle("lista");
        //ab.setBackgroundDrawable(getResources().getDrawable(R.color.actionbar));

    }

    /*
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

        final Venda venda = adpVendas.getItem(position);

        new AlertDialog.Builder(this).setMessage(R.string.mensagem_pergunta_editar).setCancelable(true)
                .setNegativeButton(getString(R.string.mensagem_editar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editarVenda(view, venda);
                    }

                })
                .setPositiveButton(getString(R.string.mensagem_excluir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirVenda(venda);
                    }

                })
                .show();
    }

    public void editarVenda(View view, Venda venda) {
        Intent it = new Intent(this, FormProdutoActivity.class);
        it.putExtra(PAR_VENDAS, venda);
        startActivityForResult(it, 0);
    }
    public void excluirVenda(Venda venda) {
        repositorioVenda.excluir(venda.getId());
        atualizarLista();
    }

    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //conectarBanco();

        //SearchFiltro searchFiltro = new SearchFiltro(adpVendas);

        //SearchView sv = new SearchView(this);
        //sv.setOnQueryTextListener(searchFiltro);

        m1 = menu.add(0, 0, 0, "Pesquisar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //m1.setActionView(sv);
        m1.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);

        m2 = menu.add(0, 0, 0, "Adicionar");
        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m2.setIcon(R.drawable.adicionarr);
        m2.setOnMenuItemClickListener(this);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exibir_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent it = new Intent(this, FormVendaActivity.class);
        startActivityForResult(it, 0);
        return false;
    }

    /*

    public void atualizarLista() {
        adpVendas = repositorioVenda.buscarProdutos(this);
        lstVendas.setAdapter(adpVendas);
    }

    public void atualizarVenda(View view, Venda venda) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra(PAR_VENDAS, venda);
        startActivityForResult(intent, 0);
    }



    public void conectarBanco(){
        try {
            dataBase = new DataBase(this);
            connection = dataBase.getWritableDatabase();
            repositorioVenda = new RepositorioVenda(connection);

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
    }*/
}
