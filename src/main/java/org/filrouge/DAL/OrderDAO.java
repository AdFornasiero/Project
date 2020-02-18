package org.filrouge.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO {

    static Connection db = DBConnect.connect();

    public static Order getOrder(int id){
        Order order = new Order();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from orders where orderID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                order.setId(res.getInt("orderID"));
                order.setOwner(res.getInt("userID"));
                order.setPrice(res.getDouble("price"));
                order.setState(res.getInt("state"));
                order.setPayed(res.getBoolean("payed"));
                order.setBillingAdress(AdressDAO.getAdress(res.getInt("billingadress")));
                order.setDeliveryAdress(AdressDAO.getAdress(res.getInt("deliveryadress")));
                if(getDiscount(res.getInt("discountID")).getId() != null){
                    order.setDiscount(getDiscount(res.getInt("discountID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public static List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select * from orders");
            while(res.next()){
                orders.add(new Order(res.getInt("orderID"), res.getInt("userID"), res.getDouble("price"), getOrderProducts(res.getInt("orderID")), AdressDAO.getAdress(res.getInt("billingadress")), AdressDAO.getAdress(res.getInt("deliveryadress")), res.getInt("state"), res.getBoolean("payed"), res.getInt("discount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static HashMap<Integer, Integer> getOrderProducts(int id){
        HashMap<Integer, Integer> products = new HashMap<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from madeof where orderID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                products.put(res.getInt("productID"), res.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    public static Discount getDiscount(int id){
        Discount discount = new Discount();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from discount where discountID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                discount.setId(res.getInt("discountID"));
                discount.setCode(res.getString("code"));
                discount.setPercentValue(res.getInt("percentvalue"));
                discount.setFixedValue(res.getInt("fixedvalue"));
                discount.setMinAmount(res.getInt("minamount"));
                discount.setActive(res.getBoolean("active"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }

}
