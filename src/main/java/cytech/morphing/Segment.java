/**
 * Classe Segment pour représenter et manipuler les segments dans un espace 2D.
 * 
 */
public class Segment 
{
	/**
	 * Point initial a du segment.
	 */
	private Point a;

	/**
	 * Point final b du segment.
	 */
	private Point b;

	/**
	 * Constructeur qui permet d'initialiser le segment avec deux points.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @param a point initial a
	 * @param b point final b
	 */
	public Segment(Point a, Point b)
	{
		this.a = a;
		this.b = b;
	}

	/**
	 * Getter qui retourne le point a.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @return point a
	 */
	public Point getA() { return a; }

	/**
	 * Getter qui retourne le point b.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @return point b
	 */
	public Point getB() { return b; }
	
	/**
	 * Setter qui remplace le point a par le point p passé en paramètre.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @param p nouveau point a
	 */
	public void setA(Point p) 
	{ 
		a = p; 
	}
	
	/**
	 * Setter qui remplace le point b par le point p passé en paramètre.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @param p nouveau point b
	 */	
	public void setB(Point p) 
	{ 
		b = p; 
	}

	/**
	 * Méthode qui permet de tester l'égalité entre le segment courant et un objet passé en paramètre.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @param o objet auquel on veut comparer le segment courant
	 * @return booléen vrai si le segment courant et o sont conceptuellement égaux
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
	 * Getter qui renvoie la longueur du segment courant.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @return la longueur du segment courant sous forme d'un double
	 */	
	public double getLongueur() { return a.distance(b); }
	
	/**
	 * Méthode qui permet d'interpoler les segments s1 et s2 en le segment courant, en fonction du coefficient 0 < coeff < 1.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @param s1 segment 1
	 * @param s2 segment 2
	 * @param coeff coefficient d'interpolation
	 */	
	public void interpolation(Segment s1, Segment s2, double coeff)
	{
        a.interpolation(s1.getA(), s2.getA(), coeff);
        b.interpolation(s1.getB(), s2.getB(), coeff);
	}

	/**
	 * Méthode qui spécifie le cast d'un segment en une chaîne de caractères.
	 * 
	 * @author Ruben PETTENG NGONGANG
	 * @return la chaîne de caractères représentant le segment courant
	 */
	@Override
	public String toString()
	{
		return "new Segment(" + this.getA() + "), " + this.getB() + " ),";
	}
}