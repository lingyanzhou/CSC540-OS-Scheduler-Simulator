import java.util.ArrayList;
import java.util.List;

/** Round-robin scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class RRScheduler implements IScheduler {
	private Job m_runningJob = null;
	private int m_quantum = 0;
	private int m_maxQuantum = 0;
	private List<Job> m_jobList = new ArrayList<Job>();
	private int m_totalWaitingTime = 0;
	private int m_curTime = 0;

	public RRScheduler(int maxQuantum) {
		m_maxQuantum = maxQuantum;
	}

	public void tick() {
		m_curTime += 1;
		m_totalWaitingTime += m_jobList.size();

		if (null != m_runningJob) {
			m_runningJob.tick();
			m_quantum -= 1;
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
			if (null == m_runningJob) {
				m_runningJob = m_jobList.remove(0);
				m_quantum= m_maxQuantum;
			} else if (0 == m_quantum) {
				m_jobList.add(m_runningJob);
				m_runningJob = m_jobList.remove(0);
				m_quantum= m_maxQuantum;
			}
		}
	}

	public String reportProcess() {
		if (null == m_runningJob) {
			return null;
		} else {
			return m_runningJob.getName();
		}
	}
}
