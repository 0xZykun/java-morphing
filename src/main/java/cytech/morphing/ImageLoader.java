package cytech.morphing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.io.File;

public class ImageLoader {
    private MorphingFx app;
    private Canvas canevasGauche, canevasDroite;
    private Button boutonImporterGauche, boutonImporterDroite, boutonEffacerGauche, boutonEffacerDroite;
    private boolean imageChargeeGauche = false;
    private boolean imageChargeeDroite = false;
    private Image imageGauche, imageDroite;

    public ImageLoader(MorphingFx app) {
        this.app = app;

        this.canevasGauche = new Canvas(app.getLargeurScene()/2.1, app.getHauteurScene()/2.1);
        this.canevasDroite = new Canvas(app.getLargeurScene()/2.1, app.getHauteurScene()/2.1);
    }

    public BorderPane creerControleImage(Canvas canevas, boolean estGauche) {
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
            app.getPointControle().clearPoints();
            app.getTriangleControle().redessinerCanevas(canevasGauche, app.getPointControle().getPointsGauche(), imageGauche);
            app.getTriangleControle().redessinerCanevas(canevasDroite, app.getPointControle().getPointsDroite(), imageDroite);
            actualiserEtatBoutons();

            if (!imageChargeeDroite && !imageChargeeGauche) {
                app.setLargeurImageOriginale(0);
                app.setHauteurImageOriginale(0);
            }
        });

        HBox hBox = new HBox(10, boutonImporter, boutonEffacer);
        hBox.setAlignment(Pos.CENTER);

        panneauBord.setTop(hBox);
        panneauBord.setCenter(canevas);

        if (app.getChoixMethode() == 4) {
            if (estGauche) {
                app.getSegmentControle().configurerCanevasSegment(canevas, app.getSegmentControle().getSegmentsGauche(), canevasDroite, app.getSegmentControle().getSegmentsDroite());
            } else {
                app.getSegmentControle().configurerCanevasSegment(canevasDroite, app.getSegmentControle().getSegmentsDroite(), canevas, app.getSegmentControle().getSegmentsGauche());
            }
        } else {
            if (estGauche) {
                app.getPointControle().configurerCanevas(canevas, app.getPointControle().getPointsGauche(), canevasDroite, app.getPointControle().getPointsDroite());
            } else {
                app.getPointControle().configurerCanevas(canevasDroite, app.getPointControle().getPointsDroite(), canevas, app.getPointControle().getPointsGauche());
            }
        }
        return panneauBord;
    }

    public void chargerImage(String chemin, int index) {
        Image imageTemporaire = new Image(chemin, false);
        ImageBit nouvelleImageBit = new ImageBit(chemin);
        double ratio = imageTemporaire.getWidth() / imageTemporaire.getHeight();
        double largeurMaxImage = app.getLargeurScene() / 2.1;
        double hauteurMaxImage = app.getHauteurScene() / 2.1;
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
    
            BorderPane panneauBord = new BorderPane();
 
            if (index == 0) {
                canevasGauche.setWidth(nouvelleLargeur);
                canevasGauche.setHeight(nouvelleHauteur);
    
                imageGauche = new Image(chemin, nouvelleLargeur, nouvelleHauteur, true, true);
                GraphicsContext gc = canevasGauche.getGraphicsContext2D();
                gc.clearRect(0, 0, canevasGauche.getWidth(), canevasGauche.getHeight());
                gc.drawImage(imageGauche, 0, 0);
    
                imageChargeeGauche = true;
                if (!app.getImagesOrigines().isEmpty()) {
                    app.getImagesOrigines().remove(0);
                }
                app.getImagesOrigines().add(0, nouvelleImageBit);
                panneauBord = (BorderPane) canevasGauche.getParent();
            } else {
                canevasDroite.setWidth(nouvelleLargeur);
                canevasDroite.setHeight(nouvelleHauteur);
    
                imageDroite = new Image(chemin, nouvelleLargeur, nouvelleHauteur, true, true);
                GraphicsContext gc = canevasDroite.getGraphicsContext2D();
                gc.clearRect(0, 0, canevasDroite.getWidth(), canevasDroite.getHeight());
                gc.drawImage(imageDroite, 0, 0);
    
                imageChargeeDroite = true;
                if (app.getImagesOrigines().size() > 1) {
                    app.getImagesOrigines().set(1, nouvelleImageBit);
                } else {
                    app.getImagesOrigines().add(nouvelleImageBit);
                }
                panneauBord = (BorderPane) canevasDroite.getParent();
            }
    
            panneauBord.setPrefSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
            panneauBord.setMaxSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
    
            Platform.runLater(panneauBord::layout);
    
            actualiserEtatBoutons();
        }
    }    

    public void chargerImage(Canvas canevas, String cote) {
        FileChooser selecteurFichier = new FileChooser();
        File fichier = selecteurFichier.showOpenDialog(null);
        if (fichier != null) {
            Image imageTemporaire = new Image(fichier.toURI().toString(), false);
            ImageBit nouvelleImageBit = new ImageBit(fichier.toURI().toString());
            double ratio = imageTemporaire.getWidth() / imageTemporaire.getHeight();
            double largeurMaxImage = app.getLargeurScene() / 2.1;
            double hauteurMaxImage = app.getHauteurScene() / 2.1;
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

                Image image = new Image(fichier.toURI().toString(), nouvelleLargeur, nouvelleHauteur, true, true);
                GraphicsContext gc = canevas.getGraphicsContext2D();
                gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());
                gc.drawImage(image, 0, 0);

                if ("gauche".equals(cote)) {
                    imageGauche = image;
                    imageChargeeGauche = true;
                    if (!app.getImagesOrigines().isEmpty()) {
                        app.getImagesOrigines().remove(0);
                    }
                    app.getImagesOrigines().add(0, nouvelleImageBit);
                } else {
                    imageDroite = image;
                    imageChargeeDroite = true;
                    if (app.getImagesOrigines().size() > 1) {
                        app.getImagesOrigines().set(1, nouvelleImageBit);
                    } else {
                        app.getImagesOrigines().add(nouvelleImageBit);
                    }
                }

                BorderPane panneauBord = (BorderPane) canevas.getParent();
                panneauBord.setPrefSize(nouvelleLargeur + 20, nouvelleHauteur + 40);
                panneauBord.setMaxSize(nouvelleLargeur + 20, nouvelleHauteur + 40);

                Platform.runLater(() -> {
                    panneauBord.layout();
                });

                actualiserEtatBoutons();
            } else {
                montrerAlerte("Erreur", "Veuillez charger des images de même taille initiale : " +
                        (int) app.getLargeurImageOriginale() + "x" + (int) app.getHauteurImageOriginale());
            }
        }
    }

    public void effacerCanevas(String cote) {
        app.getPointControle().clearPoints();
        if ("gauche".equals(cote)) {
            GraphicsContext gc = app.getImageLoader().getCanevasGauche().getGraphicsContext2D();
            gc.clearRect(0, 0, app.getImageLoader().getCanevasGauche().getWidth(), app.getImageLoader().getCanevasGauche().getHeight());
            imageChargeeGauche = false;
            imageGauche = null;
        } else {
            GraphicsContext gc = app.getImageLoader().getCanevasDroite().getGraphicsContext2D();
            gc.clearRect(0, 0, app.getImageLoader().getCanevasDroite().getWidth(), app.getImageLoader().getCanevasDroite().getHeight());
            imageChargeeDroite = false;
            imageDroite = null;
        }
        actualiserEtatBoutons();
    }

    public void initialiserBoutonsImage(Scene scene) {
        boutonImporterGauche.setOnAction(e -> chargerImage(canevasGauche, "gauche"));
        boutonImporterDroite.setOnAction(e -> chargerImage(canevasDroite, "droite"));
    }

    public void actualiserEtatBoutons() {
        if (app.getChoixMethode() == 4) {
            System.out.println(app.getSegmentControle().getSegmentsDroite());
            app.getControle().getBoutonAjouterSegment().setDisable(!(imageChargeeGauche && imageChargeeDroite));
            app.getControle().getBoutonSupprimerSegment().setDisable(app.getSegmentControle().getSegmentsDroite().isEmpty());
            app.getControle().getBoutonValider().setDisable(app.getSegmentControle().getSegmentsDroite().isEmpty());
        } else {
            if (app.getChoixMethode() == 2) {
                app.getControle().getBoutonAjouter().setDisable(!(imageChargeeGauche && imageChargeeDroite) || app.getControle().getFinirForme().isSelected());
                app.getControle().getBoutonSupprimer().setDisable(app.getPointControle().getPointsGauche().isEmpty() || app.getControle().getFinirForme().isSelected());
                app.getControle().getFinirForme().setDisable(!(imageChargeeGauche && imageChargeeDroite) || app.getPointControle().getPointsGauche().size() < 4);
                app.getControle().getBoutonValider().setDisable((app.getPointControle().getPointsGauche().size() < 4) || !app.getControle().getFinirForme().isSelected());
            } else {
                app.getControle().getAfficherTrianglesCheckbox().setDisable(!(imageChargeeGauche && imageChargeeDroite) && (app.getPointControle().getPointsGauche().size() < 3));
                app.getControle().getBoutonAjouter().setDisable(!(imageChargeeGauche && imageChargeeDroite));
                app.getControle().getBoutonSupprimer().setDisable(app.getPointControle().getPointsGauche().isEmpty());
                app.getControle().getBoutonValider().setDisable(app.getPointControle().getPointsGauche().isEmpty());
            }
        }
        boutonImporterGauche.setDisable(imageChargeeGauche);
        boutonImporterDroite.setDisable(imageChargeeDroite);
        boutonEffacerGauche.setDisable(!imageChargeeGauche);
        boutonEffacerDroite.setDisable(!imageChargeeDroite);
    }

    public HBox creerSectionImage() {
        HBox sectionImage = new HBox(10);
        sectionImage.getChildren().addAll(
            creerControleImage(canevasGauche, true), 
            app.getControle().getBoxControle(), 
            creerControleImage(canevasDroite, false)
        );

        sectionImage.setAlignment(Pos.CENTER);
        return sectionImage;
    }

    private void montrerAlerte(String titre, String contenu) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.initOwner(app.getPrimaryStage());

        alerte.showAndWait();
    }

    public Canvas getCanevasGauche() {
        return canevasGauche;
    }

    public Canvas getCanevasDroite() {
        return canevasDroite;
    }

    public boolean isImageChargeeGauche() {
        return imageChargeeGauche;
    }

    public void setImageChargeeGauche(boolean imageChargeeGauche) {
        this.imageChargeeGauche = imageChargeeGauche;
    }

    public boolean isImageChargeeDroite() {
        return imageChargeeDroite;
    }

    public void setImageChargeeDroite(boolean imageChargeeDroite) {
        this.imageChargeeDroite = imageChargeeDroite;
    }

    public Image getImageGauche() {
        return imageGauche;
    }

    public void setImageGauche(Image imageGauche) {
        this.imageGauche = imageGauche;
    }

    public Image getImageDroite() {
        return imageDroite;
    }

    public void setImageDroite(Image imageDroite) {
        this.imageDroite = imageDroite;
    }
}