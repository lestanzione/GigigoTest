package br.com.stanzione.gigigotest.productdetail;

import br.com.stanzione.gigigotest.data.Product;
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

}
