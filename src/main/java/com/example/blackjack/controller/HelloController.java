package com.example.blackjack.controller;

import com.example.blackjack.model.Partida;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class HelloController {

    private Partida partida = new Partida();
    private List<ImageView> cartascontroller = new ArrayList<>();

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
    private ImageView apostar2000;

    @FXML
    private ImageView apostar1000;

    @FXML
    private ImageView apostar500;

    @FXML
    private ImageView apostar200;


    @FXML
    private void repartir(){
        partida.pedirCartaJugador();
    }

    @FXML
    private void plantarse(){
        partida.plantarseJugador();
    }

    @FXML
    private void iniciarPartida(){
        partida.partida();
        cartascontroller = List.of(imgCarta1, imgCarta2, imgCarta3, imgCarta4, imgCarta5, imgCarta6, imgCarta7, imgCarta8);
        limpiarCartas();
    }
    private void limpiarCartas() {
        for (ImageView carta : cartascontroller) {
            carta.setImage(null);
        }
    }



    @FXML
    private void apostar(MouseEvent mouseEvent) {
        Object origen = mouseEvent.getSource();

        if (mouseEvent.getSource() == apostar200) {
           // partida.valorApuesta = int 200;
        }
        else if (mouseEvent.getSource() == apostar500) {
        //    x = int 500;
        }
        else if (mouseEvent.getSource() == apostar1000) {
         //   x = int 1000;
        }
        else if (mouseEvent.getSource() == apostar2000) {
          //  x = int 2000;
        }
    }
    @FXML
    private void aumentar(MouseEvent mouseEvent) {
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

        } else if (mouseEvent.getSource() == apostar2000) {
            apostar2000.setScaleX(1.2);
            apostar2000.setScaleY(1.2);

        } else if (mouseEvent.getSource() == apostar1000) {
            apostar1000.setScaleX(1.2);
            apostar1000.setScaleY(1.2);

        } else if (mouseEvent.getSource() == apostar500) {
            apostar500.setScaleX(1.2);
            apostar500.setScaleY(1.2);

        } else if (mouseEvent.getSource() == apostar200) {
            apostar200.setScaleX(1.2);
            apostar200.setScaleY(1.2);

        }

    }        @FXML
        private void restaurar(MouseEvent mouseEvent) {

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
        } else if (mouseEvent.getSource() == apostar2000) {
            apostar2000.setScaleX(1.0);
            apostar2000.setScaleY(1.0);

        } else if (mouseEvent.getSource() == apostar1000) {
            apostar1000.setScaleX(1.0);
            apostar1000.setScaleY(1.0);

        } else if (mouseEvent.getSource() == apostar500) {
            apostar500.setScaleX(1.0);
            apostar500.setScaleY(1.0);

        } else if (mouseEvent.getSource() == apostar200) {
            apostar200.setScaleX(1.0);
            apostar200.setScaleY(1.0);

        }
    }
}