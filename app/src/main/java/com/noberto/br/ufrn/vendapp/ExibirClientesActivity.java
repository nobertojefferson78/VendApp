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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioVenda;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

import java.util.ArrayList;

public class ExibirClientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {


    private static final int PICK_CONTACT_REQUEST = 0;
    private ListView lstClientes;
    private ArrayAdapter<Cliente> adpClientes;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioCliente repositorioCliente;
    private RepositorioVenda repositorioVenda;
    private ActionBar ab;
    private MenuItem m1, m2, editar, apagar;

    public static final String PAR_CLIENTE = "CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_clientes);


        lstClientes  = (ListView)findViewById(R.id.lstCliente);
        registerForContextMenu(lstClientes);
        lstClientes.setOnItemClickListener(this);

        ab = getSupportActionBar();
        ab.setTitle("Clientes");
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
            repositorioCliente = new RepositorioCliente(conn);
            repositorioVenda = new RepositorioVenda(conn);

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
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final Cliente cliente = adpClientes.getItem(info.position);

        editar = menu.add("Editar");
        apagar = menu.add("Apagar");

        editar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //ExibirVendasActivity.this.someFunctionInYourActivity();
                editarCliente(cliente);

                return true;
            }
        });
        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //ExibirVendasActivity.this.someFunctionInYourActivity();
                excluir(cliente);

                return true;
            }
        });;
    }

    public void excluir(final Cliente cliente1){
        new AlertDialog.Builder(this).setMessage("Excluir " + cliente1.getNome() + "?").setCancelable(true)
                .setNegativeButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        excluirCliente(cliente1);
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
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        final Cliente cliente = adpClientes.getItem(position);

    }

    public void editarCliente(Cliente cliente){
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

    public void clientesMaisAssiduos(){
        adpClientes = repositorioCliente.buscaClientesAssiduos(this);
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

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_clientes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.todosClientes) {
            clientesMaisAssiduos();
            return true;
        }else if(id == R.id.action_settings){
            atualizarLista();
            return true;
        }else if(id == R.id.cadastrarCliente){
            Intent it = new Intent(this, FormClienteActivity.class);
            startActivityForResult(it, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }

        atualizarLista();
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
