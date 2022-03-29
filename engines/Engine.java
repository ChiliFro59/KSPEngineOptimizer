/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engines;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pekin
 */
public class Engine {
	private static double dawnPanel = 0.15;
	private static double dawnNewMass;
	private static double dawnOriginalMass;
	private String name;
	private String nickname;
	private double mass;
	private double vacThrust;
	private double[][] ISPPoints;
	private double propellantWeightRatio;
	private double currentISP;
	private double currentThrust;
	private double currentTWR;
	private double maxDVPossible;
	private static List<Engine> requestedEngineList = new ArrayList<>();
	private static List<Engine> fullEngineList = new ArrayList<>();
	private Engine tempEngine;

	Engine(String name, String nickname, double mass, double vacThrust, double[][] ISPPoints, double propellantRatio,
			double currentISP, double currentThrust, double currentTWR, double maxDVPossible) {
		this.name = name;
		this.nickname = nickname;
		this.mass = mass;
		this.vacThrust = vacThrust;
		this.ISPPoints = ISPPoints;
		this.propellantWeightRatio = propellantRatio;
		this.currentISP = currentISP;
		this.currentThrust = currentThrust;
		this.currentTWR = currentTWR;
		this.maxDVPossible = maxDVPossible;
	}
//creates engine object and adds to the fullEngineList

	private void CreateEngine() {
		tempEngine = new Engine(name, nickname, mass, vacThrust, ISPPoints, propellantWeightRatio, currentISP,
				currentThrust, currentTWR, maxDVPossible);
		if (nickname.equals("Dawn")) {
			dawnOriginalMass = mass;
		}
		fullEngineList.add(tempEngine);

	}
//verifys engine values that are being passed, if correct, creates an engine object

	public void VerifyCreateEngine(String[] passedEngineValues) {
		String[] engineValues = passedEngineValues;
		boolean cleared;

		try {
			name = engineValues[0];
			nickname = engineValues[1];
			mass = Double.valueOf(engineValues[2]);
			vacThrust = Double.valueOf(engineValues[3]);
			ISPPoints = setISPPairs(Double.valueOf(engineValues[4]), Double.valueOf(engineValues[5]),
					Double.valueOf(engineValues[6]), Double.valueOf(engineValues[7]), Double.valueOf(engineValues[8]),
					Double.valueOf(engineValues[9]));
			propellantWeightRatio = Double.valueOf(engineValues[10]);
			currentISP = Double.valueOf(engineValues[11]);
			currentThrust = Double.valueOf(engineValues[12]);
			currentTWR = Double.valueOf(engineValues[13]);
			maxDVPossible = Double.valueOf(engineValues[14]);
			cleared = true;
			if (engineValues.length != 15) {
				System.out
						.println("Engine values are missing or extra ones included, " + engineValues.length + " of 15");
				cleared = false;
			}
		} catch (NumberFormatException numberFormat) {
			System.out.println("Number format incorrect ");
			numberFormat.printStackTrace();
			System.out.println("Only name and nickname should use letters");
			cleared = false;
		} catch (ArrayIndexOutOfBoundsException indexOutOfBounds) {
			System.out.println("Engine values are missing or extra ones included, " + engineValues.length + " of 15");
			cleared = false;
		} catch (Exception e) {
			System.out.println("There was an issue with engine values please fix " + e);
			cleared = false;
		}
		if (cleared) {
			CreateEngine();
		}

	}

	public Engine() {

	}

	private static double[][] setISPPairs(double x0, double y0, double x1, double y1, double x2, double y2) {

		double[][] ISPPairs = { { x0, y0 }, { x1, y1 }, { x2, y2 } };

		return ISPPairs;
	}

	public List<Engine> createRequestedEngineList(List<String> engineList) {
		for (String requestedName : engineList) {
			for (Engine allNames : fullEngineList) {
				if (allNames.getNickname().equals(requestedName)) {
					if (requestedName.equals("Dawn")) {
						// allNames.mass += dawnPanel;
						allNames.mass = dawnNewMass;
					}
					requestedEngineList.add(allNames);
					break;
				}
			}
		}

		return requestedEngineList;
	}
//setters and getters for engine object

	public static void addDawnPanelMass() {
		dawnNewMass = dawnPanel + dawnOriginalMass;
	}

	public static void noDawnPanelMass() {
		dawnNewMass = dawnOriginalMass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getVacThrust() {
		return vacThrust;
	}

	public void setVacThrust(double vacThrust) {
		this.vacThrust = vacThrust;
	}

	public double[][] getISPPoints() {
		return ISPPoints;
	}

	public void setISPPoints(double[][] ISPPoints) {
		this.ISPPoints = ISPPoints;
	}

	public double getPropellantWeightRatio() {
		return propellantWeightRatio;
	}

	public void setPropellantWeightRatio(double propellantWeightRatio) {
		this.propellantWeightRatio = propellantWeightRatio;
	}

	public double getCurrentISP() {
		return currentISP;
	}

	public void setCurrentISP(double currentISP) {
		this.currentISP = currentISP;
	}

	public double getCurrentThrust() {
		return currentThrust;
	}

	public void setCurrentThrust() {
		this.currentThrust = (vacThrust * currentISP / ISPPoints[0][1]);
		// currentThrust = (double)Math.round(currentThrust * 10000d) / 10000d;
	}

	public double getCurrentTWR() {
		return currentTWR;
	}

	public void setCurrentTWR(double currentGravity) {
		this.currentTWR = (currentThrust / (mass * currentGravity));
		// currentTWR = (double)Math.round(currentTWR * 10000d) / 10000d;
	}

	public double getMaxDVPossible() {
		return maxDVPossible;
	}

	public void setMaxDVPossible(double maxDVPossible) {
		this.maxDVPossible = maxDVPossible;
	}

	public static List<Engine> getRequestedEngineList() {
		return requestedEngineList;
	}

	public static List<Engine> getFullEngineList() {
		return fullEngineList;
	}

	public void clearEngineList() {
		fullEngineList.clear();
	}

}
