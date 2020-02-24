package org.filrouge.DAL;

import java.text.DecimalFormat;
import java.util.Map;

public class Order {

    private int id;
    private int owner;
    private String ownerLogin;
    private double price;
    private Adress billingAdress;
    private Adress deliveryAdress;
    private int state;
    private String strState;
    private boolean payed;
    private String strPayed;
    private Discount discount;
    private int nbproducts;
    private Map<Integer, Map.Entry<Integer, Boolean>> products;
    private String strPrice;

    public Order(int id, int owner, String ownerLogin, double price, Map<Integer, Map.Entry<Integer, Boolean>> products, Adress billingAdress, Adress deliveryAdress, int state, boolean payed, int discount) {
        this.id = id;
        this.owner = owner;
        this.ownerLogin = ownerLogin;
        this.billingAdress = billingAdress;
        this.deliveryAdress = deliveryAdress;
        this.state = state;
        this.payed = payed;
        this.products = products;
        this.nbproducts = 0;
        products.forEach((product, quantity) -> {
            if(ProductDAO.getProduct(product) != null)
                this.nbproducts = this.nbproducts + quantity.getKey();
        });
        if(OrderDAO.getDiscount(discount) != null){
            this.discount = OrderDAO.getDiscount(discount);
        }
        calculatePrice();

        switch(state){
            case 0: this.strState = "Non validée"; break;
            case 1: this.strState = "En cours"; break;
            case 2: this.strState = "Achevée"; break;
            case 3: this.strState = "Annulée"; break;
        }
        if(payed) this.strPayed = "Effectué";
        else this.strPayed = "En attente";
    }

    public void calculatePrice(){
        price = 0;
        products.forEach((productId, productAttr) -> {
            Product p = ProductDAO.getProduct(productId);
            if(p != null){
                price = price + productAttr.getKey() * p.getPrice() * (1 - p.getDiscount());
            }
        });
        if(discount.getId() != 0) {
            if (price >= discount.getMinAmount()) {
                if (discount.getFixedValue() != 0)
                    price = price - discount.getFixedValue();
                else if (discount.getPercentValue() != 0)
                    price = price * (1 - discount.getPercentValue()/100);

            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        this.strPrice = df.format(this.price) + "€";
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
        switch(state){
            case 0: this.strState = "Non validée"; break;
            case 1: this.strState = "En cours"; break;
            case 2: this.strState = "Achevée"; break;
            case 3: this.strState = "Annulée"; break;
        }
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
        if(payed) this.strPayed = "Effectué";
        else this.strPayed = "En attente";
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
        calculatePrice();
    }

    public int getNbproducts() {
        return nbproducts;
    }

    public void setNbproducts(int nbproducts) {
        this.nbproducts = nbproducts;
    }

    public Map<Integer, Map.Entry<Integer, Boolean>> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Map.Entry<Integer, Boolean>> products) {
        this.products = products;
        calculatePrice();
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
        switch(strState){
            case "Non validée": this.state = 0; break;
            case "En cours": this.state = 1; break;
            case "Achevée": this.state = 2; break;
            case "Annulée": this.state = 3; break;
        }
    }

    public String getStrPayed() {
        return strPayed;
    }

    public void setStrPayed(String strPayed) {
        this.strPayed = strPayed;
        if(strPayed.equals("Effectué")) this.payed = true;
        else if(strPayed.equals("En attente")) this.payed = false;
    }

    public String getStrPrice() {
        return strPrice;
    }

    public void setStrPrice(String strPrice) {
        this.strPrice = strPrice;
    }
}
