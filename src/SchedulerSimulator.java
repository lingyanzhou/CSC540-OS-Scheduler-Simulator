import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Scheduler Simulator
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SchedulerSimulator {

	private enum SubmitterType {
		PRESET1, PRESET2, PRESET3, CSV, COURSE,
	}

	public static void main(String[] args) {

		String formatName = "COURSE";
		String inPathName = "/";
		String methodName = "FIFO";
		String quantumName = "2";

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(getOptions(), args);
			if (line.hasOption("t")) {
				runTests();
				System.exit(0);
			}
			formatName = line.getOptionValue("f", "COURSE");
			inPathName = line.getOptionValue("i");
			methodName = line.getOptionValue("m", "FIFO");
			quantumName = line.getOptionValue("q", "2");
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printHelp();
			System.exit(1);
		}
		
		

		IScheduler scheduler = null;

		if (methodName.equals("FIFO")) {
			scheduler = new FIFOScheduler();
		} else if (methodName.equals("SJF")) {
			scheduler = new SJFScheduler();
		} else if (methodName.equals("SRT")) {
			scheduler = new SRTScheduler();
		} else if (methodName.equals("RR")) {
			int quantum = Integer.parseInt(quantumName);
			scheduler = new RRScheduler(quantum);
		} else {
			scheduler = new FIFOScheduler();
		}
		
		SubmitterType format = SubmitterType.COURSE;
		
		if (formatName.equals("CSV")) {
			format = SubmitterType.CSV;
		} else if (formatName.equals("COURSE")) {
			format = SubmitterType.COURSE;
		} else {
			format = SubmitterType.COURSE;
		}
		
		JobSubmitter jobSubmitter = getJobSubmitter(format,
				new File(inPathName));
		for (int i = 0; scheduler.hasRunningProcess()
				|| scheduler.hasWaitingProcess()
				|| jobSubmitter.hasFutureJobs(); ++i) {
			scheduler.acceptJobs(jobSubmitter.submitJobs());
			scheduler.schedule();
			System.out.print(i);
			System.out.print(",");
			System.out.println(scheduler.reportProcessesCSV());
			scheduler.tick();
			jobSubmitter.tick();
		}
		printReport(scheduler);

	}

	private static void runTests() {
		File[] infiles = new File[3];
		infiles[0] = new File("input/testdata1.txt");
		infiles[1] = new File("input/testdata2.txt");
		infiles[2] = new File("input/testdata3.txt");

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < infiles.length; ++j) {
				IScheduler scheduler = getScheduler(i);
				File infile = infiles[j];

				System.out.println("#============================");
				System.out.println("#============================");
				System.out.println("#Scheduler: " + scheduler.getName());
				System.out.println("#Input: " + infile.getName());
				System.out.println("#- - - - - - - - - - - - - - -");

				JobSubmitter jobSubmitter = getJobSubmitter(
						SubmitterType.COURSE, infile);
				for (int k = 0; (scheduler.hasRunningProcess()
						|| scheduler.hasWaitingProcess() || jobSubmitter
							.hasFutureJobs()); ++k) {
					scheduler.acceptJobs(jobSubmitter.submitJobs());
					scheduler.schedule();
					System.out.print(k);
					System.out.print(",");
					System.out.println(scheduler.reportProcessesCSV());
					scheduler.tick();
					jobSubmitter.tick();
				}
				printReport(scheduler);
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
		options.addOption(Option.builder("q").longOpt("quantum").hasArg()
				.desc("Round robin quantum. Default: 2").build());
		options.addOption(Option.builder("i").longOpt("input")
				.hasArg().desc("Input file").build());
		options.addOption(Option.builder("t").longOpt("test")
				.desc("Run the program with test data").build());
		return options;
	}

	private static IScheduler getScheduler(int id) {
		switch (id) {
		case 0:
			return new FIFOScheduler();
		case 1:
			return new SJFScheduler();
		case 2:
			return new RRScheduler(2);
		default:
			return new RRScheduler(3);
		}
	}

	private static JobSubmitter getJobSubmitter(SubmitterType flag, File infile) {
		switch (flag) {
		case PRESET1: {
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(new Job("job1", 24, 0));
			jobSubmitter.add(new Job("job2", 3, 0));
			jobSubmitter.add(new Job("job3", 3, 0));
			return jobSubmitter;
		}
		case PRESET2: {
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(new Job("job1", 6, 0));
			jobSubmitter.add(new Job("job2", 8, 0));
			jobSubmitter.add(new Job("job3", 7, 0));
			jobSubmitter.add(new Job("job4", 3, 0));
			return jobSubmitter;
		}
		case PRESET3: {
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(new Job("job1", 8, 0));
			jobSubmitter.add(new Job("job2", 4, 1));
			jobSubmitter.add(new Job("job3", 9, 2));
			jobSubmitter.add(new Job("job4", 5, 3));
			return jobSubmitter;
		}
		case CSV: {
			ArrayList<Job> jobs = CSVFileParser.parse(infile);
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(jobs);
			return jobSubmitter;
		}
		case COURSE: {
			ArrayList<Job> jobs = CourseFileParser.parse(infile);
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(jobs);
			return jobSubmitter;
		}
		default: {
			return null;
		}
		}

	}

	public static void printReport(IScheduler sched) {
		int procCount = sched.reportTotalProcessCount();
		System.out.println("#============================");
		System.out.print("#Total Process Count: ");
		System.out.println(procCount);
		System.out.print("#Total Waiting Time: ");
		System.out.println(sched.reportTotalWaitingTime());
		System.out.print("#Average Waiting Time: ");
		System.out.println((float) sched.reportTotalWaitingTime()
				/ (float) procCount);
		System.out.print("#Total Turnaround Time: ");
		System.out.println(sched.reportTotalTurnAroundTime());
		System.out.print("#Average Turnaround Time: ");
		System.out.println((float) sched.reportTotalTurnAroundTime()
				/ (float) procCount);
		System.out.print("#Total Context Switch Count: ");
		System.out.println(sched.reportTotalContextSwitchCount());
	}

}
