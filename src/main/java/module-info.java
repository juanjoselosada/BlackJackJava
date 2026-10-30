module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.blackjack to javafx.fxml;
    exports com.example.blackjack;
    exports com.example.blackjack.model;
    opens com.example.blackjack.model to javafx.fxml;
    opens com.example.blackjack.controller to javafx.fxml;


}