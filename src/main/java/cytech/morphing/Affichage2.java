package cytech.morphing;
// Importation des bibliotheques necessaires

import java.util.LinkedList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.shape.Polygon;

public class Affichage2 extends Application {

	private Point pointADeplacer ;

	@Override
	public void start( Stage primaryStage) throws Exception{
		//Récupération du répertoire actuel
		String currentPath = new java.io.File(".").getCanonicalPath();
		
		primaryStage.setTitle("Morphing");
		int RAYON_POINT =3;
		System.out.println();
		ImageBit imageDebut = new ImageBit(currentPath + "/src/main/resources/images/cross.png");
		ImageBit imageIntermediaire = new ImageBit(currentPath + "/src/main/resources/images/cross.png"); // A l'étape 0 l'image Intermédiaire = imageDebut
		ImageBit imageFin = new ImageBit(currentPath + "/src/main/resources/images/square.png");

		// couleur fond 
		double[] cPixelsFond1 = imageDebut.getRGBA(0,0);
		double[] cPixelsFond2= imageFin.getRGBA(0, 0);
		//couleur forme
		double[] cPixelForme1= imageDebut.getCouleurForme(cPixelsFond1);
		double[] cPixelForme2 = imageFin.getCouleurForme(cPixelsFond2);
		

		// Listes de qui contient les listes des points qui va détourer les formes
		List< List<Point>> llPoint1 = new LinkedList<>() ;
		List< List<Point>> llPoint2 = new LinkedList<>() ;
		List< List<Point>> llPointInter = new LinkedList<>() ;
		llPoint1.add(new LinkedList<Point>());
		llPoint2.add(new LinkedList<Point>());
		llPointInter.add(new LinkedList<Point>());
		
		// Liste des courbes
		List <CBezier> lCourbe1 = new LinkedList<>();
		List <CBezier> lCourbe2 = new LinkedList<>();
		List <Point> lPointC1 = new LinkedList<>(); //liste qui servira à récupérer tout les points qui compose une forme

		// Zones superposees aux deux images qui permettent le trace des points
		Pane zoneDessin1 = new Pane();
		Pane zoneDessin2 = new Pane();

		// Conteneurs plus generaux qui contiendront les images et les zones de dessin
		StackPane conteneur1 = new StackPane();
		StackPane conteneur2 = new StackPane();
		
		HBox conteneurB = new HBox();
	

		// Conteneur de niveau superieur qui va contenir les deux conteneurs generaux
		HBox conteneur3 = new HBox();
		// Conteneur principal qui va contenir le conteneur precedent
		VBox root = new VBox();
		// Boutton qui permet de lancer le morphing
		Button demarrer = new Button("Demarrer");

		// Boutton qui permet de finir le tracer d'une courbe
		Button bCourbe = new Button("Fin courbe");


		// Ajout des differents elements crees dans leur conteneur parents
		conteneur1.getChildren().addAll(imageIntermediaire.genererImage(), zoneDessin1);
		conteneur2.getChildren().addAll(imageFin.genererImage(), zoneDessin2);
		conteneurB.getChildren().addAll(demarrer,bCourbe);
		conteneur3.getChildren().addAll(conteneur1, conteneur2);
		root.getChildren().addAll(conteneur3,conteneurB );
		conteneurB.setAlignment(Pos.CENTER);
		root.setAlignment(Pos.CENTER);
		// creation de la scene (Conteneur general qui contient tout)
		Scene scene = new Scene(root);

		// Recuperation des dimensions de l'image
		final int w = imageDebut.getWidth();
		final int h = imageDebut.getHeight();		

		/* *********************************************
		 *    AJOUT DES POINTS SUR L'IMAGE DE DEPART   *
		 ********************************************* */

		// Gestionnaire d'evenement appele au clic sur l'image de depart
		zoneDessin1.setOnMouseClicked((MouseEvent event) -> {

			// Récupération des coordonnées du clic de la souris
			Double x = event.getX();
			Double y = event.getY();
		
			// On crée un point sur l'image de départ (zoneDessin1) à partir des coordonnées récupérées
			Point newPoint1 = new Point(x, y);
			Point newPointInter = new Point(x, y);
			Point newPoint2 = new Point(x, y);
			
			// Cas point principal
			if (llPoint1.get(llPoint1.size() - 1).size() >= 2) {
				llPoint1.get(llPoint1.size() - 1).add(llPoint1.get(llPoint1.size() - 1).size()-1 ,newPoint1);
				llPointInter.get(llPoint1.size() - 1).add(llPointInter.get(llPointInter.size() - 1).size()-1 ,newPointInter);
				llPoint2.get(llPoint2.size() - 1).add(llPoint2.get(llPoint2.size() - 1).size()-1 ,newPoint2);
			} 
			// Cas point "gravité"
			else {
				llPoint1.get(llPoint1.size() - 1).add(newPoint1);
				llPointInter.get(llPointInter.size() - 1).add(newPointInter);
				llPoint2.get(llPoint2.size() - 1).add(newPoint2);
			}
			// Fin courbe
			bCourbe.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					//Si la courbe possède 1 seul point rien ne se passe sinon on initialise la prochaine courbe
					if (llPoint1.get(llPoint1.size() - 1).size() > 1) {

						List<Point> ltemp1 = llPoint1.get(llPoint1.size()-1) ;
						List<Point> ltemp2 = llPoint2.get(llPoint1.size()-1) ;
						List<Point> ltempInter = llPointInter.get(llPointInter.size()-1) ;
						lCourbe1.add(new CBezier(ltemp1));
						lCourbe2.add(new CBezier(ltemp2));
		
						llPoint1.add(new LinkedList<>());
						llPointInter.add(new LinkedList<>());
						llPoint2.add(new LinkedList<>());
		
						// Le point de fin d'une courbe et le point de départ de la prochaine (on suppose que les formes sont connexes)
						llPoint1.get(llPoint1.size() - 1).add( ltemp1.get( ltemp1.size()-1));
						llPointInter.get(llPointInter.size() - 1).add( ltempInter.get( ltempInter.size()-1));
						llPoint2.get(llPoint2.size() - 1).add( ltemp2.get( ltemp2.size()-1));
						afficherCourbe(zoneDessin1, lCourbe1);
						afficherCourbe(zoneDessin2, lCourbe2);
					}
				}
			});
			// On nettoie les zones de dessin
			zoneDessin1.getChildren().clear();
			zoneDessin2.getChildren().clear();
		
		
			/* *****************************************************************
			 *    MISE A JOUR DES ZONES DE DESSINS ET DES DIFFERENTES LISTES   *
			 ***************************************************************** */

			for ( int i =0 ; i<llPoint1.size() ; i++){
				for (int j=0 ; j<llPoint1.get(i).size() ; j++){
					Point a1 = llPoint1.get(i).get(j);
					Point a2 = llPoint2.get(i).get(j);
					Circle circle1 = new Circle(a1.getX(), a1.getY(), RAYON_POINT);
					circle1.setFill(Color.RED);
					Circle circle2 = new Circle(a2.getX(), a2.getY(), RAYON_POINT);
					circle2.setFill(Color.RED);

					// affichage
					zoneDessin1.getChildren().add(circle1);
					zoneDessin2.getChildren().add(circle2);
				}
			}
		});


		/* *********************************************************************
		 *    DEPLACEMENT DES POINTS SITUE DANS LA ZONE DE L'IMAGE D'ARRIVEE   *
		 ********************************************************************* */

		// I - Gestionnaire d'evenement lie a la selection du point a deplacer
		zoneDessin2.setOnMousePressed(event -> {
			// On recupere les coordonnees de la position de la souris
			double x = event.getX();
			double y = event.getY();

			// On recupere le point le plus proche de cette position : c'est le point a deplace
			for (List<Point> lpoint : llPoint2) {
				for (Point point : lpoint){
					if (Math.abs(point.getX() - x) <= RAYON_POINT && Math.abs(point.getY() - y) <= RAYON_POINT) {
					// Sélectionner le point
					pointADeplacer = point;
					break;
					}
				}
			}
		});

		// II - Gestionnaire d'evenement lie au deplacement de la souris (Et donc du point)
		zoneDessin2.setOnMouseDragged(e -> {
			// Si un point a ete selectionne, alors on peut effectuer le deplacement
			if (pointADeplacer != null) {
				// Déplacer le point en fonction de la position de la souris
				pointADeplacer.setX(e.getX());
				pointADeplacer.setY(e.getY());
				// Rafraîchir l'affichage pour montrer le déplacement du point
				refreshDisplay(w, h, zoneDessin2, llPoint2, RAYON_POINT);

			}
		});

		// III - Gestionnaire d'evenement lie au relachement de la souris
		zoneDessin2.setOnMouseReleased(event -> {
			// On reenitialise la selection du point
			pointADeplacer = null;
		});
		
		
		/* ****************************
		 *    LANCEMENT DU MORPHING   *
		 **************************** */

		demarrer.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent event){
				
		
		
		/*
		 * Cette façon de récupérer la couleur des formes sembles être la meilleur d'un pov complexité 
		 * mais comme sur les bords les couleurs des pixels en général change (surtout avec la redimension des images)
		 * peut-être utiliser la méthode getCouleurForme de ImagesBit peut être mieux pour éviter les drames
		 */

		
		/* couleur forme 
		Point pDebut = llPoint1.get(0).get(0);
		int i = 0;
		int j = 0;
		int m = 0;
		boolean trouve = false;
		
		while (!trouve) {
			for (j = -m; j <= m; j++) {
				for (i = -m; i <= m; i++) {
					if (pDebut.getX()+i >=0 && pDebut.getX()+i <h && pDebut.getY()>=0 && pDebut.getY()<w){
						if ( !Arrays.equals(cPixelsFond1 , imageDebut.getRGBA((int) pDebut.getX()+i,(int) pDebut.getY()+j ) ) ) {
						trouve = true;
						break;
						}
					}
				}
				if (trouve) break;
			}
			if (!trouve) {
				m++;
			}
		}
		double[] cPixelForme1 = imageDebut.getRGBA((int) pDebut.getX()+i,(int) pDebut.getY()+j );
		// couleur forme2
		pDebut = llPoint2.get(0).get(0);
		i = 0;
		j = 0;
		m = 0;
		trouve = false;
	
		while (!trouve) {
			for (j = -m; j <= m; j++) {
				for (i = -m; i <= m; i++) {
					if (pDebut.getX()+i >=0 && pDebut.getX()+i <h && pDebut.getY()>=0 && pDebut.getY()<w){
						if ( !Arrays.equals(cPixelsFond2 , imageFin.getRGBA((int) pDebut.getX()+i,(int) pDebut.getY()+j ) ) ) {
						trouve = true;
						break;
					}
				}
				}
				if (trouve) break;
			}
			if (!trouve) {
				m++;
			}
		}
		double[] cPixelForme2 = imageFin.getRGBA((int) pDebut.getX()+i,(int) pDebut.getY()+j );
		*/

				// on relie le dernier point de la figure avec le premoier
				llPoint1.get(llPoint1.size()-1).add( llPoint1.get(0).get(0));
				llPoint2.get(llPoint2.size()-1).add( llPoint2.get(0).get(0));
				llPointInter.get(llPointInter.size()-1).add( llPointInter.get(0).get(0));
				lCourbe1.add(new CBezier( llPoint1.get( llPoint1.size()-1)));
				lCourbe2.add(new CBezier(llPoint2.get( llPoint2.size()-1))) ;

				// Cache points + courbes
				zoneDessin1.setVisible(false);
				zoneDessin2.setVisible(false); 

				//boucle lors de l'affichage du morphisme
				Timeline timeline = new Timeline();
				timeline.setCycleCount(4);
				timeline.setAutoReverse(true);
				int nbKeyFrames =20;
				double keyFrameDuration =80;
				
				for (int k=0 ;k <nbKeyFrames; k++){
					double coef =k/(nbKeyFrames-1.);
					
					/// Interpolation point de controle
					for (int i =0 ; i< llPoint1.size();i++){
						for ( int j=0 ; j<llPoint1.get(i).size();j++){
							llPointInter.get(i).get(j).interpolation(llPoint1.get(i).get(j),llPoint2.get(i).get(j) , coef);
						}
					}
					// Modification de Courbe 1 avec les nouvelles valeur dû à l'interpolation 
					lCourbe1.clear();
					for ( List<Point> lPi: llPointInter ){
						lCourbe1.add( new CBezier(lPi));
					}
					lPointC1.clear();
					for(CBezier courbe: lCourbe1){
						for ( Point p : courbe.cBezier()){
							lPointC1.add(p);
						}
					}
					//changement des couleurs
					Color[][] tabpixels = new Color[w][h];
					for ( int i =0 ; i<w; i++){
						for (int j=0; j<h;j++){
							Point pointCourant = new Point((double)i, (double)j);
							
							if ( pointCourant.appartientForme(lPointC1)){
								
								double newr =  (cPixelForme1[0] * (1 - coef) + coef * cPixelForme2[0]);
								double newg =  (cPixelForme1[1] * (1 - coef) + coef * cPixelForme2[1]);
								double newb = (cPixelForme1[2] * (1 - coef) + coef * cPixelForme2[2]);
								double newa = (cPixelForme1[3] * (1 - coef) + coef * cPixelForme2[3]);
								tabpixels[i][j] = new Color(newr, newg, newb, newa);
							 }
							else{ 
								double newr =  (cPixelsFond1[0] * (1 - coef) + coef * cPixelsFond2[0]);
								double newg =  (cPixelsFond1[1] * (1 - coef) + coef * cPixelsFond2[1]);
								double newb = (cPixelsFond1[2] * (1 - coef) + coef * cPixelsFond2[2]);
								double newa = (cPixelsFond1[3] * (1 - coef) + coef * cPixelsFond2[3]);
								tabpixels[i][j] = new Color(newr, newg, newb, newa);
							}
						}
					}
					System.out.println(k); // compteur pour voir combien d'images ont été généré
					
					
				
			/* ***********************************************************
			*   GENERATION DE LA FRAME LIE AUX MODIFICATIONS CI DESSUS  *
			*********************************************************** */

			// Duree de l'animation pour cette frame
			Duration keyFrameTime = Duration.millis(k * keyFrameDuration);
			// Generation de la frame
			KeyFrame keyFrame = new KeyFrame(keyFrameTime, new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					zoneDessin1.getChildren().clear();

					for (int i = 0; i < w; i++) {
						for (int l = 0; l < h; l++) {
							imageIntermediaire.setRGB(i, l, tabpixels[i][l].getRed(), tabpixels[i][l].getGreen(), tabpixels[i][l].getBlue(), (tabpixels[i][l].getOpacity()));
						}
					}
				}
			});
			// Ajout de la frame a la timeline
			timeline.getKeyFrames().add(keyFrame);
				
			
		}
		timeline.play();
		
	}
	}); 
		// ajout de la scene au stage
		primaryStage.setScene(scene);
		// Ouverture de la fenetre
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
    }


	/**
	 * Methode qui met a jour une zone de dessin (ici un pane)
	 * @param width : Largeur de l'image
	 * @param heigth : Longueur de l'image
	 * @param zoneDessin : Zone pour dessiner les triangles et points
	 * @param llp : Liste de liste de points à mettre a jour
	 * @param rayonPoint : Rayon des points (pour l'affichage)
	 */
	public void refreshDisplay(int width, int heigth, Pane zoneDessin, List<List<Point>> llp, int rayonPoint) {
		// Nettoyage en amont de la zone de dessin
		zoneDessin.getChildren().clear();
		
		for (List<Point> lpoint : llp ){
			// Mise a jour des points
			for (Point point : lpoint) {
			Circle circle = new Circle(point.getX(), point.getY(), rayonPoint);
			circle.setFill(Color.RED);
			zoneDessin.getChildren().add(circle);
		}
			// Mise a jour des courbes
			if (lpoint.size() > 1) {
				CBezier bezierCurve = new CBezier(lpoint);
				List<Point> points = bezierCurve.cBezier();
				Polygon polygon = new Polygon();
				for (Point point : points) {
					polygon.getPoints().addAll(point.getX(), point.getY());
				}
				polygon.setFill(Color.TRANSPARENT);
				polygon.setStroke(Color.BLUE);
				zoneDessin.getChildren().add(polygon);
			}
		}
		
		
	}
	public void afficherCourbe(Pane zoneDessin, List <CBezier> lCourbe){
		for(CBezier c : lCourbe){
			List<Point> courbe = c.cBezier();

			// Créer un objet Polygon à partir des points de la courbe
			Polygon polygon = new Polygon();
			for(Point p : courbe) {
				polygon.getPoints().addAll(p.getX(), p.getY());
			}

			polygon.setFill(Color.TRANSPARENT);
			polygon.setStroke(Color.BLUE);
			zoneDessin.getChildren().add(polygon);
		}
	}
}