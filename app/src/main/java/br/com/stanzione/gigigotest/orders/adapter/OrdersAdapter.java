package br.com.stanzione.gigigotest.orders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Order;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import br.com.stanzione.gigigotest.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList = new ArrayList();

    public OrdersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Order currentOrder = orderList.get(position);

        holder.orderItemTotalPriceTextView.setText(CurrencyUtil.convertToBrazilianCurrency(currentOrder.getTotalPrice()));
        holder.orderItemDateTextView.setText(DateUtil.formatDate(currentOrder.getTimestamp()));
        holder.orderItemNameTextView.setText(currentOrder.getCardName());
        holder.orderItemNumberTextView.setText(fillLeftUntil(currentOrder.getEndOfCardNumber(), 16));

    }

    @Override
    public int getItemCount() {
        return (null != orderList ? orderList.size() : 0);
    }

    private String fillLeftUntil(String initialText, int totalLength){
        StringBuilder stringBuilder = new StringBuilder(initialText);
        while (stringBuilder.length() != totalLength){
            stringBuilder.insert(0, "*");
        }
        return stringBuilder.toString();
    }

    public void setItems(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderItemTotalPriceTextView)
        TextView orderItemTotalPriceTextView;

        @BindView(R.id.orderItemNameTextView)
        TextView orderItemNameTextView;

        @BindView(R.id.orderItemDateTextView)
        TextView orderItemDateTextView;

        @BindView(R.id.orderItemNumberTextView)
        TextView orderItemNumberTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
