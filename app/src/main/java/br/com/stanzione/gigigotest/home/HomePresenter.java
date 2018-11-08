package br.com.stanzione.gigigotest.home;

import java.io.IOException;
import java.util.List;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private HomeContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomePresenter(HomeContract.Model model){
        this.model = model;
    }

    @Override
    public void getProducts() {

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                model.fetchProducts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::onProductsReceived,
                            this::onProductsError
                    )
        );
    }

    @Override
    public void attachView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    private void onProductsReceived(List<Product> productList){
        model.storeProducts(productList);
        view.showProducts(productList);
        view.setProgressBarVisible(false);
    }

    private void onProductsError(Throwable throwable){
        view.setProgressBarVisible(false);
        if(throwable instanceof IOException){
            view.showNetworkError();
        }
        else{
            view.showGeneralError();
        }
    }
}
