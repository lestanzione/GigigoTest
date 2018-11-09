package br.com.stanzione.gigigotest.orders;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.stanzione.gigigotest.data.Order;
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

public class OrdersPresenterTest {

    @Mock
    private OrdersContract.View mockView;
    @Mock
    private OrdersContract.Model mockModel;

    private OrdersPresenter presenter;

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

        presenter = new OrdersPresenter(mockModel);
        presenter.attachView(mockView);
    }

    @Test
    public void withDatabaseShouldShowOrderList(){

        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order());

        when(mockModel.fetchOrders()).thenReturn(Observable.just(orderList));

        presenter.getOrders();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, never()).setEmptyStateVisible(true);
        verify(mockView, times(1)).showOrders(orderList);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchOrders();

    }

    @Test
    public void withEmptyDatabaseShouldShowOrderList(){

        List<Order> orderList = new ArrayList<>();

        when(mockModel.fetchOrders()).thenReturn(Observable.just(orderList));

        presenter.getOrders();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, times(1)).setEmptyStateVisible(true);
        verify(mockView, never()).showOrders(anyList());
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchOrders();

    }

    @Test
    public void withGeneralErrorShouldShowGeneralMessage(){

        when(mockModel.fetchOrders()).thenReturn(Observable.error(new Throwable()));

        presenter.getOrders();

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, never()).setEmptyStateVisible(true);
        verify(mockView, never()).showOrders(anyList());
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).fetchOrders();

    }

}