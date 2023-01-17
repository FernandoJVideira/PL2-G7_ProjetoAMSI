package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class ListaProdutosAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Produto> produtos;

    public ListaProdutosAdapter(Context context, ArrayList<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
        layoutInflater = LayoutInflater.from(context);
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


    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = layoutInflater.inflate(R.layout.item_lista_produtos, null);
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(produtos.get(i));

        return view;
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

        public void update(Produto produtos){
            tvNome.setText(produtos.getNome());
            tvPreco.setText(String.format("%s€", produtos.getPreco_unit()));
            Glide.with(context).load(Singleton.URL + "/common/Images/" + produtos.getImagem()).into(imgCapa);
        }
    }
}
