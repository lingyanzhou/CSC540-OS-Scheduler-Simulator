import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * 
 * @author zhoulingyan
 * @since 12,6,2015
 */
public class TextOutputRecorder implements IOutputRecorder {
	private IScheduler m_shced = null;
	private int m_time = 0;
	private String m_inFileName = "";
	private String m_outFileName = "";
	private boolean m_isRecording = false;
	private boolean m_streamClosable = false;
	private PrintStream m_ps = null;

	public TextOutputRecorder(IScheduler sched, String name, String outfilename) {
		m_inFileName = name;
		m_shced = sched;
		m_outFileName = outfilename;
	}

	@Override
	public void tick() {
		if (m_isRecording) {
			m_ps.print(m_time);
			m_ps.print(",");
			m_ps.println(m_shced.reportProcessesCSV());
			++m_time;
		}
	}

	@Override
	public void end() {
		m_isRecording = false;
		int procCount = m_shced.reportTotalProcessCount();
		m_ps.println("#============================");
		m_ps.print("#Total Process Count: ");
		m_ps.println(procCount);
		m_ps.print("#Total Waiting Time: ");
		m_ps.println(m_shced.reportTotalWaitingTime());
		m_ps.print("#Average Waiting Time: ");
		m_ps.println((float) m_shced.reportTotalWaitingTime()
				/ (float) procCount);
		m_ps.print("#Total Turnaround Time: ");
		m_ps.println(m_shced.reportTotalTurnAroundTime());
		m_ps.print("#Average Turnaround Time: ");
		m_ps.println((float) m_shced.reportTotalTurnAroundTime()
				/ (float) procCount);
		m_ps.print("#Total Context Switch Count: ");
		m_ps.println(m_shced.reportTotalContextSwitchCount());

		m_ps.flush();
		if (m_streamClosable) {
			m_ps.close();
		}
	}

	@Override
	public void begin() throws FileNotFoundException {
		if (m_outFileName.equals("-")) {
			m_ps = new PrintStream(System.out);
			m_streamClosable = false;
		} else {
			m_ps = new PrintStream(new File(m_outFileName));
			m_streamClosable = true;
		}
		m_ps.println("#============================");
		m_ps.println("#============================");
		m_ps.println("#Scheduler: " + m_shced.getName());
		m_ps.println("#Input: " + m_inFileName);
		m_ps.println("#- - - - - - - - - - - - - - -");
		m_isRecording = true;
	}

}
