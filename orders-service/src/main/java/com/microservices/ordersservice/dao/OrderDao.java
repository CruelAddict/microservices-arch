package com.microservices.ordersservice.dao;

import com.microservices.ordersservice.model.OrderDto;
import com.microservices.ordersservice.model.OrderItemDto;
import com.microservices.ordersservice.model.exceptions.DataIntegrityViolationException;
import com.microservices.ordersservice.model.Order;
import com.microservices.ordersservice.model.exceptions.ItemNotFoundException;

import java.util.ArrayList;

public interface OrderDao {



    default OrderDto addOrder(Order order) throws DataIntegrityViolationException {
        return null;
    }

    default Order getOrderById(int id) throws ItemNotFoundException {
        return null;
    }

    default ArrayList<OrderDto> getOrders() {
        return null;
    }

    default OrderDto setOrderStatus(Order order, String status) throws DataIntegrityViolationException, ItemNotFoundException {
        return null;
    }

    default OrderDto addItems(Order order, OrderItemDto item) throws DataIntegrityViolationException, ItemNotFoundException {
        return null;
    }

    default ArrayList<OrderItemDto> getOrderItems(Order order) throws ItemNotFoundException {
        return null;
    }
}
