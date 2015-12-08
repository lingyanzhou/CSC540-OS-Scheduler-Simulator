import java.util.ArrayList;
import java.util.List;

/**
 * Round-robin scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class RRScheduler implements IScheduler {
	private Job m_runningJob = null;
	private int m_quantum = 0;
	private int m_maxQuantum = 0;
	private List<Job> m_jobList = new ArrayList<Job>();
	private Job m_lastAddedJob = null;
	private ArrayList<Job> m_allJobs = new ArrayList<Job>();

	public RRScheduler(int maxQuantum) {
		m_maxQuantum = maxQuantum;
	}

	@Override
	public void tick() {

		if (null != m_runningJob) {
			m_runningJob.tickRun();
			m_quantum -= 1;
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
		m_jobList.add(m_jobList.indexOf(m_lastAddedJob) + 1, job);
		m_allJobs.add(job);
		m_lastAddedJob = job;
	}

	@Override
	public void acceptJobs(List<Job> jobs) {
		int ind = m_jobList.indexOf(m_lastAddedJob) + 1;
		for (Job job : jobs) {
			m_jobList.add(ind, job);
			ind += 1;
			m_allJobs.add(job);
		}
		if (jobs.size() != 0) {
			m_lastAddedJob = jobs.get(jobs.size() - 1);
		}
	}

	@Override
	public void schedule() {
		if (0 != m_jobList.size()) {
			if (null == m_runningJob) {
				m_runningJob = m_jobList.remove(0);
				m_quantum = m_maxQuantum;
				m_runningJob.switchIn();
			} else if (0 >= m_quantum) {
				m_jobList.add(m_runningJob);
				m_runningJob = m_jobList.remove(0);
				m_quantum = m_maxQuantum;
				m_runningJob.switchIn();
			}
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

		return "RR-" + m_maxQuantum + " scheduler";
	}
}
