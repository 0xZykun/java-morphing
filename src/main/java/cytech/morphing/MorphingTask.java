package cytech.morphing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MorphingTask {
    private MorphingFx app;

    public MorphingTask(MorphingFx app) {
        this.app = app;
    }

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

                Color couleurFondGauche = getCouleurPlusFrequente(lecteurGauche, imageGauche.getImg(), pointsGauche);
                Color couleurFormeGauche = getCouleurPlusFrequenteDansForme(lecteurGauche, imageGauche.getImg(), pointsGauche, couleurFondGauche);
                Color couleurFondDroite = getCouleurPlusFrequente(lecteurDroite, imageDroite.getImg(), pointsDroite);
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
    
                Color couleurFondGauche = getCouleurPlusFrequente(lecteurGauche, imageGauche.getImg(), pointsGauche);
                Color couleurFormeGauche = getCouleurPlusFrequenteDansForme(lecteurGauche, imageGauche.getImg(), pointsGauche, couleurFondGauche);
                Color couleurFondDroite = getCouleurPlusFrequente(lecteurDroite, imageDroite.getImg(), pointsDroite);
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
    
    private Color[][] remplirForme(int largeur, int hauteur, Color couleurForme, Color couleurFond, List<Point> points) {
        points.add(points.get(0));

        List<Point> pointsFinales = CourbeBezier.cBezier(points);

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
            System.out.println("Y : " + y);
        }
        System.out.println("Fin remplir forme");
        return tabPixel;
    }

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

    private ProgressBar creerBarreProgression() {
        ProgressBar barreProgression = new ProgressBar(0);
        barreProgression.setMaxWidth(Double.MAX_VALUE);
        return barreProgression;
    }

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

    private void remplirFond(PixelWriter ecrivainPixels, int largeur, int hauteur, Color couleurFondIntermediaire) {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                ecrivainPixels.setColor(x, y, couleurFondIntermediaire);
            }
        }
    }

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

    private Color[][] creerTableauPixelsInitial(int largeur, int hauteur) {
        Color[][] tableauPixels = new Color[largeur][hauteur];
        for (int k = 0; k < largeur; k++) {
            for (int l = 0; l < hauteur; l++) {
                tableauPixels[k][l] = Color.WHITE;
            }
        }
        return tableauPixels;
    }

    private void afficherApercuAnimation(ImageView vueImage, int nombreEtapes, double dureeEtape) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Aperçu de l'animation");

        StackPane conteneur = new StackPane();
        conteneur.getChildren().add(vueImage);

        MenuBar barreMenu = new MenuBar();
        Menu menuGif = new Menu("Télécharger le GIF");
        MenuItem menuItemTelecharger = new MenuItem("Télécharger");

        menuItemTelecharger.setOnAction(e -> {
            try {
                LinkedList<BufferedImage> bufferedImages = new LinkedList<>();
                for (ImageBit imageBit : app.getImagesIntermediaires()) {
                    bufferedImages.add(SwingFXUtils.fromFXImage(imageBit.getImg(), null));
                }
                GifSequenceWriter.generateGif(bufferedImages, "gif.GIF", (int) dureeEtape, app.isEstCycle());
            } catch (Exception ex) {
                ex.printStackTrace();
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

        if (largeurOriginale > screenWidth || hauteurOriginale > screenHeight) {
            nouvelleLargeur = screenWidth * 0.8;
            nouvelleHauteur = nouvelleLargeur / aspectRatio;
            if (nouvelleHauteur > screenHeight * 0.8) {
                nouvelleHauteur = screenHeight * 0.8;
                nouvelleLargeur = nouvelleHauteur * aspectRatio;
            }
        }

        Scene popupScene = new Scene(racine, nouvelleLargeur, nouvelleHauteur);
        popupStage.setScene(popupScene);

        popupScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            vueImage.setFitWidth(newVal.doubleValue());
        });

        popupScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            vueImage.setFitHeight(newVal.doubleValue());
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

    public static Color getCouleurPlusFrequente(PixelReader pixelReader, Image image, List<Point> points) {
        Map<Color, Integer> compteurCouleurs = new HashMap<>();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!pointDansPolygone(x, y, points)) {
                    Color color = pixelReader.getColor(x, y);

                    compteurCouleurs.put(color, compteurCouleurs.getOrDefault(color, 0) + 1);
                }
            }
        }

        return compteurCouleurs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Color.BLACK);
    }

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