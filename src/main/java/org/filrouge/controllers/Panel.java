package org.filrouge.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.filrouge.DAL.*;

import java.io.IOException;
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

        //
        // PRODUCT PANE
        //
    // TableView
    public TableView<Product> product_table;
    public TableColumn<Product, String> col_product_reference;
    public TableColumn<Product, String> col_product_category;
    public TableColumn<Product, String> col_product_maker;
    public TableColumn<Product, String> col_product_label;
    public TableColumn<Product, String> col_product_price;

    // Search fields
    public TextField inp_product_search;
    public ComboBox cmb_category_search;
    public Label lbl_nb_products;

    // Product details
    public TextField inp_product_label;
    public TextField inp_product_reference;
    public ComboBox cmb_product_category;
    public ComboBox cmb_supplier;
    public TextField inp_product_maker;
    public TextField inp_product_stock;
    public TextField inp_product_price;
    public TextField inp_product_discount;
    public TextArea inp_product_description;
    public CheckBox chk_available;
    public Label lbl_add_date;
    public Label lbl_update_date;
    public Label lbl_id;

    // Form controls
    public Button btn_product_remove;
    public Button btn_product_save;
    public Button btn_product_cancel;
    public Button btn_product_add;
    public Label form_title;

    // Errors Labels
    public Label product_stock_error;
    public Label product_description_error;
    public Label product_price_error;
    public Label product_maker_error;
    public Label product_reference_error;
    public Label product_label_error;
    public Label product_discount_error;
    public Label product_category_error;

    public Label lbl_update_success;
    public Label lbl_add_success;


        //
        // ORDER PANE
        //
    // Search fields
    public ComboBox cmb_order_state_search;
    public ComboBox cmb_order_payment_search;
    public TextField inp_order_search_id;
    public TextField inp_order_search_owner;
    public Label lbl_nb_orders;

    // TableView
    public TableColumn<Order, Integer> col_order_id;
    public TableColumn<Order, String> col_order_ownerLogin;
    public TableColumn<Order, Adress> col_order_billing;
    public TableColumn<Order, Adress> col_order_delivery;
    public TableColumn<Order, Integer> col_order_nbproducts;
    public TableColumn<Order, String> col_order_price;
    public TableColumn<Order, Discount> col_order_discount;
    public TableColumn<Order, String> col_order_state;
    public TableView<Order> orders_table;

    // Error labels
    public Label lbl_order_billingadress_error;
    public Label lbl_order_deliveryadress_error;
    public Label lbl_order_discount_error;
    public Label lbl_order_owner_error;
    public Label lbl_order_search_id_error;

    // Order details
    public TextField inp_order_owner;
    public TextField inp_order_discount;
    public TextField inp_order_billingadress;
    public TextField inp_order_deliveryadress;
    public ComboBox cmb_order_state;
    public ComboBox cmb_order_payed;
    public Label lbl_order_id;
    public Label lbl_order_price;
    public Label lbl_order_discount;
    public Label lbl_order_date;
    public Label lbl_order_nbproducts;
    public VBox order_products_pane;


    String[] labelRules = {"required", "min_length(4)", "max_length(64)", "regex(^[0-9A-Za-z.,-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]+$)"};
    String[] makerRules = {"required", "min_length(2)", "max_length(32)", "regex(^[0-9A-Za-z.-_áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]+$)"};
    String[] priceRules = {"required", "max_length(9)", "regex(^[0-9]{1,6}(.[0-9]{1,2})?$)"};
    String[] discountRules = {"integer", "min(0)", "max(100)"};
    String[] descriptionRules = {"max_length(2048)", "regex(^[0-9A-Za-z.,-_?;:\\!%*&\"\''()@=²áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ ]*$)"};
    String[] stockRules = {"required", "integer", "max_length(8)"};
    String[] categoryRules = {"exists(categories.label)"};

    ObservableList<Product> products = FXCollections.observableArrayList();
    ObservableList<String> categories = FXCollections.observableArrayList();
    ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    ObservableList<Order> orders = FXCollections.observableArrayList();

    Product selectedProduct;
    Order selectedOrder;
    int selectedIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FormValidation.setDatabase(DBConnect.connect());
        products.addAll(ProductDAO.getProducts());
        categories.addAll(Category.getOrderedCategories());
        categories.add(0,"Sélectionnez une catégorie");
        orders.addAll(OrderDAO.getOrders());
        suppliers.addAll(SupplierDAO.getSuppliers());

        cmb_category_search.setItems(categories);
        cmb_product_category.setItems(categories);
        cmb_category_search.setValue("Sélectionnez une catégorie");

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
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("strPrice"));
        col_product_category.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getLabel()));
        product_table.setItems(products);

        col_order_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_order_ownerLogin.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));
        col_order_delivery.setCellValueFactory(new PropertyValueFactory<>("deliveryAdress"));
        col_order_billing.setCellValueFactory(new PropertyValueFactory<>("billingAdress"));
        col_order_nbproducts.setCellValueFactory(new PropertyValueFactory<>("nbproducts"));
        col_order_price.setCellValueFactory(new PropertyValueFactory<>("strPrice"));
        col_order_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        col_order_state.setCellValueFactory(new PropertyValueFactory<>("strState"));
        orders_table.setItems(orders);

        cmb_order_payment_search.setItems(FXCollections.observableArrayList("Peu importe", "Effectué", "En attente"));
        cmb_order_state_search.setItems(FXCollections.observableArrayList("Toutes", "Non validées", "En cours", "Achevées", "Annulées"));
        cmb_order_payment_search.setValue("Peu importe");
        cmb_order_state_search.setValue("Toutes");

        cmb_order_payed.setItems(FXCollections.observableArrayList("Effectué", "En attente"));
        cmb_order_state.setItems(FXCollections.observableArrayList("Non validée", "En cours", "Achevée", "Annulée"));

        if(orders.size() == 0) lbl_nb_orders.setText("Aucune commande");
        else lbl_nb_orders.setText(orders.size() + " commandes");

        order_products_pane.setFillWidth(true);

        FormValidation.setMessage("required", "Champs obligatoire");
        FormValidation.setMessage("min_length", "Ce n'est pas assez long");
        FormValidation.setMessage("max_length", "C'est un peu trop long");
        FormValidation.setMessage("regex", "Saisie incorrecte");
        FormValidation.setMessage("integer", "Entrez un nombre");
        FormValidation.setMessage("exists", "Inexistant");
        FormValidation.setMessage("unique", "Déja utilisée");
        FormValidation.setMessage("min", "Entrez un plus grand nombre");
        FormValidation.setMessage("max", "C'est un peu trop grand");

        List<String> discountList = new ArrayList<>();
        OrderDAO.getDiscounts().forEach(discount -> discountList.add(discount.getCode()));
        TextFields.bindAutoCompletion(inp_order_discount, discountList);

        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        btn_product_save.setGraphic(fontAwesome.create(FontAwesome.Glyph.GAMEPAD));

        inp_order_billingadress.textProperty().addListener((observable, oldValue, value) ->{
            billingAdressCompletion.dispose();
            billingAdressCompletion = TextFields.bindAutoCompletion(inp_order_billingadress, AdressDAO.searchAdresses(value));
        });
    }
    private AutoCompletionBinding<String> billingAdressCompletion, deliveryAdressCompletion;

    // On click on products table
    // Selects a single product and set it to fields
    public void product_select(MouseEvent mouseEvent) {
        btn_product_save.setText("Enregistrer");
        if(product_table.getSelectionModel() != null && product_table.getSelectionModel().getSelectedItem() != null) {
            selectedProduct = product_table.getSelectionModel().getSelectedItem();
            selectedIndex = product_table.getSelectionModel().getSelectedIndex();

            inp_product_label.setText(selectedProduct.getLabel());
            inp_product_reference.setText(selectedProduct.getReference());
            inp_product_maker.setText(selectedProduct.getMaker());
            inp_product_price.setText(String.valueOf(selectedProduct.getPrice()));
            inp_product_discount.setText(String.valueOf(selectedProduct.getDiscount()));
            inp_product_description.setText(selectedProduct.getDescription());
            inp_product_stock.setText(String.valueOf(selectedProduct.getStock()));
            chk_available.setSelected(selectedProduct.isAvailable());
            inp_product_stock.setText(String.valueOf(selectedProduct.getStock()));
            lbl_id.setText("ID produit: " + selectedProduct.getId());
            cmb_supplier.setValue(SupplierDAO.getSupplier(selectedProduct.getSupplier().getId()).getName());

            if (cmb_product_category.getItems().contains(selectedProduct.getCategory().getLabel())) {
                cmb_product_category.setValue(selectedProduct.getCategory().getLabel());
            } else {
                cmb_product_category.setValue("\t" + selectedProduct.getCategory().getLabel());
            }

            if (selectedProduct.getAdddate() != null) {
                LocalDate adddate = LocalDate.parse(String.valueOf(selectedProduct.getAdddate()));
                lbl_add_date.setText("Date de création: " + adddate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            } else {
                lbl_update_date.setText("Date de création:  -");
            }

            if (selectedProduct.getUpdatedate() != null) {
                LocalDate updatedate = LocalDate.parse(String.valueOf(selectedProduct.getUpdatedate()));
                lbl_update_date.setText("Dernière modification: " + updatedate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            } else {
                lbl_update_date.setText("Dernière modification:  -");
            }

            clearProductErrors();
            lbl_update_success.setVisible(false);
        }
    }

    // On click on 'save' button
    // Call 'update' or 'add' function
    public void product_save(ActionEvent actionEvent) {
        if(btn_product_save.getText().equals("Ajouter")){
            product_add();
        }
        else if(btn_product_save.getText().equals("Enregistrer")){
            product_update();
        }
    }

    // Check fields values and updates in DB the selected product
    public void product_update() {
        String[][] rules = {labelRules, referenceRules, makerRules, categoryRules, priceRules, discountRules, descriptionRules, stockRules};
        Label[] errorLabels = {product_label_error, product_reference_error, product_maker_error, product_category_error, product_price_error, product_discount_error, product_description_error, product_stock_error};
        String[] values = {inp_product_label.getText(), inp_product_reference.getText(), inp_product_maker.getText(), String.valueOf(cmb_product_category.getValue()).trim() , inp_product_price.getText(), inp_product_discount.getText(), inp_product_description.getText(), inp_product_stock.getText()};
        int id, stock;
        int supplier;
        int category;
        String label, reference, maker, description;
        double price;
        Date adddate, updatedate;
        boolean available;
        int discount;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);
        if(FormValidation.validFields(values, rules, errorLabels)){
            id = selectedProduct.getId();
            supplier = s.getId();
            label = inp_product_label.getText();
            reference = inp_product_reference.getText().toUpperCase();
            maker = inp_product_maker.getText();
            description = inp_product_description.getText();
            discount = Integer.parseInt(inp_product_discount.getText());
            category = CategoryDAO.searchCategory(String.valueOf(cmb_product_category.getValue()).trim()).getId();
            adddate = selectedProduct.getAdddate();
            updatedate = new Date(System.currentTimeMillis());
            available = chk_available.isSelected();
            stock = Integer.valueOf(inp_product_stock.getText());
            if(inp_product_price.getText().indexOf(',') == -1)
                price = Double.valueOf(inp_product_price.getText());
            else
                price = Double.valueOf(inp_product_price.getText().replace(',', '.'));

            Product p = new Product(id, supplier, label, reference, maker, price, category, description, adddate, updatedate, available, stock, discount);
            if(ProductDAO.updateProduct(p)){
                products.set(selectedIndex, p);
                lbl_update_success.setVisible(true);
            }
        }
        else{
            lbl_update_success.setVisible(false);
        }
    }

    // Check fields values and generate a product that'll be inserted in DB
    public void product_add(){
        String[] referenceRules = {"required", "min_length(4)", "max_length(10)", "regex(^[\\w\\-]+$)", "unique(products.reference)"};
        String[][] rules = {labelRules, referenceRules, makerRules, categoryRules, priceRules, discountRules, descriptionRules, stockRules};
        Label[] errorLabels = {product_label_error, product_reference_error, product_maker_error, product_category_error, product_price_error, product_discount_error, product_description_error, product_stock_error};
        String[] values = {inp_product_label.getText(), inp_product_reference.getText().toUpperCase(), inp_product_maker.getText(), String.valueOf(cmb_product_category.getValue()).trim() , inp_product_price.getText(), inp_product_discount.getText(), inp_product_description.getText(), inp_product_stock.getText()};
        int id, stock;
        int supplier;
        int category;
        String label, reference, maker, description;
        double price;
        Date adddate;
        boolean available;
        int discount;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);
        if(FormValidation.validFields(values, rules, errorLabels)){
            id = ProductDAO.getLastId() + 1;
            supplier = s.getId();
            label = inp_product_label.getText();
            reference = inp_product_reference.getText().toUpperCase();
            maker = inp_product_maker.getText();
            description = inp_product_description.getText();
            discount = Integer.parseInt(inp_product_discount.getText());
            category = CategoryDAO.searchCategory(String.valueOf(cmb_product_category.getValue()).trim()).getId();
            adddate = new Date(System.currentTimeMillis());
            available = chk_available.isSelected();
            stock = Integer.valueOf(inp_product_stock.getText());
            if(inp_product_price.getText().indexOf(',') == -1)
                price = Double.valueOf(inp_product_price.getText());
            else
                price = Double.valueOf(inp_product_price.getText().replace(',', '.'));

            Product p = new Product(id, supplier, label, reference, maker, price, category, description, adddate, available, stock, discount);

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

    // On click on 'remove' button
    // Display an alert to remove selected product
    public void product_remove(ActionEvent actionEvent) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suppression de " + selectedProduct.getLabel());
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment supprimer ce produit?\nCette opération est irréversible.");
        Optional<ButtonType> btn_confirm = confirm.showAndWait();

        if(btn_confirm.get() == ButtonType.OK){
            if(ProductDAO.removeProduct(selectedProduct.getId())){
                products.remove(selectedProduct);
            }
            else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Suppression de " + selectedProduct.getLabel());
                error.setContentText("Erreur lors de la suppression du produit. ");
            }
        }

    }

    // On input entry or 'category' combobox changed
    // Get field and combobox values to perform a custom select query depending on parameters
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

    // On click on 'Add a product' button
    // Clear form fields and turn user to adding mode
    public void btn_add(ActionEvent actionEvent) {
        product_table.setDisable(true);
        form_title.setText("Ajouter un article");
        btn_product_save.setText("Ajouter");
        btn_product_cancel.setVisible(true);
        clearProductFields();
        clearProductErrors();
    }

    // On click on 'cancel' button
    // Reset fields from adding product and turn to selection mode
    public void cancel(ActionEvent actionEvent) {
        product_table.setDisable(false);
        form_title.setText("Informations détaillées");
        btn_product_save.setText("Enregistrer");
        btn_product_cancel.setVisible(false);
        clearProductErrors();
        clearProductFields();
    }

    public void clearProductErrors(){
        product_label_error.setText("");
        product_reference_error.setText("");
        product_maker_error.setText("");
        product_price_error.setText("");
        product_description_error.setText("");
        product_stock_error.setText("");
        product_discount_error.setText("");
    }

    public void clearProductFields(){
        inp_product_label.setText("");
        inp_product_reference.setText("");
        inp_product_maker.setText("");
        inp_product_price.setText("");
        inp_product_discount.setText("");
        inp_product_description.setText("");
        inp_product_stock.setText("");
        chk_available.setSelected(true);
        lbl_add_date.setText("");
        lbl_update_date.setText("");
        lbl_id.setText("");
        lbl_update_success.setVisible(false);
        lbl_add_success.setVisible(false);
    }

    // On 'label' input value changed
    public void label_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_product_label.getText(), labelRules);
        product_label_error.setText(FormValidation.getFirstMessage());
    }

    // On 'reference' input value changed
    public void reference_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_product_reference.getText(), referenceRules);
        product_reference_error.setText(FormValidation.getFirstMessage());
    }

    // On 'maker' input value changed
    public void maker_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_product_maker.getText(), makerRules);
        product_maker_error.setText(FormValidation.getFirstMessage());
    }

    // On 'price' input value changed
    public void price_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_product_price.getText(), priceRules);
        product_price_error.setText(FormValidation.getFirstMessage());
    }

    // On 'discount' input value changed
    public void discount_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_product_discount.getText(), discountRules);
        product_discount_error.setText(FormValidation.getFirstMessage());
    }

    // On 'description' input value changed
    public void description_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_product_description.getText(), descriptionRules);
        product_description_error.setText(FormValidation.getFirstMessage());
    }
    // On 'stock' input value changed

    public void stock_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_product_stock.getText(), stockRules);
        product_stock_error.setText(FormValidation.getFirstMessage());
    }

    // On 'category' combobox item selected
    public void category_changed(ActionEvent actionEvent) {
        FormValidation.validField(String.valueOf(cmb_product_category.getValue()).trim(), categoryRules);
        product_category_error.setText(FormValidation.getFirstMessage());
    }

    // On 'supplier' combobox item selected
    public void supplier_changed(ActionEvent actionEvent) {

    }




    public void order_search_id(ActionEvent actionEvent) {
        String[] idRules = {"integer", "exists(orders.orderID)"};
        if(inp_order_search_id.getText().isBlank()){
            lbl_order_search_id_error.setText("");
            orders.clear();
            orders.addAll(OrderDAO.getOrders());
            if(orders.size() == 0) lbl_nb_orders.setText("Aucune commande");
            else lbl_nb_orders.setText(orders.size() + " commandes");
        }
        else{
            if(FormValidation.validField(inp_order_search_id.getText(), idRules).size() > 0){
                lbl_order_search_id_error.setText(FormValidation.getFirstMessage());
            }
            else{
                lbl_order_search_id_error.setText("");
                orders.clear();
                orders.addAll(OrderDAO.getOrder(Integer.parseInt(inp_order_search_id.getText())));
            }
        }
    }

    public void order_search_state(ActionEvent actionEvent) {
        search_orders();
    }

    public void order_search_payment(ActionEvent actionEvent) {
        search_orders();
    }

    public void order_search_owner(ActionEvent actionEvent) {
        search_orders();
    }

    public void search_orders(){
        String entry = inp_order_owner.getText();
        int state;
        int payment;

        switch(cmb_order_state_search.getValue().toString()){
            case("Non validées"): state = 0; break;
            case("En cours"): state = 1; break;
            case("Achevées"): state = 2; break;
            case("Annulées"): state = 3; break;
            default: state = -1;
        }
        switch(cmb_order_payment_search.getValue().toString()){
            case("Effectué"): payment = 1; break;
            case("En attente"): payment = 0; break;
            default: payment = -1;
        }

        orders.clear();
        orders.addAll(OrderDAO.searchOrders(entry, state, payment));
        if(orders.size() == 0) lbl_nb_orders.setText("Aucune commande");
        else if(orders.size() == 1) lbl_nb_orders.setText("1 commande");
        else lbl_nb_orders.setText(orders.size() + " commandes");
    }

    public void order_select(MouseEvent mouseEvent) {
        order_products_pane.getChildren().clear();
        if(orders_table.getSelectionModel() != null && orders_table.getSelectionModel().getSelectedItem() != null) {
            selectedOrder = orders_table.getSelectionModel().getSelectedItem();
            lbl_order_id.setText(String.valueOf(selectedOrder.getId()));
            inp_order_owner.setText(selectedOrder.getOwnerLogin());
            lbl_order_price.setText(selectedOrder.getStrPrice());
            inp_order_billingadress.setText(selectedOrder.getBillingAdress().toString());
            inp_order_deliveryadress.setText(selectedOrder.getDeliveryAdress().toString());
            lbl_order_nbproducts.setText(selectedOrder.getNbproducts() + " produits");
            if(selectedOrder.getDiscount().getCode() == null){
                inp_order_discount.setText("");
                lbl_order_discount.setText("");
            }
            else{
                inp_order_discount.setText(selectedOrder.getDiscount().getCode());
                lbl_order_discount.setText(selectedOrder.getDiscount().getDiscountStrType());

            }
            cmb_order_state.setValue(selectedOrder.getStrState());
            cmb_order_payed.setValue(selectedOrder.getStrPayed());

            if (selectedOrder.getDate() != null) {
                LocalDate date = LocalDate.parse(String.valueOf(selectedOrder.getDate()));
                lbl_order_date.setText(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            }
            else{
                lbl_order_date.setText(" - ");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/productLine.fxml"));

            selectedOrder.getProducts().forEach((id, attr) -> {
                Label lbldelivered, lbllabel, lblqty, lblproduct;
                Product p = ProductDAO.getProduct(id);
                if(p != null) {
                    /*lblproduct = new Label("\tProduit n°" + p.getId());
                    lbllabel = new Label("Article:\t" + p.getMaker() + " - " + p.getLabel());
                    lblqty = new Label("Quantité:  " + attr.getKey());
                    if (attr.getValue()){
                        lbldelivered = new Label("Livré:\tOui"); }
                    else {
                        lbldelivered = new Label("Livré:\tNon"); }
                    order_products_pane.getChildren().addAll(new VBox(lblproduct, lbllabel, lblqty, lbldelivered));*/

                }
            });
            /*order_products_pane.getChildren().forEach(vbox -> {
                vbox.setStyle("-fx-background-color: #e8e8e8; -fx-padding: 6px; -fx-font-family: Verdana");
                VBox.setMargin(vbox, new Insets(8,0,0,0));
            });*/

            try {
                order_products_pane.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void order_update(){
        selectedOrder.setDiscount(OrderDAO.getDiscount(inp_order_discount.getText()));
        selectedOrder.setStrState(cmb_order_state.getValue().toString());
        selectedOrder.setStrPayed(cmb_order_payed.getValue().toString());

    }

    public void order_discount_changed(ActionEvent actionEvent) {
        System.out.println("triggered");
        String[] discountRules = {"exists(discounts.code)"};
        if(FormValidation.validField(inp_order_discount.getText(), discountRules).size() == 0){
            selectedOrder.setDiscount(OrderDAO.getDiscount(inp_order_discount.getText()));
            lbl_order_discount_error.setText("");
            lbl_order_discount.setText(selectedOrder.getDiscount().getDiscountStrType());
            lbl_order_price.setText(selectedOrder.getStrPrice());
        }
        else{
            lbl_order_discount_error.setText(FormValidation.getFirstMessage());
            lbl_order_discount.setText("");
        }
    }


}
