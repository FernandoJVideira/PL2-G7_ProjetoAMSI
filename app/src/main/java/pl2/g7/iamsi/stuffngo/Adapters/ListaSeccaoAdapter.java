package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Models.Seccao;

public class ListaSeccaoAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Seccao> seccoes;


    public ListaSeccaoAdapter(Context context, ArrayList<Seccao> seccoes){
        this.context = context;
        this.seccoes = seccoes;
    }

    @Override
    public int getCount() {
        return seccoes.size();
    }


    public Object getItem(int position) {
        return seccoes.get(position);
    }


    public long getItemId(int position) {
        return seccoes.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_seccao, null);

            ListaSeccaoAdapter.ViewHolderLista viewHolder = (ListaSeccaoAdapter.ViewHolderLista) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new ListaSeccaoAdapter.ViewHolderLista(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder.update(seccoes.get(position));
        }
        return convertView;
    }

    private class ViewHolderLista
    {
        private TextView tvNome;


        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNomeSeccao);
        }

        public void update(Seccao seccoes){
            tvNome.setText(seccoes.getNome());

        }
    }

}
