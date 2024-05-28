package cytech.morphing;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui créer des courbes de bézier
 */
public class CBezier {
	/**
	 * Liste des points de controle qui vont servir à créer la courbe de Bezier
	 */
	private List<Point> tabP ;

	public CBezier( List<Point> tabP){
		this.tabP = tabP;
	}

	/**
	 * geteur de la liste
	 * @return liste des points de controle
	 */
	public List<Point> lP() { return tabP;}

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
	private Point pBernstein( double t){
		double x=0;
		double y =0;
		int n = tabP.size() ;
		for (int i=0 ; i< n; i++ ) {
	
			double c = coefBino(n-1, i) * Math.pow(t,i) * Math.pow(1-t ,n-i-1)  ;
			x+= c * tabP.get(i).getX();
			y+= c* tabP.get(i).getY();
		}
		return new Point( (int) x, (int) y);

	}

	/**
	 * Calcul le pas à prendre pour tracer la courbe
	 * @return le pas
	 */
	private double getPas(){
		double minX = tabP.get(0).getX();
		double maxX = tabP.get(0).getX();
		double minY = tabP.get(0).getY();
		double maxY = tabP.get(0).getY();
		for ( Point p : tabP){
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
	public List<Point> cBezier(){
		double pas = getPas();
		int m = (int) Math.floor(1/pas)+1;
		List<Point> tab = new ArrayList<>();
		tab.add(pBernstein(0));
		for (int k =1 ; k< m ; k++){ 
			Point pTemp = pBernstein(k*pas);
			if ( ! pTemp.equals(tab.get(tab.size()-1))){
				tab.add(pTemp);
			}
		}
		return tab;
	}

}