package br.com.stanzione.gigigotest.di;

import javax.inject.Singleton;

import br.com.stanzione.gigigotest.cart.CartActivity;
import br.com.stanzione.gigigotest.cart.CartModule;
import br.com.stanzione.gigigotest.home.HomeFragment;
import br.com.stanzione.gigigotest.home.HomeModule;
import br.com.stanzione.gigigotest.productdetail.ProductDetailActivity;
import br.com.stanzione.gigigotest.productdetail.ProductDetailModule;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AndroidModule.class,
                NetworkModule.class,
                HomeModule.class,
                ProductDetailModule.class,
                CartModule.class
        }
)
public interface ApplicationComponent {
    void inject(HomeFragment fragment);
    void inject(ProductDetailActivity activity);
    void inject(CartActivity activity);
}
