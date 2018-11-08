package br.com.stanzione.gigigotest.productdetail;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Product;
import br.com.stanzione.gigigotest.util.Configs;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = (Product) getIntent().getSerializableExtra(Configs.ARG_SELECTED_PRODUCT);

        setupUi();
    }

    private void setupUi(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }

    }

}
