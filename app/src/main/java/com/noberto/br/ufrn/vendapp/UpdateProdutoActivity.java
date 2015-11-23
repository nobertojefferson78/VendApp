package com.noberto.br.ufrn.vendapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.app.ProdutoArrayAdapter;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;
import com.noberto.br.ufrn.vendapp.modelo.Produto;

import java.util.ArrayList;

public class UpdateProdutoActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private EditText edtQuntidade;
    private Button btAdicionarProduto;
    private AutoCompleteTextView autoTvProdutosUpdate;

    private ListView lstItens;
    private ArrayAdapter<ItemVenda> adpItens;
    private ArrayAdapter<Produto> adpProdutos;
    private ArrayList<ItemVenda> itensLista;
    private ArrayList<Produto> itensListaProdutos;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioProduto repositorioProduto;

    private ActionBar ab;
    private MenuItem m1;

    private Produto produto;
    private ItemVenda itemVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produto);

        this.conectarInterface();
        this.conectarBanco();

        produto = new Produto();
        itemVenda = new ItemVenda();
    }
    @Override
    public void onClick(View v) {
        produto = repositorioProduto.buscarPorNome(autoTvProdutosUpdate.getText().toString());
        itemVenda = new ItemVenda();
        adpProdutos = new ProdutoArrayAdapter(this, R.layout.lista_itens_produto);

        int quantidade = Integer.parseInt(edtQuntidade.getText().toString());

        if (produto != null && produto.getEstoque() >= quantidade){
            itensListaProdutos.add(produto);
            itemVenda.setProduto(produto);
            itemVenda.setQuant(quantidade);
            adpItens.add(itemVenda);
            lstItens.setAdapter(adpItens);
            //armazenar todos os itens da venda
            itensLista.add(itemVenda);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn != null){
            conn.close();
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
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        updateEstoque();
        return false;
    }

    public void conectarInterface() {
        ab = getSupportActionBar();
        ab.setTitle("Vendas");
        ab.setSubtitle("Cadastrar");
        ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayShowHomeEnabled(true);

        autoTvProdutosUpdate = (AutoCompleteTextView)findViewById(R.id.autoTvProdutosUpdate);
        edtQuntidade = (EditText)findViewById(R.id.edtQuantidadeUpdate);
        btAdicionarProduto = (Button)findViewById(R.id.btAdicionarProdutoUpdate);
        lstItens = (ListView)findViewById(R.id.lstItensUpdate);

        btAdicionarProduto.setOnClickListener(this);
        //lstItens.setOnItemClickListener(this);
    }
    public void conectarBanco() {
        try{
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioProduto = new RepositorioProduto(conn);
        }catch (SQLException ex){
            MensageBox.show(this, "Erro no banco: " + ex.getMessage(), "ERRO!");
        }
    }
    public void updateEstoque() {
        for(ItemVenda v: itensLista) {
            produto = new Produto();
            produto = v.getProduto();
            produto.setEstoque(produto.getEstoque()-v.getQuant());
            repositorioProduto.alterar(produto);
        }
    }
    public void excluirProduto(Produto p){
        adpProdutos.remove(p);
        lstItens.setAdapter(adpProdutos);
        itensListaProdutos.remove(p);
    }

}
