package org.filrouge.DAL;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {

    private int id, parentID;
    private String label;
    private StringProperty name;

    public Category(int id, int parentID, String label) {
        this.id = id;
        this.parentID = parentID;
        this.label = label;
        this.name = new SimpleStringProperty(label);
    }

    public Category() {
    }

    public static List<String> getOrderedCategories(){
        List<String> list = new ArrayList<>();
        HashMap<Integer, String> parents = CategoryDAO.getParents();
        for (Map.Entry<Integer, String> parent: parents.entrySet()){
            list.add(parent.getValue());
            List<String> children = CategoryDAO.getChildren(parent.getKey());
            for (String child: children){
                list.add("\t" + child);
            }
        }
        return list;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
