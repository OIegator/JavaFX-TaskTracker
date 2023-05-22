module com.example.javafxtasktracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.javafxtasktracker to javafx.fxml;
    exports com.javafxtasktracker;
}