package pl2.g7.iamsi.stuffngo.Models;

import pl2.g7.iamsi.stuffngo.Views.MainActivity;

public class LinhaCarrinho {
    private int id;
    private int idCarrinho;
    private int idProduto;
    private int quantidade;

    public LinhaCarrinho(int id, int idCarrinho, int idProduto, int quantidade) {
        this.id = id;
        this.idCarrinho = idCarrinho;
        this.idProduto = idProduto;
        this.quantidade = quantidade;

    }

    public int getId() {
        return id;
    }

    public int getIdCarrinho() {
        return idCarrinho;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

}
