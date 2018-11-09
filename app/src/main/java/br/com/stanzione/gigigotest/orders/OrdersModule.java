package br.com.stanzione.gigigotest.orders;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class OrdersModule {

    @Singleton
    @Provides
    OrdersContract.Model providesModel(Realm realm){
        OrdersModel model = new OrdersModel(realm);
        return model;
    }

    @Singleton
    @Provides
    OrdersContract.Presenter providesPresenter(OrdersContract.Model model){
        OrdersPresenter presenter = new OrdersPresenter(model);
        return presenter;
    }

}
