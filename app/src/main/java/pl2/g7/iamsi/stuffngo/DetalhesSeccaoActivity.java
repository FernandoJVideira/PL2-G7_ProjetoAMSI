package pl2.g7.iamsi.stuffngo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetalhesSeccaoActivity extends AppCompatActivity {
    private Seccao seccao;
    private  SenhaDigital senhaDigital;
    public static final String IDSECCAO = "IDSECCAO";
    private TextView tvNomeSeccao;
    private TextView tvNumeroSenhaAtual;
    private Button btnTirarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_seccao);

        int id = getIntent().getIntExtra(IDSECCAO, 0);
        seccao = Singleton.getInstance().getSeccao(id);
        senhaDigital = Singleton.getInstance().getSenha(id);

        tvNomeSeccao = findViewById(R.id.tvSeccaoNome);
        tvNumeroSenhaAtual = findViewById(R.id.tvNumeroSenhaAtual);
        btnTirarSenha = findViewById(R.id.btTirarSenha);

        if(seccao != null){
            carregarSeccao();
        }

    }
    private void carregarSeccao() {
        Resources res = getResources();
        String title = String.format(getString(R.string.tira_senha), seccao.getNome());
        setTitle(title);
        tvNomeSeccao.setText(seccao.getNome());
        tvNumeroSenhaAtual.setText(senhaDigital.getNumeroAtual()+"");
        btnTirarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesSeccaoActivity.this);
                builder.setTitle("Tem a certeza ?")
                        .setMessage("Sua Senha: "+ (senhaDigital.getNumeroAtual()+1))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(DetalhesSeccaoActivity.this, " A sua Senha " +
                                        "("+ (senhaDigital.getNumeroAtual()+1) + ") " +
                                        "foi Tirada com sucesso \n " +
                                        "                   Aguarde a sua vez ",
                                        Toast.LENGTH_SHORT).show();

                                //Colocar a aumentar a o numero da senha Atual;
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        })

                        .show();

            }
        });

        };





}

