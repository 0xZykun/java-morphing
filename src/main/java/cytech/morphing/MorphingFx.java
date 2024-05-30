package cytech.morphing;

/*
 * see import javafx.application.Application;
 * see import javafx.geometry.Rectangle2D;
 * see import javafx.scene.Scene;
 * see import javafx.scene.control.*;
 * see import javafx.scene.layout.VBox;
 * see import javafx.stage.Screen;
 * see import javafx.stage.Stage;
 * see import javafx.scene.layout.GridPane;
 * 
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe principale pour l'application MorphingFx.
 * Gère l'interface utilisateur et les interactions avec les différents contrôles.
 * 
 */
public class MorphingFx extends Application {
    /**
     * La scène principale de l'application.
     */
    private Stage primaryStage;

    /**
     * La largeur de la scène.
     */
    private double largeurScene;

    /**
     * La hauteur de la scène.
     */
    private double hauteurScene;

    /**
     * Le chargeur d'images.
     */
    private ImageLoader imageLoader;

    /**
     * Le contrôleur des points.
     */
    private PointControle pointControle;

    /**
     * Le contrôleur des segments.
     */
    private SegmentControle segmentControle;

    /**
     * Le contrôleur des triangles.
     */
    private TriangleControle triangleControle;

    /**
     * Le visualiseur d'images.
     */
    private ImageViewer imageViewer;

    /**
     * La tâche de morphing.
     */
    private MorphingTask morphingTask;

    /**
     * Le contrôleur principal.
     */
    private Controle controle;

    /**
     * L'index de l'étape courante.
     */
    private int indexEtapeCourante = 0;

    /**
     * La liste des images intermédiaires.
     */
    private List<ImageBit> imagesIntermediaires = new ArrayList<>();

    /**
     * La largeur de l'image originale.
     */
    private int largeurImageOriginale = 0;

    /**
     * La hauteur de l'image originale.
     */
    private int hauteurImageOriginale = 0;

    /**
     * Indique si le cycle est activé.
     */
    private boolean estCycle = true;

    /**
     * Le nombre d'images.
     */
    private int nombreImages = 2;

    /**
     * Le nombre d'images intermédiaires.
     */
    private int nombreImagesIntermediaires = 24;

    /**
     * La durée du GIF en secondes.
     */
    private int dureeDuGIF = 3;

    /**
     * La liste des images d'origine.
     */
    private List<ImageBit> imagesOrigines = new ArrayList<>();

    /**
     * Le choix de la méthode de morphing.
     */
    private int choixMethode = 3;

    /**
     * Point d'entrée principal pour l'application.
     * 
     * @autor Mattéo REYNE
     * @param primaryStage la scène principale de l'application
     */
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

    /**
     * Ouvre une boîte de dialogue pour configurer les options du GIF.
     * 
     * @autor Mattéo REYNE
     */
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
    
        grille.add(new Label("Images intermédiaires (24 par défaut) :"), 0, 1);
        nbImagesIntermediaires.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                nbImagesIntermediaires.setText(ancienneValeur);
            }
        });
        grille.add(nbImagesIntermediaires, 1, 1);
    
        grille.add(new Label("Durée du GIF (s, 2 par défaut) :"), 0, 2);
        dureeGIF.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            if (!nouvelleValeur.matches("\\d*")) {
                dureeGIF.setText(ancienneValeur);
            }
        });
        grille.add(dureeGIF, 1, 2);
    
        ComboBox<String> selecteurMethode = new ComboBox<>();
        selecteurMethode.getItems().addAll("Formes simples", "Formes arrondies", "Triangulation", "Segments");
        switch (choixMethode) {
            case 1:
                selecteurMethode.setValue("Formes simples");
                break;
            case 2:
                selecteurMethode.setValue("Formes arrondies");
                break;
            case 3:
                selecteurMethode.setValue("Triangulation");
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
                        if (choixMethode == 2) {
                            this.getControle().getFinirForme().setSelected(false);
                        }
                        this.getPointControle().clearPoints();
                        this.getTriangleControle().generateDelaunayTriangles();
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

    /**
     * Ouvre une boîte de dialogue pour afficher le contenu du fichier README.
     * 
     * @author Mattéo REYNE
     */
    public void ouvrirReadme() {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.setTitle("Aide");
        dialogue.setWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.9);
        dialogue.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.9);

        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        VBox contenu = new VBox(10);
        contenu.setPadding(new Insets(10));
        
        Label instructionsLabel = new Label("Comment utiliser le programme :");
        instructionsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextFlow instructionsText = new TextFlow();

        instructionsText.getChildren().addAll(
            createBoldText(" Utiliser la barre de menu :\n"),
            createBoldText(" \n  Options :\n"),
            new Text("  - Configuration du GIF :\n"),
            createBoldText("     Nombre d’images à utiliser pour faire le morphing : "), new Text(" 2\n"),
            createBoldText("     Nombre d’images intermédiaires (par défaut 24) : "), new Text(" Nombres d’images que le programme va générer pour faire le morphing.\n"),
            createBoldText("     Durée du GIF (par défaut 2) : "), new Text(" Nombre de secondes\n"),
            createBoldText("     Choix de la méthode (par défaut Triangulation) :\n"),
            new Text("       . Formes simples\n"),
            new Text("       . Formes arrondies\n"),
            new Text("       . Triangulation\n"),
            new Text("       . Segments\n"),
            createBoldText("     Case cycle : "), new Text(" Si elle est cochée, permet de lire le gif généré en boucle.\n"),
            new Text("  - Aide : Affiche ce fichier\n"),
            createBoldText(" \n    Presets (Choisi la méthode à utiliser et place les points/segments prédéfinis) :\n"),
            new Text("       . Carré vers croix\n"),
            new Text("       . Cercle vers etoile\n"),
            new Text("       . Visages masculins\n"),
            new Text("       . Visages féminins\n"),
            createBoldText(" \n Utilisation du programme :\n"),
            new Text("   Importer une image -> Les deux images doivent avoir la même résolution\n"),
            new Text("   Supprimer une image -> Retire tous les points placés\n"),
            createBoldText(" \n    Ajouter un point :\n"),
            new Text(" Tous les points générés sur une image correspondent au point de la même couleur sur l’autre image.\n"),
            new Text(" I - Formes simples et Triangulation :\n"),
            new Text("   Le bouton ajoute un point au centre des deux images\n"),
            new Text("   Clic droit ajoute un point aux coordonnées du pointeur sur les deux images\n"),
            new Text(" II - Formes arrondies\n"),
            new Text("   Le bouton ajoute un point au centre des deux images si ce point est le premier. Sinon les points de contrôle de la courbe sont ajoutés automatiquement pour former une courbe de Bézier.\n"),
            new Text("   Clic droit ajoute un point aux coordonnées du pointeur sur les deux images si ce point est le premier. Sinon il ajoute automatiquement les points de contrôle de la courbe entre les deux points placés par l’utilisateur.\n"),
            createBoldText(" \n    Ajouter segment :\n"),
            new Text(" Tous les segments générés sur une image correspondent au segment de la même couleur sur l’autre image. Le point de départ du segment est en vert et le point d'arrivée est en rouge. Le segment est donc assimilé à un vecteur, donc son sens a une importance.\n"),
            new Text("   Le bouton ajoute un segment vertical à gauche des deux images\n"),
            new Text("   Le clic droit place un point fictif qui définira le début du segment si ce clic est le premier. Le second définira le point qui termine le segment et le programme tracera le segment final.\n"),
            createBoldText(" \n    Supprimer point :\n"),
            new Text(" I - Formes simples et Triangulation :\n"),
            new Text("   Le bouton supprimer supprime le dernier point placé\n"),
            new Text(" II - Formes arrondies\n"),
            new Text("   Si il n’y a qu’un seul point, alors ce point sera supprimé. Sinon, le dernier point placé et les 2 points de contrôle de la courbe seront supprimés.\n"),
            new Text(" III - Segments\n"),
            new Text("   Supprime le dernier segment placé\n"),
            createBoldText(" \n    Menu choix couleur de la ligne (Formes simples / Formes arrondies / Triangulation) :\n"),
            new Text(" Permet de choisir la couleur des lignes tracées\n"),
            createBoldText(" \n    Menu choix couleur des points (Formes arrondies) :\n"),
            new Text(" Permet de choisir la couleur des points de contrôle des courbes\n"),
            createBoldText(" \n    Case finir forme arrondie :\n"),
            new Text(" Si elle est cochée, ajoute la courbe reliant le premier point placé au dernier point placé avec ses points de contrôle. Nécessaire pour pouvoir valider le morphing.\n"),
            createBoldText(" \n    Case afficher les triangles Delaunay :\n"),
            new Text(" Si elle est cochée, affiche les triangles (en cyan) formés par les points\n"),
            createBoldText(" \n    Bouton valider :\n"),
            new Text(" Permet de générer le morphing selon la méthode choisie.\n"),
            createBoldText(" \n    Conditions requises :\n"),
            createBoldText(" I - Formes simples :\n"),
            new Text("   Avoir un point placé\n"),
            createBoldText(" II - Formes arrondies :\n"),
            new Text("   Avoir deux points placés et avoir coché “finir forme”\n"),
            createBoldText(" III - Triangulation :\n"),
            new Text("   Avoir trois points placés\n"),
            createBoldText(" IV - Segments :\n"),
            new Text("   Avoir un segment placé\n"),
            createBoldText(" \n Visionneuse :\n"),
            new Text("   Permet de voir les images intermédiaires générées par le programme une par une avec les flèches, ou de les faire défiler avec la scroll bar.\n")
        );
        instructionsText.setStyle("-fx-font-size: 14px;");

        ScrollPane scrollPane = new ScrollPane(instructionsText);
        scrollPane.setFitToWidth(true);

        contenu.getChildren().addAll(instructionsLabel, scrollPane);

        dialogue.getDialogPane().setContent(contenu);
        dialogue.showAndWait();
    }

    private Text createBoldText(String text) {
        Text t = new Text(text);
        t.setStyle("-fx-font-weight: bold;");
        return t;
    }

    /**
     * Ajuste les points d'un canevas aux dimensions originales de l'image.
     * 
     * @autor Mattéo REYNE
     * @param points la liste des points à ajuster
     * @param largeurCanvas la largeur du canevas
     * @param hauteurCanvas la hauteur du canevas
     * @param largeurOriginale la largeur de l'image originale
     * @param hauteurOriginale la hauteur de l'image originale
     * @return la liste des points ajustés
     */
    public List<Point> ajusterPointsAOriginal(List<Point> points, double largeurCanvas, double hauteurCanvas, double largeurOriginale, double hauteurOriginale) {
        List<Point> pointsAjustes = new ArrayList<>();
    
        double echelleX = largeurOriginale / largeurCanvas;
        double echelleY = hauteurOriginale / hauteurCanvas;
    
        for (Point point : points) {
            double ajusterX = point.getX() * echelleX;
            double ajusterY = point.getY() * echelleY;
            pointsAjustes.add(new Point(ajusterX, ajusterY));
        }
    
        return pointsAjustes;
    }    

    /**
     * Ajuste les triangles d'un canevas aux dimensions originales de l'image.
     * 
     * @autor Mattéo REYNE
     * @param triangles la liste des triangles à ajuster
     * @param canvasWidth la largeur du canevas
     * @param canvasHeight la hauteur du canevas
     * @param originalWidth la largeur de l'image originale
     * @param originalHeight la hauteur de l'image originale
     * @return la liste des triangles ajustés
     */
    public List<Triangle> ajusterTrianglesAOriginal(List<Triangle> triangles, double canvasWidth, double canvasHeight, double originalWidth, double originalHeight) {
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

    /**
     * Ajuste les segments d'un canevas aux dimensions originales de l'image.
     * 
     * @autor Mattéo REYNE
     * @param segments la liste des segments à ajuster
     * @param canvasWidth la largeur du canevas
     * @param canvasHeight la hauteur du canevas
     * @param originalWidth la largeur de l'image originale
     * @param originalHeight la hauteur de l'image originale
     * @return la liste des segments ajustés
     */
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

    // Getters et setters pour les variables d'instance

    /**
     * @autor Mattéo REYNE
     * @return la scène principale de l'application
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @autor Mattéo REYNE
     * @return la largeur de la scène
     */
    public double getLargeurScene() {
        return largeurScene;
    }

    /**
     * @autor Mattéo REYNE
     * @return la hauteur de la scène
     */
    public double getHauteurScene() {
        return hauteurScene;
    }

    /**
     * @autor Mattéo REYNE
     * @return le chargeur d'images
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * @autor Mattéo REYNE
     * @return le contrôleur des points
     */
    public PointControle getPointControle() {
        return pointControle;
    }

    /**
     * @autor Mattéo REYNE
     * @return le contrôleur des segments
     */
    public SegmentControle getSegmentControle() {
        return segmentControle;
    }

    /**
     * @autor Mattéo REYNE
     * @return le contrôleur des triangles
     */
    public TriangleControle getTriangleControle() {
        return triangleControle;
    }

    /**
     * @autor Mattéo REYNE
     * @return le visualiseur d'images
     */
    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    /**
     * @autor Mattéo REYNE
     * @return la tâche de morphing
     */
    public MorphingTask getMorphingTask() {
        return morphingTask;
    }

    /**
     * @autor Mattéo REYNE
     * @return le contrôleur principal
     */
    public Controle getControle() {
        return controle;
    }

    /**
     * @autor Mattéo REYNE
     * @return l'index de l'étape courante
     */
    public int getIndexEtapeCourante() {
        return indexEtapeCourante;
    }

    /**
     * Définit l'index de l'étape courante.
     * 
     * @autor Mattéo REYNE
     * @param indexEtapeCourante l'index de l'étape courante à définir
     */
    public void setIndexEtapeCourante(int indexEtapeCourante) {
        this.indexEtapeCourante = indexEtapeCourante;
    }

    /**
     * @autor Mattéo REYNE
     * @return la liste des images intermédiaires
     */
    public List<ImageBit> getImagesIntermediaires() {
        return imagesIntermediaires;
    }

    /**
     * Définit la liste des images intermédiaires.
     * 
     * @autor Mattéo REYNE
     * @param imagesIntermediaires la liste des images intermédiaires à définir
     */
    public void setImagesIntermediaires(List<ImageBit> imagesIntermediaires) {
        this.imagesIntermediaires = imagesIntermediaires;
    }

    /**
     * @autor Mattéo REYNE
     * @return la largeur de l'image originale
     */
    public int getLargeurImageOriginale() {
        return largeurImageOriginale;
    }

    /**
     * Définit la largeur de l'image originale.
     * 
     * @autor Mattéo REYNE
     * @param largeurImageOriginale la largeur de l'image originale à définir
     */
    public void setLargeurImageOriginale(int largeurImageOriginale) {
        this.largeurImageOriginale = largeurImageOriginale;
    }

    /**
     * @autor Mattéo REYNE
     * @return la hauteur de l'image originale
     */
    public int getHauteurImageOriginale() {
        return hauteurImageOriginale;
    }

    /**
     * Définit la hauteur de l'image originale.
     * 
     * @autor Mattéo REYNE
     * @param hauteurImageOriginale la hauteur de l'image originale à définir
     */
    public void setHauteurImageOriginale(int hauteurImageOriginale) {
        this.hauteurImageOriginale = hauteurImageOriginale;
    }

    /**
     * @autor Mattéo REYNE
     * @return vrai si le cycle est activé, faux sinon
     */
    public boolean isEstCycle() {
        return estCycle;
    }

    /**
     * Définit l'état du cycle.
     * 
     * @autor Mattéo REYNE
     * @param estCycle l'état du cycle à définir
     */
    public void setEstCycle(boolean estCycle) {
        this.estCycle = estCycle;
    }

    /**
     * @autor Mattéo REYNE
     * @return le nombre d'images intermédiaires
     */
    public int getNombreImagesIntermediaires() {
        return nombreImagesIntermediaires;
    }

    /**
     * Définit le nombre d'images intermédiaires.
     * 
     * @autor Mattéo REYNE
     * @param nombreImagesIntermediaires le nombre d'images intermédiaires à définir
     */
    public void setNombreImagesIntermediaires(int nombreImagesIntermediaires) {
        this.nombreImagesIntermediaires = nombreImagesIntermediaires;
    }

    /**
     * @autor Mattéo REYNE
     * @return la durée du GIF en secondes
     */
    public int getDureeDuGIF() {
        return dureeDuGIF;
    }

    /**
     * Définit la durée du GIF en secondes.
     * 
     * @autor Mattéo REYNE
     * @param dureeDuGIF la durée du GIF à définir
     */
    public void setDureeDuGIF(int dureeDuGIF) {
        this.dureeDuGIF = dureeDuGIF;
    }

    /**
     * @autor Mattéo REYNE
     * @return la liste des images d'origine
     */
    public List<ImageBit> getImagesOrigines() {
        return imagesOrigines;
    }

    /**
     * Définit la liste des images d'origine.
     * 
     * @autor Mattéo REYNE
     * @param imagesOrigines la liste des images d'origine à définir
     */
    public void setImagesOrigines(List<ImageBit> imagesOrigines) {
        this.imagesOrigines = imagesOrigines;
    }

    /**
     * @autor Mattéo REYNE
     * @return le choix de la méthode de morphing
     */
    public int getChoixMethode() {
        return choixMethode;
    }

    /**
     * Définit le choix de la méthode de morphing.
     *
     * @autor Mattéo REYNE
     * @param choixMethode le choix de la méthode de morphing à définir
     */
    public void setChoixMethode(int choixMethode) {
        this.choixMethode = choixMethode;
    }
}