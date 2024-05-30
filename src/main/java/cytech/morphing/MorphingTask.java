package cytech.morphing;

/*
 * see import javafx.animation.KeyFrame;
 * see import javafx.animation.Timeline;
 * see import javafx.application.Platform;
 * see import javafx.concurrent.Task;
 * see import javafx.embed.swing.SwingFXUtils;
 * see import javafx.geometry.Insets;
 * see import javafx.geometry.Pos;
 * see import javafx.scene.Scene;
 * see import javafx.scene.control.Menu;
 * see import javafx.scene.control.MenuBar;
 * see import javafx.scene.control.MenuItem;
 * see import javafx.scene.control.ProgressBar;
 * see import javafx.scene.image.Image;
 * see import javafx.scene.image.ImageView;
 * see import javafx.scene.image.PixelReader;
 * see import javafx.scene.image.PixelWriter;
 * see import javafx.scene.image.WritableImage;
 * see import javafx.scene.layout.StackPane;
 * see import javafx.scene.layout.VBox;
 * see import javafx.scene.paint.Color;
 * see import javafx.stage.FileChooser;
 * see import javafx.stage.Screen;
 * see import javafx.stage.Stage;
 * see import javafx.util.Duration;
 *
 * see import java.awt.image.BufferedImage;
 * see import java.io.File;
 * see import java.util.ArrayList;
 * see import java.util.HashMap;
 * see import java.util.LinkedList;
 * see import java.util.List;
 * see import java.util.Map;
 */
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe MorphingTask pour gérer les différents morphing.
 * 
 */
public class MorphingTask {
    /**
     * Référence à l'application principale.
     */
    private MorphingFx app;

    /**
     * Constructeur de la classe MorphingTask.
     * 
     * @param app Instance de l'application principale
     */
    public MorphingTask(MorphingFx app) {
        this.app = app;
    }

    /**
     * Effectue le morphing de l'image en utilisant la méthode 1.
     * 
     * @author Anthony GARCIA
     * @param pointsGauche Points de contrôle de l'image gauche
     * @param pointsDroite Points de contrôle de l'image droite
     * @param imageGauche Image de départ
     * @param imageDroite Image de destination
     */
    public void morphing1(List<Point> pointsGauche, List<Point> pointsDroite, ImageBit imageGauche, ImageBit imageDroite) {
        app.getImagesIntermediaires().clear();
        int nombreEtapes = app.getNombreImagesIntermediaires();
        double dureeEtape = app.getDureeDuGIF() * 1000 / nombreEtapes;

        ImageView vueImage = new ImageView();
        vueImage.setPreserveRatio(true);

        ProgressBar barreProgression = creerBarreProgression();
        Stage barreProgressionStage = creerStageProgression(barreProgression);

        Task<Void> tacheMorphing = new Task<>() {
            @Override
            public Void call() throws Exception {
                PixelReader lecteurGauche = imageGauche.getImg().getPixelReader();
                PixelReader lecteurDroite = imageDroite.getImg().getPixelReader();

                Color couleurFondGauche = getCouleurFond(lecteurGauche, imageGauche.getImg(), pointsGauche);
                Color couleurFormeGauche = getCouleurPlusFrequenteDansForme(lecteurGauche, imageGauche.getImg(), pointsGauche, couleurFondGauche);
                Color couleurFondDroite = getCouleurFond(lecteurDroite, imageDroite.getImg(), pointsDroite);
                Color couleurFormeDroite = getCouleurPlusFrequenteDansForme(lecteurDroite, imageDroite.getImg(), pointsDroite, couleurFondDroite);

                for (int etape = 0; etape <= nombreEtapes; etape++) {
                    double coeff = (double) etape / nombreEtapes;

                    List<Point> pointsIntermediaires = interpolerPoints(pointsGauche, pointsDroite, coeff);

                    int largeur = imageGauche.getWidth();
                    int hauteur = imageGauche.getHeight();

                    WritableImage imageIntermediaire = new WritableImage(largeur, hauteur);
                    PixelWriter ecrivainPixels = imageIntermediaire.getPixelWriter();

                    Color couleurFondIntermediaire = couleurFondGauche.interpolate(couleurFondDroite, coeff);
                    Color couleurFormeIntermediaire = couleurFormeGauche.interpolate(couleurFormeDroite, coeff);

                    remplirFond(ecrivainPixels, largeur, hauteur, couleurFondIntermediaire);
                    remplirForme(ecrivainPixels, pointsIntermediaires, couleurFormeIntermediaire);

                    Platform.runLater(() -> {
                        app.getImagesIntermediaires().add(new ImageBit(imageIntermediaire));
                    });

                    updateProgress(etape + 1, nombreEtapes);
                }
                app.getImageViewer().actualiserVisionneuseImage();
                return null;
            }

            @Override
            public void succeeded() {
                barreProgressionStage.close();
                afficherApercuAnimation(vueImage, nombreEtapes, dureeEtape);
            }
        };

        barreProgression.progressProperty().bind(tacheMorphing.progressProperty());
        new Thread(tacheMorphing).start();
    }

    /**
     * Effectue le morphing de l'image en utilisant la méthode 2.
     * 
     * @author Thomas BEAUSSART
     * @param pointsGauche Points de contrôle de l'image gauche
     * @param pointsDroite Points de contrôle de l'image droite
     * @param imageGauche Image de départ
     * @param imageDroite Image de destination
     */
    public void morphing2(List<Point> pointsGauche, List<Point> pointsDroite, ImageBit imageGauche, ImageBit imageDroite) {
        app.getImagesIntermediaires().clear();
        int nombreEtapes = app.getNombreImagesIntermediaires();
        double dureeEtape = app.getDureeDuGIF() * 1000 / nombreEtapes;
    
        ImageView vueImage = new ImageView();
        vueImage.setPreserveRatio(true);
    
        ProgressBar barreProgression = creerBarreProgression();
        Stage barreProgressionStage = creerStageProgression(barreProgression);
    
        Task<Void> tacheMorphing = new Task<>() {
            @Override
            public Void call() throws Exception {
    
                PixelReader lecteurGauche = imageGauche.getImg().getPixelReader();
                PixelReader lecteurDroite = imageDroite.getImg().getPixelReader();
    
                Color couleurFondGauche = getCouleurFond(lecteurGauche, imageGauche.getImg(), pointsGauche);
                Color couleurFormeGauche = getCouleurPlusFrequenteDansForme(lecteurGauche, imageGauche.getImg(), pointsGauche, couleurFondGauche);
                Color couleurFondDroite = getCouleurFond(lecteurDroite, imageDroite.getImg(), pointsDroite);
                Color couleurFormeDroite = getCouleurPlusFrequenteDansForme(lecteurDroite, imageDroite.getImg(), pointsDroite, couleurFondDroite);
    
                for (int etape = 0; etape <= nombreEtapes; etape++) {
                    double coeff = (double) etape / nombreEtapes;
    
                    List<Point> pointsIntermediaires = interpolerPoints(pointsGauche, pointsDroite, coeff);
    
                    Color couleurFondIntermediaire = couleurFondGauche.interpolate(couleurFondDroite, coeff);
                    Color couleurFormeIntermediaire = couleurFormeGauche.interpolate(couleurFormeDroite, coeff);

                    Color[][] tableauPixels = remplirForme(app.getLargeurImageOriginale(), app.getHauteurImageOriginale(), couleurFormeIntermediaire, couleurFondIntermediaire, pointsIntermediaires);

                    Image image = creerImageDepuisTableau(tableauPixels);
                    Platform.runLater(() -> {
                        app.getImagesIntermediaires().add(new ImageBit((WritableImage) image));
                    });

                    updateProgress(etape + 1, nombreEtapes);
                }
                app.getImageViewer().actualiserVisionneuseImage();
                return null;
            }
    
            @Override
            public void succeeded() {
                barreProgressionStage.close();
                afficherApercuAnimation(vueImage, nombreEtapes, dureeEtape);
            }
        };
    
        barreProgression.progressProperty().bind(tacheMorphing.progressProperty());
        new Thread(tacheMorphing).start();
    }
    
    /**
     * Remplit la forme sur l'image intermédiaire avec la couleur appropriée.
     * 
     * @autor Thomas BEAUSSART
     * @param largeur Largeur de l'image
     * @param hauteur Hauteur de l'image
     * @param couleurForme Couleur de la forme
     * @param couleurFond Couleur du fond
     * @param points Points définissant la forme
     * @return Tableau de couleurs représentant l'image
     */
    private Color[][] remplirForme(int largeur, int hauteur, Color couleurForme, Color couleurFond, List<Point> points) {
        points.add(points.get(0));

        List<Point> pointsFinales = CourbeBezier.courbeBezier(points);

        Color[][] tabPixel = new Color[largeur][hauteur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                Point p = new Point(x, y);
                if (p.appartientForme(pointsFinales)) {
                    tabPixel[x][y] = couleurForme;
                } else {
                    tabPixel[x][y] = couleurFond;
                }
            }
        }
        return tabPixel;
    }

    /**
     * Effectue le morphing de l'image en utilisant la méthode 3 (triangles de Delaunay).
     * 
     * @author Marc DJOLE
     * @param trianglesGauche Triangles de l'image gauche
     * @param trianglesDroite Triangles de l'image droite
     */
    public void morphing3(List<Triangle> trianglesGauche, List<Triangle> trianglesDroite) {
        app.getImagesIntermediaires().clear();
        int nombreEtapes = app.getNombreImagesIntermediaires();
        double dureeEtape = app.getDureeDuGIF() * 1000 / nombreEtapes;

        ImageView vueImage = new ImageView();
        vueImage.setPreserveRatio(true);

        ProgressBar barreProgression = creerBarreProgression();
        Stage barreProgressionStage = creerStageProgression(barreProgression);

        Task<Void> tacheMorphing = new Task<>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < nombreEtapes; i++) {
                    double coefficient = i / (double) (nombreEtapes - 1);

                    List<Triangle> trianglesIntermediaires = interpolerTriangles(trianglesGauche, trianglesDroite, coefficient);

                    Color[][] tableauPixels = creerTableauPixelsInitial(app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    for (int k = 0; k < app.getLargeurImageOriginale(); k++) {
                        for (int l = 0; l < app.getHauteurImageOriginale(); l++) {
                            Point pointCourant = new Point((double) k, (double) l);

                            for (int m = 0; m < trianglesIntermediaires.size(); m++) {
                                Triangle tInterCourant = trianglesIntermediaires.get(m);

                                if (tInterCourant.pointAppartient(pointCourant)) {
                                    Double[] coordBarycentriques = tInterCourant.coordoneesBarycentriques(pointCourant);
                                    Triangle tDepart = trianglesGauche.get(m);
                                    Triangle tFin = trianglesDroite.get(m);

                                    double xDepart = tDepart.getA().getX() * coordBarycentriques[0] + tDepart.getB().getX() * coordBarycentriques[1] + tDepart.getC().getX() * coordBarycentriques[2];
                                    double yDepart = tDepart.getA().getY() * coordBarycentriques[0] + tDepart.getB().getY() * coordBarycentriques[1] + tDepart.getC().getY() * coordBarycentriques[2];

                                    double xFin = tFin.getA().getX() * coordBarycentriques[0] + tFin.getB().getX() * coordBarycentriques[1] + tFin.getC().getX() * coordBarycentriques[2];
                                    double yFin = tFin.getA().getY() * coordBarycentriques[0] + tFin.getB().getY() * coordBarycentriques[1] + tFin.getC().getY() * coordBarycentriques[2];

                                    if (xDepart < app.getLargeurImageOriginale() && yDepart < app.getHauteurImageOriginale() && xFin < app.getLargeurImageOriginale() && yFin < app.getHauteurImageOriginale()) {
                                        double[] pixelsDebut = app.getImagesOrigines().get(0).getRGBA((int) xDepart, (int) yDepart);
                                        double[] pixelsFin = app.getImagesOrigines().get(1).getRGBA((int) xFin, (int) yFin);

                                        double nouveauRouge = (pixelsDebut[0] * (1 - coefficient) + coefficient * pixelsFin[0]);
                                        double nouveauVert = (pixelsDebut[1] * (1 - coefficient) + coefficient * pixelsFin[1]);
                                        double nouveauBleu = (pixelsDebut[2] * (1 - coefficient) + coefficient * pixelsFin[2]);
                                        double nouvelleAlpha = (pixelsDebut[3] * (1 - coefficient) + coefficient * pixelsFin[3]);

                                        tableauPixels[k][l] = new Color(nouveauRouge, nouveauVert, nouveauBleu, nouvelleAlpha);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Image image = creerImageDepuisTableau(tableauPixels);
                    Platform.runLater(() -> {
                        app.getImagesIntermediaires().add(new ImageBit((WritableImage) image));
                    });

                    updateProgress(i + 1, nombreEtapes);
                }
                app.getImageViewer().actualiserVisionneuseImage();
                return null;
            }

            @Override
            public void succeeded() {
                barreProgressionStage.close();
                afficherApercuAnimation(vueImage, nombreEtapes, dureeEtape);
            }
        };

        barreProgression.progressProperty().bind(tacheMorphing.progressProperty());
        new Thread(tacheMorphing).start();
    }

    /**
     * Effectue le morphing de l'image en utilisant la méthode 4 (segments).
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param segementsGauche Segments de l'image gauche
     * @param segmentsDroite Segments de l'image droite
     * @param imageGauche Image de départ
     * @param imageDroite Image de destination
     */
    public void morphing4(List<Segment> segmentsGauche, List<Segment> segmentsDroite, ImageBit imageGauche, ImageBit imageDroite) {
        app.getImagesIntermediaires().clear();
        int nombreEtapes = app.getNombreImagesIntermediaires();
        double coeff = (double) 1 / nombreEtapes;
        double dureeEtape = app.getDureeDuGIF() * 1000 / nombreEtapes;
    
        ImageView vueImage = new ImageView();
        vueImage.setPreserveRatio(true);
    
        ProgressBar barreProgression = creerBarreProgression();
        Stage barreProgressionStage = creerStageProgression(barreProgression);
    
        Task<Void> tacheMorphing = new Task<>() {
            @Override
            public Void call() throws Exception {
                Point topLeft = new Point(0, 0);
                Point topRight = new Point(imageGauche.getWidth() - 1, 0);
                Point bottomLeft = new Point(0, imageGauche.getHeight() - 1);
                Point bottomRight = new Point(imageGauche.getWidth() - 1, imageGauche.getHeight() - 1);
                
                List<Segment> border = new ArrayList<Segment>();
                border.add(new Segment(topLeft,topRight));
                border.add(new Segment(topLeft,bottomLeft));
                border.add(new Segment(bottomLeft,bottomRight));
                border.add(new Segment(bottomRight,topRight));
                segmentsGauche.addAll(border);
                segmentsDroite.addAll(border);
                
                FieldMorphing fieldMorphing = new FieldMorphing(convertirListeEnTableau(segmentsGauche), convertirListeEnTableau(segmentsDroite));
    
                for (int etape = 0; etape <= nombreEtapes+1; etape++) {
                    
                    ImageBit imageIntermediaire = new ImageBit(imageGauche.getImg());
                    if(etape<=nombreEtapes)
                    {
                        fieldMorphing.morph(imageGauche, imageDroite, imageIntermediaire, etape * coeff);
                    } else { fieldMorphing.morph(imageGauche, imageDroite, imageIntermediaire, 1); }
    
                    Platform.runLater(() -> {
                        app.getImagesIntermediaires().add(imageIntermediaire);
                    });

                    updateProgress(etape + 1, nombreEtapes);
                }
                
                app.getImageViewer().actualiserVisionneuseImage();
                return null;
            }
    
            @Override
            public void succeeded() {
                barreProgressionStage.close();
                afficherApercuAnimation(vueImage, nombreEtapes, dureeEtape);
            }
        };
    
        barreProgression.progressProperty().bind(tacheMorphing.progressProperty());
        new Thread(tacheMorphing).start();
    }

    /**
     * Convertit une liste de segments en tableau.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param segmentsList Liste de segments
     * @return Tableau de segments
     */
    public Segment[] convertirListeEnTableau(List<Segment> segmentsList) {
        return segmentsList.toArray(new Segment[0]);
    }    

    /**
     * Crée une barre de progression.
     * 
     * @author Anthony GARCIA
     * @return Barre de progression
     */
    private ProgressBar creerBarreProgression() {
        ProgressBar barreProgression = new ProgressBar(0);
        barreProgression.setMaxWidth(Double.MAX_VALUE);
        return barreProgression;
    }

    /**
     * Crée une fenêtre de progression pour la barre de progression.
     * 
     * @author Anthony GARCIA
     * @param barreProgression Barre de progression
     * @return Stage de progression
     */
    private Stage creerStageProgression(ProgressBar barreProgression) {
        Stage barreProgressionStage = new Stage();
        barreProgressionStage.setTitle("Progression du morphing");

        VBox vboxProgression = new VBox(barreProgression);
        vboxProgression.setSpacing(10);
        vboxProgression.setPadding(new Insets(10));
        vboxProgression.setAlignment(Pos.CENTER);

        Scene sceneProgression = new Scene(vboxProgression, 500, 100);
        barreProgressionStage.setScene(sceneProgression);
        barreProgressionStage.show();

        return barreProgressionStage;
    }
    
    /**
     * Interpole les points de contrôle entre deux listes de points.
     * 
     * @author Marc DJOLE
     * @param pointsGauche Points de l'image gauche
     * @param pointsDroite Points de l'image droite
     * @param coeff Coefficient d'interpolation
     * @return Liste de points interpolés
     */
    private List<Point> interpolerPoints(List<Point> pointsGauche, List<Point> pointsDroite, double coeff) {
        List<Point> pointsIntermediaires = new ArrayList<>();
        for (int i = 0; i < pointsGauche.size(); i++) {
            Point pointIntermediaire = new Point(
                pointsGauche.get(i).getX() * (1 - coeff) + pointsDroite.get(i).getX() * coeff,
                pointsGauche.get(i).getY() * (1 - coeff) + pointsDroite.get(i).getY() * coeff
            );
            pointsIntermediaires.add(pointIntermediaire);
        }
        return pointsIntermediaires;
    }

    /**
     * Remplit le fond de l'image avec une couleur.
     * 
     * @author Mattéo REYNE
     * @param ecrivainPixels Écrivain de pixels
     * @param largeur Largeur de l'image
     * @param hauteur Hauteur de l'image
     * @param couleurFondIntermediaire Couleur du fond
     */
    private void remplirFond(PixelWriter ecrivainPixels, int largeur, int hauteur, Color couleurFondIntermediaire) {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                ecrivainPixels.setColor(x, y, couleurFondIntermediaire);
            }
        }
    }

    /**
     * Interpole les triangles entre deux listes de triangles.
     * 
     * @author Marc DJOLE
     * @param trianglesGauche Triangles de l'image gauche
     * @param trianglesDroite Triangles de l'image droite
     * @param coefficient Coefficient d'interpolation
     * @return Liste de triangles interpolés
     */
    private List<Triangle> interpolerTriangles(List<Triangle> trianglesGauche, List<Triangle> trianglesDroite, double coefficient) {
        List<Triangle> trianglesIntermediaires = new ArrayList<>();
        for (int j = 0; j < trianglesGauche.size(); j++) {
            Triangle tGauche = trianglesGauche.get(j);
            Triangle tDroite = trianglesDroite.get(j);
            Triangle tIntermediaire = tGauche.morphToTriangle(tDroite, coefficient);
            trianglesIntermediaires.add(tIntermediaire);
        }
        return trianglesIntermediaires;
    }

    /**
     * Crée un tableau de pixels initialisé à blanc.
     * 
     * @author Marc DJOLE
     * @param largeur Largeur de l'image
     * @param hauteur Hauteur de l'image
     * @return Tableau de pixels
     */
    private Color[][] creerTableauPixelsInitial(int largeur, int hauteur) {
        Color[][] tableauPixels = new Color[largeur][hauteur];
        for (int k = 0; k < largeur; k++) {
            for (int l = 0; l < hauteur; l++) {
                tableauPixels[k][l] = Color.WHITE;
            }
        }
        return tableauPixels;
    }

    /**
     * Affiche un aperçu de l'animation de morphing.
     * 
     * @author Anthony GARCIA
     * @param vueImage Vue de l'image
     * @param nombreEtapes Nombre d'étapes de morphing
     * @param dureeEtape Durée de chaque étape en millisecondes
     */
    private void afficherApercuAnimation(ImageView vueImage, int nombreEtapes, double dureeEtape) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Aperçu du GIF");

        StackPane conteneur = new StackPane();
        conteneur.getChildren().add(vueImage);

        MenuBar barreMenu = new MenuBar();
        Menu menuGif = new Menu("Télécharger le GIF");
        MenuItem menuItemTelecharger = new MenuItem("Télécharger");

        menuItemTelecharger.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le GIF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF Files", "*.gif"));
            File file = fileChooser.showSaveDialog(popupStage);
            if (file != null) {
                try {
                    LinkedList<BufferedImage> bufferedImages = new LinkedList<>();
                    for (ImageBit imageBit : app.getImagesIntermediaires()) {
                        bufferedImages.add(SwingFXUtils.fromFXImage(imageBit.getImg(), null));
                    }
                    GifSequenceWriter.genererGif(bufferedImages, file.getAbsolutePath(), (int) dureeEtape, app.isEstCycle());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        menuGif.getItems().add(menuItemTelecharger);
        barreMenu.getMenus().add(menuGif);

        VBox racine = new VBox(barreMenu, conteneur);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double aspectRatio = largeurOriginale / hauteurOriginale;

        double nouvelleLargeur = largeurOriginale;
        double nouvelleHauteur = hauteurOriginale;

        // Ajustez les dimensions de l'image en fonction de l'écran
        if (largeurOriginale > screenWidth * 0.8) {
            nouvelleLargeur = screenWidth * 0.8;
            nouvelleHauteur = nouvelleLargeur / aspectRatio;
        }
        if (nouvelleHauteur > screenHeight * 0.8 - barreMenu.getHeight()) {
            nouvelleHauteur = screenHeight * 0.8 - barreMenu.getHeight();
            nouvelleLargeur = nouvelleHauteur * aspectRatio;
        }

        vueImage.setFitWidth(nouvelleLargeur);
        vueImage.setFitHeight(nouvelleHauteur);

        // Initialement, définissez la taille de la scène pour inclure la hauteur de la barre de menus et éviter que l'image soit coupée
        Scene popupScene = new Scene(racine, nouvelleLargeur, nouvelleHauteur + barreMenu.getHeight() + 20); // +20 pour tout autre espace supplémentaire
        popupStage.setScene(popupScene);

        popupScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            vueImage.setFitWidth(newVal.doubleValue());
        });

        popupScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            vueImage.setFitHeight(newVal.doubleValue() - barreMenu.getHeight() - 20); // -20 pour tout autre espace supplémentaire
        });

        Timeline chronologie = new Timeline();
        chronologie.setCycleCount(Timeline.INDEFINITE);
        chronologie.setAutoReverse(false);

        for (int i = 0; i < nombreEtapes; i++) {
            final Image image = app.getImagesIntermediaires().get(i).getImg();
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * dureeEtape), event -> vueImage.setImage(image));
            chronologie.getKeyFrames().add(keyFrame);
        }

        chronologie.play();
        popupStage.show();
    }
    
    /**
     * Obtient la couleur la plus fréquente dans une forme définie par des points.
     * 
     * @author Mattéo REYNE
     * @param pixelReader Lecteur de pixels
     * @param image Image à analyser
     * @param points Points définissant la forme
     * @param couleurFond Couleur de fond à ignorer
     * @return Couleur la plus fréquente dans la forme
     */
    private static Color getCouleurPlusFrequenteDansForme(PixelReader pixelReader, Image image, List<Point> points, Color couleurFond) {
        Map<Color, Integer> compteurCouleurs = new HashMap<>();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pointDansPolygone(x, y, points)) {
                    Color color = pixelReader.getColor(x, y);
                    if (!color.equals(couleurFond)) {
                        compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
                    }
                }
            }
        }

        return compteurCouleurs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Color.BLACK);
    }

    /**
     * Obtient la couleur de fond d'une image.
     * 
     * @author Thomas BEAUSSART
     * @param pixelReader Lecteur de pixels
     * @param image Image à analyser
     * @param points Points définissant la forme
     * @return Couleur de fond de l'image
     */
    public static Color getCouleurFond(PixelReader pixelReader, Image image, List<Point> points) {
        Map<Color, Integer> compteurCouleurs = new HashMap<>();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        Color color;
        for (int y = 0; y < height; y++) {
            if (!pointDansPolygone(0, y, points)) {
                color = pixelReader.getColor(0, y);
                compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
            }
            if (!pointDansPolygone( width-1, y, points)) {
                color = pixelReader.getColor(width-1, y);
                compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
            }
        }
        for (int x = 0; x < width; x++) {
            if (!pointDansPolygone(x, 0, points)) {
                color = pixelReader.getColor(x, 0);
                compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
            }
            if (!pointDansPolygone(x,height-1, points)) {
                color = pixelReader.getColor(x, height-1);
                compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
            }
        }


        return compteurCouleurs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Color.BLACK);

    }

    /**
     * Remplit une forme définie par des points avec une couleur.
     * 
     * @author Mattéo REYNE
     * @param ecrivainPixels Écrivain de pixels
     * @param points Points définissant la forme
     * @param couleur Couleur à utiliser pour remplir la forme
     */
    private static void remplirForme(PixelWriter ecrivainPixels, List<Point> points, Color couleur) {
        double minX = points.stream().mapToDouble(Point::getX).min().orElse(0);
        double maxX = points.stream().mapToDouble(Point::getX).max().orElse(Double.MAX_VALUE);
        double minY = points.stream().mapToDouble(Point::getY).min().orElse(0);
        double maxY = points.stream().mapToDouble(Point::getY).max().orElse(Double.MAX_VALUE);

        for (int y = (int) minY; y <= (int) maxY; y++) {
            for (int x = (int) minX; x <= (int) maxX; x++) {
                if (pointDansPolygone(x, y, points)) {
                    ecrivainPixels.setColor(x, y, couleur);
                }
            }
        }
    }

    /**
     * Vérifie si un point est à l'intérieur d'un polygone défini par des points.
     * 
     * @author Marc DJOLE
     * @param x Coordonnée x du point
     * @param y Coordonnée y du point
     * @param points Points définissant le polygone
     * @return true si le point est à l'intérieur du polygone, false sinon
     */
    private static boolean pointDansPolygone(int x, int y, List<Point> points) {
        boolean result = false;
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            if ((points.get(i).getY() > y) != (points.get(j).getY() > y) &&
                    (x < (points.get(j).getX() - points.get(i).getX()) * (y - points.get(i).getY()) / (points.get(j).getY() - points.get(i).getY()) + points.get(i).getX())) {
                result = !result;
            }
            j = i;
        }
        return result;
    }

    /**
     * Crée une image à partir d'un tableau de pixels.
     * 
     * @author Mattéo REYNE
     * @param tabPixels Tableau de pixels
     * @return Image créée
     */
    private Image creerImageDepuisTableau(Color[][] tabPixels) {
        WritableImage writableImage = new WritableImage(app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < app.getLargeurImageOriginale(); x++) {
            for (int y = 0; y < app.getHauteurImageOriginale(); y++) {
                pixelWriter.setColor(x, y, tabPixels[x][y]);
            }
        }

        return writableImage;
    }
    
}