/*
 * see import javafx.scene.paint.Color;
 *
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe CouleurGenerateur pour générer une liste de couleurs différentes.
 * 
 */
public class CouleurGenerateur {

	/**
     * Génère une liste de couleurs différentes.
     *
     * @author Anthony Garcia
     * @param nombre le nombre de couleurs à générer
     * @return une liste de couleurs différentes
     */
	public static List<Color> genererCouleursDifferentes(int nombre) {
    	List<Color> couleurs = new ArrayList<>();
    	float pasTeinte = 360.0f / nombre;

    	for (int i = 0; i < nombre; i++) {
        	float teinte = (i * pasTeinte ) % 360;
        	couleurs.add(Color.hsb(teinte, 0.75, 0.8));    	}

    	return couleurs;
	}
}


