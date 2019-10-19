package com.microservices.ordersservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemDto {
    @JsonProperty("item_id")
    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    private final int itemId;
    private final int amount;

    public OrderItemDto(@JsonProperty("item_id") int itemId, @JsonProperty("amount") int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }
}
