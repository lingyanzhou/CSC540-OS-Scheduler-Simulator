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

	private enum SubmitterType {
		PRESET1, PRESET2, PRESET3, CSV, COURSE,
	}

	/**
	 * Run the simulator
	 * 
	 * @param inPathName
	 *            input file path name
	 * @param methodName
	 *            scheduling algorithm
	 * @param formatName
	 *            file format
	 * @param quantumName
	 *            RR quantum
	 * @param nogui
	 *            Do not use GUI
	 * @throws FileNotFoundException
	 */
	public void run(String inPathName, String outPathName, String methodName,
			String formatName, String quantumName, boolean nogui)
			throws FileNotFoundException {

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

		IOutputRecorder outputRecorder = null;
		if (nogui) {
			outputRecorder = new TextOutputRecorder(scheduler, inPathName,
					outPathName);
		} else {
			// TODO
			outputRecorder = new TextOutputRecorder(scheduler, inPathName,
					outPathName);
		}

		JobSubmitter jobSubmitter = getJobSubmitter(format,
				new File(inPathName));

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

}
