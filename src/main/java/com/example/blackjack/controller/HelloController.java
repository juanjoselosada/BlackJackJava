package com.example.blackjack.controller;

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
    private void aumentarCarta(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == imgCarta1) {
            imgCarta1.setScaleX(1.2);
            imgCarta1.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta2) {
            imgCarta2.setScaleX(1.2);
            imgCarta2.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta3) {
            imgCarta3.setScaleX(1.2);
            imgCarta3.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta4) {
            imgCarta4.setScaleX(1.2);
            imgCarta4.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta5) {
            imgCarta5.setScaleX(1.2);
            imgCarta5.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta6) {
            imgCarta6.setScaleX(1.2);
            imgCarta6.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta7) {
            imgCarta7.setScaleX(1.2);
            imgCarta7.setScaleY(1.2);
        } else if (mouseEvent.getSource() == imgCarta8) {
            imgCarta8.setScaleX(1.2);
            imgCarta8.setScaleY(1.2);
        }

    }        @FXML
        private void restaurarCarta(MouseEvent mouseEvent) {

        if (mouseEvent.getSource() == imgCarta1) {
            imgCarta1.setScaleX(1.0);
            imgCarta1.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta2) {
            imgCarta2.setScaleX(1.0);
            imgCarta2.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta3) {
            imgCarta3.setScaleX(1.0);
            imgCarta3.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta4) {
            imgCarta4.setScaleX(1.0);
            imgCarta4.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta5) {
            imgCarta5.setScaleX(1.0);
            imgCarta5.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta6) {
            imgCarta6.setScaleX(1.0);
            imgCarta6.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta7) {
            imgCarta7.setScaleX(1.0);
            imgCarta7.setScaleY(1.0);
        } else if (mouseEvent.getSource() == imgCarta8) {
            imgCarta8.setScaleX(1.0);
            imgCarta8.setScaleY(1.0);
        }
    }
}