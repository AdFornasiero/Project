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
            PreparedStatement stmt = db.prepareStatement("select * from orders, users where orderID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                order = new Order(res.getInt("orderID"), res.getInt("userID"), res.getString("login"), res.getDouble("price"), getOrderProducts(res.getInt("orderID")), AdressDAO.getAdress(res.getInt("billingadress")), AdressDAO.getAdress(res.getInt("deliveryadress")), res.getInt("state"), res.getBoolean("payed"), res.getInt("discountID"));
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
            ResultSet res = stmt.executeQuery("select * from orders, users where orders.userID = users.userID order by orderID");
            while(res.next()){
                orders.add(new Order(res.getInt("orderID"), res.getInt("userID"), res.getString("login"),res.getDouble("price"), getOrderProducts(res.getInt("orderID")), AdressDAO.getAdress(res.getInt("billingadress")), AdressDAO.getAdress(res.getInt("deliveryadress")), res.getInt("state"), res.getBoolean("payed"), res.getInt("discountID")));
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
            PreparedStatement stmt = db.prepareStatement("select * from discounts where discountID = ?");
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

    public static List<Order> searchOrders(String entry, int state, int payed){
        List<Order> orders = new ArrayList<>();
        String query = "select * from orders, users where orders.userID = users.userID ";

        if(!entry.isBlank()){
            query = query.concat("&& (users.userID like '" + entry + "%' || login like '" + entry + "%') ");
        }

        if(state != -1) query = query.concat("&& state = " + state + " ");
        if(payed != -1) query = query.concat("&& payed = " + payed);
        query.concat(" order by orderID");
        System.out.println(query);
        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()){
                orders.add(new Order(res.getInt("orderID"), res.getInt("userID"), res.getString("login"),res.getDouble("price"), getOrderProducts(res.getInt("orderID")), AdressDAO.getAdress(res.getInt("billingadress")), AdressDAO.getAdress(res.getInt("deliveryadress")), res.getInt("state"), res.getBoolean("payed"), res.getInt("discountID")));
            }
            /*if(!entry.isBlank()) stmt.setString(1, entry);
            if(state != -1) query.concat("&& state = ? ");
            if(payed != -1) query.concat("&& payed = ?");*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

}
