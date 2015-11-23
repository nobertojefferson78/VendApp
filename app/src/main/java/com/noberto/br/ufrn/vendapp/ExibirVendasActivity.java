package com.noberto.br.ufrn.vendapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
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
import android.widget.SearchView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioItemVenda;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioVenda;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

public class ExibirVendasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {


    private ListView lstVenda;
    private ArrayAdapter<Venda> adpVendas;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioVenda repositorioVenda;
    private RepositorioItemVenda repositorioItemVenda;
    private ActionBar ab;
    private MenuItem m1, m2;

    public static final String PAR_VENDA = "VENDA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_vendas);


        lstVenda  = (ListView)findViewById(R.id.lstVenda);
        lstVenda.setOnItemClickListener(this);

        ab = getSupportActionBar();
        ab.setTitle("Vendas");
        ab.setSubtitle("lista");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);

        conectarBanco();
    }

    public void conectarBanco(){
        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioVenda = new RepositorioVenda(conn);
            repositorioItemVenda = new RepositorioItemVenda(conn);

            adpVendas = repositorioVenda.buscarVendas(this);
            lstVenda.setAdapter(adpVendas);

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

        final Venda venda = adpVendas.getItem(position);


        Intent it = new Intent(this, DetalheVendaActivity.class);
        it.putExtra(PAR_VENDA, venda);
        startActivityForResult(it, 0);

    }

    public void editarVenda(View view, Venda venda){
        Intent it = new Intent(this, FormVendaActivity.class);

        it.putExtra(PAR_VENDA, venda);

        startActivityForResult(it, 0);
    }

    public void excluirVenda(Venda venda){
        repositorioVenda.excluir(venda.getId());
        atualizarLista();
    }

    public void atualizarLista(){
        adpVendas = repositorioVenda.buscarVendas(this);

        lstVenda.setAdapter(adpVendas);
        SearchFiltro searchFiltro = new SearchFiltro(adpVendas);

        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(searchFiltro);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        conectarBanco();

        SearchFiltro searchFiltro = new SearchFiltro(adpVendas);

        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(searchFiltro);

        m1 = menu.add(0, 0, 0, "Pesquisar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m1.setActionView(sv);
        m1.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);

        m2 = menu.add(0, 0, 0, "Adicionar");
        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m2.setIcon(R.drawable.adicionarr);
        m2.setOnMenuItemClickListener(this);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_clientes, menu);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent it = new Intent(this, FormVendaActivity.class);
        startActivityForResult(it, 0);
        return false;
    }

    private class SearchFiltro implements SearchView.OnQueryTextListener {

        private ArrayAdapter<Venda> arrayAdapter;

        //inicializa com o construtor recebendo  o parametro do array que utilizou
        private SearchFiltro(ArrayAdapter<Venda> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        public void setArrayAdapter(ArrayAdapter<Venda> arrayAdapter){
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
