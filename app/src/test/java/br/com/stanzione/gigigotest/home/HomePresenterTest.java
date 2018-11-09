package br.com.stanzione.gigigotest.home;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomePresenterTest {

    @Mock
    private HomeContract.View mockView;
    @Mock
    private HomeContract.Model mockModel;

    private HomePresenter presenter;

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

        presenter = new HomePresenter(mockModel);
        presenter.attachView(mockView);
    }

    @Test
    public void withNetworkShouldShowProductList(){

        List<Product> productList = new ArrayList<>();

        when(mockModel.fetchProducts()).thenReturn(Observable.just(productList));

        presenter.getProducts();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).showProducts(productList);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchProducts();
        verify(mockModel, times(1)).storeProducts(productList);

    }

    @Test
    public void withNoNetworkShouldShowNetworkMessage(){

        when(mockModel.fetchProducts()).thenReturn(Observable.error(new IOException()));

        presenter.getProducts();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showProducts(anyList());
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, times(1)).showNetworkError();
        verify(mockModel, times(1)).fetchProducts();
        verify(mockModel, never()).storeProducts(anyList());

    }

    @Test
    public void withGeneralErrorShouldShowGeneralMessage(){

        when(mockModel.fetchProducts()).thenReturn(Observable.error(new Throwable()));

        presenter.getProducts();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showProducts(anyList());
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchProducts();
        verify(mockModel, never()).storeProducts(anyList());

    }

}