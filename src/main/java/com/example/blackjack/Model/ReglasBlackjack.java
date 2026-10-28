package com.example.blackjack.Model;

public class ReglasBlackjack {


    public static String validarApuesta(int monto, int saldo) {

        if(saldo < 10) {
            return "No puedes jugar otra ronda, saldo insuficiente";
        }
        if(monto < 10) {
            return "Sube la apuesta, minimo son 10";
        }
        if(monto % 10 != 0) {
            return "Solo puedes con multiplos de 10";
        }
        if(monto > saldo) {
            return "Saldo insuficiente";
        }
        return "";
    }

    public static int calcularAcreditacion(int apuesta, String resultado, boolean victoriaConBlackjack) {
        if(resultado.equals())
    }

}
