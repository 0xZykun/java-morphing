package cytech.morphing;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preset {
    private MorphingFx app;

    public Preset(MorphingFx app) {
        this.app = app;
    }

    public void appliquerPreset1() {
        app.getImageLoader().effacerCanevas("Gauche");
        app.getImageLoader().effacerCanevas("Droite");
        app.setChoixMethode(1);
        app.getControle().miseJourControle(app.getChoixMethode());

        chargerEtRedimensionnerImage(app.getImageLoader().getCanevasGauche(), "square.png", 0);
        chargerEtRedimensionnerImage(app.getImageLoader().getCanevasDroite(), "cross.png", 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();

        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(100 * ratioLargeur, 100 * ratioHauteur),
            new Point(100 * ratioLargeur, 100 * ratioHauteur),
            new Point(100 * ratioLargeur, 100 * ratioHauteur),
            new Point(400 * ratioLargeur, 100 * ratioHauteur),
            new Point(400 * ratioLargeur, 100 * ratioHauteur),
            new Point(400 * ratioLargeur, 100 * ratioHauteur),
            new Point(400 * ratioLargeur, 400 * ratioHauteur),
            new Point(400 * ratioLargeur, 400 * ratioHauteur),
            new Point(400 * ratioLargeur, 400 * ratioHauteur),
            new Point(100 * ratioLargeur, 400 * ratioHauteur),
            new Point(100 * ratioLargeur, 400 * ratioHauteur),
            new Point(100 * ratioLargeur, 400 * ratioHauteur)
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(100 * ratioLargeur, 225 * ratioHauteur),
            new Point(225 * ratioLargeur, 225 * ratioHauteur),
            new Point(225 * ratioLargeur, 100 * ratioHauteur),
            new Point(275 * ratioLargeur, 100 * ratioHauteur),
            new Point(275 * ratioLargeur, 225 * ratioHauteur),
            new Point(400 * ratioLargeur, 225 * ratioHauteur),
            new Point(400 * ratioLargeur, 275 * ratioHauteur),
            new Point(275 * ratioLargeur, 275 * ratioHauteur),
            new Point(275 * ratioLargeur, 400 * ratioHauteur),
            new Point(225 * ratioLargeur, 400 * ratioHauteur),
            new Point(225 * ratioLargeur, 275 * ratioHauteur),
            new Point(100 * ratioLargeur, 275 * ratioHauteur)
        ));

        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        //app.getImageLoader().setPresetBoolean(true);
        app.getImageLoader().actualiserEtatBoutons();
    }

    public void appliquerPreset3() {
        app.getImageLoader().effacerCanevas("Gauche");
        app.getImageLoader().effacerCanevas("Droite");
        app.setChoixMethode(3);
        app.getControle().miseJourControle(app.getChoixMethode());

        chargerEtRedimensionnerImage(app.getImageLoader().getCanevasGauche(), "boy3.jpg", 0);
        chargerEtRedimensionnerImage(app.getImageLoader().getCanevasDroite(), "boy5.jpg", 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();

        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(50 * ratioLargeur, 50 * ratioHauteur),
            new Point(150 * ratioLargeur, 150 * ratioHauteur),
            new Point(250 * ratioLargeur, 50 * ratioHauteur)
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(75 * ratioLargeur, 50 * ratioHauteur),
            new Point(175 * ratioLargeur, 150 * ratioHauteur),
            new Point(275 * ratioLargeur, 50 * ratioHauteur)
        ));

        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);

        app.getTriangleControle().generateDelaunayTriangles();
        app.getTriangleControle().mettreAJourTrianglesEtDessiner();

        //app.getImageLoader().setPresetBoolean(true);
        app.getImageLoader().actualiserEtatBoutons();
    }

    private void chargerEtRedimensionnerImage(Canvas canevas, String imagePath, int index) {
        Image imageTemporaire = new Image(imagePath, false);
        ImageBit nouvelleImageBit = new ImageBit(imagePath);
        double ratio = imageTemporaire.getWidth() / imageTemporaire.getHeight();
        double largeurMaxImage = app.getLargeurScene() / 2.2;
        double hauteurMaxImage = app.getHauteurScene() / 2.2;
        double nouvelleLargeur, nouvelleHauteur;
    
        if (ratio >= 1) {
            nouvelleLargeur = largeurMaxImage;
            nouvelleHauteur = nouvelleLargeur / ratio;
            if (nouvelleHauteur > hauteurMaxImage) {
                nouvelleHauteur = hauteurMaxImage;
                nouvelleLargeur = nouvelleHauteur * ratio;
            }
        } else {
            nouvelleHauteur = hauteurMaxImage;
            nouvelleLargeur = nouvelleHauteur * ratio;
            if (nouvelleLargeur > largeurMaxImage) {
                nouvelleLargeur = largeurMaxImage;
                nouvelleHauteur = nouvelleLargeur / ratio;
            }
        }
    
        if ((app.getLargeurImageOriginale() == 0 && app.getHauteurImageOriginale() == 0) ||
                (imageTemporaire.getWidth() == app.getLargeurImageOriginale() && imageTemporaire.getHeight() == app.getHauteurImageOriginale())) {
            if (app.getLargeurImageOriginale() == 0 && app.getHauteurImageOriginale() == 0) {
                app.setLargeurImageOriginale((int) imageTemporaire.getWidth());
                app.setHauteurImageOriginale((int) imageTemporaire.getHeight());
            }
    
            canevas.setWidth(nouvelleLargeur);
            canevas.setHeight(nouvelleHauteur);
    
            Image image = new Image(imagePath, nouvelleLargeur, nouvelleHauteur, true, true);
            GraphicsContext gc = canevas.getGraphicsContext2D();
            gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());
            gc.drawImage(image, 0, 0);
    
            if (index == 0) {
                app.getImageLoader().setImageGauche(image);
                app.getImageLoader().setImageChargeeGauche(true);
                if (!app.getImagesOrigines().isEmpty()) {
                    app.getImagesOrigines().remove(0);
                }
                app.getImagesOrigines().add(0, nouvelleImageBit);
            } else {
                app.getImageLoader().setImageDroite(image);
                app.getImageLoader().setImageChargeeDroite(true);
                if (app.getImagesOrigines().size() > 1) {
                    app.getImagesOrigines().set(1, nouvelleImageBit);
                } else {
                    app.getImagesOrigines().add(nouvelleImageBit);
                }
            }
    
            BorderPane panneauBord = (BorderPane) canevas.getParent();
            panneauBord.setPrefSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
            panneauBord.setMaxSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
    
            Platform.runLater(panneauBord::layout);
    
            app.getTriangleControle().redessinerCanevas(canevas, 
                (index == 0) ? app.getPointControle().getPointsGauche() : app.getPointControle().getPointsDroite(), 
                image);
    
            app.getImageLoader().actualiserEtatBoutons();
        }
    }       
}