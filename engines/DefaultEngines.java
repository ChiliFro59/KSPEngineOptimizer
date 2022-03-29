package engines;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author pekin
 */
public class DefaultEngines {
	static Engine engine = new Engine();
	private static String version;
	FileWriter myWriter;

	public void loadEngines() throws IOException {
		
			engine.clearEngineList();
			readFromFile();
		
	}


	private void write(Engine n) throws Exception {

		myWriter.append(n.getName() + " ");
		myWriter.append(n.getNickname() + " ");
		myWriter.append(n.getMass() + " ");
		myWriter.append(n.getVacThrust() + " ");
		StringBuilder sb = new StringBuilder();
		sb.append(n.getISPPoints()[0][0] + " ");
		sb.append(n.getISPPoints()[0][1] + " ");
		sb.append(n.getISPPoints()[1][0] + " ");
		sb.append(n.getISPPoints()[1][1] + " ");
		sb.append(n.getISPPoints()[2][0] + " ");
		sb.append(n.getISPPoints()[2][1] + " ");
		myWriter.append(sb);
		myWriter.append(n.getPropellantWeightRatio() + " ");
		myWriter.append("0 0 0 0\n");

	}

	private void readFromFile() throws IOException {

		InputStream is = getClass().getResourceAsStream("/engines/engines.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		version = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] engineValues = line.split(" ");
			engine.VerifyCreateEngine(engineValues);

		}
		reader.close();

	}

}
