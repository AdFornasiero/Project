package org.filrouge.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.filrouge.DAL.*;
import java.sql.Date;
import java.time.LocalDate;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class Panel implements Initializable {


    public TextField inp_product_search;
    public ComboBox cmb_category_search;
    public TableView<Product> product_table;
    public TableColumn<Product, String> col_product_reference;
    public TableColumn<Product, String> col_product_category;
    public TableColumn<Product, String> col_product_maker;
    public TableColumn<Product, String> col_product_label;
    public TableColumn<Product, Double> col_product_price;
    public TextField inp_label;
    public TextField inp_reference;
    public ComboBox cmb_category;
    public TextField inp_maker;
    public TextField inp_stock;
    public TextField inp_price;
    public TextArea inp_description;
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
    public Label lbl_nb_products;
    public Button btn_product_add;
    public Label category_error;
    public Label form_title;
    public Label lbl_add_success;
    public ComboBox cmb_supplier;

        // ORDER PANE
    public ComboBox cmb_order_state_search;
    public ComboBox cmb_order_payment_search;
    public TextField inp_order_id;
    public TextField inp_order_owner;
    public Label lbl_nb_orders;
    public TableColumn<Order, Integer> col_order_id;
    public TableColumn<Order, String> col_order_ownerLogin;
    public TableColumn<Order, Adress> col_order_billing;
    public TableColumn<Order, Adress> col_order_delivery;
    public TableColumn<Order, Integer> col_order_nbproducts;
    public TableColumn<Order, Double> col_order_price;
    public TableColumn<Order, Discount> col_order_discount;
    public TableColumn<Order, Integer> col_order_state;
    public TableView<Order> orders_table;
    public Label orderid_error;

    String[] labelRules = {"required", "min_length(4)", "max_length(64)", "regex(^[0-9A-Za-z.,-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]+$)"};
    String[] makerRules = {"required", "min_length(2)", "max_length(32)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] priceRules = {"required", "max_length(9)", "regex(^[0-9]{1,6}(.[0-9]{1,2})?$)"};
    String[] descriptionRules = {"max_length(2048)", "regex(^[0-9A-Za-z.,-_?;:\\!%*&\"\''()@=²áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]*$)"};
    String[] stockRules = {"required", "integer", "max_length(8)"};
    String[] categoryRules = {"exists(categories.label)"};
    String labelEr, referenceEr, makerEr, priceEr, descriptionEr, stockEr, categoryEr;

    ObservableList<Product> products = FXCollections.observableArrayList();
    ObservableList<String> categories = FXCollections.observableArrayList();
    ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    ObservableList<Order> orders = FXCollections.observableArrayList();

    Product selected;
    int selectedIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FormValidation.setDatabase(DBConnect.connect());
        products.addAll(ProductDAO.getProducts());
        categories.addAll(Category.getOrderedCategories());
        orders.addAll(OrderDAO.getOrders());

        categories.add(0,"Sélectionnez une catégorie");
        cmb_category_search.setItems(categories);
        cmb_category.setItems(categories);
        cmb_category_search.setValue("Sélectionnez une catégorie");

        suppliers.addAll(SupplierDAO.getSuppliers());
        cmb_supplier.setItems(suppliers);
        cmb_supplier.setCellFactory(new Callback<ListView<Supplier>, ListCell<Supplier>>(){
            public ListCell<Supplier> call(ListView<Supplier> listView){
                return new ListCell<Supplier>(){
                    protected void updateItem(Supplier s, boolean empty){
                        super.updateItem(s, empty);
                        if(s != null)
                            setText(s.getName());
                    }
                };
            }
        });

        col_product_reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        col_product_maker.setCellValueFactory(new PropertyValueFactory<>("maker"));
        col_product_label.setCellValueFactory(new PropertyValueFactory<>("label"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_product_category.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getLabel()));
        product_table.setItems(products);

        col_order_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_order_ownerLogin.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));
        col_order_delivery.setCellValueFactory(new PropertyValueFactory<>("deliveryAdress"));
        col_order_billing.setCellValueFactory(new PropertyValueFactory<>("billingAdress"));
        col_order_nbproducts.setCellValueFactory(new PropertyValueFactory<>("nbproducts"));
        col_order_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_order_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        col_order_state.setCellValueFactory(new PropertyValueFactory<>("state"));
        orders_table.setItems(orders);

        cmb_order_payment_search.setItems(FXCollections.observableArrayList("Toutes", "Effectué", "En attente"));
        cmb_order_state_search.setItems(FXCollections.observableArrayList("Toutes", "Non validée", "En cours", "Achevée", "Annulée"));

        FormValidation.setMessage("required", "Champs obligatoire");
        FormValidation.setMessage("min_length", "Ce n'est pas assez long");
        FormValidation.setMessage("max_length", "C'est un peu trop long");
        FormValidation.setMessage("regex", "Saisie incorrecte");
        FormValidation.setMessage("integer", "Entrez un nombre");
        FormValidation.setMessage("exists", "Inexistant");
        FormValidation.setMessage("unique", "Déja utilisée");
    }

    public void product_select(MouseEvent mouseEvent) {
        btn_product_save.setText("Enregistrer");
        if(product_table.getSelectionModel() != null && product_table.getSelectionModel().getSelectedItem() != null) {
            selected = product_table.getSelectionModel().getSelectedItem();
            selectedIndex = product_table.getSelectionModel().getSelectedIndex();

            inp_label.setText(selected.getLabel());
            inp_reference.setText(selected.getReference());
            inp_maker.setText(selected.getMaker());
            inp_price.setText(String.valueOf(selected.getPrice()));
            inp_description.setText(selected.getDescription());
            inp_stock.setText(String.valueOf(selected.getStock()));
            chk_available.setSelected(selected.isAvailable());
            inp_stock.setText(String.valueOf(selected.getStock()));
            lbl_id.setText("ID produit: " + selected.getId());
            cmb_supplier.setValue(SupplierDAO.getSupplier(selected.getSupplier().getId()).getName());

            if (cmb_category.getItems().contains(selected.getCategory().getLabel())) {
                cmb_category.setValue(selected.getCategory().getLabel());
            } else {
                cmb_category.setValue("\t" + selected.getCategory().getLabel());
            }

            if (selected.getAdddate() != null) {
                LocalDate adddate = LocalDate.parse(String.valueOf(selected.getAdddate()));
                lbl_add_date.setText("Date de création: " + adddate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            } else {
                lbl_update_date.setText("Date de création:  -");
            }

            if (selected.getUpdatedate() != null) {
                LocalDate updatedate = LocalDate.parse(String.valueOf(selected.getUpdatedate()));
                lbl_update_date.setText("Dernière modification: " + updatedate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            } else {
                lbl_update_date.setText("Dernière modification:  -");
            }

            clearErrors();
            lbl_update_success.setVisible(false);
        }
    }

    public void product_save(ActionEvent actionEvent) {
        if(btn_product_save.getText().equals("Ajouter")){
            product_add();
        }
        else if(btn_product_save.getText().equals("Enregistrer")){
            product_update();
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

    public void product_update() {
        String[][] rules = {labelRules, referenceRules, makerRules, categoryRules, priceRules, descriptionRules, stockRules};
        Label[] errorLabels = {label_error, reference_error, maker_error, category_error, price_error, description_error, stock_error};
        String[] values = {inp_label.getText(), inp_reference.getText(), inp_maker.getText(), String.valueOf(cmb_category.getValue()).trim() ,inp_price.getText(), inp_description.getText(), inp_stock.getText()};
        int id, stock;
        int supplier;
        int category;
        String label, reference, maker, description;
        double price;
        Date adddate, updatedate;
        boolean available;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);
        if(FormValidation.validFields(values, rules, errorLabels)){
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
            if(ProductDAO.updateProduct(p)){
                products.set(selectedIndex, p);
                lbl_update_success.setVisible(true);
            }
        }
        else{
            lbl_update_success.setVisible(false);
        }
    }

    public void product_search(ActionEvent actionEvent) {
        List<Product> productsList = new ArrayList<>();
        String entry = inp_product_search.getText();
        String strCategory = String.valueOf(cmb_category_search.getValue()).trim();
        Category category = CategoryDAO.searchCategory(strCategory);

        if(!entry.isBlank()){
            if(category.getId() == 0){
                productsList = ProductDAO.searchProducts(entry);
            }
            else{
                productsList = ProductDAO.searchProducts(entry, category);
            }
        }
        else{
            if(category.getId() == 0){
                productsList = ProductDAO.getProducts();
            }
            else{
                productsList = ProductDAO.searchProducts(category);
            }
        }
        products.clear();
        products.addAll(productsList);
        if(products.size() != 0)
            lbl_nb_products.setText(products.size() + " produits");
        else{
            lbl_nb_products.setText("Aucun produit");
        }
        form_title.setText("Informations détaillées");
        lbl_update_success.setText("");
    }

    public void product_add(){
        String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]+$)", "unique(products.reference)"};
        String[][] rules = {labelRules, referenceRules, makerRules, categoryRules, priceRules, descriptionRules, stockRules};
        Label[] errorLabels = {label_error, reference_error, maker_error, category_error, price_error, description_error, stock_error};
        String[] values = {inp_label.getText(), inp_reference.getText().toUpperCase(), inp_maker.getText(), String.valueOf(cmb_category.getValue()).trim() ,inp_price.getText(), inp_description.getText(), inp_stock.getText()};
        int id, stock;
        int supplier;
        int category;
        String label, reference, maker, description;
        double price;
        Date adddate;
        boolean available;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);
        if(FormValidation.validFields(values, rules, errorLabels)){
            id = ProductDAO.getLastId() + 1;
            supplier = s.getId();
            label = inp_label.getText();
            reference = inp_reference.getText().toUpperCase();
            maker = inp_maker.getText();
            description = inp_description.getText();
            category = CategoryDAO.searchCategory(String.valueOf(cmb_category.getValue()).trim()).getId();
            adddate = new Date(System.currentTimeMillis());
            available = chk_available.isSelected();
            stock = Integer.valueOf(inp_stock.getText());
            if(inp_price.getText().indexOf(',') == -1)
                price = Double.valueOf(inp_price.getText());
            else
                price = Double.valueOf(inp_price.getText().replace(',', '.'));

            Product p = new Product(id, supplier, label, reference, maker, price, category, description, adddate, available, stock);

            if(ProductDAO.addProduct(p)){
                products.add(p);
                lbl_add_success.setVisible(true);
                product_table.setDisable(false);
            }
        }
        else{
            lbl_add_success.setVisible(false);
        }
    }

    public void btn_add(ActionEvent actionEvent) {
        product_table.setDisable(true);
        form_title.setText("Ajouter un article");
        btn_product_save.setText("Ajouter");
        btn_product_cancel.setVisible(true);
        clearFields();
        clearErrors();
    }

    public void cancel(ActionEvent actionEvent) {
        product_table.setDisable(false);
        form_title.setText("Informations détaillées");
        btn_product_save.setText("Enregistrer");
        btn_product_cancel.setVisible(false);
        clearErrors();
        clearFields();
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

    public void category_changed(ActionEvent actionEvent) {
        FormValidation.validField(String.valueOf(cmb_category.getValue()).trim(), categoryRules);
        categoryEr = FormValidation.getFirstMessage();
        category_error.setText(categoryEr);
    }

    public void supplier_changed(ActionEvent actionEvent) {

    }

    public void clearErrors(){
        label_error.setText("");
        reference_error.setText("");
        maker_error.setText("");
        price_error.setText("");
        description_error.setText("");
        stock_error.setText("");
    }

    public void clearFields(){
        inp_label.setText("");
        inp_reference.setText("");
        inp_maker.setText("");
        inp_price.setText("");
        inp_description.setText("");
        inp_stock.setText("");
        chk_available.setSelected(true);
        lbl_add_date.setText("");
        lbl_update_date.setText("");
        lbl_id.setText("");
        lbl_update_success.setVisible(false);
        lbl_add_success.setVisible(false);
    }


    public void order_search_id(ActionEvent actionEvent) {
        search_order();
    }

    public void order_search_state(ActionEvent actionEvent) {
        search_orders();
    }

    public void order_search_payment(ActionEvent actionEvent) {
        search_orders();
    }



    public void order_search_owner(ActionEvent actionEvent) {
        //search_orders();
    }

    public void search_order(){
        String[] idRules = {"integer", "exists(orders.orderID)"};
        if(inp_order_id.getText().isBlank()){
            orderid_error.setText("");
            orders.clear();
            orders.addAll(OrderDAO.getOrders());
        }
        else{
            if(FormValidation.validField(inp_order_id.getText(), idRules).size() > 0){
                orderid_error.setText(FormValidation.getFirstMessage());
            }
            else{
                orderid_error.setText("");
                orders.clear();
                orders.addAll(OrderDAO.getOrder(Integer.parseInt(inp_order_id.getText())));
            }
        }
    }

    public void search_orders(){

    }
}
