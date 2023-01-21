package pl2.g7.iamsi.stuffngo.Models;

public class Iva {
    private int id, iva, id_categoria;

    public Iva(int id, int iva, int id_categoria) {
        this.id = id;
        this.iva = iva;
        this.id_categoria = id_categoria;
    }

    public int getId() {
        return id;
    }

    public int getIva() {
        return iva;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
}
