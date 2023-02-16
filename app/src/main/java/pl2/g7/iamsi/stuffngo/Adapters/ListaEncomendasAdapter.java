package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Encomenda;
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class ListaEncomendasAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Encomenda> encomendas;

    public ListaEncomendasAdapter(Context context, ArrayList<Encomenda> encomendas) {
        this.context = context;
        this.encomendas = encomendas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return encomendas.size(); }

    @Override
    public Object getItem(int i) { return encomendas.get(i); }

    @Override
    public long getItemId(int i) { return encomendas.get(i).getId(); }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = layoutInflater.inflate(R.layout.item_lista_encomendas, null);
        ListaEncomendasAdapter.ViewHolderLista viewHolder = (ListaEncomendasAdapter.ViewHolderLista) view.getTag();
        if(viewHolder == null) {
            viewHolder = new ListaEncomendasAdapter.ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(encomendas.get(i));

        return view;

    }

    private static class ViewHolderLista
    {
        TextView tvIdEncomenda, tvEstado, tvDataEncomenda, tvMorada;

        public ViewHolderLista(View view)
        {
            tvIdEncomenda = view.findViewById(R.id.tvIdEncomenda);
            tvEstado = view.findViewById(R.id.tvEstado);
            tvDataEncomenda = view.findViewById(R.id.tvDataEncomenda);
            tvMorada = view.findViewById(R.id.tvMorada);
        }

        public void update(Encomenda encomenda)
        {
            tvIdEncomenda.setText(String.valueOf(encomenda.getId()));
            tvEstado.setText(encomenda.getEstado());
            tvDataEncomenda.setText(encomenda.getData());
            Morada morada = Singleton.getInstance(tvMorada.getContext()).getMorada(encomenda.getIdMorada());
            if (morada != null)
                tvMorada.setText(morada.toString());
            else
                tvMorada.setText("Morada apagada");
        }
    }
}
