package br.com.stanzione.gigigotest.cart;

import java.util.List;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.CartItem;
import io.reactivex.Observable;

public interface CartContract {

    interface View extends BaseView {
        void showCartItems(List<CartItem> cartItemList);
        void updateTotalPrice(String totalPrice);
    }

    interface Presenter extends BasePresenter<View> {
        void getCartItems();
    }

    interface Model {
        Observable<List<CartItem>> fetchCartItems();
    }

}
