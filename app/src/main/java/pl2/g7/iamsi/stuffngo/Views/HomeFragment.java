    package pl2.g7.iamsi.stuffngo.Views;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ListView;
    import android.widget.SearchView;

    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    import java.util.ArrayList;

    import pl2.g7.iamsi.stuffngo.Adapters.ListaProdutosAdapter;
    import pl2.g7.iamsi.stuffngo.Listeners.ProdutosListener;
    import pl2.g7.iamsi.stuffngo.Models.Favorito;
    import pl2.g7.iamsi.stuffngo.Models.Produto;
    import pl2.g7.iamsi.stuffngo.R;
    import pl2.g7.iamsi.stuffngo.Models.Singleton;

    public class HomeFragment extends Fragment implements ProdutosListener {
        ListView lvProdutos;
        private FloatingActionButton fabLista;
        private SearchView searchView;
        public static final int DETALHES = 1;
        public HomeFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);;
            setHasOptionsMenu(true);
            lvProdutos = view.findViewById(R.id.lvProdutos);

            Singleton.getInstance(getContext()).setProdutosListener(this);
            Singleton.getInstance(getContext()).getAllProdutosAPI(getContext());

            lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    Intent intent = new Intent(getContext(), DetalhesProdutosActivity.class);
                    intent.putExtra(DetalhesProdutosActivity.IDPRODUTO, (int) id);
                    startActivity(intent);
                }
            });

            return view;
        }

        @Override
        public void onRefreshListaProdutos(ArrayList<Produto> produtos) {
            if (!produtos.isEmpty()) {
                lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), produtos, this));
            }
        }

    }