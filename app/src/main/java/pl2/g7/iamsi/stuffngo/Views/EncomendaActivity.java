package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Adapters.ListaEncomendasAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.EncomendasListener;
import pl2.g7.iamsi.stuffngo.Models.Encomenda;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.databinding.ActivityEncomendaBinding;

public class EncomendaActivity extends AppCompatActivity implements EncomendasListener {

    private ActivityEncomendaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEncomendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lvEncomendas.setAdapter(new ListaEncomendasAdapter(this, new ArrayList<Encomenda>()));
        Singleton.getInstance(this).setEncomendasListener(this);
        Singleton.getInstance(this).getAllEncomendasAPI(this, "w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");

        binding.lvEncomendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), DetalhesEncomendaActivity.class);
                intent.putExtra(DetalhesEncomendaActivity.IDENCOMENDA, (int) id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRefreshListaEncomendas(ArrayList<Encomenda> encomendas) {
        if (!encomendas.isEmpty()) {
            binding.lvEncomendas.setAdapter(new ListaEncomendasAdapter(getApplicationContext(), encomendas));
        }
    }
}