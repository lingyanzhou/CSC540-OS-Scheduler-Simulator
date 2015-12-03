
/** Job class.
 * 
 *  Keeps track of running time, job name, and remaing time
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class Job {
	
	/**
	 * Name of the job
	 */
	private String m_name = null;

	/**
	 * Estimated total time
	 */
	private int m_ttime = 0;

	/**
	 * Used CPU time 
	 */
	private int m_ctime = 0;

	/**
	 * Arrival time 
	 */
	private int m_atime = 0;

	/**
	 * End time 
	 */
	private int m_etime = 0;

	/**
	 * Wait time 
	 */
	private int m_wtime = 0;

	/**
	 * Context switch count
	 */
	private int m_contextSwitchCount = 0;
	

	/** Constructor
	 * 
	 * @param name Job name
	 * @param time Estimated job CPU time.
	 */
	public Job(String name, int ttime, int atime) {
		m_name = name;
		m_ttime = ttime;
		m_atime = atime;
		m_wtime = 0;
		m_ctime = 0;
		m_etime = atime;
	}
	
	/**
	 * Get the name of the job
	 * @return the name of the job
	 */
	public String getName() {
		return m_name;
	}
	

	/**
	 * Get the remaining time
	 * @return the remaining time
	 */
	public int getRemainTime() {
		return m_ttime-m_ctime;
	}

	/**
	 * Get the CPU time
	 * @return CPU time
	 */
	public int getCPUTime() {
		return m_ctime;
	}

	/**
	 * Get the arrival time
	 * @return the arrival time
	 */
	public int getArrivalTime() {
		return m_atime;
	}

	/**
	 * Get the waiting time
	 * @return the waiting time
	 */
	public int getWaitingTime() {
		return m_wtime;
	}

	/**
	 * Get the TurnAround time
	 * @return the TurnAround time
	 */
	public int getTurnAroundTime() {
		return m_etime-m_atime;
	}

	/**
	 * Get the Context switch count
	 * @return the Context switch count
	 */
	public int getContextSwitchCount() {
		return m_contextSwitchCount;
	}

	/**
	 * Scheduler switches in this process
	 * @return the Context switch count
	 */
	public void switchIn() {
		m_contextSwitchCount += 1;
	}

	/**
	 * The job gets a CPU time unit to run.
	 */
	public void tickRun() {
		m_ctime += 1;
		m_etime += 1;
	}

	/**
	 * The job gets a CPU time unit to wait.
	 */
	public void tickWait() {
		m_wtime += 1;
		m_etime += 1;
	}

	/**
	 * Whether the job is finished.
	 * @return true if the job is finished
	 */
	public boolean isFinished() {
		return m_ctime==m_ttime;
	}
	
}
