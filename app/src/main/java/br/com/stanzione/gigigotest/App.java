package br.com.stanzione.gigigotest;

import android.app.Application;

import br.com.stanzione.gigigotest.di.ApplicationComponent;
import br.com.stanzione.gigigotest.di.DaggerApplicationComponent;
import br.com.stanzione.gigigotest.di.NetworkModule;
import br.com.stanzione.gigigotest.home.HomeModule;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .homeModule(new HomeModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
