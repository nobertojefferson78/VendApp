package com.noberto.br.ufrn.vendapp.modelo;

import java.io.Serializable;

/**
 * Created by André on 16/10/2015.
 */
public class ItemVenda implements Serializable {

    //variaveis usadas para nomear os nomes das colunas
    public static String TABELA = "ITEM_VENDA";
    public static String ID = "_id";
    public static String PRODUTO = "_id_PRODUTO";
    public static String QUANTIDADE = "QUANTIDADE";

    private long id;
    private Produto produto;
    private int quant;

    public ItemVenda() {
        this.setId(0);
    }

    public double calcularValor() {
        return (this.getProduto().getValor()*this.getQuant());
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Produto getProduto() {
        return this.produto;
    }
    public void setQuant(int quant) {
        this.quant = quant;
    }
    public int getQuant() {
        return this.quant;
    }
}
