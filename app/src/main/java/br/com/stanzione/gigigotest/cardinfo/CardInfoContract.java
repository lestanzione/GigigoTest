package br.com.stanzione.gigigotest.cardinfo;

import br.com.stanzione.gigigotest.BasePresenter;
import br.com.stanzione.gigigotest.BaseView;
import br.com.stanzione.gigigotest.data.CardInformation;
import io.reactivex.Completable;

public interface CardInfoContract {

    interface View extends BaseView {
        void showSuccessMessage();
    }

    interface Presenter extends BasePresenter<View> {
        void sendCardInformation(CardInformation cardInformation);
    }

    interface Model {
        Completable postCardInformation(CardInformation cardInformation);
        Completable storeOrder(CardInformation cardInformation);
    }

}
