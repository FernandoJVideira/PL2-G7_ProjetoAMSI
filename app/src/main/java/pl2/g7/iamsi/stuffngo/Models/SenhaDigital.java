package pl2.g7.iamsi.stuffngo.Models;

public class SenhaDigital {
    private int id, id_seccao,NumeroAtual, UltimoNumero;

    public SenhaDigital(int id,int id_seccao,int NumeroAtual,int UltimoNumero){
        this.id = id ;
        this.id_seccao = id_seccao;
        this.NumeroAtual = NumeroAtual;
        this.UltimoNumero = UltimoNumero;

    }
    public int getId(){return id ;}
    public int getId_seccao(){return id_seccao;}
    public int getNumeroAtual() {return NumeroAtual;}
    public int getUltimoNumero() {return UltimoNumero;}

}
