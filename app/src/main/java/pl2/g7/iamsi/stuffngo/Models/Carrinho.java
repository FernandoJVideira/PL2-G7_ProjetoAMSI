package pl2.g7.iamsi.stuffngo.Models;

import java.util.ArrayList;

public class Carrinho {
    private int id;
    private int idMorada;
    private int idLoja;
    private int idPromo;
    private String data;
    private ArrayList<LinhaCarrinho> linhas;
    private boolean estado;

    private Double subTotal, iva, desconto, total;

    public Carrinho(int id, String data, int idMorada, int idLoja, int idPromo, ArrayList<LinhaCarrinho> linhas, boolean estado, double subTotal, double iva, double desconto, double total) {
        this.id = id;
        this.data = data;
        this.idMorada = idMorada;
        this.idLoja = idLoja;
        this.idPromo = idPromo;
        this.estado = estado;
        this.linhas = linhas;
        this.subTotal = subTotal;
        this.iva = iva;
        this.desconto = desconto;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMorada() {
        return idMorada;
    }

    public int getIdLoja() {
        return idLoja;
    }

    public int getIdPromo() {
        return idPromo;
    }

    public String getData() {
        return data;
    }

    public ArrayList<LinhaCarrinho> getLinhas() {
        return linhas;
    }

    public boolean getEstado() {
        return estado;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Double getIva() {
        return iva;
    }

    public Double getDesconto() {
        return desconto;
    }

    public Double getTotal() {
        return total;
    }
}
