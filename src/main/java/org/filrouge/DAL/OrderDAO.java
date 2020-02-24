package org.filrouge.DAL;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

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

    public static Map<Integer, Map.Entry<Integer, Boolean>> getOrderProducts(int id){
        Map<Integer, Map.Entry<Integer, Boolean>> products = new HashMap<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from madeof where orderID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                products.put(res.getInt("productID"), new AbstractMap.SimpleEntry<>(res.getInt("quantity"), res.getBoolean("delivered")));
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

    public static Discount getDiscount(String code){
        Discount discount = new Discount();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from discounts where discountID = ?");
            stmt.setString(1, code);
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

        if(!entry.isBlank()){ query = query.concat("&& (users.userID like '" + entry + "%' || login like '" + entry + "%') ");
        }
        if(state != -1) query = query.concat("&& state = " + state + " ");
        if(payed != -1) query = query.concat("&& payed = " + payed);
        query.concat(" order by orderID");

        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()){
                orders.add(new Order(res.getInt("orderID"), res.getInt("userID"), res.getString("login"),res.getDouble("price"), getOrderProducts(res.getInt("orderID")), AdressDAO.getAdress(res.getInt("billingadress")), AdressDAO.getAdress(res.getInt("deliveryadress")), res.getInt("state"), res.getBoolean("payed"), res.getInt("discountID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static boolean updateOrder(Order o){
        boolean updated = false;
        try {
            PreparedStatement stmt = db.prepareStatement("update orders set price = ?, deliveryadress = ?, billingadress = ?, state = ?, payed = ?, discountID = ? where orderID = ?");
            PreparedStatement stmtDelProducts = db.prepareStatement("delete from madeof where orderID = ?");
            PreparedStatement stmtAddProduct = db.prepareStatement("insert into madeof (orderID, productID, quantity, delivered) values (?,?,?,?)");
            stmt.setDouble(1, o.getPrice());
            stmt.setInt(2, o.getDeliveryAdress().getId());
            stmt.setInt(3, o.getBillingAdress().getId());
            stmt.setInt(4, o.getState());
            stmt.setBoolean(5, o.isPayed());
            stmt.setInt(6, o.getDiscount().getId());
            stmt.setInt(7, o.getId());
            if(stmt.executeUpdate() != 0){
                updated = true;
            }
            stmtDelProducts.setInt(1, o.getId());
            stmtAddProduct.setInt(1, o.getId());
            o.getProducts().forEach((id, details) -> {
                try {
                    stmtAddProduct.setInt(2, id);
                    stmtAddProduct.setInt(3, details.getKey());
                    stmtAddProduct.setBoolean(4, details.getValue());
                    stmtAddProduct.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }
}
