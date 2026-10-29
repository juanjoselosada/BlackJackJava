package com.example.blackjack.model;

public class Partida {

    public static final int SALDO_INICIAL = 1000;
    private int saldo;
    private int  apuestaActual;
    private boolean rondaActiva;
    private Turno turno;
    private ResultadoRonda resultado;
    private boolean liquidada;
    private Mazo mazo;
    private Mano manoJugador;
    private Mano manoBanca;

    public Partida() {
    this.saldo = SALDO_INICIAL;
    apuestaActual = 0;
    rondaActiva = false;
    this.turno = Turno.FIN_RONDA;
    this.resultado = ResultadoRonda.NINGUNO;
    liquidada = false;
    this.manoJugador = new Mano();
    this.manoBanca = new Mano();
    this.mazo = null;
    }

    public int getSaldo() {
        return saldo;
    }
    public int getApuestaActual() {
        return apuestaActual;
    }
    public boolean isRondaActiva() {
        return rondaActiva;
    }
    public Turno getTurno() {
        return turno;
    }
    public ResultadoRonda getResultado() {
        return resultado;
    }
    public boolean isLiquidada() {
        return liquidada;
    }
    public Mazo getMazo() {
        return mazo;
    }
    public Mano getManoJugador() {
        return manoJugador;
    }
    public Mano getManoBanca() {
        return manoBanca;
    }

    public String validarApuesta(int monto) {
        return ReglasBlackjack.validarApuesta(monto, saldo);
    }

    public void iniciarRonda(int apuesta) {
        // HU02: si la apuesta no es válida, no inicia la ronda
        if (!validarApuesta(apuesta).equals("")) {
            return;
        }
        // HU03: no permitir otra ronda si ya hay una activa
        if (rondaActiva) {
            return;
        }

        // Guarda la apuesta y descuenta el saldo una sola vez
        apuestaActual = apuesta;
        saldo = saldo - apuesta;

        // Estado de la ronda
        rondaActiva = true;
        liquidada = false;
        resultado = ResultadoRonda.NINGUNO;

        // Manos vacías (no quedan cartas de la ronda anterior)
        manoJugador.limpiar();
        manoBanca.limpiar();

        // Mazo nuevo de 52 cartas y mezclado (HU03)
        mazo = new Mazo();
        mazo.mezclar();

        // Solo el jugador recibe 2 cartas; la banca espera (HU03)
        manoJugador.agregarCarta(mazo.repartirCarta());
        manoJugador.agregarCarta(mazo.repartirCarta());

        // Turno del jugador: puede pedir o plantarse
        turno = Turno.JUGADOR;

        int puntajeInicial = ReglasBlackjack.calcularPuntaje(manoJugador);
        if (ReglasBlackjack.esBlackjack(manoJugador) || puntajeInicial == 21) {
            ejecutarTurnoBanca();
            resolverRonda();
            liquidarApuesta();
        }
    }
    // -- HU04: pedir carta (solo en turno del jugador) --
    public void pedirCartaJugador() {
        if (!rondaActiva || turno != Turno.JUGADOR) {
            return;
        }

        manoJugador.agregarCarta(mazo.repartirCarta());
        int puntaje = ReglasBlackjack.calcularPuntaje(manoJugador);

        // Se pasó: pierde ya; la banca no juega (HU04)
        if (puntaje > 21) {
            resolverRonda();
            liquidarApuesta();
            return;
        }

        // 21: cierra turno del jugador y juega la banca (HU04)
        if (puntaje == 21) {
            ejecutarTurnoBanca();
            resolverRonda();
            liquidarApuesta();
        }
    }

    // --- HU05: plantarse ---
    public void plantarseJugador() {
        if (!rondaActiva || turno != Turno.JUGADOR) {
            return;
        }

        ejecutarTurnoBanca();
        resolverRonda();
        liquidarApuesta();
    }

    // --- HU06: turno automático de la banca ---
    private void ejecutarTurnoBanca() {
        turno = Turno.BANCA;

        // Sus dos primeras cartas al empezar su turno (HU05 / HU06)
        if (manoBanca.getTamano() == 0) {
            manoBanca.agregarCarta(mazo.repartirCarta());
            manoBanca.agregarCarta(mazo.repartirCarta());
        }

        // Pide mientras tenga menos de 17; se planta con 17–21 (HU06)
        while (ReglasBlackjack.calcularPuntaje(manoBanca) < 17) {
            manoBanca.agregarCarta(mazo.repartirCarta());
        }
    }

    // --- HU08: quién gana ---
    private void resolverRonda() {
        boolean jugadorSePaso = ReglasBlackjack.calcularPuntaje(manoJugador) > 21;
        boolean bancaSePaso = manoBanca.getTamano() > 0
                && ReglasBlackjack.calcularPuntaje(manoBanca) > 21;

        String res = ReglasBlackjack.determinarResultado(
                manoJugador, manoBanca, jugadorSePaso, bancaSePaso);

        resultado = convertirResultado(res);
        rondaActiva = false;
        turno = Turno.FIN_RONDA;
    }

    // --- HU09: pago (una sola vez por ronda) ---
    public void liquidarApuesta() {
        if (liquidada) {
            return;
        }

        boolean blackjackJugador = ReglasBlackjack.esBlackjack(manoJugador)
                && resultado == ResultadoRonda.GANA_JUGADOR;

        int acreditacion = ReglasBlackjack.calcularAcreditacion(
                apuestaActual,
                resultado.name(),
                blackjackJugador);

        saldo = saldo + acreditacion;
        liquidada = true;
    }

    // --- HU10: ¿puede apostar otra vez? ---
    public boolean puedeJugarOtraRonda() {
        return !rondaActiva && saldo >= 10;
    }

    // --- HU10: limpiar estado entre rondas (el saldo no se toca) ---
    public void prepararNuevaRonda() {
        if (rondaActiva) {
            return;
        }
        apuestaActual = 0;
        resultado = ResultadoRonda.NINGUNO;
        liquidada = false;
        manoJugador.limpiar();
        manoBanca.limpiar();
        mazo = null;
        turno = Turno.FIN_RONDA;
    }

    // Convierte el String de ReglasBlackjack al enum
    private ResultadoRonda convertirResultado(String res) {
        switch (res) {
            case "GANA_JUGADOR":
                return ResultadoRonda.GANA_JUGADOR;
            case "GANA_BANCA":
                return ResultadoRonda.GANA_BANCA;
            case "EMPATE":
                return ResultadoRonda.EMPATE;
            default:
                return ResultadoRonda.NINGUNO;
        }
    }
}

enum Turno {
    JUGADOR,
    BANCA,
    FIN_RONDA
}
enum ResultadoRonda {
    NINGUNO,
    GANA_JUGADOR,
    GANA_BANCA,
    EMPATE
}
