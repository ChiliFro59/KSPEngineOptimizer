package main;

import java.util.ArrayList;
import java.util.List;
//import main.KSPOneController;
import calculations.Calculations;
import engines.Engine;

/**
 *
 * @author pekin
 */
public class KSPDriver {
	private static calculations.Calculations best = null;
	private static List<engines.Engine> engineList = new ArrayList<>();
	private static ArrayList<calculations.Calculations> requestedEngineList = new ArrayList<>();
	private static calculations.AtmosphereCurve atmoCurve = new calculations.AtmosphereCurve();
	private static calculations.Calculations calculations = new calculations.Calculations();
	private static KSPOneController KSPHome = new KSPOneController();
	private static engines.Engine engine = new engines.Engine();
	private static double findCurrentISP;
	private static double standardGravity = 9.80655;
	private static double currentGravity;
	private static double currentPressure;
	private static double payload;
	private static double minDV;
	private static double currentDV = 0;
	private static double maxDV;
	private static double minTWR;
	private static double currentTWR = 0;
	private static double maxTWR;
	private static int maxEngines;
	private static List<String> requestedEngines;

	static public List<Double> graphPoints = new ArrayList<>();

	/*
	 * sets the requested values from input form to be processed, sets a graph
	 * points and returned
	 */
	public static void setRequestedValues(List<String> requestedEnginesr, double payloadMassr, double gravityr,
			double minDVr, double maxDVr, double minTWRr, double maxTWRr, double atmoPressurer, int numEnginesr) {
		requestedEngines = requestedEnginesr;
		payload = payloadMassr;
		currentGravity = gravityr;
		minDV = minDVr;
		maxDV = maxDVr;
		minTWR = minTWRr;
		maxTWR = maxTWRr;
		currentPressure = atmoPressurer;
		maxEngines = numEnginesr;
		start();
	}

	private static void start() {

		engineList = engine.createRequestedEngineList(requestedEngines);

		for (int i = 0; i < engineList.size(); i++) {
			findCurrentISP = atmoCurve.atmosphereCurve(engineList.get(i).getISPPoints(), currentPressure);
			engineList.get(i).setCurrentISP(findCurrentISP);
			engineList.get(i).setCurrentThrust();
			// currentTWR = (currentThrust/(mass*currentGravity)); //in the old code
			engineList.get(i).setCurrentTWR(currentGravity);// something seems wrong here
			Calculations currentEngine = null;
			requestedEngineList.add(calculations.fullAnalysis(engineList.get(i), payload, minDV, minTWR, maxEngines));
			double highestDV = 0;
			double plotTWRChange = (maxTWR - minTWR) / 500;
			double plotDVChange = (maxDV - minDV) / 500;

			for (double plotTWR = maxTWR; plotTWR >= minTWR + .000001; plotTWR -= plotTWRChange) {
				for (double plotDV = minDV; plotDV <= maxDV + .0; plotDV += plotDVChange) {
					currentEngine = calculations.fullAnalysis(engineList.get(i), payload, plotDV, plotTWR, maxEngines);

					if (currentEngine.getNumengineList() != -1) {
						if (plotDV > highestDV) {
							highestDV = plotDV;
						}
					} else {
						break;
					}
				}
				graphPoints.add(plotTWR);
				graphPoints.add(highestDV);
				highestDV = 0;
			}
		}
		// System.out.println(graphPoints.size());

		// System.out.println(graphPoints);
		requestedEngineList.clear();
	}

	/*
	 * calls on full analysis to find the best and second best engine at a given twr
	 * and delta v
	 */
	static public Calculations displayBestEngines(double requestedDV, double requestedTWR) {
		best = null;
		requestedEngineList.clear();
		for (int i = 0; i < engineList.size(); i++) {
			requestedEngineList
					.add(calculations.fullAnalysis(engineList.get(i), payload, requestedDV, requestedTWR, maxEngines));
		}
		for (int i = 0; i < requestedEngineList.size(); i++) {
			best = calculations.findBestEngine(requestedEngineList, requestedTWR, requestedDV, payload, maxEngines);
		}
		return best;
	}

	protected static void engineListClear() {
		engineList.clear();
	}
	protected static List<Engine> getEngineList(){
		return engineList;
	}

}
