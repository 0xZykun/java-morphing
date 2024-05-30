package cytech.morphing;

/*
 * see import javafx.scene.canvas.Canvas;
 * see import javafx.scene.canvas.GraphicsContext;
 * see import javafx.scene.image.Image;
 * see import javafx.scene.paint.Color;
 *
 * see import java.util.ArrayList;
 * see import java.util.List;
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui gère le contrôle des triangles dans l'application de morphing.
 * 
 */
public class TriangleControle {
    /**
     * Instance de l'application principale.
     */
    private MorphingFx app;

    /**
     * Liste des triangles pour l'image gauche.
     */
    private List<Triangle> listeTriangleGauche = new ArrayList<>();

    /**
     * Liste des triangles pour l'image droite.
     */
    private List<Triangle> listeTriangleDroite = new ArrayList<>();

    /**
     * Constructeur de la classe TriangleControle.
     * 
     * @autor Mattéo REYNE
     * @param app Instance de l'application principale
     */
    public TriangleControle(MorphingFx app) {
        this.app = app;
    }

    /**
     * Redessine le canevas avec les points et les triangles.
     * 
     * @autor Mattéo REYNE
     * @param canevas Canvas à redessiner
     * @param points Liste des points à dessiner
     * @param image Image à dessiner sur le canevas
     */
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

    /**
     * Dessine les lignes reliant les points sur le canevas.
     * 
     * @autor Mattéo REYNE
     * @param gc Contexte graphique pour dessiner
     * @param points Liste des points à relier
     */
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

    /**
     * Dessine les points sur le canevas.
     * 
     * @autor Mattéo REYNE
     * @param gc Contexte graphique pour dessiner
     * @param points Liste des points à dessiner
     */
    private void dessinerPoints(GraphicsContext gc, List<Point> points) {
        List<Color> couleurs = CouleurGenerateur.genererCouleursDifferentes(15);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            Color couleur;
            if (app.getChoixMethode() != 2) {
                couleur = couleurs.get(i % couleurs.size());
            } else {
                if ((i % 3 == 1 || i % 3 == 2) && app.getChoixMethode() == 2) {
                    couleur = app.getControle().getCouleurPoints().getValue();
                } else {
                    couleur = couleurs.get(i / 3 % couleurs.size());
                }
            }

            gc.setFill(couleur);
            gc.fillOval(point.getX() - 5, point.getY() - 5, 10, 10);
        }
    }

    /**
     * Met à jour les triangles Delaunay et redessine le canevas.
     * 
     * @autor Mattéo REYNE
     */
    public void mettreAJourTrianglesEtDessiner() {
        if (app.getPointControle().getPointsGauche().size() >= 3) {
            generateDelaunayTriangles();
        }

        redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());
    }

    /**
     * Génère les triangles Delaunay pour les points actuels.
     * 
     * @autor Mattéo REYNE
     */
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

    /**
     * Vérifie si un triangle a un sommet dans un coin de l'image.
     * 
     * @autor Mattéo REYNE
     * @param triangle Triangle à vérifier
     * @param width Largeur de l'image
     * @param height Hauteur de l'image
     * @return true si un sommet est dans un coin, false sinon
     */
    private boolean estTriangleCoin(Triangle triangle, double width, double height) {
        return (triangle.getA().estAuCoin((int) width, (int) height) ||
                triangle.getB().estAuCoin((int) width, (int) height) ||
                triangle.getC().estAuCoin((int) width, (int) height));
    }

    /**
     * Getter pour la liste des triangles de l'image gauche.
     * 
     * @autor Mattéo REYNE
     * @return Liste des triangles de l'image gauche
     */
    public List<Triangle> getListeTriangleGauche() {
        return listeTriangleGauche;
    }

    /**
     * Setter pour la liste des triangles de l'image gauche.
     * 
     * @autor Mattéo REYNE
     * @param listeTriangleGauche Nouvelle liste des triangles de l'image gauche
     */
    public void setListeTriangleGauche(List<Triangle> listeTriangleGauche) {
        this.listeTriangleGauche = listeTriangleGauche;
    }

    /**
     * Getter pour la liste des triangles de l'image droite.
     * 
     * @autor Mattéo REYNE
     * @return Liste des triangles de l'image droite
     */
    public List<Triangle> getListeTriangleDroite() {
        return listeTriangleDroite;
    }

    /**
     * Setter pour la liste des triangles de l'image droite.
     * 
     * @autor Mattéo REYNE
     * @param listeTriangleDroite Nouvelle liste des triangles de l'image droite
     */
    public void setListeTriangleDroite(List<Triangle> listeTriangleDroite) {
        this.listeTriangleDroite = listeTriangleDroite;
    }
}