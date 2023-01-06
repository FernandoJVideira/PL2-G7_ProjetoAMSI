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

import pl2.g7.iamsi.stuffngo.Adapters.ListaSeccaoAdapter;

public class SenhaFragment extends Fragment {
    ListView lvSeccao;
    ArrayList<Seccao> seccao;


    public SenhaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_senha, container, false);

        lvSeccao = view.findViewById(R.id.ListViewSeccao);

        seccao = Singleton.getInstance().getSeccoes();

        lvSeccao.setAdapter(new ListaSeccaoAdapter(getContext(),seccao));

        lvSeccao.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getContext(), DetalhesSeccaoActivity.class);
                intent.putExtra(DetalhesSeccaoActivity.IDSECCAO, (int) id);
                startActivity(intent);
            }
        });

        return view ;
    }
}