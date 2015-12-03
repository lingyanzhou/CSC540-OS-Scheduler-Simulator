import java.util.ArrayList;
import java.util.List;

/** Shortest Remaining Time Scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SRTScheduler implements IScheduler {
	private Job m_runningJob = null;
	private List<Job> m_jobList = new ArrayList<Job>();
	private int m_totalWaitingTime = 0;
	private int m_curTime = 0;

	public void tick() {
		m_curTime += 1;
		m_totalWaitingTime += m_jobList.size();

		if (null != m_runningJob) {
			m_runningJob.tick();
			if (m_runningJob.isFinished()) {
				m_runningJob = null;
			}
		}
	}

	public int reportTotalWaitingTime() {
		return m_totalWaitingTime;
	}

	public boolean hasProcess() {
		return null == m_runningJob;
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
		if (0 != m_jobList.size()) {
			m_runningJob = removeSRTJob();
		}
	}

	/**
	 * Find the Shrotest Remaining Time job in the job queue, including the running job.
	 * @return the SRT job or null
	 */
	private Job removeSRTJob() {
		if (0 == m_jobList.size()) {
			return m_runningJob;
		}
		Job targetJob = null;
		if (null != m_runningJob) {
			targetJob = m_runningJob;
			for (int i = 0; i < m_jobList.size(); ++i) {
				Job job = m_jobList.get(i);
				if (job.getRemainTime() < targetJob.getRemainTime()) {
					targetJob = job;
				}
			}
			if (m_jobList.remove(targetJob)) {
				m_jobList.add(0, m_runningJob);
			}
		} else {
			targetJob = m_jobList.get(0);
			for (int i = 1; i < m_jobList.size(); ++i) {
				Job job = m_jobList.get(i);
				if (job.getRemainTime() < targetJob.getRemainTime()) {
					targetJob = job;
				}
			}
			m_jobList.remove(targetJob);
		}
		return targetJob;
	}

	public String reportProcess() {
		if (null == m_runningJob) {
			return null;
		} else {
			return m_runningJob.getName();
		}
	}
}
