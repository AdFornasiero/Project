package org.filrouge.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.filrouge.DAL.Category;
import org.filrouge.DAL.DBConnect;
import org.filrouge.DAL.Product;
import org.filrouge.DAL.ProductDAO;

import java.time.LocalDate;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class Panel implements Initializable {


    public TextField inp_product_search;
    public ComboBox cmb_category_search;
    public TableView<Product> product_table;
    public TableColumn<Product, String> reference;
    public TableColumn<Product, String> category;
    public TableColumn<Product, String> maker;
    public TableColumn<Product, String> label;
    public TableColumn<Product, Double> price;
    public TextField inp_label;
    public TextField inp_reference;
    public ComboBox cmb_category;
    public TextField inp_maker;
    public TextField inp_stock;
    public TextField inp_price;
    public TextArea inp_description;
    public TextField inp_supplier;
    public Label lbl_add_date;
    public Label lbl_update_date;
    public Label lbl_id;
    public Button btn_product_remove;
    public Button btn_product_save;
    public Button btn_product_cancel;
    public CheckBox chk_available;
    public Label stock_error;
    public Label description_error;
    public Label price_error;
    public Label maker_error;
    public Label reference_error;
    public Label label_error;

    HashMap<String, String> labelErr, referenceErr, makerErr, priceErr, descriptionErr, stockErr;

    String[] labelRules = {"required", "min_length(4)", "max_length(64)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]?$)"};
    String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]?$)"};
    String[] makerRules = {"required", "min_length(2)", "max_length(32)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]?$)"};
    String[] priceRules = {"required", "max_length(9)", "regex(^[0-9]{1,6}(.[0-9]{1,2})?$)"};
    String[] descriptionRules = {"max_length(1024)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]?$)"};
    String[] stockRules = {"required", "integer", "max_length(8)"};


    ObservableList<Product> products = FXCollections.observableArrayList();
    ObservableList<String> categories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        products.addAll(ProductDAO.getProducts());
        categories.addAll(Category.getOrderedCategories());

        cmb_category_search.setItems(categories);
        cmb_category.setItems(categories);

        reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        maker.setCellValueFactory(new PropertyValueFactory<>("maker"));
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        category.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getLabel()));

        product_table.setItems(products);

        FormValidation.setDatabase(DBConnect.connect());
    }

    public void product_select(MouseEvent mouseEvent) {
        Product p = product_table.getSelectionModel().getSelectedItem();
        inp_label.setText(p.getLabel());
        inp_reference.setText(p.getReference());
        inp_maker.setText(p.getMaker());
        inp_price.setText(String.valueOf(p.getPrice()));
        inp_description.setText(p.getDescription());
        //inp_supplier.setText(p.);
        inp_stock.setText(String.valueOf(p.getStock()));
        chk_available.setSelected(p.isAvailable());
        inp_stock.setText(String.valueOf(p.getStock()));
        lbl_id.setText("ID produit: " + String.valueOf(p.getId()));

        if(cmb_category.getItems().contains(p.getCategory().getLabel())){
            cmb_category.setValue(p.getCategory().getLabel());
        }
        else{
            cmb_category.setValue("\t" + p.getCategory().getLabel());
        }

        if(p.getAdddate() != null){
            LocalDate adddate = LocalDate.parse(String.valueOf(p.getAdddate()));
            lbl_add_date.setText("Date de création: " + adddate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
        else{
            lbl_update_date.setText("Date de création:  -");
        }

        if(p.getUpdatedate() != null){
            LocalDate updatedate = LocalDate.parse(String.valueOf(p.getUpdatedate()));
            lbl_update_date.setText("Dernière modification: " + updatedate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
        else{
            lbl_update_date.setText("Dernière modification:  -");
        }
    }

    public void product_remove(ActionEvent actionEvent) {

        Product p = product_table.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //alert.initStyle(StageStyle.);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression de " + p.getLabel());
        alert.setContentText("Voulez-vous vraiment supprimer ce produit?\n\nCette opération est irréversible.");
        Optional<ButtonType> confirm = alert.showAndWait();

        if(confirm.get() == ButtonType.OK){
            if(ProductDAO.removeProduct(p.getId())){
                products.remove(p);
            }
            else{
                System.out.println("ERREUR");
            }
        }

    }

    public void product_search(ActionEvent actionEvent) {
        System.out.println("on action");
    }

    public void text_changed(InputMethodEvent inputMethodEvent) {
        System.out.println("text changed");
    }

    public void product_update(ActionEvent actionEvent) {

        FormValidation.setMessage("required", "Champs obligatoire");
        FormValidation.setMessage("min_length", "Champs incorrect");
        FormValidation.setMessage("max_length", "Champs incorrect");
        FormValidation.setMessage("regex", "Champs incorrect");
        FormValidation.setMessage("integer", "Champs incorrect");

        FormValidation.validField(inp_maker.getText(), makerRules);
        makerErr = FormValidation.getMessages();
        maker_error.setText(makerErr.values().stream().findFirst().get());

        FormValidation.validField(inp_price.getText(), priceRules);
        priceErr = FormValidation.getMessages();
        price_error.setText(priceErr.values().stream().findFirst().get());

        FormValidation.validField(inp_description.getText(), descriptionRules);
        descriptionErr = FormValidation.getMessages();
        description_error.setText(descriptionErr.values().stream().findFirst().get());

        FormValidation.validField(inp_stock.getText(), stockRules);
        stockErr = FormValidation.getMessages();
        stock_error.setText(stockErr.values().stream().findFirst().get());

        //Product p = new Product();

    }

    public void label_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_label.getText(), labelRules);
        labelErr = FormValidation.getMessages();
        System.out.println(labelErr);
        if(!labelErr.isEmpty()){
            label_error.setText(labelErr.values().stream().findFirst().get());
        }
    }

    public void reference_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_reference.getText(), referenceRules);
        referenceErr = FormValidation.getMessages();
        if(!labelErr.isEmpty()){
            reference_error.setText(referenceErr.values().stream().findFirst().get());
        }
    }


}
