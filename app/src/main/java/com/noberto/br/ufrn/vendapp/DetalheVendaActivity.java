package com.noberto.br.ufrn.vendapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.app.ItemVendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.app.MensageBox;
import com.noberto.br.ufrn.vendapp.database.DataBase;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioCliente;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioItemVenda;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioProduto;
import com.noberto.br.ufrn.vendapp.dominio.RepositorioVenda;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

import java.text.SimpleDateFormat;

public class DetalheVendaActivity extends AppCompatActivity {
    TextView txtNomeClienteVenda, txtDataVendaDetalhe, txtValorVenda;
    private RepositorioVenda repositorioVenda;
    private RepositorioCliente repositorioCliente;
    private RepositorioProduto repositorioProduto;
    private RepositorioItemVenda repositorioItemVenda;
    private ArrayAdapter<ItemVenda> adpItemVendas;
    private ListView lstItensVendas;

    private DataBase dataBase;
    private SQLiteDatabase conn;

    private Venda venda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_venda);
        conectarInterface();
        conectarBanco();

        Bundle bundle = getIntent().getExtras();

        if((bundle != null) && (bundle.containsKey(ExibirVendasActivity.PAR_VENDA))){

            this.venda = (Venda)bundle.getSerializable(ExibirVendasActivity.PAR_VENDA);

        }else  { }

        atualizarCampos();

    }

    protected void conectarInterface(){
        txtNomeClienteVenda = (TextView)findViewById(R.id.txtNomeClienteVenda);
        txtDataVendaDetalhe = (TextView)findViewById(R.id.txtDataVendaDetalhe);
        txtValorVenda = (TextView)findViewById(R.id.txtValorDetalheVenda);
        lstItensVendas = (ListView)findViewById(R.id.lstProdutosAdquirods);

    }

    protected void conectarBanco(){
        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioVenda = new RepositorioVenda(conn);
            repositorioItemVenda = new RepositorioItemVenda(conn);
            repositorioCliente = new RepositorioCliente(conn);
            repositorioProduto = new RepositorioProduto(conn);



        }catch (SQLException ex){
            MensageBox.show(this, "Erro no banco: " + ex.getMessage(), "ERRO!");
        }
    }

    protected void atualizarCampos(){
        txtNomeClienteVenda.setText(venda.getCliente().getNome());
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formatador.format(venda.getDataVenda());

        txtDataVendaDetalhe.setText(dataFormatada);
        adpItemVendas = repositorioItemVenda.buscarItensVendaIdArray(this, venda.getId());
        lstItensVendas.setAdapter(adpItemVendas);
        txtValorVenda.setText(Double.toString(venda.getValorVenda()));

    }


}
