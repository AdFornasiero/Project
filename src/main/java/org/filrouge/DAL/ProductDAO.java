package org.filrouge.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    static Connection db = DBConnect.connect();

    public static List<Product> getProducts(){
        List<Product> list = new ArrayList<>();
        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select * from products");
            while(res.next()){
                Product p = new Product(res.getInt("productID"),
                        res.getInt("supplierID"),
                        res.getString("label"),
                        res.getString("reference"),
                        res.getString("maker"),
                        res.getDouble("ptprice"),
                        res.getInt("categoryID"),
                        res.getString("description"),
                        res.getDate("adddate"),
                        res.getDate("updatedate"),
                        res.getBoolean("available"),
                        res.getInt("stock"),
                        res.getInt("discount"));

                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean removeProduct(int id){
        boolean removed = false;
        try {
            PreparedStatement stmt = db.prepareStatement("delete from products where productID = ?");
            stmt.setInt(1, id);
            if(stmt.executeUpdate() == 1){
                removed = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removed;
    }

    public static boolean updateProduct(Product p){
        boolean updated = false;
        try {
            PreparedStatement stmt = db.prepareStatement("update products set label = ?, reference = ?, maker = ?, ptprice = ?, discount = ?, description = ?, stock = ?, updatedate = ?, available = ?, supplierID = ?, categoryID = ? where productID = ?");
            stmt.setString(1, p.getLabel());
            stmt.setString(2, p.getReference());
            stmt.setString(3, p.getMaker());
            stmt.setDouble(4, p.getPrice());
            stmt.setInt(5, p.getDiscount());
            stmt.setString(6, p.getDescription());
            stmt.setInt(7, p.getStock());
            stmt.setDate(8, p.getUpdatedate());
            stmt.setBoolean(9, p.isAvailable());
            stmt.setInt(10, p.getSupplier().getId());
            stmt.setInt(11, p.getCategory().getId());
            stmt.setInt(12, p.getId());

            if(stmt.executeUpdate() == 1){
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public static boolean addProduct(Product p){
        boolean added = false;
        try {
            PreparedStatement stmt = db.prepareStatement("insert into products (label, reference, maker, ptprice, discount, description, stock, adddate, available, supplierID, categoryID) values (?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, p.getLabel());
            stmt.setString(2, p.getReference());
            stmt.setString(3, p.getMaker());
            stmt.setDouble(4, p.getPrice());
            stmt.setInt(5, p.getDiscount());
            stmt.setString(6, p.getDescription());
            stmt.setInt(7, p.getStock());
            stmt.setDate(8, p.getAdddate());
            stmt.setBoolean(9, p.isAvailable());
            stmt.setInt(10, p.getSupplier().getId());
            stmt.setInt(11, p.getCategory().getId());

            if(stmt.executeUpdate() == 1){
                added = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    public static Product getProduct(int id){
        Product p = null;
        try {
            PreparedStatement stmt = db.prepareStatement("select * from products where productID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                p = new Product(res.getInt("productID"),
                        res.getInt("supplierID"),
                        res.getString("label"),
                        res.getString("reference"),
                        res.getString("maker"),
                        res.getDouble("ptprice"),
                        res.getInt("categoryID"),
                        res.getString("description"),
                        res.getDate("adddate"),
                        res.getDate("updatedate"),
                        res.getBoolean("available"),
                        res.getInt("stock"),
                        res.getInt("discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static List<Product> searchProducts(String entry){
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from products where reference like ? OR label like ? or maker like ?");
            stmt.setString(1, entry + "%");
            stmt.setString(2, "%" + entry + "%");
            stmt.setString(3, entry + "%");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                Product p = new Product(res.getInt("productID"),
                        res.getInt("supplierID"),
                        res.getString("label"),
                        res.getString("reference"),
                        res.getString("maker"),
                        res.getDouble("ptprice"),
                        res.getInt("categoryID"),
                        res.getString("description"),
                        res.getDate("adddate"),
                        res.getDate("updatedate"),
                        res.getBoolean("available"),
                        res.getInt("stock"),
                        res.getInt("discount"));

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static List<Product> searchProducts(String entry, Category category){
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from products where categoryID = ? AND (reference like ? OR label like ? or maker like ?)");
            stmt.setInt(1, category.getId());
            System.out.println(category.getId());
            stmt.setString(2, entry + "%");
            stmt.setString(3, "%" + entry + "%");
            stmt.setString(4, entry + "%");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                Product p = new Product(res.getInt("productID"),
                        res.getInt("supplierID"),
                        res.getString("label"),
                        res.getString("reference"),
                        res.getString("maker"),
                        res.getDouble("ptprice"),
                        res.getInt("categoryID"),
                        res.getString("description"),
                        res.getDate("adddate"),
                        res.getDate("updatedate"),
                        res.getBoolean("available"),
                        res.getInt("stock"),
                        res.getInt("discount"));

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static List<Product> searchProducts(Category category){
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from products where categoryID = ?");
            stmt.setInt(1, category.getId());
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                Product p = new Product(res.getInt("productID"),
                        res.getInt("supplierID"),
                        res.getString("label"),
                        res.getString("reference"),
                        res.getString("maker"),
                        res.getDouble("ptprice"),
                        res.getInt("categoryID"),
                        res.getString("description"),
                        res.getDate("adddate"),
                        res.getDate("updatedate"),
                        res.getBoolean("available"),
                        res.getInt("stock"),
                        res.getInt("discount"));

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static int getLastId(){
        int id = 0;
        try{
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select MAX(productID) as id from products");
            res.next();
            id = res.getInt("id");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }



}
