package com.noberto.br.ufrn.vendapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.app.ItemVendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.app.ViewHelper;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioItemVenda;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioVenda;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;
import com.noberto.br.ufrn.vendapp.modelo.Produto;
import com.noberto.br.ufrn.vendapp.modelo.Venda;
import com.noberto.br.ufrn.vendapp.negocio.Regras;
import com.noberto.br.ufrn.vendapp.util.DatesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FormVendaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    private EditText cpDataVenda, edtQuntidade;
    private TextView cpValorTotal;
    private Button btAdicionarProduto;

    private ListView lstItemVenda;
    private ArrayAdapter<ItemVenda> adpItemVendas;
    private Spinner spnCliente, spnProdutos;
    private ArrayAdapter<String> adaptadorClienteVenda;
    private ArrayList<Cliente> clienteLista;
    private ArrayList<Produto> produtoLista;
    private ArrayList<Produto> itensListaProdutos;
    private ArrayAdapter<String> getAdaptadorItemVenda;
    private ItemVendaArrayAdapter itemVendaArrayAdapter;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioVenda repositorioVenda;
    private RepositorioCliente repositorioCliente;
    private RepositorioProduto repositorioProduto;
    private RepositorioItemVenda repositorioItemVenda;
    private ArrayAdapter<Produto> adpProdutos;
    private ArrayAdapter<ItemVenda> getAdpItemVendas;

    private ActionBar ab;
    private MenuItem m1, m2;

    private Venda venda;
    private Produto produto;
    private ItemVenda itemVenda;
    private Cliente cliente;
    private int quantidadeTotal;
    private double valorVenda;
    private int quantidadePorProduto = 0;
    private Regras regras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_venda);
        conectarInterface();

//        Bundle bundle = getIntent().getExtras();
//
//        if((bundle != null) && (bundle.containsKey(ExibirProdutoVendaActivity.PAR_PRODUTO))){
//
//            this.produto = (Produto)bundle.getSerializable(ExibirProdutoVendaActivity.PAR_PRODUTO);
//
//        }else  { venda = new Venda(); produto = new Produto();}

        venda = new Venda();
        produto = new Produto();
        itemVenda = new ItemVenda();
        cliente = new Cliente();
        itensListaProdutos = new ArrayList<Produto>();
        regras = new Regras();

        conectarBanco();

        preencheSpinner();

    }

    public void conectarInterface(){
        ab = getSupportActionBar();
        ab.setTitle("Vendas");
        ab.setSubtitle("Cadastrar");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);

        spnCliente = (Spinner)findViewById(R.id.cpCompleteCliente);
        spnProdutos = (Spinner)findViewById(R.id.spnItenSelecionado);
        lstItemVenda = (ListView)findViewById(R.id.lstItemVenda);
        btAdicionarProduto = (Button)findViewById(R.id.btAdicionarProduto);
        edtQuntidade = (EditText)findViewById(R.id.edtQuantidade);
        cpValorTotal = (TextView)findViewById(R.id.cpValorTotal);


        btAdicionarProduto.setOnClickListener(this);
        lstItemVenda.setOnItemClickListener(this);

        cpDataVenda = (EditText)findViewById(R.id.cpDataVenda);

        ExibeDataListener edl = new ExibeDataListener();
        cpDataVenda.setOnClickListener(edl);
        cpDataVenda.setKeyListener(null);
    }

    public void conectarBanco(){
        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioVenda = new RepositorioVenda(conn);
            repositorioItemVenda = new RepositorioItemVenda(conn);
            repositorioCliente = new RepositorioCliente(conn);
            repositorioProduto = new RepositorioProduto(conn);

            adaptadorClienteVenda = ViewHelper.createArrayAdapter(this, spnCliente);
            getAdaptadorItemVenda = ViewHelper.createArrayAdapter(this, spnProdutos);


        }catch (SQLException ex){
            MensageBox.show(this, "Erro no banco: " + ex.getMessage(), "ERRO!");
        }
    }

    public void preencheSpinner(){
        clienteLista = repositorioCliente.buscaListaClientes(this);

        for(int i = 0; i < clienteLista.size(); i++){
            Cliente cliente = clienteLista.get(i);
            adaptadorClienteVenda.add(cliente.getNome());
        }

        produtoLista = repositorioProduto.buscaListaProdutos(this);

        for (int i = 0; i < produtoLista.size(); i++){
            Produto produto = produtoLista.get(i);
            getAdaptadorItemVenda.add(produto.getNome());
        }
    }

    public void salvar(){

        venda.setCliente(clienteLista.get(spnCliente.getSelectedItemPosition()));
        venda.setProdutos(itensListaProdutos);

        itemVenda.setVenda(this.venda);

        for(Produto p : itensListaProdutos){
            itemVenda.setProduto(p);
            venda.setValorVenda(p.getValor());
            //neste caso altero apenas a informação da quantidade de estoque para dizer quantos itens eu vendi
            itemVenda.setQuant(regras.getProduto().getEstoque());

            repositorioItemVenda.inserir(itemVenda);
        }

        repositorioVenda.inserir(venda);
    }

    private void exibeData(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(calendar.YEAR);
        int mes = calendar.get(calendar.MONTH);
        int dia = calendar.get(calendar.DAY_OF_MONTH);

        //usado para exibir o calendario
        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        //pegando o id do produto selecionado do spinner
        long id = spnProdutos.getSelectedItemId();

        //criei uma classe chamada regra para separar regras de negocio


        //quantidade alterada na regra de negocio para validar, esse pega la do campo de texto
        regras.setQuantidade(Integer.parseInt(edtQuntidade.getText().toString()));


        regras.setProduto(produtoLista.get((int) id));

        if(regras.produtoNoEstoque()) {

            //aqui tenho que guardar a quantidade por produto dentro da regra para salvar no itenVenda
            regras.getProduto().setEstoque(regras.getQuantidade());

            //usado para armazenar apenas os itens escolhidos
            itensListaProdutos.add(produtoLista.get((int) id));

            String auxiliar = Integer.toString(regras.calcularValorProdutoQuantidade());

            cpValorTotal.setText(auxiliar);

            //armazenando o valor total da venda;
            valorVenda += regras.calcularValorProdutoQuantidade();
            adpProdutos = repositorioProduto.buscarProdutoNome(this, regras.getProduto().getNome());
            lstItemVenda.setAdapter(adpProdutos);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Produto produto = adpProdutos.getItem(position);

        new AlertDialog.Builder(this).setMessage(R.string.mensagem_pergunta_editar).setCancelable(true)
                .setNegativeButton(getString(R.string.mensagem_editar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        m1 = menu.add(0, 0, 0, "Salvar");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //m1.setIcon(R.drawable.abc_ic_search_api_mtrl_alpha);
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

    public void excluirProduto(Produto p){
        adpProdutos.remove(p);
        lstItemVenda.setAdapter(adpProdutos);
        itensListaProdutos.remove(p);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        salvar();
        return false;
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                exibeData();
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dataFormatada = DatesUtil.dateToString(year, monthOfYear, dayOfMonth);
            Date data = DatesUtil.getDate(year, monthOfYear, dayOfMonth);

            cpDataVenda.setText(dataFormatada);
            venda.setDataVenda(data);
        }
    }

}
