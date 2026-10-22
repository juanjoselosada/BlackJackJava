import java.util.ArrayList;
import java.util.Collections;

public class Dealer {
    private ArrayList<String> mazo;

    public Dealer() {
        mazo = new ArrayList<>();

        String[] palos = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        String[] valores = {
                "As", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K"
        };

        for (String palo : palos) {
            for (String valor : valores) {
                mazo.add(valor + " de " + palo);
            }
        }

        Collections.shuffle(mazo);
    }

    public String repartirCarta() {
        if (mazo.isEmpty()) {
            return "No quedan cartas";
        }

        return mazo.remove(0);
    }

    public int cartasRestantes() {
        return mazo.size();
    }
}