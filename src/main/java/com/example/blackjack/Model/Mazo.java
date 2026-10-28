package com.example.blackjack.Model;

import java.util.Random;

public class Mazo {

    private Carta[] cartas;
    private int indice;

    public Mazo() {
        this.cartas = new Carta[52];
        indice = 0;
        generarBaraja();
    }

    private void generarBaraja() {
        String[] palos = {"C", "D", "T", "P"};
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int indice = 0;
        // for-each: "para cada palo en palos..."
        for (String palo : palos) {
            // ...y para cada valor de ese palo, crea la Carta y avanza la casilla.
            for (String valor : valores) {
                cartas[indice] = new Carta(palo, valor);
                indice++;
            }
        }
    }

    public void mezclar() {
        Random random = new Random();
        // i empieza en la última carta (51) y baja hasta 1. La posición 0 queda sola.
        for (int i = cartas.length - 1; i > 0; i--) {
            // nextInt(i + 1) da un entero en [0, i], inclusive.
            int j = random.nextInt(i + 1);
            //Fisher yates
            Carta temp = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = temp;
        }
        indice = 0;
    }

    public Carta repartirCarta() {
        if (indice >= cartas.length) {
            return null;
        }
        Carta carta = cartas[indice];
        indice++;
        return carta;
    }

    public void reiniciar() {
        indice = 0;
    }

    public int cartasRestantes() {
        return cartas.length - indice;
    }
}
