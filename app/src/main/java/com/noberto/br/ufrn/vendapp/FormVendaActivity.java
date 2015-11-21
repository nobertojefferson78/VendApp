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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.app.ItemVendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.app.ProdutoArrayAdapter;
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
    private AutoCompleteTextView autoTvProdutosVenda, autoEtCliente;

    private ListView lstItemVenda;
    private ArrayAdapter<ItemVenda> adpItemVendas;
    private Spinner spnCliente, spnProdutos;
    private ArrayAdapter<String> adaptadorClienteVenda;
    private ArrayList<Cliente> clienteLista;
    private ArrayList<Produto> produtoLista;
    private ArrayList<ItemVenda> itemVendasLista;
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

    //usada para saber o valor total da venda;
    private double auxiliarPrecoVenda;


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
        itemVendasLista = new ArrayList<ItemVenda>();

        this.conectarBanco();
        this.preencheDados();

    }

    public void conectarInterface(){
        ab = getSupportActionBar();
        ab.setTitle("Vendas");
        ab.setSubtitle("Cadastrar");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);

        lstItemVenda = (ListView)findViewById(R.id.lstItemVenda);
        btAdicionarProduto = (Button)findViewById(R.id.btAdicionarProduto);
        edtQuntidade = (EditText)findViewById(R.id.edtQuantidade);
        cpValorTotal = (TextView)findViewById(R.id.cpValorTotal);
        autoTvProdutosVenda = (AutoCompleteTextView)findViewById(R.id.autoTvProdutosVenda);
        autoEtCliente = (AutoCompleteTextView)findViewById(R.id.autoEtCliente);

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

            //inicializando a lista de itensvenda
            adpItemVendas = new ItemVendaArrayAdapter(this, R.layout.lista_itens_itens);

        }catch (SQLException ex){
            MensageBox.show(this, "Erro no banco: " + ex.getMessage(), "ERRO!");
        }
    }

    public void preencheDados(){
        clienteLista = repositorioCliente.buscaListaClientes(this);
        produtoLista = repositorioProduto.buscaListaProdutos(this);


        ArrayList<String> nomesProdutos = repositorioProduto.buscaNomesProdutos(this);
        ArrayList<String> nomesClientes = repositorioCliente.buscaNomesClientes(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nomesProdutos);
        ArrayAdapter<String> adapterCliente = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nomesClientes);

        autoEtCliente.setAdapter(adapterCliente);
        autoTvProdutosVenda.setAdapter(adapter);

    }

    public void salvar(){
        //salve o cliente boy, lembre de pegar la do campo edo repositorio buscando pelo nome viu?
        cliente = repositorioCliente.buscarPorNome(autoEtCliente.getText().toString());
        venda.setCliente(cliente);
        venda.setValorVenda(auxiliarPrecoVenda);
        //aqui ja coloco o id correto pegando o id do ultimo produto inserido e depois adicionando +1 para poder salvar
        repositorioVenda.inserir(venda);
        //depois de salvar recupero a venda salva para pegar o id da venda realizada
        venda = repositorioVenda.pegarUltimaVendaPorCliente(cliente.getNome());

        //aqui salvo minha lista de itens com id da venda ja salva
        for(ItemVenda v : itemVendasLista){
            v.setVenda(venda);
            repositorioItemVenda.inserir(v);
        }

        updateEstoque(itemVendasLista);

        finish();
    }

    public void updateEstoque(ArrayList<ItemVenda> itensVenda) {
        Produto prod = new Produto();
        for(ItemVenda v: itensVenda) {
            prod = new Produto();
            prod = v.getProduto();
            prod.setEstoque(prod.getEstoque()-v.getQuant());
            repositorioProduto.alterar(prod);
        }
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
        produto = repositorioProduto.buscarPorNome(autoTvProdutosVenda.getText().toString());
        itemVenda = new ItemVenda();
        adpProdutos = new ProdutoArrayAdapter(this, R.layout.lista_itens_produto);


        int quantidade = Integer.parseInt(edtQuntidade.getText().toString());

        if (produto != null && produto.getEstoque() >= quantidade){
            itensListaProdutos.add(produto);
            itemVenda.setProduto(produto);
            itemVenda.setQuant(quantidade);
            adpItemVendas.add(itemVenda);
            lstItemVenda.setAdapter(adpItemVendas);
            //armazenar todos os itens da venda
            itemVendasLista.add(itemVenda);
            auxiliarPrecoVenda = calcularPreco(itemVenda);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn != null){
            conn.close();
        }
    }

    public double calcularPreco(ItemVenda itemVenda){
        String aux = cpValorTotal.getText().toString();
        double valorTotal;

        if(aux.equalsIgnoreCase("Valor")){
            valorTotal = itemVenda.calcularValor();

            cpValorTotal.setText(String.valueOf(valorTotal));
            return valorTotal;
        }else{
            double valorAnterior = Double.valueOf(aux);
            valorTotal = itemVenda.calcularValor();

            valorTotal += valorAnterior;

            cpValorTotal.setText(String.valueOf(valorTotal));
            return valorTotal;
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
