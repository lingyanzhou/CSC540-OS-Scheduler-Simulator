import java.io.File;
import java.util.ArrayList;

/**
 * Scheduler Simulator
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SchedulerSimulator {

	private enum SubmitterType {
		PRESET1, PRESET2, PRESET3, CSV,
	}

	public static void main(String[] args) {
		IScheduler scheduler = null;

		if (args[0].equals("FIFO")) {
			scheduler = new FIFOScheduler();
		} else if (args[0].equals("SJF")) {
			scheduler = new SJFScheduler();
		} else if (args[0].equals("SRT")) {
			scheduler = new SRTScheduler();
		} else if (args[0].equals("RR")) {
			scheduler = new RRScheduler(2);
		} else {
			scheduler = new FIFOScheduler();
		}
		JobSubmitter jobSubmitter = getJobSubmitter(SubmitterType.CSV, new File(args[1]));
		for (int i = 0; i < 32; ++i) {
			scheduler.acceptJobs(jobSubmitter.submitJobs());
			scheduler.schedule();
			System.out.print(i);
			System.out.print(",");
			System.out.println(scheduler.reportRunningProcess());
			scheduler.tick();
			jobSubmitter.tick();
		}
		printReport(scheduler);
	}

	private static JobSubmitter getJobSubmitter(SubmitterType flag, File csv) {
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
			ArrayList<Job> jobs = CSVFileParser.parse(csv);
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
		System.out.println("============================");
		System.out.print("Total Process Count: ");
		System.out.println(procCount);
		System.out.print("Total Waiting Time: ");
		System.out.println(sched.reportTotalWaitingTime());
		System.out.print("Average Waiting Time: ");
		System.out.println((float) sched.reportTotalWaitingTime()
				/ (float) procCount);
		System.out.print("Total Turnaround Time: ");
		System.out.println(sched.reportTotalTurnAroundTime());
		System.out.print("Average Turnaround Time: ");
		System.out.println((float) sched.reportTotalTurnAroundTime()
				/ (float) procCount);
		System.out.print("Total Context Switch Count: ");
		System.out.println(sched.reportTotalContextSwitchCount());
	}

}
