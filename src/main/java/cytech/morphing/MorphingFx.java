package cytech.morphing;

import javafx.application.Application;
//import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
//import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

public class MorphingFx extends Application {
    private Stage primaryStage;
    private double largeurScene;
    private double hauteurScene;
    private ImageLoader imageLoader;
    private PointControle pointControle;
    private SegmentControle segmentControle;
    private TriangleControle triangleControle;
    private ImageViewer imageViewer;
    private MorphingTask morphingTask;
    private Controle controle;

    private int indexEtapeCourante = 0;
    private List<ImageBit> imagesIntermediaires = new ArrayList<>();
    private int largeurImageOriginale = 0;
    private int hauteurImageOriginale = 0;
    private boolean estCycle = true;
    private int nombreImages = 2;
    private int nombreImagesIntermediaires = 8;
    private int dureeDuGIF = 3;
    private List<ImageBit> imagesOrigines = new ArrayList<>();
    private int choixMethode = 3;

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MorphingFx");
        primaryStage.setMaximized(true);

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        largeurScene = screen.getWidth() * 0.9;
        hauteurScene = screen.getHeight() * 0.9;

        BarreMenu menu = new BarreMenu(this);
        imageLoader = new ImageLoader(this);
        pointControle = new PointControle(this);
        segmentControle = new SegmentControle(this);
        triangleControle = new TriangleControle(this);
        imageViewer = new ImageViewer(this);
        morphingTask = new MorphingTask(this);
        controle = new Controle(this);

        VBox root = new VBox(10);
        root.getChildren().addAll(menu.getMenuBar(), imageLoader.creerSectionImage(), imageViewer.creerVisionneuseImage());

        Scene scene = new Scene(root, largeurScene, hauteurScene);
        primaryStage.setScene(scene);
        primaryStage.show();

        imageLoader.actualiserEtatBoutons();
        imageLoader.initialiserBoutonsImage(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void ouvrirDialogueOptions() {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.setTitle("Configurer GIF");
    
        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);
    
        TextField compteurImages = new TextField(String.valueOf(nombreImages));
        TextField nbImagesIntermediaires = new TextField(String.valueOf(nombreImagesIntermediaires));
        TextField dureeGIF = new TextField(String.valueOf(dureeDuGIF));
    
        grille.add(new Label("Nombre d'images (2 par défaut) :"), 0, 0);
        compteurImages.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                compteurImages.setText(ancienneValeur);
            }
        });
        grille.add(compteurImages, 1, 0);
    
        grille.add(new Label("Images intermédiaires (20 par défaut) :"), 0, 1);
        nbImagesIntermediaires.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                nbImagesIntermediaires.setText(ancienneValeur);
            }
        });
        grille.add(nbImagesIntermediaires, 1, 1);
    
        grille.add(new Label("Durée du GIF (s, 3 par défaut) :"), 0, 2);
        dureeGIF.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                dureeGIF.setText(ancienneValeur);
            }
        });
        grille.add(dureeGIF, 1, 2);
    
        ComboBox<String> selecteurMethode = new ComboBox<>();
        selecteurMethode.getItems().addAll("Formes simples", "Formes arrondies", "Triangles", "Segments");
        switch (choixMethode) {
            case 1:
                selecteurMethode.setValue("Formes simples");
                break;
            case 2:
                selecteurMethode.setValue("Formes arrondies");
                break;
            case 3:
                selecteurMethode.setValue("Triangles");
                break;
            case 4:
                selecteurMethode.setValue("Segments");
                break;
        }
        grille.add(new Label("Choix de la méthode :"), 0, 3);
        grille.add(selecteurMethode, 1, 3);
    
        CheckBox cycleCheckBox = new CheckBox("Cycle");
        cycleCheckBox.setSelected(estCycle);
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
                    estCycle = cycleCheckBox.isSelected();
                    
                    controle.miseJourControle(choixMethode);

                    if (choixMethode == 4) {
                        this.getSegmentControle().configurerCanevasSegment(this.getImageLoader().getCanevasGauche(), this.getSegmentControle().getSegmentsGauche(), this.getImageLoader().getCanevasDroite(), this.getSegmentControle().getSegmentsDroite());
                        this.getSegmentControle().configurerCanevasSegment(this.getImageLoader().getCanevasDroite(), this.getSegmentControle().getSegmentsDroite(), this.getImageLoader().getCanevasGauche(), this.getSegmentControle().getSegmentsGauche());
                        segmentControle.clearSegments();
                    } else {
                        this.getPointControle().configurerCanevas(this.getImageLoader().getCanevasGauche(), this.getPointControle().getPointsGauche(), this.getImageLoader().getCanevasDroite(), this.getPointControle().getPointsDroite());
                        this.getPointControle().configurerCanevas(this.getImageLoader().getCanevasDroite(), this.getPointControle().getPointsDroite(), this.getImageLoader().getCanevasGauche(), this.getPointControle().getPointsGauche());
                    }
                    

                    pointControle.clearPoints();
                    imageLoader.actualiserEtatBoutons();
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : L'un des champs contient des données non valides.");
                }
            }
            return null;
        });
    
        dialogue.showAndWait();
    }    

    /*public void ouvrirDialoguePreferences() {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.setTitle("Style et Apparence");
        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);

        TextField champTaillePolice = new TextField(String.valueOf(12));
        grille.add(new Label("Taille de la police :"), 0, 0);
        grille.add(champTaillePolice, 1, 0);

        ColorPicker selecteurCouleurPolice = new ColorPicker(Color.BLACK);
        grille.add(new Label("Couleur de la police :"), 0, 1);
        grille.add(selecteurCouleurPolice, 1, 1);

        ColorPicker selecteurCouleurFond = new ColorPicker(Color.WHITE);
        grille.add(new Label("Couleur du fond :"), 0, 2);
        grille.add(selecteurCouleurFond, 1, 2);

        dialogue.getDialogPane().setContent(grille);

        dialogue.setResultConverter(boutonDialogue -> {
            if (boutonDialogue == ButtonType.OK) {
                try {
                    int taillePolice = Integer.parseInt(champTaillePolice.getText());
                    Color couleurPolice = selecteurCouleurPolice.getValue();
                    Color couleurFond = selecteurCouleurFond.getValue();
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
        List<Button> tousLesBoutons = Arrays.asList(
                controle.getBoutonAjouter(), 
                controle.getBoutonSupprimer(), 
                controle.getBoutonValider()
        );
        for (Button bouton : tousLesBoutons) {
            bouton.setStyle(cssBase);
        }
    }*/

    public List<Point> adjusterPointsAOriginal(List<Point> points, double largeurCanvas, double hauteurCanvas, double largeurOriginale, double hauteurOriginale) {
        List<Point> pointsAjustes = new ArrayList<>();
    
        if (choixMethode == 2) {
            double largeurEffectiveCanvas = largeurCanvas;
            double hauteurEffectiveCanvas = hauteurCanvas;
            double decalageX = largeurCanvas * 0.05;
            double decalageY = hauteurCanvas * 0.05;
            double echelleX = largeurOriginale / largeurEffectiveCanvas;
            double echelleY = hauteurOriginale / hauteurEffectiveCanvas;
    
            for (Point point : points) {
                double ajusterX = (point.getX() - decalageX) * echelleX;
                double ajusterY = (point.getY() - decalageY) * echelleY;
                pointsAjustes.add(new Point(ajusterX, ajusterY));
            }
        } else {
            double echelleX = largeurOriginale / largeurCanvas;
            double echelleY = hauteurOriginale / hauteurCanvas;
    
            for (Point point : points) {
                double ajusterX = point.getX() * echelleX;
                double ajusterY = point.getY() * echelleY;
                pointsAjustes.add(new Point(ajusterX, ajusterY));
            }
        }
    
        return pointsAjustes;
    }    

    public List<Triangle> adjustTrianglesToOriginal(List<Triangle> triangles, double canvasWidth, double canvasHeight, double originalWidth, double originalHeight) {
        List<Triangle> adjusteTriangles = new ArrayList<>();
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
            adjusteTriangles.add(newTriangle);
        }

        return adjusteTriangles;
    }

    public List<Segment> adjusterSegmentsAOriginal(List<Segment> segments, double canvasWidth, double canvasHeight, double originalWidth, double originalHeight) {
        List<Segment> adjusteSegments = new ArrayList<>();
        double scaleX = originalWidth / canvasWidth;
        double scaleY = originalHeight / canvasHeight;

        for (Segment segment : segments) {
            double ax = segment.getA().getX() * scaleX;
            double ay = segment.getA().getY() * scaleY;
            double bx = segment.getB().getX() * scaleX;
            double by = segment.getB().getY() * scaleY;

            Segment newSegment = new Segment(new Point(ax, ay), new Point(bx, by));
            adjusteSegments.add(newSegment);
        }
        return adjusteSegments;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public double getLargeurScene() {
        return largeurScene;
    }

    public double getHauteurScene() {
        return hauteurScene;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public PointControle getPointControle() {
        return pointControle;
    }

    public SegmentControle getSegmentControle() {
        return segmentControle;
    }

    public TriangleControle getTriangleControle() {
        return triangleControle;
    }

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    public MorphingTask getMorphingTask() {
        return morphingTask;
    }

    public Controle getControle() {
        return controle;
    }

    public int getIndexEtapeCourante() {
        return indexEtapeCourante;
    }

    public void setIndexEtapeCourante(int indexEtapeCourante) {
        this.indexEtapeCourante = indexEtapeCourante;
    }

    public List<ImageBit> getImagesIntermediaires() {
        return imagesIntermediaires;
    }

    public void setImagesIntermediaires(List<ImageBit> imagesIntermediaires) {
        this.imagesIntermediaires = imagesIntermediaires;
    }

    public int getLargeurImageOriginale() {
        return largeurImageOriginale;
    }

    public void setLargeurImageOriginale(int largeurImageOriginale) {
        this.largeurImageOriginale = largeurImageOriginale;
    }

    public int getHauteurImageOriginale() {
        return hauteurImageOriginale;
    }

    public void setHauteurImageOriginale(int hauteurImageOriginale) {
        this.hauteurImageOriginale = hauteurImageOriginale;
    }

    public boolean isEstCycle() {
        return estCycle;
    }

    public void setEstCycle(boolean estCycle) {
        this.estCycle = estCycle;
    }

    public int getNombreImagesIntermediaires() {
        return nombreImagesIntermediaires;
    }

    public void setNombreImagesIntermediaires(int nombreImagesIntermediaires) {
        this.nombreImagesIntermediaires = nombreImagesIntermediaires;
    }

    public int getDureeDuGIF() {
        return dureeDuGIF;
    }

    public void setDureeDuGIF(int dureeDuGIF) {
        this.dureeDuGIF = dureeDuGIF;
    }

    public List<ImageBit> getImagesOrigines() {
        return imagesOrigines;
    }

    public void setImagesOrigines(List<ImageBit> imagesOrigines) {
        this.imagesOrigines = imagesOrigines;
    }

    public int getChoixMethode() {
        return choixMethode;
    }

    public void setChoixMethode(int choixMethode) {
        this.choixMethode = choixMethode;
    }
}