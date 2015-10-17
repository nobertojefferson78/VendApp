package com.noberto.br.ufrn.vendapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.modelo.ItemVenda;

/**
 * Created by André on 16/10/2015.
 */
public class ItemVendaArrayAdapter extends ArrayAdapter<ItemVenda> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ItemVendaArrayAdapter(Context context, int resource) {
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

            viewHolder.txtItemVendaValor = (TextView)view.findViewById(R.id.txtCor);
            viewHolder.txtItemVendaProduto = (TextView)view.findViewById(R.id.txtNome);
            viewHolder.getTxtItemVendaQuantidade = (TextView)view.findViewById(R.id.txtTelefone);

            view.setTag(viewHolder);

            convertView = view;

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        ItemVenda itemVenda = getItem(position);

        viewHolder.txtItemVendaProduto.setText(itemVenda.getProduto().getNome());
        viewHolder.getTxtItemVendaQuantidade.setText(itemVenda.getQuant());

        return view;
    }

    static class ViewHolder {
        TextView txtItemVendaValor;
        TextView txtItemVendaProduto;
        TextView getTxtItemVendaQuantidade;
    }

}
