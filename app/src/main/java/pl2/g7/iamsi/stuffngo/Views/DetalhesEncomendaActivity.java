package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;

import pl2.g7.iamsi.stuffngo.Adapters.ListaEncomendasAdapter;
import pl2.g7.iamsi.stuffngo.Adapters.ListaLinhasEncomendaAdapter;
import pl2.g7.iamsi.stuffngo.Listeners.DetalhesEncomendaListener;
import pl2.g7.iamsi.stuffngo.Models.Encomenda;
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.databinding.ActivityDetalhesEncomendaBinding;

public class DetalhesEncomendaActivity extends AppCompatActivity implements DetalhesEncomendaListener {

    public static final String IDENCOMENDA = "IDENCOMENDA";
    private ActivityDetalhesEncomendaBinding binding;
    private Encomenda encomenda;
    private String fatura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesEncomendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int idEncomenda = getIntent().getIntExtra(IDENCOMENDA, 0);

        encomenda = Singleton.getInstance(this).getEncomenda(idEncomenda);

        Singleton.getInstance(DetalhesEncomendaActivity.this).setDetalhesEncomendaListener(this);
        if(encomenda.getEstado().equals("emProcessamento")){
            binding.fabDownload.setVisibility(View.INVISIBLE);

        }else{
            Singleton.getInstance(DetalhesEncomendaActivity.this).getFaturaAPI(encomenda.getId(), this);
        }
        binding.tvIdEncomenda.setText(String.valueOf(encomenda.getId()));
        binding.tvEstado.setText(encomenda.getEstado());
        binding.tvDataEncomenda.setText(encomenda.getData());
        Morada morada = Singleton.getInstance(this).getMorada(encomenda.getIdMorada());
        if (morada != null)
            binding.tvMorada.setText(morada.toString());
        else
            binding.tvMorada.setText("Morada apagada");
        binding.lvLinhasEncomenda.setAdapter(new ListaLinhasEncomendaAdapter(this, encomenda.getLinhas()));


        binding.fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, "fatura"+ encomenda.getId() +".pdf");

                try (FileOutputStream fos = new FileOutputStream(file); ) {
                    // To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
                    byte[] decoder = new byte[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        decoder = Base64.getDecoder().decode(fatura);
                    }
                    fos.write(decoder);
                    Toast.makeText(DetalhesEncomendaActivity.this, R.string.txt_fatura_saved, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefreshDetalhesEncomenda(String fatura) {
        if(fatura!=null){
            this.fatura = fatura;
        }
    }
}