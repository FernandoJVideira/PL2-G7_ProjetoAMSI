package pl2.g7.iamsi.stuffngo.Models;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import pl2.g7.iamsi.stuffngo.Listeners.FavoritosListener;
import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Listeners.ProdutosListener;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;

public class Singleton {

        private static Singleton INSTANCE = null;
        private ArrayList<Produto> produtos;
        private ArrayList<Favorito> favoritos;
        private ArrayList<Seccao> seccao;
        private BDHelper bdHelper;
        private ArrayList<SenhaDigital> senhasdigitais;
        private static RequestQueue requestQueue = null;
        private ProdutosListener produtosListener = null;
        private LoginListener loginListener = null;
        private FavoritosListener favoritosListener = null;
        public static final String URL = "http://10.0.2.2/PL2-G7_ProjetoPlatSI";
        private static final String URL_API = URL + "/backend/web/api";
        private static final String TOKEN = "?auth_key=EjcxGqpBhnEcyV4TPiSUjIQmTcPVLsHo";
        private String token;

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

    public void setFavoritosListener(FavoritosListener listener){
        this.favoritosListener = listener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
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
        produtos = bdHelper.getAllProdutosBD();
        return new ArrayList<>(produtos);
    }

    public void atualizarProdutosBD(ArrayList<Produto> produtos) {
        bdHelper.removerAllProdutosBD();
        for (Produto produto : produtos) {
            bdHelper.adicionarProdutoBD(produto);
        }
    }

    public void loginAPI(final String username, final String password,  final Context context) {
        if(!AppJsonParser.isConnectionInternet(context))
        {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_API + "/auth", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                token = AppJsonParser.parserJsonLogin(response);

                if (loginListener != null) {
                    loginListener.onValidateLogin(token);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Falha ao tentar aceder ao servidor", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Erro na ligação ao servidor", Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage() + "");
                        }
                    }
            );

            requestQueue.add(req);
        }
    }

    public void adicionarFavoritoApi(final Context context, final Produto produto){
        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritos(getFavoritosBD());
            }
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL_API + "/favorito/" + produto.getId() + TOKEN, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("message"))
                        Toast.makeText(context, response.optString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "Erro na ligação ao servidor", Toast.LENGTH_LONG).show();
                    System.out.println(error.getMessage() + "");
                }
            });
            requestQueue.add(req);
        }
    }

    public void getAllFavoritosAPI(final Context context){
        if(!AppJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritos(getFavoritosBD());
            }
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL_API + "/favorito" + TOKEN, null, new Response.Listener<JSONArray>() {
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
                    Toast.makeText(context, "Erro na ligação ao servidor", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, URL_API + "/favorito/" + produto.getId() + TOKEN, null, new Response.Listener<JSONObject>() {
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
                    Toast.makeText(context, "Erro na ligação ao servidor", Toast.LENGTH_LONG).show();
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

    public void adicionarFavoritoBD(Favorito favorito) {
        bdHelper.adicionarFavoritoBD(favorito);
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
}
