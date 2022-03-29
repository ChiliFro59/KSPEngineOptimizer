/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.Scanner;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import calculations.Calculations;
import main.KSPDriver;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static java.util.stream.DoubleStream.builder;
import static java.util.stream.IntStream.builder;
import static java.util.stream.LongStream.builder;
import static java.util.stream.Stream.builder;

/**
 *
 * @author pekin
 */
public class KSPOneController implements Initializable {
	// Labels
	@FXML
	private Label label;
	@FXML
	private Label minDVLabel;
	@FXML
	private Label minTWRLabel;
	@FXML
	private Label maxDVLabel;
	@FXML
	private Label maxTWRLabel;
	@FXML
	private Label gravityLabel;
	@FXML
	private Label atmosphereLabel;
	@FXML
	private Label numEnginesLabel;
	@FXML
	private Label payloadLabel;

	// Buttons
	@FXML
	private Button buttonCalculate;
	@FXML
	private Button loadExampleButton;
	@FXML
	private Button btnSaveStage;

	// Fields
	@FXML
	private TextField minDVField;
	@FXML
	private TextField minTWRField;
	@FXML
	private TextField maxTWRField;
	@FXML
	private TextField maxDVField;
	@FXML
	private TextField gravityField;
	@FXML
	private TextField atmosphereField;
	@FXML
	private TextField numEnginesField;
	@FXML
	private TextField payloadField;

	// Radio Buttons
	// Small Engines
	@FXML
	private CheckBox selectAllSmall;
	@FXML
	private RadioButton radioSpark;
	@FXML
	private RadioButton radioThud;
	@FXML
	private RadioButton radioDawn;
	@FXML
	private CheckBox dawnSolarPanelsCheck;

	// Medium Engines
	@FXML
	private CheckBox selectAllMedium;
	@FXML
	private RadioButton radioReliant;
	@FXML
	private RadioButton radioTerrier;
	@FXML
	private RadioButton radioSwivel;
	@FXML
	private RadioButton radioVector;
	@FXML
	private RadioButton radioNerv;

	// Large Engines
	@FXML
	private CheckBox selectAllLarge;
	@FXML
	private RadioButton radioPoodle;
	@FXML
	private RadioButton radioSkipper;
	@FXML
	private RadioButton radioMainsail;
	@FXML
	private RadioButton radioTwinboar;
	@FXML
	private RadioButton radioMammoth;
	@FXML
	private RadioButton radioRhino;

	// Jet Engines
	@FXML
	private CheckBox selectAllJet;
	@FXML
	private RadioButton radioDart;
	@FXML
	private RadioButton radioJuno;
	@FXML
	private RadioButton radioRapier;
	@FXML
	private RadioButton radioGoliath;
	@FXML
	private RadioButton radioWheesley;
	@FXML
	private RadioButton radioPanther;
	@FXML
	private RadioButton radioWhiplash;
	// Solid Fuel Engines
	@FXML
	private CheckBox selectAllSolidBooster;
	@FXML
	private RadioButton radioShrimp;
	@FXML
	private RadioButton radioMite;
	@FXML
	private RadioButton radioHammer;
	@FXML
	private RadioButton radioThumper;
	@FXML
	private RadioButton radioKickback;
	@FXML
	private RadioButton radioFlea;
	@FXML
	private RadioButton radioClydesdale;
	@FXML
	private RadioButton radioThoroughbred;

	// Menus
	// Custom Engines
	@FXML
	private MenuButton customEnginesMenu;
	@FXML
	private CheckBox custom0;
	@FXML
	private CheckBox custom1;
	@FXML
	private CheckBox custom2;
	@FXML
	private CheckBox custom3;
	@FXML
	private CheckBox custom4;
	@FXML
	private CheckBox custom5;
	@FXML
	private CheckBox custom6;
	@FXML
	private CheckBox custom7;
	@FXML
	private CheckBox custom8;
	@FXML
	private CheckBox custom9;

	// Gravity Section
	@FXML
	private MenuButton planetsGravityMenu;
	private String version;
	FileWriter myWriter;
	private static ArrayList<ArrayList<String>> planetValueList = new ArrayList<ArrayList<String>>();

	public void loadPlanets() throws IOException {
		
			planetValueList.clear();
			readFromFile();


		planetValueList = KSPOnePlanet.getPlanetValueList();

	}
	//updates local text file for planets if it doesnt match database version
	private void updateFile() {
		System.out.println("Time to update planets!");
		try {
			planetValueList.forEach((n) -> {
				try {
					write(n);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private void write(ArrayList<String> n) throws Exception {
		myWriter.append(n.get(0) + " ");
		myWriter.append(n.get(1) + " ");
		myWriter.append(n.get(2) + "\n");
	}

	private static String planetName = null;
	private static String planetGravity = null;
	private static String planetAtmosphere = null;
	private KSPOnePlanets KSPOnePlanet = new KSPOnePlanets();

	private void readFromFile() throws IOException {

		try {

			InputStream is =  getClass().getResourceAsStream("/main/planets.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			version = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] planetValues = line.split(" ");
				KSPOnePlanet.verifyPlanetValues(planetValues);

			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred." + e.getStackTrace());
		}
	}

	/*
	 * public void verifyPlanetValues(String[] values) { boolean cleared;
	 * 
	 * try { planetName = values[0]; planetGravity = values[1]; planetAtmosphere =
	 * values[2]; cleared = true;
	 * 
	 * if (values.length != 3) { cleared = false; throw new
	 * ArrayIndexOutOfBoundsException(); }
	 * 
	 * } catch (NumberFormatException numberFormat) {
	 * System.out.println("Number format incorrect " + numberFormat);
	 * System.out.println("Only name should use letters or characters"); cleared =
	 * false; } catch (ArrayIndexOutOfBoundsException indexOutOfBounds) {
	 * System.out.println(planetName);
	 * System.out.println("Planet values are missing or extra ones included, " +
	 * values.length + " of 3"); cleared = false; } catch (Exception e) {
	 * System.out.println("There was an issue with planet values please fix " + e);
	 * cleared = false; } if (cleared) { createPlanetLists(); }
	 * 
	 * }
	 * 
	 * private static void createPlanetLists() {
	 * 
	 * planetValueList.add(new ArrayList<>(Arrays.asList(planetName,planetGravity,
	 * planetAtmosphere)));
	 * 
	 * }
	 */
	// Pressure
	@FXML
	private MenuButton planetsPressureMenu;

	// Variables for passing to driver class
	private boolean cleared = true;
	private double payloadMass;
	private double gravity;
	private double minDV;
	private double maxDV;
	private double minTWR;
	private double maxTWR;
	private double atmoPressure;
	private int numEngines;
	private String regex = "\\d+";

	// Chart variables
	@FXML
	private AreaChart<Number, Number> engineChart;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private NumberAxis xAxis;
	private List<String> requestedEnginesList = new ArrayList<String>();
	private List<XYChart.Series> seriesArray = new ArrayList<XYChart.Series>();

	// output info panel
	@FXML
	private Text errorTextField;
	@FXML
	private Text outputTWR;
	@FXML
	private Text outputDV;
	@FXML
	private Text outputBest;
	@FXML
	private Text outputPayloadFraction;
	@FXML
	private Text outputBurnout;
	@FXML
	private Text outputTank;
	@FXML
	private Text outputPropellant;
	@FXML
	private Text outputPayloadt;
	@FXML
	private Text outputSecondBest;
	@FXML
	private Text outputSecondBurnout;
	@FXML
	private Text outputSecondEngines;
	@FXML
	private Text outputSecondPropellant;
	@FXML
	private Text outputSecondPayload;
	@FXML
	private Text outputNumEngines;
	@FXML
	private Text outputSecondTank;
	@FXML
	private Text outputSecondPayloadFraction;
	@FXML
	private Text txtDVStagePlanner;
	@FXML
	private Text txtMassStagePlanner;
	@FXML
	private Text txtEngineStagePlanner;
	@FXML
	private Text txtPropellantPlanner;
	@FXML
	private Text txtTankPlanner;
	@FXML
	private Text txtTWRPlanner;
	@FXML
	private Pane paneInstruction;
	@FXML
	private Text txtTankPropellantMass;

	/*
	 * when calculate button is pressed, this verifys input and if it gets cleared
	 * it get sent to kspdriver class for processing
	 */
	@FXML
	private void handleCalculateAction(ActionEvent event) {

		KSPDriver.engineListClear();
		verifyInput();
		if (cleared) {
			engineChart.setTitle("Payload = " + payloadMass + "(t) @ " + atmoPressure + " Atmosphere Pressure");
			errorTextField.setText("");
			KSPDriver.setRequestedValues(requestedEnginesList, payloadMass, gravity, minDV, maxDV, minTWR, maxTWR,
					atmoPressure, numEngines);
			setGraph();
			Label cursorCoords = createCursorGraphCoordsMonitorLabel(engineChart);
		} else {
			reset();
		}

	}

	/*
	 * used to reset all the stored info
	 */
	private void reset() {
		// requestedEngines.clear();
		requestedEnginesList.clear();
		seriesArray.clear();
		engineChart.getData().clear();
	}

	/*
	 * graphing funtion to draw the graph
	 */
	@SuppressWarnings("unchecked")
	private void setGraph() {
		KSPDriver KSPDriver = new KSPDriver();
		List<Double> graphPoints = KSPDriver.graphPoints;
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(minDV);
		xAxis.setUpperBound(maxDV);
		xAxis.setTickUnit(100);
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(minTWR);
		yAxis.setUpperBound(maxTWR);
		yAxis.setTickUnit(.1);
		engineChart.getData().clear();
		int graphPointSize = 1000;
		int engStart = 1;
		int numEng = 1;
		int i = 0;
		int temp = 0;
		for (int j = 0; j < requestedEnginesList.size(); j++) {
			while (i < graphPointSize * numEng) {
				temp++;
				if (graphPoints.get((graphPointSize * numEng) - 1) <= 1) {
					seriesArray.get(j).getData().add(new XYChart.Data(graphPoints.get(i + 1), graphPoints.get(i)));
					i = graphPointSize * numEng;

					break;
				}
				if (graphPoints.get(i + 1) >= maxDV - .005) {
					seriesArray.get(j).getData().add(new XYChart.Data(maxDV, graphPoints.get(i)));

				} else {
					seriesArray.get(j).getData().add(new XYChart.Data(graphPoints.get(i + 1), graphPoints.get(i)));
				}
				i += 2;
			}

			// System.out.println(graphPoints.get(engStart));
			if (graphPoints.get(engStart) > 0) {
				seriesArray.get(j).getData().add(new XYChart.Data(minDV, maxTWR));
			}

			numEng++;
			engStart += 1000;
			temp = 0;
			// System.out.println(graphPoints.size());
			engineChart.getData().add(seriesArray.get(j));
		}
		XYChart.Series tempSeries = new XYChart.Series();

		int engineChartSize = engineChart.getData().size();
		// System.out.println(graphPoints);
		// System.out.println(engineChart.getData().get(0).getData());

		/*
		 * sorts the series, displaying the largest twr/delta-v pair first
		 */
		for (int p = 0; p < engineChart.getData().size(); p++) {
			int maxTWRIndex = 0;
			double largestTWR = 0;
			double largestDV = 0;
			for (int k = 0; k < engineChartSize; k++) {

				double tempTWR = (double) engineChart.getData().get(k).getData()
						.get(engineChart.getData().get(k).getData().size() - 1).getYValue();
				double tempDV = (double) engineChart.getData().get(k).getData()
						.get(engineChart.getData().get(k).getData().size() - 1).getXValue();
				if (tempTWR >= largestTWR) {
					if (tempTWR == largestTWR) {
						if (tempDV >= largestDV) {
							largestTWR = tempTWR;
							largestDV = tempDV;
							tempSeries = engineChart.getData().get(k);
							maxTWRIndex = k;
						}
					} else {
						largestTWR = tempTWR;
						largestDV = tempDV;
						tempSeries = engineChart.getData().get(k);
						maxTWRIndex = k;
					}
				}
			}

			engineChart.getData().remove(maxTWRIndex);
			engineChart.getData().add(tempSeries);
			engineChartSize--;
		}
		engineChart.setCreateSymbols(false);
		KSPDriver.graphPoints.clear();
		requestedEnginesList.clear();
		seriesArray.clear();
		numEng = 1;
	}

	/*
	 * method to verify correct info input and will display error message, and
	 * change color of the incorrect section
	 */
	private boolean verifyInput() {
		cleared = true;
		checkRadioButtons();
		if (requestedEnginesList.isEmpty()) {
			cleared = false;
			errorTextField.setText("Please select at least one engine!!");
		}
		try {
			minDV = Double.parseDouble(minDVField.getText());
			minDVLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			minDVField.setText("Numbers only!");
			minDVLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			maxDV = Double.parseDouble(maxDVField.getText());
			maxDVLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			maxDVField.setText("Numbers only!");
			maxDVLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;

		}
		try {
			minTWR = Double.parseDouble(minTWRField.getText());
			minTWRLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			minTWRField.setText("Numbers only!");
			minTWRLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			maxTWR = Double.parseDouble(maxTWRField.getText());
			maxTWRLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			maxTWRField.setText("Numbers only!");
			maxTWRLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			payloadMass = Double.parseDouble(payloadField.getText());
			payloadLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			payloadField.setText("Numbers only!");
			payloadLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			gravity = Double.parseDouble(gravityField.getText());
			gravityLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			gravityField.setText("Numbers only!");
			gravityLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			atmoPressure = Double.parseDouble(atmosphereField.getText());
			atmosphereLabel.setTextFill(Color.web("#FFFFFF"));
		} catch (java.lang.NumberFormatException e) {
			atmosphereField.setText("Numbers only!");
			atmosphereLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		try {
			numEngines = Integer.parseInt(numEnginesField.getText());
			if (numEngines == 0) {
				numEngines = 999999999;
			}
			numEnginesLabel.setTextFill(Color.web("#FFFFFF"));

		} catch (java.lang.NumberFormatException e) {
			numEnginesField.setText("Numbers only!");
			numEnginesLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Please use numbers only!!");
			cleared = false;
		}
		if (numEngines <= 0 || atmoPressure < 0 || gravity < 0 || payloadMass < 0 || maxTWR <= 0 || minTWR < 0
				|| maxDV <= 0 || minDV < 0) {
			errorTextField.setText("One or more values are negative!!");
			cleared = false;
		}
		if (maxDV - minDV <= 0) {
			maxDVLabel.setTextFill(Color.web("#FA2E02"));
			minDVLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Max DV cannot be 0 or less than Min DV!!");
			cleared = false;
		}
		if (maxTWR - minTWR <= 0) {
			maxTWRLabel.setTextFill(Color.web("#FA2E02"));
			minTWRLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Max TWR cannot 0 or less than Min TWR!!");
			cleared = false;
		}
		if (gravity == 0) {
			gravityLabel.setTextFill(Color.web("#FA2E02"));
			errorTextField.setText("Gravity shouldn't be 0");
			cleared = false;
		}

		return cleared;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	/*
	 * toggles all small engines on and off
	 */
	@FXML
	private void toggleSmallEngines(ActionEvent event) {
		if (selectAllSmall.isSelected()) {
			radioSpark.setSelected(true);
			radioThud.setSelected(true);
			radioDawn.setSelected(true);
			dawnSolarPanelsCheck.setSelected(true);
		} else {
			radioSpark.setSelected(false);
			radioThud.setSelected(false);
			radioDawn.setSelected(false);
			dawnSolarPanelsCheck.setSelected(false);
		}

	}

	@FXML
	private void toggleDawn(ActionEvent event) {
		if (dawnSolarPanelsCheck.isSelected() && !radioDawn.isSelected()) {
			radioDawn.setSelected(true);
		}
	}

	/*
	 * toggles all medium engines on and off
	 */
	@FXML
	private void toggleMediumEngines(ActionEvent event) {
		if (selectAllMedium.isSelected()) {
			radioReliant.setSelected(true);
			radioTerrier.setSelected(true);
			radioSwivel.setSelected(true);
			radioVector.setSelected(true);
			radioNerv.setSelected(true);
		} else {
			radioReliant.setSelected(false);
			radioReliant.setSelected(false);
			radioTerrier.setSelected(false);
			radioSwivel.setSelected(false);
			radioVector.setSelected(false);
			radioNerv.setSelected(false);
		}
	}

	/*
	 * toggles all large engines on and off
	 */
	@FXML
	private void toggleLargeEngines(ActionEvent event) {
		if (selectAllLarge.isSelected()) {
			radioPoodle.setSelected(true);
			radioSkipper.setSelected(true);
			radioMainsail.setSelected(true);
			radioTwinboar.setSelected(true);
			radioMammoth.setSelected(true);
			radioRhino.setSelected(true);
		} else {
			radioPoodle.setSelected(false);
			radioSkipper.setSelected(false);
			radioMainsail.setSelected(false);
			radioTwinboar.setSelected(false);
			radioMammoth.setSelected(false);
			radioRhino.setSelected(false);
		}
	}

	/*
	 * toggles all jet engines on and off
	 */
	@FXML
	private void toggleJetEngines(ActionEvent event) {
		if (selectAllJet.isSelected()) {
			radioDart.setSelected(true);
			radioJuno.setSelected(true);
			radioRapier.setSelected(true);
			radioGoliath.setSelected(true);
			radioWheesley.setSelected(true);
			radioPanther.setSelected(true);
			radioWhiplash.setSelected(true);
		} else {
			radioDart.setSelected(false);
			radioJuno.setSelected(false);
			radioRapier.setSelected(false);
			radioGoliath.setSelected(false);
			radioWheesley.setSelected(false);
			radioPanther.setSelected(false);
			radioWhiplash.setSelected(false);
		}
	}

	/*
	 * toggles all solid fuel engines on and off
	 */
	@FXML
	private void toggleSolidFuelEngines(ActionEvent event) {
		if (selectAllSolidBooster.isSelected()) {
			radioShrimp.setSelected(true);
			radioMite.setSelected(true);
			radioHammer.setSelected(true);
			radioThumper.setSelected(true);
			radioKickback.setSelected(true);
			radioFlea.setSelected(true);
			radioClydesdale.setSelected(true);
			radioThoroughbred.setSelected(true);

		} else {
			radioShrimp.setSelected(false);
			radioMite.setSelected(false);
			radioHammer.setSelected(false);
			radioThumper.setSelected(false);
			radioKickback.setSelected(false);
			radioFlea.setSelected(false);
			radioClydesdale.setSelected(false);
			radioThoroughbred.setSelected(false);

		}
	}

	/*
	 * default example values to load into the form
	 */
	@FXML
	private void handleLoadExampleAction(ActionEvent event) {
		minDVField.setText("3000");
		maxDVField.setText("4000");
		minTWRField.setText("1");
		maxTWRField.setText("2");
		gravityField.setText("9.81");
		payloadField.setText("10");
		atmosphereField.setText("1");
		numEnginesField.setText("10");
		planetsGravityMenu.setText("Kerbin");
		planetsPressureMenu.setText("Kerbin");

	}

	@FXML
	private void handleSetGravityField(ActionEvent event) {
		String planetNameGravity = ((MenuItem) event.getSource()).getText();
		handlePlanetFields(planetNameGravity);

	}

	private void handlePlanetFields(String planet) {
		for (int i = 0; i < planetValueList.size(); i++) {

			if (planet.equalsIgnoreCase(planetValueList.get(i).get(0))) {
				gravityField.setText(planetValueList.get(i).get(1));
				planetsGravityMenu.setText(planetValueList.get(i).get(0));

				atmosphereField.setText(planetValueList.get(i).get(2));
				planetsPressureMenu.setText(planetValueList.get(i).get(0));
				break;
			}
		}
	}

	private void handleVacuum() {
		atmosphereField.setText("0");
		planetsPressureMenu.setText("Vacuum");
	}

	@FXML
	private void handleSetAtmosphereField(ActionEvent event) {
		String planetNameAtmosphere = ((MenuItem) event.getSource()).getText();
		if (planetNameAtmosphere.equals("Vacuum")) {
			handleVacuum();
		} else {
			handlePlanetFields(planetNameAtmosphere);
		}
	}

	/*
	 * checks for selected radio buttons, buttons that are selected are added to
	 * requestedEngineList and creates a graph series. non-selected are ignored
	 */
	private void checkRadioButtons() {
		// Small engines
		if (radioSpark.isSelected()) {
			requestedEnginesList.add("Spark");
			XYChart.Series Spark = new XYChart.Series();
			Spark.setName("Spark");
			seriesArray.add(Spark);
		}
		if (radioThud.isSelected()) {
			requestedEnginesList.add("Thud");
			XYChart.Series Thud = new XYChart.Series();
			Thud.setName("Thud");
			seriesArray.add(Thud);
		}
		if (radioDawn.isSelected()) {
			if (dawnSolarPanelsCheck.isSelected()) {
				engines.Engine.addDawnPanelMass(); // add approximate mass required to generate enough power via solar
													// panels
			} else {
				engines.Engine.noDawnPanelMass();
			}
			requestedEnginesList.add("Dawn");
			XYChart.Series Dawn = new XYChart.Series();
			Dawn.setName("Dawn");
			seriesArray.add(Dawn);
		}

		// Medium Engines
		if (radioReliant.isSelected()) {
			requestedEnginesList.add("Reliant");
			XYChart.Series Reliant = new XYChart.Series();
			Reliant.setName("Reliant");
			seriesArray.add(Reliant);
		}
		if (radioTerrier.isSelected()) {
			requestedEnginesList.add("Terrier");
			XYChart.Series Terrier = new XYChart.Series();
			Terrier.setName("Terrier");
			seriesArray.add(Terrier);
		}
		if (radioSwivel.isSelected()) {
			requestedEnginesList.add("Swivel");
			XYChart.Series Swivel = new XYChart.Series();
			Swivel.setName("Swivel");
			seriesArray.add(Swivel);
		}
		if (radioVector.isSelected()) {
			requestedEnginesList.add("Vector");
			XYChart.Series Vector = new XYChart.Series();
			Vector.setName("Vector");
			seriesArray.add(Vector);
		}
		if (radioNerv.isSelected()) {
			requestedEnginesList.add("Nerv");
			XYChart.Series Nerv = new XYChart.Series();
			Nerv.setName("Nerv");
			seriesArray.add(Nerv);
		}

		// Large Engines
		if (radioPoodle.isSelected()) {
			requestedEnginesList.add("Poodle");
			XYChart.Series Poodle = new XYChart.Series();
			Poodle.setName("Poodle");
			seriesArray.add(Poodle);
		}
		if (radioSkipper.isSelected()) {
			requestedEnginesList.add("Skipper");
			XYChart.Series Skipper = new XYChart.Series();
			Skipper.setName("Skipper");
			seriesArray.add(Skipper);
		}
		if (radioMainsail.isSelected()) {
			requestedEnginesList.add("Mainsail");
			XYChart.Series Mainsail = new XYChart.Series();
			Mainsail.setName("Mainsail");
			seriesArray.add(Mainsail);
		}
		if (radioTwinboar.isSelected()) {
			requestedEnginesList.add("Twin-Boar");
			XYChart.Series Twinboar = new XYChart.Series();
			Twinboar.setName("Twinboar");
			seriesArray.add(Twinboar);
		}
		if (radioMammoth.isSelected()) {
			requestedEnginesList.add("Mammoth");
			XYChart.Series Mammoth = new XYChart.Series();
			Mammoth.setName("Mammoth");
			seriesArray.add(Mammoth);
		}
		if (radioRhino.isSelected()) {
			requestedEnginesList.add("Rhino");
			XYChart.Series Rhino = new XYChart.Series();
			Rhino.setName("Rhino");
			seriesArray.add(Rhino);
		}

		// Jet Engines
		if (radioDart.isSelected()) {
			requestedEnginesList.add("Dart");
			XYChart.Series Dart = new XYChart.Series();
			Dart.setName("");
			seriesArray.add(Dart);
		}
		if (radioJuno.isSelected()) {
			requestedEnginesList.add("Juno");
			XYChart.Series Juno = new XYChart.Series();
			Juno.setName("Juno");
			seriesArray.add(Juno);
		}
		if (radioRapier.isSelected()) {
			requestedEnginesList.add("R.A.P.I.E.R.");
			XYChart.Series Rapier = new XYChart.Series();
			Rapier.setName("R.A.P.I.E.R.");
			seriesArray.add(Rapier);
		}
		if (radioGoliath.isSelected()) {
			requestedEnginesList.add("Goliath");
			XYChart.Series Goliath = new XYChart.Series();
			Goliath.setName("Goliath");
			seriesArray.add(Goliath);
		}
		if (radioWheesley.isSelected()) {
			requestedEnginesList.add("Wheesley");
			XYChart.Series Wheesley = new XYChart.Series();
			Wheesley.setName("Wheesley");
			seriesArray.add(Wheesley);
		}
		if (radioPanther.isSelected()) {
			requestedEnginesList.add("Panther");
			XYChart.Series Panther = new XYChart.Series();
			Panther.setName("Panther");
			seriesArray.add(Panther);
		}
		if (radioWhiplash.isSelected()) {
			requestedEnginesList.add("Whiplash");
			XYChart.Series Whiplash = new XYChart.Series();
			Whiplash.setName("Whiplash");
			seriesArray.add(Whiplash);
		}

		// Solid Fuel Booster
		if (radioShrimp.isSelected()) {
			requestedEnginesList.add("Shrimp");
			XYChart.Series Shrimp = new XYChart.Series();
			Shrimp.setName("Shrimp");
			seriesArray.add(Shrimp);
		}
		if (radioMite.isSelected()) {
			requestedEnginesList.add("Mite");
			XYChart.Series Mite = new XYChart.Series();
			Mite.setName("Mite");
			seriesArray.add(Mite);
		}
		if (radioHammer.isSelected()) {
			requestedEnginesList.add("Hammer");
			XYChart.Series Hammer = new XYChart.Series();
			Hammer.setName("Hammer");
			seriesArray.add(Hammer);
		}
		if (radioFlea.isSelected()) {
			requestedEnginesList.add("Flea");
			XYChart.Series Flea = new XYChart.Series();
			Flea.setName("Flea");
			seriesArray.add(Flea);
		}
		if (radioThumper.isSelected()) {
			requestedEnginesList.add("Thumper");
			XYChart.Series Thumper = new XYChart.Series();
			Thumper.setName("Thumper");
			seriesArray.add(Thumper);
		}
		if (radioKickback.isSelected()) {
			requestedEnginesList.add("Kickback");
			XYChart.Series Kickback = new XYChart.Series();
			Kickback.setName("Kickback");
			seriesArray.add(Kickback);
		}
		if (radioClydesdale.isSelected()) {
			requestedEnginesList.add("Clydesdale");
			XYChart.Series Clydesdale = new XYChart.Series();
			Clydesdale.setName("Clydesdale");
			seriesArray.add(Clydesdale);
		}
		if (radioThoroughbred.isSelected()) {
			requestedEnginesList.add("Thoroughbred");
			XYChart.Series Thoroughbred = new XYChart.Series();
			Thoroughbred.setName("Thoroughbred");
			seriesArray.add(Thoroughbred);
		}

	}

	



	private double xValue;
	private double yValue;
	private Calculations best;

	/*
	 * method to retrieve cursor coordinates from graph it also sends the
	 * twr/delta-v point to kspdriver to find the best engine at each hovered point
	 * on graph and displays the result. on click it will retain that specific point
	 * and display best engine at that location but wont update till the graph is
	 * clicked again
	 */
	private Label createCursorGraphCoordsMonitorLabel(AreaChart<Number, Number> engineChart) {
		final Axis<Number> xAxis = engineChart.getXAxis();
		final Axis<Number> yAxis = engineChart.getYAxis();
		final Label cursorCoords = new Label();
		final Node chartBackground = engineChart.lookup(".chart-plot-background");
		for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
			if (n != chartBackground && n != xAxis && n != yAxis) {
				n.setMouseTransparent(true);
			}
		}
		chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				xValue = (double) xAxis.getValueForDisplay(mouseEvent.getX());
				yValue = (double) yAxis.getValueForDisplay(mouseEvent.getY());
				best = KSPDriver.displayBestEngines(xValue, yValue);
				Calculations secondBest = calculations.Calculations.getSecondBest();
				if (best != null && best.getNumengineList() > 0) {
					outputTWR.setText("TWR: " + String.format("%.2f", yValue));
					outputDV.setText("Delta-V: " + String.format("%.2f", xValue));
					outputBest.setText("Best: " + best.getEngine().getNickname() + " * " + best.getNumengineList()
							+ "    " + best.getTotalMass() + "(t)");
					outputPayloadFraction.setText(
							"Payload Fraction: " + String.format("%.2f", best.getPayloadFraction() * 100) + "%");
					outputBurnout.setText("Burnout Time: " + best.getBurnOutTime() + "s");
					outputPayloadt.setText("Payload: " + payloadMass);
					outputPropellant.setText("Propellant: " + best.getPropellantMass());
					outputTank.setText("Tank: " + best.getTankMass());
					outputNumEngines.setText("Engines: " + best.getEngineMass());
				} else {
					outputTWR.setText("TWR: " + String.format("%.2f", yValue));
					outputDV.setText("Delta-V: " + String.format("%.2f", xValue));
					outputBest.setText("Best: ");
					outputPayloadFraction.setText("Payload Fraction: ");
					outputBurnout.setText("Burnout Time: ");
					outputPayloadt.setText("Payload: ");
					outputPropellant.setText("Propellant: ");
					outputNumEngines.setText("Engines: ");
					outputTank.setText("Tank: ");
				}

				if (secondBest != null && secondBest.getNumengineList() > 0) {
					outputSecondBest.setText("Second Best: " + secondBest.getEngine().getNickname() + " * "
							+ secondBest.getNumengineList() + "    " + secondBest.getTotalMass() + "(t)");
					outputSecondPayloadFraction.setText(
							"Payload Fraction: " + String.format("%.2f", secondBest.getPayloadFraction() * 100) + "%");
					outputSecondBurnout.setText("Burnout Time: " + secondBest.getBurnOutTime() + "s");
					outputSecondPayload.setText("Payload: " + payloadMass);
					outputSecondPropellant.setText("Propellant: " + secondBest.getPropellantMass());
					outputSecondTank.setText("Tank: " + secondBest.getTankMass());
					outputSecondEngines.setText("Engines: " + secondBest.getEngineMass());
				} else {
					outputSecondBest.setText("Second Best: ");
					outputSecondPayloadFraction.setText("Payload Fraction: ");
					outputSecondBurnout.setText("Burnout Time: ");
					outputSecondPayload.setText("Payload: ");
					outputSecondPropellant.setText("Propellant: ");
					outputSecondEngines.setText("Engines: ");
					outputSecondTank.setText("Tank: ");
				}
			}
		});

		return cursorCoords;
	}

	double stageMass;

	@FXML
	private void handleSetStagePlanner() {
		if (best != null && best.getNumengineList() > 0) {
			stageMass = best.getTotalMass();
			txtDVStagePlanner.setText("Delta-V: " + String.format("%.2f", xValue));
			txtTWRPlanner.setText("TWR: " + String.format("%.2f", yValue));
			txtMassStagePlanner.setText("Mass: " + best.getTotalMass() + "(t)");
			txtEngineStagePlanner
					.setText("Engine: " + best.getEngine().getNickname() + " * " + best.getNumengineList());
			txtPropellantPlanner.setText(best.getPropellantMass() + "(t)");
			txtTankPlanner.setText(best.getTankMass() + "(t)");
			double tankPropellantMass = best.getTankMass() + best.getPropellantMass();
			txtTankPropellantMass.setText(String.format("%.2f", tankPropellantMass) + "(t)");
		}
	}

	/*
	 * loads mass from stage planner to input form to help start the next stage
	 * planning
	 */
	private void handleLoadValues(ActionEvent event) {
		String mass = String.valueOf(stageMass);
		payloadField.setText(mass);
	}

	@FXML
	private void handleOpenInstructions() {
		if (paneInstruction.isVisible() == false) {
			paneInstruction.setVisible(true);
		} else {
			paneInstruction.setVisible(false);
		}
	}

	private void handleCloseInstructions() {
		paneInstruction.setVisible(false);
	}
}
