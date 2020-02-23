package org.filrouge.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Callback;
import org.filrouge.DAL.*;
import org.w3c.dom.ls.LSOutput;

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
    public TableColumn<Product, String> col_product_price;
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
    public TableColumn<Order, String> col_order_price;
    public TableColumn<Order, Discount> col_order_discount;
    public TableColumn<Order, String> col_order_state;
    public TableView<Order> orders_table;
    public Label orderid_error;
    public Label lbl_order_owner;
    public Label lbl_order_id;
    public Label lbl_order_price;
    public Label lbl_order_discount;
    public Label lbl_order_billingadress;
    public Label lbl_order_deliveryadress;
    public Label lbl_order_nbproducts;
    public VBox order_products_pane;
    public ComboBox cmb_order_state;
    public ComboBox cmb_order_payed;
    public TextField inp_order_discount;
    public Label lbl_order_nbproducts2;
    public Label lbl_dicount_error;
    public Label lbl_order_billingaddress_display;
    public Label lbl_order_discount_display;

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

    Product selectedProduct;
    Order selectedOrder;
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
    }

    // On click on products table
    // Selects a single product and set it to fields
    public void product_select(MouseEvent mouseEvent) {
        btn_product_save.setText("Enregistrer");
        if(product_table.getSelectionModel() != null && product_table.getSelectionModel().getSelectedItem() != null) {
            selectedProduct = product_table.getSelectionModel().getSelectedItem();
            selectedIndex = product_table.getSelectionModel().getSelectedIndex();

            inp_label.setText(selectedProduct.getLabel());
            inp_reference.setText(selectedProduct.getReference());
            inp_maker.setText(selectedProduct.getMaker());
            inp_price.setText(String.valueOf(selectedProduct.getPrice()));
            inp_description.setText(selectedProduct.getDescription());
            inp_stock.setText(String.valueOf(selectedProduct.getStock()));
            chk_available.setSelected(selectedProduct.isAvailable());
            inp_stock.setText(String.valueOf(selectedProduct.getStock()));
            lbl_id.setText("ID produit: " + selectedProduct.getId());
            cmb_supplier.setValue(SupplierDAO.getSupplier(selectedProduct.getSupplier().getId()).getName());

            if (cmb_category.getItems().contains(selectedProduct.getCategory().getLabel())) {
                cmb_category.setValue(selectedProduct.getCategory().getLabel());
            } else {
                cmb_category.setValue("\t" + selectedProduct.getCategory().getLabel());
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
        int discount = 0;

        // TEMP
        Supplier s = new Supplier(); s.setId(2);
        if(FormValidation.validFields(values, rules, errorLabels)){
            id = selectedProduct.getId();
            supplier = s.getId();
            label = inp_label.getText();
            reference = inp_reference.getText().toUpperCase();
            maker = inp_maker.getText();
            description = inp_description.getText();
            category = CategoryDAO.searchCategory(String.valueOf(cmb_category.getValue()).trim()).getId();
            adddate = selectedProduct.getAdddate();
            updatedate = new Date(System.currentTimeMillis());
            available = chk_available.isSelected();
            stock = Integer.valueOf(inp_stock.getText());
            if(inp_price.getText().indexOf(',') == -1)
                price = Double.valueOf(inp_price.getText());
            else
                price = Double.valueOf(inp_price.getText().replace(',', '.'));

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
        int discount = 0;

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
        label_error.setText("");
        reference_error.setText("");
        maker_error.setText("");
        price_error.setText("");
        description_error.setText("");
        stock_error.setText("");
    }

    public void clearProductFields(){
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

    // On 'label' input value changed
    public void label_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_label.getText(), labelRules);
        labelEr = FormValidation.getFirstMessage();
        label_error.setText(labelEr);
    }

    // On 'reference' input value changed
    public void reference_changed(KeyEvent actionEvent) {
        FormValidation.validField(inp_reference.getText(), referenceRules);
        referenceEr = FormValidation.getFirstMessage();
        reference_error.setText(referenceEr);
    }

    // On 'maker' input value changed
    public void maker_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_maker.getText(), makerRules);
        makerEr = FormValidation.getFirstMessage();
        maker_error.setText(makerEr);
    }

    // On 'price' input value changed
    public void price_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_price.getText(), priceRules);
        priceEr = FormValidation.getFirstMessage();
        price_error.setText(priceEr);
    }

    // On 'description' input value changed
    public void description_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_description.getText(), descriptionRules);
        descriptionEr = FormValidation.getFirstMessage();
        description_error.setText(descriptionEr);
    }
    // On 'stock' input value changed

    public void stock_changed(KeyEvent keyEvent) {
        FormValidation.validField(inp_stock.getText(), stockRules);
        stockEr = FormValidation.getFirstMessage();
        stock_error.setText(stockEr);
    }

    // On 'category' combobox item selected
    public void category_changed(ActionEvent actionEvent) {
        FormValidation.validField(String.valueOf(cmb_category.getValue()).trim(), categoryRules);
        categoryEr = FormValidation.getFirstMessage();
        category_error.setText(categoryEr);
    }

    // On 'supplier' combobox item selected
    public void supplier_changed(ActionEvent actionEvent) {

    }




    public void order_search_id(ActionEvent actionEvent) {
        String[] idRules = {"integer", "exists(orders.orderID)"};
        if(inp_order_id.getText().isBlank()){
            orderid_error.setText("");
            orders.clear();
            orders.addAll(OrderDAO.getOrders());
            if(orders.size() == 0) lbl_nb_orders.setText("Aucune commande");
            else lbl_nb_orders.setText(orders.size() + " commandes");
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
        Stop[] stops = {new Stop(0, Color.RED),
                new Stop(0.5, Color.GREEN),
                new Stop(1, Color.BLUE)};

        order_products_pane.getChildren().clear();
        if(orders_table.getSelectionModel() != null && orders_table.getSelectionModel().getSelectedItem() != null) {
            selectedOrder = orders_table.getSelectionModel().getSelectedItem();
            lbl_order_id.setText(String.valueOf(selectedOrder.getId()));
            lbl_order_owner.setText(selectedOrder.getOwnerLogin());
            cmb_order_payed.setValue(selectedOrder.getStrPayed());
            lbl_order_price.setText(selectedOrder.getStrPrice());
            lbl_order_billingadress.setText(selectedOrder.getBillingAdress().detailedStringAdress());
            lbl_order_deliveryadress.setText(selectedOrder.getDeliveryAdress().detailedStringAdress());
            lbl_order_nbproducts.setText(String.valueOf(selectedOrder.getNbproducts()));
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

            selectedOrder.getProducts().forEach((id, attr) -> {
                Label lbldelivered, lbllabel, lblqty, lblproduct;
                Product p = ProductDAO.getProduct(id);
                if(p != null) {
                    lblproduct = new Label("\tProduit n°" + p.getId());
                    lbllabel = new Label("Article:\t" + p.getMaker() + " - " + p.getLabel());
                    lblqty = new Label("Quantité:  " + attr.getKey());
                    if (attr.getValue()){
                        lbldelivered = new Label("Livré:\tOui"); }
                    else {
                        lbldelivered = new Label("Livré:\tNon"); }
                    order_products_pane.getChildren().addAll(new VBox(lblproduct, lbllabel, lblqty, lbldelivered));

                }
            });
            order_products_pane.getChildren().forEach(vbox -> {
                vbox.setStyle("-fx-background-color: #e8e8e8; -fx-padding: 6px; -fx-font-family: Verdana");
                VBox.setMargin(vbox, new Insets(8,0,0,0));

            });
        }
    }

    public void order_discount_changed(ActionEvent actionEvent) {
        String[] discountRules = {"exists(discounts.code)"};
        if(FormValidation.validField(inp_order_discount.getText(), discountRules).size() == 0){
            // UPDATE ORDER
            selectedOrder.calculatePrice();
            lbl_order_discount.setText(selectedOrder.getDiscount().getDiscountStrType());
            lbl_order_price.setText(selectedOrder.getStrPrice());
        }
        lbl_dicount_error.setText(FormValidation.getFirstMessage());
        lbl_order_discount.setText("");
    }
}
