package org.filrouge.DAL;

public class Adress {

    int id;
    String label;
    int zipcode;
    String city;
    int owner;
    String country;
    String charcode;

    public Adress(int id, String label, int zipcode, String city, String country, int owner, String charcode) {
        this.id = id;
        this.label = label;
        this.zipcode = zipcode;
        this.city = city;
        this.owner = owner;
        this.country = country;
        this.charcode = charcode;
    }

    public Adress() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCharcode() {
        return charcode;
    }

    public void setCharcode(String charcode) {
        this.charcode = charcode;
    }


    @Override
    public String toString() {
        return label + ", " + city + " (" + zipcode + "), " + country;
    }

    public String detailedStringAdress(){
        String str = label + "\n"
                + zipcode + ",  " + city + "\n" +
                country + " (" + charcode + ")";
        return str;
    }
}
