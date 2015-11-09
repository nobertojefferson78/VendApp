package com.noberto.br.ufrn.vendapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.modelo.Venda;

import java.text.SimpleDateFormat;

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

            viewHolder.txtVendNomeCliente = (TextView)view.findViewById(R.id.txtVendCliente);
            viewHolder.txtVendData = (TextView)view.findViewById(R.id.txtVendData);
            viewHolder.txtVendValor = (TextView)view.findViewById(R.id.txtVendValor);

            view.setTag(viewHolder);

            convertView = view;

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Venda venda = getItem(position);

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/aaaa");
        String data = formatador.format(venda.getDataVenda());



        viewHolder.txtVendNomeCliente.setText(venda.getCliente().getNome());
        viewHolder.txtVendData.setText(data);
        viewHolder.txtVendValor.setText("R$ " + venda.getValorVenda());

        return view;
    }

    static class ViewHolder {
        TextView txtVendNomeCliente, txtVendData, txtVendValor;
    }

}
