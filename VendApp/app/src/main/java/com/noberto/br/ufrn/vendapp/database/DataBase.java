package com.noberto.br.ufrn.vendapp.database;

import android.content.Context;
import android.database.sqlite.*;

/**
 * Created by jefferson on 28/06/2015.
 */
public class DataBase extends SQLiteOpenHelper{

    public static final String DB_CLIENTE = "CLIENTE";

    public DataBase(Context context){
        super(context, DB_CLIENTE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSQL.getCreateCliente());
        db.execSQL(ScriptSQL.getCreateProduto());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}