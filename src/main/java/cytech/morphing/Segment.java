package cytech.morphing;

public class Segment
{
	private Point a; // point initial a
	private Point b; // point final b
	private double longueur; // longueur du segment, distance entre a et b

	/**
	 * Constructeur qui permet d'initialiser le point
	 * @param a : point initial a
	 * @param b : point final b
	 */
	public Segment(Point a, Point b)
	{
		this.a = a;
		this.b = b;
		updateLongueur();
	}

	/**
	 * Getter qui retourne le point a
	 * @return : point a
	 */	
	public Point getA() { return a; }

	/**
	 * Getter qui retourne le point b
	 * @return : point b
	 */	
	public Point getB() { return b; }
	
	/**
	 * Setter qui remplace le point a par le point p passé en paramètre
	 * @param p : nouveau point a
	 */	
	public void setA(Point p) 
	{ 
		a = p; 
		updateLongueur();
	}
	
	/**
	 * Setter qui remplace le point b par le point p passé en paramètre
	 * @param p : nouveau point b
	 */		
	public void setB(Point p) 
	{ 
		b = p; 
		updateLongueur();
	}

	/**
	 * Méthode qui permet de tester l'égalité entre le segment courant et un object passé en paramètre
  	 * @param o : object auquel on veut comparer le segment courant
	 * @return : booléen vrai si le segment courant et o sont conceptuellements égaux
	 */		
	public boolean equals(Object o)
	{
		Segment s;
		if(o instanceof Segment)
		{
			s = (Segment)o;
			return a.equals(s.getA()) && b.equals(s.getB());
		} else { return false; }
	}

	/**
	 * Méthode qui mets à jour la longueur du segment courant dans son attribut longueur
	 */	
	private void updateLongueur()
	{
		longueur = a.distance(b);
	}

	/**
	 * Getteur qui renvoie la longueur du segment courant
	 * @return : la longueur du segment courant sous forme d'un double
	 */		
	public double getLongueur() { return longueur; }
	
	/**
	 * Méthode qui permet d'interpoler les segments s1 et s2 en le segment courant, en fonction du coefficient 0 < coeff < 1
	 * @param s1 : Segement 1
  	 * @param s2 : Segement 2
     * @param coeff : double coefficient
	 */		
	public void interpolation(Segment s1, Segment s2, double coeff)
	{
		a.interpolation(s1.getA(), s2.getA(), coeff);
		b.interpolation(s1.getB(), s2.getB(), coeff);
	}

	/**
	 * Methode qui spécifie le cast d'un segment en une chaine de caractères
	 * @return : La chaine de caractères représentant le segment courant
	 */	
	@Override
	public String toString()
	{
		return "a : \n    " + a.toString() + "\n" + "b : \n    " + b.toString();
	}
}