package br.com.stanzione.gigigotest.productdetail;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ProductDetailModule {

    @Singleton
    @Provides
    ProductDetailContract.Model providesModel(Realm realm){
        ProductDetailModel model = new ProductDetailModel(realm);
        return model;
    }

    @Singleton
    @Provides
    ProductDetailContract.Presenter providesPresenter(ProductDetailContract.Model model){
        ProductDetailPresenter presenter = new ProductDetailPresenter(model);
        return presenter;
    }

}
