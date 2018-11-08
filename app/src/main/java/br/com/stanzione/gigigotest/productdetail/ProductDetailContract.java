package br.com.stanzione.gigigotest.productdetail;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;

public interface ProductDetailContract {

    interface View extends BaseView {
        void showProductDetails(Product product);
    }

    interface Presenter extends BasePresenter<View> {
        void getProductDetails(String productId);
    }

    interface Model {
        Observable<Product> fetchProduct(String productId);
    }

}
