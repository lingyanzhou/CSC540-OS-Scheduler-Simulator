import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * 
 * @author zhoulingyan
 * @since 12,6,2015
 */
public class LatexOutputRecorder implements IOutputRecorder {
	private IScheduler m_sched = null;
	private int m_time = 0;
	private String m_inFileName = "";
	private String m_outFileName = "";
	private boolean m_isRecording = false;
	private boolean m_streamClosable = false;
	private PrintStream m_ps = null;
	private ArrayList<JobIntervalStruct> m_schedHistory = new ArrayList<JobIntervalStruct>();

	public LatexOutputRecorder(IScheduler sched, String name, String outfilename) {
		m_inFileName = name;
		m_sched = sched;
		m_outFileName = outfilename;
	}

	@Override
	public void tick() {
		if (m_isRecording) {
			if (m_schedHistory.isEmpty()) {
				if (null == m_sched.reportRunningProcess()) {
					m_schedHistory.add(null);
				} else {
					JobIntervalStruct tmp = new JobIntervalStruct();
					tmp.job = m_sched.getRunningJob();
					tmp.interval = 1;
					tmp.startTime = m_time;
					m_schedHistory.add(tmp);
				}
			} else {
				if (null == m_sched.reportRunningProcess()) {
					if (m_schedHistory.get(m_schedHistory.size() - 1) != null)
						m_schedHistory.add(null);
				} else {
					if (m_schedHistory.get(m_schedHistory.size() - 1) == null) {
						JobIntervalStruct tmp = new JobIntervalStruct();
						tmp.job = m_sched.getRunningJob();
						tmp.interval = 1;
						tmp.startTime = m_time;
						m_schedHistory.add(tmp);
					} else {
						if (m_schedHistory.get(m_schedHistory.size() - 1).job == (m_sched
								.getRunningJob())) {
							m_schedHistory.get(m_schedHistory.size() - 1).interval += 1;
						} else {
							JobIntervalStruct tmp = new JobIntervalStruct();
							tmp.job = m_sched.getRunningJob();
							tmp.interval = 1;
							tmp.startTime = m_time;
							m_schedHistory.add(tmp);
						}
					}

				}
			}
			++m_time;
		}
	}

	@Override
	public void end() throws FileNotFoundException {
		m_isRecording = false;
		if (m_outFileName.equals("-")) {
			m_ps = new PrintStream(System.out);
			m_streamClosable = false;
		} else {
			m_ps = new PrintStream(new File(m_outFileName));
			m_streamClosable = true;
		}

		m_ps.println("\\documentclass{standalone}\n"
				+ "\\usepackage[utf8]{inputenc}\n"
				+ "\\usepackage{amsmath}\n"
				+ "\\usepackage{amsfonts}\n"
				+ "\\usepackage{amssymb}\n"
				+ "\\usepackage{tikz}\n"
				+ "\\usetikzlibrary{calc}\n\n"
				+ "% GanttHeader setups some parameters for the rest of the diagram\n"
				+ "% #1 Width of the diagram\n"
				+ "% #2 Width of the space reserved for task numbers\n"
				+ "% #3 Width of the space reserved for task names\n"
				+ "% #4 Number of months in the diagram\n"
				+ "% In addition to these parameters, the layout of the diagram is influenced\n"
				+ "% by keys defined below, such as y, which changes the vertical scale\n"
				+ "\\def\\GanttHeader#1#2#3#4{%\n"
				+ " \\pgfmathparse{(#1-#2-#3)/(#4)}\n"
				+ " \\tikzset{y=7mm, task number/.style={left, font=\\bfseries},\n"
				+ "     task description/.style={text width=#3,  right, draw=none,\n"
				+ "           font=\\sffamily, xshift=#2,\n"
				+ "           minimum height=2em},\n"
				+ "     gantt bar/.style={draw=black, fill=blue!30},\n"
				+ "     help lines/.style={draw=black!30, dashed},\n"
				+ "     x=\\pgfmathresult pt\n"
				+ "     }\n"
				+ "  \\def\\totalmonths{#4}\n"
				+ "  \\node (Header) [task description] at (0,0) {\\textbf{\\large }};\n"
				+ "  \\begin{scope}[shift=($(Header.south east)$)]\n"
				+ "    \\foreach \\x in {1,...,#4}\n"
				+ "      \\node[above,rotate=90] at (\\x,1) {\\tiny\\x};\n"
				+ " \\end{scope}\n"
				+ "}\n\n"
				+ "% This macro adds a task to the diagram\n"
				+ "% #1 Number of the task\n"
				+ "% #2 Task's name\n"
				+ "% #3 Starting date of the task (month's number, can be non-integer)\n"
				+ "% #4 Task's duration in months (can be non-integer)\n"
				+ "\\def\\Task#1#2{%\n"
				+ "%\\node[task number] at ($(Header.west) + (0, -#1)$) {#1};\n"
				+ "\\node[task description] at (0,-#1) {#2};\n"
				+ "\\begin{scope}[shift=($(Header.south east)$)]\n"
				+ "  \\draw (0,-#1) rectangle +(\\totalmonths, 1);\n"
				+ "  \\foreach \\x in {1,...,\\totalmonths}\n"
				+ "    \\draw[help lines] (\\x,-#1) -- +(0,1);\n"
				+ "\\end{scope}\n"
				+ "}\n\n"
				+ "% This macro adds a task to the diagram\n"
				+ "% #1 Number of the task\n"
				+ "% #2 Task's name\n"
				+ "% #3 Starting date of the task (month's number, can be non-integer)\n"
				+ "% #4 Task's duration in months (can be non-integer)\n"
				+ "\\def\\TaskActive#1#2#3{%\n"
				+ "\\begin{scope}[shift=($(Header.south east)$)]\n"
				+ "  \\filldraw[gantt bar] ($(#2, -#1+0.2)$) rectangle +(#3,0.6);\n"
				+ "\\end{scope}\n" + "}\n\n" + "\\begin{document}\n");
		m_ps.println("\\begin{tabular}{l}");
		m_ps.println("\\begin{tabular}{lr}");
		m_ps.println("Scheduler: & " + m_sched.getName());
		m_ps.println("\\\\");
		m_ps.println("Input: & " + m_inFileName);
		m_ps.println("\\\\");

		if (m_schedHistory.isEmpty()) {

			m_ps.println("\\end{tabular}");
			m_ps.println("\\end{tabular}");
		} else {

			int procCount = m_sched.reportTotalProcessCount();
			m_ps.print("Total Process Count: & ");
			m_ps.println(procCount);
			m_ps.println("\\\\");
			m_ps.print("Total Waiting Time: & ");
			m_ps.println(m_sched.reportTotalWaitingTime());
			m_ps.println("\\\\");
			m_ps.print("Average Waiting Time: & ");
			m_ps.println((float) m_sched.reportTotalWaitingTime()
					/ (float) procCount);
			m_ps.println("\\\\");
			m_ps.print("Total Turnaround Time: & ");
			m_ps.println(m_sched.reportTotalTurnAroundTime());
			m_ps.println("\\\\");
			m_ps.print("Average Turnaround Time: & ");
			m_ps.println((float) m_sched.reportTotalTurnAroundTime()
					/ (float) procCount);
			m_ps.println("\\\\");
			m_ps.print("Total Context Switch Count: & ");
			m_ps.println(m_sched.reportTotalContextSwitchCount());
			m_ps.println("\\\\");
			m_ps.println("\\end{tabular}");

			m_ps.println("\\\\");

			m_ps.println("\\begin{tikzpicture}");
			m_ps.println("\\GanttHeader{32cm}{5ex}{1.5cm}{" + m_time + "}");
			for (Job j : m_sched.getAllJobs()) {
				m_ps.println("\\Task{" + j.getJobId() + "}{"
						+ j.getName() + "}");
			}
			for (int i = 0; i < m_schedHistory.size(); ++i) {
				m_ps.println("\\TaskActive{"
						+ m_schedHistory.get(i).job.getJobId() + "}{"
						+ m_schedHistory.get(i).startTime + "}{"
						+ m_schedHistory.get(i).interval + "}");
			}
			m_ps.println("\\end{tikzpicture}");

			m_ps.println("\\end{tabular}");
		}

		m_ps.println("\\end{document}");

		m_ps.flush();
		if (m_streamClosable) {
			m_ps.close();
		}
	}

	@Override
	public void begin() throws FileNotFoundException {

		m_isRecording = true;
	}

}

class JobIntervalStruct {
	public int startTime = 0;
	public int interval = 0;
	public Job job = null;
}
