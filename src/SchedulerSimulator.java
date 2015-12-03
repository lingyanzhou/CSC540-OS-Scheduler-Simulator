
/** Scheduler Simulator
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SchedulerSimulator {
	public static void main(String[] args) {
		//IScheduler scheduler= new FIFOScheduler();
		//IScheduler scheduler= new SJFScheduler();
		//IScheduler scheduler= new SRTScheduler();
		IScheduler scheduler= new RRScheduler(2);
		JobSubmitter jobSubmitter = new JobSubmitter();
		jobSubmitter.add(new Job("job1", 8), 0);
		jobSubmitter.add(new Job("job2", 4), 0);
		jobSubmitter.add(new Job("job3", 9), 0);
		jobSubmitter.add(new Job("job4", 5), 0);
		for (int i=0; i<32; ++i) {
			scheduler.acceptJobs(jobSubmitter.submitJobs());
			scheduler.schedule();
			System.out.print(i);
			System.out.print(",");
			System.out.println(scheduler.reportProcess());
			scheduler.tick();
			jobSubmitter.tick();
		}
		System.out.println(scheduler.reportTotalWaitingTime());
	}
}
