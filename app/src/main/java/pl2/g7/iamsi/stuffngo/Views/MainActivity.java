package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Objects;
import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Listeners.MqttListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MqttListener {
    private ActivityMainBinding binding;
    public static int DEL = 1, ADD = 2, UPDATE = 3;
    private Menu menu;
    public static String TOKEN = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upper_nav_menu, menu);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences",MODE_PRIVATE);
        TOKEN = sharedPreferences.getString("Token", TOKEN);
        if(TOKEN == null){
            loggedIn(false);
        }
        return true;
    }

    public void loggedIn(boolean loggedIn){
        menu.findItem(R.id.carrinho_icon).setVisible(loggedIn);
        menu.findItem(R.id.encomendas).setVisible(loggedIn);
        menu.findItem(R.id.logout).setVisible(loggedIn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_icon:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
               return true;
            case R.id.carrinho_icon:
                    Intent intent = new Intent(this, CarrinhoActivity.class);
                    startActivity(intent);
                return true;
            case R.id.encomendas: //Encomendas
                Toast.makeText(this, "Encomendas", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EncomendaActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("Token");
                editor.remove("Username");
                editor.apply();
                TOKEN = null;
                loggedIn(false);
                replaceFragment(new HomeFragment());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definition of the Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_stuffngo);

        //MqttListener
        Singleton.getInstance(this).setMqttListener(this);

        //Acess Shared Preferences and put the TOKEN as the shared Preference "Token"
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences",MODE_PRIVATE);
        TOKEN = sharedPreferences.getString("Token", TOKEN);

        //Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set Original Fragment;
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
                    if(TOKEN == null){
                        replaceFragment(new LoginFragment());
                    }else{
                        replaceFragment(new SettingsFragment());
                    }
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
                    //Toast.makeText(this, promo, Toast.LENGTH_SHORT).show();
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

            Singleton.getInstance(getApplicationContext()).mqttClient = new MqttAndroidClient(this, "tcp://10.0.2.2:1883", "Cliente", Ack.AUTO_ACK);
            Singleton.getInstance(getApplicationContext()).mqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        Singleton.getInstance(getApplicationContext()).mqttClient.subscribe("promo", 1);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
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