package com.noberto.br.ufrn.vendapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noberto.br.ufrn.vendapp.R;
import com.noberto.br.ufrn.vendapp.modelo.Cliente;


/**
 * Created by PauloVinicius on 16/05/2015.
 */
public class ClienteArrayAdapter extends ArrayAdapter<Cliente>{

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ClienteArrayAdapter(Context context, int resource)
    {
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

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtCor = (TextView)view.findViewById(R.id.txtCor);
            viewHolder.txtNome = (TextView)view.findViewById(R.id.txtNome);
            viewHolder.txtTelefone = (TextView)view.findViewById(R.id.txtTelefone);

            view.setTag(viewHolder);

            convertView = view;

        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Cliente contato = getItem(position);

        viewHolder.txtNome.setText(contato.getNome());
        viewHolder.txtTelefone.setText(contato.getTelefone());

        return view;

    }

    static class ViewHolder
    {
        TextView txtCor;
        TextView txtNome;
        TextView txtTelefone;
    }
}