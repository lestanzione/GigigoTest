package br.com.stanzione.gigigotest.cart;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class CartModule {

    @Singleton
    @Provides
    CartContract.Model providesModel(Realm realm){
        CartModel model = new CartModel(realm);
        return model;
    }

    @Singleton
    @Provides
    CartContract.Presenter providesPresenter(CartContract.Model model){
        CartPresenter presenter = new CartPresenter(model);
        return presenter;
    }

}
