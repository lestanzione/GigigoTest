package br.com.stanzione.gigigotest;

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void dispose();
}
