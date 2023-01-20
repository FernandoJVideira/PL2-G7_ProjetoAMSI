package pl2.g7.iamsi.stuffngo.Models;

public class Loja {
    private int id ;
    private String descricao ;
    private String rua ;
    private String cidade ;
    private String cod_postal ;
    private String pais ;
    private String email;
    private String telefone;

    public Loja(int id, String descricao, String email, String telefone,String rua, String cidade, String cod_postal, String pais){
        this.id = id;
        this.descricao = descricao;
        this.email = email;
        this.telefone = telefone;
        this.rua = rua;
        this.cidade = cidade;
        this.cod_postal = cod_postal;
        this.pais = pais;
    }

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public String getDescricao(){return descricao;}

    public String getEmail(){return email;}

    public String getTelefone(){return telefone;}

    public String getRua(){return rua;}

    public String getCidade(){return cidade;}

    public String getCod_postal(){return cod_postal;}

    public String getPais(){return pais;}

}
