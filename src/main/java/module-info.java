module org.filrouge {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;

    opens org.filrouge to javafx.fxml;
    exports org.filrouge;
    exports org.filrouge.controllers;
    exports org.filrouge.DAL;
}