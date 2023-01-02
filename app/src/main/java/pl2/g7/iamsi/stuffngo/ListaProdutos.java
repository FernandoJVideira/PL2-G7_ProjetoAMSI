package pl2.g7.iamsi.stuffngo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaProdutos extends AppCompatActivity {
    ListView lvProdutos;
    ArrayList<Produtos> produtos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_produtos);

        lvProdutos = findViewById(R.id.lvProdutos);

        produtos = SingletonProdutos.getInstance().getProdutos();

        lvProdutos.setAdapter(new ListaProdutosAdapter(this, produtos));

        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), DetalhesProdutosActivity.class);
                intent.putExtra(DetalhesProdutosActivity.IDPRODUTO, (int) id);
                startActivity(intent);

            }
        });
    }
}