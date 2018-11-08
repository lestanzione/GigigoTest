package br.com.stanzione.gigigotest.cart.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItemList = new ArrayList();

    public CartItemsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CartItem currentCartItem = cartItemList.get(position);

        holder.cartItemNameTextView.setText(currentCartItem.getName());

        int quantity = currentCartItem.getQuantity();
        double price = currentCartItem.getPrice();

        String description = quantity + " x " + CurrencyUtil.convertToBrazilianCurrency(price);
        String total = CurrencyUtil.convertToBrazilianCurrency(quantity * price);

        holder.cartItemDescriptionTextView.setText(description);
        holder.cartItemTotalPriceTextView.setText(total);

        Glide.with(context)
                .load(currentCartItem.getImageUrl())
                .apply(
                        new RequestOptions()
                                .centerCrop()
                                .fitCenter()
                )
                .into(holder.cartItemImageView);

        holder.constraintLayout.setOnClickListener(view -> {});

    }

    @Override
    public int getItemCount() {
        return (null != cartItemList ? cartItemList.size() : 0);
    }

    public void setItems(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cartItemLayout)
        ConstraintLayout constraintLayout;

        @BindView(R.id.cartItemImageView)
        ImageView cartItemImageView;

        @BindView(R.id.cartItemNameTextView)
        TextView cartItemNameTextView;

        @BindView(R.id.cartItemDescriptionTextView)
        TextView cartItemDescriptionTextView;

        @BindView(R.id.cartItemTotalPriceTextView)
        TextView cartItemTotalPriceTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
