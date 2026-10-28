package com.example.blackjack.Model;

public class Mano {
    private NodoMano cabeza;
    private int tamano;

    public Mano() {
        cabeza = null;
        tamano = 0;
    }

    public void agregarCarta(Carta carta) {
        NodoMano nuevoNodo = new NodoMano(carta);

        if(cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoMano actual = cabeza;
            while(actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public void limpiar() {
        cabeza = null;
        tamano = 0;
    }
    public int getTamano() {

        return tamano;
    }
    public NodoMano getCabeza() {

        return cabeza;
    }

}
