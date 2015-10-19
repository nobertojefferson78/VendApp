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

    private long id;
    private Cliente cliente;
    private ArrayList<ItemVenda> itensVenda;
    private Date dataVenda;

    public Venda() {
        this.setId(0);
    }

    public void addItemVenda(ItemVenda itemVenda) {
        this.getItensVenda().add(itemVenda);
    }
    public void removeItemVenda(ItemVenda itemVenda) {
        this.getItensVenda().remove(itemVenda);
    }

    public double calcularValorTotal() {
        int valor = 0;

        for(ItemVenda i: itensVenda) {
            valor+=i.calcularValor();
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
    public void setItensVenda(ArrayList<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
    }
    public List<ItemVenda> getItensVenda() {
        return this.itensVenda;
    }
    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }
    public Date getDataVenda() {
        return this.dataVenda;
    }

}
