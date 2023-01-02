package pl2.g7.iamsi.stuffngo;

public class Produtos {
    private int id, preco_unit,dataCriacao, ativo, id_categoria, imagem;
    private String nome , descricao ;

    public Produtos(int id, int preco_unit, int dataCriacao, int ativo, int id_categoria, String nome, String descricao, int imagem) {
        this.id = id;
        this.preco_unit = preco_unit;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
        this.id_categoria = id_categoria;
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
    }
    public int getId() {
        return id;
    }

    public int getPreco_unit() {
        return preco_unit;
    }

    public int getDataCriacao() {
        return dataCriacao;
    }

    public int getAtivo() {
        return ativo;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public String getNome() {
        return nome;
    }

    public int getImagem() {
        return imagem;
    }

    public String getDescricao() {
        return descricao;
    }
}
