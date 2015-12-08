import java.util.ArrayList;
import java.util.List;

/**
 * Shortest Remaining Time Scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SRTScheduler implements IScheduler {
	private Job m_runningJob = null;
	private List<Job> m_jobList = new ArrayList<Job>();
	private ArrayList<Job> m_allJobs = new ArrayList<Job>();

	@Override
	public void tick() {

		if (null != m_runningJob) {
			m_runningJob.tickRun();
			if (m_runningJob.isFinished()) {
				m_runningJob = null;
			}
		}
		for (Job job : m_jobList) {
			job.tickWait();
		}
	}

	@Override
	public int reportTotalProcessCount() {
		return m_allJobs.size();
	}

	@Override
	public int reportTotalWaitingTime() {
		int ret = 0;
		for (Job job : m_allJobs) {
			ret += job.getWaitingTime();
		}
		return ret;
	}

	@Override
	public int reportTotalTurnAroundTime() {
		int ret = 0;
		for (Job job : m_allJobs) {
			ret += job.getTurnAroundTime();
		}
		return ret;
	}

	@Override
	public int reportTotalContextSwitchCount() {
		int ret = 0;
		for (Job job : m_allJobs) {
			ret += job.getContextSwitchCount();
		}
		return ret;
	}

	@Override
	public boolean hasRunningProcess() {
		return null != m_runningJob;
	}

	@Override
	public boolean hasWaitingProcess() {
		return !m_jobList.isEmpty();
	}

	@Override
	public void acceptJob(Job job) {
		m_jobList.add(job);
		m_allJobs.add(job);
	}

	@Override
	public void acceptJobs(List<Job> jobs) {
		for (Job job : jobs) {
			m_jobList.add(job);
			m_allJobs.add(job);
		}
	}

	@Override
	public void schedule() {
		if (0 != m_jobList.size()) {
			m_runningJob = removeSRTJob();
		}
	}

	/**
	 * Find the Shrotest Remaining Time job in the job queue, including the
	 * running job.
	 * 
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
				targetJob.switchIn();
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
			targetJob.switchIn();
		}
		return targetJob;
	}

	@Override
	public String reportRunningProcess() {
		if (null == m_runningJob) {
			return null;
		} else {
			return m_runningJob.getName();
		}
	}

	@Override
	public String reportProcessesCSV() {
		String ret = "";
		for (int i = 0; i < m_allJobs.size(); ++i) {
			Job job = m_allJobs.get(i);
			if (job != m_runningJob) {
				ret += ", ";
			} else {
				ret += m_runningJob.getName() + ", ";
			}
		}
		return ret;
	}

	@Override
	public String getName() {

		return "SRT scheduler";
	}
}
