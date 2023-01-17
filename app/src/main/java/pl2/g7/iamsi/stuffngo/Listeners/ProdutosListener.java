package pl2.g7.iamsi.stuffngo.Listeners;

import java.util.ArrayList;

import pl2.g7.iamsi.stuffngo.Models.Produto;

public interface ProdutosListener {
    void onRefreshListaProdutos(ArrayList<Produto> produtos);
}
