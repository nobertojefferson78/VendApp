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
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtProdNome = (TextView)view.findViewById(R.id.txtProdNome);
            viewHolder.txtProdReferencia = (TextView)view.findViewById(R.id.txtProdReferencia);
            viewHolder.txtProdValor = (TextView)view.findViewById(R.id.txtProdValor);
            viewHolder.txtProdQuantidade = (TextView)view.findViewById(R.id.txtProdQuantidade);


            view.setTag(viewHolder);

            convertView = view;
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Produto produto = getItem(position);

        viewHolder.txtProdNome.setText(produto.getNome());
        viewHolder.txtProdReferencia.setText(produto.getReferencia());
        viewHolder.txtProdValor.setText("R$ "+produto.getValor()+"");
        viewHolder.txtProdQuantidade.setText(""+produto.getEstoque());

        return view;
    }

    static class ViewHolder {
        TextView txtProdCor, txtProdNome, txtProdReferencia, txtProdValor, txtProdQuantidade;
    }
}
