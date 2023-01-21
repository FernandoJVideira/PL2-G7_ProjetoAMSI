package pl2.g7.iamsi.stuffngo.Listeners;

import pl2.g7.iamsi.stuffngo.Models.Carrinho;

public interface CarrinhoListener {
    void onCarrinhoCupao();
    void onCarrinhoCheckout();
    void onCarrinhoRefresh(Carrinho carrinho);
    void onCarrinhoUpdate(int type);


}
