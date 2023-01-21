package pl2.g7.iamsi.stuffngo.Models;

public class Categoria {
    private int id;
    private String nome;
    private int id_iva;

    public Categoria(int id, String nome, int id_iva) {
        this.id = id;
        this.nome = nome;
        this.id_iva = id_iva;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getId_iva() {
        return id_iva;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId_iva(int id_iva) {
        this.id_iva = id_iva;
    }
}
