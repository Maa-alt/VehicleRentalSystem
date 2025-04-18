module com.example.demo {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.demo;
    opens com.example.demo to javafx.fxml;
}
