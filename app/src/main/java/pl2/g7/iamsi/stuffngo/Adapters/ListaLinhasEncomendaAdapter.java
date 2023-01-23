package pl2.g7.iamsi.stuffngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.LinhaCarrinho;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class ListaLinhasEncomendaAdapter extends BaseAdapter {


    Context context;
    LayoutInflater layoutInflater;
    ArrayList<LinhaCarrinho> linha;

    public ListaLinhasEncomendaAdapter(Context context, ArrayList<LinhaCarrinho> linha) {
        this.context = context;
        this.linha = linha;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return linha.size(); }

    @Override
    public Object getItem(int i) { return linha.get(i); }

    @Override
    public long getItemId(int i) { return linha.get(i).getId(); }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = layoutInflater.inflate(R.layout.item_lista_linhas_encomendas, null);
        ListaLinhasEncomendaAdapter.ViewHolderLista viewHolder = (ListaLinhasEncomendaAdapter.ViewHolderLista) view.getTag();
        if(viewHolder == null) {
            viewHolder = new ListaLinhasEncomendaAdapter.ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(linha.get(i),context);

        return view;

    }

    private static class ViewHolderLista
    {
        TextView tvProdutoLinha, tvQuantidade;

        public ViewHolderLista(View view)
        {

           tvProdutoLinha = view.findViewById(R.id.tvProdutoLinha);
           tvQuantidade = view.findViewById(R.id.tvQuantidade);
        }

        public void update(LinhaCarrinho linhaCarrinho,Context context)
        {
            //get the product name using linhaCarrinho.getIdProduto()
            String productName = getProductName(linhaCarrinho.getIdProduto(),context);
            tvProdutoLinha.setText(productName);
            tvQuantidade.setText(String.valueOf(linhaCarrinho.getQuantidade()));
        }

        private String getProductName(int productId, Context context){
            //logic or function call to get product name from id
            String Nome = Singleton.getInstance(context).getProdutosBD().get(productId).getNome();
            return Nome;
        }
    }
}
