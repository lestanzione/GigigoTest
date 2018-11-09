package br.com.stanzione.gigigotest.orders;

import java.io.IOException;
import java.util.List;

import br.com.stanzione.gigigotest.data.Order;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrdersPresenter implements OrdersContract.Presenter {

    private OrdersContract.View view;
    private OrdersContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public OrdersPresenter(OrdersContract.Model model){
        this.model = model;
    }

    @Override
    public void getOrders() {

        view.setProgressBarVisible(true);
        view.setEmptyStateVisible(false);

        compositeDisposable.add(
                model.fetchOrders()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onOrdersReceived,
                                this::onOrdersError
                        )
        );
    }

    @Override
    public void attachView(OrdersContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    private void onOrdersReceived(List<Order> orderList){
        view.setProgressBarVisible(false);
        if(orderList.isEmpty()){
            view.setEmptyStateVisible(true);
        }
        else {
            view.showOrders(orderList);
        }
    }

    private void onOrdersError(Throwable throwable){
        view.setProgressBarVisible(false);
        if(throwable instanceof IOException){
            view.showNetworkError();
        }
        else{
            view.showGeneralError();
        }
    }
}
