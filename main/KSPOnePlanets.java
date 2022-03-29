package main;

import java.util.ArrayList;
import java.util.Arrays;

public class KSPOnePlanets {
	private static String planetName = null;
	private static String planetGravity = null;
	private static String planetAtmosphere = null;
	private static ArrayList<ArrayList<String>> planetValueList = new ArrayList<ArrayList<String>>();

	public void verifyPlanetValues(String[] values) {
		boolean cleared;

		try {
			
			
			planetName = values[0];
			planetGravity = values[1];
			planetAtmosphere = values[2];
			cleared = true;
			if (values.length >= 4) {
				cleared = false;
				throw new ArrayIndexOutOfBoundsException();
			}
			if(planetName.isBlank() || planetGravity.isBlank() || planetAtmosphere.isBlank())
				throw new Exception();
			
			double test = Double.valueOf(values[1]);
			test = Double.valueOf(values[2]);
		} catch (NumberFormatException numberFormat) {
			System.out.println("Number format incorrect " + numberFormat);
			System.out.println("Only name should use letters or characters");
			cleared = false;
		} catch (ArrayIndexOutOfBoundsException indexOutOfBounds) {
			System.out.println(planetName);
			System.out.println("Planet values are missing or extra ones included, " + values.length + " of 3");
			cleared = false;
		} catch (Exception e) {
			System.out.println("There was an issue with planet values please fix " + e);
			cleared = false;
		}
		if (cleared) {
			createPlanetLists();

		}

	}

	private static void createPlanetLists() {

		planetValueList.add(new ArrayList<>(Arrays.asList(planetName, planetGravity, planetAtmosphere)));

	}

	public ArrayList<ArrayList<String>> getPlanetValueList() {
		return planetValueList;
	}
	public void clearList() {
		planetValueList.clear();
	}
}
