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
import javafx.stage.Popup;
import org.filrouge.DAL.*;

import java.sql.Date;
import java.time.LocalDate;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    public Label lbl_update_success;

    String labelEr, referenceEr, makerEr, priceEr, descriptionEr, stockEr;

    String[] labelRules = {"required", "min_length(4)", "max_length(64)", "regex(^[0-9A-Za-z.,-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]+$)"};
    String[] makerRules = {"required", "min_length(2)", "max_length(32)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] priceRules = {"required", "max_length(9)", "regex(^[0-9]{1,6}(.[0-9]{1,2})?$)"};
    String[] descriptionRules = {"max_length(2048)", "regex(^[0-9A-Za-z.,-_?;:\\!%*&\"\''()@=²áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]*$)"};
    String[] stockRules = {"required", "integer", "max_length(8)"};

    String[][] rules = {labelRules, referenceRules, makerRules, priceRules, descriptionRules, stockRules};


    ObservableList<Product> products = FXCollections.observableArrayList();
    ObservableList<String> categories = FXCollections.observableArrayList();

    Product selected;
    int selectedIndex;

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


        FormValidation.setMessage("required", "Champs obligatoire");
        FormValidation.setMessage("min_length", "Ce n'est pas assez long");
        FormValidation.setMessage("max_length", "C'est un peu trop long");
        FormValidation.setMessage("regex", "Saisie incorrecte");
        FormValidation.setMessage("integer", "Entrez un nombre");
    }

    public void product_select(MouseEvent mouseEvent) {
        selected = product_table.getSelectionModel().getSelectedItem();
        selectedIndex = product_table.getSelectionModel().getSelectedIndex();

        inp_label.setText(selected.getLabel());
        inp_reference.setText(selected.getReference());
        inp_maker.setText(selected.getMaker());
        inp_price.setText(String.valueOf(selected.getPrice()));
        inp_description.setText(selected.getDescription());
        //inp_supplier.setText(p.);
        inp_stock.setText(String.valueOf(selected.getStock()));
        chk_available.setSelected(selected.isAvailable());
        inp_stock.setText(String.valueOf(selected.getStock()));
        lbl_id.setText("ID produit: " + String.valueOf(selected.getId()));

        clearErrors();
        lbl_update_success.setVisible(false);

        if(cmb_category.getItems().contains(selected.getCategory().getLabel())){
            cmb_category.setValue(selected.getCategory().getLabel());
        }
        else{
            cmb_category.setValue("\t" + selected.getCategory().getLabel());
        }

        if(selected.getAdddate() != null){
            LocalDate adddate = LocalDate.parse(String.valueOf(selected.getAdddate()));
            lbl_add_date.setText("Date de création: " + adddate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
        else{
            lbl_update_date.setText("Date de création:  -");
        }

        if(selected.getUpdatedate() != null){
            LocalDate updatedate = LocalDate.parse(String.valueOf(selected.getUpdatedate()));
            lbl_update_date.setText("Dernière modification: " + updatedate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
        else{
            lbl_update_date.setText("Dernière modification:  -");
        }
    }

    public void product_remove(ActionEvent actionEvent) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suppression de " + selected.getLabel());
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment supprimer ce produit?\nCette opération est irréversible.");
        Optional<ButtonType> btn_confirm = confirm.showAndWait();

        if(btn_confirm.get() == ButtonType.OK){
            if(ProductDAO.removeProduct(selected.getId())){
                products.remove(selected);
            }
            else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Suppression de " + selected.getLabel());
                error.setContentText("Erreur lors de la suppression du produit. ");
            }
        }

    }

    public void product_update(ActionEvent actionEvent) {

        String[] values = {inp_label.getText(), inp_reference.getText(), inp_maker.getText(), inp_price.getText(), inp_description.getText(), inp_stock.getText()};

        int id, stock;
        int supplier;
        int category;
        String label, reference, maker, description;
        double price;
        Date adddate, updatedate;
        boolean available;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);

        if(FormValidation.validFields(values, rules)){
            id = selected.getId();
            supplier = s.getId();
            label = inp_label.getText();
            reference = inp_reference.getText().toUpperCase();
            maker = inp_maker.getText();
            description = inp_description.getText();
            category = CategoryDAO.searchCategory(String.valueOf(cmb_category.getValue()).trim()).getId();
            adddate = selected.getAdddate();
            updatedate = new Date(System.currentTimeMillis());
            available = chk_available.isSelected();
            stock = Integer.valueOf(inp_stock.getText());
            if(inp_price.getText().indexOf(',') == -1)
                price = Double.valueOf(inp_price.getText());
            else
                price = Double.valueOf(inp_price.getText().replace(',', '.'));

            Product p = new Product(id, supplier, label, reference, maker, price, category, description, adddate, updatedate, available, stock);
            ProductDAO.updateProduct(p);

            products.set(selectedIndex, p);
            lbl_update_success.setVisible(true);
        }
        else{
            lbl_update_success.setVisible(false);
        }

    }

    public void product_search(ActionEvent actionEvent) {

    }

    public void text_changed(InputMethodEvent inputMethodEvent) {
    }


    public void label_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_label.getText(), labelRules);
        labelEr = FormValidation.getFirstMessage();
        label_error.setText(labelEr);
    }

    public void reference_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_reference.getText(), referenceRules);
        referenceEr = FormValidation.getFirstMessage();
        reference_error.setText(referenceEr);
    }
    
    public void maker_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_maker.getText(), makerRules);
        makerEr = FormValidation.getFirstMessage();
        maker_error.setText(makerEr);
    }

    public void price_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_price.getText(), priceRules);
        priceEr = FormValidation.getFirstMessage();
        price_error.setText(priceEr);
    }

    public void description_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_description.getText(), descriptionRules);
        descriptionEr = FormValidation.getFirstMessage();
        description_error.setText(descriptionEr);
    }

    public void stock_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_stock.getText(), stockRules);
        stockEr = FormValidation.getFirstMessage();
        stock_error.setText(stockEr);
    }

    public void clearErrors(){
        label_error.setText("");
        reference_error.setText("");
        maker_error.setText("");
        price_error.setText("");
        description_error.setText("");
        stock_error.setText("");
    }
}
