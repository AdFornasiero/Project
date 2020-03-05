package org.filrouge.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> searchAdresses(String entry){
        List<Adress> adresses = new ArrayList<>();
        List<String> strAdresses = new ArrayList<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from adresses, countries where adresses.charcode = countries.charcode && label like ? or zipcode = ? or city like ? limit 20");
            stmt.setString(1, "%" + entry + "%");
            stmt.setString(2, entry);
            stmt.setString(3, entry + "%");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                adresses.add(new Adress(res.getInt("adressID"), res.getString("label"), res.getInt("zipcode"), res.getString("city"), res.getString("name"), res.getInt("userID"), res.getString("adresses.charcode")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adresses.forEach(adress -> {
            strAdresses.add(adress.toString());
        });
        System.out.println(strAdresses);
        return strAdresses;
    }

}
