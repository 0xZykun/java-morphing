package cytech.morphing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SegmentControle {
    private MorphingFx app;
    private List<Segment> segmentsGauche = new ArrayList<>();
    private List<Segment> segmentsDroite = new ArrayList<>();
    private int indexSelectionne = -1;
    private Point pointTemp = null; // Temporarily stores the first point of a segment

    public SegmentControle(MorphingFx app) {
        this.app = app;
    }

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

    private void dessinerSegments(GraphicsContext gc, List<Segment> segments) {
        List<Color> couleurs = ColorGenerateur.genererCouleursDifferentes(15);

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

    public void supprimerSegment() {
        if (!segmentsDroite.isEmpty()) {
            segmentsDroite.remove(segmentsDroite.size() - 1);
            segmentsGauche.remove(segmentsGauche.size() - 1);

            redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), segmentsGauche, app.getImageLoader().getImageGauche());
            redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), segmentsDroite, app.getImageLoader().getImageDroite());
            app.getImageLoader().actualiserEtatBoutons();
        }
    }

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

    public void clearSegments() {
        segmentsGauche.clear();
        segmentsDroite.clear();

        redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), segmentsGauche, app.getImageLoader().getImageGauche());
        redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), segmentsDroite, app.getImageLoader().getImageDroite());
    }

    public List<Segment> getSegmentsGauche() {
        return segmentsGauche;
    }

    public void setSegmentsGauche(List<Segment> segmentsGauche) {
        this.segmentsGauche = segmentsGauche;
    }

    public List<Segment> getSegmentsDroite() {
        return segmentsDroite;
    }

    public void setSegmentsDroite(List<Segment> segmentsDroite) {
        this.segmentsDroite = segmentsDroite;
    }

    public void resetPointTemp() {
        pointTemp = null;
    }
}