package com.example.blackjack;

import javafx.application.Application;
import com.example.blackjack.model.Carta;

public class Launcher {
    public static void main(String[] args) {
        Carta carta;
        carta = new Carta("picas", "Ocho");
        System.out.println(new Carta("Corazones", "As"));
        //new Mazo();
        Application.launch(BlackJackGame.class, args);

    }
}