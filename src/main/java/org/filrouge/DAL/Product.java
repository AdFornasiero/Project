package org.filrouge.DAL;

import java.sql.Date;

public class Product {

    private int id, stock;
    private Supplier supplier;
    private Category category;
    private String label, reference, maker, description;
    private double price;
    private Date adddate, updatedate;
    private boolean available;


    public Product(int id, int supplierID, String label, String reference, String maker, double price, int category, String description, Date adddate, Date updatedate, boolean available, int stock) {
        this.id = id;
        this.supplier = SupplierDAO.getSupplier(supplierID);
        this.label = label;
        this.reference = reference;
        this.maker = maker;
        this.price = price;
        this.category = CategoryDAO.getCategory(category);
        this.description = description;
        this.adddate = adddate;
        this.updatedate = updatedate;
        this.available = available;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", stock=" + stock +
                ", supplier=" + supplier +
                ", category=" + category +
                ", label='" + label + '\'' +
                ", reference='" + reference + '\'' +
                ", maker='" + maker + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", adddate=" + adddate +
                ", updatedate=" + updatedate +
                ", available=" + available +
                '}';
    }
}
