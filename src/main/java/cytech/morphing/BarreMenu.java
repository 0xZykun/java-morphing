package cytech.morphing;

/*
 * see import javafx.scene.control.*;
 * 
 * see import java.util.ArrayList;
 * see import java.util.Arrays;
 * see import java.util.List;
 */
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe BarreMenu pour gérer les menus de l'application MorphingFx.
 * 
 */
public class BarreMenu {
    /**
     * Référence à l'application principale.
     */
    MorphingFx app;

    /**
     * Barre de menus de l'application.
     */
    private MenuBar menuBar;

    /**
     * Constructeur de la classe BarreMenu.
     * Initialise les menus et leurs actions.
     * 
     * @autor Mattéo REYNE
     * @param app l'application principale MorphingFx
     */
    public BarreMenu(MorphingFx app) {
        this.app = app;
        this.menuBar = new MenuBar();

        Menu menuOptions = new Menu("Options");
        MenuItem menuItemOptions = new MenuItem("Configurer GIF");
        menuItemOptions.setOnAction(e -> app.ouvrirDialogueOptions());

        MenuItem menuHelp = new MenuItem("Aide");
        menuOptions.getItems().addAll(menuItemOptions, menuHelp);
        menuHelp.setOnAction(e -> app.ouvrirReadme());

        Menu menuPresets = new Menu("Presets");
        MenuItem preset1 = new MenuItem("Carré en croix : forme simple");
        preset1.setOnAction(e -> appliquerPreset1());

        MenuItem preset2 = new MenuItem("Cercle en étoile : forme arrondie");
        preset2.setOnAction(e -> appliquerPreset2());

        MenuItem preset3 = new MenuItem("Visages masculins : triangulation");
        preset3.setOnAction(e -> appliquerPreset3());

        MenuItem preset4 = new MenuItem("Visages feminins : segments");
        preset4.setOnAction(e -> appliquerPreset4());

        menuPresets.getItems().addAll(preset1, preset2, preset3, preset4);
        menuBar.getMenus().addAll(menuOptions, menuPresets);
    }

    /**
     * Applique le preset 1: Carré en croix avec formes simples.
     * 
     * @autor Mattéo REYNE
     */
    public void appliquerPreset1() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(1);

        app.getControle().miseJourControle(1);

        String currentPath = new java.io.File(".").getCanonicalPath();
        String cheminImageGauche = currentPath + "/src/main/resources/images/square.png";
        String cheminImageDroite = currentPath + "/src/main/resources/images/cross.png";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point((50.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (150.0 * ratioHauteur))
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point((50.0 * ratioLargeur), (90.0 * ratioHauteur)),
            new Point((90.0 * ratioLargeur), (90.0 * ratioHauteur)),
            new Point((90.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((110.0 * ratioLargeur), (50.0 * ratioHauteur)),
            new Point((110.0 * ratioLargeur), (90.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (90.0 * ratioHauteur)),
            new Point((150.0 * ratioLargeur), (110.0 * ratioHauteur)),
            new Point((110.0 * ratioLargeur), (110.0 * ratioHauteur)),
            new Point((110.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((90.0 * ratioLargeur), (150.0 * ratioHauteur)),
            new Point((90.0 * ratioLargeur), (110.0 * ratioHauteur)),
            new Point((50.0 * ratioLargeur), (110.0 * ratioHauteur))
        ));


        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite());
        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche());
        app.getImageLoader().actualiserEtatBoutons();
    }

    /**
     * Applique le preset 2: Cercle en etoile avec formes arrondies.
     * 
     * @autor Mattéo REYNE
     */
    public void appliquerPreset2() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(2);

        app.getControle().miseJourControle(2);
        app.getControle().getFinirForme().setSelected(true);

        String currentPath = new java.io.File(".").getCanonicalPath();
        String cheminImageGauche = currentPath + "/src/main/resources/images/circle.png";
        String cheminImageDroite = currentPath + "/src/main/resources/images/star.png";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(68.41423948220064 * ratioLargeur, 59.805825242718456 * ratioHauteur),
            new Point(94.69255663430422 * ratioLargeur, 45.760517799352755 * ratioHauteur),
            new Point(106.92556634304206 * ratioLargeur, 46.666666666666664 * ratioHauteur),
            new Point(130.4854368932039 * ratioLargeur, 59.35275080906149 * ratioHauteur),
            new Point(153.1391585760518 * ratioLargeur, 78.83495145631068 * ratioHauteur),
            new Point(151.77993527508096 * ratioLargeur, 100.12944983818771 * ratioHauteur),
            new Point(147.24919093851136 * ratioLargeur, 116.89320388349515 * ratioHauteur),
            new Point(137.7346278317152 * ratioLargeur, 142.26537216828478 * ratioHauteur),
            new Point(118.70550161812301 * ratioLargeur, 147.7022653721683 * ratioHauteur),
            new Point(101.48867313915859 * ratioLargeur, 149.9676375404531 * ratioHauteur),
            new Point(74.30420711974112 * ratioLargeur, 150.42071197411002 * ratioHauteur),
            new Point(58.44660194174759 * ratioLargeur, 133.6569579288026 * ratioHauteur),
            new Point(50.74433656957929 * ratioLargeur, 111.0032362459547 * ratioHauteur),
            new Point(48.02588996763753 * ratioLargeur, 89.25566343042073 * ratioHauteur),
            new Point(50.291262135922324 * ratioLargeur, 79.28802588996764 * ratioHauteur)
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(81.97329835255178 * ratioLargeur, 74.27414878352761 * ratioHauteur),
            new Point(95.56003288612398 * ratioLargeur, 42.118877054073586 * ratioHauteur),
            new Point(104.61785590850539 * ratioLargeur, 41.665985902954525 * ratioHauteur),
            new Point(117.75169929095834 * ratioLargeur, 74.72703993464668 * ratioHauteur),
            new Point(154.4358825316032 * ratioLargeur, 76.08571338800388 * ratioHauteur),
            new Point(157.60612058943664 * ratioLargeur, 86.95510101486161 * ratioHauteur),
            new Point(129.5268692200543 * ratioLargeur, 108.693876268577 * ratioHauteur),
            new Point(139.03758339355477 * ratioLargeur, 142.66071260250732 * ratioHauteur),
            new Point(129.5268692200543 * ratioLargeur, 150.8127533226506 * ratioHauteur),
            new Point(100.12944983818774 * ratioLargeur, 130.93851132686083 * ratioHauteur),
            new Point(71.58576051779933 * ratioLargeur, 149.9676375404531 * ratioHauteur),
            new Point(61.16504854368931 * ratioLargeur, 143.62459546925567 * ratioHauteur),
            new Point(70.22653721682846 * ratioLargeur, 110.09708737864078 * ratioHauteur),
            new Point(41.68284789644013 * ratioLargeur, 85.63106796116504 * ratioHauteur),
            new Point(46.213592233009706 * ratioLargeur, 77.47572815533982 * ratioHauteur)
        ));

        app.getPointControle().setPointsGauche(pointsGauche);
        app.getPointControle().setPointsDroite(pointsDroite);

        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite());
        app.getPointControle().configurerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche());
        app.getImageLoader().actualiserEtatBoutons();
    }

    /**
     * Applique le preset 3: Visages masculins avec triangles.
     * 
     * @autor Mattéo REYNE
     */
    public void appliquerPreset3() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(3);

        app.getControle().miseJourControle(3);

        String currentPath = new java.io.File(".").getCanonicalPath();
        String cheminImageGauche = currentPath + "/src/main/resources/images/bg1.jpg";
        String cheminImageDroite = currentPath + "/src/main/resources/images/bg2.jpg";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(129.64724919093854 * ratioLargeur, 130.74595469255664 * ratioHauteur),
            new Point(273.57766990291265 * ratioLargeur, 102.17961165048543 * ratioHauteur),
            new Point(137.33818770226543 * ratioLargeur, 212.05016181229772 * ratioHauteur),
            new Point(296.6504854368933 * ratioLargeur, 183.48381877022658 * ratioHauteur),
            new Point(282.3673139158577 * ratioLargeur, 223.03721682847893 * ratioHauteur),
            new Point(180.18770226537217 * ratioLargeur, 239.51779935275079 * ratioHauteur),
            new Point(260.3932038834953 * ratioLargeur, 286.7621359223301 * ratioHauteur),
            new Point(114.26537216828484 * ratioLargeur, 280.16990291262135 * ratioHauteur),
            new Point(143.9304207119741 * ratioLargeur, 360.37540453074433 * ratioHauteur),
            new Point(259.2944983818771 * ratioLargeur, 406.5210355987055 * ratioHauteur),
            new Point(209.85275080906152 * ratioLargeur, 343.89482200647245 * ratioHauteur),
            new Point(306.5388349514563 * ratioLargeur, 325.21682847896443 * ratioHauteur),
            new Point(329.611650485437 * ratioLargeur, 346.09223300970876 * ratioHauteur),
            new Point(339.5 * ratioLargeur, 294.4530744336569 * ratioHauteur),
            new Point(382.34951456310694 * ratioLargeur, 286.7621359223301 * ratioHauteur),
            new Point(312.03236245954696 * ratioLargeur, 64.82362459546927 * ratioHauteur),
            new Point(110.96925566343042 * ratioLargeur, 62.62621359223302 * ratioHauteur),
            new Point(47.24433656957932 * ratioLargeur, 203.26051779935275 * ratioHauteur),
            new Point(57.13268608414241 * ratioLargeur, 330.71035598705504 * ratioHauteur)
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(162.60841423948233 * ratioLargeur, 204.3592233009709 * ratioHauteur),
            new Point(219.74110032362475 * ratioLargeur, 193.37216828478967 * ratioHauteur),
            new Point(154.91747572815544 * ratioLargeur, 246.11003236245955 * ratioHauteur),
            new Point(252.70226537216845 * ratioLargeur, 229.6294498381877 * ratioHauteur),
            new Point(248.3074433656959 * ratioLargeur, 265.8867313915857 * ratioHauteur),
            new Point(176.8915857605179 * ratioLargeur, 275.7750809061489 * ratioHauteur),
            new Point(221.9385113268609 * ratioLargeur, 313.13106796116506 * ratioHauteur),
            new Point(137.33818770226551 * ratioLargeur, 319.7233009708738 * ratioHauteur),
            new Point(154.91747572815544 * ratioLargeur, 382.34951456310677 * ratioHauteur),
            new Point(232.9255663430421 * ratioLargeur, 405.4223300970874 * ratioHauteur),
            new Point(190.0760517799354 * ratioLargeur, 358.17799352750814 * ratioHauteur),
            new Point(258.1957928802591 * ratioLargeur, 348.28964401294496 * ratioHauteur),
            new Point(288.9595469255663 * ratioLargeur, 363.67152103559874 * ratioHauteur),
            new Point(295.5517799352753 * ratioLargeur, 324.1181229773463 * ratioHauteur),
            new Point(387.8430420711975 * ratioLargeur, 285.66343042071196 * ratioHauteur),
            new Point(326.3155339805826 * ratioLargeur, 78.00809061488673 * ratioHauteur),
            new Point(119.75889967637545 * ratioLargeur, 70.31715210355988 * ratioHauteur),
            new Point(16.480582524271973 * ratioLargeur, 197.76699029126215 * ratioHauteur),
            new Point(57.13268608414241 * ratioLargeur, 330.71035598705504 * ratioHauteur)
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

    /**
     * Applique le preset 4: Visages feminins avec triangles.
     * 
     * @autor Mattéo REYNE
     */
    public void appliquerPreset4() {
        app.setHauteurImageOriginale(0);
        app.setLargeurImageOriginale(0);
        app.getImageLoader().effacerCanevas("gauche");
        app.getImageLoader().effacerCanevas("droite");
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasGauche(), app.getPointControle().getPointsGauche(), app.getImageLoader().getImageGauche());
        app.getTriangleControle().redessinerCanevas(app.getImageLoader().getCanevasDroite(), app.getPointControle().getPointsDroite(), app.getImageLoader().getImageDroite());

        app.setChoixMethode(4);

        app.getControle().miseJourControle(4);

        String currentPath = new java.io.File(".").getCanonicalPath();
        String cheminImageGauche = currentPath + "/src/main/resources/images/bg3.jpg";
        String cheminImageDroite = currentPath + "/src/main/resources/images/bg4.jpg";
        app.getImageLoader().chargerImage(cheminImageGauche, 0);
        app.getImageLoader().chargerImage(cheminImageDroite, 1);

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Segment> segmentsGauche = new ArrayList<>(Arrays.asList(
            new Segment(new Point(221.93851132686095 * ratioLargeur, 202.16181229773463 * ratioHauteur), new Point(288.9595469255664 * ratioLargeur, 213.14886731391584 * ratioHauteur)),
            new Segment(new Point(227.43203883495158 * ratioLargeur, 234.02427184466018 * ratioHauteur), new Point(271.3802588996765 * ratioLargeur, 237.3203883495146 * ratioHauteur)),
            new Segment(new Point(128.5485436893204 * ratioLargeur, 229.6294498381877 * ratioHauteur), new Point(179.0889967637541 * ratioLargeur, 231.82686084142398 * ratioHauteur)),
            new Segment(new Point(108.77184466019422 * ratioLargeur, 208.7540453074434 * ratioHauteur), new Point(183.48381877022663 * ratioLargeur, 204.3592233009709 * ratioHauteur)),
            new Segment(new Point(180.18770226537217 * ratioLargeur, 288.9595469255663 * ratioHauteur), new Point(229.6294498381877 * ratioLargeur, 286.7621359223301 * ratioHauteur)),
            new Segment(new Point(167.00323624595472 * ratioLargeur, 326.3155339805825 * ratioHauteur), new Point(243.91262135922344 * ratioLargeur, 324.1181229773463 * ratioHauteur)),
            new Segment(new Point(98.88349514563113 * ratioLargeur, 317.52588996763757 * ratioHauteur), new Point(169.200647249191 * ratioLargeur, 376.85598705501616 * ratioHauteur)),
            new Segment(new Point(287.86084142394833 * ratioLargeur, 318.62459546925567 * ratioHauteur), new Point(250.5048543689321 * ratioLargeur, 372.4611650485437 * ratioHauteur)),
            new Segment(new Point(135.14077669902917 * ratioLargeur, 125.25242718446603 * ratioHauteur), new Point(261.49190938511333 * ratioLargeur, 129.6472491909385 * ratioHauteur)),
            new Segment(new Point(113.1666666666667 * ratioLargeur, 146.12783171521036 * ratioHauteur), new Point(86.79773462783177 * ratioLargeur, 224.1359223300971 * ratioHauteur)),
            new Segment(new Point(275.7750809061489 * ratioLargeur, 150.52265372168287 * ratioHauteur), new Point(303.242718446602 * ratioLargeur, 217.54368932038832 * ratioHauteur)),
            new Segment(new Point(143.9304207119741 * ratioLargeur, 60.42880258899677 * ratioHauteur), new Point(242.8139158576052 * ratioLargeur, 63.72491909385114 * ratioHauteur)),
            new Segment(new Point(115.36407766990298 * ratioLargeur, 80.205501618123 * ratioHauteur), new Point(63.72491909385116 * ratioLargeur, 168.10194174757282 * ratioHauteur)),
            new Segment(new Point(275.7750809061489 * ratioLargeur, 85.69902912621359 * ratioHauteur), new Point(328.5129449838188 * ratioLargeur, 177.99029126213597 * ratioHauteur))
        ));

        List<Segment> segmentsDroite = new ArrayList<>(Arrays.asList(
            new Segment(new Point(241.71521035598707 * ratioLargeur, 174.69417475728156 * ratioHauteur), new Point(304.34142394822 * ratioLargeur, 184.58252427184465 * ratioHauteur)),
            new Segment(new Point(254.89967637540443 * ratioLargeur, 208.7540453074434 * ratioHauteur), new Point(297.7491909385113 * ratioLargeur, 210.95145631067965 * ratioHauteur)),
            new Segment(new Point(145.02912621359226 * ratioLargeur, 195.56957928802586 * ratioHauteur), new Point(199.96440129449843 * ratioLargeur, 196.66828478964402 * ratioHauteur)),
            new Segment(new Point(141.7330097087379 * ratioLargeur, 168.10194174757282 * ratioHauteur), new Point(217.54368932038832 * ratioLargeur, 169.20064724919095 * ratioHauteur)),
            new Segment(new Point(192.2734627831715 * ratioLargeur, 251.60355987055016 * ratioHauteur), new Point(249.40614886731382 * ratioLargeur, 262.5906148867314 * ratioHauteur)),
            new Segment(new Point(165.9045307443365 * ratioLargeur, 282.36731391585755 * ratioHauteur), new Point(262.5906148867315 * ratioLargeur, 297.74919093851133 * ratioHauteur)),
            new Segment(new Point(109.87055016181229 * ratioLargeur, 284.56472491909386 * ratioHauteur), new Point(182.38511326860836 * ratioLargeur, 364.7702265372168 * ratioHauteur)),
            new Segment(new Point(295.5517799352752 * ratioLargeur, 312.0323624595469 * ratioHauteur), new Point(253.80097087378635 * ratioLargeur, 358.17799352750814 * ratioHauteur)),
            new Segment(new Point(158.21359223300976 * ratioLargeur, 91.1925566343042 * ratioHauteur), new Point(282.3673139158575 * ratioLargeur, 88.99514563106796 * ratioHauteur)),
            new Segment(new Point(134.04207119741102 * ratioLargeur, 131.8446601941748 * ratioHauteur), new Point(107.673139158576 * ratioLargeur, 188.97734627831719 * ratioHauteur)),
            new Segment(new Point(295.5517799352752 * ratioLargeur, 110.96925566343042 * ratioHauteur), new Point(319.72330097087377 * ratioLargeur, 182.38511326860845 * ratioHauteur)),
            new Segment(new Point(161.50970873786412 * ratioLargeur, 40.65210355987056 * ratioHauteur), new Point(257.0970873786409 * ratioLargeur, 38.454692556634306 * ratioHauteur)),
            new Segment(new Point(129.64724919093848 * ratioLargeur, 63.72491909385114 * ratioHauteur), new Point(73.61326860841426 * ratioLargeur, 157.11488673139158 * ratioHauteur)),
            new Segment(new Point(303.2427184466019 * ratioLargeur, 71.41585760517799 * ratioHauteur), new Point(339.5000000000001 * ratioLargeur, 170.29935275080908 * ratioHauteur))
        ));

        app.getSegmentControle().setSegmentsGauche(segmentsGauche);
        app.getSegmentControle().setSegmentsDroite(segmentsDroite);

        app.getSegmentControle().redessinerCanevasSegments(app.getImageLoader().getCanevasGauche(), app.getSegmentControle().getSegmentsGauche(), app.getImageLoader().getImageGauche());
        app.getSegmentControle().redessinerCanevasSegments(app.getImageLoader().getCanevasDroite(), app.getSegmentControle().getSegmentsDroite(), app.getImageLoader().getImageDroite());

        app.getSegmentControle().configurerCanevasSegment(app.getImageLoader().getCanevasGauche(), app.getSegmentControle().getSegmentsGauche(), app.getImageLoader().getCanevasDroite(), app.getSegmentControle().getSegmentsDroite());
        app.getSegmentControle().configurerCanevasSegment(app.getImageLoader().getCanevasDroite(), app.getSegmentControle().getSegmentsDroite(), app.getImageLoader().getCanevasGauche(), app.getSegmentControle().getSegmentsGauche());
        app.getImageLoader().actualiserEtatBoutons();
    }

    /**
     * Retourne la barre de menus.
     * 
     * @autor Mattéo REYNE
     * @return la barre de menus
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }
}