package com.example.blackjack.controller;

import com.example.blackjack.model.Carta;
import com.example.blackjack.model.Mano;
import com.example.blackjack.model.Mazo;
import com.example.blackjack.model.NodoMano;
import com.example.blackjack.model.Partida;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class HelloController {
    private Partida partida;
    private int apuestaSeleccionada;

    @FXML
    private Label totalDinero;

    @FXML
    private Label totalDinero1;

    @FXML
    private Button botonPerdir;

    @FXML
    private Button botonRepartir;

    @FXML
    private Button botonPlantarse;

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
    private void initialize() {
        partida = new Partida();
        apuestaSeleccionada = 0;
        actualizarInterfaz();
    }

    @FXML
    private void apostar(MouseEvent mouseEvent) {
        if (partida.isRondaActiva()) {
            return;
        }
        if (partida.isLiquidada() || partida.getApuestaActual() > 0) {
            partida.prepararNuevaRonda();
            apuestaSeleccionada = 0;
        }

        Object origen = mouseEvent.getSource();
        int ficha = 0;
        if (origen == apostar200) {
            ficha = 200;
        } else if (origen == apostar500) {
            ficha = 500;
        } else if (origen == apostar1000) {
            ficha = 1000;
        } else if (origen == apostar2000) {
            ficha = 2000;
        }

        if (ficha > 0 && apuestaSeleccionada + ficha <= partida.getSaldo()) {
            apuestaSeleccionada += ficha;
        }
        actualizarInterfaz();
    }

    @FXML
    private void repartir() {
        if (partida.isRondaActiva()) {
            return;
        }
        if (!partida.validarApuesta(apuestaSeleccionada).isEmpty()) {
            return;
        }
        partida.iniciarRonda(apuestaSeleccionada);
        apuestaSeleccionada = 0;
        actualizarInterfaz();
    }

    @FXML
    private void pedirCarta() {
        partida.pedirCartaJugador();
        actualizarInterfaz();
    }

    @FXML
    private void plantarse() {
        partida.plantarseJugador();
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        totalDinero.setText(String.valueOf(partida.getSaldo()));

        boolean mostrandoSeleccion = !partida.isRondaActiva() && apuestaSeleccionada > 0;
        int apuestaVista = mostrandoSeleccion ? apuestaSeleccionada : partida.getApuestaActual();
        totalDinero1.setText(String.valueOf(apuestaVista));

        boolean turnoJugador = partida.isRondaActiva() && partida.getTurno() == Partida.Turno.JUGADOR;
        botonPerdir.setDisable(!turnoJugador);
        botonPlantarse.setDisable(!turnoJugador);
        botonRepartir.setDisable(partida.isRondaActiva() || partida.getSaldo() < 10);

        boolean puedeElegirFicha = !partida.isRondaActiva() && partida.getSaldo() >= 10;
        apostar200.setDisable(!puedeElegirFicha);
        apostar500.setDisable(!puedeElegirFicha);
        apostar1000.setDisable(!puedeElegirFicha);
        apostar2000.setDisable(!puedeElegirFicha);

        Mazo mazo = partida.getMazo();
        if (mazo != null && mazo.cartasRestantes() == 0 && partida.getResultado() != Partida.ResultadoRonda.NINGUNO) {
            botonRepartir.setDisable(partida.getSaldo() < 10);
        }

        mostrarMano(partida.getManoJugador(), imgCarta5, imgCarta6, imgCarta7, imgCarta8);
        if (partida.isRondaActiva() && partida.getTurno() == Partida.Turno.JUGADOR) {
            mostrarMano(new Mano(), imgCarta1, imgCarta2, imgCarta3, imgCarta4);
        } else {
            mostrarMano(partida.getManoBanca(), imgCarta1, imgCarta2, imgCarta3, imgCarta4);
        }
    }

    private void mostrarMano(Mano mano, ImageView... vistas) {
        NodoMano nodo = mano.getCabeza();
        for (ImageView vista : vistas) {
            if (nodo == null) {
                vista.setImage(null);
                nodo = null;
            } else {
                vista.setImage(imagenDe(nodo.getCarta()));
                nodo = nodo.getSiguiente();
            }
        }
    }

    private Image imagenDe(Carta carta) {
        String palo = switch (carta.getPalo()) {
            case "C" -> "corazones";
            case "D" -> "diamantes";
            case "T" -> "tréboles";
            case "P" -> "picas";
            default -> carta.getPalo().toLowerCase();
        };
        String nombre = "/com/example/blackjack/Images/" + carta.getValor() + "_de_" + palo + ".png";
        var url = getClass().getResource(nombre);
        if (url == null) {
            return null;
        }
        return new Image(url.toExternalForm());
    }
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