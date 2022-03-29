/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import stageplanner.KSPOneDVMap;

import static javafx.application.Application.launch;

/**
 *
 * @author pekin
 */
public class MainController extends Application {
	/*
	 * sets the stage to ksp home as the main page has a button bar with all the
	 * links to different pages
	 */

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Kerbal Space Program Engine Optimizer");
		// parent pages for each tab
		Parent KSPOne = FXMLLoader.load(getClass().getResource("KSPOne.fxml"));
		Parent stagePlanner = FXMLLoader.load(getClass().getResource("/stageplanner/StagePlanner.fxml"));

		// button for each parent tab
		Button btnKSPOne = new Button("KSP One");
		Button btnStagePlanner = new Button("Stage Planner");

		VBox layoutKSPHome = new VBox();
		HBox buttonLayout = new HBox(2);

		layoutKSPHome.setId("bodybg");
		layoutKSPHome.getStylesheets().addAll(this.getClass().getResource("kspone.css").toExternalForm());

		// main button layout
		buttonLayout.getChildren().addAll(btnKSPOne, btnStagePlanner);
		// initial page loading
		layoutKSPHome.getChildren().addAll(buttonLayout, KSPOne);

		layoutKSPHome.setSpacing(10);
		layoutKSPHome.setPadding(new Insets(10, 0, 10, 10));

		// display main page
		Scene mainPage = new Scene(layoutKSPHome);

		// controller for parent buttons
		// first clears the home page, the adds the requested page back and displays
		
		btnKSPOne.setOnAction(e -> {
			layoutKSPHome.getChildren().clear();
			layoutKSPHome.getChildren().addAll(buttonLayout, KSPOne);
		});
		btnStagePlanner.setOnAction(e -> {
			layoutKSPHome.getChildren().clear();
			layoutKSPHome.getChildren().addAll(buttonLayout, stagePlanner);
		});
		

		// stage.setResizable(false);
		stage.setScene(mainPage);
		stage.show();

	}

	public static void main(String[] args) throws IOException {
		engines.DefaultEngines de = new engines.DefaultEngines();
		KSPOneController KSPOne = new KSPOneController();
		KSPOneDVMap KSPOneDV = new KSPOneDVMap();
		de.loadEngines();
		KSPOne.loadPlanets();
		KSPOneDV.createDVMap();
		launch(args);
	}

}
