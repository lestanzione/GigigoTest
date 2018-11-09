package br.com.stanzione.gigigotest.productdetail;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductDetailPresenterTest {

    @Mock
    private ProductDetailContract.View mockView;
    @Mock
    private ProductDetailContract.Model mockModel;

    private ProductDetailPresenter presenter;

    @BeforeClass
    public static void setupRxSchedulers() {

        Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

    }

    @AfterClass
    public static void tearDownRxSchedulers(){
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        presenter = new ProductDetailPresenter(mockModel);
        presenter.attachView(mockView);
    }

    @Test
    public void withDatabaseShouldShowProductDetail(){

        Product product = new Product();
        String productId = "fake-id";
        product.setId(productId);

        when(mockModel.fetchProduct(productId)).thenReturn(Observable.just(product));

        presenter.getProductDetails(productId);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).showProductDetails(product);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockView, never()).showAddToCartFailureMessage();
        verify(mockView, never()).showAddToCartSuccessMessage();
        verify(mockModel, times(1)).fetchProduct(productId);
        verify(mockModel, never()).storeCartItem(any(Product.class), anyInt());

    }

    @Test
    public void withGeneralErrorShouldShowGeneralMessage(){

        when(mockModel.fetchProduct(anyString())).thenReturn(Observable.error(new Throwable()));

        presenter.getProductDetails(anyString());

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showProductDetails(any(Product.class));
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockView, never()).showAddToCartFailureMessage();
        verify(mockView, never()).showAddToCartSuccessMessage();
        verify(mockModel, times(1)).fetchProduct(anyString());
        verify(mockModel, never()).storeCartItem(any(Product.class), anyInt());

    }

    @Test
    public void withAddToCartShouldShowSuccessMessage(){

        Product product = new Product();
        int quantity = 1;

        when(mockModel.storeCartItem(product, quantity)).thenReturn(Completable.complete());

        presenter.addToCart(product, quantity);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showProductDetails(any(Product.class));
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockView, never()).showAddToCartFailureMessage();
        verify(mockView, times(1)).showAddToCartSuccessMessage();
        verify(mockModel, never()).fetchProduct(anyString());
        verify(mockModel, times(1)).storeCartItem(product, quantity);

    }

    @Test
    public void withAddToCartAndErrorShouldShowFailureMessage(){

        Product product = new Product();
        int quantity = 1;

        when(mockModel.storeCartItem(product, quantity)).thenReturn(Completable.error(new Throwable()));

        presenter.addToCart(product, quantity);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showProductDetails(any(Product.class));
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockView, times(1)).showAddToCartFailureMessage();
        verify(mockView, never()).showAddToCartSuccessMessage();
        verify(mockModel, never()).fetchProduct(anyString());
        verify(mockModel, times(1)).storeCartItem(product, quantity);

    }

}