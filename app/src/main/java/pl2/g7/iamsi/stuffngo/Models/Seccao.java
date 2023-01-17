package pl2.g7.iamsi.stuffngo.Models;

public class Seccao {
    private int id ;
    private String nome ;

    public Seccao(int id,String nome){
        this.id = id ;
        this.nome = nome ;
    }

    public int getId(){return id ;}

    public String getNome(){return nome ;}

}
