package com.example.blackjack.Model;

import java.util.ArrayList;

    public class Mazo {
        private ArrayList<Carta> Mano = new ArrayList<>();


        public Mazo(Carta carta) {
        Mano.add(carta);
    }
}