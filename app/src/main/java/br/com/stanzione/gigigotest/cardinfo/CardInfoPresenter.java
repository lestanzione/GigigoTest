package br.com.stanzione.gigigotest.cardinfo;

import java.io.IOException;

import br.com.stanzione.gigigotest.data.CardInformation;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CardInfoPresenter implements CardInfoContract.Presenter {

    private CardInfoContract.View view;
    private CardInfoContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CardInfoPresenter(CardInfoContract.Model model){
        this.model = model;
    }

    @Override
    public void sendCardInformation(CardInformation cardInformation) {

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                model.postCardInformation(cardInformation)
                        .andThen(model.storeOrder(cardInformation))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onOrderProcessed,
                                this::onOrderError
                        )
        );
    }

    @Override
    public void attachView(CardInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    public void onOrderProcessed(){
        view.setProgressBarVisible(false);
        view.showSuccessMessage();
    }

    public void onOrderError(Throwable throwable){
        view.setProgressBarVisible(false);
        if(throwable instanceof IOException){
            view.showNetworkError();
        }
        else{
            view.showGeneralError();
        }
    }

}
