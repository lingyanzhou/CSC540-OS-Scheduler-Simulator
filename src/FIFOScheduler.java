import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


/** First-In-First-Out job scheduler
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 * 
 * 
 */
public class FIFOScheduler implements IScheduler {
	private Job m_runningJob=null;
	private Queue<Job> m_jobQueue=new ArrayDeque<Job>();
	private int m_totalWaitingTime =0;
	private int m_curTime = 0;
	
	
	public void tick() {
		m_curTime += 1;
		m_totalWaitingTime += m_jobQueue.size();
		
		if (null!=m_runningJob) {
			m_runningJob.tick();
			if (m_runningJob.isFinished()) {
				m_runningJob =null;
			}
		}
	}
	
	public int reportTotalWaitingTime() {
		return m_totalWaitingTime;
	}
	public boolean hasProcess() {
		return null==m_runningJob;
	}
	public void acceptJob(String name, int time) {
		m_jobQueue.add(new Job(name, time));
	}
	public void acceptJob(Job job) {
		m_jobQueue.add(job);
	}
	public void acceptJobs(List<Job> jobs) {
		for (Job job : jobs) {
			m_jobQueue.add(job);
		}
	}

	public void schedule() {
		if (m_runningJob==null && 0!=m_jobQueue.size()) {
			m_runningJob= m_jobQueue.remove();
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
