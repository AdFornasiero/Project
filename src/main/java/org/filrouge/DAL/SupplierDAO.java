package org.filrouge.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    static Connection db = DBConnect.connect();

    public static Supplier getSupplier(int id){
        Supplier s = new Supplier();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from suppliers where supplierID = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                s.setId(rs.getInt("supplierID"));
                s.setName(rs.getString("name"));
                s.setContact(rs.getString("contact"));
                s.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static List<Supplier> getSuppliers(){
        List<Supplier> suppliers = new ArrayList<>();
        try{
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select * from suppliers order by name");
            while(res.next()){
                suppliers.add(new Supplier(res.getInt("supplierID"), res.getString("name"), res.getString("contact"), res.getString("phone")));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return suppliers;
    }

}
