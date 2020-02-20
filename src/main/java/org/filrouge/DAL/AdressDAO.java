package org.filrouge.DAL;

import java.sql.*;

public class AdressDAO {

    static Connection db = DBConnect.connect();

    public static Adress getAdress(int id){
        Adress adress = new Adress();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from adresses, countries where adressID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                adress.setId(res.getInt("adressID"));
                adress.setLabel(res.getString("label"));
                adress.setZipcode(res.getInt("zipcode"));
                adress.setCity(res.getString("city"));
                adress.setCountry(res.getString("countries.name"));
                adress.setOwner(res.getInt("userID"));
                adress.setCharcode(res.getString("charcode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adress;
    }

}
