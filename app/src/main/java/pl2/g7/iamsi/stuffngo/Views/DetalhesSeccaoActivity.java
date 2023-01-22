package pl2.g7.iamsi.stuffngo.Views;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.MqttException;
import pl2.g7.iamsi.stuffngo.Listeners.SenhaListener;
import pl2.g7.iamsi.stuffngo.Models.Seccao;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Models.Singleton;

public class DetalhesSeccaoActivity extends AppCompatActivity implements SenhaListener {
    private Seccao seccao;
    public static final String SECCAO = "SECCAO";
    private TextView tvNomeSeccao;
    private TextView tvNumeroSenhaAtual;
    private Button btnTirarSenha;
    private TextView tvTextoSenhaAtual, tvSenha;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_seccao);
        id = getIntent().getIntExtra(SECCAO, 0);
        Singleton.getInstance(this).setSenhaListener(this);
        seccao = Singleton.getInstance(this).getSeccao(id);
        tvNomeSeccao = findViewById(R.id.tvSeccaoNome);
        tvNumeroSenhaAtual = findViewById(R.id.tvNumeroSenhaAtual);
        btnTirarSenha = findViewById(R.id.btTirarSenha);
        tvTextoSenhaAtual = findViewById(R.id.tvTextoSenhaAtual);
        tvSenha = findViewById(R.id.tvSenha);
        btnTirarSenha.setBackgroundColor(Color.parseColor("#BD9017"));
        SharedPreferences sharedInfoUser = getSharedPreferences("senhaDigital", MODE_PRIVATE);
        if(sharedInfoUser.getString("seccao_" + seccao.getId(), null) != null){
            String senha = sharedInfoUser.getString("seccao_" + seccao.getId(), null);
            tvSenha.setText(senha);
            tvSenha.setVisibility(View.VISIBLE);
            tvTextoSenhaAtual.setVisibility(View.VISIBLE);
        }
        if(seccao != null){
            carregarSeccao();
        }

    }
    private void carregarSeccao() {
        String title = String.format(getString(R.string.tira_senha), seccao.getNome());
        setTitle(title);
        tvNomeSeccao.setText(seccao.getNome());
        tvNumeroSenhaAtual.setText(String.format("%d", seccao.getNumeroActual()));
        btnTirarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesSeccaoActivity.this);
                builder.setTitle(seccao.getNome())
                        .setMessage(String.format("Deseja tirar senha para %s?", seccao.getNome()))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Singleton.getInstance(getApplicationContext()).getSenhaDigitalAPI(id);
                                try {
                                    Singleton.getInstance(getApplicationContext()).mqttClient.subscribe("seccao_" + id, 1);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onRefreshSenha(String number, String numeroActual) {
        SharedPreferences sharedInfoUser = getSharedPreferences("senhaDigital", MODE_PRIVATE);
        if(number != null){
            SharedPreferences.Editor editor = sharedInfoUser.edit();
            editor.putString("seccao_" + seccao.getId(), number);
            editor.apply();
            tvSenha.setText(number);
            tvSenha.setVisibility(View.VISIBLE);
            tvTextoSenhaAtual.setVisibility(View.VISIBLE);
        }
        if(numeroActual != null){
            if(numeroActual.equals(sharedInfoUser.getString("seccao_" + seccao.getId(), null)))
                Singleton.getInstance(this).createNotification(this,
                    "StuffNgo - Senha Digital",
                    "Senha actual :" + numeroActual,
                    "Estamos a chamar a sua senha!");
            tvNumeroSenhaAtual.setText(numeroActual);
        }
    }
}

