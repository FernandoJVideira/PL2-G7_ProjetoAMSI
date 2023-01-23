package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Views.HomeFragment;
import pl2.g7.iamsi.stuffngo.Views.MainActivity;

public class ListaProdutosAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Produto> produtos;
    HomeFragment homeFragment;

    public ListaProdutosAdapter(Context context, ArrayList<Produto> produtos, HomeFragment homeFragment) {
        this.context = context;
        this.produtos = produtos;
        this.homeFragment = homeFragment;
        layoutInflater = LayoutInflater.from(context);
        try {
            TextView tvNoProducts = homeFragment.getView().findViewById(R.id.tvNenhumProduto);

            if(produtos.size() == 0) {
                tvNoProducts.setVisibility(View.VISIBLE);
            }
            else
            {
                tvNoProducts.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
        }

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

        ImageButton cartButton = view.findViewById(R.id.btCart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance(context).addProdutoCarrinhoApi(produtos.get(i).getId(), 1);
            }
        });

        return view;
    }

    private class ViewHolderLista
    {
        private TextView tvNome, tvPreco;
        private ImageView imgCapa;
        private ImageButton btCart;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNome);
            tvPreco = view.findViewById(R.id.tvPreco);
            imgCapa = view.findViewById(R.id.imageView);
            btCart = view.findViewById(R.id.btCart);
            if(MainActivity.TOKEN == null){
                btCart.setImageAlpha(75);
                btCart.setEnabled(false);
            }

        }

        public void update(Produto produtos){
            tvNome.setText(produtos.getNome());
            tvPreco.setText(String.format("%s€", produtos.getPreco_unit()));
            Glide.with(context).load(Singleton.URL + "/common/Images/" + produtos.getImagem()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgCapa);
        }
    }
}


