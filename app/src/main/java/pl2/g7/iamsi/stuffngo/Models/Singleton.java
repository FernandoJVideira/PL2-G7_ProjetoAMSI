package pl2.g7.iamsi.stuffngo.Models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Listeners.ProdutosListener;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;

public class Singleton {

        private static Singleton INSTANCE = null;
        private ArrayList<Produto> produtos;
        private ArrayList<Seccao> seccao;
        private ProdutoBDHelper produtoBD;
        private ArrayList<SenhaDigital> senhasdigitais;
        private static RequestQueue requestQueue = null;
        private ProdutosListener produtosListener;

        public static final String URL = "http://192.168.137.108/PL2-G7_ProjetoPlatSI";
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

}
