/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageplanner;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author pekin
 */
public class StagePlannerController implements Initializable {

	/**
	 * Initializes the controller class.
	 */

	@FXML
	private MenuButton startPlanet;
	@FXML
	private MenuButton endPlanet;
	@FXML
	private RadioButton startLanded;
	@FXML
	private RadioButton startOrbit;
	@FXML
	private RadioButton endLanded;
	@FXML
	private RadioButton endOrbit;
	@FXML
	private CheckBox aerobrake;
	@FXML
	private Button calculate;
	@FXML
	private ScrollPane stageOutputPane;
	@FXML
	private TextArea dvTextArea;
	@FXML
	private Spinner<Integer> errorProtection;
	@FXML
	private ImageView dvMap;

	private KSPOneDVMap KSPOneDV = new KSPOneDVMap();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		SpinnerValueFactory<Integer> valueFactory = //
				new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 20);
		errorProtection.setValueFactory(valueFactory);
		Image randomImage = new Image(getClass().getClassLoader().getResource("stageplanner/dvmap.jpg").toString(),
				true);
		dvMap.setImage(randomImage);

	}

	@FXML
	private void handleStartPlanet(ActionEvent event) {
		startPlanet.setText(((MenuItem) event.getSource()).getText());

	}

	@FXML
	private void handleEndPlanet(ActionEvent event) {
		endPlanet.setText(((MenuItem) event.getSource()).getText());
	}

	@FXML
	private void handleCalculate(ActionEvent event) {
		try {
			calculate();
			calculate.setStyle(null);

		} catch (Exception e) {
			e.printStackTrace();
			calculate.setStyle("-fx-background-color: #ff0000; ");
		}
	}

	private int stage = 0;
	private String output = "";
	private int totalDV = 0;

	public void calculate() throws Exception {

		String start = startPlanet.getText();
		String startLocation;
		int startLocationInt;
		if (startLanded.isSelected()) {
			startLocation = "Currently on";
			startLocationInt = 0;
		} else {
			startLocation = "Currently orbiting";
			startLocationInt = 1;
		}
		String end = endPlanet.getText();
		String endLocation;
		int endLocationInt;
		if (endLanded.isSelected()) {
			endLocation = "land on";
			endLocationInt = 0;
		} else {
			endLocation = "orbit";
			endLocationInt = 1;
		}
		boolean aerobrakeCheck;
		if (aerobrake.isSelected()) {
			aerobrakeCheck = true;
		} else {
			aerobrakeCheck = false;
		}
		int baseDV = KSPOneDV.findDV(start, startLocationInt, end, endLocationInt, aerobrakeCheck);
		int errorDV = (int) (baseDV * ((double) errorProtection.getValue() / 100));
		int stageTotalDV = baseDV + errorDV;
		totalDV += stageTotalDV;
		output = "\nStage " + stage + "\n" + startLocation + " " + start + ", going to " + endLocation + " " + end
				+ "\nCost: \t" + stageTotalDV + " Delta-V" + "\nTotal:\t" + totalDV + " Delta-V"
				+ "\n - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -";

		dvTextArea.appendText(output);
		stage++;
	}

	@FXML
	private void handleReset(ActionEvent event) {
		stage = 0;
		totalDV = 0;
		dvTextArea.setText("");
	}
}
