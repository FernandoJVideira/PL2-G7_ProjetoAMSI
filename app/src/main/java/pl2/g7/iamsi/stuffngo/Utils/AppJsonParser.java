package pl2.g7.iamsi.stuffngo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Carrinho;
import pl2.g7.iamsi.stuffngo.Models.Encomenda;
import pl2.g7.iamsi.stuffngo.Models.Favorito;
import pl2.g7.iamsi.stuffngo.Models.LinhaCarrinho;
import pl2.g7.iamsi.stuffngo.Models.Loja;
import pl2.g7.iamsi.stuffngo.Models.Morada;
import pl2.g7.iamsi.stuffngo.Models.Produto;
import pl2.g7.iamsi.stuffngo.Models.Seccao;
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

    public static Carrinho parserJsonCarrinho(JSONObject carrinho){
        if(carrinho.has("message"))
            return null;
        ArrayList<LinhaCarrinho> linhasCarrinho = new ArrayList<>();
        try{
            if(carrinho.has("linhascarrinho")){
                JSONArray linhas = carrinho.getJSONArray("linhascarrinho");
                for(int i = 0; i < linhas.length(); i++){
                    JSONObject linha = linhas.getJSONObject(i);
                    linhasCarrinho.add(parserJsonLinhaCarrinho(linha));
                }
            }
            JSONObject cart = carrinho.getJSONObject("carrinho");
            JSONObject dados = carrinho.getJSONObject("dados");
            return new Carrinho(cart.getInt("idCarrinho"), cart.getString("data_criacao"), cart.optInt("id_morada", -1), cart.optInt("id_loja", -1), cart.optInt("id_promocao", -1), linhasCarrinho, false, dados.optDouble("subTotal"), dados.optDouble("iva"), dados.optDouble("desconto"), dados.optDouble("total"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static LinhaCarrinho parserJsonLinhaCarrinho(JSONObject linha) {
        try {
            return new LinhaCarrinho(linha.getInt("idLinha"), linha.getInt("id_carrinho"), linha.getInt("id_produto"), linha.getInt("quantidade"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static ArrayList<Encomenda> parserEncomendasJson(JSONArray response) {
        ArrayList<Encomenda> encomenda = new ArrayList<>();
        Encomenda encomendaAux = null;
        ArrayList<LinhaCarrinho> linhas = new ArrayList<>();

        for(int i = 0; i < response.length(); i++){
            try {
                JSONObject respObj = (JSONObject) response.get(i);

                if (!respObj.has("encomenda") || !respObj.has("linhas")) {
                    return null;
                }

                JSONObject encomendaJson = respObj.getJSONObject("encomenda");
                JSONArray linhasJson = respObj.getJSONArray("linhas");

                int id = encomendaJson.getInt("idCarrinho");
                String estado = encomendaJson.getString("estado");
                String data = encomendaJson.getString("data_criacao");
                int idMorada = encomendaJson.getInt("id_morada");
                int idLoja = encomendaJson.getInt("id_loja");

                linhas = new ArrayList<>();
                for (int j = 0; j < linhasJson.length(); j++) {

                    JSONObject linhaJSON = linhasJson.getJSONObject(j);

                    int idLinha = linhaJSON.getInt("idLinha");
                    int quantidade = linhaJSON.getInt("quantidade");
                    int idProduto = linhaJSON.getInt("id_produto");

                    linhas.add(new LinhaCarrinho(idLinha, id, idProduto, quantidade));
                }

                encomendaAux = new Encomenda(id, idMorada, idLoja, estado, data, linhas);
                encomenda.add(encomendaAux);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
        return encomenda;
    }

    public static String parserFaturaJson(JSONObject response) {

        String fatura = null;
        try {
            fatura = response.getString("fatura");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fatura;
    }
}