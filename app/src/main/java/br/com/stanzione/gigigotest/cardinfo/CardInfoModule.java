package br.com.stanzione.gigigotest.cardinfo;

import javax.inject.Singleton;

import br.com.stanzione.gigigotest.api.FakeStoreApi;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class CardInfoModule {

    @Singleton
    @Provides
    CardInfoContract.Model providesModel(FakeStoreApi fakeStoreApi, Realm realm){
        CardInfoModel model = new CardInfoModel(fakeStoreApi, realm);
        return model;
    }

    @Singleton
    @Provides
    CardInfoContract.Presenter providesPresenter(CardInfoContract.Model model){
        CardInfoPresenter presenter = new CardInfoPresenter(model);
        return presenter;
    }

}
