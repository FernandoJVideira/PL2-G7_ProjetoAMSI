    package pl2.g7.iamsi.stuffngo;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ListView;

    import java.util.ArrayList;

    import pl2.g7.iamsi.stuffngo.Adapters.ListaProdutosAdapter;

    public class HomeFragment extends Fragment {
        ListView lvProdutos;
        ArrayList<Produtos> produtos;

        public HomeFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);;

            lvProdutos = view.findViewById(R.id.lvProdutos);

            produtos = Singleton.getInstance().getProdutos();

            lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), produtos));

            lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    Intent intent = new Intent(getContext(), DetalhesProdutosActivity.class);
                    intent.putExtra(DetalhesProdutosActivity.IDPRODUTO, (int) id);
                    startActivity(intent);

                }
            });
            return view;
        }
    }