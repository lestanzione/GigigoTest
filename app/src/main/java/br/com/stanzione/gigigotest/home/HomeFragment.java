package br.com.stanzione.gigigotest.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Product;
import br.com.stanzione.gigigotest.home.adapter.ProductsAdapter;
import br.com.stanzione.gigigotest.productdetail.ProductDetailActivity;
import br.com.stanzione.gigigotest.util.Configs;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements HomeContract.View, ProductsAdapter.OnProductSelectedListener {

    @BindView(R.id.productRecyclerView)
    RecyclerView productRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    HomeContract.Presenter presenter;

    private ProductsAdapter adapter;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupUi(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        setupInjector(context);
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getProducts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }

    private void setupInjector(Context context){
        ((App) (context.getApplicationContext()))
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    private void setupUi(View view){
        ButterKnife.bind(this, view);

        adapter = new ProductsAdapter(getContext());
        adapter.setListener(this);
        productRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProducts(List<Product> productList) {
        adapter.setItems(productList);
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showGeneralError() {
        Snackbar.make(productRecyclerView, getResources().getString(R.string.message_general_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(productRecyclerView , getResources().getString(R.string.message_network_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onProductSelected(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(Configs.ARG_SELECTED_PRODUCT, product);
        startActivity(intent);
    }
}
