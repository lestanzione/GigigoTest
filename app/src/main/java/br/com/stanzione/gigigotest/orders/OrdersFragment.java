package br.com.stanzione.gigigotest.orders;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Order;
import br.com.stanzione.gigigotest.orders.adapter.OrdersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends Fragment implements OrdersContract.View {

    @BindView(R.id.orderRecyclerView)
    RecyclerView orderRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.orderEmptyStateTextView)
    TextView orderEmptyStateTextView;

    @Inject
    OrdersContract.Presenter presenter;

    private OrdersAdapter adapter;

    public OrdersFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
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
        adapter.setItems(new ArrayList<>());
        presenter.getOrders();
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

        getActivity().setTitle(R.string.title_orders);

        adapter = new OrdersAdapter(getContext());
        orderRecyclerView.setAdapter(adapter);
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void showOrders(List<Order> orderList) {
        adapter.setItems(orderList);
    }

    @Override
    public void setEmptyStateVisible(boolean visible) {
        if (visible) {
            orderEmptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            orderEmptyStateTextView.setVisibility(View.INVISIBLE);
        }
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
        Snackbar.make(orderRecyclerView, getResources().getString(R.string.message_general_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(orderRecyclerView , getResources().getString(R.string.message_network_error), Snackbar.LENGTH_LONG).show();
    }
}
