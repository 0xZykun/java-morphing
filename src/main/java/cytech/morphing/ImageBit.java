/*
 * see import javafx.embed.swing.SwingFXUtils;
 * see import javafx.scene.image.*;
 * see import javafx.scene.paint.Color;
 * 
 * see import java.util.Arrays;
 * see import java.util.HashMap;
 * see import java.awt.image.BufferedImage;
 */
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.awt.image.BufferedImage;

/**
 * Classe ImageBit pour gérer les opérations sur les images.
 *
 */
public class ImageBit {

    /**
     * Autre format de l'image.
     */
    private WritableImage img;

    /**
     * Hauteur de l'image.
     */
    private int height;

    /**
     * Largeur de l'image.
     */
    private int width;

    /**
     * Constructeur qui permet d'initialiser l'image.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param chemin chemin de l'image dans le répertoire
     */
    public ImageBit(String chemin) {
        Image image = new Image(chemin);
        this.height = (int) image.getHeight();
        this.width = (int) image.getWidth();
        this.img = new WritableImage((int) width, (int) height);
        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.getPixelWriter().setColor(x, y, pixelReader.getColor(x, y));
            }
        }
    }

    /**
     * Constructeur supplémentaire pour initialiser ImageBit directement avec un WritableImage.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param writableImage image à utiliser pour initialiser
     */
    public ImageBit(WritableImage writableImage) {
        this.width = (int) writableImage.getWidth();
        this.height = (int) writableImage.getHeight();
        this.img = new WritableImage((int) width, (int) height);
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.getPixelWriter().setColor(x, y, pixelReader.getColor(x, y));
            }
        }
    }

    /**
     * Méthode qui permet de récupérer la couleur d'un point de l'image.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param x coordonnée x du point dont on doit récupérer la couleur
     * @param y coordonnée y du point dont on doit récupérer la couleur
     * @return retourne la couleur du point sous forme de tableau de l'intensité des couleurs primaires
     */
    public double[] getRGBA(int x, int y) {
        PixelReader pixelReader = img.getPixelReader();
        Color color = pixelReader.getColor(x, y);
        double r =   color.getRed();
        double g =  color.getGreen();
        double b =  color.getBlue();
        double a =  color.getOpacity();
        return new double[]{r, g, b, a};
    }

    /**
     * Méthode qui permet de modifier la couleur d'un point.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param x coordonnée x du point dont on doit modifier la couleur
     * @param y coordonnée y du point dont on doit modifier la couleur
     * @param r pourcentage de rouge
     * @param g pourcentage de vert
     * @param b pourcentage de bleu
     * @param a opacité
     * @return retourne vrai si la couleur a pu être modifiée, faux sinon
     */
    public boolean setRGB(int x, int y, double r, double g, double b, double a) {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && (r >= 0) && (r < 256) && (g >= 0) && (g < 256) && (b >= 0) && (b < 256)) {
            // Convertir l'opacité à la plage 0.0-1.0
            PixelWriter pixelWriter = img.getPixelWriter();
            pixelWriter.setColor(x, y, new Color(r, g, b, a));
            return true;
        } else {
            return false;
        }
    }


    /**
     * Méthode qui renvoie la hauteur de l'image.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return retourne la hauteur de l'image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Méthode qui renvoie la largeur de l'image.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return retourne la largeur de l'image
     */
    public int getWidth() {
        return width;
    }


    /**
     * Méthode qui crée une instance graphique de l'image et la renvoie.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return retourne une instance graphique de l'image
     */
    public ImageView  genererImage(){
        ImageView imageView = new ImageView(img);
        return imageView;
    }

    /**
     * Méthode qui réalise une copie profonde de l'objet ImageBit.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return une nouvelle instance de ImageBit avec les mêmes données d'image
     */
    public ImageBit copieProfonde() {
        ImageBit copy = new ImageBit(this.img); // Assumons l'existence d'un constructeur prenant un WritableImage
        return copy;
    }

    /**
     * Convertit l'objet ImageBit en BufferedImage.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return l'image convertie au format BufferedImage
     */
    public BufferedImage toBufferedImage() {
        return SwingFXUtils.fromFXImage(this.img, null);
    }

    /**
     * Méthode qui renvoie l'image au format WritableImage.
     * 
     * @author Ruben PETTENG NGONGANG
     * @return l'image au format WritableImage
     */
    public WritableImage getImg() {
        return img;
    }

    /**
     * Méthode qui renvoie la couleur de la forme.
     * 
     * @author Ruben PETTENG NGONGANG
     * @param couleurFond couleur du fond
     * @return couleur de la forme
     */
    public double[] getCouleurForme(double[] couleurFond){
        HashMap<double[],Integer> couleur = new HashMap<>();
        for (int i = 0 ; i<height ; i++){
            for (int j =0 ; j<width;j++){
                double[] couleurPixel = getRGBA(i, j);
                if (!Arrays.equals(couleurPixel,couleurFond) ){
                    if (couleur.containsKey(couleurPixel)){
                        couleur.put(couleurPixel,couleur.get(couleurPixel) +1);
                    }
                    else{
                        couleur.put(couleurPixel,1);
                    }
                }
            }
        }
        double[] couleurForme = null;
        int nbPixel = Integer.MIN_VALUE;
        for ( double[] nc : couleur.keySet()){
            if ( nbPixel < couleur.get(nc)){
                nbPixel = couleur.get(nc);
                couleurForme = nc;
            }
        }
        return couleurForme ;
    }
}