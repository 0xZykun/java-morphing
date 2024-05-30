/*
 * see import javafx.scene.canvas.Canvas;
 * import javafx.scene.canvas.GraphicsContext;
 * import javafx.scene.control.Alert;
 * import javafx.scene.control.Button;
 * import javafx.stage.FileChooser;
 * import javafx.scene.image.Image;
 * import javafx.scene.layout.BorderPane;
 * import javafx.scene.layout.HBox;
 * import javafx.application.Platform;
 * import javafx.geometry.Pos;
 * import javafx.scene.Scene;
 * 
 * see import java.io.File;
 */
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

/**
 * Classe ImageLoader pour gérer le chargement et la manipulation des images.
 * 
 */
public class ImageLoader {
    /**
     * Référence à l'application principale.
     */
    private MorphingFx app;

    /**
     * Canevas pour l'image de gauche.
     */
    private Canvas canevasGauche;

    /**
     * Canevas pour l'image de droite.
     */
    private Canvas canevasDroite;

    /**
     * Bouton pour importer une image à gauche.
     */
    private Button boutonImporterGauche;

    /**
     * Bouton pour importer une image à droite.
     */
    private Button boutonImporterDroite;

    /**
     * Bouton pour effacer l'image de gauche.
     */
    private Button boutonEffacerGauche;

    /**
     * Bouton pour effacer l'image de droite.
     */
    private Button boutonEffacerDroite;

    /**
     * Indicateur si une image est chargée à gauche.
     */
    private boolean imageChargeeGauche = false;

    /**
     * Indicateur si une image est chargée à droite.
     */
    private boolean imageChargeeDroite = false;

    /**
     * Image chargée à gauche.
     */
    private Image imageGauche;

    /**
     * Image chargée à droite.
     */
    private Image imageDroite;

    /**
     * Classe ImageLoader pour gérer le chargement et la manipulation des images.
     * 
     * @author Anthony GARCIA
     */
    public ImageLoader(MorphingFx app) {
        this.app = app;

        this.canevasGauche = new Canvas(app.getLargeurScene()/2.1, app.getHauteurScene()/2.1);
        this.canevasDroite = new Canvas(app.getLargeurScene()/2.1, app.getHauteurScene()/2.1);
    }

    /**
     * Crée les contrôles d'image pour l'interface utilisateur.
     * 
     * @author Anthony GARCIA
     * @param canevas le canevas sur lequel dessiner l'image
     * @param estGauche indique si le canevas est à gauche
     * @return un BorderPane contenant les contrôles d'image
     */
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
            app.getTriangleControle().generateDelaunayTriangles();
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

    /**
     * Crée les contrôles d'image pour l'interface utilisateur.
     * 
     * @author Anthony GARCIA
     * @param Chemin le chemin de l'image
     * @param index indique si le canevas est à gauche
     * @return un BorderPane contenant les contrôles d'image
     */
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

    /**
     * Charge une image et l'affiche sur le canevas spécifié.
     * 
     * @author Anthony GARCIA
     * @param canevas le canevas sur lequel dessiner l'image
     * @param cote indique si l'image est pour la gauche ou la droite
     */
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

    /**
     * Efface le canevas spécifié et réinitialise les contrôles.
     * 
     * @author Anthony GARCIA
     * @param cote indique si le canevas à effacer est à gauche ou à droite
     */
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

    /**
     * Initialise les boutons d'importation d'image.
     * 
     * @author Anthony GARCIA
     * @param scene la scène principale de l'application
     */
    public void initialiserBoutonsImage(Scene scene) {
        boutonImporterGauche.setOnAction(e -> chargerImage(canevasGauche, "gauche"));
        boutonImporterDroite.setOnAction(e -> chargerImage(canevasDroite, "droite"));
    }

    /**
     * Actualise l'état des boutons en fonction des images chargées.
     * 
     * @author Anthony GARCIA
     */
    public void actualiserEtatBoutons() {
        if (app.getChoixMethode() == 4) {
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

    /**
     * Crée une section pour afficher les images et les contrôles associés.
     * 
     * @author Anthony GARCIA
     * @return un HBox contenant les contrôles d'image et de contrôle
     */
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

    /**
     * Affiche une alerte à l'utilisateur.
     * 
     * @author Anthony GARCIA
     * @param titre le titre de l'alerte
     * @param contenu le contenu de l'alerte
     */
    private void montrerAlerte(String titre, String contenu) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.initOwner(app.getPrimaryStage());

        alerte.showAndWait();
    }

    /**
     * Retourne le canevas de gauche.
     * 
     * @author Anthony GARCIA
     * @return le canevas de gauche
     */
    public Canvas getCanevasGauche() {
        return canevasGauche;
    }

    /**
     * Retourne le canevas de droite.
     * 
     * @author Anthony GARCIA
     * @return le canevas de droite
     */
    public Canvas getCanevasDroite() {
        return canevasDroite;
    }

    /**
     * Indique si une image est chargée à gauche.
     * 
     * @author Anthony GARCIA
     * @return true si une image est chargée à gauche, sinon false
     */
    public boolean isImageChargeeGauche() {
        return imageChargeeGauche;
    }

    /**
     * Définit si une image est chargée à gauche.
     * 
     * @author Anthony GARCIA
     * @param imageChargeeGauche true si une image est chargée à gauche, sinon false
     */
    public void setImageChargeeGauche(boolean imageChargeeGauche) {
        this.imageChargeeGauche = imageChargeeGauche;
    }

    /**
     * Indique si une image est chargée à droite.
     * 
     * @author Anthony GARCIA
     * @return true si une image est chargée à droite, sinon false
     */
    public boolean isImageChargeeDroite() {
        return imageChargeeDroite;
    }

    /**
     * Définit si une image est chargée à droite.
     * 
     * @author Anthony GARCIA
     * @param imageChargeeDroite true si une image est chargée à droite, sinon false
     */
    public void setImageChargeeDroite(boolean imageChargeeDroite) {
        this.imageChargeeDroite = imageChargeeDroite;
    }

    /**
     * Retourne l'image de gauche.
     * 
     * @author Anthony GARCIA
     * @return l'image de gauche
     */
    public Image getImageGauche() {
        return imageGauche;
    }

    /**
     * Définit l'image de gauche.
     * 
     * @author Anthony GARCIA
     * @param imageGauche l'image de gauche
     */
    public void setImageGauche(Image imageGauche) {
        this.imageGauche = imageGauche;
    }

    /**
     * Retourne l'image de droite.
     * 
     * @author Anthony GARCIA
     * @return l'image de droite
     */
    public Image getImageDroite() {
        return imageDroite;
    }

    /**
     * Définit l'image de droite.
     * 
     * @author Anthony GARCIA
     * @param imageDroite l'image de droite
     */
    public void setImageDroite(Image imageDroite) {
        this.imageDroite = imageDroite;
    }
}