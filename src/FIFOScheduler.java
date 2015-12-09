import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * First-In-First-Out job scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 * 
 * 
 */
public class FIFOScheduler implements IScheduler {
	private Job m_runningJob = null;
	private Queue<Job> m_jobQueue = new ArrayDeque<Job>();
	private ArrayList<Job> m_allJobs = new ArrayList<Job>();

	@Override
	public void tick() {

		if (null != m_runningJob) {
			m_runningJob.tickRun();
			if (m_runningJob.isFinished()) {
				m_runningJob = null;
			}
		}
		for (Job job : m_jobQueue) {
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
		return !m_jobQueue.isEmpty();
	}

	@Override
	public void acceptJob(Job job) {
		m_jobQueue.add(job);
		m_allJobs.add(job);

	}

	@Override
	public void acceptJobs(List<Job> jobs) {
		for (Job job : jobs) {
			m_jobQueue.add(job);
			m_allJobs.add(job);
		}
	}

	@Override
	public void schedule() {
		if (m_runningJob == null && 0 != m_jobQueue.size()) {
			m_runningJob = m_jobQueue.remove();
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

		return "FIFO scheduler";
	}

	@Override
	public Job getRunningJob() {
		return m_runningJob;
	}
}
