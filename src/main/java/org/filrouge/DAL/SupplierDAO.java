package org.filrouge.DAL;

import java.sql.*;

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

}
