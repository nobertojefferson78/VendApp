package com.noberto.br.ufrn.vendapp.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.app.ProdutoArrayAdapter;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Produto;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by André on 15/10/2015.
 */
public class RepositorioProduto {

    private SQLiteDatabase connection;

    public RepositorioProduto(SQLiteDatabase connection) {
        this.setConnection(connection);
    }

    public void setConnection(SQLiteDatabase connection) {
        this.connection = connection;
    }
    public SQLiteDatabase getConnection() {
        return this.connection;
    }

    private ContentValues preencheContentValues(Produto cliente) {
        ContentValues values = new ContentValues();

        values.put(Produto.REFERENCIA, cliente.getReferencia());
        values.put(Produto.NOME, cliente.getNome());
        values.put(Produto.VALOR, cliente.getValor());
        values.put(Produto.ESTOQUE, cliente.getEstoque());

        return values;
    }

    public void inserir(Produto produto) {
        ContentValues values = preencheContentValues(produto);
        connection.insertOrThrow(Produto.TABELA, null, values);
    }
    public void alterar(Produto produto) {
        ContentValues values = preencheContentValues(produto);
        connection.update(Produto.TABELA, values, " _id = ? ", new String[]{String.valueOf(produto.getId())});
    }
    public void excluir(long id) {
        connection.delete(Produto.TABELA, " _id = ? ", new String[]{String.valueOf(id)});
    }
    public ProdutoArrayAdapter buscarProdutos(Context context) {
        ProdutoArrayAdapter produtoArrayAdapter = new ProdutoArrayAdapter(context, R.layout.lista_itens_produto);
        Cursor cursor = connection.query(Produto.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                Produto produto = new Produto();
                produto.setId(cursor.getLong(cursor.getColumnIndex(Produto.ID)));
                produto.setReferencia(cursor.getString(cursor.getColumnIndex(Produto.REFERENCIA)));
                produto.setNome(cursor.getString(cursor.getColumnIndex(Produto.NOME)));
                produto.setValor(cursor.getDouble(cursor.getColumnIndex(Produto.VALOR)));
                produto.setEstoque(cursor.getInt(cursor.getColumnIndex(Produto.ESTOQUE)));

                produtoArrayAdapter.add(produto);
            }while(cursor.moveToNext());
        }
        return produtoArrayAdapter;
    }

    public ProdutoArrayAdapter buscarProdutoNome(Context context, String nome) {
        ProdutoArrayAdapter produtoArrayAdapter = new ProdutoArrayAdapter(context, R.layout.lista_itens_produto);
        Cursor cursor = connection.query(Produto.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                Produto produto = new Produto();
                produto.setId(cursor.getLong(cursor.getColumnIndex(Produto.ID)));
                produto.setReferencia(cursor.getString(cursor.getColumnIndex(Produto.REFERENCIA)));
                produto.setNome(cursor.getString(cursor.getColumnIndex(Produto.NOME)));
                produto.setValor(cursor.getDouble(cursor.getColumnIndex(Produto.VALOR)));
                produto.setEstoque(cursor.getInt(cursor.getColumnIndex(Produto.ESTOQUE)));

                if(produto.getNome().equalsIgnoreCase(nome)) {
                    produtoArrayAdapter.add(produto);
                }
            }while(cursor.moveToNext());
        }
        return produtoArrayAdapter;
    }


    public Produto buscarPorId(long id) {

        Cursor cursor = connection.query(Produto.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            do{
                Produto produto = new Produto();
                produto.setId(cursor.getLong(cursor.getColumnIndex(Produto.ID)));
                produto.setReferencia(cursor.getString(cursor.getColumnIndex(Produto.REFERENCIA)));
                produto.setNome(cursor.getString(cursor.getColumnIndex(Produto.NOME)));
                produto.setValor(cursor.getDouble(cursor.getColumnIndex(Produto.VALOR)));
                produto.setEstoque(cursor.getInt(cursor.getColumnIndex(Produto.ESTOQUE)));

                if(produto.getId() == id) {
                    return produto;
                }

            }while(cursor.moveToNext());
        }

        return null;
    }

    public ArrayList<Produto> buscaListaProdutos(Context context){
        ArrayList<Produto> produtos = new ArrayList<Produto>();

        Cursor cursor = connection.query(Produto.TABELA, null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            do{

                Produto produto = new Produto();
                produto.setId(cursor.getLong(cursor.getColumnIndex(Produto.ID)));
                produto.setReferencia(cursor.getString(cursor.getColumnIndex(Produto.REFERENCIA)));
                produto.setNome(cursor.getString(cursor.getColumnIndex(Produto.NOME)));
                produto.setValor(cursor.getDouble(cursor.getColumnIndex(Produto.VALOR)));
                produto.setEstoque(cursor.getInt(cursor.getColumnIndex(Produto.ESTOQUE)));

                produtos.add(produto);

            }while (cursor.moveToNext());
        }

        return produtos;
    }

    public ArrayList<String> buscaNomesProdutos(Context context){
        ArrayList<String> produtos = new ArrayList<String>();

        Cursor cursor = connection.query(Produto.TABELA, null, null, null, null, null, null);

        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            Produto produto;
            do{
                produto = new Produto();
                produto.setId(cursor.getLong(cursor.getColumnIndex(Produto.ID)));
                produto.setReferencia(cursor.getString(cursor.getColumnIndex(Produto.REFERENCIA)));
                produto.setNome(cursor.getString(cursor.getColumnIndex(Produto.NOME)));
                produto.setValor(cursor.getDouble(cursor.getColumnIndex(Produto.VALOR)));
                produto.setEstoque(cursor.getInt(cursor.getColumnIndex(Produto.ESTOQUE)));

                produtos.add(produto.getNome());

            }while (cursor.moveToNext());
        }

        return produtos;
    }
}
