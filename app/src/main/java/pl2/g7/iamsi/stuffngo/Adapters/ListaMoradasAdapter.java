package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.R;

public class ListaMoradasAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Morada> moradas;

    public ListaMoradasAdapter(Context context, ArrayList<Morada> moradas) {
        this.context = context;
        this.moradas = moradas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return moradas.size(); }

    @Override
    public Object getItem(int i) { return moradas.get(i); }

    @Override
    public long getItemId(int i) { return moradas.get(i).getId(); }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = layoutInflater.inflate(R.layout.item_lista_morada, null);
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(moradas.get(i));

        return view;

    }

    private static class ViewHolderLista
    {
        TextView tvMorada;

        public ViewHolderLista(View view)
        {
            tvMorada = view.findViewById(R.id.tvMorada);
        }

        public void update(Morada morada)
        {
            tvMorada.setText(morada.getRua() + ", " + morada.getCodPostal() + ", " + morada.getCidade() + ", " + morada.getPais());
        }
    }
}
