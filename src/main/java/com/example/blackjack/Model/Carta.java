package com.example.blackjack.Model;

public class Carta {

    private String palo;
    private String valor;

    public Carta(String palo, String valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public String getPalo() {
        return this.palo;
    }
    public String getValor() {
        return this.valor;
    }

    public String toString() {
        return "La carta es " + valor + " de " + palo;
    }
}

