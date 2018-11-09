package br.com.stanzione.gigigotest.cardinfo;

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

import br.com.stanzione.gigigotest.cart.CartPresenter;
import br.com.stanzione.gigigotest.data.CardInformation;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CardInfoPresenterTest {

    @Mock
    private CardInfoContract.View mockView;
    @Mock
    private CardInfoContract.Model mockModel;

    private CardInfoPresenter presenter;

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

        presenter = new CardInfoPresenter(mockModel);
        presenter.attachView(mockView);
    }

    @Test
    public void withNetworkShouldShowSuccessMessage(){

        CardInformation cardInformation = new CardInformation();

        when(mockModel.postCardInformation(cardInformation)).thenReturn(Completable.complete());
        when(mockModel.storeOrder(cardInformation)).thenReturn(Completable.complete());

        presenter.sendCardInformation(cardInformation);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).showSuccessMessage();
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).postCardInformation(cardInformation);
        verify(mockModel, times(1)).storeOrder(cardInformation);

    }

    @Test
    public void withNoNetworkShouldShowNetworkMessage(){

        CardInformation cardInformation = new CardInformation();

        when(mockModel.postCardInformation(cardInformation)).thenReturn(Completable.error(new IOException()));
        when(mockModel.storeOrder(cardInformation)).thenReturn(Completable.error(new IOException()));

        presenter.sendCardInformation(cardInformation);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showSuccessMessage();
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showGeneralError();
        verify(mockView, times(1)).showNetworkError();
        verify(mockModel, times(1)).postCardInformation(cardInformation);
        verify(mockModel, times(1)).storeOrder(cardInformation);

    }

    @Test
    public void withGeneralErrorShouldShowGeneralMessage(){

        CardInformation cardInformation = new CardInformation();

        when(mockModel.postCardInformation(cardInformation)).thenReturn(Completable.error(new Throwable()));
        when(mockModel.storeOrder(cardInformation)).thenReturn(Completable.error(new Throwable()));

        presenter.sendCardInformation(cardInformation);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, never()).showSuccessMessage();
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showGeneralError();
        verify(mockView, never()).showNetworkError();
        verify(mockModel, times(1)).postCardInformation(cardInformation);
        verify(mockModel, times(1)).storeOrder(cardInformation);

    }

}