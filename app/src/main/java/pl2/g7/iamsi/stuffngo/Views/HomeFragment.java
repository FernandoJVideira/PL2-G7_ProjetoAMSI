package pl2.g7.iamsi.stuffngo.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private SearchView searchView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ;
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);
        inflater.inflate(R.menu.menu_favoritos, menu);

        MenuItem itemPesquisa = menu.findItem(R.id.search_icon);
        MenuItem itemFavoritos = menu.findItem(R.id.favoritos);
        searchView = (SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Produto> listaTemp = new ArrayList<>();
                for (Produto l : Singleton.getInstance(getContext()).getProdutosBD()) {
                    if (l.getNome().toLowerCase().contains(s.toLowerCase())) {
                        listaTemp.add(l);
                    }
                }
                lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), listaTemp, HomeFragment.this));
                return true;
            }
        });

        itemFavoritos.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ArrayList<Favorito> favoritos = Singleton.getInstance(getContext()).getFavoritosBD();
                ArrayList<Produto> produtos = Singleton.getInstance(getContext()).getProdutosBD();
                ArrayList<Produto> listaTemp = new ArrayList<>();
                for (Favorito f : favoritos) {
                    for (Produto p : produtos) {
                        if (f.getId_produto() == p.getId()) {
                            listaTemp.add(p);
                        }
                    }
                }
                lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), listaTemp, HomeFragment.this));
                getActivity().setTitle("Favoritos");
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> produtos) {
        if (!produtos.isEmpty()) {
            lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), produtos, this));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getTitle().equals("Favoritos")) {
            ArrayList<Favorito> favoritos = Singleton.getInstance(getContext()).getFavoritosBD();
            ArrayList<Produto> produtos = Singleton.getInstance(getContext()).getProdutosBD();
            ArrayList<Produto> listaTemp = new ArrayList<>();
            for (Favorito f : favoritos) {
                for (Produto p : produtos) {
                    if (f.getId_produto() == p.getId()) {
                        listaTemp.add(p);
                    }
                }
            }
            lvProdutos.setAdapter(new ListaProdutosAdapter(getContext(), listaTemp, HomeFragment.this));
            getActivity().setTitle("Favoritos");
        }
    }
    }