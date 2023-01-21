package pl2.g7.iamsi.stuffngo.Models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl2.g7.iamsi.stuffngo.Listeners.MoradaListener;
import pl2.g7.iamsi.stuffngo.Listeners.ProdutosListener;
import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;
import pl2.g7.iamsi.stuffngo.Views.MainActivity;

public class Singleton {

        private static Singleton INSTANCE = null;
        private ArrayList<Produto> produtos;
        private ArrayList<Seccao> seccao;
        private User user;
        private ProdutoBDHelper produtoBD;
        private ArrayList<SenhaDigital> senhasdigitais;
        private static RequestQueue requestQueue = null;
        private ProdutosListener produtosListener;
        private UserListener userListener;
        private MoradaListener moradasListener;

        public static final String URL = "http://10.0.2.2:8081";
        private static final String URL_API = URL + "/backend/web/api";

        private static final String TOKEN = "auth_key";

        public static synchronized Singleton getInstance(Context context) {
            if (INSTANCE == null) {
                INSTANCE = new Singleton(context);
                requestQueue = Volley.newRequestQueue(context);
            }
            return INSTANCE;
        }

        private Singleton(Context context){ //Constructor
            produtos = new ArrayList<>();
            produtoBD = new ProdutoBDHelper(context);
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

    public SenhaDigital getSenha(int id){

        for(SenhaDigital senhasdigitais : senhasdigitais){
            if(senhasdigitais.getId() == id){
                return senhasdigitais;
            }
        }

        return null;
    }

    public void setProdutosListener(ProdutosListener listener){
        this.produtosListener = listener;
    }

    public ArrayList<Produto> getProdutos() {
        return new ArrayList<>(produtos);
    }

    public ArrayList<Seccao> getSeccoes() {
        if(seccao == null){
            seccao = new ArrayList<>();
        }
        return new ArrayList<>(seccao);
    }

    public ArrayList<SenhaDigital> getSenhasdigitais() {
        if(senhasdigitais == null){
            senhasdigitais = new ArrayList<>();
        }
        return new ArrayList<>(senhasdigitais);
    }

    public ArrayList<Produto> getProdutosBD() {
        produtos = produtoBD.getAllProdutosBD();
        return new ArrayList<>(produtos);
    }

    public void atualizarProdutosBD(ArrayList<Produto> livros) {
        produtoBD.removerAllProdutosBD();
        for (Produto produto : produtos) {
            produtoBD.adicionarProdutoBD(produto);
        }
    }

    public void getAllProdutosAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, error.getMessage()+ "", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    //region User

    public void setUserListener(UserListener listener){
        this.userListener = listener;
    }

    public User getUser() { return user; }

    public void getUserDataAPI(final Context context, final String token){
            if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API + "/user" + "?auth_key=" + token,null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            user = AppJsonParser.parserJsonUser(response);
                            if (user != null) {
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

    public void editarDadosAPI(final User user, final Context context, final String token){

        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(context, "Utilizador editado com sucesso", Toast.LENGTH_SHORT).show();

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

    public void setMoradaListener(MoradaListener listener){
        this.moradasListener = listener;
    }

    public void editarMoradaAPI(final Morada morada, final Context context, final String token){

        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
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

    public void addMoradaAPI(final Morada morada, final Context context, final String token){

        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
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

    public void removerMoradaAPI(final Morada morada, final Context context, final String token){
        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
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

}
