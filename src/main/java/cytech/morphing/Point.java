package cytech.morphing;

/* *************
																												*    POINT    *
																												***************/

import java.util.ArrayList;
import java.util.List;

public class Point {
	double x; // Coordonnee x du point
	double y; // Coordonne y du point


	/**
	 * Constructeur qui permet d'initialiser le point
	 * @param x : Cordonnee x du point
	 * @param y : Coordonnee y du point
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * Getter qui retourne la coordonne x du point
	 * @return : retourne la coordonne x du point
	 */
	public double getX() {
		return x;
	}


	/**
	 * Setter qui remplace la coordonnee x par newy
	 * @param newx : Nouvelle coordonnee x du point
	 */
	public void setX(double newx) {
		this.x = newx;
	}


	/**
	 * Getter qui retourne la coordonne y du point
	 * @return : retourne la coordonne y du point
	 */
	public double getY() {
		return y;
	}


	/**
	 * Setter qui remplace la coordonnee y par newy
	 * @param newy : Nouvelle coordonnee y du point
	 */
	public void setY(double newy) {
		this.y = newy;
	}


	/**
	 * Methoque qyi verifie si deux point sont egaux
	 * @param obj : Objet de type Object
	 * @return : retourne vrai si les deux points sont egaux, faux sinon
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point other = (Point) obj;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}


	/**
	 * Methode qui calcule la distance euclidienne entre deux points
	 * @param x2 : Coordonnee x du deuxieme point
	 * @param y2 : Coordonnee y du deuxieme point
	 * @return : Retourne la distance entre ces deux points
	 */
	public double distance(double x2, double y2){
		double x1 = this.getX();
		double y1 = this.getY();
		double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		return distance;
	}


	/**
	 * Methode qui calcule la distance euclidienne entre deux points
	 * @param p : Point jusqu'auquel on veut calculer la distance
	 * @return : Retourne la distance entre ces deux points
	 */
	public double distance(Point p)
	{
		return distance(p.getX(),p.getY());
	}


	/**
	 * Methode qui interpole deux point et les stockent dans le point courant
	 * @param p1 : Point 1
	 * @param p2 : Point 2
	 * @param coeff : Coefficient d'interpolation
	 */
	public void interpolation(Point p1, Point p2, double coeff) {
		this.x = p1.x * (1 - coeff) + p2.x * coeff;
		this.y = p1.y * (1 - coeff) + p2.y * coeff;
	}

	/**
	 * Methode qui somme deux point
	 * @param p1 : Point 1
	 * @param p2 : Point 2
	 * @return resultat de l'addition
	 */	
	public static Point addition(Point p1, Point p2)
	{
		return new Point(p1.getX() + p2.getX(),p1.getY() + p2.getY());
	}

	/**
	 * Methode qui soustrait p2 à p1
	 * @param p1 : Point 1
	 * @param p2 : Point 2
	 * @return resultat de la soustraction
	 */	
	public static Point soustraction(Point p1, Point p2)
	{
		return new Point(p1.getX() - p2.getX(),p1.getY() - p2.getY());
	}

	public double norme2()
	{
		return Math.sqrt(scalaire(this,this));
	}

	/**
	 * Methode qui calcule le produit scalaire entre deux points
	 * @param p1 : Point 1
	 * @param p2 : Point 2
	 * @return produit scalaire des points 1 et 2
	 */	
	public static double scalaire(Point p1, Point p2)
	{
		return p1.getX()*p2.getX() + p1.getY()*p2.getY();
	}


	/**
	 * Methode qui calcule un vecteur orthogonal au vecteur p1p2, de même norme que p1p2
	 * @param p1 : Point 1
	 * @param p2 : Point 2
	 * @return point qui contient le vecteur orthogonal à p1p2
	 */	
	public static Point orthogonal(Point p1, Point p2)
	{
		Point p1p2 = soustraction(p2,p1);	
		double y = Math.sqrt((p1p2.getX()*p1p2.getX() + p1p2.getY()*p1p2.getY())/(1+(p1p2.getY()*p1p2.getY()/p1p2.getX()*p1p2.getX())));
		
		return new Point(-(p1p2.getY()/p1p2.getX())*y,y);
	}

	@Override
    public String toString() {
        return "new Point(" + x + " * ratioLargeur, " + y + " * ratioHauteur),";
    }

	/**
	 * Methode qui verifie si un point est situe dans un des coins de l'image
	 * @param width : Largeur de l'image
	 * @param height : Hauteur de l'image
	 * @return : Retourne vrai si le point est situe dans un des coins de l'image
	 */
	public boolean estAuCoin(int width, int height){
		return ((x == 0 && y == 0) ||               // Si le point est situe dans le coin superieur gauche
				(x == 0 && y == height - 1) ||      // Si le point est situe dans le coin inferieur gauche
				(x == width -1 && y == 0) ||        // // Si le point est situe dans le coin superieur droit
				(x == width -1 && y == height - 1)  // Si le point est situe dans le coin inferieur droit
		       );
	}


	public static List<Point> deepCopy(List<Point> listePoints){
		List<Point> copy  = new ArrayList<>();
		for (Point point : listePoints){
			copy.add(new Point(point.getX(), point.getY()));
		}
		return  copy;
	}

	/**
	 * Methode qui renvoie si le pixel choisis fait partie de la forme
	 * @param Liste des points qui forme le contour de l'image
	 * @return true si le pixel appartient à l'image, false sinon
	 */
	public boolean appartientForme( List<Point> points){
		boolean haut = false;
		boolean bas = false;
		boolean droite = false;
		boolean gauche = false;

			for (Point P : points){
				if ( P.getX()== x && P.getY()>= y){
					haut = true;
				}
				if ( P.getX()== x && P.getY()<= y){
					bas=true;
				}
				if ( P.getX()>= x && P.getY()== y){
					droite =true;
				}
				if ( P.getX()<= x && P.getY()== y){
					gauche =true;
				}
				if (haut && droite && bas && gauche){
					break;
				}
			}
		
		return haut && droite && bas && gauche ;
	}
}