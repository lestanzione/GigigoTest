package br.com.stanzione.gigigotest.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class AndroidModule {

    @Singleton
    @Provides
    Realm providesRealm(){
        return Realm.getDefaultInstance();
    }

}
