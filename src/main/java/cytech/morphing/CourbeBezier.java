/*
 * see import javafx.scene.canvas.GraphicsContext;
 * see import javafx.scene.paint.Color;
 * 
 * see import java.util.ArrayList;
 * see import java.util.LinkedList;
 * see import java.util.List;
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe CourbeBezier pour dessiner des courbes de Bézier et gérer les points de contrôle.
 * 
 */
public class CourbeBezier {

    /**
     * Dessine une courbe de Bézier sur le contexte graphique donné.
     * 
     * @author Thomas BEAUSSART
     * @param gc le contexte graphique sur lequel dessiner
     * @param points la liste des points de contrôle de la courbe
     * @param couleur la couleur de la courbe
     * @param estFini indique si la courbe doit être fermée
     */
    public static void dessinerCourbeBezier(GraphicsContext gc, List<Point> points, Color couleur, Boolean estFini) {
        if (points.size() < 4) {
            return;
        }

        gc.setStroke(couleur);
        gc.setLineWidth(2);
        gc.beginPath();

        for (int i = 0; i < points.size() - 3; i += 3) {
            Point p0 = points.get(i);
            Point c0 = points.get(i + 1);
            Point c1 = points.get(i + 2);
            Point p1 = points.get(i + 3);

            for (double t = 0; t <= 1; t += 0.01) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * c0.getX() + 3 * (1 - t) * Math.pow(t, 2) * c1.getX() + Math.pow(t, 3) * p1.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * c0.getY() + 3 * (1 - t) * Math.pow(t, 2) * c1.getY() + Math.pow(t, 3) * p1.getY();

                gc.lineTo(x, y);
            }
        }

        if (estFini) {
            int n = points.size();
            Point p0 = points.get(n - 3);
            Point c0 = points.get(n - 2);
            Point c1 = points.get(n - 1);
            Point p1 = points.get(0);

            for (double t = 0; t <= 1; t += 0.01) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * c0.getX() + 3 * (1 - t) * Math.pow(t, 2) * c1.getX() + Math.pow(t, 3) * p1.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * c0.getY() + 3 * (1 - t) * Math.pow(t, 2) * c1.getY() + Math.pow(t, 3) * p1.getY();

                gc.lineTo(x, y);
            }
        }

        gc.stroke();
    }

    /**
     * Ajoute les points de contrôle pour fermer la courbe de Bézier.
     * 
     * @author Thomas BEAUSSART
     * @param pointsDir la liste des points de contrôle de la courbe à fermer
     */
    private static void fermerCourbe(List<Point> pointsDir){
        // Ajouter les points de contrôle pour fermer la courbe
        Point premierPoint = pointsDir.get(0);
        Point dernierPoint = pointsDir.get(pointsDir.size() - 1);

        Point controle1 = new Point(
            dernierPoint.getX() * 2 / 3 + premierPoint.getX() / 3,
            dernierPoint.getY() * 2 / 3 + premierPoint.getY() / 3
        );
        Point controle2 = new Point(
            dernierPoint.getX() / 3 + premierPoint.getX() * 2 / 3,
            dernierPoint.getY() / 3 + premierPoint.getY() * 2 / 3
        );

        pointsDir.add(controle1);
        pointsDir.add(controle2);
    }

    /**
     * Ajoute les points de contrôle pour fermer les courbes de Bézier gauche et droite.
     * 
     * @author Thomas BEAUSSART
     * @param pointsGauche la liste des points de contrôle de la courbe gauche
     * @param pointsDroite la liste des points de contrôle de la courbe droite
     */
    public static void finirForme(List<Point> pointsGauche, List<Point> pointsDroite) {
        if (pointsGauche.isEmpty() || pointsDroite.isEmpty()) {
            return;
        }
        fermerCourbe(pointsGauche);
        fermerCourbe(pointsDroite);
    }

    /**
     * Fonction factorielle évaluée en n.
     * 
     * @author Thomas BEAUSSART
     * @param n la valeur pour laquelle calculer la factorielle
     * @return la factorielle de n
     */
	private static int factorielle(int n){
		if( n>1){
			return n* factorielle(n-1);
		}
		else return 1;
	}

	/**
     * Calcule le coefficient binomial "i parmi n".
     * 
     * @author Thomas BEAUSSART
     * @param n le nombre total d'éléments
     * @param i le nombre d'éléments choisis
     * @return le coefficient binomial "i parmi n"
     */
	private static int coefficientBinomial( int n , int i){
		return (int) factorielle(n)/ (factorielle(i)*factorielle(n-i) );
	}

	/**
     * Calcule la valeur de la courbe de Bézier à l'instant t.
     * 
     * @author Thomas BEAUSSART
     * @param courbe la liste des points de contrôle de la courbe
     * @param t l'instant pour lequel calculer la valeur
     * @return la valeur de la courbe à l'instant t
     */
	private static Point valeurCourbe(List<Point> courbe, double t){
		double x=0;
		double y =0;
		int n = courbe.size() ;
		for (int i=0 ; i< n; i++ ) {
	
			double c = coefficientBinomial(n-1, i) * Math.pow(t,i) * Math.pow(1-t ,n-i-1)  ;
			x+= c * courbe.get(i).getX();
			y+= c* courbe.get(i).getY();
		}
		return new Point( (int) x, (int) y);

	}

	/**
     * Calcule le pas à prendre pour tracer la courbe.
     * 
     * @author Thomas BEAUSSART
     * @param courbe la liste des points de contrôle de la courbe
     * @return le pas à prendre pour tracer la courbe
     */
	private static double getPas(List<Point> courbe){
		double minX = courbe.get(0).getX();
		double maxX = courbe.get(0).getX();
		double minY = courbe.get(0).getY();
		double maxY = courbe.get(0).getY();
		for ( Point p : courbe){
			if ( minX> p.getX()) { minX = p.getX();}
			if ( maxX< p.getX()) { maxX = p.getX();}
			if ( minY> p.getY()) { minY = p.getY();}
			if ( maxY< p.getY()) { maxY = p.getY();}
		}
		return 1/ ( (1+maxX-minX) *(1+maxY- minY));
	}

	/**
     * Donne la liste de points qui forme la courbe de Bézier sans doublon.
     * 
     * @author Thomas BEAUSSART
     * @param points la liste des points de contrôle de la courbe
     * @return la liste des points formant la courbe de Bézier
     */
	public static List<Point> courbeBezier(List<Point> points){
        List<Point> pointsFinal = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i += 3) {
            List<Point> courbe = new LinkedList<>();
            courbe.add(points.get(i));
            courbe.add(points.get(i+1));
            courbe.add(points.get(i+2));
            courbe.add(points.get(i+3));

            double pas = getPas(courbe);
            int m = (int) Math.floor(1/pas)+1;

            pointsFinal.add(valeurCourbe(courbe, 0));
            for (int k =1 ; k< m ; k++){ 
                Point pTemp = valeurCourbe(courbe, k*pas);
                if ( ! pTemp.equals(pointsFinal.get(pointsFinal.size()-1))){
                    pointsFinal.add(pTemp);
                }
            }
        }

		return pointsFinal;
	}
}
