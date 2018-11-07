package br.com.stanzione.gigigotest.home;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private HomeContract.Model model;

    public HomePresenter(HomeContract.Model model){
        this.model = model;
    }

    @Override
    public void getProducts() {

    }

    @Override
    public void attachView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {

    }
}
