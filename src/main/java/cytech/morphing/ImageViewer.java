package cytech.morphing;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ImageViewer {
    private MorphingFx app;
    private ImageView imageView;
    private Slider slider;
    private Button boutonPrecedent;
    private Button boutonSuivant;

    public ImageViewer(MorphingFx app) {
        this.app = app;
        imageView = new ImageView();
        slider = new Slider(0, 0, 0);
        boutonPrecedent = new Button("<");
        boutonSuivant = new Button(">");
    }

    public HBox creerVisionneuseImage() {
        BorderPane visionneuse = new BorderPane();

        boutonPrecedent.setOnAction(e -> {
            if (app.getIndexEtapeCourante() > 0) {
                app.setIndexEtapeCourante(app.getIndexEtapeCourante() - 1);
                imageView.setImage(app.getImagesIntermediaires().get(app.getIndexEtapeCourante()).genererImage().getImage());
                slider.setValue(app.getIndexEtapeCourante());
                actualiserEtatBoutonsVisionneuse();
            }
        });

        boutonSuivant.setOnAction(e -> {
            if (app.getIndexEtapeCourante() < app.getImagesIntermediaires().size() - 1) {
                app.setIndexEtapeCourante(app.getIndexEtapeCourante() + 1);
                imageView.setImage(app.getImagesIntermediaires().get(app.getIndexEtapeCourante()).genererImage().getImage());
                slider.setValue(app.getIndexEtapeCourante());
                actualiserEtatBoutonsVisionneuse();
            }
        });

        imageView.setFitWidth(app.getLargeurScene() / 2.1);
        imageView.setFitHeight(app.getHauteurScene() / 2.1);
        imageView.setPreserveRatio(true);

        VBox boxLeft = new VBox(boutonPrecedent);
        boxLeft.setAlignment(Pos.CENTER);
        VBox boxRight = new VBox(boutonSuivant);
        boxRight.setAlignment(Pos.CENTER);

        visionneuse.setLeft(boxLeft);
        visionneuse.setCenter(imageView);
        visionneuse.setRight(boxRight);

        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);

        slider.maxWidthProperty().bind(imageView.fitWidthProperty());

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!app.getImagesIntermediaires().isEmpty()) {
                app.setIndexEtapeCourante(newValue.intValue());
                imageView.setImage(app.getImagesIntermediaires().get(app.getIndexEtapeCourante()).genererImage().getImage());
                actualiserEtatBoutonsVisionneuse();
            }
        });

        VBox boxBottom = new VBox(slider);
        boxBottom.setAlignment(Pos.CENTER);
        visionneuse.setBottom(boxBottom);

        mettreAJourSlider();
    
        HBox conteneur = new HBox(1);
        conteneur.getChildren().add(visionneuse);
        conteneur.setAlignment(Pos.CENTER);
    
        return conteneur;
    }

    public void mettreAJourSlider() {
        if (!app.getImagesIntermediaires().isEmpty()) {
            slider.setMax(app.getImagesIntermediaires().size() - 1);
            slider.setValue(0);
        } else {
            slider.setMax(0);
            slider.setValue(0);
        }
        actualiserEtatBoutonsVisionneuse();
    }

    public void actualiserEtatBoutonsVisionneuse() {
        boutonPrecedent.setDisable(app.getIndexEtapeCourante() == 0);
        boutonSuivant.setDisable(app.getIndexEtapeCourante() >= app.getImagesIntermediaires().size() - 1);
    }

    public void actualiserVisionneuseImage() {
        if (!app.getImagesIntermediaires().isEmpty()) {
            app.setIndexEtapeCourante(0);
            imageView.setImage(app.getImagesIntermediaires().get(0).genererImage().getImage());
        }
        mettreAJourSlider();
        actualiserEtatBoutonsVisionneuse();
    }

    // Getters and Setters
    public ImageView getImageView() {
        return imageView;
    }

    public Slider getSlider() {
        return slider;
    }

    public Button getBoutonPrecedent() {
        return boutonPrecedent;
    }

    public Button getBoutonSuivant() {
        return boutonSuivant;
    }
}