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
	 * Report the total process count
	 * @return the total process count
	 */
	public int reportTotalProcessCount();
	
	/**
	 * Report the total waiting time
	 * @return the total waiting time
	 */
	public int reportTotalWaitingTime();
	
	/**
	 * Report the total turnaround time
	 * @return the total turnaround time
	 */
	public int reportTotalTurnAroundTime();
	
	/**
	 * Report the total context switch count
	 * @return the total context switch count
	 */
	public int reportTotalContextSwitchCount();
	
	/**
	 * Whether there is a running process
	 * @return true if there is a running process
	 */
	public boolean hasRunningProcess();
	
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
	public String reportRunningProcess();
}
