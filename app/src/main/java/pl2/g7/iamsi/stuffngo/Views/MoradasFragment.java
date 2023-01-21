package pl2.g7.iamsi.stuffngo.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Adapters.ListaMoradasAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Models.User;
import pl2.g7.iamsi.stuffngo.R;

public class MoradasFragment extends Fragment implements UserListener {

    private ListView lvMoradas;
    private FloatingActionButton fabAdd;
    public static final int DETAILS = 1;

    public MoradasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moradas, container, false);

        lvMoradas = view.findViewById(R.id.lvMoradas);
        lvMoradas.setAdapter(new ListaMoradasAdapter(getContext(), Singleton.getInstance(getContext()).getUser().getMoradas()));
        fabAdd = view.findViewById(R.id.fabAddList);
        Singleton.getInstance(getContext()).setUserListener(this);
        Singleton.getInstance(getContext()).getUserDataAPI(getContext(), "w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");

        lvMoradas.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Ao clique vai para a vista de Detalhes
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getContext(), DetalhesMoradaActivity.class);
                intent.putExtra(DetalhesMoradaActivity.POS, pos);
                startActivityForResult(intent, DETAILS);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetalhesMoradaActivity.class);
                startActivityForResult(intent, DETAILS);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == DETAILS) {

            lvMoradas.setAdapter(new ListaMoradasAdapter(getContext(),Singleton.getInstance(getContext()).getUser().getMoradas()));

            switch (intent.getIntExtra(MainActivity.OPERACAO, 0)) {
                case MainActivity.EDIT:
                    Toast.makeText(getContext(), R.string.txt_succ_edit, Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.ADD:
                    Toast.makeText(getContext(), R.string.txt_succ_add, Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.DELETE:
                    Toast.makeText(getContext(), R.string.txt_succ_delete, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onRefreshUser(User user) {
        if (!user.getMoradas().isEmpty()) {
            lvMoradas.setAdapter(new ListaMoradasAdapter(getContext(), user.getMoradas()));
        }
    }
}