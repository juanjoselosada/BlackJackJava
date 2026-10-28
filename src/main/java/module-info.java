module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.blackjack to javafx.fxml;
    exports com.example.blackjack;
    exports com.example.blackjack.Model;
    opens com.example.blackjack.Model to javafx.fxml;
}