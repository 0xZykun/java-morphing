package cytech.morphing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.concurrent.Task;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test extends Application {
    private Slider slider;
    private List<Point> pointsGauche = new ArrayList<>();
    private List<Point> pointsDroite = new ArrayList<>();
    private List<Triangle> listeTriangleGauche = new ArrayList<>();
    private List<Triangle> listeTriangleDroite = new ArrayList<>();
    private Canvas canevasGauche, canevasDroite;
    private Image imageGauche, imageDroite;
    private List<ImageBit> imagesOrigines = new ArrayList<>();
    private boolean imageChargeeGauche = false;
    private boolean imageChargeeDroite = false;
    private boolean optionsValidee = true;
    private CheckBox afficherTrianglesCheckbox = new CheckBox("Afficher triangles");
    private Button boutonPrecedent, boutonSuivant, boutonAjouter, boutonSupprimer, boutonImporterGauche, boutonImporterDroite, boutonEffacerGauche, boutonEffacerDroite, boutonValider;
    private double largeurScene;
    private double hauteurScene;
    private int largeurImageOriginale = 0;
    private int hauteurImageOriginale = 0;
    private List<ImageBit> imagesIntermediaires = new ArrayList<>();
    private ImageView imageView = new ImageView();
    private int indexEtapeCourante = 0;
    private int choixMethode = 3;
    private int nombreImages = 2;
    private int dureeDuGIF = 5;
    private boolean estCycle = true;
    private int nombreImagesIntermediaires = dureeDuGIF*10;
    private Stage primaryStage;
    private int taillePolice = 12;
    private Color couleurPolice = Color.web("#000000");
    private Color couleurFond = Color.web("#FFFFFF");

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MorphingFx");

        // Maximiser la fenêtre pour occuper tout l'écran disponible
        primaryStage.setMaximized(true);

        // Récupérer les dimensions de l'écran disponible
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Initialiser la taille de la scène pour qu'elle occupe 90% de l'écran disponible
        largeurScene = screenBounds.getWidth() * 0.9;
        hauteurScene = screenBounds.getHeight() * 0.9;      

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();

        // Menu Options
        Menu menuOptions = new Menu("Options");
        MenuItem menuItemOptions = new MenuItem("Configurer GIF");
        menuItemOptions.setOnAction(e -> ouvrirDialogueOptions());
        menuOptions.getItems().add(menuItemOptions);

        // Menu Préférences
        Menu menuPreferences = new Menu("Préférences");
        MenuItem menuItemPreferences = new MenuItem("Style et Apparence");
        menuItemPreferences.setOnAction(e -> ouvrirDialoguePreferences());
        menuPreferences.getItems().add(menuItemPreferences);

        // Ajouter les menus à la barre de menu
        menuBar.getMenus().addAll(menuOptions, menuPreferences);

        VBox root = new VBox(10);

        root.getChildren().addAll(menuBar, creerSectionImage(), creerVisionneuseImage());

        Scene scene = new Scene(root, largeurScene, hauteurScene);
        primaryStage.setScene(scene);

        primaryStage.show();

        actualiserEtatBoutons();
        initialiserBoutonsImage(scene);
    }

    private void ajouterPointsBordure(List<Point> points, Canvas canevas) {
        points.add(new Point(0, canevas.getHeight() * (hauteurImageOriginale - 1) / hauteurImageOriginale));
        points.add(new Point(0, 0));
        points.add(new Point(canevas.getWidth() * (largeurImageOriginale - 1) / largeurImageOriginale, 0));
        points.add(new Point(canevas.getWidth() * (largeurImageOriginale - 1) / largeurImageOriginale, canevas.getHeight() * (hauteurImageOriginale - 1) / hauteurImageOriginale));
    }

    private void enleverPointsBordure(List<Point> points) {
        for (int i = 0; i < 4; i++) {
            if (!points.isEmpty()) {
                points.remove(points.size() - 1);
            }
        }
    }

    private HBox creerSectionImage() {
        double largeurInitiale = largeurScene/2.4;
        double hauteurInitiale = hauteurScene/2.4;
    
        canevasGauche = new Canvas(largeurInitiale, hauteurInitiale);
        canevasDroite = new Canvas(largeurInitiale, hauteurInitiale);
        ////////.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 2;");//////////////
    
        HBox boiteCentre = new HBox(30);
        boiteCentre.getChildren().addAll(
            creerControleImage(canevasGauche, pointsGauche, true),
            creerSectionControle(),
            creerControleImage(canevasDroite, pointsDroite, false)
        );
        boiteCentre.setAlignment(Pos.CENTER);
        return boiteCentre;
    }

    private BorderPane creerControleImage(Canvas canevas, List<Point> points, boolean estGauche) {
        BorderPane panneauBord = new BorderPane();
        
        Button boutonImporter = new Button("Importer Image");
        if (estGauche) {
            boutonImporterGauche = boutonImporter;
        } else {
            boutonImporterDroite = boutonImporter;
        }
        
        Button boutonEffacer = new Button("Supprimer Image");
        if (estGauche) {
            boutonEffacerGauche = boutonEffacer;
        } else {
            boutonEffacerDroite = boutonEffacer;
        }
        boutonEffacer.setOnAction(e -> {
            effacerCanevas(estGauche ? "gauche" : "droite");
            pointsGauche.clear();
            pointsDroite.clear();
            redessinerCanevas(canevasGauche, pointsGauche, imageGauche);
            redessinerCanevas(canevasDroite, pointsDroite, imageDroite);
            actualiserEtatBoutons();
            if (!imageChargeeDroite && !imageChargeeGauche) {
                largeurImageOriginale = 0;
                hauteurImageOriginale = 0;
            }
        });
        
        HBox boxTop = new HBox(10, boutonImporter, boutonEffacer);
        boxTop.setAlignment(Pos.CENTER);  // Centrer les boutons dans le HBox
        panneauBord.setTop(boxTop);
        panneauBord.setCenter(canevas);
        
        configurerCanevas(canevas, points);
        
        return panneauBord;
    }    
    
    private VBox creerSectionControle() {
        VBox controle = new VBox(10);
        controle.setAlignment(Pos.CENTER);  // Centrer les boutons dans le VBox
    
        boutonAjouter = new Button("Ajouter Point");
        boutonAjouter.setOnAction(e -> ajouterPointCentre());
    
        boutonSupprimer = new Button("Supprimer Point");
        boutonSupprimer.setOnAction(e -> supprimerDernierPoint());
    
        boutonValider = new Button("Valider");
        boutonValider.setOnAction(e -> {
            imageView.setImage(null);
    
            List<Triangle> trianglesValideeGauche = Triangle.deepCopy(listeTriangleGauche);
            List<Triangle> trianglesValideeDroite = Triangle.deepCopy(listeTriangleDroite);
    
            if (choixMethode == 3) {
                ajouterPointsBordure(pointsGauche, canevasGauche);
                ajouterPointsBordure(pointsDroite, canevasGauche);
                generateDelaunayTriangles();
            }

            trianglesValideeGauche = adjustTrianglesToOriginal(listeTriangleGauche, canevasGauche.getWidth(), canevasGauche.getHeight(), largeurImageOriginale, hauteurImageOriginale);
            trianglesValideeDroite = adjustTrianglesToOriginal(listeTriangleDroite, canevasGauche.getWidth(), canevasGauche.getHeight(), largeurImageOriginale, hauteurImageOriginale);
    
            morphing3(trianglesValideeGauche, trianglesValideeDroite);
            actualiserVisionneuseImage();
            actualiserEtatBoutons();
            mettreAJourSlider();
    
            if (choixMethode == 3) {
                enleverPointsBordure(pointsGauche);
                enleverPointsBordure(pointsDroite);
                generateDelaunayTriangles();
            }
        });
    
        afficherTrianglesCheckbox.setSelected(true);
    
        controle.getChildren().addAll(boutonAjouter, boutonSupprimer, afficherTrianglesCheckbox, boutonValider);
        afficherTrianglesCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> redessinerCanevas(canevasGauche, pointsGauche, imageGauche));
        afficherTrianglesCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> redessinerCanevas(canevasDroite, pointsDroite, imageDroite));
        return controle;
    }       

    private HBox creerVisionneuseImage() {
        double largeurInitiale = largeurScene / 2.4;
        double hauteurInitiale = hauteurScene / 2.4;
    
        BorderPane visionneuse = new BorderPane();
    
        boutonPrecedent = new Button("<");
        boutonSuivant = new Button(">");
    
        boutonPrecedent.setOnAction(e -> {
            if (indexEtapeCourante > 0) {
                indexEtapeCourante--;
                imageView.setImage(imagesIntermediaires.get(indexEtapeCourante).genererImage().getImage());
                slider.setValue(indexEtapeCourante);
                actualiserEtatBoutonsVisionneuse();
            }
        });
    
        boutonSuivant.setOnAction(e -> {
            if (indexEtapeCourante < imagesIntermediaires.size() - 1) {
                indexEtapeCourante++;
                imageView.setImage(imagesIntermediaires.get(indexEtapeCourante).genererImage().getImage());
                slider.setValue(indexEtapeCourante);
                actualiserEtatBoutonsVisionneuse();
            }
        });
    
        imageView.setFitWidth(largeurInitiale);
        imageView.setFitHeight(hauteurInitiale);
        imageView.setPreserveRatio(true);
    
        VBox boxLeft = new VBox(boutonPrecedent);
        boxLeft.setAlignment(Pos.CENTER);
        VBox boxRight = new VBox(boutonSuivant);
        boxRight.setAlignment(Pos.CENTER);
    
        visionneuse.setLeft(boxLeft);
        visionneuse.setCenter(imageView);
        visionneuse.setRight(boxRight);
    
        slider = new Slider(0, 0, 0);
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
    
        // Lier la largeur du slider à la largeur de l'image
        slider.maxWidthProperty().bind(imageView.fitWidthProperty());
    
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!imagesIntermediaires.isEmpty()) {
                indexEtapeCourante = newValue.intValue();
                imageView.setImage(imagesIntermediaires.get(indexEtapeCourante).genererImage().getImage());
                actualiserEtatBoutonsVisionneuse();
            }
        });
    
        VBox boxBottom = new VBox(slider);
        boxBottom.setAlignment(Pos.CENTER);
        visionneuse.setBottom(boxBottom);
    
        mettreAJourSlider();
    
        HBox container = new HBox(1);
        container.getChildren().add(visionneuse);
        container.setAlignment(Pos.CENTER);
    
        return container;
    }
    
    private void mettreAJourSlider() {
        if (!imagesIntermediaires.isEmpty()) {
            slider.setMax(imagesIntermediaires.size() - 1);
            slider.setValue(0);
        } else {
            slider.setMax(0);
            slider.setValue(0);
        }
        actualiserEtatBoutonsVisionneuse();
    }
    
    private void actualiserEtatBoutonsVisionneuse() {
        boutonPrecedent.setDisable(indexEtapeCourante == 0);
        boutonSuivant.setDisable(indexEtapeCourante >= imagesIntermediaires.size() - 1);
    }
    
    private void actualiserVisionneuseImage() {
        if (!imagesIntermediaires.isEmpty()) {
            indexEtapeCourante = 0;
            imageView.setImage(imagesIntermediaires.get(0).genererImage().getImage());
        }
        mettreAJourSlider();
        actualiserEtatBoutonsVisionneuse();
    }    

    private void initialiserBoutonsImage(Scene scene) {
        boutonImporterGauche.setOnAction(e -> chargerImage(canevasGauche, "gauche", scene));
        boutonImporterDroite.setOnAction(e -> chargerImage(canevasDroite, "droite", scene));
    }

    private void chargerImage(Canvas canevas, String cote, Scene scene) {
        FileChooser selecteurFichier = new FileChooser();
        File fichier = selecteurFichier.showOpenDialog(null);
        if (fichier != null) {
            Image imageTemporaire = new Image(fichier.toURI().toString(), false);
            ImageBit nouvelleImageBit = new ImageBit(fichier.toURI().toString());
            double ratio = imageTemporaire.getWidth() / imageTemporaire.getHeight();
            double largeurMaxImage = scene.getWidth() / 2.4;
            double hauteurMaxImage = scene.getHeight() / 3;
            double nouvelleLargeur, nouvelleHauteur;
    
            // Ajuster les dimensions en fonction de la largeur et de la hauteur maximales
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
    
            if ((largeurImageOriginale == 0 && hauteurImageOriginale == 0) ||
                    (imageTemporaire.getWidth() == largeurImageOriginale && imageTemporaire.getHeight() == hauteurImageOriginale)) {
                if (largeurImageOriginale == 0 && hauteurImageOriginale == 0) {
                    largeurImageOriginale = (int) imageTemporaire.getWidth();
                    hauteurImageOriginale = (int) imageTemporaire.getHeight();
                }
    
                // Redimensionner le canvas pour qu'il corresponde exactement à la taille de l'image
                canevas.setWidth(nouvelleLargeur);
                canevas.setHeight(nouvelleHauteur);
    
                Image image = new Image(fichier.toURI().toString(), nouvelleLargeur, nouvelleHauteur, true, true);
                GraphicsContext gc = canevas.getGraphicsContext2D();
                gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());
                gc.drawImage(image, 0, 0);
    
                if ("gauche".equals(cote)) {
                    imageGauche = image;
                    imageChargeeGauche = true;
                    if (!imagesOrigines.isEmpty()) {
                        imagesOrigines.remove(0);
                    }
                    imagesOrigines.add(0, nouvelleImageBit);
                } else {
                    imageDroite = image;
                    imageChargeeDroite = true;
                    if (imagesOrigines.size() > 1) {
                        imagesOrigines.set(1, nouvelleImageBit);
                    } else {
                        imagesOrigines.add(nouvelleImageBit);
                    }
                }
    
                // Redimensionner le BorderPane center pour correspondre à la taille de l'image
                BorderPane panneauBord = (BorderPane) canevas.getParent();
                panneauBord.setPrefSize(nouvelleLargeur + 20, nouvelleHauteur + 40);  // +20 pour la bordure et +40 pour les boutons
                panneauBord.setMaxSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
                
                // Forcer la mise à jour du layout avec les nouvelles tailles
                Platform.runLater(() -> {
                    panneauBord.layout();
                });
    
                actualiserEtatBoutons();
            } else {
                montrerAlerte("Erreur", "Veuillez charger des images de même taille initiale : " +
                        (int) largeurImageOriginale + "x" + (int) hauteurImageOriginale);
            }
        }
    }              

    private void montrerAlerte(String titre, String contenu) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.initOwner(primaryStage);
        alerte.showAndWait();
    }

    private void configurerCanevas(Canvas canevas, List<Point> points) {
        canevas.setOnMousePressed(e -> {
            int indexSelectionne = trouverIndexPoint(e.getX(), e.getY(), points);
            if (indexSelectionne != -1) {
                canevas.setOnMouseDragged(event -> {
                    Point nouveauPoint = new Point(
                            Math.min(Math.max(event.getX(), 0), canevas.getWidth()),
                            Math.min(Math.max(event.getY(), 0), canevas.getHeight())
                    );
                    points.set(indexSelectionne, nouveauPoint);
                    redessinerCanevas(canevas, points, canevas == canevasGauche ? imageGauche : imageDroite);
                });
            }
        });
    
        canevas.setOnMouseReleased(e -> {
            canevas.setOnMouseDragged(null);
            redessinerCanevas(canevas, points, canevas == canevasGauche ? imageGauche : imageDroite);
            mettreAJourTrianglesEtDessiner();
        });
    }    

    private int trouverIndexPoint(double x, double y, List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.distance(x, y) < 5) {
                return i;
            }
        }
        return -1;
    }

    private void mettreAJourTrianglesEtDessiner() {
        if (pointsGauche.size() >= 3) {
            generateDelaunayTriangles();
        }

        redessinerCanevas(canevasGauche, pointsGauche, imageGauche);
        redessinerCanevas(canevasDroite, pointsDroite, imageDroite);
    }

    private void redessinerCanevas(Canvas canevas, List<Point> points, Image image) {
        GraphicsContext gc = canevas.getGraphicsContext2D();
        gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());
    
        if (image != null) {
            double largeur = image.getWidth();
            double hauteur = image.getHeight();
            
            gc.drawImage(image, 0, 0, largeur, hauteur);
        }
    
        // Dessiner les lignes grises en premier
        if (points.size() > 1) {
            dessinerLignesGrises(gc, points);
        }
    
        // Dessiner les triangles cyan si la méthode choisie est 3
        if (choixMethode == 3 && afficherTrianglesCheckbox.isSelected() && points.size() > 1) {
            gc.setStroke(Color.CYAN);
            List<Triangle> triangles = (canevas == canevasGauche) ? listeTriangleGauche : listeTriangleDroite;
            for (Triangle triangle : triangles) {
                if (!estTriangleCoin(triangle, canevas.getWidth(), canevas.getHeight())) {
                    Point a = triangle.getA();
                    Point b = triangle.getB();
                    Point c = triangle.getC();
                    gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
                    gc.strokeLine(b.getX(), b.getY(), c.getX(), c.getY());
                    gc.strokeLine(c.getX(), c.getY(), a.getX(), a.getY());
                }
            }
        }
    
        // Dessiner les points en dernier
        dessinerPoints(gc, points);
    }    

    private boolean estTriangleCoin(Triangle triangle, double width, double height) {
        return (triangle.getA().estAuCoin((int) width, (int) height) ||
                triangle.getB().estAuCoin((int) width, (int) height) ||
                triangle.getC().estAuCoin((int) width, (int) height));
    }

    private void dessinerLignesGrises(GraphicsContext gc, List<Point> points) {
        Point pointPrecedent = null;
        for (Point point : points) {
            if (pointPrecedent != null) {
                gc.setStroke(Color.GRAY);
                gc.strokeLine(pointPrecedent.getX(), pointPrecedent.getY(), point.getX(), point.getY());
            }
            pointPrecedent = point;
        }

        if (!points.isEmpty() && points.size() > 1) {
            Point premierPoint = points.get(0);
            Point dernierPoint = points.get(points.size() - 1);
            gc.setStroke(Color.GRAY);
            gc.strokeLine(dernierPoint.getX(), dernierPoint.getY(), premierPoint.getX(), premierPoint.getY());
        }
    }

    private void effacerCanevas(String cote) {
        pointsGauche.clear();
        pointsDroite.clear();

        if ("gauche".equals(cote)) {
            if (canevasGauche != null) {
                GraphicsContext gc = canevasGauche.getGraphicsContext2D();
                gc.clearRect(0, 0, canevasGauche.getWidth(), canevasGauche.getHeight());
            }
            imageChargeeGauche = false;
            imageGauche = null;
        } else {
            if (canevasDroite != null) {
                GraphicsContext gc = canevasDroite.getGraphicsContext2D();
                gc.clearRect(0, 0, canevasDroite.getWidth(), canevasDroite.getHeight());
            }
            imageChargeeDroite = false;
            imageDroite = null;
        }

        actualiserEtatBoutons();

        redessinerCanevas(canevasGauche, pointsGauche, imageGauche);
        redessinerCanevas(canevasDroite, pointsDroite, imageDroite);
        mettreAJourTrianglesEtDessiner();
    }

    private void ajouterPointCentre() {
        double xGauche = canevasGauche.getWidth() / 2;
        double yGauche = canevasGauche.getHeight() / 2;
        pointsGauche.add(new Point(xGauche, yGauche));
        
        double xDroite = canevasDroite.getWidth() / 2;
        double yDroite = canevasDroite.getHeight() / 2;
        pointsDroite.add(new Point(xDroite, yDroite));
        
        redessinerCanevas(canevasGauche, pointsGauche, imageGauche);
        redessinerCanevas(canevasDroite, pointsDroite, imageDroite);
        actualiserEtatBoutons();
        mettreAJourTrianglesEtDessiner();
    }

    private void supprimerDernierPoint() {
        if (!pointsGauche.isEmpty() && !pointsDroite.isEmpty()) {
            pointsGauche.remove(pointsGauche.size() - 1);
            pointsDroite.remove(pointsDroite.size() - 1);

            generateDelaunayTriangles();

            redessinerCanevas(canevasGauche, pointsGauche, imageGauche);
            redessinerCanevas(canevasDroite, pointsDroite, imageDroite);
            mettreAJourTrianglesEtDessiner();
            actualiserEtatBoutons();
        }
    }

    private void dessinerPoints(GraphicsContext gc, List<Point> points) {
        List<Color> couleurs = ColorGenerateur.genererCouleursDifferentes(15);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Color couleur = couleurs.get(i % couleurs.size());
            gc.setFill(couleur);
            gc.fillOval(point.getX() - 5, point.getY() - 5, 10, 10);
        }
    }

    /*private List<Point> adjustPointsToOriginal(List<Point> points, double canvasWidth, double canvasHeight, double originalWidth, double originalHeight) {
        List<Point> adjustedPoints = new ArrayList<>();
        double scaleX = originalWidth / canvasWidth;
        double scaleY = originalHeight / canvasHeight;

        for (Point point : points) {
            double adjustedX = point.getX() * scaleX;
            double adjustedY = point.getY() * scaleY;
            adjustedPoints.add(new Point(adjustedX, adjustedY));
        }

        return adjustedPoints;
    }
*/
    private List<Triangle> adjustTrianglesToOriginal(List<Triangle> triangles, double canvasWidth, double canvasHeight, double originalWidth, double originalHeight) {
        List<Triangle> adjustedTriangles = new ArrayList<>();
        double scaleX = originalWidth / canvasWidth;
        double scaleY = originalHeight / canvasHeight;

        for (Triangle triangle : triangles) {
            double ax = triangle.getA().getX() * scaleX;
            double ay = triangle.getA().getY() * scaleY;
            double bx = triangle.getB().getX() * scaleX;
            double by = triangle.getB().getY() * scaleY;
            double cx = triangle.getC().getX() * scaleX;
            double cy = triangle.getC().getY() * scaleY;

            Triangle newTriangle = new Triangle(new Point(ax, ay), new Point(bx, by), new Point(cx, cy));
            adjustedTriangles.add(newTriangle);
        }

        return adjustedTriangles;
    }

    private void actualiserEtatBoutons() {
        boolean lesDeuxImagesChargees = imageChargeeGauche && imageChargeeDroite;
        boutonAjouter.setDisable(!lesDeuxImagesChargees);
        boutonSupprimer.setDisable(pointsGauche.isEmpty());
        boutonImporterGauche.setDisable(imageChargeeGauche);
        boutonImporterDroite.setDisable(imageChargeeDroite);
        boutonEffacerGauche.setDisable(!imageChargeeGauche);
        boutonEffacerDroite.setDisable(!imageChargeeDroite);
        boutonValider.setDisable(pointsGauche.isEmpty() || !optionsValidee);
        boutonPrecedent.setDisable(indexEtapeCourante == 0);
        boutonSuivant.setDisable(indexEtapeCourante >= imagesIntermediaires.size() - 1);
    }

    public void generateDelaunayTriangles() {
        listeTriangleDroite.clear();
        listeTriangleGauche.clear();

        if (pointsGauche.size() < 3) {
            return;
        }

        for (int i = 0; i < pointsGauche.size(); i++) {
            for (int j = i + 1; j < pointsGauche.size(); j++) {
                for (int k = j + 1; k < pointsGauche.size(); k++) {
                    Point a = pointsGauche.get(i);
                    Point b = pointsGauche.get(j);
                    Point c = pointsGauche.get(k);
                    Triangle newTriangle = new Triangle(a, b, c);

                    Point a2 = pointsDroite.get(i);
                    Point b2 = pointsDroite.get(j);
                    Point c2 = pointsDroite.get(k);
                    Triangle newTriangle2 = new Triangle(a2, b2, c2);

                    boolean bonTriangle = true;
                    for (int l = 0; l < pointsGauche.size(); l++) {
                        Point d = pointsGauche.get(l);
                        if (newTriangle.cercleContientPoint(d)) {
                            bonTriangle = false;
                            break;
                        }
                    }

                    if (bonTriangle) {
                        listeTriangleGauche.add(newTriangle);
                        listeTriangleDroite.add(newTriangle2);
                    }
                }
            }
        }
    }

    public void morphing3(List<Triangle> trianglesGauche, List<Triangle> trianglesDroite) {
        imagesIntermediaires.clear();
        int nombreEtapes = nombreImagesIntermediaires;
        double dureeEtape = dureeDuGIF * 1000 / nombreEtapes; // durée de chaque étape en millisecondes
    
        ImageView vueImage = new ImageView();
        ProgressBar barreProgression = new ProgressBar(0); // Création de la ProgressBar
        barreProgression.setMaxWidth(Double.MAX_VALUE);
    
        // Créer une nouvelle fenêtre pour la barre de progression
        Stage barreProgressionStage = new Stage();
        barreProgressionStage.setTitle("Progression du morphing");
    
        VBox vboxProgression = new VBox(barreProgression);
        vboxProgression.setSpacing(10);
        vboxProgression.setPadding(new Insets(10));
        vboxProgression.setAlignment(Pos.CENTER); // Centrer la barre de progression

        Scene sceneProgression = new Scene(vboxProgression, 500, 100);
        barreProgressionStage.setScene(sceneProgression);
        barreProgressionStage.show();
    
        // Créer une tâche pour le morphing
        Task<Void> morphingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < nombreEtapes; i++) {
                    double coefficient = i / (nombreEtapes - 1.0);
    
                    List<Triangle> listeTrianglesIntermediaires = new ArrayList<>();
                    for (int j = 0; j < trianglesGauche.size(); j++) {
                        Triangle tGauche = trianglesGauche.get(j);
                        Triangle tDroite = trianglesDroite.get(j);
                        Triangle tIntermediaire = tGauche.morphToTriangle(tDroite, coefficient);
                        listeTrianglesIntermediaires.add(tIntermediaire);
                    }
    
                    Color[][] tableauPixels = new Color[largeurImageOriginale][hauteurImageOriginale];
                    for (int k = 0; k < largeurImageOriginale; k++) {
                        for (int l = 0; l < hauteurImageOriginale; l++) {
                            tableauPixels[k][l] = Color.WHITE;
                        }
                    }
    
                    for (int k = 0; k < largeurImageOriginale; k++) {
                        for (int l = 0; l < hauteurImageOriginale; l++) {
                            Point pointCourant = new Point((double) k, (double) l);
    
                            for (int m = 0; m < listeTrianglesIntermediaires.size(); m++) {
                                Triangle tInterCourant = listeTrianglesIntermediaires.get(m);
    
                                if (tInterCourant.pointAppartient(pointCourant)) {
                                    Double[] coordBarycentriques = tInterCourant.coordoneesBarycentriques(pointCourant);
                                    Triangle tDepart = trianglesGauche.get(m);
                                    Triangle tFin = trianglesDroite.get(m);
    
                                    double xDepart = tDepart.getA().getX() * coordBarycentriques[0] + tDepart.getB().getX() * coordBarycentriques[1] + tDepart.getC().getX() * coordBarycentriques[2];
                                    double yDepart = tDepart.getA().getY() * coordBarycentriques[0] + tDepart.getB().getY() * coordBarycentriques[1] + tDepart.getC().getY() * coordBarycentriques[2];
    
                                    double xFin = tFin.getA().getX() * coordBarycentriques[0] + tFin.getB().getX() * coordBarycentriques[1] + tFin.getC().getX() * coordBarycentriques[2];
                                    double yFin = tFin.getA().getY() * coordBarycentriques[0] + tFin.getB().getY() * coordBarycentriques[1] + tFin.getC().getY() * coordBarycentriques[2];
    
                                    if (xDepart < largeurImageOriginale && yDepart < hauteurImageOriginale && xFin < largeurImageOriginale && yFin < hauteurImageOriginale) {
                                        double[] pixelsDebut = imagesOrigines.get(0).getRGBA((int) xDepart, (int) yDepart);
                                        double[] pixelsFin = imagesOrigines.get(1).getRGBA((int) xFin, (int) yFin);
    
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
                        imagesIntermediaires.add(new ImageBit((WritableImage) image));
                    });
    
                    // Mise à jour de la ProgressBar
                    updateProgress(i + 1, nombreEtapes); // Utilisation de updateProgress pour mettre à jour la barre de progression
                }
                return null;
            }
    
            @Override
            protected void succeeded() {
                // Fermer la fenêtre de progression lorsque la tâche est terminée
                barreProgressionStage.close();
                actualiserVisionneuseImage();
    
                // Afficher l'aperçu de l'animation
                Stage popupStage = new Stage();
                popupStage.setTitle("Aperçu de l'animation");
    
                StackPane conteneur = new StackPane();
                conteneur.getChildren().add(vueImage);
    
                MenuBar barreMenu = new MenuBar();
                Menu menuGif = new Menu("Télécharger le GIF");
                MenuItem menuItemTelecharger = new MenuItem("Télécharger");
    
                menuItemTelecharger.setOnAction(e -> {
                    try {
                        //GifSequenceWriter.generateGif(new LinkedList<>(imagesIntermediaires), "gif.GIF", (int) dureeEtape, estCycle);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    
                menuGif.getItems().add(menuItemTelecharger);
                barreMenu.getMenus().add(menuGif);
    
                VBox racine = new VBox(barreMenu, conteneur); // Ne pas ajouter la ProgressBar ici
                Scene popupScene = new Scene(racine, largeurImageOriginale, hauteurImageOriginale);
                popupStage.setScene(popupScene);
    
                // Lancement de l'animation (chronologie)
                Timeline chronologie = new Timeline();
                chronologie.setCycleCount(nombreEtapes); // Nombre de frames
                chronologie.setAutoReverse(false); // Pas d'inversion de l'animation
    
                for (int i = 0; i < nombreEtapes; i++) {
                    final Image image = imagesIntermediaires.get(i).getImg(); // Accéder directement à img
                    KeyFrame keyFrame = new KeyFrame(Duration.millis(i * dureeEtape), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            vueImage.setImage(image);
                        }
                    });
                    chronologie.getKeyFrames().add(keyFrame);
                }
    
                chronologie.play();
                popupStage.show();
            }
        };
    
        // Lier la barre de progression à la tâche
        barreProgression.progressProperty().bind(morphingTask.progressProperty());
    
        // Démarrer la tâche dans un thread séparé
        new Thread(morphingTask).start();
    }          
    
    private Image creerImageDepuisTableau(Color[][] tabpixels) {
        WritableImage writableImage = new WritableImage(largeurImageOriginale, hauteurImageOriginale);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < largeurImageOriginale; x++) {
            for (int y = 0; y < hauteurImageOriginale; y++) {
                pixelWriter.setColor(x, y, tabpixels[x][y]);
            }
        }

        return writableImage;
    }

    private void ouvrirDialogueOptions() {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.setTitle("Configurer GIF");
    
        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);
    
        grille.add(new Label("Nombre d'images (2 par défaut) :"), 0, 0);
        TextField compteurImages = new TextField(String.valueOf(nombreImages));
        compteurImages.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                compteurImages.setText(ancienneValeur);
            }
        });
        grille.add(compteurImages, 1, 0);
    
        grille.add(new Label("Images intermédiaires (10 par défaut) :"), 0, 1);
        TextField nbImagesIntermediaires = new TextField(String.valueOf(nombreImagesIntermediaires));
        nbImagesIntermediaires.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                nbImagesIntermediaires.setText(ancienneValeur);
            }
        });
        grille.add(nbImagesIntermediaires, 1, 1);
    
        grille.add(new Label("Durée du GIF (s, 5 par défaut) :"), 0, 2);
        TextField dureeGIF = new TextField(String.valueOf(dureeDuGIF));
        dureeGIF.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                dureeGIF.setText(ancienneValeur);
            }
        });
        grille.add(dureeGIF, 1, 2);
    
        ComboBox<String> selecteurMethode = new ComboBox<>();
        selecteurMethode.getItems().addAll("Formes simples", "Formes arrondies", "Images");
        switch (choixMethode) {
            case 1:
                selecteurMethode.setValue("Formes simples");
                break;
            case 2:
                selecteurMethode.setValue("Formes arrondies");
                break;
            case 3:
                selecteurMethode.setValue("Images");
                break;
        }
        grille.add(new Label("Choix de la méthode :"), 0, 3);
        grille.add(selecteurMethode, 1, 3);
    
        // Ajouter une CheckBox pour l'option de cycle
        CheckBox cycleCheckBox = new CheckBox("Cycle");
        cycleCheckBox.setSelected(estCycle);  // Par défaut, la CheckBox est cochée
        grille.add(new Label("Cycle :"), 0, 4);
        grille.add(cycleCheckBox, 1, 4);
    
        dialogue.getDialogPane().setContent(grille);
    
        dialogue.setResultConverter(boutonDialogue -> {
            if (boutonDialogue == ButtonType.OK) {
                try {
                    nombreImages = Integer.parseInt(compteurImages.getText());
                    nombreImagesIntermediaires = Integer.parseInt(nbImagesIntermediaires.getText());
                    dureeDuGIF = Integer.parseInt(dureeGIF.getText());
                    choixMethode = selecteurMethode.getSelectionModel().getSelectedIndex() + 1;
                    estCycle = cycleCheckBox.isSelected(); // Mettre à jour l'attribut isCycling
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : L'un des champs contient des données non valides qui ne sont pas des nombres entiers.");
                }
            }
            return null;
        });
    
        dialogue.showAndWait();
    }    

    private void ouvrirDialoguePreferences() {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.setTitle("Style et Apparence");
        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);

        TextField champTaillePolice = new TextField(String.valueOf(taillePolice));
        grille.add(new Label("Taille de la police :"), 0, 0);
        grille.add(champTaillePolice, 1, 0);

        ColorPicker selecteurCouleurPolice = new ColorPicker(couleurPolice);
        grille.add(new Label("Couleur de la police :"), 0, 1);
        grille.add(selecteurCouleurPolice, 1, 1);

        ColorPicker selecteurCouleurFond = new ColorPicker(couleurFond);
        grille.add(new Label("Couleur du fond :"), 0, 3);
        grille.add(selecteurCouleurFond, 1, 3);

        dialogue.getDialogPane().setContent(grille);

        dialogue.setResultConverter(boutonDialogue -> {
            if (boutonDialogue == ButtonType.OK) {
                try {
                    taillePolice = Integer.parseInt(champTaillePolice.getText());
                    couleurPolice = selecteurCouleurPolice.getValue();
                    couleurFond = selecteurCouleurFond.getValue();
                    appliquerStyle(taillePolice, couleurPolice, couleurFond);
                } catch (NumberFormatException e) {
                    System.out.println("La taille de la police doit être un nombre valide.");
                }
            }
            return null;
        });

        dialogue.showAndWait();
    }

    private void appliquerStyle(int taillePolice, Color couleurPolice, Color couleurFond) {
        Platform.runLater(() -> {
            String cssBase = String.format("-fx-font-size: %dpx; -fx-text-fill: %s; -fx-background-color: %s; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5px",
                    taillePolice,
                    toCssColor(couleurPolice),
                    toCssColor(couleurFond));

            primaryStage.getScene().getRoot().setStyle(cssBase);
            appliquerStyleAuxBoutons(cssBase);
        });
    }

    private String toCssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }

    private void appliquerStyleAuxBoutons(String cssBase) {
        List<Button> tousLesBoutons = Arrays.asList(boutonAjouter, boutonSupprimer, boutonImporterGauche, boutonImporterDroite, boutonEffacerGauche, boutonEffacerDroite, boutonValider);
        for (Button bouton : tousLesBoutons) {
            bouton.setStyle(cssBase);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}