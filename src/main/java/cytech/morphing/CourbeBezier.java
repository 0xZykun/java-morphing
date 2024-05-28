package cytech.morphing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CourbeBezier {

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
            // Tracer la courbe de Bézier entre les trois derniers points et le premier point
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

    public static void finirForme(List<Point> pointsGauche, List<Point> pointsDroite) {
        if (pointsGauche.isEmpty() || pointsDroite.isEmpty()) {
            return;
        }

        // Ajouter les points de contrôle pour fermer la courbe à gauche
        Point premierPointGauche = pointsGauche.get(0);
        Point dernierPointGauche = pointsGauche.get(pointsGauche.size() - 1);

        Point controle1Gauche = new Point(
            dernierPointGauche.getX() * 2 / 3 + premierPointGauche.getX() / 3,
            dernierPointGauche.getY() * 2 / 3 + premierPointGauche.getY() / 3
        );
        Point controle2Gauche = new Point(
            dernierPointGauche.getX() / 3 + premierPointGauche.getX() * 2 / 3,
            dernierPointGauche.getY() / 3 + premierPointGauche.getY() * 2 / 3
        );

        pointsGauche.add(controle1Gauche);
        pointsGauche.add(controle2Gauche);

        // Ajouter les points de contrôle pour fermer la courbe à droite
        Point premierPointDroite = pointsDroite.get(0);
        Point dernierPointDroite = pointsDroite.get(pointsDroite.size() - 1);

        Point controle1Droite = new Point(
            dernierPointDroite.getX() * 2 / 3 + premierPointDroite.getX() / 3,
            dernierPointDroite.getY() * 2 / 3 + premierPointDroite.getY() / 3
        );
        Point controle2Droite = new Point(
            dernierPointDroite.getX() / 3 + premierPointDroite.getX() * 2 / 3,
            dernierPointDroite.getY() / 3 + premierPointDroite.getY() * 2 / 3
        );

        pointsDroite.add(controle1Droite);
        pointsDroite.add(controle2Droite);
    }

    /**
	 * Fonction factorielle évalué en n
	 * @param n
	 * @return n!
	 */
	private static int facto(int n){
		if( n>1){
			return n* facto(n-1);
		}
		else return 1;
	}

	/**
	 * Calcul i parmi n 
	 * @param n
	 * @param i
	 * @return i parmi n
	 */
	private static int coefBino( int n , int i){
		return (int) facto(n)/ (facto(i)*facto(n-i) );
	}

	/**
	 * Calcul la valeur de la courbe à l'instant t
	 * @param t
	 * @return C(t)
	 */
	private static Point pBernstein(List<Point> courbe, double t){
		double x=0;
		double y =0;
		int n = courbe.size() ;
		for (int i=0 ; i< n; i++ ) {
	
			double c = coefBino(n-1, i) * Math.pow(t,i) * Math.pow(1-t ,n-i-1)  ;
			x+= c * courbe.get(i).getX();
			y+= c* courbe.get(i).getY();
		}
		return new Point( (int) x, (int) y);

	}

	/**
	 * Calcul le pas à prendre pour tracer la courbe
	 * @return le pas
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
	 * Donne la liste de points qui forme la courbe de bézier sans doublon
	 * @return la liste de points
	 */
	public static List<Point> cBezier(List<Point> points){
        List<Point> pointsFinal = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i += 3) {
            List<Point> courbe = new LinkedList<>();
            courbe.add(points.get(i));
            courbe.add(points.get(i+1));
            courbe.add(points.get(i+2));
            courbe.add(points.get(i+3));

            double pas = getPas(courbe);
            int m = (int) Math.floor(1/pas)+1;

            pointsFinal.add(pBernstein(courbe, 0));
            for (int k =1 ; k< m ; k++){ 
                Point pTemp = pBernstein(courbe, k*pas);
                if ( ! pTemp.equals(pointsFinal.get(pointsFinal.size()-1))){
                    pointsFinal.add(pTemp);
                }
            }
        }

		return pointsFinal;
	}
}
