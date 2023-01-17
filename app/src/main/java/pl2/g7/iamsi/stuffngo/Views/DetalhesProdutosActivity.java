package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Models.Singleton;

public class DetalhesProdutosActivity extends AppCompatActivity {
    private Produto produto;
    public static final String IDPRODUTO = "IDPRODUTO";
    private TextView tvNome;
    private TextView tvDescricao;
    private TextView tvPreco;
    private ImageView Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);

        tvNome = findViewById(R.id.tvNomeDetalhes);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvPreco = findViewById(R.id.tvPrecoDetalhes);
        Image = findViewById(R.id.ImageDetalhes);

        int id = getIntent().getIntExtra(IDPRODUTO, 0);
        produto = Singleton.getInstance(this).getProduto(id);

        if(produto != null) {

            carregarProduto();
        }
    }

    private void carregarProduto() {
        Resources res = getResources();
        String title = String.format(res.getString(R.string.act_livro), produto.getNome());
        setTitle(title);
        tvNome.setText(produto.getNome());
        tvDescricao.setText(produto.getDescricao());
        tvPreco.setText(Double.toString(produto.getPreco_unit()) + " €");
        Glide.with(this).load(Singleton.URL + "/common/Images/" + produto.getImagem()).into(Image);

    }
}