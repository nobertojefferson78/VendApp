package com.noberto.br.ufrn.vendapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.modelo.Produto;

/**
 * Created by André on 15/10/2015.
 */
public class ProdutoArrayAdapter extends ArrayAdapter<Produto> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ProdutoArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setResource(resource);
        this.setContext(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

           // viewHolder.txtProdCor = (TextView)view.findViewById(R.id.txtProdCor);
           // viewHolder.txtProdNome = (TextView)view.findViewById(R.id.txtProdNome);
           // viewHolder.txtProdReferencia = (TextView)view.findViewById(R.id.txtProdReferencia);

            view.setTag(viewHolder);

            convertView = view;
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Produto produto = getItem(position);

        viewHolder.txtProdNome.setText(produto.getNome());
        viewHolder.txtProdReferencia.setText(produto.getReferencia());

        return view;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
    public int getResource() {
        return this.resource;
    }
    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }
    public LayoutInflater getInflater() {
        return this.inflater;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return this.context;
    }

    static class ViewHolder {
        TextView txtProdCor, txtProdNome, txtProdReferencia;
    }
}
