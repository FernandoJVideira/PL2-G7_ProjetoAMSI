package pl2.g7.iamsi.stuffngo.Models;

public class Seccao {
    private int id;
    private int numeroActual;
    private int ultimoNumero;
    private String nome;

    public Seccao(int id, String nome, int numeroActual, int ultimoNumero){
        this.id = id;
        this.nome = nome;
        this.numeroActual = numeroActual;
        this.ultimoNumero = ultimoNumero;
    }

    public int getId(){return id;}
    public String getNome(){return nome;}
    public int getNumeroActual(){return numeroActual;}
    public int getUltimoNumero(){return ultimoNumero;}

}
