package com.noberto.br.ufrn.vendapp.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by André on 19/10/2015.
 */
public class Venda implements Serializable {

    //variaveis usadas para nomear os nomes das colunas
    public static String TABELA = "VENDA";
    public static String ID = "_id";
    public static String CLIENTE = "_id_CLIENTE";
    public static String DATA = "DATA";
    public static String VALORVENDA = "VALORVENDA";

    private long id;
    private Cliente cliente;
    private ArrayList<Produto> produtos = new ArrayList<Produto>();
    private Date dataVenda;
    private int quantidade;
    private double valorVenda;

    public Venda() {
        this.setId(0);
        valorVenda = 0;
    }

    public void addProduto(Produto produto) {
        this.getProdutos().add(produto);
    }
    public void removeItemVenda(Produto produto) {
        this.getProdutos().remove(produto);
    }

    public double calcularValorTotal() {
        int valor = 0;

        for(Produto i: produtos) {
            valor += (i.getValor()*this.getQuantidade());
        }
        return 0;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Cliente getCliente() {
        return this.cliente;
    }
    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }
    public List<Produto> getProdutos() {
        return this.produtos;
    }
    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }
    public Date getDataVenda() {
        return this.dataVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda += valorVenda;
    }
}
