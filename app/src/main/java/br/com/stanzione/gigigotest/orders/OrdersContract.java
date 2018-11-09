package br.com.stanzione.gigigotest.orders;

import java.util.List;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.Order;
import io.reactivex.Observable;

public interface OrdersContract {

    interface View extends BaseView {
        void showOrders(List<Order> orderList);
        void setEmptyStateVisible(boolean visible);
    }

    interface Presenter extends BasePresenter<View> {
        void getOrders();
    }

    interface Model {
        Observable<List<Order>> fetchOrders();
    }

}
