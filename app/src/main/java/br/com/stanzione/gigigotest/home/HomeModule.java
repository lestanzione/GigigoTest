package br.com.stanzione.gigigotest.home;

import javax.inject.Singleton;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @Singleton
    @Provides
    HomeContract.Model providesModel(FakeStoreApi fakeStoreApi){
        HomeModel model = new HomeModel(fakeStoreApi);
        return model;
    }

    @Singleton
    @Provides
    HomePresenter providesPresenter(HomeContract.Model model){
        HomePresenter presenter = new HomePresenter(model);
        return presenter;
    }

}
