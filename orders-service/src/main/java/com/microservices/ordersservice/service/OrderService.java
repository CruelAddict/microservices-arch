package com.microservices.ordersservice.service;

import com.microservices.ordersservice.dao.OrderDao;
import com.microservices.ordersservice.model.OrderDto;
import com.microservices.ordersservice.model.OrderItemDto;
import com.microservices.ordersservice.model.exceptions.DataIntegrityViolationException;
import com.microservices.ordersservice.model.Order;
import com.microservices.ordersservice.model.exceptions.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderService(@Qualifier("sqlite") OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderDto addOrder(Order order) throws DataIntegrityViolationException {
        return orderDao.addOrder(order);
    }

    public OrderDto getOrderByID(int id) throws ItemNotFoundException {
        Order foundOrder = orderDao.getOrderById(id);
        return new OrderDto(foundOrder, orderDao.getOrderItems(foundOrder));
    }

    public ArrayList<OrderItemDto> getOrderItems(int id) throws ItemNotFoundException {
        return orderDao.getOrderItems(getOrderByID(id));
    }

    public ArrayList<OrderDto> getOrders() {
        return orderDao.getOrders();
    }

    public OrderDto setOrderStatus(int id, String status) throws ItemNotFoundException, DataIntegrityViolationException {
        return orderDao.setOrderStatus(getOrderByID(id), status);
    }

    public OrderDto addItems(int id, OrderItemDto item) throws ItemNotFoundException, DataIntegrityViolationException {
        return orderDao.addItems(getOrderByID(id), item);
    }

}
