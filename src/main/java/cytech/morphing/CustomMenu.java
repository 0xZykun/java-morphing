package cytech.morphing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.*;

public class CustomMenu {
    MorphingFx app;
    private MenuBar menuBar;

    public CustomMenu(MorphingFx app) {
        this.app = app;
        this.menuBar = new MenuBar();

        Menu menuOptions = new Menu("Options");
        MenuItem menuItemOptions = new MenuItem("Configurer GIF");
        menuItemOptions.setOnAction(e -> app.ouvrirDialogueOptions());
        menuOptions.getItems().add(menuItemOptions);

        /*Menu menuPreferences = new Menu("Préférences");
        MenuItem menuItemPreferences = new MenuItem("Style et Apparence");
        menuItemPreferences.setOnAction(e -> app.ouvrirDialoguePreferences());
        menuPreferences.getItems().add(menuItemPreferences);*/

        Menu menuPresets = new Menu("Presets");
        MenuItem preset1 = new MenuItem("Carré en croix : forme simple");
        preset1.setOnAction(e -> appliquerPreset1());

        MenuItem preset3 = new MenuItem("Visages masculins : triangles");
        preset3.setOnAction(e -> appliquerPreset3());
        menuPresets.getItems().addAll(preset1, preset3);

        menuBar.getMenus().addAll(menuOptions, menuPresets);
    }

    public void appliquerPreset1() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(1);

        app.getControle().miseJourControle(1);

        String cheminImageGauche = "square.png";
        String cheminImageDroite = "cross.png";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point((100 * ratioLargeur), (100 * ratioHauteur)),
            new Point((100 * ratioLargeur), (100 * ratioHauteur)),
            new Point((100 * ratioLargeur), (100 * ratioHauteur)),
            new Point((400 * ratioLargeur), (100 * ratioHauteur)),
            new Point((400 * ratioLargeur), (100 * ratioHauteur)),
            new Point((400 * ratioLargeur), (100 * ratioHauteur)),
            new Point((400 * ratioLargeur), (400 * ratioHauteur)),
            new Point((400 * ratioLargeur), (400 * ratioHauteur)),
            new Point((400 * ratioLargeur), (400 * ratioHauteur)),
            new Point((100 * ratioLargeur), (400 * ratioHauteur)),
            new Point((100 * ratioLargeur), (400 * ratioHauteur)),
            new Point((100 * ratioLargeur), (400 * ratioHauteur))
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point((100 * ratioLargeur), (225 * ratioHauteur)),
            new Point((225 * ratioLargeur), (225 * ratioHauteur)),
            new Point((225 * ratioLargeur), (100 * ratioHauteur)),
            new Point((275 * ratioLargeur), (100 * ratioHauteur)),
            new Point((275 * ratioLargeur), (225 * ratioHauteur)),
            new Point((400 * ratioLargeur), (225 * ratioHauteur)),
            new Point((400 * ratioLargeur), (275 * ratioHauteur)),
            new Point((275 * ratioLargeur), (275 * ratioHauteur)),
            new Point((275 * ratioLargeur), (400 * ratioHauteur)),
            new Point((225 * ratioLargeur), (400 * ratioHauteur)),
            new Point((225 * ratioLargeur), (275 * ratioHauteur)),
            new Point((100 * ratioLargeur), (275 * ratioHauteur))
        ));

        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite());
        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche());
        app.getImageLoader().actualiserEtatBoutons();
    }

    public void appliquerPreset3() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(3);

        app.getControle().miseJourControle(3);

        String cheminImageGauche = "bogosseNumero1.jpg";
        String cheminImageDroite = "bogosseNumero2.jpg";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(87.20000000000002,216.8),
            new Point(82.4,154.39999999999998),
            new Point(71.99999999999997,207.2),
            new Point(62.40000000000001,160.0),
            new Point(96.79999999999998,148.0),
            new Point(116.79999999999998,130.39999999999998),
            new Point(154.4,149.60000000000002),
            new Point(105.6,161.60000000000002),
            new Point(125.6,150.39999999999998),
            new Point(147.99999999999997,163.2),
            new Point(123.99999999999997,168.0),
            new Point(88.79999999999998,120.00000000000001), 
            new Point(94.4,79.2),
            new Point(118.4,53.60000000000001),
            new Point(171.20000000000002,46.400000000000006),
            new Point(225.6,55.2),
            new Point(254.39999999999995,93.60000000000001), 
            new Point(264.80000000000007,156.0),
            new Point(257.6,220.0),
            new Point(276.80000000000007,216.0),
            new Point(287.20000000000005,165.60000000000002),
            new Point(179.99999999999997,146.39999999999998),
            new Point(211.20000000000002,132.8),
            new Point(243.99999999999997,144.0),
            new Point(187.99999999999997,164.0),
            new Point(208.80000000000004,150.39999999999998),
            new Point(234.39999999999995,159.2),
            new Point(190.39999999999995,196.8),
            new Point(184.80000000000004,220.8),
            new Point(165.6,228.8),
            new Point(147.20000000000002,218.39999999999998),
            new Point(145.6,201.59999999999997),
            new Point(152.79999999999998,172.8),
            new Point(181.6,173.60000000000002),
            new Point(219.20000000000002,240.8),
            new Point(117.6,237.59999999999997),
            new Point(127.99999999999997,247.2),
            new Point(154.4,240.8),
            new Point(165.6,245.59999999999997),
            new Point(179.99999999999997,240.8),
            new Point(208.80000000000004,246.39999999999998),
            new Point(179.99999999999997,260.8),
            new Point(155.99999999999997,260.8),
            new Point(249.6,250.39999999999998),
            new Point(211.99999999999997,293.59999999999997),
            new Point(187.20000000000002,308.0),
            new Point(141.6,308.8),
            new Point(123.99999999999997,292.0),
            new Point(89.6,246.39999999999998),
            new Point(233.6,276.0),
            new Point(103.99999999999997,275.2),
            new Point(230.39999999999995,313.59999999999997),
            new Point(99.20000000000002,301.59999999999997),
            new Point(50.40000000000001,123.2),
            new Point(68.79999999999998,54.400000000000006),
            new Point(106.4,11.200000000000003),
            new Point(175.20000000000002,1.6000000000000085),
            new Point(260.80000000000007,20.0),
            new Point(285.6,58.400000000000006),
            new Point(292.80000000000007,126.39999999999999),
            new Point(262.4,137.60000000000002),
            new Point(127.20000000000002,157.60000000000002),
            new Point(211.99999999999986,158.39999999999998)
        ));
        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(81.6,234.39999999999998),
            new Point(75.1999999999999,176.8),
            new Point(56.79999999999993,224.0),
            new Point(55.99999999999998,166.39999999999998),
            new Point(96.79999999999998,148.0),
            new Point(120.79999999999993,133.60000000000002),
            new Point(153.6,144.0),
            new Point(105.6,161.60000000000002),
            new Point(130.39999999999995,152.8),
            new Point(150.39999999999995,159.2),
            new Point(127.99999999999997,167.2),
            new Point(85.6,130.39999999999998),
            new Point(91.99999999999997,79.2),
            new Point(123.1999999999999,69.60000000000001),
            new Point(166.39999999999995,65.60000000000001),
            new Point(220.80000000000004,71.2),
            new Point(244.80000000000004,102.39999999999999),
            new Point(249.6,173.60000000000002),
            new Point(247.99999999999986,229.59999999999997),
            new Point(260.80000000000007,213.59999999999997),
            new Point(262.4,166.39999999999998),
            new Point(187.1999999999999,144.0),
            new Point(210.39999999999995,133.60000000000002),
            new Point(241.6,151.2),
            new Point(190.39999999999995,163.2),
            new Point(211.99999999999986,153.60000000000002),
            new Point(232.80000000000004,164.0),
            new Point(190.39999999999995,190.39999999999998),
            new Point(191.1999999999999,212.0),
            new Point(173.6,217.59999999999997),
            new Point(151.99999999999997,211.2),
            new Point(145.6,193.59999999999997),
            new Point(152.79999999999998,172.8),
            new Point(183.99999999999986,173.60000000000002),
            new Point(218.39999999999995,232.8),
            new Point(115.99999999999997,234.39999999999998),
            new Point(124.79999999999993,242.39999999999998),
            new Point(156.79999999999993,232.0),
            new Point(171.1999999999999,239.2),
            new Point(185.6,233.59999999999997),
            new Point(210.39999999999995,240.8),
            new Point(185.6,264.8),
            new Point(159.99999999999997,264.8),
            new Point(241.6,255.2),
            new Point(208.80000000000004,296.8),
            new Point(195.1999999999999,311.2),
            new Point(147.1999999999999,307.2),
            new Point(128.79999999999993,296.8),
            new Point(90.39999999999995,264.0),
            new Point(230.39999999999995,274.4),
            new Point(110.39999999999995,285.59999999999997),
            new Point(226.39999999999995,315.2),
            new Point(86.39999999999995,296.8),
            new Point(51.19999999999991,116.8),
            new Point(72.79999999999993,60.000000000000014),
            new Point(112.79999999999993,28.799999999999997),
            new Point(163.99999999999997,12.0),
            new Point(213.6,27.200000000000003),
            new Point(249.6,61.60000000000001),
            new Point(263.19999999999993,103.2),
            new Point(262.4,137.60000000000002),
            new Point(127.1999999999999,159.2),
            new Point(211.99999999999986,158.39999999999998)
        ));

        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);
        app.getTriangleControle().generateDelaunayTriangles();

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite());
        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche());
        app.getImageLoader().actualiserEtatBoutons();
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}