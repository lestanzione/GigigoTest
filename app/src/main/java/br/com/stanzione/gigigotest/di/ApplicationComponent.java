package br.com.stanzione.gigigotest.di;

import javax.inject.Singleton;

import br.com.stanzione.gigigotest.home.HomeFragment;
import br.com.stanzione.gigigotest.home.HomeModule;
import dagger.Component;

@Singleton
@Component(
        modules = {
                NetworkModule.class,
                HomeModule.class
        }
)
public interface ApplicationComponent {
    void inject(HomeFragment fragment);
}
