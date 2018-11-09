package br.com.stanzione.gigigotest.data;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Order extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Date timestamp;
    private String endOfCardNumber;
    private String cardName;
    private double totalPrice;
    private RealmList<OrderItem> orderItemList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEndOfCardNumber() {
        return endOfCardNumber;
    }

    public void setEndOfCardNumber(String endOfCardNumber) {
        this.endOfCardNumber = endOfCardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RealmList<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(RealmList<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
