package pl2.g7.iamsi.stuffngo.Models;

import java.util.ArrayList;

public class User {


    private String username, nome, email, nif, telemovel;
    private ArrayList<Morada> moradas;

public User( String username, String nome, String email, String nif, String telemovel, ArrayList<Morada> moradas) {

        this.username = username;
        this.nome = nome;
        this.email = email;
        this.nif = nif;
        this.telemovel = telemovel;
        this.moradas = moradas;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public String getEmail() {
        return email;
    }

    public String getNif() {
        return nif;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setMoradas(ArrayList<Morada> moradas) {
        this.moradas = moradas;
    }

    public ArrayList<Morada> getMoradas() {
        return moradas;
    }
    public ArrayList<Morada> getMoradasActivas() {
        ArrayList<Morada> moradasActivas = new ArrayList<>();
        for (Morada m : moradas) {
            if (m.isActive()) {
                moradasActivas.add(m);
            }
        }
        return moradasActivas;
    }




}
