package br.com.stanzione.gigigotest.home;

import java.util.List;

import br.com.stanzione.gigigotest.api.FakeStoreApi;

public class HomeModel implements HomeContract.Model {

    private FakeStoreApi fakeStoreApi;

    public HomeModel(FakeStoreApi fakeStoreApi){
        this.fakeStoreApi = fakeStoreApi;
    }

    @Override
    public List fetchProducts() {
        return null;
    }
}
