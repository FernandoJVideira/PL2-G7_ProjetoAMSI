package pl2.g7.iamsi.stuffngo.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pl2.g7.iamsi.stuffngo.Adapters.ListaMoradasAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Models.User;
import pl2.g7.iamsi.stuffngo.R;

public class MoradasFragment extends Fragment implements UserListener {

    private ListView lvMoradas;

    public MoradasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moradas, container, false);

        lvMoradas = view.findViewById(R.id.lvMoradas);
        Singleton.getInstance(getContext()).setUserListener(this);
        Singleton.getInstance(getContext()).getUserDataAPI(getContext(), "w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");

        lvMoradas.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getContext(), DetalhesMoradaActivity.class);
                intent.putExtra(DetalhesMoradaActivity.IDMORADA, (int) id);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRefreshUser(User user) {
        if (!user.getMoradas().isEmpty()) {
            lvMoradas.setAdapter(new ListaMoradasAdapter(getContext(), user.getMoradas()));
        }
    }
}