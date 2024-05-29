package cytech.morphing;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorGenerateur {

	public static List<Color> genererCouleursDifferentes(int nombre) {
    	List<Color> couleurs = new ArrayList<>();
    	float pasTeinte = 360.0f / nombre;

    	for (int i = 0; i < nombre; i++) {
        	float teinte = (i * pasTeinte ) % 360;
        	couleurs.add(Color.hsb(teinte, 0.75, 0.8));    	}

    	return couleurs;
	}
}

