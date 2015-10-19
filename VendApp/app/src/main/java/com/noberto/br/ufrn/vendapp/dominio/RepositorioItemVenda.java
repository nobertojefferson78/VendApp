package com.noberto.br.ufrn.vendapp.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.app.ItemVendaArrayAdapter;
import com.noberto.br.ufrn.vendapp.app.ProdutoArrayAdapter;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;

/**
 * Created by André on 19/10/2015.
 */
public class RepositorioItemVenda {

    private SQLiteDatabase connection;

    public RepositorioItemVenda(SQLiteDatabase connection) {
        this.setConnection(connection);
    }

    public void setConnection(SQLiteDatabase connection) {
        this.connection = connection;
    }
    public SQLiteDatabase getConnection() {
        return this.connection;
    }

    private ContentValues preencheContentValues(ItemVenda itemVenda) {
        ContentValues values = new ContentValues();

        values.put(ItemVenda.PRODUTO, itemVenda.getProduto().getId());
        values.put(ItemVenda.QUANTIDADE, itemVenda.getQuant());

        return values;
    }
    public void inserir(ItemVenda itemVenda) {
        ContentValues values = preencheContentValues(itemVenda);
        connection.insertOrThrow(ItemVenda.TABELA, null, values);
    }
    public void alterar(ItemVenda itemVenda) {
        ContentValues values = preencheContentValues(itemVenda);
        connection.update(ItemVenda.TABELA, values, " _id = ? ", new String[]{String.valueOf(itemVenda.getId())});
    }
    public void excluir(long id) {
        connection.delete(ItemVenda.TABELA, " _id = ? ", new String[]{String.valueOf(id)});
    }
    public ItemVendaArrayAdapter buscarProdutos(Context context) {
        ItemVendaArrayAdapter itemVendaArrayAdapter = new ItemVendaArrayAdapter(context, R.layout.lista_itens_produto);
        Cursor cursor = connection.query(ItemVenda.TABELA, null, null, null, null, null, null);

        RepositorioProduto repositorioProduto = new RepositorioProduto(connection);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setId(cursor.getLong(cursor.getColumnIndex(ItemVenda.ID)));
                itemVenda.setProduto(repositorioProduto.buscarPorId(cursor.getLong(cursor.getColumnIndex(ItemVenda.PRODUTO))));
                itemVenda.setQuant(cursor.getInt(cursor.getColumnIndex(ItemVenda.QUANTIDADE)));

                itemVendaArrayAdapter.add(itemVenda);
            }while(cursor.moveToNext());
        }
        return itemVendaArrayAdapter;
    }
}
