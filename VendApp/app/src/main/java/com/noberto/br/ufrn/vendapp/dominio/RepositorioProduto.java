package com.noberto.br.ufrn.vendapp.dominio;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.noberto.br.ufrn.vendapp.modelo.Produto;

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
        connection.delete(Produto.TABELA, " _id = ? ", new String[]{String.valueOf( id )});
    }
}
