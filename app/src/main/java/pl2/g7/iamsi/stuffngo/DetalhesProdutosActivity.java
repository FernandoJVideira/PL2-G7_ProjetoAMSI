package pl2.g7.iamsi.stuffngo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetalhesProdutosActivity extends AppCompatActivity {
    private Produtos produto;
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
        produto = Singleton.getInstance().getProduto(id);

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
        tvPreco.setText(Integer.toString(produto.getPreco_unit()) + " €");
        Image.setImageResource(produto.getImagem());
    }
}