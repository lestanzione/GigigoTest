package br.com.stanzione.gigigotest.home;

import javax.inject.Singleton;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class HomeModule {

    @Singleton
    @Provides
    HomeContract.Model providesModel(FakeStoreApi fakeStoreApi, Realm realm){
        HomeModel model = new HomeModel(fakeStoreApi, realm);
        return model;
    }

    @Singleton
    @Provides
    HomeContract.Presenter providesPresenter(HomeContract.Model model){
        HomePresenter presenter = new HomePresenter(model);
        return presenter;
    }

}
