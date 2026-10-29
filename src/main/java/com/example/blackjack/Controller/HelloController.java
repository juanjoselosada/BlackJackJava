package com.example.blackjack.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private ImageView imgCarta1;

    @FXML
    private ImageView imgCarta2;

    @FXML
    private ImageView imgCarta3;

    @FXML
    private ImageView imgCarta4;

    @FXML
    private ImageView imgCarta5;

    @FXML
    private ImageView imgCarta6;

    @FXML
    private ImageView imgCarta7;

    @FXML
    private ImageView imgCarta8;

    @FXML
    private void aumentarCarta(MouseEvent mouseEvent){
        if(mouseEvent.getSource()==imgCarta1){
            imgCarta1.setScaleX(1.5);
            imgCarta1.setScaleY(1.5);
        }else{
            imgCarta2.setScaleX(1.5);
            imgCarta2.setScaleY(1.5);
        }
    }
}
