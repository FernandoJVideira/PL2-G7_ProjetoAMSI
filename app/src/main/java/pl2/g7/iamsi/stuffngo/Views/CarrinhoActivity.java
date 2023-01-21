package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl2.g7.iamsi.stuffngo.Adapters.ListaCarrinhoAdapter;
import pl2.g7.iamsi.stuffngo.Adapters.ListaSeccaoAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.CarrinhoListener;
import pl2.g7.iamsi.stuffngo.Models.Carrinho;
import pl2.g7.iamsi.stuffngo.Models.LinhaCarrinho;
import pl2.g7.iamsi.stuffngo.Models.Loja;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoListener {
    ListView listaCarrinho;
    Button btnCheckout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Singleton.getInstance(this).setCarrinhoListener(this);
        Singleton.getInstance(this).getCarrinhoAPI(this);
        listaCarrinho = findViewById(R.id.cart_list);
        listaCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CarrinhoActivity.this, "Clicou no item " + id, Toast.LENGTH_SHORT).show();
            }
        });

        int size = Singleton.getInstance(this).getLojasBD().size();
        String[] listItems = new String[size];
        int[] listItemsId = new int[size];
        for (int i = 0; i < size; i++) {
            listItems[i] = Singleton.getInstance(this).getLojasBD().get(i).getDescricao();
            listItemsId[i] = Singleton.getInstance(this).getLojasBD().get(i).getId();
        }

        btnCheckout = findViewById(R.id.checkout_button);
        btnCheckout.setOnClickListener(v -> {
            // initialise the alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(CarrinhoActivity.this);

            // set the title for the alert dialog
            builder.setTitle("Escolha a loja onde quer fazer o levantamento");

            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
                // when the user clicks on the item, the item is selected and the dialog is dismissed
                //tvSelectedItemsPreview.setText(String.format("%s%s, ", tvSelectedItemsPreview.getText(), selectedItems.get(which)));
                Singleton.getInstance(CarrinhoActivity.this).carrinhoCheckouApi(1, listItemsId[which]);
                dialog.dismiss();
            });
            // alert dialog shouldn't be cancellable
            builder.setCancelable(false);

            // handle the negative button of the alert dialog
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
            });

            // create the builder
            builder.create();

            // create the alert dialog with the alert dialog builder instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Singleton.getInstance(this).getCarrinhoAPI(this);
    }

    @Override
    public void onCarrinhoCupao() {

    }

    @Override
    public void onCarrinhoCheckout() {
        Toast.makeText(this, "Compra concluida com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onCarrinhoRefresh(Carrinho carrinho) {
        if (carrinho == null) {
            Toast.makeText(this, "Carrinho vazio", Toast.LENGTH_SHORT).show();
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.75f);
            return;
        }
        listaCarrinho = findViewById(R.id.cart_list);
        listaCarrinho.setAdapter(new ListaCarrinhoAdapter(this, carrinho.getLinhas()));
        TextView tvSubTotal = findViewById(R.id.tvSubTotal);
        TextView tvIva = findViewById(R.id.tvIva);
        TextView tvDesconto = findViewById(R.id.tvDesconto);
        TextView tvTotal = findViewById(R.id.tvTotal);

        tvSubTotal.setText(carrinho.getSubTotal() + "€");
        tvIva.setText(carrinho.getIva() + "€");
        tvDesconto.setText(carrinho.getDesconto() + "€");
        tvTotal.setText(carrinho.getTotal() + "€");
    }

    @Override
    public void onCarrinhoUpdate(int type) {
        switch (type) {
            case 1:
                Toast.makeText(this, "Produto removido", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Produto adicionado", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Carrinho actualizado", Toast.LENGTH_SHORT).show();
                break;
        }
        Singleton.getInstance(this).getCarrinhoAPI(this);
    }
}