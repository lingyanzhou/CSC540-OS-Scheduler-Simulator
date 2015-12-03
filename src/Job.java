
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
	private String m_name;

	/**
	 * Remaining time
	 */
	private int m_rtime;

	/**
	 * Used CPU time 
	 */
	private int m_time;
	

	/** Constructor
	 * 
	 * @param name Job name
	 * @param time Estimated job CPU time.
	 */
	public Job(String name, int time) {
		m_name = name;
		m_rtime = time;
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
		return m_rtime;
	}

	/**
	 * Get the CPU time
	 * @return CPU time
	 */
	public int getTime() {
		return m_time;
	}

	/**
	 * The job gets a CPU time unit to run.
	 */
	public void tick() {
		m_time += 1;
		m_rtime -= 1;
	}

	/**
	 * Whether the job is finished.
	 * @return true if the job is finished
	 */
	public boolean isFinished() {
		return m_rtime==0;
	}
	
}
