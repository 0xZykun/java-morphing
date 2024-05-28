package cytech.morphing;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class PointControle {
    private MorphingFx app;
    private List<Point> pointsGauche = new ArrayList<>();
    private List<Point> pointsDroite = new ArrayList<>();
    private int indexSelectionne = -1;

    public PointControle(MorphingFx app) {
        this.app = app;
    }

    public void configurerCanevas(Canvas canevas, List<Point> points, Canvas canevasOppose, List<Point> pointsOppose) {
        canevas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                indexSelectionne = trouverIndexPoint(e.getX(), e.getY(), points);
                if (indexSelectionne == -1) {
                    return;
                } else {
                    canevas.setOnMouseDragged(event -> {
                        Point nouveauPoint = new Point(
                                Math.min(Math.max(event.getX(), 0), canevas.getWidth()),
                                Math.min(Math.max(event.getY(), 0), canevas.getHeight())
                        );
                        if (indexSelectionne >= 0 && indexSelectionne < points.size()) {
                            points.set(indexSelectionne, nouveauPoint);
                        }
                        app.getTriangleControle().redessinerCanevas(canevas, points, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
                    });
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                if (app.getImageLoader().isImageChargeeGauche() && app.getImageLoader().isImageChargeeDroite() && (app.getChoixMethode() != 2 || !app.getControle().getFinirForme().isSelected())) {
                    ajouterPoint(e.getX(), e.getY(), points, pointsOppose, canevas, canevasOppose);
                }
            }
        });
    
        canevas.setOnMouseReleased(e -> {
            canevas.setOnMouseDragged(null);
            app.getTriangleControle().redessinerCanevas(canevas, points, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
            if (app.getChoixMethode() == 3) {
                app.getTriangleControle().mettreAJourTrianglesEtDessiner();
            }
            indexSelectionne = -1;
        });
    }

    private void ajouterPoint(double x, double y, List<Point> points, List<Point> pointsOppose, Canvas canevas, Canvas canevasOppose) {
        Point point = new Point(x, y);

        if (points.isEmpty() || app.getChoixMethode() != 2) {
            points.add(point);
            pointsOppose.add(point);
        } else {
            Point lastPoint = points.get(points.size() - 1);
            Point controle1 = new Point(
            lastPoint.getX() * 2 / 3 + point.getX() / 3,
            lastPoint.getY() * 2 / 3 + point.getY() / 3
            );
            Point controle2 = new Point(
                lastPoint.getX() / 3 + point.getX() * 2 / 3,
                lastPoint.getY() / 3 + point.getY() * 2 / 3
            );

            points.add(controle1);
            points.add(controle2);
            points.add(point);

            pointsOppose.add(controle1);
            pointsOppose.add(controle2);
            pointsOppose.add(point);
        }

        app.getTriangleControle().redessinerCanevas(canevas, points, canevas == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
        app.getTriangleControle().redessinerCanevas(canevasOppose, pointsOppose, canevasOppose == app.getImageLoader().getCanevasGauche() ? app.getImageLoader().getImageGauche() : app.getImageLoader().getImageDroite());
        app.getImageLoader().actualiserEtatBoutons();
        if (app.getChoixMethode() == 3) {
            app.getTriangleControle().mettreAJourTrianglesEtDessiner();
        }
    }

    private int trouverIndexPoint(double x, double y, List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.distance(x, y) < 5) {
                return i;
            }
        }
        return -1;
    }

    public void clearPoints() {
        pointsGauche.clear();
        pointsDroite.clear();

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), pointsGauche, app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), pointsDroite, app.getImageLoader().getImageDroite());
    }

    public void ajouterPointsBordure(List<Point> points, Canvas canevas) {
        points.add(new Point(0, canevas.getHeight() * (app.getHauteurImageOriginale() - 1) / app.getHauteurImageOriginale()));
        points.add(new Point(0, 0));
        points.add(new Point(canevas.getWidth() * (app.getLargeurImageOriginale() - 1) / app.getLargeurImageOriginale(), 0));
        points.add(new Point(canevas.getWidth() * (app.getLargeurImageOriginale() - 1) / app.getLargeurImageOriginale(), canevas.getHeight() * (app.getHauteurImageOriginale() - 1) / app.getHauteurImageOriginale()));
    }

    public void enleverPointsBordure(List<Point> points) {
        for (int i = 0; i < 4; i++) {
            if (!points.isEmpty()) {
                points.remove(points.size() - 1);
            }
        }
    }

    public void ajouterPointCentre() {
        if (app.getChoixMethode() == 2 && !app.getPointControle().getPointsDroite().isEmpty()) {
            double xGauche = app.getImageLoader().getCanevasGauche().getWidth() / 2;
            double yGauche = app.getImageLoader().getCanevasGauche().getHeight() / 2;
            ajouterPoint(xGauche, yGauche, pointsGauche, pointsDroite, app.getImageLoader().getCanevasGauche(), app.getImageLoader().getCanevasDroite());
        } else {
            double xGauche = app.getImageLoader().getCanevasGauche().getWidth() / 2;
            double yGauche = app.getImageLoader().getCanevasGauche().getHeight() / 2;
            pointsGauche.add(new Point(xGauche, yGauche));

            double xDroite = app.getImageLoader().getCanevasDroite().getWidth() / 2;
            double yDroite = app.getImageLoader().getCanevasDroite().getHeight() / 2;
            pointsDroite.add(new Point(xDroite, yDroite));
        }

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), pointsGauche, app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), pointsDroite, app.getImageLoader().getImageDroite());
        app.getImageLoader().actualiserEtatBoutons();
        if (app.getChoixMethode() == 3) {
            app.getTriangleControle().mettreAJourTrianglesEtDessiner();
        }
    }

    public void supprimerDernierPoint() {
        if (!pointsGauche.isEmpty()) {
            pointsGauche.remove(pointsGauche.size() - 1);
            pointsDroite.remove(pointsDroite.size() - 1);

            if (app.getChoixMethode() == 3) {
                app.getTriangleControle().generateDelaunayTriangles();
            }

            if (app.getChoixMethode() == 2 && !pointsDroite.isEmpty()) {
                pointsGauche.remove(pointsGauche.size() - 1);
                pointsDroite.remove(pointsDroite.size() - 1);
                pointsGauche.remove(pointsGauche.size() - 1);
                pointsDroite.remove(pointsDroite.size() - 1);
            }

            app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), pointsGauche, app.getImageLoader().getImageGauche());
            app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), pointsDroite, app.getImageLoader().getImageDroite());
            if (app.getChoixMethode() == 3) {
                app.getTriangleControle().mettreAJourTrianglesEtDessiner();
            }
            app.getImageLoader().actualiserEtatBoutons();
        }
    }

    public List<Point> getPointsGauche() {
        return pointsGauche;
    }

    public void setPointsGauche(List<Point> pointsGauche) {
        this.pointsGauche = pointsGauche;
    }

    public List<Point> getPointsDroite() {
        return pointsDroite;
    }

    public void setPointsDroite(List<Point> pointsDroite) {
        this.pointsDroite = pointsDroite;
    }
}