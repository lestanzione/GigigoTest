package br.com.stanzione.gigigotest.cart;

import java.util.List;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.CartItem;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface CartContract {

    interface View extends BaseView {
        void showCartItems(List<CartItem> cartItemList);
        void updateTotalPrice(String totalPrice);
        void removeCartItem(CartItem cartItem, int position);
    }

    interface Presenter extends BasePresenter<View> {
        void getCartItems();
        void calculateTotalPrice(List<CartItem> cartItemList);
        void removeCartItem(CartItem cartItem, int position);
    }

    interface Model {
        Observable<List<CartItem>> fetchCartItems();
        Completable deleteCartItem(CartItem cartItem);
    }

}
