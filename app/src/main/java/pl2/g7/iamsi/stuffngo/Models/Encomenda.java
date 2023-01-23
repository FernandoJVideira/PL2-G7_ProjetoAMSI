package pl2.g7.iamsi.stuffngo.Models;

import java.util.ArrayList;

public class Encomenda {

    private int id, idMorada, idLoja; //idPromocao;
    private String estado, data;
    private ArrayList<LinhaCarrinho> linhas;

    public Encomenda(int id, int idMorada, int idLoja, /*int idPromocao,*/ String estado, String data, ArrayList<LinhaCarrinho> linhas) {
        this.id = id;
        this.idMorada = idMorada;
        this.idLoja = idLoja;
        //this.idPromocao = idPromocao;
        this.estado = estado;
        this.data = data;
        this.linhas = linhas;
    }

    public int getId() {
        return id;
    }

    public int getIdMorada() {
        return idMorada;
    }

    public int getIdLoja() {
        return idLoja;
    }

    /*public int getIdPromocao() {
        return idPromocao;
    }*/

    public String getEstado() {
        return estado;
    }

    public String getData() {
        return data;
    }

    public ArrayList<LinhaCarrinho> getLinhas() {
        return linhas;
    }
}
