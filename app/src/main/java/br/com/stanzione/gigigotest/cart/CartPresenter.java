package br.com.stanzione.gigigotest.cart;

import java.io.IOException;
import java.util.List;

import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class CartPresenter implements CartContract.Presenter {

    private CartContract.View view;
    private CartContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CartPresenter(CartContract.Model model){
        this.model = model;
    }

    @Override
    public void getCartItems() {

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                model.fetchCartItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onCartItemsReceived,
                                this::onCartItemsError
                        )
        );
    }

    @Override
    public void removeCartItem(CartItem cartItem, int position) {

        compositeDisposable.add(
                model.deleteCartItem(cartItem)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> onCartItemDeleted(cartItem, position))
        );
    }

    @Override
    public void attachView(CartContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    public void calculateTotalPrice(List<CartItem> cartItemList){
        double total = 0.0;

        for(CartItem cartItem : cartItemList){
            total += (cartItem.getQuantity() * cartItem.getPrice());
        }

        view.updateTotalPrice(CurrencyUtil.convertToBrazilianCurrency(total));
    }

    private void onCartItemsReceived(List<CartItem> cartItemList) {
        view.setProgressBarVisible(false);
        view.showCartItems(cartItemList);
        calculateTotalPrice(cartItemList);
    }

    private void onCartItemsError(Throwable throwable) {
        view.setProgressBarVisible(false);
        if(throwable instanceof IOException){
            view.showNetworkError();
        }
        else{
            view.showGeneralError();
        }
    }

    private void onCartItemDeleted(CartItem cartItem, int position){
        view.removeCartItem(cartItem, position);
    }

}
