package br.com.stanzione.gigigotest.productdetail;

import java.io.IOException;
import java.util.List;

import br.com.stanzione.gigigotest.data.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    private ProductDetailContract.View view;
    private ProductDetailContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProductDetailPresenter(ProductDetailContract.Model model){
        this.model = model;
    }

    @Override
    public void getProductDetails(String productId) {

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                model.fetchProduct(productId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onProductReceived,
                                this::onProductError
                        )
        );
    }

    @Override
    public void addToCart(Product product, int quantity) {

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                model.storeCartItem(product, quantity)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onItemAddedToCart,
                                this::onItemAddedToCartError
                        )
        );
    }

    @Override
    public void attachView(ProductDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    private void onProductReceived(Product product){
        view.setProgressBarVisible(false);
        view.showProductDetails(product);
    }

    private void onProductError(Throwable throwable){
        view.setProgressBarVisible(false);
        if(throwable instanceof IOException){
            view.showNetworkError();
        }
        else{
            view.showGeneralError();
        }
    }

    private void onItemAddedToCart(){
        view.setProgressBarVisible(false);
        view.showAddToCartSuccessMessage();
    }

    private void onItemAddedToCartError(Throwable throwable){
        view.setProgressBarVisible(false);
        view.showAddToCartFailureMessage();
    }
}
