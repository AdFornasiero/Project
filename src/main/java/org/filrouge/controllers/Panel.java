package org.filrouge.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import org.filrouge.DAL.Category;
import org.filrouge.DAL.Product;
import org.filrouge.DAL.ProductDAO;

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

        System.out.println(cmb_category.getValue());

    }
}
