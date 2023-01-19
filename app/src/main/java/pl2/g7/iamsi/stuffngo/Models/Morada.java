package pl2.g7.iamsi.stuffngo.Models;

public class Morada {

    private int id;
    private String rua, cidade, cod_postal, pais;

    public Morada(int id, String rua, String cidade, String cod_postal, String pais) {
        this.id = id;
        this.rua = rua;
        this.cidade = cidade;
        this.cod_postal = cod_postal;
        this.pais = pais;
    }

    public int getId() {
        return id;
    }

    public String getRua() {
        return rua;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCod_postal() {
        return cod_postal;
    }

    public String getPais() {
        return pais;
    }
}
