package com.noberto.br.ufrn.vendapp.database;

/**
 * Created by jeff_ on 14/10/2015.
 */
public class ScriptSQL {

    public static String getCreateCliente(){
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS CLIENTE (");
        sqlBuilder.append("_id INTEGER       NOT NULL");
        sqlBuilder.append("      PRIMARY KEY AUTOINCREMENT,");
        sqlBuilder.append("NOME               VARCHAR (255),");
        sqlBuilder.append("CPF               VARCHAR (14),");
        sqlBuilder.append("TELEFONE               VARCHAR (15),");
        sqlBuilder.append("EMAIL               VARCHAR (255),");
        sqlBuilder.append("DATANASCIMENTO      DATE,");
        sqlBuilder.append("SEXO               VARCHAR (10),");
        sqlBuilder.append("PROMOCAO             VARCHAR (10)");
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }
    public static String getCreateProduto() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS PRODUTO (");
        sqlBuilder.append("_id INTEGER NOT NULL");
        sqlBuilder.append(" PRIMARY KEY AUTOINCREMENT,");
        sqlBuilder.append("REFERENCIA VARCHAR(255),");
        sqlBuilder.append("NOME VARCHAR(255),");
        sqlBuilder.append("VALOR DOUBLE,");
        sqlBuilder.append("ESTOQUE INTEGER");
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }

    public static String getCreateItemVenda() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ITEM_VENDA (");
        sqlBuilder.append("_id INTEGER NOT NULL");
        sqlBuilder.append(" PRIMARY KEY AUTOINCREMENT,");
        sqlBuilder.append("_id_PRODUTO INTEGER NOT NULL,");
        sqlBuilder.append("_id_VENDA INTERGER NOT NULL,");
        sqlBuilder.append("QUANTIDADE INTEGER NOT NULL");
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }

    public static String getCreateVenda() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS VENDA (");
        sqlBuilder.append("_id INTEGER NOT NULL");
        sqlBuilder.append(" PRIMARY KEY AUTOINCREMENT,");
        sqlBuilder.append("_id_CLIENTE INTEGER NOT NULL,");
        sqlBuilder.append("DATA DATE NOT NULL");
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }

}
