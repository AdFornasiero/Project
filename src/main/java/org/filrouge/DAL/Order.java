package org.filrouge.DAL;

import java.util.HashMap;

public class Order {

    private int id;
    private int owner;
    private String ownerLogin;
    private double price;
    private Adress billingAdress;
    private Adress deliveryAdress;
    private int state;
    private boolean payed;
    private Discount discount;
    private int nbproducts;
    private HashMap<Integer, Integer> products;

    public Order(int id, int owner, String ownerLogin, double price, HashMap<Integer, Integer> products, Adress billingAdress, Adress deliveryAdress, int state, boolean payed, int discount) {
        this.id = id;
        this.owner = owner;
        this.ownerLogin = ownerLogin;
        this.price = price;
        this.billingAdress = billingAdress;
        this.deliveryAdress = deliveryAdress;
        this.state = state;
        this.payed = payed;
        this.products = products;
        this.nbproducts = 0;
        products.forEach((product, quantity) -> this.nbproducts = this.nbproducts + quantity);
        if(OrderDAO.getDiscount(discount) != null){
            this.setDiscount(OrderDAO.getDiscount(discount));
        }
        else{
            this.setDiscount(null);
        }
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Adress getBillingAdress() {
        return billingAdress;
    }

    public void setBillingAdress(Adress billingAdress) {
        this.billingAdress = billingAdress;
    }

    public Adress getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(Adress deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int getNbproducts() {
        return nbproducts;
    }

    public void setNbproducts(int nbproducts) {
        this.nbproducts = nbproducts;
    }

    public HashMap<Integer, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Integer, Integer> products) {
        this.products = products;
    }
}
