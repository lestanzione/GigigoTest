package br.com.stanzione.gigigotest.cardinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import br.com.stanzione.gigigotest.data.CardInformation;
import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.data.Order;
import br.com.stanzione.gigigotest.data.OrderItem;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.realm.Realm;
import io.realm.RealmList;

public class CardInfoModel implements CardInfoContract.Model {

    private FakeStoreApi fakeStoreApi;
    private Realm realm;

    public CardInfoModel(FakeStoreApi fakeStoreApi, Realm realm){
        this.fakeStoreApi = fakeStoreApi;
        this.realm = realm;
    }

    @Override
    public Completable postCardInformation(CardInformation cardInformation) {
        return fakeStoreApi.postOrder(cardInformation.toJsonObject());
    }

    @Override
    public Completable storeOrder(CardInformation cardInformation) {

        realm.executeTransactionAsync(realm -> {
                    List<CartItem> cartItemList = realm.where(CartItem.class).findAll();
                    RealmList<OrderItem> orderItemList = new RealmList<>();
                    OrderItem orderItem;
                    double totalPrice = 0.0;

                    for (CartItem cartItem : cartItemList) {
                        orderItem = new OrderItem();
                        orderItem.setName(cartItem.getName());
                        orderItem.setPrice(cartItem.getPrice());
                        orderItem.setQuantity(cartItem.getQuantity());
                        totalPrice += (cartItem.getQuantity() * cartItem.getPrice());

                        orderItemList.add(orderItem);
                    }

                    Order order = new Order();
                    order.setTimestamp(new Date());
                    order.setEndOfCardNumber(cardInformation.getNumber().substring(cardInformation.getNumber().length() - 4, cardInformation.getNumber().length()));
                    order.setCardName(cardInformation.getName());
                    order.setOrderItemList(orderItemList);
                    order.setTotalPrice(totalPrice);

                    realm.copyToRealm(order);
                    realm.where(CartItem.class).findAll().deleteAllFromRealm();

                });

        return  Completable.complete();
    }
}
