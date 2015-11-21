package com.noberto.br.ufrn.vendapp.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.app.ClienteArrayAdapter;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Paulo on 05/04/2015.
 */
public class RepositorioCliente {

    private SQLiteDatabase connection;

    public RepositorioCliente(SQLiteDatabase connection)
    {
        this.connection = connection;
    }


    private ContentValues preencheContentValues(Cliente cliente)
    {
        ContentValues values = new ContentValues();

        values.put(Cliente.NOME    , cliente.getNome());
        values.put(Cliente.CPF    , cliente.getCpf());
        values.put(Cliente.TELEFONE, cliente.getTelefone());
        values.put(Cliente.EMAIL    , cliente.getEmail());
        values.put(Cliente.DATANASCIMENTO, cliente.getDataNascimento().getTime());
        values.put(Cliente.SEXO, cliente.getSexo());
        values.put(Cliente.PROMOCAO, cliente.getPromocao());

        return values;

    }

    private Cliente setComponentes(Cursor cursor){
        Cliente cliente = new Cliente();
        cliente.setId(cursor.getLong(cursor.getColumnIndex(Cliente.ID)));
        cliente.setNome(cursor.getString(cursor.getColumnIndex(Cliente.NOME)));
        cliente.setCpf(cursor.getString(cursor.getColumnIndex(Cliente.CPF)));
        cliente.setTelefone(cursor.getString(cursor.getColumnIndex(Cliente.TELEFONE)));
        cliente.setEmail(cursor.getString(cursor.getColumnIndex(Cliente.EMAIL)));
        cliente.setDataNascimento(new Date(cursor.getLong(cursor.getColumnIndex(Cliente.DATANASCIMENTO))));
        cliente.setSexo(cursor.getString(cursor.getColumnIndex(Cliente.SEXO)));
        cliente.setPromocao(cursor.getString(cursor.getColumnIndex(Cliente.PROMOCAO)));

        return cliente;
    }

    public void excluir(long id)
    {
        connection.delete(Cliente.TABELA, " _id = ? ", new String[]{String.valueOf(id)});
    }

    public void alterar(Cliente cliente)
    {
        ContentValues values = preencheContentValues(cliente);
        connection.update(Cliente.TABELA, values, " _id = ? ", new String[]{String.valueOf(cliente.getId())});

    }

    public void inserir(Cliente cliente)
    {
        ContentValues values = preencheContentValues(cliente);
        connection.insertOrThrow(Cliente.TABELA, null, values);
    }


    public ClienteArrayAdapter buscaClientes(Context context)
    {
        ClienteArrayAdapter adpContatos = new ClienteArrayAdapter(context, R.layout.lista_itens );
        Cursor cursor  =  connection.query(Cliente.TABELA, null, null, null, null, null, null);

        if (cursor.getCount() > 0 )
        {
            cursor.moveToFirst();
            do {

                Cliente cliente = setComponentes(cursor);
                adpContatos.add(cliente);
            }while (cursor.moveToNext());
        }
        return adpContatos;
    }
    // teste de busca por assiduidade
    public ClienteArrayAdapter buscaClientesAssiduos(Context context)
    {
        ClienteArrayAdapter adpContatos = new ClienteArrayAdapter(context, R.layout.lista_itens );
        Cursor cursor  =  connection.rawQuery("SELECT * FROM CLIENTE as c, (SELECT _id_CLIENTE, COUNT(_id_CLIENTE) as count FROM VENDA ORDER BY count DESC) as v\n" +
                "WHERE c._id = v._id_CLIENTE;", null);

        if (cursor.getCount() > 0 )
        {
            cursor.moveToFirst();
            do {

                Cliente cliente = setComponentes(cursor);
                adpContatos.add(cliente);
            }while (cursor.moveToNext());
        }
        return adpContatos;
    }

    public Cliente buscarPorId(long id) {

        Cursor cursor = connection.query(Cliente.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            do{
                Cliente cliente = setComponentes(cursor);

                if(cliente.getId() == id) {
                    return cliente;
                }

            }while(cursor.moveToNext());
        }

        return null;
    }

    public Cliente buscarPorNome(String nome) {

        Cursor cursor = connection.query(Cliente.TABELA, null, null, null, null, null, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            do{
                Cliente cliente = setComponentes(cursor);

                if(cliente.getNome().equalsIgnoreCase(nome)) {
                    return cliente;
                }

            }while(cursor.moveToNext());
        }

        return null;
    }

    public ArrayList<Cliente> buscaListaClientes(Context context){
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();

        Cursor cursor = connection.query(Cliente.TABELA, null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            do{

                Cliente cliente = setComponentes(cursor);

                clientes.add(cliente);

            }while (cursor.moveToNext());
        }

        return clientes;
    }

    public ArrayList<String> buscaNomesClientes(Context context){
        ArrayList<String> clientes = new ArrayList<String>();

        Cursor cursor = connection.query(Cliente.TABELA, null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            do{

                Cliente cliente = setComponentes(cursor);

                clientes.add(cliente.getNome());

            }while (cursor.moveToNext());
        }

        return clientes;
    }


}