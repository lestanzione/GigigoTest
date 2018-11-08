package br.com.stanzione.gigigotest.productdetail;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Product;
import br.com.stanzione.gigigotest.util.Configs;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.productDetailImageView)
    ImageView productDetailImageView;

    @BindView(R.id.productDetailNameTextView)
    TextView productDetailNameTextView;

    @BindView(R.id.productDetailPriceTextView)
    TextView productDetailPriceTextView;

    @BindView(R.id.productDetailQuantityTextView)
    TextView productDetailQuantityTextView;

    @BindView(R.id.productDetailLessImageButton)
    ImageButton productDetailLessImageButton;

    @BindView(R.id.productDetailMoreImageButton)
    ImageButton productDetailMoreImageButton;

    @BindView(R.id.productDetailAddCartButton)
    Button productDetailAddCartButton;

    @Inject
    ProductDetailContract.Presenter presenter;

    int quantity = 1;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setupUi();
        setupInjector();

        String productId = getIntent().getStringExtra(Configs.ARG_SELECTED_PRODUCT);
        presenter.getProductDetails(productId);

    }

    private void setupUi(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productDetailQuantityTextView.setText(String.valueOf(quantity));
    }

    private void setupInjector(){
        ((App) (getApplicationContext()))
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    @OnClick(R.id.productDetailLessImageButton)
    public void onLessImageButtonClicked(){
        if(quantity > 1){
            quantity--;
        }
        productDetailQuantityTextView.setText(String.valueOf(quantity));
    }

    @OnClick(R.id.productDetailMoreImageButton)
    public void onMoreImageButtonClicked(){
        quantity++;
        productDetailQuantityTextView.setText(String.valueOf(quantity));
    }

    @OnClick(R.id.productDetailAddCartButton)
    public void onAddToCartButtonClicked(){
        presenter.addToCart(product, quantity);
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
    public void showProductDetails(Product product) {
        this.product = product;

        productDetailNameTextView.setText(product.getName());
        productDetailPriceTextView.setText(CurrencyUtil.convertToBrazilianCurrency(product.getPrice()));
        productDetailQuantityTextView.setText("1");

        Glide.with(getApplicationContext())
                .load(product.getImageUrl())
                .apply(
                        new RequestOptions()
                                .centerCrop()
                                .fitCenter()
                )
                .into(productDetailImageView);
    }

    @Override
    public void showAddToCartSuccessMessage() {
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.message_add_cart_success), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddToCartFailureMessage() {
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.message_add_cart_failure), Snackbar.LENGTH_LONG).show();
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
}
