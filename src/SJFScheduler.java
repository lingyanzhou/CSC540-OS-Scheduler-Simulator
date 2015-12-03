import java.util.ArrayList;
import java.util.List;


/** Shortest-Job-First scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SJFScheduler implements IScheduler {
	private Job m_runningJob=null;
	private List<Job> m_jobList=new ArrayList<Job>();
	private int m_totalWaitingTime =0;
	private int m_curTime = 0;
	
	
	public void tick() {
		m_curTime += 1;
		m_totalWaitingTime += m_jobList.size();
		
		if (null!=m_runningJob) {
			m_runningJob.tick();
			if (m_runningJob.isFinished()) {
				m_runningJob =null;
			}
		}
	}
	
	/**
	 * Find the Shrotest job in the job queue.
	 * @return the shortest job or null
	 */
	private Job removeShortestJob() {
		if (0==m_jobList.size()) {
			return null;
		}
		Job targetJob = m_jobList.get(0);
		for (int i=1; i<m_jobList.size(); ++i) {
			Job job = m_jobList.get(i);
			if (job.getRemainTime()<targetJob.getRemainTime()) {
				targetJob = job;
			}
		}
		m_jobList.remove(targetJob);
		return targetJob;
	}

	public int reportTotalWaitingTime() {
		return m_totalWaitingTime;
	}
	public boolean hasProcess() {
		return null==m_runningJob;
	}
	public void acceptJob(String name, int time) {
		m_jobList.add(new Job(name, time));
	}
	public void acceptJob(Job job) {
		m_jobList.add(job);
	}
	public void acceptJobs(List<Job> jobs) {
		for (Job job : jobs) {
			m_jobList.add(job);
		}
	}

	public void schedule() {
		if (m_runningJob==null && 0!=m_jobList.size()) {
			m_runningJob= removeShortestJob();
		}
	}
	
	public String reportProcess() {
		if (null==m_runningJob) {
			return null;
		} else {
			return m_runningJob.getName();
		}
	}
}
