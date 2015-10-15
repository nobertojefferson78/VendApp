package com.noberto.br.ufrn.vendapp.app;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by jefferson on 25/06/2015.
 */
public class MensageBox {

    public static void show(Context ctx, String title, String mensage){
        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setMessage(mensage);
        dlg.setNeutralButton("OK", null);
        dlg.setTitle(title);
        dlg.show();
    }

    public static void show(Context ctx, String title, String mensage, int idIcon){
        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setMessage(mensage);
        dlg.setNeutralButton("OK", null);
        dlg.setIcon(idIcon);
        dlg.setTitle(title);
        dlg.show();
    }
}
