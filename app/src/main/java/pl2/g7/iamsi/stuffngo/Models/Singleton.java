package pl2.g7.iamsi.stuffngo.Models;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import info.mqtt.android.service.MqttAndroidClient;
import pl2.g7.iamsi.stuffngo.Listeners.CarrinhoListener;
import pl2.g7.iamsi.stuffngo.Listeners.DetalhesEncomendaListener;
import pl2.g7.iamsi.stuffngo.Listeners.EncomendasListener;
import pl2.g7.iamsi.stuffngo.Listeners.FavoritosListener;
import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Listeners.LojasListener;
import pl2.g7.iamsi.stuffngo.Listeners.MoradaListener;
import pl2.g7.iamsi.stuffngo.Listeners.MqttListener;
import pl2.g7.iamsi.stuffngo.Listeners.ProdutosListener;
import pl2.g7.iamsi.stuffngo.Listeners.SeccoesListener;
import pl2.g7.iamsi.stuffngo.Listeners.SenhaListener;
import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.R;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;
import pl2.g7.iamsi.stuffngo.Views.MainActivity;

public class Singleton {
    private static Singleton INSTANCE = null;
    private ArrayList<Produto> produtos;
    private ArrayList<Favorito> favoritos;
    private ArrayList<Loja> lojas;
    private ArrayList<Seccao> seccao;
    private ArrayList<Morada> moradas = new ArrayList<>();
    private Carrinho carrinho;
    private BDHelper bdHelper;
    private static RequestQueue requestQueue = null;
    private ProdutosListener produtosListener = null;
    private LoginListener loginListener = null;
    private FavoritosListener favoritosListener = null;
    private SeccoesListener seccoesListener = null;
    private LojasListener lojasListener = null;
    public SenhaListener senhaListener = null;
    public UserListener userListener = null;
    public DetalhesEncomendaListener detalhesEncomendaListener = null;
    public MoradaListener moradasListener = null;
    public EncomendasListener encomendasListener = null;
    private MqttListener mqttListener = null;
    private CarrinhoListener carrinhoListener = null;
    private static final String IP = "10.0.2.2";
    public static final String URL = "http://"+ IP +"/PL2-G7_ProjetoPlatSI";
    private static final String URL_API = URL + "/backend/web/api";
    public MqttAndroidClient mqttClient;
    private String token;
    private String USERNAME = null;
    private User user;
    private ArrayList<Encomenda> encomendas;
    private String SENHA = null;
    private String fatura;

    public static synchronized Singleton getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Singleton(context);
            requestQueue = Volley.newRequestQueue(context);
        }
        return INSTANCE;
    }

    private Singleton(Context context){ //Constructor
        produtos = new ArrayList<>();
        favoritos = new ArrayList<>();
        bdHelper = new BDHelper(context);
        SharedPreferences sharedInfoUser = context.getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        if(sharedInfoUser.getString("Token", null) != null)
            token = sharedInfoUser.getString("Token", null);
    }

    public Produto getProduto(int id){

        for(Produto produtos : produtos){
            if(produtos.getId() == id){
                return produtos;
            }
        }

        return null;
    }

    public Seccao getSeccao(int id){

        for(Seccao seccao : seccao){
            if(seccao.getId() == id){
                return seccao;
            }
        }

        return null;
    }

    public void setProdutosListener(ProdutosListener listener){
        this.produtosListener = listener;
    }
    public void setMqttListener(MqttListener mqttListener){
        this.mqttListener = mqttListener;
    }
    public void setCarrinhoListener(CarrinhoListener carrinhoListener){
        this.carrinhoListener = carrinhoListener;
    }
    public void setFavoritosListener(FavoritosListener listener){
        this.favoritosListener = listener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setSenhaListener(SenhaListener senhaListener) {
        this.senhaListener = senhaListener;
    }

    public void setSeccoesListener(SeccoesListener seccoesListener) {
        this.seccoesListener = seccoesListener;
    }

    public void setLojasListener(LojasListener lojasListener) {
        this.lojasListener = lojasListener;
    }

    public ArrayList<Produto> getProdutos() {
        return new ArrayList<>(produtos);
    }

    public ArrayList<Produto> getProdutosBD() {
        produtos = bdHelper.getAllProdutosBD();
        return new ArrayList<>(produtos);
    }

    public void atualizarProdutosBD(ArrayList<Produto> produtos) {
        bdHelper.removerAllProdutosBD();
        for (Produto produto : produtos) {
            bdHelper.adicionarProdutoBD(produto);
        }
    }

    public static synchronized void mqtt(MqttAndroidClient mqttClient, MqttListener mqttListener) {
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage msg) throws Exception {
                    String messageBody = new String(msg.getPayload());
                    if(mqttListener != null){
                        mqttListener.onMessageArrived(topic, messageBody);
                    }
                    System.out.println("Message arrived: " + messageBody);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete");
                }
                @Override
                public void connectionLost(Throwable exception) {
                    System.out.println("Connection lost");
                }
            });
    }

    public void loginAPI(final String username, final String password,  final Context context) {
        if(!AppJsonParser.isConnectionInternet(context))
        {
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_API + "/auth", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                token = AppJsonParser.parserJsonLogin(response);
                if (token != null)
                    USERNAME = username;
                if (loginListener != null) {
                    loginListener.onValidateLogin(token, username);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.txt_erro, Toast.LENGTH_SHORT).show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64Encode(username + ":" + password));
                System.out.println("Authorization: " + base64Encode("Basic " + username + ":" + password));
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void getAllProdutosAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (produtosListener != null) {
                produtosListener.onRefreshListaProdutos(getProdutosBD());
            }
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/produtos",null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    produtos = AppJsonParser.parserJsonProdutos(response);
                    atualizarProdutosBD(produtos);
                    if (produtosListener != null) {
                        produtosListener.onRefreshListaProdutos(produtos);
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, R.string.txt_erro, Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage() + "");
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public void delProdutoCarrinhoAPI(int id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL_API + "/carrinho/produto/" + id + "?auth_key=" + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("message") && response.optString("message").equals("No carrinho found."))
                    if (carrinhoListener != null) {
                        carrinhoListener.onCarrinhoRefresh(null);
                    }
                else
                    if (carrinhoListener != null) {
                        carrinhoListener.onCarrinhoUpdate(MainActivity.DELETE);
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(req);
    }

    public void addProdutoCarrinhoApi(int idProduto, int quantidade){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("idProduto", idProduto);
            jsonBody.put("quantidade", quantidade);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, URL_API + "/carrinho/produto?auth_key=" + token, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (carrinhoListener != null) {
                    carrinhoListener.onCarrinhoUpdate(MainActivity.ADD);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        requestQueue.add(req);
    }

    public void carrinhoCheckouApi(int id_morada, int id_loja){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_morada", id_morada);
            jsonBody.put("id_loja", id_loja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL_API + "/carrinho/checkout?auth_key=" + token, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (carrinhoListener != null) {
                    carrinhoListener.onCarrinhoCheckout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        requestQueue.add(req);
    }

    public Carrinho getCarrinhoBD() {
        carrinho = bdHelper.getCarrinhoBD();
        return carrinho;
    }

    public void getCarrinhoAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (carrinhoListener != null) {
                carrinhoListener.onCarrinhoRefresh(getCarrinhoBD());
            }
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/carrinho" + "?auth_key=" + token,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    carrinho = AppJsonParser.parserJsonCarrinho(response);
                    if (carrinhoListener != null) {
                        carrinhoListener.onCarrinhoRefresh(carrinho);
                    }
                    if (carrinho != null) {
                        bdHelper.adicionarCarrinhoBD(carrinho);
                    }
                    else {
                        if(getCarrinhoBD() != null) {
                            bdHelper.removerAllLinhasCarrinhoBD(getCarrinhoBD().getId());
                            bdHelper.removerAllCarrinhosBD();
                        }
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(getCarrinhoBD() != null){
                                bdHelper.removerAllLinhasCarrinhoBD(getCarrinhoBD().getId());
                                bdHelper.removerAllCarrinhosBD();
                            }
                            System.out.println(error.getMessage() + "");
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public void getAllSeccoesAPI(final Context context , final int id){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/seccao/" + id,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    seccao = AppJsonParser.parserJsonSeccoes(response);
                    if (seccoesListener != null) {
                        seccoesListener.onRefreshListaSeccoes(seccao);
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, R.string.txt_erro, Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage() + "");
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public void getSenhaDigitalAPI(int id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/seccao/senha/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SENHA = AppJsonParser.parserJsonSenhaDigital(response);
                if (senhaListener != null) {
                    senhaListener.onRefreshSenha(SENHA, null);
                }
            }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage() + "");
                    }
                }
        );

        requestQueue.add(req);
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public ArrayList<Seccao> getSeccoes() {
        if(seccao == null){
            seccao = new ArrayList<>();
        }
        return new ArrayList<>(seccao);
    }

    public void adicionarFavoritoApi(final Context context, final Produto produto){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", token);
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            int id = 0;
            try {
                id = favoritos.get(favoritos.size() - 1).getId();
            } catch (Exception ignored) {
            }
            Favorito favorito = new Favorito(id + 1, produto.getId());
            bdHelper.adicionarFavoritoBD(favorito);
            favoritos = bdHelper.getAllFavoritosBD();
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritos(favoritos);
            }
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL_API + "/favorito/" + produto.getId() + "?auth_key=" + token, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("message"))
                        Toast.makeText(context, response.optString("message"), Toast.LENGTH_SHORT).show();
                    else {
                        Favorito favorito = AppJsonParser.parserJsonFavorito(response);
                        favoritos.add(favorito);
                        bdHelper.adicionarFavoritoBD(favorito);
                        if (favoritosListener != null) {
                            favoritosListener.onRefreshListaFavoritos(favoritos);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.txt_erro, Toast.LENGTH_LONG).show();
                    System.out.println(error.getMessage() + "");
                }
            });
            requestQueue.add(req);
        }
    }

    public void getAllFavoritosAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            favoritos = bdHelper.getAllFavoritosBD();
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritos(favoritos);
            }
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/favorito" + "?auth_key=" + token, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    favoritos = AppJsonParser.parserJsonFavoritos(response);
                    atualizarFavoritosBD(favoritos);
                    if (favoritosListener != null) {
                        favoritosListener.onRefreshListaFavoritos(favoritos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage() + "");
                }
            });
            requestQueue.add(req);
        }
    }

    public ArrayList<Favorito> getFavoritosBD() {
        favoritos = bdHelper.getAllFavoritosBD();
        return new ArrayList<>(favoritos);
    }

    public void removerFavoritoApi(final Context context, final Produto produto){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            bdHelper.removerFavoritoBD(produto);
            favoritos = bdHelper.getAllFavoritosBD();
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritos(favoritos);
            }
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, URL_API + "/favorito/" + produto.getId() + "?auth_key=" + token, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("message") && !response.optString("message").equals("Produto removed"))
                        Toast.makeText(context, response.optString("message"), Toast.LENGTH_LONG).show();
                    for (Favorito favorito : favoritos) {
                        if(favorito.getId_produto() == produto.getId()){
                            favoritos.remove(favorito);
                            bdHelper.removerFavoritoBD(produto);
                            if (favoritosListener != null) {
                                favoritosListener.onRefreshListaFavoritos(favoritos);
                            }
                            break;
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.txt_erro, Toast.LENGTH_LONG).show();
                    System.out.println(error.getMessage() + "");
                }
            });
            requestQueue.add(req);
        }
    }

    public boolean isFavorito(Produto produto){
        for(Favorito favorito : favoritos){
            if(favorito.getId_produto() == produto.getId()){
                return true;
            }
        }
        return false;
    }
    public void atualizarFavoritosBD(ArrayList<Favorito> favoritos) {
        bdHelper.removerAllFavoritosBD();
        for (Favorito favorito : favoritos) {
            bdHelper.adicionarFavoritoBD(favorito);
        }
    }

    public void atualizarLojasBD(ArrayList<Loja> lojas) {
        bdHelper.removerAllLojasBD();
        for (Loja loja : lojas) {
            bdHelper.adicionarLojaBD(loja);
        }
    }

    public void adicionarCupaoAPI(Context context, String cupao){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/promocao/" + cupao + "?auth_key=" + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("message") && response.optString("message").equals("Promocao not found"))
                    Toast.makeText(context, R.string.txt_cupao_invalido, Toast.LENGTH_LONG).show();
                else{
                    carrinho = AppJsonParser.parserJsonCarrinho(response);
                    if(carrinhoListener != null)
                        carrinhoListener.onCarrinhoRefresh(carrinho);
                    Toast.makeText(context, R.string.txt_cupao_valido, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage() + "");
            }
        });
        requestQueue.add(req);
    }

    public void adicionarFavoritoBD(Favorito favorito) {
        bdHelper.adicionarFavoritoBD(favorito);
    }

    public void getAllLojasAPI(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/seccao/lojas", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lojas = AppJsonParser.parserJsonLojas(response);
                atualizarLojasBD(lojas);
                if (lojasListener != null) {
                    lojasListener.onRefreshListaLojas(lojas);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage() + "");
            }
        });
        requestQueue.add(req);
    }

    public ArrayList<Loja> getLojasBD() {
        lojas = bdHelper.getAllLojasBD();
        return new ArrayList<>(lojas);
    }

    private static String base64Encode(String value) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Base64.getEncoder()
                            .encodeToString(value.getBytes(StandardCharsets.UTF_8.toString()));
                }
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            return null;
    }

    public void createNotification(Context context, String title, String Smessage, String Bmessage){
        // Create an Intent for the activity you want to start
        Random random = new Random();
        int id = random.nextInt(9999 - 1000) + 1000;
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = context.getString(R.string.app_name);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Bmessage))
                .setContentText(Smessage)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) ContextCompat.getSystemService(context, NotificationManager.class);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, "My channel", importance);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id, builder.build());
    }
    //region User

    public void setUserListener(UserListener listener){
        this.userListener = listener;
    }

    public User getUser() { return user; }

    public void getUserDataAPI(final Context context){
            if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/user" + "?auth_key=" + token,null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            user = AppJsonParser.parserJsonUser(response);
                            if (user != null && userListener != null){
                                userListener.onRefreshUser(user);
                            }
                        }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage()+ "", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public void editarDadosAPI(final User user, final Context context){

        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }else{

            Map<String, String> params = new HashMap<>();
            params.put("token", token);
            params.put("nome", user.getNome());
            params.put("nif", user.getNif());
            params.put("telemovel", user.getTelemovel());
            params.put("email", user.getEmail());
            params.put("username", user.getUsername());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL_API + "/user/utilizador" + "?auth_key=" + token, new JSONObject(params),new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(context, R.string.txt_user_update, Toast.LENGTH_SHORT).show();

                    if(userListener != null){
                        userListener.onRefreshUser(user);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMoradaListener(MoradaListener listener){
        this.moradasListener = listener;
    }

    public Morada getMorada(int id){
        for(Morada m : user.getMoradas()){
            if(m.getId() == id){
                return m;
            }
        }
        return null;
    }

    public void editarMoradaAPI(final Morada morada, final Context context){

        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }else{

            Map<String, String> params = new HashMap<>();
            //params.put("token", token);
            params.put("rua", morada.getRua());
            params.put("cidade", morada.getCidade());
            params.put("cod_postal", morada.getCodPostal());
            params.put("pais", morada.getPais());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL_API + "/morada/" + morada.getId() + "?auth_key=" + token, new JSONObject(params),new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if(moradasListener != null){
                        moradasListener.onMoradasRefresh(MainActivity.EDIT);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }

    public void addMoradaAPI(final Morada morada, final Context context){

        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }else{

            Map<String, String> params = new HashMap<>();
            //params.put("token", token);
            params.put("rua", morada.getRua());
            params.put("cidade", morada.getCidade());
            params.put("cod_postal", morada.getCodPostal());
            params.put("pais", morada.getPais());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_API + "/morada" + "?auth_key=" + token, new JSONObject(params),new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    getUser().getMoradas().add(AppJsonParser.parseMorada(response));
                    if(moradasListener != null){
                        moradasListener.onMoradasRefresh(MainActivity.ADD);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }

    public void removerMoradaAPI(final Morada morada, final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(Request.Method.DELETE, URL_API + "/morada/" + morada.getId() + "?auth_key=" + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(moradasListener != null){
                        moradasListener.onMoradasRefresh(MainActivity.DELETE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(request);
        }
    }

    //endregion

    //region Encomenda

    public void setEncomendasListener(EncomendasListener listener){
        this.encomendasListener = listener;
    }

    public void getAllEncomendasAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/encomenda" + "?auth_key=" + token,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    encomendas = AppJsonParser.parserEncomendasJson(response);
                    if (encomendasListener != null) {
                        encomendasListener.onRefreshListaEncomendas(encomendas);
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage()+ "", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public Encomenda getEncomenda(int id){
        for(Encomenda e : encomendas){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }

    public void setDetalhesEncomendaListener(DetalhesEncomendaListener listener){
        this.detalhesEncomendaListener = listener;
    }


    public void getFaturaAPI(int id, final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            //Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/fatura/" + id + "?auth_key=" + token, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    fatura = AppJsonParser.parserFaturaJson(response);

                    if (detalhesEncomendaListener != null) {
                        detalhesEncomendaListener.onRefreshDetalhesEncomenda(fatura);
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage()+ "", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    //endregion

}
