package org.filrouge.DAL;

import java.sql.*;

public class AdressDAO {

    static Connection db = DBConnect.connect();

    public static Adress getAdress(int id){
        Adress adress = null;
        try {
            PreparedStatement stmt = db.prepareStatement("select * from adresses, countries where adresses.charcode = countries.charcode && adressID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                adress = new Adress(
                res.getInt("adressID"),
                res.getString("label"),
                res.getInt("zipcode"),
                res.getString("city"),
                res.getString("name"),
                res.getInt("userID"),
                res.getString("charcode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adress;
    }

}
