package pl2.g7.iamsi.stuffngo.Adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.LinhaCarrinho;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Seccao;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Views.CarrinhoActivity;
import pl2.g7.iamsi.stuffngo.Views.DetalhesProdutosActivity;
import pl2.g7.iamsi.stuffngo.Views.DetalhesSeccaoActivity;

public class ListaCarrinhoAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<LinhaCarrinho> linhas;

    public ListaCarrinhoAdapter(Context context, ArrayList<LinhaCarrinho> linhas) {
        this.linhas = linhas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return linhas.size();
    }

    @Override
    public Object getItem(int position) {
        return linhas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return linhas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinhaCarrinho item = linhas.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_carrinho, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemQuantity = convertView.findViewById(R.id.item_quantity);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        ImageButton removeButton = convertView.findViewById(R.id.remove_button);
        TextView itemTotal = convertView.findViewById(R.id.tvTotal);
        ImageButton editButton = convertView.findViewById(R.id.edit_button);
        //get product name
        Produto produto = Singleton.getInstance(context).getProduto(item.getIdProduto());
        itemName.setText(String.valueOf(produto.getNome()));
        itemQuantity.setText(String.valueOf(item.getQuantidade()));
        itemPrice.setText(produto.getPreco_unit() + "€");
        itemTotal.setText((double) Math.round((produto.getPreco_unit() * item.getQuantidade()) * 100) / 100 + "€");
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.txt_remover_produto)
                        .setMessage(R.string.txt_remover_produto_question)
                        .setPositiveButton(R.string.txt_sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Singleton.getInstance(context).delProdutoCarrinhoAPI(item.getIdProduto());
                                linhas.remove(item);
                                notifyDataSetChanged();
                                if (linhas.size() == 0){
                                    ((CarrinhoActivity)context).onCarrinhoRefresh(null);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        }).show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetalhesProdutosActivity.class);
                intent.putExtra(DetalhesProdutosActivity.IDPRODUTO, item.getIdProduto());
                intent.putExtra("quantidade", item.getQuantidade());
                view.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
