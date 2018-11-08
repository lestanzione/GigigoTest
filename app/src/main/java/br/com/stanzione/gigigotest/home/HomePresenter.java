package br.com.stanzione.gigigotest.home;

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
                    .subscribe(new Consumer<List<Product>>() {
                                   @Override
                                   public void accept(List<Product> products) throws Exception {
                                        System.out.println("Products: " + products.size());
                                        view.showProducts(products);
                                        view.setProgressBarVisible(false);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    view.setProgressBarVisible(false);
                                    throwable.printStackTrace();
                                }
                            })
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
}
