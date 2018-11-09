package br.com.stanzione.gigigotest.productdetail;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ProductDetailContract {

    interface View extends BaseView {
        void showProductDetails(Product product);
        void showAddToCartSuccessMessage();
        void showAddToCartFailureMessage();
    }

    interface Presenter extends BasePresenter<View> {
        void getProductDetails(String productId);
        void addToCart(Product product, int quantity);
    }

    interface Model {
        Observable<Product> fetchProduct(String productId);
        Completable storeCartItem(Product product, int quantity);
    }

}
