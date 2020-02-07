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
                        res.getInt("stock"));

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
            PreparedStatement stmt = db.prepareStatement("update products set label = ?, reference = ?, maker = ?, ptprice = ?, description = ?, stock = ?, updatedate = ?, available = ?, supplierID = ?, categoryID = ? where productID = ?");
            stmt.setString(1, p.getLabel());
            stmt.setString(2, p.getReference());
            stmt.setString(3, p.getMaker());
            stmt.setDouble(4, p.getPrice());
            stmt.setString(5, p.getDescription());
            stmt.setInt(6, p.getStock());
            //stmt.setDate(7, );
            stmt.setBoolean(8, p.isAvailable());
            stmt.setInt(9, p.getSupplier().getId());
            stmt.setInt(10, p.getCategory().getId());
            stmt.setInt(11, p.getId());

            if(stmt.executeUpdate() == 1){
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }


}
