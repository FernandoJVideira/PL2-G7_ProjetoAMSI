package pl2.g7.iamsi.stuffngo;

import java.util.ArrayList;

public class Singleton {

        private static Singleton INSTANCE = null;
        private ArrayList<Produtos> produtos;
        private ArrayList<Seccao> seccao ;
        private ArrayList<SenhaDigital> senhasdigitais;

        public static synchronized Singleton getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new Singleton();
            }
            return INSTANCE;
        }

        private Singleton(){ //Constructor

            gerarProdutos(); //Otimizar isto, pq está a carregar os 3 quando invoco o SngletonProdutos, ou seja vai estar a gerar dados desnecessáriso;
            gerarSeccoes();
            gerarSenhas();
        }

        public Produtos getProduto(int id){

            for(Produtos produtos : produtos){
                if(produtos.getId() == id){
                    return produtos;
                }
            }

            return null;
        }

        public Seccao getSeccao(int id){

            for(Seccao seccao : seccao){
                if(seccao.getId() == id){
                    return seccao;
                }
            }

            return null;
        }

    public SenhaDigital getSenha(int id){

        for(SenhaDigital senhasdigitais : senhasdigitais){
            if(senhasdigitais.getId() == id){
                return senhasdigitais;
            }
        }

        return null;
    }

        private void gerarProdutos() {
            produtos = new ArrayList<>();
            produtos.add(new Produtos(1, 2000, 266, 1,1, "Bon Jovi", "Crush", R.drawable.its_my_life));
            produtos.add(new Produtos(2, 2012, 250, 1,1, "Imagine Dragons", "Night Visions", R.drawable.radioative));
            produtos.add(new Produtos(3, 2008, 240, 1,2, "ColdPlay", "Death and All His Friends", R.drawable.viva_la_vida));
            produtos.add(new Produtos(4, 1988, 180, 1,1, "Xutos & Pontapés", "88", R.drawable.xutos_pontapes));
        }

        private void gerarSeccoes(){
            seccao = new ArrayList<>();
            seccao.add(new Seccao(1, "A"));
            seccao.add(new Seccao(2,"B"));
            seccao.add(new Seccao(3,"C"));

        }

        private void gerarSenhas(){
            senhasdigitais = new ArrayList<>();
            senhasdigitais.add(new SenhaDigital( 1,1,0,0));
            senhasdigitais.add(new SenhaDigital(2,2,0,0));
            senhasdigitais.add(new SenhaDigital(3,2,0,0));

        }

        public ArrayList<Produtos> getProdutos() {

            return new ArrayList(produtos);
        }

        public ArrayList<Seccao> getSeccoes() {

            return new ArrayList(seccao);
        }

    public ArrayList<SenhaDigital> getSenhasdigitais() {

        return new ArrayList(senhasdigitais);
    }
}
