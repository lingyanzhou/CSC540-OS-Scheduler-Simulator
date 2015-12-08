import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Scheduler Simulator App entry point
 * 
 * @author zhoulingyan
 * @since 12,6,2015
 */
public class SchedulerSimulatorApp {

	public static void main(String[] args) throws FileNotFoundException {

		String formatName = "COURSE";
		String inPathName = "input.txt";
		String outPathName = "-";
		String methodName = "FIFO";
		String quantumName = "2";
		boolean nogui = true;

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(getOptions(), args);
			// parse the command line arguments
			if (line.hasOption("h")) {
				printHelp();
				System.exit(0);
			}
			if (line.hasOption("t")) {
				runTests();
				System.exit(0);
			}
			formatName = line.getOptionValue("f", "COURSE");
			inPathName = line.getOptionValue("i", "input.txt");
			outPathName = line.getOptionValue("o", "-");
			methodName = line.getOptionValue("m", "FIFO");
			quantumName = line.getOptionValue("q", "2");
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printHelp();
			System.exit(1);
		}

		SchedulerSimulator simulator = new SchedulerSimulator();
		simulator.run(inPathName, outPathName, methodName, formatName,
				quantumName, nogui);
	}

	private static void runTests() throws FileNotFoundException {
		String[] inPathNames = { "input/testdata1.txt", "input/testdata2.txt",
				"input/testdata3.txt" };
		String[] methodNames = { "FIFO", "SJF", "RR", "RR" };
		String[] quantumNames = { "0", "0", "2", "3" };
		String formatName = "COURSE";
		String[] outPathNames = new String[12];

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 4; ++j) {
				outPathNames[i * 4 + j] = "output/" + methodNames[j] + i
						+ ".txt";
			}
		}

		for (int i = 0; i < 3; ++i) {
			String inPathName = inPathNames[i];
			for (int j = 0; j < 4; ++j) {
				String methodName = methodNames[j];
				SchedulerSimulator simulator = new SchedulerSimulator();
				simulator.run(inPathName, outPathNames[i * 4 + j], methodName,
						formatName, quantumNames[j], true);
			}
		}

	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		PrintWriter writer = new PrintWriter(System.err);
		formatter.printHelp(writer, 80, "java -cp <path> SchedulerSimulator ",
				"CSC540 Project Simulated Schedulers Help", getOptions(), 4, 8,
				"Author: Lingyan Zhou", true);
		writer.close();
	}

	private static Options getOptions() {
		Options options = new Options();
		options.addOption(Option
				.builder("m")
				.longOpt("method")
				.hasArg()
				.desc("Scheduling algorithm. [FIFO | RR | SJF | SRT]. Default: FIFO")
				.build());
		options.addOption(Option.builder("f").longOpt("iformat").hasArg()
				.desc("Input file format. [COURSE, CSV]. Default: COURSE")
				.build());
		options.addOption(Option.builder("g").longOpt("oformat").hasArg()
				.desc("Output file format. [LATEX, CSV]. Default: CSV").build());
		options.addOption(Option.builder("q").longOpt("quantum").hasArg()
				.desc("Round robin quantum. Default: 2").build());
		options.addOption(Option.builder("i").longOpt("input").hasArg()
				.desc("Input file. Default: input.txt").build());
		options.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file. Default: - (stdout)").build());
		options.addOption(Option.builder("t").longOpt("test")
				.desc("Run the program with test data").build());
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Print this help page").build());
		return options;
	}
}
