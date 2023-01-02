package pl2.g7.iamsi.stuffngo;

import java.util.ArrayList;

public class SingletonProdutos {

        private static SingletonProdutos INSTANCE = null;
        private ArrayList<Produtos> produtos;

        public static synchronized SingletonProdutos getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new SingletonProdutos();
            }
            return INSTANCE;
        }

        private SingletonProdutos(){ //Constructor

            gerarProdutos();
        }

        public Produtos getProduto(int id){

            for(Produtos produtos : produtos){
                if(produtos.getId() == id){
                    return produtos;
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

        public ArrayList<Produtos> getProdutos() {

            return new ArrayList(produtos);
        }
}
