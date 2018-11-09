package br.com.stanzione.gigigotest.home;

import java.util.List;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;
import io.realm.Realm;

public class HomeModel implements HomeContract.Model {

    private FakeStoreApi fakeStoreApi;
    private Realm realm;

    public HomeModel(FakeStoreApi fakeStoreApi, Realm realm){
        this.fakeStoreApi = fakeStoreApi;
        this.realm = realm;
    }

    @Override
    public Observable<List<Product>> fetchProducts() {
        return fakeStoreApi.getProducts();
    }

    @Override
    public void storeProducts(List<Product> productList) {
        realm.executeTransactionAsync(realm -> {
            realm.delete(Product.class);
            realm.copyToRealm(productList);
        });
    }
}
