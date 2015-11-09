package com.noberto.br.ufrn.vendapp.negocio;

import com.noberto.br.ufrn.vendapp.modelo.Cliente;
import com.noberto.br.ufrn.vendapp.modelo.Produto;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

/**
 * Created by jefferson on 08/11/2015.
 */
public class Regras {
    private Produto produto;
    private Venda venda;
    private Cliente cliente;
    private int quantidade;
    private int quantidadeTotal;

    public void Regras(){
        setProduto(new Produto());
        setVenda(new Venda());
        setCliente(new Cliente());
        setQuantidade(0);
    }


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public boolean produtoNoEstoque(){
        if(produto.getEstoque() >= quantidade && quantidade != 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean produtoValido(){
        if(produto != null){
            return true;
        }else
            return false;
    }

    public int calcularValorProdutoQuantidade(){
        return (int) (produto.getValor()* getQuantidade());
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
}
