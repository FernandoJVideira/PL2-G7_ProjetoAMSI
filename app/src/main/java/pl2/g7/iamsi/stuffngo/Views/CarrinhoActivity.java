package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoListener {
    ListView listaCarrinho;
    Button btnCheckout;

    TextView tvSubTotal;
    TextView tvIva;
    TextView tvDesconto;
    TextView tvTotal;
    String m_Text;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Singleton.getInstance(this).setCarrinhoListener(this);

        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvIva = findViewById(R.id.tvIva);
        tvDesconto = findViewById(R.id.tvDesconto);
        tvTotal = findViewById(R.id.tvTotal);

        listaCarrinho = findViewById(R.id.cart_list);

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
            builder.setTitle(R.string.txt_lojas);

            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
                ArrayList<Morada> moradas = Singleton.getInstance(CarrinhoActivity.this).getUser().getMoradasActivas();
                if (moradas.size() == 0) {
                    Toast.makeText(CarrinhoActivity.this, R.string.txt_no_moradas, Toast.LENGTH_SHORT).show();
                    return;
                }
                Singleton.getInstance(CarrinhoActivity.this).carrinhoCheckouApi(moradas.get(moradas.size() - 1).getId(), listItemsId[which]);
                dialog.dismiss();
            });
            // alert dialog shouldn't be cancellable
            builder.setCancelable(false);

            // handle the negative button of the alert dialog
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            });

            // create the builder
            builder.create();

            // create the alert dialog with the alert dialog builder instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cupao, menu);
        MenuItem item = menu.findItem(R.id.cupao_icon);
        item.setOnMenuItemClickListener(item1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.txt_cupao);

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    Singleton.getInstance(CarrinhoActivity.this).adicionarCupaoAPI(CarrinhoActivity.this, m_Text);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        });
        return true;
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
        Toast.makeText(this, R.string.txt_compra_concluida, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onCarrinhoRefresh(Carrinho carrinho) {
        if (carrinho == null) {
            Toast.makeText(this, R.string.txt_empty_cart, Toast.LENGTH_SHORT).show();
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.75f);
            tvSubTotal.setText("0.00€");
            tvIva.setText("0.00€");
            tvDesconto.setText("0.00€");
            tvTotal.setText("0.00€");
            menu.findItem(R.id.cupao_icon).setVisible(false);
            return;
        }
        listaCarrinho = findViewById(R.id.cart_list);
        listaCarrinho.setAdapter(new ListaCarrinhoAdapter(this, carrinho.getLinhas()));

        tvSubTotal.setText(carrinho.getSubTotal() + "€");
        tvIva.setText(carrinho.getIva() + "€");
        tvDesconto.setText(carrinho.getDesconto() + "€");
        tvTotal.setText(carrinho.getTotal() + "€");
    }

    @Override
    public void onCarrinhoUpdate(int type) {
        switch (type) {
            case 30:
                Toast.makeText(this, R.string.txt_produto_rem, Toast.LENGTH_SHORT).show();
                break;
            case 20:
                Toast.makeText(this, R.string.txt_produto_add, Toast.LENGTH_SHORT).show();
                break;
            case 10:
                Toast.makeText(this, R.string.txt_cart_updated, Toast.LENGTH_SHORT).show();
                break;
        }
        Singleton.getInstance(this).getCarrinhoAPI(this);
    }
}