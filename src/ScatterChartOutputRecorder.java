import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * @author zhoulingyan
 * @since 12,9,2015
 */
public class ScatterChartOutputRecorder extends Observable implements
		IOutputRecorder {
	private IScheduler m_shced = null;
	private int m_time = 0;
	private String m_inFileName = "";
	private boolean m_isRecording = false;
	private XYSeries m_data = new XYSeries("");

	/**
	 * 
	 * @param sched
	 * @param infilename
	 * @param appFrame
	 * @param infoPanel
	 */
	public ScatterChartOutputRecorder(IScheduler sched, String infilename,
			Observer appFrame, Observer infoPanel) {
		m_inFileName = infilename;
		m_shced = sched;
		addObserver(appFrame);
		// this.addObserver(infoPanel);
	}

	/**
	 * 
	 * @return recorded dataset
	 */
	public JFreeChart getChart() {
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(m_data);

		JFreeChart chart = ChartFactory.createScatterPlot(m_shced.getName(), // chart
																				// title
				"Job Id", // domain axis label
				"Time", // range axis label
				collection, // data
				PlotOrientation.HORIZONTAL, false, // include legend
				true, // tooltips
				false // urls
				);

		// chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
		return chart;
	}

	/**
	 * Get the text report
	 * 
	 * @return the report
	 */
	public String getReport() {
		int procCount = m_shced.reportTotalProcessCount();
		String ret = "Scheduler: " + m_shced.getName() + "\n";
		
		ret += "Input: " + m_inFileName + "\n";
		
		ret += "Total Process Count: " + procCount + "\n";

		ret += "Total Waiting Time: " + m_shced.reportTotalWaitingTime() + "\n";
		ret += "Average Waiting Time: "
				+ ((float) m_shced.reportTotalWaitingTime() / (float) procCount)
				+ "\n";
		ret += "Total Turnaround Time: " + m_shced.reportTotalTurnAroundTime()
				+ "\n";
		ret += "Average Turnaround Time: "
				+ ((float) m_shced.reportTotalTurnAroundTime() / (float) procCount)
				+ "\n";
		ret += "Total Context Switch Count: "
				+ m_shced.reportTotalContextSwitchCount() + "\n";
		return ret;
	}

	@Override
	public void tick() {
		if (m_isRecording) {
			m_data.add(new XYDataItem(m_shced.getRunningJob().getJobId(),
					m_time + 0.5));
			++m_time;
		}
	}

	@Override
	public void end() {
		m_isRecording = false;
		setChanged();
		notifyObservers();
	}

	@Override
	public void begin() throws FileNotFoundException {
		m_isRecording = true;
	}

}
