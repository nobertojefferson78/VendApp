package com.noberto.br.ufrn.vendapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

/**
 * Created by André on 19/10/2015.
 */
public class VendaArrayAdapter extends ArrayAdapter<Venda> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public VendaArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtProdNome = (TextView)view.findViewById(R.id.txtProdNome);
            viewHolder.txtProdQuantidade = (TextView)view.findViewById(R.id.txtProdQuantidade);
            viewHolder.txtProdValor = (TextView)view.findViewById(R.id.txtProdValor);

            view.setTag(viewHolder);

            convertView = view;

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Venda venda = getItem(position);

        viewHolder.txtProdNome.setText(venda.getCliente().getNome());
        //viewHolder.txtProdQuantidade.setText(venda.getQuant());
        //viewHolder.txtProdValor.setText("R$ " + itemVenda.calcularValor());

        return view;
    }

    static class ViewHolder {
        TextView txtProdNome, txtProdQuantidade, txtProdValor;
    }

}