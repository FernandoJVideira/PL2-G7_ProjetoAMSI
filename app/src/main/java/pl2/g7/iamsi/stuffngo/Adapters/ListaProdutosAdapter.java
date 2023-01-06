package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Produtos;
import pl2.g7.iamsi.stuffngo.R;

public class ListaProdutosAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Produtos> produtos;

    public ListaProdutosAdapter(Context context, ArrayList<Produtos> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }


    public Object getItem(int position) {
        return produtos.get(position);
    }


    public long getItemId(int position) {
        return produtos.get(position).getId();
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_lista_produtos, null);

            ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new ViewHolderLista(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder.update(produtos.get(position));
        }
        return convertView;
    }

    private class ViewHolderLista
    {
        private TextView tvNome, tvPreco;
        private ImageView imgCapa;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNome);
            tvPreco = view.findViewById(R.id.tvPreco);
            imgCapa = view.findViewById(R.id.imageView);
        }

        public void update(Produtos produtos){
            tvNome.setText(produtos.getNome());
            tvPreco.setText(produtos.getPreco_unit()+"€");
            imgCapa.setImageResource(produtos.getImagem());
        }
    }
}
