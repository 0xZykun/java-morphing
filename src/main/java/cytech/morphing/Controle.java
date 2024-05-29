package cytech.morphing;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Controle {
    private MorphingFx app;
    private VBox boxControle;
    private Button boutonAjouter;
    private Button boutonAjouterSegment;
    private Button boutonSupprimer;
    private Button boutonSupprimerSegment;
    private Button boutonValider;
    private CheckBox afficherTrianglesCheckbox;
    private CheckBox finirForme;
    private ColorPicker couleurLignes, couleurPoints;

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
                    pointsValideeGauche = Point.deepCopy(app.getPointControle().getPointsGauche());
                    pointsValideeDroite = Point.deepCopy(app.getPointControle().getPointsDroite());

                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing1(pointsValideeGauche, pointsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));                    
                    break;
                case 2:
                    pointsValideeGauche = Point.deepCopy(app.getPointControle().getPointsGauche());
                    pointsValideeDroite = Point.deepCopy(app.getPointControle().getPointsDroite());

                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing2(pointsValideeGauche, pointsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));    
                    break;
                case 3:
                    pointsValideeGauche = app.ajusterPointsAOriginal(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    pointsValideeDroite = app.ajusterPointsAOriginal(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    List<Triangle> trianglesValideeGauche = Triangle.deepCopy(app.getTriangleControle().getListeTriangleGauche());
                    List<Triangle> trianglesValideeDroite = Triangle.deepCopy(app.getTriangleControle().getListeTriangleDroite());

                    app.getPointControle().ajouterPointsBordure(app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasGauche());
                    app.getPointControle().ajouterPointsBordure(app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasDroite());
                    app.getTriangleControle().generateDelaunayTriangles();

                    trianglesValideeGauche = app.ajusterTrianglesAOriginal(app.getTriangleControle().getListeTriangleGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    trianglesValideeDroite = app.ajusterTrianglesAOriginal(app.getTriangleControle().getListeTriangleDroite(), app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing3(trianglesValideeGauche, trianglesValideeDroite);
                    break;
                case 4:
                    List<Segment> segmentsValideeGauche = app.adjusterSegmentsAOriginal(app.getSegmentControle().getSegmentsGauche(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());
                    List<Segment> segmentsValideeDroite = app.adjusterSegmentsAOriginal(app.getSegmentControle().getSegmentsDroite(), app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight(), app.getLargeurImageOriginale(), app.getHauteurImageOriginale());

                    app.getMorphingTask().morphing4(segmentsValideeGauche, segmentsValideeDroite, app.getImagesOrigines().get(0), app.getImagesOrigines().get(1));
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

    public VBox getBoxControle() {
        return boxControle;
    }

    public Button getBoutonAjouterSegment() {
        return boutonAjouterSegment;
    }

    public Button getBoutonSupprimerSegment() {
        return boutonSupprimerSegment;
    }

    public Button getBoutonAjouter() {
        return boutonAjouter;
    }

    public Button getBoutonSupprimer() {
        return boutonSupprimer;
    }

    public Button getBoutonValider() {
        return boutonValider;
    }

    public CheckBox getAfficherTrianglesCheckbox() {
        return afficherTrianglesCheckbox;
    }

    public ColorPicker getCouleurLignes() {
        return couleurLignes;
    }

    public ColorPicker getCouleurPoints() {
        return couleurPoints;
    }

    public CheckBox getFinirForme() {
        return finirForme;
    }
}