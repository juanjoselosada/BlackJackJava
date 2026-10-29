package com.example.blackjack.model;

public class NodoMano {
    private Carta carta;
    private NodoMano siguiente;
    public NodoMano(Carta carta) {
        this.carta = carta;
        this.siguiente = null;
    }
    public Carta getCarta() {
        return carta;
    }
    public NodoMano getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoMano siguiente) {
        this.siguiente = siguiente;
    }
}