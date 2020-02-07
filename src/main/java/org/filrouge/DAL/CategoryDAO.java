package org.filrouge.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryDAO {

    static Connection db = DBConnect.connect();

    public static List<String> getCategories(){
        List<String> list = new ArrayList<>();
        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select * from categories");
            while(res.next()){
                list.add(res.getString("label"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HashMap<Integer, String> getParents(){
        HashMap<Integer, String> list = new HashMap<>();
        try {
            Statement stmt = db.createStatement();
            ResultSet res = stmt.executeQuery("select * from categories where parentID = 0");
            while(res.next()){
                list.put(res.getInt("categoryID"),res.getString("label"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getChildren(int parent){
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from categories where parentID = ?");
            stmt.setInt(1, parent);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                list.add(res.getString("label"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getCategoryName(int id){
        String name = "";
        try {
            PreparedStatement stmt = db.prepareStatement("select label from categories where categoryID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                name = res.getString("label");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static Category getCategory(int id){
        Category c = new Category();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from categories where categoryID = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                c.setId(id);
                c.setLabel(res.getString("label"));
                c.setParentID(res.getInt("parentID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static Category searchCategory(String lbl){
        Category c = new Category();
        try {
            PreparedStatement stmt = db.prepareStatement("select * from categories where label = ?");
            stmt.setString(1, lbl);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                c.setId(res.getInt("categoryID"));
                c.setLabel(res.getString("label"));
                c.setParentID(res.getInt("parentID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }


}
