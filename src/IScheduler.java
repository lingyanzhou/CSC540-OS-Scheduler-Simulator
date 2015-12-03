import java.util.List;

/** Interface for schedulers
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public interface IScheduler {
	/**
	 * Simulate a CPU time unit
	 */
	public void tick();
	
	/**
	 * Report the total waiting time
	 * @return the total waiting time
	 */
	public int reportTotalWaitingTime();
	
	/**
	 * Whether there is a running process
	 * @return true if there is a running process
	 */
	public boolean hasProcess();
	
	/**
	 * Accept a job
	 * @param name Job name
	 * @param time Estimated CPU time
	 */
	public void acceptJob(String name, int time);
	
	/**
	 * Accept a job
	 * @param job
	 */
	public void acceptJob(Job job);
	
	/**
	 * Accept a list of jobs
	 * @param jobs 
	 */
	public void acceptJobs(List<Job> jobs);
	
	/**
	 * Run the scheduler
	 */
	public void schedule();
	
	/**
	 * Report the running process
	 * @return the name of the running process
	 */
	public String reportProcess();
}
