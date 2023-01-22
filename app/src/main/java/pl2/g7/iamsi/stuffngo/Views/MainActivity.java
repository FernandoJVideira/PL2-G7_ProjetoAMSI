package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONException;
import org.json.JSONObject;
import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import pl2.g7.iamsi.stuffngo.Listeners.MqttListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MqttListener {

    private ActivityMainBinding binding ;
    public static String TOKEN = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upper_nav_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_icon:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
               return true;
            case R.id.carrinho_icon:
                Toast.makeText(this,"Carrinho", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.dropdown:
                Toast.makeText(this, "Encomendas", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_stuffngo);
        Singleton.getInstance(this).setMqttListener(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        if(Singleton.getInstance(getApplicationContext()).mqttClient == null)
            connectMqtt();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home :
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.copoes:
                    replaceFragment(new SenhaFragment());
                    break;
                case R.id.qr:
                    replaceFragment(new QrFragment());
                    break;
                case R.id.settings:
                    //replaceFragment(new SettingsFragment());
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    break;
            }
            return true ;
        });
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMessageArrived(String topic, String message) {
        if (topic.equals("promo")){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                String promo = null, codigo = null, percentagem = null, data_limite = null;
                try {
                    promo = jsonObject.getString("nome_promo");
                    codigo = jsonObject.getString("codigo");
                    percentagem = jsonObject.getString("percentagem");
                    data_limite = jsonObject.getString("data_limite");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (promo != null) {
                    Singleton.getInstance(this).createNotification(this,
                            "StuffNgo - " + promo,
                            "Nova promoção à sua espera!",
                            "Desconto de " + percentagem + "% com o código " + codigo + " até " + data_limite + "!");
                }
            }
        }
        else if(topic.contains("seccao_")){
            if(Singleton.getInstance(this).senhaListener != null){
                Singleton.getInstance(this).senhaListener.onRefreshSenha(null, message);
            }
        }
    }

    private void connectMqtt(){
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);

            Singleton.getInstance(getApplicationContext()).mqttClient = new MqttAndroidClient(this, "tcp://188.37.63.6:1883", "Cliente", Ack.AUTO_ACK);
            Singleton.getInstance(getApplicationContext()).mqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        Singleton.getInstance(getApplicationContext()).mqttClient.subscribe("promo", 1);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Error: " + exception.getMessage());
                }
            });

            Singleton.mqtt(Singleton.getInstance(getApplicationContext()).mqttClient, this);
        }
        catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }
}