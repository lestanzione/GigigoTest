package br.com.stanzione.gigigotest.home;

import java.util.List;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;

public class HomeModel implements HomeContract.Model {

    private FakeStoreApi fakeStoreApi;

    public HomeModel(FakeStoreApi fakeStoreApi){
        this.fakeStoreApi = fakeStoreApi;
    }

    @Override
    public Observable<List<Product>> fetchProducts() {
        return fakeStoreApi.getProducts();
    }
}
