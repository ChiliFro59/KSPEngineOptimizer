package calculations;

import java.util.ArrayList;
import java.util.List;
//some calculation help from https://meithan.net/KSP/engines/
public class AtmosphereCurve {
	// Spline curve for atmospheric Isp values
	// Uses a Hermite cubic spline
	private static engines.Engine engine = new engines.Engine();
	//figures out the curve of engine efficiency based on isp points and current planet 
	public double atmosphereCurve(double[][] points, double currentPressure) {
		List<Double> x = new ArrayList<>();
		List<Double> y = new ArrayList<>();

		// double[] x = new double[3];
		// double[] y = new double[3];
		if (currentPressure < 0) {
			currentPressure = 0;
		}

		for (int i = 0; i < points.length; i++) {
			x.add(points[i][0]);
			y.add(points[i][1]);
		}
		int num = points.length;

		// Compute tangents
		int last = num - 1;
		List<Double> m = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			if (i == 0) {
				m.add(i, (y.get(1) - y.get(0)) / (x.get(1) - x.get(0)));
			} else if (i == last) {
				m.add(i, ((y.get(last) - y.get(last - 1) / (x.get(last) - x.get(last - 1)))));

			} else {
				m.add(i, (0.5 * ((y.get(i + 1) - y.get(i)) / (x.get(i + 1) - x.get(i))
						+ (y.get(i) - y.get(i - 1)) / (x.get(i) - x.get(i - 1)))));
			}
		}

		// Evaluate the curve for some value x
		// this.evaluate = function (x) {
		// Determine the segment
		int k = 0;
		if (currentPressure < x.get(0)) {
			k = 0;
		}
		if (currentPressure > x.get(last)) {

			k = last - 1;
			return 0;
		} else {
			for (int i = 0; i < last; i++) { // linear search at the moment
				if ((currentPressure >= x.get(i)) && (currentPressure <= x.get(i + 1))) {
					k = i;
					break;
				}
			}
		}
		// Evaluate the Hermite spline
		double t = (currentPressure - x.get(k)) / (x.get(k + 1) - x.get(k));
		double currentISP = (2 * t * t * t - 3 * t * t + 1) * y.get(k)
				+ (t * t * t - 2 * t * t + t) * (x.get(k + 1) - x.get(k)) * m.get(k)
				+ (-2 * t * t * t + 3 * t * t) * y.get(k + 1)
				+ (t * t * t - t * t) * (x.get(k + 1) - x.get(k)) * m.get(k + 1);
		// currentISP = (double)Math.round(currentISP * 10000d) / 10000d;
		return currentISP;
	}
}
