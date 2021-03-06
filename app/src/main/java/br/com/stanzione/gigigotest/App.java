package br.com.stanzione.gigigotest;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import br.com.stanzione.gigigotest.cardinfo.CardInfoModule;
import br.com.stanzione.gigigotest.cart.CartModule;
import br.com.stanzione.gigigotest.di.AndroidModule;
import br.com.stanzione.gigigotest.di.ApplicationComponent;
import br.com.stanzione.gigigotest.di.DaggerApplicationComponent;
import br.com.stanzione.gigigotest.di.NetworkModule;
import br.com.stanzione.gigigotest.home.HomeModule;
import br.com.stanzione.gigigotest.orders.OrdersModule;
import br.com.stanzione.gigigotest.productdetail.ProductDetailModule;
import io.realm.Realm;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule())
                .networkModule(new NetworkModule())
                .homeModule(new HomeModule())
                .productDetailModule(new ProductDetailModule())
                .cartModule(new CartModule())
                .cardInfoModule(new CardInfoModule())
                .ordersModule(new OrdersModule())
                .build();

        Realm.init(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

}
