package calculations;

import java.util.ArrayList;
import engines.Engine;
//some calculation help from https://meithan.net/KSP/engines/
public class Calculations {
	// Calculations
	private Engine engine;
	private double result;
	private double totalMass;
	private int numengineList;
	private double payloadFraction;
	private double totalThrust;
	private double engineMass;
	private double tankMass;
	private double propellantMass;
	private double minAccel;
	private double maxAccel;
	private double actualDV;
	private double mDot;
	private double burnOutTime;
	private double avgAccel;
	private double veff;
	private double burntime100;
	private double burntime200;
	private double burntime500;
	private double burntime1000;
	private double nEngTerm;
	private double DVTerm;
	private double standardGravity = 9.80655;
	private Calculations best = null;
	private static Calculations secondBest = null;
	// static engines.Engine engine = new engines.Engine();

	/*
	 * cretes a calculation object to store engine and information at current twr
	 * and delta v
	 */
	Calculations(Engine engine, double totalMass, int numengineList, double payloadFraction, double minAccel,
			double avgAccel, double maxAccel, double burnOutTime, double propellantMass, double tankMass,
			double engineMass, double burntime100, double burntime200, double burntime500, double burntime1000,
			double DVTerm, double nEngTerm) {

		this.engine = engine;
		// this.result = result;
		this.totalMass = totalMass;
		this.numengineList = numengineList;
		this.payloadFraction = payloadFraction;
		// this.totalThrust = totalThrust;
		this.engineMass = engineMass;
		this.tankMass = tankMass;
		this.propellantMass = propellantMass;
		this.minAccel = minAccel;
		this.maxAccel = maxAccel;
		// this.actualDV = actualDV;
		// this.mDot = mDot;
		this.burnOutTime = burnOutTime;
		this.avgAccel = avgAccel;
		// this.veff = veff;
		this.burntime100 = burntime100;
		this.burntime200 = burntime200;
		this.burntime500 = burntime500;
		this.burntime1000 = burntime1000;
		this.DVTerm = DVTerm;
		this.nEngTerm = nEngTerm;
	}

	public double getnEngineTerm() {
		return nEngTerm;
	}

	Calculations(Engine engine, double currentISP, double currentThrust, double currentTWR, double maxDVPossible,
			double nEngTerm, double DVTerm) {

	}

	public Calculations() {
		// TODO Auto-generated constructor stub
	}

	// Number of engines needed to satisfy TWR restriction
	public double calcnumengineList(Engine engine, double payload, double dv, double requestedTWR, double gravity) {
		if (requestedTWR == 0) {
			return 1;
		} else {
			double A = engine.getCurrentTWR() / requestedTWR
					* ((engine.getPropellantWeightRatio() + 1) * Math.exp(-dv / (engine.getCurrentISP() * gravity))
							- 1);
			// System.out.println(A +" "+ engine.getPropellantWeightRatio());
			if (A > engine.getPropellantWeightRatio()) {
				return (int) Math.ceil((engine.getPropellantWeightRatio() * payload / engine.getMass())
						/ (A - engine.getPropellantWeightRatio()));
			} else {
				return -1;
			}
		}
	}

	// Maximum dv for a given minTWR no limit to number of engines
	public double calcMaxDV(Engine engine, double minTWR, double gravity) {
		double maxdv = engine.getCurrentISP() * gravity * Math.log((1.0 + engine.getPropellantWeightRatio())
				/ (1.0 + engine.getPropellantWeightRatio() * minTWR / engine.getCurrentTWR()));

		if (maxdv > 0) {
			// System.out.println(maxdv);
			engine.setMaxDVPossible(maxdv);

			return (int) maxdv;
		} else {
			return -1;
		}
	}

	// Optimal mass for an engine, payload and dv combination, subject to
	// restrictions on the final TWR and the number of engineList
	// Returns the list [optimal mass, number of engineList]
	public double[][] calcMass(Engine engine, double payload, double dv, double requestedTWR, int maxengineList,
			double gravity) {
		if (dv > calcMaxDV(engine, requestedTWR, gravity)) {
			double[][] temp = { { -1, -1 } };
			return temp;
		} else {
			double numengineList = calcnumengineList(engine, payload, dv, requestedTWR, gravity);
			if (numengineList < 0 || ((maxengineList > 0) && (numengineList > maxengineList))) {
				double[][] temp = { { -1, -1 } };
				return temp;
			} else {
				double mass = (engine.getPropellantWeightRatio() * (payload + numengineList * engine.getMass()))
						/ ((engine.getPropellantWeightRatio() + 1) * Math.exp(-dv / (engine.getCurrentISP() * gravity))
								- 1.0);
				mass = (double) Math.round(mass * 100d) / 100d;
				double[][] temp = { { mass, numengineList } };
				return temp;
			}
		}
	}

	// Computes the burntime for dv given the parameters
	public double burntime(double totalThrust, double totalMass, double veff, double maxDV, double requestedDV) {
		if (requestedDV <= maxDV) {
			return totalMass / totalThrust * veff * (1 - Math.exp(-requestedDV / veff));
		} else {
			return -1;
		}
	}

	// Calls calcMass to obtain total mass and number of engineList, but also
	// computes other stuff like acceleration, burn times, etc.
	public Calculations fullAnalysis(Engine engine, double payload, double dv, double minTWR, int maxengineList) {

		double[][] result = calcMass(engine, payload, dv, minTWR, maxengineList, standardGravity);
		double totalMass = result[0][0];
		int numengineList = (int) result[0][1];
		double payloadFraction = payload / totalMass;
		payloadFraction = (double) Math.round(payloadFraction * 1000d) / 1000d;
		double totalThrust = numengineList * engine.getCurrentThrust();
		double engineMass = numengineList * engine.getMass();
		double tankMass = (totalMass - payload - engineMass) / (engine.getPropellantWeightRatio() + 1);
		tankMass = (double) Math.round(tankMass * 10d) / 10d;
		double propellantMass = engine.getPropellantWeightRatio() * tankMass;
		propellantMass = (double) Math.round(propellantMass * 10d) / 10d;
		double minAccel = totalThrust / totalMass;
		double maxAccel = totalThrust / (totalMass - propellantMass);
		double actualDV = engine.getCurrentISP() * standardGravity * Math.log(totalMass / (totalMass - propellantMass));
		double mDot = totalThrust / (engine.getCurrentISP() * standardGravity); // in t/s
		double burnOutTime = propellantMass / mDot;
		burnOutTime = (double) Math.round(burnOutTime * 10d) / 10d;
		double avgAccel = actualDV / burnOutTime;
		double veff = engine.getCurrentISP() * standardGravity;
		double burntime100 = burntime(totalThrust, totalMass, veff, actualDV, 100);
		double burntime200 = burntime(totalThrust, totalMass, veff, actualDV, 200);
		double burntime500 = burntime(totalThrust, totalMass, veff, actualDV, 500);
		double burntime1000 = burntime(totalThrust, totalMass, veff, actualDV, 1000);
		double DVTerm = (engine.getPropellantWeightRatio() + 1.0)
				* Math.exp(-dv / (engine.getCurrentISP() * standardGravity)) - 1.0;
		double nEngTerm = engine.getCurrentTWR() / minTWR * DVTerm;

		Calculations currentStats = new Calculations(engine, totalMass, numengineList, payloadFraction, minAccel,
				avgAccel, maxAccel, burnOutTime, propellantMass, tankMass, engineMass, burntime100, burntime200,
				burntime500, burntime1000, DVTerm, nEngTerm);
		return currentStats;
	}

	// Helper function to find best engine for a given dv, minTWR and payload
	// Assumes maxdv, nengterm and dvterm have already been computed
	// Returns the Engine object of the best engine, or -1 is none found
	public Calculations findBestEngine(ArrayList<Calculations> engineList, double requestedDV, double requestedTWR,
			double payload, int maxengineList) {

		// int i;
		double totalMass;
		double minmass;
		double numengineList;
		double secondMass = 1e30;
		minmass = 1e30;
		best = engineList.get(0);
		for (int i = 0; i < engineList.size(); i++) {
			if (requestedDV < engineList.get(i).engine.getMaxDVPossible()) {
				// calcDVTerms(engineList.get(i),requestedDV, requestedTWR);
				// Compute numengineList
				if (requestedTWR == 0) {
					numengineList = 1;
				} else if (engineList.get(i).nEngTerm > engineList.get(i).engine.getPropellantWeightRatio()) {
					numengineList = Math.ceil((engineList.get(i).engine.getPropellantWeightRatio() * payload
							/ engineList.get(i).engine.getMass())
							/ (engineList.get(i).nEngTerm - engineList.get(i).engine.getPropellantWeightRatio()));
				} else {
					continue;
				}
				if ((maxengineList > 0) && (numengineList > maxengineList)) {
					continue;
				}

				// Compute total mass
				totalMass = engineList.get(i).engine.getPropellantWeightRatio()
						* (payload + numengineList * engineList.get(i).engine.getMass()) / engineList.get(i).DVTerm;
				if (Double.isNaN(totalMass)) {
					continue;
				}

				// Check if better than current
				if (totalMass < minmass) {
					secondBest = best;
					best = engineList.get(i);
					minmass = totalMass;
					if (best.engine.getName().equals(secondBest.engine.getName())) {
						// System.out.println("Here");
						secondBest = null;
					}
				} else if (totalMass < secondMass) {
					secondBest = engineList.get(i);
					secondMass = secondBest.totalMass;
				}

			}
		}
		return best;

	}

	/*
	 * returns best engine at given twr and delta-v
	 */
	public Calculations getBest() {
		return best;
	}

	/*
	 * returns second best engine at given twr and delta-v
	 */
	public static Calculations getSecondBest() {
		return secondBest;
	}

	public String toString() {

		return "Engine: " + engine.getName() + " " + engine.getNickname() + "\nNumber of engines: " + numengineList
				+ "\nTotal Mass: " + totalMass + "\nPayload Fraction: " + payloadFraction * 100 + "%\nPropellant Mass: "
				+ propellantMass + "\nTank Mass: " + tankMass + "\nEngine Mass: " + engineMass + "\nBurn Time: "
				+ burnOutTime + "\nDVTerm: " + DVTerm + "\nNEngTerm " + nEngTerm;
	}

	public Engine getEngine() {
		return engine;
	}

	public int getNumengineList() {
		return numengineList;
	}

	public double getEngineMass() {
		return engineMass;
	}

	public double getTotalMass() {
		return totalMass;
	}

	public double getPayloadFraction() {
		return payloadFraction;
	}

	public double getPropellantMass() {
		return propellantMass;
	}

	public double getBurnOutTime() {
		return burnOutTime;
	}

	public double getTankMass() {
		return tankMass;
	}

}
