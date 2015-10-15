package com.noberto.br.ufrn.vendapp.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Paulo on 11/04/2015.
 */
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Paulo on 11/04/2015.
 */
public class Cliente implements Serializable{


    //variaveis usadas para nomear os nomes das colunas
    public static String TABELA = "CLIENTE";
    public static String ID = "_id";
    public static String NOME = "NOME";
    public static String CPF = "CPF";
    public static String TELEFONE = "TELEFONE";
    public static String EMAIL = "EMAIL";
    public static String DATANASCIMENTO = "DATANASCIMENTO";
    public static String SEXO = "SEXO";
    public static String PROMOCAO = "PROMOCAO";

    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Date dataNascimento;
    private String sexo;
    private String promocao;

    public Cliente()
    {
        id = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return nome + " " + cpf;
    }

    public String getPromocao() {
        return promocao;
    }

    public void setPromocao(String promocao) {
        this.promocao = promocao;
    }
}
