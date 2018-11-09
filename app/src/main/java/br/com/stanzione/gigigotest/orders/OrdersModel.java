package br.com.stanzione.gigigotest.orders;

import java.util.List;

import br.com.stanzione.gigigotest.data.Order;
import io.reactivex.Observable;
import io.realm.Realm;

public class OrdersModel implements OrdersContract.Model {

    private Realm realm;

    public OrdersModel(Realm realm){
        this.realm = realm;
    }

    @Override
    public Observable<List<Order>> fetchOrders() {
        List<Order> orderList = realm.copyFromRealm(realm.where(Order.class).findAll());
        return Observable.just(orderList);
    }
}
