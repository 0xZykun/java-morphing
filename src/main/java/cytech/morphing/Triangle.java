/*
 * see import javafx.scene.paint.Color;
 * see import javafx.scene.shape.Circle;
 * see import javafx.scene.shape.Polygon;
 * 
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe représentant un triangle avec des méthodes pour diverses opérations géométriques et graphiques.
 * 
 */
public class Triangle {
	/**
     * Sommet a du triangle.
     */
    private Point a;

    /**
     * Sommet b du triangle.
     */
    private Point b;

    /**
     * Sommet c du triangle.
     */
    private Point c;

    /**
     * Constructeur qui permet d'initialiser le triangle.
     * 
     * @author Marc DJOLE
     * @param a Un sommet du triangle
     * @param b Un sommet du triangle
     * @param c Un sommet du triangle
     */
	public Triangle(Point a, Point b, Point c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
     * Getter qui retourne le sommet a.
     * 
     * @author Marc DJOLE
     * @return Retourne le Point a
     */
	public Point getA() {
		return a;
	}

	/**
     * Setter qui remplace le sommet a par newa.
     * 
     * @author Marc DJOLE
     * @param newa Sommet de remplacement
     */
	public void setA(Point newa) {
		this.a.setX(newa.getX());
		this.a.setY(newa.getY());
	}

	/**
     * Getter qui retourne le sommet b.
     * 
     * @author Marc DJOLE
     * @return Retourne le Point b
     */
	public Point getB() {
		return b;
	}

	/**
     * Setter qui remplace le sommet b par newb.
     * 
     * @author Marc DJOLE
     * @param newb Sommet de remplacement
     */
	public void setB(Point newb) {
		this.b.setX(newb.getX());
		this.b.setY(newb.getY());
	}

	/**
     * Getter qui retourne le sommet c.
     * 
     * @author Marc DJOLE
     * @return Retourne le Point c
     */
	public Point getC() {
		return c;
	}

	/**
     * Setter qui remplace le sommet c par newc.
     * 
     * @author Marc DJOLE
     * @param newc Sommet de remplacement
     */
	public void setC(Point newc) {
		this.c.setX(newc.getX());
		this.c.setY(newc.getY());
	}

	/**
     * Méthode qui interpole deux triangles et renvoie le résultat.
     * 
     * @author Marc DJOLE
     * @param t Coefficient d'interpolation
     * @param p Deuxième triangle de l'interpolation
     * @return Retourne le triangle résultat de l'interpolation
     */
	public	Triangle morphToTriangle(Triangle t,double p) {
		return new Triangle(new Point(a.getX()*(1-p) + t.getA().getX()*p,a.getY()*(1-p) + t.getA().getY()*p),
							new Point(b.getX()*(1-p) + t.getB().getX()*p,b.getY()*(1-p) + t.getB().getY()*p),
							new Point(c.getX()*(1-p) + t.getC().getX()*(p),c.getY()*(1-p) + t.getC().getY()*p));
	}


	/**
     * Méthode qui calcule les coordonnées du cercle inscrit au triangle et les renvoie.
     * 
     * @author Marc DJOLE
     * @return Retourne un tableau qui contient les coordonnées du cercle circonscrit et son rayon
     */
	public double[] cercleInscrit(){

		// mediane du segment [ab]
		// On cherche les coefficients de l'equation (d1) : a1x + b1y + c1 = 0

		double a1 = 2 * (b.getX() - a.getX());
		double b1 = 2 * (b.getY() - a.getY());
		double c1 = Math.pow(a.getX(), 2) + Math.pow(a.getY(), 2) - Math.pow(b.getX(), 2) - Math.pow(b.getY(), 2);
		
		// mediane du segment [bc]
		// On cherche les coefficients de l'equation (d2) : a2x + b2y + c2 = 0

		double a2 = 2 * (c.getX() - b.getX());
		double b2 = 2 * (c.getY() - b.getY());
		double c2 = Math.pow(b.getX(), 2) + Math.pow(b.getY(), 2) - Math.pow(c.getX(), 2) - Math.pow(c.getY(), 2);
		
		// Coordonnees du centre du cercle inscrit au Triangle abc
		double centreX = (b2 * (-c1) - b1 * (-c2)) / (a1 * b2 - b1 * a2);
		double centreY = (a1 * (-c2) - a2 * (-c1)) / (a1 * b2 - b1 * a2);

		double rayon = Math.sqrt(Math.pow((centreX - a.getX()), 2) + Math.pow((centreY - a.getY()), 2));
		
		// On cree une matrice pour stocker toutes ces valeurs
		double[] coeffs = {centreX, centreY, rayon};
		
		return coeffs;

	}

	/**
     * Méthode qui crée une instance graphique du triangle et la renvoie.
     * 
     * @author Marc DJOLE
     * @return Retourne une instance graphique du triangle
     */
	public Polygon tracerTriangle() {
		
		// On place les coordonnes des points dans un tableau 
		Double[] tabPoints = {a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY()};
		Polygon trace = new Polygon();
		trace.getPoints().addAll(tabPoints);
		// Définition de la couleur de remplissage sur transparent
		trace.setFill(Color.TRANSPARENT);
		// couleur des aretes du triangle
		trace.setStroke(Color.CYAN);

		// on retourne l'instance graphique
		return trace; 
		
	}

	/**
     * Méthode qui crée une instance graphique du cercle inscrit au triangle.
     * 
     * @author Marc DJOLE
     * @return Retourne une instance graphique du cercle inscrit au triangle
     */
	public Circle tracerCercleInscrit() {

		double[] valeurs = cercleInscrit();
		Circle cercle = new Circle(valeurs[0], valeurs[1], valeurs[2]);
		cercle.setFill(Color.TRANSPARENT); // Définition de la couleur de remplissage sur transparent
	    cercle.setStroke(Color.TOMATO); //

		// on retourne l'instance graphique
		return cercle;
	}

	/**
     * Méthode qui teste si un point appartient au cercle circonscrit à un triangle.
     * 
     * @author Marc DJOLE
     * @param p Point dont on doit tester l'appartenance au cercle inscrit au triangle
     * @return Retourne vrai si le point appartient au cercle, faux sinon
     */
	public boolean cercleContientPoint(Point p){

		// Si le point a tester est un sommet du triangle, on retourne faux
		if (p.equals(a) || p.equals(b) || p.equals(c)){
			return false;
		}
		double[] valeurs = cercleInscrit();
		double distance = Math.sqrt(Math.pow(valeurs[0] - p.getX(), 2) + Math.pow(valeurs[1] - p.getY(), 2));
		return distance < valeurs[2];
	}

	/**
     * Méthode qui calcule l'aire du triangle.
     * 
     * @author Marc DJOLE
     * @return Retourne l'aire du triangle
     */
	public double aire() {
		double x1 = a.getX();
		double y1 = a.getY();
		double x2 = b.getX();
		double y2 = b.getY();
		double x3 = c.getX();
		double y3 = c.getY();

		double aireTriangle = 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
		// On retourne l'aire du triangle
		return aireTriangle;
	}


	/**
     * Méthode qui calcule les coordonnées barycentriques d'un point.
     * 
     * @author Marc DJOLE
     * @param p Point dont on doit calculer les coordonnées barycentriques
     * @return Retourne les coordonnées barycentriques sous forme de tableau
     */
	public Double[] coordoneesBarycentriques(Point p){
		Triangle BCP = new Triangle(b, c, p);
		Triangle ACP = new Triangle(a, c, p);
		Triangle ABP = new Triangle(a, b, p);

		Double alpha = BCP.aire() / this.aire();
		Double beta = ACP.aire() / this.aire();
		Double gamma = ABP.aire() / this.aire();

		Double[] coorBarycentriques = {alpha, beta, gamma};
		// On retourne les coordonnees barycentriques
		return coorBarycentriques;
	}


	/**
     * Méthode qui teste si un point appartient au triangle.
     * 
     * @author Marc DJOLE
     * @param p Point dont on doit tester l'appartenance au triangle
     * @return Retourne vrai si le point appartient au triangle, faux sinon
     */
	public boolean pointAppartient(Point p) {
		Triangle BCP = new Triangle(b, c, p);
		Triangle ACP = new Triangle(a, c, p);
		Triangle ABP = new Triangle(a, b, p);

		Double aireBCP = BCP.aire();
		Double aireACP = ACP.aire();
		Double aireABP = ABP.aire();

		/*retourne vrai si la difference de l'aire du triangle et de la somme des aires des triangles abp, acp et bcp
		 est inferieur a 1e-9. 1e-9 est un marge de securite inextinguible faute des arrondi. elle peut toutefois etre modifie
		 */
		return(aireBCP + aireABP + aireACP - this.aire() < 1e-9);
	}

	/**
     * Méthode toString pour afficher les coordonnées des sommets du triangle.
     * 
     * @author Marc DJOLE
     * @return Chaîne représentant les coordonnées des sommets du triangle
     */
	@Override
    public String toString() {
        return "A : " + a + " | B : " + b + " | C : " + c;
    }

	/**
     * Méthode qui vérifie si un des sommets du triangle est situé dans un coin de l'image.
     * 
     * @author Marc DJOLE
     * @param width Largeur de l'image
     * @param heigth Hauteur de l'image
     * @return Retourne vrai si un sommet est situé dans un coin, faux sinon
     */
	public boolean unSommetEstAuCoin(int width, int heigth){
		return (a.estAuCoin(width, heigth) || b.estAuCoin(width, heigth) || c.estAuCoin(width, heigth));
	}

	/**
     * Méthode de copie profonde pour une liste de triangles.
     * 
     * @author Marc DJOLE
     * @param listeTriangles Liste de triangles à copier
     * @return Copie profonde de la liste de triangles
     */
	public static List<Triangle> copieProfonde(List<Triangle> listeTriangles){
		List<Triangle> copy  = new ArrayList<>();
		for (Triangle triangle : listeTriangles){
			copy.add(new Triangle(new Point(triangle.getA().getX(), triangle.getA().getY()), new Point(triangle.getB().getX(), triangle.getB().getY()), new Point(triangle.getC().getX(), triangle.getC().getY())));
		}
		return  copy;
	}

}