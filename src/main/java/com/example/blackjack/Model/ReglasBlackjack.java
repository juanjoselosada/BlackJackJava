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
        if(resultado.equals("GANA_BANCA")) {
            return 0;
        } else if (resultado.equals("EMPATE")) {
            return apuesta;
        } else if(resultado.equals("GANA_JUGADOR")) {
            if (victoriaConBlackjack) {
                return apuesta * 5 / 2;
            } else {
                return apuesta * 2;
            }
        }
        return 0;
    }

    public static int calcularPuntaje(Mano mano) {

        NodoMano actual = mano.getCabeza();
        int suma = 0;
        int ases = 0;
        while(actual != null) {
            String valor = actual.getCarta().getValor();

            if(valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
                suma += 10;
            } else if (valor.equals("A")) {
                suma += 1;
                ases++;
            }
            else {
                suma += Integer.parseInt(valor);
            }

            actual = actual.getSiguiente();
        }
        while(ases > 0 && suma + 10 <= 21) {
            suma += 10;
            ases--;
        }
        return suma;
    }
    public static boolean esBlackjack(Mano mano) {
        if(mano.getTamano() == 2 && calcularPuntaje(mano) == 21) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean esVeinteYMedio(Mano mano) {
        if(mano.getTamano() == 2 && calcularPuntaje(mano) == 14) {
            return true;
        } else {
            return false;
        }
    }
    public static String determinarResultado(Mano manoJugador, Mano manoBanca, boolean jugadorSePaso, boolean bancaSePaso) {
        if(jugadorSePaso) {
            return "GANA_BANCA";
        }
        if(bancaSePaso) {
            return "GANA_JUGADOR";
        }
        if (esBlackjack(manoJugador) && esBlackjack(manoBanca)) {
            return "EMPATE";
        }
        if (esBlackjack(manoJugador)) {
            return "GANA_JUGADOR";
        }
        if (esBlackjack(manoBanca)) {
            return "GANA_BANCA";
        }
        if (esVeinteYMedio(manoJugador) && esVeinteYMedio(manoBanca)) {
            return "EMPATE";
        }
        if (esVeinteYMedio(manoJugador)) {
            if (calcularPuntaje(manoBanca) == 21) {
                return "GANA_BANCA";
            }
            return "GANA_JUGADOR";
        }
        if (esVeinteYMedio(manoBanca)) {
            if (calcularPuntaje(manoJugador) == 21) {
                return "GANA_JUGADOR";
            }
            return "GANA_BANCA";
        }

        int pJ = calcularPuntaje(manoJugador);
        int pB = calcularPuntaje(manoBanca);
        if (pJ > pB) {
            return "GANA_JUGADOR";
        }
        if (pB > pJ) {
            return "GANA_BANCA";
        }
        return "EMPATE";
    }
}
