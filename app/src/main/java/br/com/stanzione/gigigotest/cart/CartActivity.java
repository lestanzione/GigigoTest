package br.com.stanzione.gigigotest.cart;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.cardinfo.CardInfoActivity;
import br.com.stanzione.gigigotest.cart.adapter.CartItemsAdapter;
import br.com.stanzione.gigigotest.data.CartItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity implements CartContract.View {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.cartRecyclerView)
    RecyclerView cartRecyclerView;

    @BindView(R.id.cartTotalAmountTextView)
    TextView cartTotalAmountTextView;

    @BindView(R.id.cartPayButton)
    Button cartPayButton;

    @Inject
    CartContract.Presenter presenter;

    private CartItemsAdapter adapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupUi();
        setupInjector();

        presenter.getCartItems();
    }

    private void setupUi(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CartItemsAdapter(this);
        cartRecyclerView.setAdapter(adapter);
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        enableSwipe();
    }

    private void setupInjector(){
        ((App) (getApplicationContext()))
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    @OnClick(R.id.cartPayButton)
    public void onCartPayButtonClicked(){
        if(null != cartItemList && !cartItemList.isEmpty()) {
            Intent intent = new Intent(this, CardInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }

    }

    @Override
    public void showCartItems(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        adapter.setItems(cartItemList);
    }

    @Override
    public void updateTotalPrice(String totalPrice) {
        cartTotalAmountTextView.setText(totalPrice);
    }

    @Override
    public void removeCartItem(CartItem cartItem, int position) {
        adapter.removeItem(position);
        presenter.calculateTotalPrice(cartItemList);
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
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.message_general_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(coordinatorLayout , getResources().getString(R.string.message_network_error), Snackbar.LENGTH_LONG).show();
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    CartItem cartItem = cartItemList.get(position);
                    presenter.removeCartItem(cartItem, position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(cartRecyclerView);
    }

}
