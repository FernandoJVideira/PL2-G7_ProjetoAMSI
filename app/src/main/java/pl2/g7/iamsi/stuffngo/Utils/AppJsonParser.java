package pl2.g7.iamsi.stuffngo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Favorito;
import pl2.g7.iamsi.stuffngo.Models.Loja;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Seccao;

public class AppJsonParser {

    public static String parserJsonLogin(JSONObject response) {

        String token = null;

        try{
            if(response.has("token")){
                token = response.getString("token");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }


    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public static String parserJsonSenhaDigital(JSONObject response) {

        String number = null;

        try{
            if(response.has("number")){
                number = response.getString("number");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return number;
    }

    public static ArrayList<Produto> parserJsonProdutos(JSONArray response) {
        ArrayList<Produto> produtos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject produto = response.optJSONObject(i);
                int id = produto.optInt("idProduto");
                int idCategoria = produto.optInt("id_categoria");
                String nome = produto.optString("nome");
                String descricao = produto.optString("descricao");
                double preco = Double.parseDouble(produto.optString("preco_unit"));
                String imagem = produto.optString("imagem");
                produtos.add(new Produto(id, preco, idCategoria, nome, descricao, imagem));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public static ArrayList<Favorito> parserJsonFavoritos(JSONArray response) {
        ArrayList<Favorito> favoritos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject favorito = response.optJSONObject(i);
                int id = favorito.optInt("idFavorito");
                int idProduto = favorito.optInt("id_produto");
                favoritos.add(new Favorito(id, idProduto));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return favoritos;
    }

    public static Favorito parserJsonFavorito(JSONObject response) {
        Favorito favorito = null;
        try {
            int id = response.optInt("idFavorito");
            int idProduto = response.optInt("id_produto");
            favorito = new Favorito(id, idProduto);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return favorito;
    }

    public static ArrayList<Loja> parserJsonLojas(JSONArray response){
        ArrayList<Loja> lojas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject loja = response.optJSONObject(i);
                JSONObject morada = loja.getJSONObject("morada");
                lojas.add(new Loja(
                        loja.optInt("id"),
                        loja.optString("descricao"),
                        loja.optString("email"),
                        loja.optString("telefone"),
                        morada.getString("rua"),
                        morada.getString("cidade"),
                        morada.getString("cod_postal"),
                        morada.getString("pais")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return lojas;
    }

    public static ArrayList<Seccao> parserJsonSeccoes(JSONArray response){
        ArrayList<Seccao> seccoes = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject seccao = response.optJSONObject(i);
                seccoes.add(new Seccao(seccao.optInt("id"), seccao.optString("nome"), seccao.optInt("numeroAtual"), seccao.optInt("ultimoNumero")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return seccoes;
    }
}