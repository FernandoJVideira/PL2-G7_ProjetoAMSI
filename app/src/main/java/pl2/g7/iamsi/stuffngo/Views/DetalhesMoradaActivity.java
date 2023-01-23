package pl2.g7.iamsi.stuffngo.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import pl2.g7.iamsi.stuffngo.Adapters.ListaMoradasAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.MoradaListener;
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Models.User;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.databinding.ActivityDetalhesMoradaBinding;
import pl2.g7.iamsi.stuffngo.databinding.ActivityMainBinding;

public class DetalhesMoradaActivity extends AppCompatActivity implements MoradaListener {

    public static final int MIN_CHAR = 2;
    public static final int MIN_COD_POSTAL = 4;

    public static final String POS = "POS";
    private Morada morada;
    private ArrayList<Morada> moradas;
    private User user;

    private ActivityDetalhesMoradaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesMoradaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_stuffngo);

        Singleton.getInstance(this).setMoradaListener(this);

        user = Singleton.getInstance(getApplicationContext()).getUser();
        moradas = user.getMoradas();

        int pos = getIntent().getIntExtra(POS, -1);

        morada = (pos!=-1) ? moradas.get(pos) : null;

        if (morada != null) {
            carregarMorada();
            binding.fabSave.setImageResource(R.drawable.ic_save);
        } else {
            setTitle(getString(R.string.txtAddMorada));
            binding.fabSave.setImageResource(R.drawable.ic_action_add);
        }

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(isMoradaValida());
                if(isMoradaValida())
                {
                    String rua = binding.etRua.getText().toString();
                    String cidade = binding.etCidade.getText().toString();
                    String codPostal = binding.etCodPostal.getText().toString();
                    String pais = binding.etPais.getText().toString();
                    if(morada != null)
                    {
                        morada.setRua(rua);
                        morada.setCidade(cidade);
                        morada.setCodPostal(codPostal);
                        morada.setPais(pais);
                        Singleton.getInstance(getApplicationContext()).editarMoradaAPI(morada, getApplicationContext(),"w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");
                    }
                    else
                    {
                        morada = new Morada(0, rua, cidade, codPostal, pais);
                        Singleton.getInstance(getApplicationContext()).addMoradaAPI(morada, getApplicationContext(),"w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (morada == null) {
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void carregarMorada(){
        binding.etRua.setText(morada.getRua());
        binding.etCidade.setText(morada.getCidade());
        binding.etCodPostal.setText(morada.getCodPostal());
        binding.etPais.setText(morada.getPais());
    }

    private boolean isMoradaValida()
    {
        String rua = binding.etRua.getText().toString();
        String cidade = binding.etCidade.getText().toString();
        String codPostal = binding.etCodPostal.getText().toString();
        String pais = binding.etPais.getText().toString();

        if (rua.length() < MIN_CHAR) {
            binding.etRua.setError("Rua Invalida");
            return false;
        }
        if (cidade.length() < MIN_CHAR) {
            binding.etCidade.setError("Cidade Inválida");
            return false;
        }
        if (codPostal.length() < MIN_COD_POSTAL) {
            binding.etCodPostal.setError("Código Postal Inválido");
            return false;
        }else if (!codPostal.matches("[1-9][0-9]{3}-[0-9]{3}")) {
            binding.etCodPostal.setError("Código Postal Inválido");
            return false;
        }

        if (pais.length() < MIN_CHAR) {
            binding.etPais.setError("País Inválido");
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDelete:
                dialogDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.txtDelete)
                .setMessage(R.string.txtDeleteMsg)
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moradas.remove(morada);
                        user.setMoradas(moradas);
                        Singleton.getInstance(getApplicationContext()).removerMoradaAPI(morada,
                                getApplicationContext(), "w9MY9udTVVlUX_xyIjoHfG7JDt2q0ji7");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onMoradasRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();

    }

}