package pl2.g7.iamsi.stuffngo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void OnclickListaProdutos(View view){
       Intent intent = new Intent(this, ListaProdutos.class);
       startActivity(intent);
   };
}