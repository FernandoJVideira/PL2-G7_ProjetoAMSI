package pl2.g7.iamsi.stuffngo.Views;
import static android.os.SystemClock.sleep;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import pl2.g7.iamsi.stuffngo.Listeners.FavoritosListener;
import pl2.g7.iamsi.stuffngo.Models.Favorito;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Utils.InputFilterMinMax;

public class DetalhesProdutosActivity extends AppCompatActivity implements FavoritosListener {
    private Produto produto;
    public static final String IDPRODUTO = "IDPRODUTO";
    private TextView tvNome;
    private TextView tvDescricao;
    private TextView tvPreco;
    private ImageView Image;
    private EditText etQuantidade;
    private ImageButton btPlus, btMinus, btFav, btCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);

        tvNome = findViewById(R.id.tvNomeDetalhes);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvPreco = findViewById(R.id.tvPrecoDetalhes);
        Image = findViewById(R.id.ImageDetalhes);
        btPlus = findViewById(R.id.btPlus);
        btMinus = findViewById(R.id.btMinus);
        btFav = findViewById(R.id.btFav);
        btCart = findViewById(R.id.btCart);
        etQuantidade = findViewById(R.id.etQuantidade);
        etQuantidade.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "10")});
        Singleton.getInstance(this).getAllFavoritosAPI(this);
        int id = getIntent().getIntExtra(IDPRODUTO, 0);
        produto = Singleton.getInstance(this).getProduto(id);
        Singleton.getInstance(this).setFavoritosListener(this);

        if(produto != null) {
            carregarProduto();
        }
        btPlus.setOnClickListener(v -> {
            int qtd = Integer.parseInt(etQuantidade.getText().toString());
            if(qtd < 10) {
                qtd++;
                etQuantidade.setText(String.valueOf(qtd));
            }
        });

        btMinus.setOnClickListener(v -> {
            int qtd = Integer.parseInt(etQuantidade.getText().toString());
            if(qtd > 1) {
                qtd--;
                etQuantidade.setText(String.valueOf(qtd));
            }
        });

        btFav.setOnClickListener(v -> {
            if (Singleton.getInstance(this).isFavorito(produto)) {
                Singleton.getInstance(this).removerFavoritoApi(this, produto);
                btFav.setImageResource(R.drawable.ic_fav_n);
            } else {
                Singleton.getInstance(this).adicionarFavoritoApi(this, produto);
                btFav.setImageResource(R.drawable.ic_fav);
            }
        });
    }

    private void carregarProduto() {
        Resources res = getResources();
        String title = String.format(res.getString(R.string.act_produto), produto.getNome());
        setTitle(title);
        tvNome.setText(produto.getNome());
        tvDescricao.setText(produto.getDescricao());
        tvPreco.setText(String.format("%s €", produto.getPreco_unit()));
        Glide.with(this).load(Singleton.URL + "/common/Images/" + produto.getImagem()).into(Image);

    }

    @Override
    public void onRefreshListaFavoritos(ArrayList<Favorito> favoritos) {
        if (!favoritos.isEmpty()) {
            if(Singleton.getInstance(this).isFavorito(produto))
                btFav.setImageResource(R.drawable.ic_fav);
            else
                btFav.setImageResource(R.drawable.ic_fav_n);
        }
    }
}