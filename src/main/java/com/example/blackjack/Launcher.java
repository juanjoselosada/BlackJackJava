package com.example.blackjack;

import javafx.application.Application;
import com.example.blackjack.Model.Carta;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(new Carta("Corazones", "As"));
        Application.launch(BlackJackGame.class, args);

    }
}
