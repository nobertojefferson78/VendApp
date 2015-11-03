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
import com.noberto.br.ufrn.vendapp.modelo.Cliente;

public class ExibirClientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    private EditText edtPesquisa;
    private ListView lstClientes;
    private ArrayAdapter<Cliente> adpClientes;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioCliente repositorioCliente;
    private ActionBar ab;
    private MenuItem m1, m2;

    public static final String PAR_CLIENTE = "CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_clientes);

        //edtPesquisa  = (EditText)findViewById(R.id.cpPesquisarClientes);
        lstClientes  = (ListView)findViewById(R.id.lstCliente);

        lstClientes.setOnItemClickListener(this);

        ab = getSupportActionBar();
        ab.setTitle("Clientes");
        ab.setSubtitle("lista");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.actionbar));

        conectarBanco();
    }

    public void conectarBanco(){
        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioCliente = new RepositorioCliente(conn);

            adpClientes = repositorioCliente.buscaClientes(this);

            lstClientes.setAdapter(adpClientes);

            //FiltraDados filtraDados = new FiltraDados(adpClientes);
            //edtPesquisa.addTextChangedListener(filtraDados);


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
        SearchFiltro searchFiltro = new SearchFiltro(adpClientes);

        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(searchFiltro);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        conectarBanco();

        SearchFiltro searchFiltro = new SearchFiltro(adpClientes);

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent it = new Intent(this, FormClienteActivity.class);
        startActivityForResult(it, 0);
        return false;
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

    private class SearchFiltro implements SearchView.OnQueryTextListener {

        private ArrayAdapter<Cliente> arrayAdapter;

        //inicializa com o construtor recebendo  o parametro do array que utilizou
        private SearchFiltro(ArrayAdapter<Cliente> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        public void setArrayAdapter(ArrayAdapter<Cliente> arrayAdapter){
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
