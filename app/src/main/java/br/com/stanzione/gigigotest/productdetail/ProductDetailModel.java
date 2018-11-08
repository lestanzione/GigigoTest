package br.com.stanzione.gigigotest.productdetail;

import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.realm.Realm;

public class ProductDetailModel implements ProductDetailContract.Model {

    private Realm realm;

    public ProductDetailModel(Realm realm){
        this.realm = realm;
    }

    @Override
    public Observable<Product> fetchProduct(String productId) {
        Product product = realm.where(Product.class).equalTo(Product.COLUMN_ID, productId).findFirst();
        if(null == product){
            return Observable.error(new Throwable());
        }
        else {
            return Observable.just(product);
        }
    }

    @Override
    public Completable storeCartItem(Product product, int quantity) {

        CartItem cartItem = new CartItem();
        cartItem.setName(product.getName());
        cartItem.setPrice(product.getPrice());
        cartItem.setImageUrl(product.getImageUrl());
        cartItem.setQuantity(quantity);

        return Completable.create(
                emitter -> realm.executeTransactionAsync(realm -> {
                    realm.copyToRealm(cartItem);
                },
                emitter::onComplete,
                emitter::onError)
        );

    }

}
