package pl2.g7.iamsi.stuffngo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.User;

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

    public static User parserJsonUser(JSONObject response) {
        User user = null;
        ArrayList<Morada> moradas = new ArrayList<>();

        try {
            if(!response.has("dados") || !response.has("moradas"))
            {
                return null;
            }

            JSONObject dados = response.getJSONObject("dados");
            JSONArray moradasJson = response.getJSONArray("moradas");

            String nome = dados.getString("nome");
            String telemovel = dados.optString("telemovel");
            String nif = dados.optString("nif");
            String username = dados.getString("username");
            String email = dados.getString("email");

            for (int i = 0; i < moradasJson.length(); i++)
            {
                JSONObject moradaJSON = moradasJson.getJSONObject(i);

                int idMorada = moradaJSON.getInt("idMorada");
                String rua = moradaJSON.getString("rua");
                String cidade = moradaJSON.getString("cidade");
                String codigoPostal = moradaJSON.getString("cod_postal");
                String pais = moradaJSON.getString("pais");

                moradas.add(new Morada(idMorada, rua, cidade, codigoPostal, pais));
            }

            user = new User(username, nome, email, nif, telemovel, moradas);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
