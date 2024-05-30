/*
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Point pour représenter et manipuler les points dans un espace 2D.
 * 
 */
public class Point {
	/**
     * Coordonnée x du point.
     */
    private double x;

    /**
     * Coordonnée y du point.
     */
    private double y;

    /**
     * Constructeur qui permet d'initialiser le point.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param x Coordonnée x du point
     * @param y Coordonnée y du point
     */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}


	/**
     * Getter qui retourne la coordonnée x du point.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @return retourne la coordonnée x du point
     */
	public double getX() {
		return x;
	}


	/**
     * Setter qui remplace la coordonnée x par newx.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param newx Nouvelle coordonnée x du point
     */
	public void setX(double newx) {
		this.x = newx;
	}


	/**
     * Getter qui retourne la coordonnée y du point.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @return retourne la coordonnée y du point
     */
	public double getY() {
		return y;
	}


	/**
     * Setter qui remplace la coordonnée y par newy.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param newy Nouvelle coordonnée y du point
     */
	public void setY(double newy) {
		this.y = newy;
	}


	/**
     * Méthode qui vérifie si deux points sont égaux.
     * 
	 * @author Marc DJOLE
     * @param obj Objet de type Object
     * @return retourne vrai si les deux points sont égaux, faux sinon
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
     * Méthode qui calcule la distance euclidienne entre deux points.
     * 
	 * @author Marc DJOLE
     * @param x2 Coordonnée x du deuxième point
     * @param y2 Coordonnée y du deuxième point
     * @return Retourne la distance entre ces deux points
     */
	public double distance(double x2, double y2){
		double x1 = this.getX();
		double y1 = this.getY();
		double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		return distance;
	}

	/**
     * Méthode qui calcule la distance euclidienne entre deux points.
     * 
	 * @author Marc DJOLE
     * @param p Point jusqu'auquel on veut calculer la distance
     * @return Retourne la distance entre ces deux points
     */
	public double distance(Point p)
	{
		return distance(p.getX(),p.getY());
	}


	/**
     * Méthode qui interpole deux points et les stocke dans le point courant.
     * 
	 * @author Marc DJOLE
     * @param p1 Point 1
     * @param p2 Point 2
     * @param coeff Coefficient d'interpolation
     */
	public void interpolation(Point p1, Point p2, double coeff) {
		this.x = p1.x * (1 - coeff) + p2.x * coeff;
		this.y = p1.y * (1 - coeff) + p2.y * coeff;
	}

	/**
     * Méthode qui additionne deux points.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param p1 Point 1
     * @param p2 Point 2
     * @return résultat de l'addition
     */
	public static Point addition(Point p1, Point p2)
	{
		return new Point(p1.getX() + p2.getX(),p1.getY() + p2.getY());
	}

	/**
     * Méthode qui soustrait p2 à p1.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param p1 Point 1
     * @param p2 Point 2
     * @return résultat de la soustraction
     */
	public static Point soustraction(Point p1, Point p2)
	{
		return new Point(p1.getX() - p2.getX(),p1.getY() - p2.getY());
	}

	/**
     * Calcule la norme du point.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @return la norme du point
     */
	public double norme2()
	{
		return Math.sqrt(scalaire(this,this));
	}

	/**
     * Méthode qui calcule le produit scalaire entre deux points.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param p1 Point 1
     * @param p2 Point 2
     * @return produit scalaire des points 1 et 2
     */
	public static double scalaire(Point p1, Point p2)
	{
		return p1.getX()*p2.getX() + p1.getY()*p2.getY();
	}

	/**
     * Méthode qui calcule un vecteur orthogonal au vecteur p1p2, de même norme que p1p2.
     * 
	 * @author Marc DJOLE
     * @param p1 Point 1
     * @param p2 Point 2
     * @return point qui contient le vecteur orthogonal à p1p2
     */
	public static Point orthogonal(Point p1, Point p2)
	{
		Point p1p2 = soustraction(p2,p1);
		return new Point(p1p2.getY(),-p1p2.getX());
	}

	@Override
    public String toString() {
        return "new Point(" + x + " * ratioLargeur, " + y + " * ratioHauteur),";
    }

	/**
     * Méthode qui vérifie si un point est situé dans un des coins de l'image.
     * 
	 * @author Marc DJOLE
     * @param width Largeur de l'image
     * @param height Hauteur de l'image
     * @return Retourne vrai si le point est situé dans un des coins de l'image
     */
	public boolean estAuCoin(int width, int height){
		return ((x == 0 && y == 0) ||               // Si le point est situe dans le coin superieur gauche
				(x == 0 && y == height - 1) ||      // Si le point est situe dans le coin inferieur gauche
				(x == width -1 && y == 0) ||        // // Si le point est situe dans le coin superieur droit
				(x == width -1 && y == height - 1)  // Si le point est situe dans le coin inferieur droit
		       );
	}

	/**
     * Méthode qui vérifie si un point est situé dans un des coins de l'image.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param width Largeur de l'image
     * @param height Hauteur de l'image
     * @return Retourne vrai si le point est situé dans un des coins de l'image
     */
	public static List<Point> copieProfonde(List<Point> listePoints){
		List<Point> copy  = new ArrayList<>();
		for (Point point : listePoints){
			copy.add(new Point(point.getX(), point.getY()));
		}
		return  copy;
	}

	/**
     * Méthode qui vérifie si le point courant appartient à la forme définie par une liste de points.
     * 
	 * @author Thomas BEAUSSART
     * @param points Liste des points qui forment le contour de l'image
     * @return true si le point appartient à l'image, false sinon
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

	/**
     * Méthode qui calcule la distance depuis le point courant jusqu'à un segment en paramètre.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param s segment jusqu'auquel on veut calculer la distance
     * @return double qui contient la distance
     */
	public double distanceSegment(Segment s)
	{
		// calcul du projeté orthogonal du point courant sur la droite portée par s
		Point v = Point.soustraction(s.getB(),s.getA());
		v = Point.scale(v,1/v.norme2());
		Point u = Point.soustraction(this,s.getA());
		Point h = Point.scale(v,Point.scalaire(u,v));
		h = Point.addition(h, s.getA());

		//calcul de la distance au Segment s
		if(h.distance(s.getA()) > s.getLongueur())
		{
			return distance(s.getB());
		}
		else
		{	
			if(h.distance(s.getB()) > s.getLongueur())
			{
				return distance(s.getA());
			} else { return distance(h); }
		}
		
	}

	/**
     * Méthode qui multiplie un point par un scalaire.
     * 
	 * @author Ruben PETTENG NGONGANG
     * @param p Point que l'on veut mettre à l'échelle
     * @param x scalaire par lequel multiplier
     * @return point résultat de la multiplication de p par x
     */		
	public static Point scale(Point p,double x)
	{
		return new Point(p.getX()*x,p.getY()*x);
	}
}