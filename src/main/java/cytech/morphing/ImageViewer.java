package cytech.morphing;

/*
 * see import javafx.scene.control.Button;
 * see import javafx.scene.control.Slider;
 * see import javafx.scene.image.ImageView;
 * see import javafx.scene.layout.BorderPane;
 * see import javafx.scene.layout.HBox;
 * see import javafx.scene.layout.VBox;
 * see import javafx.geometry.Pos;
 */
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

/**
 * Classe ImageViewer pour gérer la visionneuse d'images.
 * 
 */
public class ImageViewer {
    /**
     * Référence à l'application principale.
     */
    private MorphingFx app;

    /**
     * Visionneuse d'images.
     */
    private ImageView imageView;

    /**
     * Curseur pour naviguer entre les images.
     */
    private Slider slider;

    /**
     * Bouton pour aller à l'image précédente.
     */
    private Button boutonPrecedent;

    /**
     * Bouton pour aller à l'image suivante.
     */
    private Button boutonSuivant;

    /**
     * Constructeur pour initialiser ImageViewer avec l'application principale.
     * 
     * @autor Mattéo REYNE
     * @param app l'application principale MorphingFx
     */
    public ImageViewer(MorphingFx app) {
        this.app = app;
        imageView = new ImageView();
        slider = new Slider(0, 0, 0);
        boutonPrecedent = new Button("<");
        boutonSuivant = new Button(">");

        boutonPrecedent.setOnAction(e -> {
            if (app.getIndexEtapeCourante() > 0) {
                app.setIndexEtapeCourante(app.getIndexEtapeCourante() - 1);
                actualiserVisionneuseImage();
            }
        });
        boutonSuivant.setOnAction(e -> {
            if (app.getIndexEtapeCourante() < app.getImagesIntermediaires().size() - 1) {
                app.setIndexEtapeCourante(app.getIndexEtapeCourante() + 1);
                actualiserVisionneuseImage();
            }
        });
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            app.setIndexEtapeCourante(newVal.intValue());
            actualiserVisionneuseImage();
        });

        // Définir les dimensions de l'imageView dès le début
        redimensionnerImageView();
    }

    /**
     * Crée la visionneuse d'images pour l'interface utilisateur.
     * 
     * @autor Mattéo REYNE
     * @return un HBox contenant la visionneuse d'images
     */
    public HBox creerVisionneuseImage() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        borderPane.setLeft(boutonPrecedent);
        borderPane.setRight(boutonSuivant);
        BorderPane.setAlignment(imageView, Pos.CENTER);
        BorderPane.setAlignment(boutonPrecedent, Pos.CENTER);
        BorderPane.setAlignment(boutonSuivant, Pos.CENTER);

        VBox vBox = new VBox(10, borderPane, slider);
        vBox.setAlignment(Pos.CENTER);

        HBox root = new HBox(vBox);
        root.setPrefSize(app.getLargeurScene() / 2.2, app.getHauteurScene() / 2.4);
        root.setAlignment(Pos.CENTER);

        mettreAJourSlider();
        actualiserVisionneuseImage();

        return root;
    }

    /**
     * Met à jour le curseur de la visionneuse d'images.
     * 
     * @autor Mattéo REYNE
     */
    public void mettreAJourSlider() {
        int nombreImages = app.getImagesIntermediaires().size();
        if (nombreImages > 0) {
            slider.setMax(nombreImages - 1);
            slider.setValue(app.getIndexEtapeCourante());
        } else {
            slider.setMax(0);
            slider.setValue(0);
        }
    }

    /**
     * Actualise l'état des boutons de la visionneuse.
     * 
     * @autor Mattéo REYNE
     */
    public void actualiserEtatBoutonsVisionneuse() {
        boutonPrecedent.setDisable(app.getIndexEtapeCourante() == 0);
        boutonSuivant.setDisable(app.getIndexEtapeCourante() >= app.getImagesIntermediaires().size() - 1);
    }

    /**
     * Actualise la visionneuse d'images.
     * 
     * @autor Mattéo REYNE
     */
    public void actualiserVisionneuseImage() {
        if (!app.getImagesIntermediaires().isEmpty()) {
            Image image = app.getImagesIntermediaires().get(app.getIndexEtapeCourante()).genererImage().getImage();
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
        }
        redimensionnerImageView();
        actualiserEtatBoutonsVisionneuse();
        mettreAJourSlider();
    }

    /**
     * Redimensionne l'image en utilisant les dimensions de la scène.
     * 
     * @autor Mattéo REYNE
     */
    private void redimensionnerImageView() {
        imageView.setFitWidth(app.getLargeurScene() / 2.2);
        imageView.setFitHeight(app.getHauteurScene() / 2.4);
        imageView.setPreserveRatio(true);
    }

    /**
     * Retourne la visionneuse d'images.
     * 
     * @autor Mattéo REYNE
     * @return la visionneuse d'images
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Retourne le curseur de la visionneuse.
     * 
     * @autor Mattéo REYNE
     * @return le curseur de la visionneuse
     */
    public Slider getSlider() {
        return slider;
    }

    /**
     * Retourne le bouton pour aller à l'image précédente.
     * 
     * @autor Mattéo REYNE
     * @return le bouton pour aller à l'image précédente
     */
    public Button getBoutonPrecedent() {
        return boutonPrecedent;
    }

    /**
     * Retourne le bouton pour aller à l'image suivante.
     * 
     * @autor Mattéo REYNE
     * @return le bouton pour aller à l'image suivante
     */
    public Button getBoutonSuivant() {
        return boutonSuivant;
    }
}
