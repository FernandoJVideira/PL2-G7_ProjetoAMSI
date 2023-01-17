package pl2.g7.iamsi.stuffngo.Models;

public class Produto {
    private int id, id_categoria;

    double preco_unit;
    private String nome , descricao, imagem;

    public Produto(int id, double preco_unit, int id_categoria, String nome, String descricao, String imagem) {
        this.id = id;
        this.preco_unit = preco_unit;
        this.id_categoria = id_categoria;
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getPreco_unit() {
        return preco_unit;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public String getNome() {
        return nome;
    }

    public String getImagem() {
        return imagem;
    }

    public String getDescricao() {
        return descricao;
    }
}
