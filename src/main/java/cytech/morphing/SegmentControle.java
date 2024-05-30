package cytech.morphing;

/*
 * see import javafx.scene.canvas.Canvas;
 * see import javafx.scene.canvas.GraphicsContext;
 * see import javafx.scene.image.Image;
 * see import javafx.scene.input.MouseButton;
 * see import javafx.scene.paint.Color;
 *
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe SegmentControle pour gérer les segments sur les canevas.
 * 
 * @autor Ruben PETTENG NGONGANG
 */
public class SegmentControle {
    /**
     * Référence à l'application principale.
     */
    private MorphingFx app;

    /**
     * Liste des segments sur le canevas gauche.
     */
    private List<Segment> segmentsGauche = new ArrayList<>();

    /**
     * Liste des segments sur le canevas droit.
     */
    private List<Segment> segmentsDroite = new ArrayList<>();

    /**
     * Index du segment sélectionné.
     */
    private int indexSelectionne = -1;

    /**
     * Point temporaire pour stocker le premier point d'un segment.
     */
    private Point pointTemp = null;

    /**
     * Constructeur pour initialiser SegmentControle avec l'application principale.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param app l'application principale MorphingFx
     */
    public SegmentControle(MorphingFx app) {
        this.app = app;
    }

    /**
     * Configure les événements de souris pour les segments sur le canevas.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param canevas le canevas à configurer
     * @param segments liste des segments pour ce canevas
     * @param canevasOppose le canevas opposé à configurer
     * @param segmentsOppose liste des segments pour le canevas opposé
     */
    public void configurerCanevasSegment(Canvas canevas, List<Segment> segments, Canvas canevasOppose, List<Segment> segmentsOppose) {
        canevas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                indexSelectionne = trouverIndexPointSegment(e.getX(), e.getY(), segments);
                if (indexSelectionne != -1) {
                    canevas.setOnMouseDragged(event -> {
                        Point nouveauPoint = new Point(
                                Math.min(Math.max(event.getX(), 0), canevas.getWidth()),
                                Math.min(Math.max(event.getY(), 0), canevas.getHeight())
                        );
                        if (indexSelectionne >= 0 && indexSelectionne < 2 * segmentsGauche.size()) {
                            Segment nouveauSegment = segments.get(indexSelectionne / 2);

                            if (indexSelectionne % 2 == 0) {
                                nouveauSegment.setA(nouveauPoint);
                            } else {
                                nouveauSegment.setB(nouveauPoint);
                            }
                            segments.set(indexSelectionne / 2, nouveauSegment);
                        }
                        redessinerCanevasSegments(canevas, segments, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
                    });
                }
            } else {
                if (e.getButton() == MouseButton.SECONDARY) {
                    if (app.getImageLoader().isImageChargeeGauche() && app.getImageLoader().isImageChargeeDroite()) {
                        if (pointTemp == null) {
                            pointTemp = new Point(e.getX(), e.getY());
                        } else {
                            Point secondPoint = new Point(e.getX(), e.getY());
                            segments.add(new Segment(pointTemp, secondPoint));
                            segmentsOppose.add(new Segment(pointTemp, secondPoint));
                            pointTemp = null;
                            app.getImageLoader().actualiserEtatBoutons();
                            redessinerCanevasSegments(canevas, segments, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
                            redessinerCanevasSegments(canevasOppose, segmentsOppose, canevasOppose == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
                        }
                    }
                }
            }
        });

        canevas.setOnMouseReleased(e -> {
            canevas.setOnMouseDragged(null);
            redessinerCanevasSegments(canevas, segments, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
        });
    }

    /**
     * Redessine les segments sur le canevas.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param canevas le canevas à redessiner
     * @param segments liste des segments à dessiner
     * @param image image de fond à dessiner
     */
    public void redessinerCanevasSegments(Canvas canevas, List<Segment> segments, Image image) {
        GraphicsContext gc = canevas.getGraphicsContext2D();
        gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());

        if (image != null) {
            double largeur = image.getWidth();
            double hauteur = image.getHeight();
            gc.drawImage(image, 0, 0, largeur, hauteur);
        }

        dessinerSegments(gc, segments);
    }

    /**
     * Dessine les segments sur le GraphicsContext.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param gc le GraphicsContext sur lequel dessiner
     * @param segments liste des segments à dessiner
     */
    private void dessinerSegments(GraphicsContext gc, List<Segment> segments) {
        List<Color> couleurs = CouleurGenerateur.genererCouleursDifferentes(15);

        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            Point a = segment.getA();
            Point b = segment.getB();
            Color couleurSegment = couleurs.get(i);

            gc.setStroke(couleurSegment);
            gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());

            gc.setFill(Color.GREEN);
            gc.fillOval(a.getX() - 5, a.getY() - 5, 10, 10);

            gc.setFill(Color.RED);
            gc.fillOval(b.getX() - 5, b.getY() - 5, 10, 10);
        }
    }

    /**
     * Ajoute un nouveau segment.
     * 
     * @autor Ruben PETTENG NGONGANG
     */
    public void ajouterSegment() {
        double largeur = app.getImageLoader().getCanevasGauche().getWidth();
        double hauteur = app.getImageLoader().getCanevasGauche().getHeight();
        
        Point p1 = new Point(0.1 * largeur, 0.1 * hauteur);
        Point p2 = new Point(0.1 * largeur, 0.9 * hauteur);
        segmentsDroite.add(new Segment(p1, p2));

        Point p3 = new Point(0.1 * largeur, 0.1 * hauteur);
        Point p4 = new Point(0.1 * largeur, 0.9 * hauteur);
        segmentsGauche.add(new Segment(p3, p4));

        redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), segmentsGauche, app.getImageLoader().getImageGauche());
        redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), segmentsDroite, app.getImageLoader().getImageDroite());
        app.getImageLoader().actualiserEtatBoutons();
    }

    /**
     * Supprime le dernier segment ajouté.
     * 
     * @autor Ruben PETTENG NGONGANG
     */
    public void supprimerSegment() {
        if (!segmentsDroite.isEmpty()) {
            segmentsDroite.remove(segmentsDroite.size() - 1);
            segmentsGauche.remove(segmentsGauche.size() - 1);

            redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), segmentsGauche, app.getImageLoader().getImageGauche());
            redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), segmentsDroite, app.getImageLoader().getImageDroite());
            app.getImageLoader().actualiserEtatBoutons();
        }
    }

    /**
     * Trouve l'index du point de segment le plus proche des coordonnées spécifiées.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param x coordonnée x à vérifier
     * @param y coordonnée y à vérifier
     * @param segments liste des segments
     * @return l'index du point de segment le plus proche, ou -1 si aucun point n'est trouvé
     */
    private int trouverIndexPointSegment(double x, double y, List<Segment> segments) {
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            if (segment.getA().distance(x, y) < 5) {
                return 2 * i;
            } else {
                if (segment.getB().distance(x, y) < 5) {
                    return 2 * i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Efface tous les segments des canevas.
     * 
     * @autor Ruben PETTENG NGONGANG
     */
    public void clearSegments() {
        segmentsGauche.clear();
        segmentsDroite.clear();

        redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), segmentsGauche, app.getImageLoader().getImageGauche());
        redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), segmentsDroite, app.getImageLoader().getImageDroite());
    }

    /**
     * Retourne la liste des segments du canevas gauche.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @return liste des segments du canevas gauche
     */
    public List<Segment> getSegmentsGauche() {
        return segmentsGauche;
    }

    /**
     * Définit la liste des segments du canevas gauche.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param segmentsGauche nouvelle liste des segments du canevas gauche
     */
    public void setSegmentsGauche(List<Segment> segmentsGauche) {
        this.segmentsGauche = segmentsGauche;
    }

    /**
     * Retourne la liste des segments du canevas droit.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @return liste des segments du canevas droit
     */
    public List<Segment> getSegmentsDroite() {
        return segmentsDroite;
    }

    /**
     * Définit la liste des segments du canevas droit.
     * 
     * @autor Ruben PETTENG NGONGANG
     * @param segmentsDroite nouvelle liste des segments du canevas droit
     */
    public void setSegmentsDroite(List<Segment> segmentsDroite) {
        this.segmentsDroite = segmentsDroite;
    }

    /**
     * Réinitialise le point temporaire utilisé pour créer un segment.
     * 
     * @autor Ruben PETTENG NGONGANG
     */
    public void resetPointTemp() {
        pointTemp = null;
    }
}