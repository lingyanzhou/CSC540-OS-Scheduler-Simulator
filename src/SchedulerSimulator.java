import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Scheduler Simulator
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SchedulerSimulator {

	/**
	 * Run the simulator
	 * 
	 * @param inPathName
	 *            input file path name
	 * @param methodName
	 *            scheduling algorithm
	 * @param inFormatName
	 *            file format
	 * @param quantumName
	 *            RR quantum
	 * @throws FileNotFoundException
	 */
	public void run(String inPathName, String outPathName, String methodName,
			String inFormatName, String outFormatName, String quantumName)
			throws FileNotFoundException {

		IScheduler scheduler = null;

		scheduler = createScheduler(methodName, quantumName);

		IOutputRecorder outputRecorder = null;

		outputRecorder = createOutputRecorder(scheduler, outFormatName,
				inPathName, outPathName);

		JobSubmitter jobSubmitter = createJobSubmitter(inFormatName, new File(
				inPathName));

		run(scheduler, outputRecorder, jobSubmitter);

	}

	public void run(IScheduler scheduler, IOutputRecorder outputRecorder,
			JobSubmitter jobSubmitter) throws FileNotFoundException {

		outputRecorder.begin();

		while (scheduler.hasRunningProcess() || scheduler.hasWaitingProcess()
				|| jobSubmitter.hasFutureJobs()) {
			scheduler.acceptJobs(jobSubmitter.submitJobs());
			scheduler.schedule();
			outputRecorder.tick();
			scheduler.tick();
			jobSubmitter.tick();
		}
		outputRecorder.end();

	}

	public static IOutputRecorder createOutputRecorder(IScheduler scheduler,
			String outFormatName, String inPathName, String outPathName) {
		if (outFormatName.equals("LATEX")) {
			return new LatexOutputRecorder(scheduler, inPathName, outPathName);
		} else {
			return new TextOutputRecorder(scheduler, inPathName, outPathName);
		}

	}

	public static IScheduler createScheduler(String methodName,
			String quantumName) {
		if (methodName.equals("FIFO")) {
			return new FIFOScheduler();
		} else if (methodName.equals("SJF")) {
			return new SJFScheduler();
		} else if (methodName.equals("SRT")) {
			return new SRTScheduler();
		} else if (methodName.equals("RR")) {
			int quantum = Integer.parseInt(quantumName);
			return new RRScheduler(quantum);
		} else {
			return new FIFOScheduler();
		}
	}

	public static JobSubmitter createJobSubmitter(String inFormatName,
			File infile) {
		if (inFormatName.equals("CSV")) {
			ArrayList<Job> jobs = CSVFileParser.parse(infile);
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(jobs);
			return jobSubmitter;
		} else if (inFormatName.equals("COURSE")) {
			ArrayList<Job> jobs = CourseFileParser.parse(infile);
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(jobs);
			return jobSubmitter;
		} else {
			ArrayList<Job> jobs = CourseFileParser.parse(infile);
			JobSubmitter jobSubmitter = new JobSubmitter();
			jobSubmitter.add(jobs);
			return jobSubmitter;
		}

	}

}
