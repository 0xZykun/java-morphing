package cytech.morphing;

/**
 * see import javafx.geometry.Pos;
 * see import javafx.scene.control.Button;
 * see import javafx.scene.control.CheckBox;
 * see import javafx.scene.layout.VBox;
 * see import javafx.scene.control.ColorPicker;
 * see import javafx.scene.control.Label;
 * see import javafx.scene.paint.Color;
 *
 * see import java.util.List;
 */
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Classe Controle pour gérer les contrôles de l'application MorphingFx.
 * 
 */
public class Controle {
    /**
     * Référence à l'application principale.
     */
    private MorphingFx app;

    /**
     * Conteneur pour les contrôles de l'application.
     */
    private VBox boxControle;

    /**
     * Bouton pour ajouter un point.
     */
    private Button boutonAjouter;

    /**
     * Bouton pour ajouter un segment.
     */
    private Button boutonAjouterSegment;

    /**
     * Bouton pour supprimer un point.
     */
    private Button boutonSupprimer;

    /**
     * Bouton pour supprimer un segment.
     */
    private Button boutonSupprimerSegment;

    /**
     * Bouton pour valider les opérations.
     */
    private Button boutonValider;

    /**
     * Checkbox pour afficher les triangles.
     */
    private CheckBox afficherTrianglesCheckbox;

    /**
     * Checkbox pour finir la forme arrondie.
     */
    private CheckBox finirForme;

    /**
     * Sélecteur de couleur pour les lignes.
     */
    private ColorPicker couleurLignes;

    /**
     * Sélecteur de couleur pour les points.
     */
    private ColorPicker couleurPoints;

    /**
     * Constructeur de la classe Controle.
     * 
     * @author Mattéo REYNE
     * @param app Instance de l'application principale
     */
    public Controle(MorphingFx app) {
        this.app = app;

        boxControle = new VBox(10);
        boxControle.setAlignment(Pos.CENTER);

        couleurLignes = new ColorPicker(Color.GREY);
        couleurLignes.setOnAction(e -> app.getTriangleControle().mettreAJourTrianglesEtDessiner());

        couleurPoints = new ColorPicker(Color.BLACK);

        boutonAjouterSegment = new Button("Ajouter Segment");
        boutonAjouterSegment.setOnAction(e -> {
            app.getSegmentControle().ajouterSegment();
            app.getSegmentControle().resetPointTemp();
        });

        boutonSupprimerSegment = new Button("Supprimer Segment");
        boutonSupprimerSegment.setOnAction(e -> {
            app.getSegmentControle().supprimerSegment();
            app.getSegmentControle().resetPointTemp();
        });

        boutonAjouter = new Button("Ajouter Point");
        boutonAjouter.setOnAction(e -> app.getPointControle().ajouterPointCentre());

        boutonSupprimer = new Button("Supprimer Point");
        boutonSupprimer.setOnAction(e -> app.getPointControle().supprimerDernierPoint());

        boutonValider = new Button("Valider");
        boutonValider.setOnAction(e -> {
            List<Point> pointsValideeGauche;
            List<Point> pointsValideeDroite;
            app.getImageViewer().getImageView().setImage(null);

            switch (app.getChoixMethode()) {
                case 1:
                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing1(pointsValideeGauche, pointsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));                    
                    app.getImageViewer().actualiserVisionneuseImage();
                    break;
                case 2:
                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing2(pointsValideeGauche, pointsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));    
                    app.getImageViewer().actualiserVisionneuseImage();
                    break;
                case 3:
                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    List<Triangle> trianglesValideeGauche = Triangle.copieProfonde(app.getTriangleControle().getListeTriangleGauche());
                    List<Triangle> trianglesValideeDroite = Triangle.copieProfonde(app.getTriangleControle().getListeTriangleDroite());

                    app.getPointControle().ajouterPointsBordure(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche());
                    app.getPointControle().ajouterPointsBordure(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite());
                    app.getTriangleControle().generateDelaunayTriangles();

                    trianglesValideeGauche = app.ajusterTrianglesAOriginal(app.getTriangleControle().getListeTriangleGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    trianglesValideeDroite = app.ajusterTrianglesAOriginal(app.getTriangleControle().getListeTriangleDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing3(trianglesValideeGauche, trianglesValideeDroite);
                    app.getImageViewer().actualiserVisionneuseImage();
                    break;
                case 4:
                    List<Segment> segmentsValideeGauche = app.adjusterSegmentsAOriginal(app.getSegmentControle().getSegmentsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    List<Segment> segmentsValideeDroite = app.adjusterSegmentsAOriginal(app.getSegmentControle().getSegmentsDroite(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing4(segmentsValideeGauche, segmentsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));
                    app.getImageViewer().actualiserVisionneuseImage();
                    break;
            }

            app.getImageViewer().actualiserVisionneuseImage();
            app.getImageLoader().actualiserEtatBoutons();
            app.getImageViewer().mettreAJourSlider();

            if (app.getChoixMethode() == 3) {
                app.getPointControle().enleverPointsBordure(app.getPointControle().getPointsGauche());
                app.getPointControle().enleverPointsBordure(app.getPointControle().getPointsDroite());
                app.getTriangleControle().generateDelaunayTriangles();
            }
        });

        miseJourControle(app.getChoixMethode());
    }

    /**
     * Met à jour les contrôles de l'application en fonction de la méthode sélectionnée.
     * 
     * @author Mattéo REYNE
     * @param methode Méthode de morphing sélectionnée
     */
    public void miseJourControle(int methode) {
        boxControle.getChildren().clear();

        switch (methode) {
            case 1:
                boxControle.getChildren().addAll(boutonAjouter, boutonSupprimer, new Label("Couleur des lignes :"), couleurLignes, boutonValider);
                break;
            case 2:
                if (couleurPoints == null) {
                    couleurPoints = new ColorPicker(Color.BLACK);
                    couleurPoints.setOnAction(e -> app.getTriangleControle().mettreAJourTrianglesEtDessiner());
                }
                if (finirForme == null) {
                    finirForme = new CheckBox("Finir forme");
                    finirForme.setSelected(false);
                    finirForme.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            CourbeBezier.finirForme(app.getPointControle().getPointsGauche(), app.getPointControle().getPointsDroite());
                            app.getImageLoader().actualiserEtatBoutons();
                        } else {
                            List<Point> pointsGauche = app.getPointControle().getPointsGauche();
                            List<Point> pointsDroite = app.getPointControle().getPointsDroite();
        
                            pointsGauche.remove(pointsGauche.size() - 1);
                            pointsDroite.remove(pointsDroite.size() - 1);
                            pointsGauche.remove(pointsGauche.size() - 1);
                            pointsDroite.remove(pointsDroite.size() - 1);
        
                            app.getPointControle().setPointsGauche(pointsGauche);
                            app.getPointControle().setPointsDroite(pointsDroite);
                        }
                        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
                        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());
                        app.getImageLoader().actualiserEtatBoutons();
                    });
                }
                boxControle.getChildren().addAll(boutonAjouter, boutonSupprimer, new Label("Couleur des lignes :"), couleurLignes, new Label("Couleur des points Béziers :"), couleurPoints, finirForme, boutonValider);
                break;
            case 3:
                if (afficherTrianglesCheckbox == null) {
                    afficherTrianglesCheckbox = new CheckBox("Afficher triangles");
                    afficherTrianglesCheckbox.setSelected(true);
                    afficherTrianglesCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
                        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());
                    });
                }
                boxControle.getChildren().addAll(boutonAjouter, boutonSupprimer, afficherTrianglesCheckbox, new Label("Couleur des lignes :"), couleurLignes, boutonValider);
                break;
            case 4:
                boxControle.getChildren().addAll(boutonAjouterSegment, boutonSupprimerSegment, boutonValider);
                break;
        }
    }

    /**
     * Retourne le conteneur pour les contrôles de l'application.
     * 
     * @author Mattéo REYNE
     * @return Le conteneur pour les contrôles de l'application
     */
    public VBox getBoxControle() {
        return boxControle;
    }

    /**
     * Retourne le bouton pour ajouter un segment.
     * 
     * @author Mattéo REYNE
     * @return Le bouton pour ajouter un segment
     */
    public Button getBoutonAjouterSegment() {
        return boutonAjouterSegment;
    }

    /**
     * Retourne le bouton pour supprimer un segment.
     * 
     * @author Mattéo REYNE
     * @return Le bouton pour supprimer un segment
     */
    public Button getBoutonSupprimerSegment() {
        return boutonSupprimerSegment;
    }

    /**
     * Retourne le bouton pour ajouter un point.
     * 
     * @author Mattéo REYNE
     * @return Le bouton pour ajouter un point
     */
    public Button getBoutonAjouter() {
        return boutonAjouter;
    }

    /**
     * Retourne le bouton pour supprimer un point.
     * 
     * @author Mattéo REYNE
     * @return Le bouton pour supprimer un point
     */
    public Button getBoutonSupprimer() {
        return boutonSupprimer;
    }

    /**
     * Retourne le bouton pour valider les opérations.
     * 
     * @author Mattéo REYNE
     * @return Le bouton pour valider les opérations
     */
    public Button getBoutonValider() {
        return boutonValider;
    }

    /**
     * Retourne la checkbox pour afficher les triangles.
     * 
     * @author Mattéo REYNE
     * @return La checkbox pour afficher les triangles
     */
    public CheckBox getAfficherTrianglesCheckbox() {
        return afficherTrianglesCheckbox;
    }

    /**
     * Retourne le sélecteur de couleur pour les lignes.
     * 
     * @author Mattéo REYNE
     * @return Le sélecteur de couleur pour les lignes
     */
    public ColorPicker getCouleurLignes() {
        return couleurLignes;
    }

    /**
     * Retourne le sélecteur de couleur pour les points.
     * 
     * @author Mattéo REYNE
     * @return Le sélecteur de couleur pour les points
     */
    public ColorPicker getCouleurPoints() {
        return couleurPoints;
    }

    /**
     * Retourne la checkbox pour finir la forme arrondie.
     * 
     * @author Mattéo REYNE
     * @return La checkbox pour finir la forme arrondie
     */
    public CheckBox getFinirForme() {
        return finirForme;
    }
}
