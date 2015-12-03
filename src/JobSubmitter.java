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
	private ArrayList<Job> m_jobList = new ArrayList<Job>();
	
	/**
	 * Current CPU time unit
	 */
	private int m_curTime =0;
	
	/**
	 * Add jobs to the simulation
	 * @param job Job
	 */
	public void add(Job job) {
		int i = 0;
		for (i=0; i<m_jobList.size(); ++i) {
			Job tmp = m_jobList.get(i);
			if (tmp.getArrivalTime()>job.getArrivalTime()) {
				m_jobList.add(i, job);
				break;
			}
		}
		if (i==m_jobList.size()) {
			m_jobList.add(job);
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
		for (Job job : m_jobList) {
			if (job.getArrivalTime()==m_curTime) {
				ret.add(job);
			}
		}
		return ret;
	}
}


