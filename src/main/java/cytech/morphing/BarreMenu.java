package cytech.morphing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.*;

public class BarreMenu {
    MorphingFx app;
    private MenuBar menuBar;

    public BarreMenu(MorphingFx app) {
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

        double largeurOriginale = app.getLargeurImageOriginale();
        double hauteurOriginale = app.getHauteurImageOriginale();
        double ratioLargeur = app.getImageLoader().getImageGauche().getWidth() / largeurOriginale;
        double ratioHauteur = app.getImageLoader().getImageGauche().getHeight() / hauteurOriginale;

        List<Point> pointsGauche = new ArrayList<>(Arrays.asList(
            new Point(393.58702988029034 * ratioLargeur, 748.301266685984 * ratioHauteur),
            new Point(629.2533378950322 * ratioLargeur, 750.7308162531465 * ratioHauteur),
            new Point(507.9128371089537 * ratioLargeur, 668.3063646170443 * ratioHauteur),
            new Point(456.87853290183375 * ratioLargeur, 437.4368932038835 * ratioHauteur),
            new Point(558.9471413160733 * ratioLargeur, 439.86709816612733 * ratioHauteur),
            new Point(189.5048662386582 * ratioLargeur, 471.33261602948346 * ratioHauteur),
            new Point(208.94126277595657 * ratioLargeur, 634.1124370293567 * ratioHauteur),
            new Point(823.6173032680152 * ratioLargeur, 665.6965814024663 * ratioHauteur),
            new Point(845.4832493724755 * ratioLargeur, 490.7690125667819 * ratioHauteur),
            new Point(318.27099329825944 * ratioLargeur, 811.4695554322036 * ratioHauteur),
            new Point(502.9167604025932 * ratioLargeur, 937.8061329246426 * ratioHauteur),
            new Point(675.4147796711154 * ratioLargeur, 855.2014476411248 * ratioHauteur),
            new Point(284.25729935798745 * ratioLargeur, 170.06846970136004 * ratioHauteur),
            new Point(745.8717171188218 * ratioLargeur, 170.06846970136004 * ratioHauteur),
            new Point(245.38450628339086 * ratioLargeur, 366.8619846415052 * ratioHauteur),
            new Point(784.7445101934183 * ratioLargeur, 359.5733359400183 * ratioHauteur),
            new Point(553.9373013130011 * ratioLargeur, 4.859099134324598 * ratioHauteur),
            new Point(187.07531667149604 * ratioLargeur, 187.07531667149607 * ratioHauteur),
            new Point(857.630997208287 * ratioLargeur, 194.36396537298296 * ratioHauteur),
            new Point(383.8688316116411 * ratioLargeur, 485.90991343245724 * ratioHauteur),
            new Point(641.4010857308436 * ratioLargeur, 481.05081429813265 * ratioHauteur)
        ));

        List<Point> pointsDroite = new ArrayList<>(Arrays.asList(
            new Point(386.4025889967638 * ratioLargeur, 731.4916936353831 * ratioHauteur),
            new Point(639.1439050701185 * ratioLargeur, 736.3521035598706 * ratioHauteur),
            new Point(517.633656957929 * ratioLargeur, 626.9928802588996 * ratioHauteur),
            new Point(471.45976267529676 * ratioLargeur, 427.71607335490836 * ratioHauteur),
            new Point(566.2377562028046 * ratioLargeur, 430.1462783171522 * ratioHauteur),
            new Point(153.10291262135945 * ratioLargeur, 490.90140237324715 * ratioHauteur),
            new Point(196.8466019417477 * ratioLargeur, 709.6198489751887 * ratioHauteur),
            new Point(780.0957928802592 * ratioLargeur, 692.6084142394823 * ratioHauteur),
            new Point(794.6770226537218 * ratioLargeur, 498.19201725997846 * ratioHauteur),
            new Point(306.20582524271856 * ratioLargeur, 831.1300970873785 * ratioHauteur),
            new Point(502.9167604025932 * ratioLargeur, 937.8061329246426 * ratioHauteur),
            new Point(675.4147796711154 * ratioLargeur, 855.2014476411248 * ratioHauteur),
            new Point(277.04336569579294 * ratioLargeur, 204.13721682847898 * ratioHauteur),
            new Point(702.3292340884578 * ratioLargeur, 226.00906148867313 * ratioHauteur),
            new Point(250.3111111111113 * ratioLargeur, 383.97238403451996 * ratioHauteur),
            new Point(746.0729234088458 * ratioLargeur, 381.5421790722762 * ratioHauteur),
            new Point(461.73894282632176 * ratioLargeur, 51.03430420711974 * ratioHauteur),
            new Point(182.26537216828507 * ratioLargeur, 208.99762675296657 * ratioHauteur),
            new Point(777.6655879180155 * ratioLargeur, 243.02049622437977 * ratioHauteur),
            new Point(388.83279395900786 * ratioLargeur, 483.6107874865156 * ratioHauteur),
            new Point(646.4345199568502 * ratioLargeur, 483.6107874865156 * ratioHauteur)
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