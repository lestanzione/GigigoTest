package br.com.stanzione.gigigotest.home;

import java.util.List;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;

public class HomeContract {
    interface View extends BaseView{
        void showProducts(List<Product> productList);
    }

    interface Presenter extends BasePresenter<View>{
        void getProducts();
    }

    interface Model{
        Observable<List<Product>> fetchProducts();
    }
}
