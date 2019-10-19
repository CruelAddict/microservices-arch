package com.microservices.ordersservice.dao;

import com.microservices.ordersservice.model.OrderDto;
import com.microservices.ordersservice.model.OrderItemDto;
import com.microservices.ordersservice.model.OrderStatus;
import com.microservices.ordersservice.model.exceptions.DataIntegrityViolationException;
import com.microservices.ordersservice.model.Order;
import com.microservices.ordersservice.model.exceptions.ItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;

@Repository("sqlite")
public class OrderDataAccessService implements OrderDao {

    @Override
    public OrderDto addOrder(Order order) throws DataIntegrityViolationException {

        executeQueryUpdate(MessageFormat.format("INSERT INTO Orders VALUES(NULL, {0,number,#}, {1,number,#}, {2});",
                order.getTotalAmount(), order.getOrderStatus().ordinal(), order.totalCost));
        try {
            ArrayList<OrderDto> resultArray = selectOrders("SELECT * FROM Orders ORDER BY ID DESC LIMIT 1;");
            return resultArray.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException();
        }
    }

    @Override
    public ArrayList<OrderItemDto> getOrderItems(Order order) throws ItemNotFoundException {
        return selectItems(order.getId());
    }

    @Override
    public Order getOrderById(int id) throws ItemNotFoundException {
        try {
            return selectOrders("SELECT * FROM Orders WHERE ID = "+id+";").get(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new ItemNotFoundException();
        }
    }

    @Override
    public ArrayList<OrderDto> getOrders() {
        try {
            return selectOrders("SELECT * FROM Orders;");
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderDto setOrderStatus(Order order, String status) throws DataIntegrityViolationException, ItemNotFoundException {
        switch (status) {
            case("COLLECTING"):
                return putOrderStatus(order, OrderStatus.COLLECTING);
            case("PAID"):
                return putOrderStatus(order, OrderStatus.PAID);
            case("SHIPPING"):
                return putOrderStatus(order, OrderStatus.SHIPPING);
            case("COMPLETED"):
                return putOrderStatus(order, OrderStatus.COMPLETED);
            case("FAILED"):
                return putOrderStatus(order, OrderStatus.FAILED);
            case("CANCELLED"):
                return putOrderStatus(order, OrderStatus.CANCELLED);
            default:
                throw new DataIntegrityViolationException();

        }
    }

    @Override
    public OrderDto addItems(Order order, OrderItemDto item) throws DataIntegrityViolationException, ItemNotFoundException {
        executeQueryUpdate(MessageFormat.format("INSERT INTO OrdersItems VALUES({0,number,#}, {1,number,#}, {2,number,#});", order.getId(), item.getItemId(), item.getAmount()));
        return new OrderDto(order, getOrderItems(order));
    }

    private void executeQueryUpdate(String query) throws DataIntegrityViolationException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Orders.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException();
        } finally {
            try { connection.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private ArrayList<OrderDto> selectOrders(String query) throws ItemNotFoundException {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Orders.db");
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ArrayList<OrderDto> output = parseOrderSelect(rs);

            return output;

        } catch (IndexOutOfBoundsException e) {
            throw new ItemNotFoundException();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { connection.close(); } catch (Exception e) { e.printStackTrace(); }
            try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private ArrayList<OrderItemDto> selectItems(int id)throws ItemNotFoundException {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Orders.db");
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(MessageFormat.format("SELECT ItemID, OrderItemAmount FROM OrdersItems WHERE OrderID={0,number,#};", id));
            ArrayList<OrderItemDto> output = new ArrayList<>();
            while (rs.next()) {
                int item_id = rs.getInt("ItemId");
                int itemAmount = rs.getInt("OrderItemAmount");
                output.add(new OrderItemDto(item_id, itemAmount));
            }

            return output;

        } catch (IndexOutOfBoundsException e) {
            throw new ItemNotFoundException();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { connection.close(); } catch (Exception e) { e.printStackTrace(); }
            try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private OrderDto putOrderStatus(Order order, OrderStatus status) throws DataIntegrityViolationException, ItemNotFoundException {
        executeQueryUpdate(MessageFormat.format( "UPDATE Orders SET OrderStatus = {0,number,#} WHERE ID = {1,number,#} ;", status.ordinal(), order.getId()));
        order.setOrderStatus(status);
        order.setOrderStatus(status);

        return new OrderDto(order, getOrderItems(order));
    }

    private ArrayList<OrderDto> parseOrderSelect(ResultSet rs) throws SQLException, ItemNotFoundException {
        ArrayList<OrderDto> output = new ArrayList<>();
        while(rs.next()){
            int orderId  = rs.getInt("ID");
            int totalAmount = rs.getInt("TotalAmount");
            String totalCost = rs.getString("TotalCost");
            int orderStatus = rs.getInt("OrderStatus");
            Order currentOrder = new Order(orderId, totalAmount, orderStatus, totalCost);
            output.add(new OrderDto(currentOrder, getOrderItems(currentOrder)));
        }
        return output;
    }

}
