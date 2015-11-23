package com.noberto.br.ufrn.vendapp.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.app.ItemVendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.app.VendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by norberto on 26/10/15.
 */
public class RepositorioVenda {

    private SQLiteDatabase connection, connection2;

    RepositorioCliente rpCliente;

    public RepositorioVenda(SQLiteDatabase connection) {
        this.setConnection(connection);
        rpCliente = new RepositorioCliente(connection2);
    }

    public void setConnection(SQLiteDatabase connection) {
        this.connection = connection;
        connection2 = connection;
    }
    public SQLiteDatabase getConnection() {
        return this.connection;
    }

    private ContentValues preencheContentValues(Venda venda) {
        ContentValues values = new ContentValues();

        values.put(Venda.CLIENTE, venda.getCliente().getId());
        values.put(Venda.DATA, venda.getDataVenda().getTime());
        values.put(Venda.VALORVENDA, venda.getValorVenda());

        return values;
    }

    private Venda setComponentes(Cursor cursor){
        Venda venda = new Venda();
        venda.setId(cursor.getLong(cursor.getColumnIndex(Venda.ID)));
        venda.setCliente(rpCliente.buscarPorId(cursor.getColumnIndex(Venda.CLIENTE)));
        venda.setValorVenda(cursor.getDouble(cursor.getColumnIndex(Venda.VALORVENDA)));
        venda.setDataVenda(new Date(cursor.getLong(cursor.getColumnIndex(Venda.DATA))));

        return venda;
    }

    public void inserir(Venda venda) {
        ContentValues values = preencheContentValues(venda);
        connection.insertOrThrow(Venda.TABELA, null, values);
    }


    public void alterar(Venda venda) {
        ContentValues values = preencheContentValues(venda);
        connection.update(Venda.TABELA, values, " _id = ? ", new String[]{String.valueOf(venda.getId())});
    }
    public void excluir(long id) {
        connection.delete(Venda.TABELA, " _id = ? ", new String[]{String.valueOf(id)});
    }
    public VendaArrayAdapter buscarVendas(Context context) {
        VendaArrayAdapter vendaArrayAdapter = new VendaArrayAdapter(context, R.layout.lista_itens_venda);
        Cursor cursor = connection.query(Venda.TABELA, null, null, null, null, null, null);



        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                Venda venda = setComponentes(cursor);

                vendaArrayAdapter.add(venda);
            }while(cursor.moveToNext());
        }
        return vendaArrayAdapter;
    }
    public Venda buscarPorId(long id) {

        Cursor cursor = connection.query(Venda.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            do{
                Venda venda = setComponentes(cursor);

                if(venda.getId() == id) {
                    return venda;
                }

            }while(cursor.moveToNext());
        }

        return null;
    }

    public Venda pegarUltimaVendaPorCliente(String nomeCliente){
        Cursor cursor = connection.query(Venda.TABELA, null, null, null, null, null, null);
        Venda venda = new Venda();
        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            do{

                venda = setComponentes(cursor);

                if (venda.getCliente().getNome().equalsIgnoreCase(nomeCliente)){
                    return venda;
                }
            }while (cursor.moveToNext());
        }

        return null;

    }

    public ArrayList<Venda> buscarTodasVendas() {

        ArrayList<Venda> vendas = new ArrayList<Venda>();
        Cursor cursor = connection.query(Venda.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            do{
                Venda venda = setComponentes(cursor);

                vendas.add(venda);
            }while(cursor.moveToNext());
        }

        return vendas;
    }


}
