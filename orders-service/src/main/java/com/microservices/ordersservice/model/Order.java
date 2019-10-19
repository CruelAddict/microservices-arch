package com.microservices.ordersservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

    public Integer getId() {
        return id;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public double getTotalCost() {
        return Double.parseDouble(totalCost);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private final Integer id;
    private final Integer totalAmount;

    private OrderStatus orderStatus;

    public final String totalCost;

    public Order(Integer id,
                 Integer totalAmount,
                 Integer orderStatus,
                 String totalCost) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.totalCost = totalCost;
        this.orderStatus = OrderStatus.values()[orderStatus];
    }

    public Order(@JsonProperty("totalAmount") Integer totalAmount,
                 @JsonProperty("totalCost") String totalCost) {
        this.id = null;
        this.totalAmount = totalAmount;
        this.totalCost = totalCost;
        this.orderStatus = OrderStatus.COLLECTING;
    }
}
