package pl2.g7.iamsi.stuffngo.Views;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pl2.g7.iamsi.stuffngo.Adapters.ListaSeccaoAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.LojasListener;
import pl2.g7.iamsi.stuffngo.Listeners.SeccoesListener;
import pl2.g7.iamsi.stuffngo.Models.Loja;
import pl2.g7.iamsi.stuffngo.Models.Seccao;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;

public class SenhaFragment extends Fragment implements SeccoesListener, LojasListener {
    ListView lvSeccao;
    ArrayList<Seccao> seccoes;
    ArrayList<Loja> lojas;
    NiceSpinner niceSpinner;
    public SenhaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senha, container, false);
        lvSeccao = view.findViewById(R.id.ListViewSeccao);
        Singleton.getInstance(getContext()).getAllLojasAPI();
        lojas = Singleton.getInstance(getContext()).getLojasBD();
        niceSpinner = (NiceSpinner) view.findViewById(R.id.nice_spinner);
        if(lojas != null && AppJsonParser.isConnectionInternet(getContext())) {
            List<String> list = new ArrayList<String>();
            for (Loja l : lojas) {
                list.add(l.getDescricao());
            }
            String[] stringArray = list.toArray(new String[0]);
            List<String> dataset = new LinkedList<>(Arrays.asList(stringArray));
            niceSpinner.attachDataSource(dataset);
            int id = niceSpinner.getSelectedIndex();
            Singleton.getInstance(getContext()).setSeccoesListener(this);
            Singleton.getInstance(getContext()).setLojasListener(this);
            Singleton.getInstance(getContext()).getAllSeccoesAPI(getContext(), id + 1);
            seccoes = Singleton.getInstance(getContext()).getSeccoes();
            lvSeccao.setAdapter(new ListaSeccaoAdapter(getContext(),seccoes));
        }
        else
            niceSpinner.setText("Nenhuma loja disponível");

        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = parent.getItemAtPosition(position).toString();
                Singleton.getInstance(getContext()).getAllSeccoesAPI(getContext(), position + 1);
                seccoes = Singleton.getInstance(getContext()).getSeccoes();
                lvSeccao.setAdapter(new ListaSeccaoAdapter(getContext(),seccoes));
            }
        });

        lvSeccao.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getContext(), DetalhesSeccaoActivity.class);
                seccoes = Singleton.getInstance(getContext()).getSeccoes();
                intent.putExtra(DetalhesSeccaoActivity.SECCAO, seccoes.get(pos).getId());
                startActivity(intent);
            }
        });

        return view ;
    }
    @Override
    public void onRefreshListaSeccoes(ArrayList<Seccao> seccoes) {
        if(seccoes != null) {
            lvSeccao.setAdapter(new ListaSeccaoAdapter(getContext(),seccoes));
            SharedPreferences sharedInfoUser = getContext().getSharedPreferences("senhaDigital", MODE_PRIVATE);
            for (Seccao s : seccoes) {
                if (sharedInfoUser.getString("seccao_" + s.getId(), null) != null) {
                    if (Integer.parseInt(sharedInfoUser.getString("seccao_" + s.getId(), null)) < s.getNumeroActual()) {
                        sharedInfoUser.edit().remove(s.getNome()).apply();
                    }
                }
            }
        }
        else
            Toast.makeText(getContext(), "Nenhuma seccao disponível", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefreshListaLojas(ArrayList<Loja> lojas) {
        if(lojas != null) {
            List<String> list = new ArrayList<String>();
            for (Loja l : lojas) {
                list.add(l.getDescricao());
            }
            String[] stringArray = list.toArray(new String[0]);
            List<String> dataset = new LinkedList<>(Arrays.asList(stringArray));
            niceSpinner.attachDataSource(dataset);
            int id = niceSpinner.getSelectedIndex();
            Singleton.getInstance(getContext()).getAllSeccoesAPI(getContext(), id + 1);
            seccoes = Singleton.getInstance(getContext()).getSeccoes();
            lvSeccao.setAdapter(new ListaSeccaoAdapter(getContext(),seccoes));
        }
        else {
            niceSpinner.setText("Nenhuma loja disponível");
        }
    }
}