package org.filrouge.DAL;

public class Discount {

    int id;
    String code;
    Integer percentValue;
    Integer fixedValue;
    int minAmount;
    boolean active;

    public Discount(int id, String code, int percentValue, int fixedValue, int minAmount, boolean active) {
        this.id = id;
        this.code = code;
        this.percentValue = percentValue;
        this.fixedValue = fixedValue;
        this.minAmount = minAmount;
        this.active = active;
    }

    public Discount() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(int percentValue) {
        this.percentValue = percentValue;
    }

    public int getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(int fixedValue) {
        this.fixedValue = fixedValue;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        String str = "";
        if(this.fixedValue != null && this.percentValue != null) {
            if (this.percentValue != 0)
                str = this.code + " (-" + this.percentValue + "%)";
            else if (this.fixedValue != 0)
                str = this.code + " (-" + this.fixedValue + "€)";
            else
                str = this.code;
        }
        return str;
    }

    public String getDiscountStrType(){
        String str = "";
        if(this.id != 0){
            if(this.fixedValue != null && this.percentValue != null) {
                if (this.percentValue != 0)
                    str = " (-" + this.percentValue + "%)";
                else if (this.fixedValue != 0)
                    str = " (-" + this.fixedValue + "€)";
            }
        }
        return str;
    }
}
