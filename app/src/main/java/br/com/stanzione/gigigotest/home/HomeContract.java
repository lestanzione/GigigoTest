package br.com.stanzione.gigigotest.home;

import java.util.List;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;

public class HomeContract {
    interface View extends BaseView{
        void showProducts(List productList);
    }

    interface Presenter extends BasePresenter<View>{
        void getProducts();
    }
    
    interface Model{
        List fetchProducts();
    }
}
