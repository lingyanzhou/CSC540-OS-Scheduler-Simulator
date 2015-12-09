import java.util.ArrayList;
import java.util.List;

/**
 * Shortest-Job-First scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class SJFScheduler implements IScheduler {
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

	/**
	 * Find the Shrotest job in the job queue.
	 * 
	 * @return the shortest job or null
	 */
	private Job removeShortestJob() {
		if (0 == m_jobList.size()) {
			return null;
		}
		Job targetJob = m_jobList.get(0);
		for (int i = 1; i < m_jobList.size(); ++i) {
			Job job = m_jobList.get(i);
			if (job.getRemainTime() < targetJob.getRemainTime()) {
				targetJob = job;
			}
		}
		m_jobList.remove(targetJob);
		return targetJob;
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
		if (m_runningJob == null && 0 != m_jobList.size()) {
			m_runningJob = removeShortestJob();
			m_runningJob.switchIn();
		}
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
		return "SJF scheduler";
	}

	@Override
	public Job getRunningJob() {
		return m_runningJob;
	}
}
