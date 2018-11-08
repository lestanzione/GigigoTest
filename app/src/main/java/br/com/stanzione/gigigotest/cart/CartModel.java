package br.com.stanzione.gigigotest.cart;

import java.util.List;

import br.com.stanzione.gigigotest.data.CartItem;
import io.reactivex.Observable;
import io.realm.Realm;

public class CartModel implements CartContract.Model {

    private Realm realm;

    public CartModel(Realm realm) {
        this.realm = realm;
    }

    @Override
    public Observable<List<CartItem>> fetchCartItems() {
        List<CartItem> cartItemList = realm.where(CartItem.class).findAll();
        if(null == cartItemList){
            return Observable.error(new Throwable());
        }
        else {
            return Observable.just(realm.copyFromRealm(cartItemList));
        }
    }
}
