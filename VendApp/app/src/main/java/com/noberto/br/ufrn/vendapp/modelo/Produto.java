package com.noberto.br.ufrn.vendapp.modelo;

import java.io.Serializable;

/**
 * Created by André on 15/10/2015.
 */
public class Produto implements Serializable {

    //variaveis usadas para nomear os nomes das colunas
    public static String TABELA = "PRODUTO";
    public static String ID = "_id";
    public static String REFERENCIA = "REFERENCIA";
    public static String NOME = "NOME";
    public static String VALOR = "VALOR";
    public static String ESTOQUE = "ESTOQUE";

    private long id;
    private String referencia, nome;
    private double valor;
    private int estoque;

    public Produto() {
        this.setId(0);
    }

    @Override
    public String toString() {
        return this.getReferencia() + " " + this.getNome() + " " + this.getValor();
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public String getReferencia() {
        return this.referencia;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return this.nome;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public double getValor() {
        return this.valor;
    }
    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    public int getEstoque() {
        return this.estoque;
    }

    public void incrementarEstoque(int valor) {
        this.setEstoque(this.getEstoque()+valor);
    }
    public void decrementarEstoque(int valor) {
        this.setEstoque(this.getEstoque()-valor);
    }

}
