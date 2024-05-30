/**
 * Classe FieldMorphing pour gérer le morphing entre deux ensembles de segments.
 * 
 */
public class FieldMorphing 
{
	/**
     * Segments définis sur l'image source.
     */
    private Segment[] s_src;

    /**
     * Segments définis sur l'image de destination.
     */
    private Segment[] s_dest;

    /**
     * Segments à un état intermédiaire.
     */
    private Segment[] s;

    /**
     * Réel positif qui croît avec la douceur de la déformation produite.
     */
    private double a;

    /**
     * Réel positif qui croît avec l'impact de la distance depuis un pixel vers un segment sur la transformation occasionnée sur ce pixel.
     */
    private double b;

    /**
     * Réel positif qui croît avec l'impact de la longueur des segments sur l'intensité des déformations occasionnées.
     */
    private double p;

    /**
     * Constructeur de FieldMorphing.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param src segments définis sur l'image source
     * @param dest segments définis sur l'image de destination
     * @param a réel positif qui croît avec la douceur de la déformation produite
     * @param b réel positif qui croît avec l'impact de la distance depuis un pixel vers un segment sur la transformation occasionnée sur ce pixel
     * @param p réel positif qui croît avec l'impact de la longueur des segments sur l'intensité des déformations occasionnées
     */
	public FieldMorphing(Segment[] src, Segment[] dest, double a, double b, double p)
	{
		int size = Math.min(src.length, dest.length);
		s_src = new Segment[size];
		s_dest = new Segment[size];
		s = new Segment[size];
		
		for(int i=0;i<size;i++)
		{
			s_src[i] = src[i];
			s_dest[i] = dest[i];
			s[i] = new Segment(new Point(dest[i].getA().getX(),dest[i].getA().getY()),new Point(dest[i].getB().getX(),dest[i].getB().getY()));
		}
		
		this.a = a;
		this.b = b;
		this.p = p;
	}

	/**
     * Constructeur de FieldMorphing avec des valeurs par défaut pour a, b et p.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param src segments définis sur l'image source
     * @param dest segments définis sur l'image de destination
     */
	public FieldMorphing(Segment[] src, Segment[] dest)
	{
		this(src,dest,0.5,1.25,0.5);
	}

	/**
     * Getter pour le paramètre a.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return paramètre a
     */
	public double getA() { return a; }

	/**
     * Setter pour le paramètre a.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param a nouvelle valeur de a
     */
	public void setA(double a) { this.a = a; }

	/**
     * Getter pour le paramètre b.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return paramètre b
     */
	public double getB() { return b; }

	/**
     * Setter pour le paramètre b.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param b nouvelle valeur de b
     */
	public void setB(double b) { this.b = b; }
	
	/**
     * Getter pour le paramètre p.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return paramètre p
     */
	public double getP() { return p; }

	/**
     * Setter pour le paramètre p.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param p nouvelle valeur de p
     */
	public void setP(double p) { this.p = p; }

	/**
     * Getter pour le ième segment de s_src, le tableau des segments de l'image source.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param i index dans s_src du segment qui sera retourné
     * @return ième segment de s_src
     */
	public Segment getSrcAt(int i)
	{
		return s_src[i];
	}

	/**
     * Getter pour le ième segment de s_dest, le tableau des segments de l'image de destination.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param i index dans s_dest du segment qui sera retourné
     * @return ième segment de s_dest
     */
	public Segment getDestAt(int i)
	{
		return s_dest[i];
	}
	
	/**
     * Calcule un scalaire U qui intervient dans le calcul de déformation de l'image par rapport aux segments.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param p point sur l'image de destination dont on veut déterminer l'associé sur l'image source
     * @param i index dans s_dest du segment concerné
     * @return valeur du scalaire U
     */	
	private double getU(Point p,int i)
	{
		Point ab = Point.soustraction(s[i].getB(),s[i].getA());
		return Point.scalaire(Point.soustraction(p,s[i].getA()),ab)/Point.scalaire(ab, ab);
	}

	/**
     * Calcule un scalaire V qui intervient dans le calcul de déformation de l'image par rapport aux segments.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param p point sur l'image de destination dont on veut déterminer l'associé sur l'image source
     * @param i index dans s_dest du segment concerné
     * @return valeur du scalaire V
     */
	private double getV(Point p, int i)
	{
		Point ab = Point.soustraction(s[i].getB(),s[i].getA());
		return Point.scalaire(Point.soustraction(p,s[i].getA()),Point.orthogonal(s[i].getA(), s[i].getB()))/ab.norme2();
	}

	/**
     * Détermine le point sur l'image source associé au point p sur l'image intermédiaire à l'étape coeff de la transformation.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param p point sur l'image intermédiaire dont on veut déterminer l'associé sur l'image source
     * @param coeff étape de la transformation
     * @return point associé à p sur l'image source
     */
	public Point getSrcPos(Point p, double coeff)
	{
		Point result = new Point(0,0);
		Point[] positions = new Point[s.length];
		double weight;
		double weightSum = 0;
		
		//calcul des segments à l'instant coeff
		for(int i=0;i<s.length;i++)
		{
			s[i].interpolation(s_src[i], s_dest[i], coeff);
		}	
		//calcul des positions sources Xi associées à chaque segment
		for(int i=0;i<s.length;i++)
		{
			positions[i] = Point.addition(s_src[i].getA(),Point.scale(Point.soustraction(s_src[i].getB(),s_src[i].getA()),getU(p,i)));
			positions[i] = Point.addition(positions[i],Point.scale(Point.orthogonal(s_src[i].getA(), s_src[i].getB()), getV(p,i)/Point.soustraction(s_src[i].getB(), s_src[i].getA()).norme2()));
			//calcul du poids
			weight = Math.pow(Math.pow(s[i].getLongueur(),this.p)/(a+p.distanceSegment(s[i])),b);
			//point de l'image intermédiaire + moyenne pondérée par le poids, des déplacements associés sur l'image source pour chaque segment
			result = Point.addition(result, Point.scale(Point.soustraction(positions[i], p),weight));
			weightSum += weight;
		}
		
		result = Point.scale(result, (double)1/weightSum);
		result = Point.addition(result, p);
		return result;
	}
	
	/**
     * Détermine le point sur l'image de destination associé au point p sur l'image intermédiaire à l'étape coeff de la transformation.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param p point sur l'image intermédiaire dont on veut déterminer l'associé sur l'image de destination
     * @param coeff étape de la transformation
     * @return point associé à p sur l'image de destination
     */	
	public Point getDestPos(Point p, double coeff)
	{
		Point result = new Point(0,0);
		Point[] positions = new Point[s.length];
		double weight;
		double weightSum = 0;
		
		//calcul des segments à l'instant coeff
		for(int i=0;i<s.length;i++)
		{
			s[i].interpolation(s_src[i], s_dest[i], coeff);
		}
		//calcul des positions sources Xi associées à chaque segment
		for(int i=0;i<s.length;i++)
		{
			positions[i] = Point.addition(s_dest[i].getA(),Point.scale(Point.soustraction(s_dest[i].getB(),s_dest[i].getA()),getU(p,i)));
			positions[i] = Point.addition(positions[i],Point.scale(Point.orthogonal(s_dest[i].getA(), s_dest[i].getB()), getV(p,i)/Point.soustraction(s_dest[i].getB(), s_dest[i].getA()).norme2()));
			//calcul du poids
			weight = Math.pow(Math.pow(s[i].getLongueur(),this.p)/(a+p.distanceSegment(s[i])),b);
			weightSum += weight;
			//point de l'image intermédiaire + moyenne pondérée par le poids, des déplacements associés sur l'image source pour chaque segment
			result = Point.addition(result, Point.scale(Point.soustraction(positions[i], p),weight));
		}
		
		result = Point.scale(result, (double)1/weightSum);
		result = Point.addition(result, p);
		return result;
	}

	/**
     * Modifie l'ImageBit inter pour représenter l'étape coeff du morphing de src vers dest selon les paramètres de l'objet FieldMorphing présent.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param src ImageBit de l'image initiale
     * @param dest ImageBit de l'image finale
     * @param inter ImageBit de l'image intermédiaire
     * @param coeff étape de la transformation
     */
	public void morph(ImageBit src, ImageBit dest, ImageBit inter, double coeff)
	{
		Point result;
		Point tmp = new Point(0,0);
		int resultX;
		int resultY;
		double[] rgbSrc;
		double[] rgbDest;
		//itération sur les pixels de l'image intermédiaire
		for(int x=0;x<src.getWidth();x++)
        	{
            		for(int y=0;y<src.getHeight();y++)
            		{
            			//mise à jour du point courant
		                tmp.setX(x);
		                tmp.setY(y);
		                //déformation de l'image source
		                result = getSrcPos(tmp, coeff);
		                //vérification de la validité des coordonnées obtenues
		                resultX = (int)Math.min(src.getWidth()-1,Math.max(0,result.getX()));
		                resultY = (int)Math.min(src.getHeight()-1,Math.max(0,result.getY()));
		                //récupération des valeurs rgba
		                rgbSrc = src.getRGBA(resultX,resultY);
		                
		                //déformation de l'image de destination
		                result = getDestPos(tmp, coeff);
		                //vérification de la validité des coordonnées obtenues
		                resultX = (int)Math.min(src.getWidth()-1,Math.max(0,result.getX()));
		                resultY = (int)Math.min(src.getHeight()-1,Math.max(0,result.getY()));
		                //récupération des valeurs rgba
		                rgbDest = dest.getRGBA(resultX,resultY);
		                
		                //fondu enchainé linéraire
		                inter.setRGB(x, y, rgbSrc[0]*(1-coeff) + rgbDest[0]*coeff, rgbSrc[1]*(1-coeff) + rgbDest[1]*coeff, rgbSrc[2]*(1-coeff) + rgbDest[2]*coeff, rgbSrc[3]*(1-coeff) + rgbDest[3]*coeff);
            		}
       		}		
	}
}