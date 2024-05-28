package cytech.morphing;
																											/* ****************
                                                                                                             *    IMAGEBIT    *
                                                                                                             **************** */


// Importation des bibliotheques necessaires
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class ImageBit {

    private WritableImage img; // Autre format de l'image
    private int height; // hauteur de l'image
    private int width;  // largeur de l'image


    /**
     * Constructeur qui permet d'initialiser l'image
     * @param chemin : chemin de l'image dans le repertoire
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
     * Constructeur supplémentaire pour initialiser ImageBit directement avec un WritableImage
     * @param writableImage : image à utiliser pour initialiser
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
     * Methode qui permet de recuperer la couleur d'un point de l'image
     * @param x : Cordonnee x du point dont on doit recuperer la couleur
     * @param y : Coordonnee y du point dont on doit recuperer la couleur
     * @return : Retourne la couleur du point sous forme de tableau de l'intensite des couleurs primaires
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
     * Methode qui permet de modifier la couleur d'un point
     * @param x : Coordonne x du point dont on doit modifier la couleur
     * @param y : Coordonne y du point dont on doit modifier la couleur
     * @param r : Pourcentage de rouge
     * @param g : Pourcentage de vert
     * @param b : Pourcentage de bleu
     * @param a : Opacite
     * @return  : retourne vrai si la couleur a pu etre modifie, faux sinon
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
     * Methode qui renvoie la hauteur de l'image
     * @return : Retourne la hauteur de l'image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Methode qui renvoie la largeur de l'image
     * @return : Retourne la largeur de l'image
     */
    public int getWidth() {
        return width;
    }


    /**
     * Methode qui cree une instance graphique de l'image et la renvoie
     * @return : Retourne une instance graphique de l'image
     */
    public ImageView  genererImage(){
        ImageView imageView = new ImageView(img);
        return imageView;
    }

    /**
     * Méthode qui réalise une copie profonde de l'objet ImageBit.
     * @return ImageBit : une nouvelle instance de ImageBit avec les mêmes données d'image.
     */
    public ImageBit deepCopy() {
        ImageBit copy = new ImageBit(this.img); // Assumons l'existence d'un constructeur prenant un WritableImage
        return copy;
    }

    /**
     * Convertit l'objet ImageBit en BufferedImage.
     * @return BufferedImage : l'image convertie au format BufferedImage.
     */
    public BufferedImage toBufferedImage() {
        return SwingFXUtils.fromFXImage(this.img, null);
    }

    public WritableImage getImg() {
        return img;
    }

    /**
     * Methode qui renvoie la couleur de la forme 
     * @param  couleur du fond
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