package br.com.stanzione.gigigotest.cart;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.stanzione.gigigotest.data.CartItem;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartPresenterTest {

    @Mock
    private CartContract.View mockView;
    @Mock
    private CartContract.Model mockModel;

    private CartPresenter presenter;

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

        presenter = new CartPresenter(mockModel);
        presenter.attachView(mockView);
    }

    @Test
    public void withDatabaseShouldShowProductDetail(){

        List<CartItem> cartItemList = new ArrayList<>();

        when(mockModel.fetchCartItems()).thenReturn(Observable.just(cartItemList));

        presenter.getCartItems();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).showCartItems(cartItemList);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).updateTotalPrice(anyString());
        verify(mockView, never()).removeCartItem(any(CartItem.class), anyInt());
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchCartItems();
        verify(mockModel, never()).deleteCartItem(any(CartItem.class));

    }

    @Test
    public void withGeneralErrorShouldShowGeneralMessage(){

        when(mockModel.fetchCartItems()).thenReturn(Observable.error(new Throwable()));

        presenter.getCartItems();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showCartItems(anyList());
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).updateTotalPrice(anyString());
        verify(mockView, never()).removeCartItem(any(CartItem.class), anyInt());
        verify(mockView, times(1)).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchCartItems();
        verify(mockModel, never()).deleteCartItem(any(CartItem.class));

    }

    @Test
    public void withRemoveItemShouldUpdateList(){

        CartItem cartItem = new CartItem();
        int position = 1;

        when(mockModel.deleteCartItem(cartItem)).thenReturn(Completable.complete());

        presenter.removeCartItem(cartItem, position);

        verify(mockView, never()).setProgressBarVisible(true);
        verify(mockView, never()).showCartItems(anyList());
        verify(mockView, never()).setProgressBarVisible(false);
        verify(mockView, never()).updateTotalPrice(anyString());
        verify(mockView, times(1)).removeCartItem(cartItem, position);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, never()).fetchCartItems();
        verify(mockModel, times(1)).deleteCartItem(cartItem);

    }

}