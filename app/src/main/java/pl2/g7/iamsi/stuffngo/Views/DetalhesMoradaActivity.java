package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl2.g7.iamsi.stuffngo.R;

public class DetalhesMoradaActivity extends AppCompatActivity {

    public static final String IDMORADA = "IDMORADA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_morada);
    }
}