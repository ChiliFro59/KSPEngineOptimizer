package stageplanner;

public class KSPOneDVMap {
	private String name;
	private int landed;
	private int circularize;
	private int breakOrbit;
	private static KSPOneDVMap kerbin;
	private static KSPOneDVMap keostationaryOrbit;
	private static KSPOneDVMap mun;
	private static KSPOneDVMap minmus;
	private static KSPOneDVMap kerbol;
	private static KSPOneDVMap eeloo;
	private static KSPOneDVMap moho;
	private static KSPOneDVMap eve;
	private static KSPOneDVMap gilly;
	private static KSPOneDVMap duna;
	private static KSPOneDVMap ike;
	private static KSPOneDVMap jool;
	private static KSPOneDVMap laythe;
	private static KSPOneDVMap vall;
	private static KSPOneDVMap tylo;
	private static KSPOneDVMap bop;
	private static KSPOneDVMap pol;
	private static KSPOneDVMap dres;
	private boolean aerobrake;
	private KSPOneDVMap start;
	private KSPOneDVMap end;

	public KSPOneDVMap(String name, int landed, int circularize, int breakOrbit, boolean aerobrake) {
		this.name = name;
		this.landed = landed; // landed to circularized and circularized to landed
		this.circularize = circularize; // eliptical / flyby to circularized and circlularized to elliptical / break
										// orbit
		this.breakOrbit = breakOrbit; // middle ground transit between planets
		this.aerobrake = aerobrake;
	}

	// landed to circle, circle to eliptical, eliptical to capture, capture to
	// circularized, circularized to landed
	// landed to circle, circle to capture, capture to circle, circle to landed
	public KSPOneDVMap() {
		// TODO Auto-generated constructor stub
	}

	public void createDVMap() {
		kerbin = new KSPOneDVMap("Kerbin", 3400, 0, 950, true);
		keostationaryOrbit = new KSPOneDVMap("Keostationary", 3400, 1115, 0, true);
		mun = new KSPOneDVMap("Mun", 580, 310, 860, false);
		minmus = new KSPOneDVMap("Minmus", 180, 160, 930, false);
		kerbol = new KSPOneDVMap("Kerbol", 67000, 13700, 6000, true);
		moho = new KSPOneDVMap("Moho", 870, 2410, 760, false);
		eve = new KSPOneDVMap("Eve", 8000, 1330 + 80, 90, true);
		gilly = new KSPOneDVMap("Gilly", 30, 410 + 60 + 80, 90, false);
		duna = new KSPOneDVMap("Duna", 1450, 360 + 250, 130, true);
		ike = new KSPOneDVMap("Ike", 390, 180 + 30 + 250, 130, false);
		eeloo = new KSPOneDVMap("Eeloo", 620, 1370, 1140, false);
		jool = new KSPOneDVMap("Jool", 14000, 2810 + 160, 980, true);
		laythe = new KSPOneDVMap("Laythe", 2900, 1070 + 930 + 160, 980, true);
		vall = new KSPOneDVMap("Vall", 860, 910 + 620 + 160, 980, false);
		tylo = new KSPOneDVMap("Tylo", 2270, 1100 + 400 + 160, 980, false);
		bop = new KSPOneDVMap("Bop", 230, 900 + 220 + 160, 980, false);
		pol = new KSPOneDVMap("Pol", 130, 820 + 160 + 160, 980, false);
		dres = new KSPOneDVMap("Dres", 430, 1290, 610, false);
	}

	public KSPOneDVMap getPlanetDVValues(String planet) {

		switch (planet) {
		case "Kerbin":
			return kerbin;
		case "Keostationary Orbit":
			return keostationaryOrbit;
		case "Mun":
			return mun;
		case "Minmus":
			return minmus;
		case "Kerbol":
			return kerbol;
		case "Moho":
			return moho;
		case "Eve":
			return eve;
		case "Gilly":
			return gilly;
		case "Duna":
			return duna;
		case "Ike":
			return ike;
		case "Eeloo":
			return eeloo;
		case "Jool":
			return jool;
		case "Laythe":
			return laythe;
		case "Vall":
			return vall;
		case "Tylo":
			return tylo;
		case "Bop":
			return bop;
		case "Pol":
			return pol;
		case "Dres":
			return dres;
		default:
			return null;
		}

	}
	//location: 0 = landed, 1 = orbit
	//finds the dv based on provided values and checks
	public int findDV(String startPlanet, int startLocation, String endPlanet, int endLocation,
			boolean aerobrakeCheck) {
		start = getPlanetDVValues(startPlanet);
		end = getPlanetDVValues(endPlanet);
		int totalDV = 0;
		//if starting landed on a planet include landed cost otherwise dont
		if (startLocation == 0) {
			totalDV = start.landed + start.circularize + start.breakOrbit;
		} else {
			totalDV = start.breakOrbit + start.circularize;
		}
		//if ending landed included landed cost othewise dont
		if (endLocation == 0) {
			totalDV += end.breakOrbit + end.circularize + end.landed;
		} else {
			totalDV += end.breakOrbit + end.circularize;
		}
		//minmus and mun require different handling due to orbiting kerbin
		if (endPlanet.equals("Mun") || endPlanet.equals("Minmus")) {
			if (endLocation == 0) {
				totalDV = kerbin.landed + end.breakOrbit + end.circularize + end.landed;
			} else {
				totalDV = kerbin.landed + end.breakOrbit + end.circularize;
			}
			if (startLocation == 1) {
				totalDV -= kerbin.landed;
			}
		}
		if (startPlanet.equals("Mun") || startPlanet.equals("Minmus")) {
			if (endLocation == 0) {
				totalDV = start.landed + start.circularize + start.breakOrbit + kerbin.landed;
			} else {
				totalDV = start.landed + start.circularize + start.breakOrbit;

			}
			if (startLocation == 1) {
				totalDV -= start.landed;
			}
			if (aerobrakeCheck) {
				if (end.aerobrake) {
					if (endLocation == 0) {
						totalDV -= start.breakOrbit + end.circularize + end.landed;
					} else {
						totalDV -= start.breakOrbit + end.circularize;
					}
				}
			}
			//means that you start and end on or orbiting the same planet
			if (startPlanet.equals(endPlanet)) {
				totalDV = 0;
				if (startLocation == 0 && endLocation == 0 || startLocation == 1 && endLocation == 1) {
					totalDV = start.landed + start.landed;
				} else {
					totalDV = start.landed;
				}

			}
			return totalDV;
		}
		//handles cases where aerobraking is possible and the user requested it
		if (aerobrakeCheck) {
			if (end.aerobrake) {
				if (endLocation == 0) {
					totalDV -= start.breakOrbit + end.breakOrbit + end.circularize + end.landed;
				} else {
					totalDV -= start.breakOrbit + end.circularize + end.breakOrbit;
				}
				if (startPlanet.equals("Kerbin")) {
					totalDV += kerbin.breakOrbit;
				}
			}
		}
		if (startPlanet.equals(endPlanet)) {
			totalDV = 0;
			if (startLocation == 0 && endLocation == 0 || startLocation == 1 && endLocation == 1) {
				totalDV = start.landed + start.landed;
				if (aerobrakeCheck && end.aerobrake) {
					totalDV = start.landed;
				}
			} else {
				totalDV = start.landed;
				if (endLocation == 0 && aerobrakeCheck && end.aerobrake) {
					totalDV = 0;
				}
			}

		}
		if (endPlanet.equals("Keostationary Orbit")) {
			totalDV = end.landed + end.circularize;
		}
		return totalDV;
	}

	public String getName() {
		return name;
	}
}
