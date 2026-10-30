package com.example.blackjack.controller;

// ----------------------------------------------------------------------------
// IMPORTS
// ----------------------------------------------------------------------------

// Clases del MODELO (la lógica del juego vive aquí, el controlador solo la usa)
import com.example.blackjack.model.Carta;           // una carta: palo + valor
import com.example.blackjack.model.Mano;            // lista enlazada de cartas de un jugador
import com.example.blackjack.model.NodoMano;        // nodo de la lista enlazada de Mano
import com.example.blackjack.model.Partida;         // estado y flujo completo de una partida
import com.example.blackjack.model.ReglasBlackjack; // cálculo de puntajes, blackjack, etc.

// Clases de JavaFX
import javafx.fxml.FXML;                     // marca campos/métodos que enlaza el archivo .fxml
import javafx.scene.Node;                    // cualquier elemento visual (imagen, botón, etiqueta...)
import javafx.scene.control.Alert;           // ventana emergente de aviso
import javafx.scene.control.Button;          // botón
import javafx.scene.control.Label;           // texto en pantalla
import javafx.scene.image.Image;             // imagen cargada en memoria
import javafx.scene.image.ImageView;         // "marco" donde se dibuja una Image
import javafx.scene.input.MouseButton;       // PRIMARY (izquierdo) / SECONDARY (derecho)
import javafx.scene.input.MouseEvent;        // evento de ratón (clic, entrar, salir...)
import javafx.scene.layout.Pane;             // panel con posicionamiento libre (layoutX/layoutY)

// Clases de Java estándar
import java.net.URL;                         // ubicación de un recurso (imagen) dentro del proyecto
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;                     // para formatear números con punto de miles
import java.util.Map;

/**
 * Controlador de la pantalla principal (hello-view.fxml).
 *
 * Su trabajo es SOLO conectar la interfaz con el modelo:
 *   1. Escucha los clics (INICIAR, PEDIR, PLANTARSE, fichas).
 *   2. Le pide al modelo ({@link Partida}) que haga lo que corresponda.
 *   3. Redibuja la pantalla según el estado que tenga el modelo.
 *
 * Las reglas del blackjack (quién gana, cuándo juega la banca, pagos...) NO están
 * aquí: están en Partida y ReglasBlackjack.
 *
 * Distribución de la mesa:
 *   - Fila de ARRIBA (imgCarta1..4)  = cartas de la BANCA.
 *   - Fila de ABAJO  (imgCarta5..8)  = cartas del JUGADOR.
 */
public class HelloController {

    // ------------------------------------------------------------------------
    // CONSTANTES
    // ------------------------------------------------------------------------

    /** Carpeta (dentro de resources) donde están las imágenes de cartas y fichas. */
    private static final String RUTA_IMAGENES = "/com/example/blackjack/Images/";

    /*
     * Valor en dinero de cada ficha.
     * OJO: los nombres apostar200 / apostar500 / apostar1000 / apostar2000 son los
     * fx:id del archivo .fxml (se dejaron así para no tener que editar el FXML).
     * El valor REAL de cada ficha se define aquí, no en el nombre del fx:id:
     *
     *   fx:id           imagen             valor que apuesta
     *   apostar200   -> ficha_negra.png  -> 200
     *   apostar500   -> ficha_verde.png  -> 500
     *   apostar1000  -> ficha_azul.png   -> 750   (antes 1000)
     *   apostar2000  -> ficha_roja.png   -> 1000  (antes 2000)
     *
     * Todos son múltiplos de 10, como exige ReglasBlackjack.validarApuesta.
     */
    private static final int VALOR_FICHA_NEGRA = 200;
    private static final int VALOR_FICHA_VERDE = 500;
    private static final int VALOR_FICHA_AZUL = 750;
    private static final int VALOR_FICHA_ROJA = 1000;

    // ------------------------------------------------------------------------
    // ESTADO DEL CONTROLADOR
    // ------------------------------------------------------------------------

    /** El modelo: guarda saldo, apuesta, manos, mazo, turno y resultado. */
    private Partida partida = new Partida();

    /** Fila de cartas de la banca (arriba). Se crea en initialize(). */
    private FilaCartas filaBanca;
    /** Fila de cartas del jugador (abajo). Se crea en initialize(). */
    private FilaCartas filaJugador;

    /** Texto con los puntos de la banca (no está en el FXML, se crea por código). */
    private Label lblPuntosBanca;
    /** Texto con los puntos del jugador (no está en el FXML, se crea por código). */
    private Label lblPuntosJugador;

    /**
     * Caché de imágenes ya cargadas: nombre de archivo -> Image.
     * Así cada PNG se lee del disco una sola vez aunque salga muchas veces.
     */
    private final Map<String, Image> cacheImagenes = new HashMap<>();

    /** Apuesta que el jugador va armando con las fichas ANTES de pulsar INICIAR. */
    private int apuestaSeleccionada = 0;

    /**
     * Saldo justo antes de empezar la ronda.
     * Se usa al final para calcular cuánto se ganó o se perdió (saldo final - este valor).
     */
    private int saldoAntesDeRonda = 0;

    // ------------------------------------------------------------------------
    // ELEMENTOS DEL FXML
    // (JavaFX los rellena solo, buscando el fx:id con el mismo nombre del campo)
    // ------------------------------------------------------------------------

    // Casillas de cartas. 1-4 = banca (fila de arriba), 5-8 = jugador (fila de abajo)
    @FXML
    private ImageView imgCarta1;
    @FXML
    private ImageView imgCarta2;
    @FXML
    private ImageView imgCarta3;
    @FXML
    private ImageView imgCarta4;
    @FXML
    private ImageView imgCarta5;
    @FXML
    private ImageView imgCarta6;
    @FXML
    private ImageView imgCarta7;
    @FXML
    private ImageView imgCarta8;

    // Fichas de apuesta (ver la tabla de valores en las constantes de arriba)
    @FXML
    private ImageView apostar2000; // ficha roja  -> VALOR_FICHA_ROJA  (1000)
    @FXML
    private ImageView apostar1000; // ficha azul  -> VALOR_FICHA_AZUL  (750)
    @FXML
    private ImageView apostar500;  // ficha verde -> VALOR_FICHA_VERDE (500)
    @FXML
    private ImageView apostar200;  // ficha negra -> VALOR_FICHA_NEGRA (200)

    // Estos elementos ya tenían fx:id en el FXML, pero el controlador no los usaba
    @FXML
    private Label totalDinero;     // muestra el SALDO del jugador
    @FXML
    private Label totalDinero1;    // muestra la APUESTA
    @FXML
    private Button botonPlantarse; // botón PLANTARSE
    @FXML
    private Button botonPerdir;    // botón PEDIR (el fx:id en el FXML se llama así, con ese nombre)
    @FXML
    private Button botonRepartir;  // botón INICIAR

    // ========================================================================
    // PREPARACIÓN INICIAL
    // ========================================================================

    /**
     * JavaFX llama a este método UNA vez, automáticamente, justo después de
     * cargar el FXML y de rellenar todos los campos @FXML.
     * Aquí dejamos el juego listo para que el jugador apueste.
     */
    @FXML
    private void initialize() {
        // En este proyecto "partida()" hace de constructor: crea las manos vacías,
        // pone el saldo inicial (1000) y deja todo en estado "sin ronda".
        partida.partida();

        // Agrupamos las 4 casillas de cada fila en un objeto FilaCartas,
        // que sabe dibujar las cartas y ampliarse si una mano pasa de 4 cartas.
        filaBanca = new FilaCartas(imgCarta1, imgCarta2, imgCarta3, imgCarta4);
        filaJugador = new FilaCartas(imgCarta5, imgCarta6, imgCarta7, imgCarta8);

        // Etiquetas de puntos: la de la banca encima de su fila, la del jugador
        // entre las dos filas (encima de sus cartas).
        lblPuntosBanca = crearEtiquetaPuntos(47, 12);
        lblPuntosJugador = crearEtiquetaPuntos(47, 258);

        // Primer dibujo: sin cartas, saldo 1.000, apuesta 0, solo INICIAR activo
        actualizarVista();
    }

    // ========================================================================
    // BOTONES DEL JUEGO
    // ========================================================================

    /**
     * Botón INICIAR: empieza una ronda nueva con la apuesta que haya en la mesa.
     *
     * Pasos:
     *   1. Si ya hay ronda en curso, no hace nada.
     *   2. Si el jugador se quedó sin saldo, reinicia la partida completa.
     *   3. Valida la apuesta con las reglas del proyecto.
     *   4. Limpia la ronda anterior, descuenta la apuesta y reparte las cartas.
     *   5. Si salió 21 de entrada, la ronda ya terminó: se muestra el resultado.
     */
    @FXML
    private void iniciarPartida() {
        // 1. No se puede iniciar otra ronda mientras haya una activa
        if (partida.isRondaActiva()) {
            return;
        }

        // 2. Sin saldo suficiente (menos de 10 = apuesta mínima): INICIAR arranca
        //    una partida totalmente nueva (el saldo vuelve al inicial) y sale.
        if (partida.getSaldo() < 10) {
            partida.partida();
            apuestaSeleccionada = 0;
            actualizarVista();
            return;
        }

        // 3. Validar la apuesta con ReglasBlackjack (mínimo 10, múltiplo de 10, no
        //    mayor al saldo). validarApuesta devuelve "" si todo está bien, o un
        //    mensaje de error si algo falla.
        String error = partida.validarApuesta(apuestaSeleccionada);
        if (!error.isEmpty()) {
            avisar(Alert.AlertType.WARNING, "Apuesta no válida",
                    error + "\nHaz clic en las fichas para apostar (clic derecho para quitar).");
            return;
        }

        // 4. Preparar y arrancar la ronda
        partida.prepararNuevaRonda();               // borra manos y mazo de la ronda anterior (no toca el saldo)
        saldoAntesDeRonda = partida.getSaldo();     // guardamos el saldo para calcular la ganancia al final
        partida.iniciarRonda(apuestaSeleccionada);  // descuenta la apuesta, mezcla un mazo nuevo y reparte
        // 2 cartas al jugador y 2 a la banca

        actualizarVista(); // dibuja las cartas repartidas (la 2.ª de la banca sale tapada)

        // 5. Si el jugador sacó 21 con sus dos primeras cartas, Partida ya jugó el
        //    turno de la banca y cerró la ronda por sí sola: solo falta mostrar el resultado.
        if (!partida.isRondaActiva()) {
            finalizarRonda();
        }
    }

    /**
     * Botón PEDIR: el jugador toma una carta más.
     * (Se llama "repartir" porque así está enlazado en el FXML: onAction="#repartir").
     */
    @FXML
    private void repartir() {
        // Solo se puede pedir carta si hay ronda y es el turno del jugador
        if (!partida.isRondaActiva() || partida.getTurno() != Partida.Turno.JUGADOR) {
            return;
        }

        // El modelo reparte la carta y decide qué pasa después:
        //   - más de 21  -> el jugador pierde y la ronda se cierra
        //   - exactamente 21 -> se planta solo, juega la banca y la ronda se cierra
        //   - menos de 21 -> la ronda sigue y el jugador puede pedir otra vez
        partida.pedirCartaJugador();
        actualizarVista();

        // Si la ronda se cerró (se pasó o llegó a 21), mostramos el resultado
        if (!partida.isRondaActiva()) {
            finalizarRonda();
        }
    }

    /**
     * Botón PLANTARSE: el jugador se queda con lo que tiene.
     * Juega la banca, se decide el ganador y se paga la apuesta.
     */
    @FXML
    private void plantarse() {
        // Solo se puede plantar si hay ronda y es el turno del jugador
        if (!partida.isRondaActiva() || partida.getTurno() != Partida.Turno.JUGADOR) {
            return;
        }

        // El modelo hace todo: la banca pide cartas hasta llegar a 17 o más,
        // se compara contra el jugador y se liquida la apuesta.
        partida.plantarseJugador();
        actualizarVista(); // ahora la banca ya se destapa y se ven todas sus cartas

        // Plantarse SIEMPRE termina la ronda
        finalizarRonda();
    }

    // ========================================================================
    // FICHAS DE APUESTA
    // ========================================================================

    /**
     * Clic sobre una ficha.
     *   - Clic izquierdo: suma el valor de la ficha a la apuesta.
     *   - Clic derecho:   resta el valor de la ficha (para corregir).
     * Solo funciona entre rondas; durante una ronda la apuesta está bloqueada.
     */
    @FXML
    private void apostar(MouseEvent mouseEvent) {
        // La apuesta no se puede cambiar a mitad de ronda
        if (partida.isRondaActiva()) {
            return;
        }

        // Averiguamos qué ficha se pulsó (getSource() devuelve el ImageView clicado)
        int valor = valorFicha(mouseEvent.getSource());
        if (valor == 0) {
            return; // no era una ficha conocida
        }

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            // Clic derecho: restar, sin bajar de 0
            apuestaSeleccionada = Math.max(0, apuestaSeleccionada - valor);
        } else {
            // Clic izquierdo: probar la apuesta nueva ANTES de aceptarla.
            // Así no se puede apostar más que el saldo.
            int nueva = apuestaSeleccionada + valor;
            String error = partida.validarApuesta(nueva);
            if (!error.isEmpty()) {
                avisar(Alert.AlertType.WARNING, "Apuesta no válida", error);
                return;
            }
            apuestaSeleccionada = nueva;
        }

        actualizarVista(); // refresca la etiqueta de apuesta
    }

    /**
     * Traduce "qué ficha se pulsó" a "cuánto dinero vale".
     * Para cambiar el valor de una ficha, se modifica la constante correspondiente arriba.
     *
     * @param origen el ImageView que recibió el clic
     * @return el valor de la ficha, o 0 si no es ninguna ficha
     */
    private int valorFicha(Object origen) {
        if (origen == apostar200) return VALOR_FICHA_NEGRA;
        if (origen == apostar500) return VALOR_FICHA_VERDE;
        if (origen == apostar1000) return VALOR_FICHA_AZUL;
        if (origen == apostar2000) return VALOR_FICHA_ROJA;
        return 0;
    }

    // ========================================================================
    // FIN DE RONDA
    // ========================================================================

    /**
     * Se llama cuando la ronda ya terminó (el modelo ya calculó el resultado y pagó).
     * Muestra una ventana con quién ganó, los puntos de cada lado y cuánto se
     * ganó o se perdió, y deja la mesa lista para la siguiente apuesta.
     */
    private void finalizarRonda() {
        // Ganancia (+) o pérdida (-) neta de la ronda: saldo actual menos el de antes de apostar
        int neto = partida.getSaldo() - saldoAntesDeRonda;

        // ¿Fue blackjack? (21 con exactamente 2 cartas). Solo cambia el título del aviso.
        boolean blackjack = ReglasBlackjack.esBlackjack(partida.getManoJugador());

        // Según el resultado que guardó el modelo, armamos el título y el detalle
        String titulo;
        String detalle;
        switch (partida.getResultado()) {
            case GANA_JUGADOR:
                titulo = blackjack ? "¡Blackjack!" : "¡Ganaste!";
                detalle = "Ganas " + formatear(neto) + ".";
                break;
            case GANA_BANCA:
                titulo = "Gana la banca";
                detalle = "Pierdes " + formatear(-neto) + "."; // neto es negativo, lo mostramos en positivo
                break;
            case EMPATE:
                titulo = "Empate";
                detalle = "Recuperas tu apuesta.";
                break;
            default:
                return; // NINGUNO: la ronda no se resolvió, no hay nada que mostrar
        }

        // Puntaje final de ambos
        String puntos = "Tú: " + ReglasBlackjack.calcularPuntaje(partida.getManoJugador())
                + "   Banca: " + ReglasBlackjack.calcularPuntaje(partida.getManoBanca());

        // Si el saldo no alcanza ni para la apuesta mínima, avisamos que INICIAR reinicia todo
        String extra = partida.getSaldo() < 10
                ? "\n\nTe quedaste sin saldo. Pulsa INICIAR para empezar una partida nueva."
                : "";

        // Mesa lista para la próxima ronda: apuesta de nuevo en 0
        apuestaSeleccionada = 0;
        actualizarVista(); // se redibuja ANTES del aviso para que se vean todas las cartas

        avisar(Alert.AlertType.INFORMATION, titulo, puntos + "\n" + detalle + extra);
    }

    // ========================================================================
    // DIBUJAR LA PANTALLA
    // ========================================================================

    /**
     * Redibuja TODO según el estado actual del modelo: cartas, puntos, saldo,
     * apuesta y qué botones están activos. Se llama después de cada acción.
     */
    private void actualizarVista() {
        // La segunda carta de la banca va tapada solo mientras es el turno del jugador.
        // Cuando la ronda termina (o juega la banca) se destapa.
        boolean ocultarBanca = partida.isRondaActiva() && partida.getTurno() == Partida.Turno.JUGADOR;

        // --- Cartas ---
        filaBanca.mostrar(imagenesDe(partida.getManoBanca(), ocultarBanca));
        filaJugador.mostrar(imagenesDe(partida.getManoJugador(), false));

        // --- Puntos (vacío si esa mano aún no tiene cartas) ---
        lblPuntosBanca.setText(textoPuntosBanca(ocultarBanca));
        lblPuntosJugador.setText(partida.getManoJugador().getTamano() == 0 ? ""
                : "JUGADOR: " + ReglasBlackjack.calcularPuntaje(partida.getManoJugador()));

        // --- Dinero ---
        totalDinero.setText(formatear(partida.getSaldo()));
        // Durante la ronda se muestra la apuesta en juego; entre rondas, la que se está armando
        totalDinero1.setText(formatear(partida.isRondaActiva()
                ? partida.getApuestaActual() : apuestaSeleccionada));

        // --- Botones ---
        // PEDIR y PLANTARSE solo sirven en el turno del jugador; INICIAR solo entre rondas
        boolean turnoJugador = partida.isRondaActiva() && partida.getTurno() == Partida.Turno.JUGADOR;
        botonPerdir.setDisable(!turnoJugador);
        botonPlantarse.setDisable(!turnoJugador);
        botonRepartir.setDisable(partida.isRondaActiva());
    }

    /**
     * Convierte una mano (lista enlazada de cartas) en la lista de imágenes a dibujar.
     *
     * @param mano            la mano a convertir
     * @param ocultarSegunda  si es true, la 2.ª carta se dibuja boca abajo (reves_carta.png)
     * @return una imagen por cada carta, en el mismo orden de la mano
     */
    private List<Image> imagenesDe(Mano mano, boolean ocultarSegunda) {
        List<Image> imagenes = new ArrayList<>();

        // Recorremos la lista enlazada desde la cabeza hasta que no haya más nodos
        NodoMano actual = mano.getCabeza();
        int posicion = 0; // 0 = primera carta, 1 = segunda...
        while (actual != null) {
            if (ocultarSegunda && posicion == 1) {
                imagenes.add(cargarImagen("reves_carta.png")); // carta tapada
            } else {
                imagenes.add(imagenDe(actual.getCarta()));     // carta a la vista
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return imagenes;
    }

    /**
     * Texto de los puntos de la banca.
     * Mientras su segunda carta está tapada solo se cuenta la visible
     * (por ejemplo "BANCA: 10 + ?") para no revelar el puntaje real.
     */
    private String textoPuntosBanca(boolean ocultarSegunda) {
        Mano banca = partida.getManoBanca();

        // Aún no hay cartas de la banca: no se muestra nada
        if (banca.getTamano() == 0) {
            return "";
        }

        if (ocultarSegunda) {
            // Mano temporal con solo la primera carta, para reutilizar calcularPuntaje
            Mano visible = new Mano();
            visible.agregarCarta(banca.getCabeza().getCarta());
            return "BANCA: " + ReglasBlackjack.calcularPuntaje(visible) + " + ?";
        }
        return "BANCA: " + ReglasBlackjack.calcularPuntaje(banca);
    }

    // ========================================================================
    // IMÁGENES
    // ========================================================================

    /**
     * Imagen de una carta a partir de su palo y valor.
     * Mazo guarda los palos como "C","D","T","P" y los valores como
     * "A","2".."10","J","Q","K"; los archivos se llaman, por ejemplo,
     * "A_de_corazones.png" o "10_de_tréboles.png".
     */
    private Image imagenDe(Carta carta) {
        return cargarImagen(carta.getValor() + "_de_" + nombrePalo(carta.getPalo()) + ".png");
    }

    /**
     * Carga una imagen de la carpeta de recursos (con caché).
     *
     * @param archivo nombre del archivo, por ejemplo "reves_carta.png"
     * @return la imagen, o null si el archivo no existe (se avisa por consola)
     */
    private Image cargarImagen(String archivo) {
        // Si ya la cargamos antes, la reutilizamos
        Image imagen = cacheImagenes.get(archivo);
        if (imagen != null) {
            return imagen;
        }

        // Buscar el archivo dentro del proyecto (getResource funciona también
        // cuando el programa se empaqueta en un .jar)
        URL url = getClass().getResource(RUTA_IMAGENES + archivo);
        if (url == null) {
            // Segundo intento sin tilde, por si algún día se renombran los archivos
            // "tréboles" a "treboles" para evitar problemas de codificación
            url = getClass().getResource(RUTA_IMAGENES + archivo.replace("é", "e"));
        }
        if (url == null) {
            System.err.println("No se encontró la imagen: " + archivo);
            return null;
        }

        imagen = new Image(url.toExternalForm());
        cacheImagenes.put(archivo, imagen);
        return imagen;
    }

    /** Convierte la letra del palo (como la guarda Mazo) al nombre usado en los archivos. */
    private String nombrePalo(String palo) {
        switch (palo) {
            case "C": return "corazones";
            case "D": return "diamantes";
            case "T": return "tréboles";
            default:  return "picas"; // "P"
        }
    }

    // ========================================================================
    // UTILIDADES
    // ========================================================================

    /**
     * Crea por código una etiqueta de puntos (texto blanco, grande y en negrita)
     * y la añade a la mesa en la posición indicada.
     */
    private Label crearEtiquetaPuntos(double x, double y) {
        Label etiqueta = new Label();
        etiqueta.setLayoutX(x);
        etiqueta.setLayoutY(y);
        etiqueta.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // El "padre" de las cartas es el panel de la mesa; ahí agregamos la etiqueta
        ((Pane) imgCarta1.getParent()).getChildren().add(etiqueta);
        return etiqueta;
    }

    /**
     * Da formato de dinero con punto de miles: 1000 -> "1.000".
     * Se usa Locale.GERMANY porque el formato español agrupa miles solo desde 5 dígitos.
     */
    private String formatear(int numero) {
        return String.format(Locale.GERMANY, "%,d", numero);
    }

    /**
     * Muestra una ventana emergente y espera a que el jugador la cierre.
     *
     * @param tipo    WARNING (advertencia) o INFORMATION (informativo)
     * @param titulo  texto grande de la cabecera
     * @param mensaje texto del cuerpo
     */
    private void avisar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Blackjack");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // bloquea hasta que el jugador pulse "Aceptar"
    }

    // ========================================================================
    // EFECTO HOVER (enlazados en el FXML con onMouseEntered / onMouseExited)
    // ========================================================================

    /** El ratón entra en una carta o ficha: se agranda un 20%. */
    @FXML
    private void aumentar(MouseEvent mouseEvent) {
        Node nodo = (Node) mouseEvent.getSource(); // el elemento sobre el que está el ratón
        nodo.setScaleX(1.2);
        nodo.setScaleY(1.2);
    }

    /** El ratón sale de una carta o ficha: vuelve a su tamaño normal. */
    @FXML
    private void restaurar(MouseEvent mouseEvent) {
        Node nodo = (Node) mouseEvent.getSource();
        nodo.setScaleX(1.0);
        nodo.setScaleY(1.0);
    }

    // ========================================================================
    // CLASE AUXILIAR: UNA FILA DE CARTAS
    // ========================================================================

    /**
     * Representa una fila de cartas en la mesa (la de la banca o la del jugador).
     *
     * El FXML solo trae 4 casillas por fila. Si una mano supera las 4 cartas
     * (pasa a menudo, sobre todo a la banca), esta clase crea casillas extra y
     * comprime toda la fila para que las cartas quepan sin invadir los botones.
     */
    private static class FilaCartas {

        /** Coordenada X máxima hasta donde pueden llegar las cartas (después están botones y fichas). */
        private static final double LIMITE_DERECHO = 640;

        private final Pane contenedor;                        // panel de la mesa donde se agregan casillas extra
        private final List<ImageView> casillas = new ArrayList<>(); // casillas del FXML + las creadas después
        private final double[] xOriginal;                     // posición X original de cada casilla del FXML
        private final double ancho;                           // ancho de una carta
        private final double alto;                            // alto de una carta
        private final double y;                               // posición Y de la fila

        /**
         * @param base las casillas que ya existen en el FXML, en orden de izquierda a derecha
         */
        FilaCartas(ImageView... base) {
            contenedor = (Pane) base[0].getParent();
            casillas.addAll(Arrays.asList(base));

            // Guardamos dónde estaba cada casilla para poder volver a esa posición
            // cuando la mano vuelva a tener 4 cartas o menos
            xOriginal = new double[base.length];
            for (int i = 0; i < base.length; i++) {
                xOriginal[i] = base[i].getLayoutX();
            }

            // Las casillas extra copian tamaño y altura de la primera casilla
            ancho = base[0].getFitWidth();
            alto = base[0].getFitHeight();
            y = base[0].getLayoutY();
        }

        /**
         * Dibuja las imágenes dadas, de izquierda a derecha. Las casillas
         * sobrantes quedan vacías.
         */
        void mostrar(List<Image> imagenes) {
            int n = imagenes.size();         // cartas a dibujar
            int fijas = xOriginal.length;    // casillas que trae el FXML (4)

            // Si hay más cartas que casillas, creamos las que falten
            while (casillas.size() < n) {
                ImageView extra = new ImageView();
                extra.setFitWidth(ancho);
                extra.setFitHeight(alto);
                extra.setLayoutY(y);
                contenedor.getChildren().add(extra);
                casillas.add(extra);
            }

            // Con más de 4 cartas, reparte el espacio disponible entre todas:
            // "paso" es la distancia horizontal entre una carta y la siguiente.
            // Con 4 o menos no hace falta (paso = 0, se usan las posiciones del FXML).
            double paso = n > fijas ? (LIMITE_DERECHO - xOriginal[0] - ancho) / (n - 1) : 0;

            for (int i = 0; i < casillas.size(); i++) {
                ImageView casilla = casillas.get(i);

                // Pone la imagen de la carta i, o vacía si esa casilla sobra
                casilla.setImage(i < n ? imagenes.get(i) : null);

                if (n > fijas) {
                    // Fila comprimida: posiciones calculadas
                    casilla.setLayoutX(xOriginal[0] + i * paso);
                } else if (i < fijas) {
                    // Fila normal: posiciones originales del FXML
                    casilla.setLayoutX(xOriginal[i]);
                }
            }
        }
    }
}
