package pl2.g7.iamsi.stuffngo.Models;

public class Favorito {
    private int id, id_produto;

    public Favorito(int id, int id_produto) {
        this.id = id;
        this.id_produto = id_produto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getId_produto() {
        return id_produto;
    }
}
