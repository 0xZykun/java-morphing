package cytech.morphing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TriangleControle {
    private MorphingFx app;
    private List<Triangle> listeTriangleGauche = new ArrayList<>();
    private List<Triangle> listeTriangleDroite = new ArrayList<>();

    public TriangleControle(MorphingFx app) {
        this.app = app;
    }

    public void redessinerCanevas(Canvas canevas, List<Point> points, Image image) {
        GraphicsContext gc = canevas.getGraphicsContext2D();
        gc.clearRect(0, 0, canevas.getWidth(), canevas.getHeight());

        if (image != null) {
            double largeur = image.getWidth();
            double hauteur = image.getHeight();
            gc.drawImage(image, 0, 0, largeur, hauteur);
        }

        if (app.getChoixMethode() == 2 && points.size() >= 4) {
            CourbeBezier.dessinerCourbeBezier(gc, points, app.getControle().getCouleurLignes().getValue(), app.getControle().getFinirForme().isSelected());
        } else {
            if (points.size() > 1) {
                dessinerLignes(gc, points);
            }

            if (app.getChoixMethode() == 3 && app.getControle().getAfficherTrianglesCheckbox().isSelected() && points.size() > 1) {
                gc.setStroke(Color.CYAN);
                List<Triangle> triangles = (canevas == app.getImageLoader().getCanevasGauche()) ? listeTriangleGauche : listeTriangleDroite;
                for (Triangle triangle : triangles) {
                    if (!estTriangleCoin(triangle, canevas.getWidth(), canevas.getHeight())) {
                        Point a = triangle.getA();
                        Point b = triangle.getB();
                        Point c = triangle.getC();
                        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
                        gc.strokeLine(b.getX(), b.getY(), c.getX(), c.getY());
                        gc.strokeLine(c.getX(), c.getY(), a.getX(), a.getY());
                    }
                }
            }
        }

        dessinerPoints(gc, points);
    }

    private void dessinerLignes(GraphicsContext gc, List<Point> points) {
        Point pointPrecedent = null;
        Color selectedColor = app.getControle().getCouleurLignes().getValue();

        for (Point point : points) {
            if (pointPrecedent != null) {
                gc.setStroke(selectedColor);
                gc.strokeLine(pointPrecedent.getX(), pointPrecedent.getY(), point.getX(), point.getY());
            }
            pointPrecedent = point;
        }

        if (!points.isEmpty() && points.size() > 1) {
            Point premierPoint = points.get(0);
            Point dernierPoint = points.get(points.size() - 1);
            gc.setStroke(selectedColor);
            gc.strokeLine(dernierPoint.getX(), dernierPoint.getY(), premierPoint.getX(), premierPoint.getY());
        }
    }

    private void dessinerPoints(GraphicsContext gc, List<Point> points) {
        List<Color> couleurs = ColorGenerateur.genererCouleursDifferentes(15);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Color couleur;
            if ((i % 3 == 1 || i % 3 == 2) && app.getChoixMethode() == 2) {
                couleur = app.getControle().getCouleurPoints().getValue();
            } else {
                couleur = couleurs.get(i / 3 % couleurs.size());
            }

            gc.setFill(couleur);
            gc.fillOval(point.getX() - 5, point.getY() - 5, 10, 10);
        }
    }

    public void mettreAJourTrianglesEtDessiner() {
        if (app.getPointControle().getPointsGauche().size() >= 3) {
            generateDelaunayTriangles();
        }

        redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());
    }

    public void generateDelaunayTriangles() {
        listeTriangleDroite.clear();
        listeTriangleGauche.clear();

        if (app.getPointControle().getPointsGauche().size() < 3 || app.getPointControle().getPointsDroite().size() < 3) {
            return;
        }

        for (int i = 0; i < app.getPointControle().getPointsGauche().size(); i++) {
            for (int j = i + 1; j < app.getPointControle().getPointsGauche().size(); j++) {
                for (int k = j + 1; k < app.getPointControle().getPointsGauche().size(); k++) {
                    Point a = app.getPointControle().getPointsGauche().get(i);
                    Point b = app.getPointControle().getPointsGauche().get(j);
                    Point c = app.getPointControle().getPointsGauche().get(k);
                    Triangle newTriangle = new Triangle(a, b, c);

                    Point a2 = app.getPointControle().getPointsDroite().get(i);
                    Point b2 = app.getPointControle().getPointsDroite().get(j);
                    Point c2 = app.getPointControle().getPointsDroite().get(k);
                    Triangle newTriangle2 = new Triangle(a2, b2, c2);

                    boolean bonTriangle = true;
                    for (int l = 0; l < app.getPointControle().getPointsGauche().size(); l++) {
                        Point d = app.getPointControle().getPointsGauche().get(l);
                        if (newTriangle.cercleContientPoint(d)) {
                            bonTriangle = false;
                            break;
                        }
                    }

                    if (bonTriangle) {
                        listeTriangleGauche.add(newTriangle);
                        listeTriangleDroite.add(newTriangle2);
                    }
                }
            }
        }
    }

    private boolean estTriangleCoin(Triangle triangle, double width, double height) {
        return (triangle.getA().estAuCoin((int) width, (int) height) ||
                triangle.getB().estAuCoin((int) width, (int) height) ||
                triangle.getC().estAuCoin((int) width, (int) height));
    }

    public List<Triangle> getListeTriangleGauche() {
        return listeTriangleGauche;
    }

    public void setListeTriangleGauche(List<Triangle> listeTriangleGauche) {
        this.listeTriangleGauche = listeTriangleGauche;
    }

    public List<Triangle> getListeTriangleDroite() {
        return listeTriangleDroite;
    }

    public void setListeTriangleDroite(List<Triangle> listeTriangleDroite) {
        this.listeTriangleDroite = listeTriangleDroite;
    }
}