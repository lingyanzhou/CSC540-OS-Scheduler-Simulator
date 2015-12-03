import java.util.ArrayList;
import java.util.List;


/** Simulation of incoming jobs
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class JobSubmitter {
	/**
	 * A list of jobs and their arrival time
	 */
	private ArrayList<JobArrivalStruct> m_jobArrivalList = new ArrayList<JobArrivalStruct>();
	
	/**
	 * Current CPU time unit
	 */
	private int m_curTime =0;
	
	/**
	 * Add jobs to the simulation
	 * @param job Job
	 * @param arrivalTime Arrival time
	 */
	public void add(Job job, int arrivalTime) {
		int i = 0;
		for (i=0; i<m_jobArrivalList.size(); ++i) {
			JobArrivalStruct tmp = m_jobArrivalList.get(i);
			if (tmp.arrivalTime>arrivalTime) {
				m_jobArrivalList.add(i, new JobArrivalStruct(job, arrivalTime));
				break;
			}
		}
		if (i==m_jobArrivalList.size()) {
			m_jobArrivalList.add(new JobArrivalStruct(job, arrivalTime));
		}
		
	}
	
	/**
	 * Simulate a CPU time unit
	 */
	public void tick() {
		m_curTime  += 1;
	}
	
	/**
	 * Submit a list of jobs that "arrives" at this CPU time unit
	 * @return the list of jobs
	 */
	public List<Job> submitJobs() {
		ArrayList<Job> ret = new ArrayList<Job>();
		for (JobArrivalStruct jobArrivalStruct : m_jobArrivalList) {
			if (jobArrivalStruct.arrivalTime==m_curTime) {
				ret.add(jobArrivalStruct.job);
			}
		}
		return ret;
	}
}

/** Keeps track of jobs and their arrival time.
 * 
 * @author zhoulingyan
 *
 */
class JobArrivalStruct {
	/**
	 * Arrival time
	 */
	public int arrivalTime = 0;
	
	/**
	 * Job
	 */
	public Job job = null;
	
	/**
	 * Constructor
	 * 
	 * @param job Job
	 * @param arrivalTime Arrival time
	 */
	public JobArrivalStruct (Job job, int arrivalTime) {
		this.arrivalTime = arrivalTime;
		this.job = job;
	}
}
