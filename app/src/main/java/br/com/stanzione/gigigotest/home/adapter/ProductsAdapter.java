package br.com.stanzione.gigigotest.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Product;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList = new ArrayList();

    public ProductsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.productNameTextView.setText(currentProduct.getName());
        holder.productPriceTextView.setText(CurrencyUtil.convertToBrazilianCurrency(currentProduct.getPrice()));

        Glide.with(context)
                .load(currentProduct.getImageUrl())
                .apply(
                        new RequestOptions()
                                .centerCrop()
                                .fitCenter()
                )
                .into(holder.productImageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != productList ? productList.size() : 0);
    }

    public void setItems(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productLayout)
        LinearLayout linearLayout;

        @BindView(R.id.productImageView)
        ImageView productImageView;

        @BindView(R.id.productNameTextView)
        TextView productNameTextView;

        @BindView(R.id.productPriceTextView)
        TextView productPriceTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
