import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.jfree.chart.ChartPanel;

import java.util.Observable;
import java.util.Observer;

public class AppFrame extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -83613314562317727L;
	private ControlPanel m_controlPanel = null;
	private ChartPanel m_chartPanel = null;
	private JTextArea m_infoArea = null;

	public AppFrame(int height, int width) {

		setTitle("Cellular Automata");
		// -- add some items to the content pane of the frame
		// JButton okButton = new JButton("OK");
		// frame.add(okButton);

		// -- size of the frame: width, height
		setSize(width, height);

		// -- center the frame on the screen
		setLocationRelativeTo(null);

		// -- shut down the entire application when the frame is closed
		// if you don't include this the application will continue to
		// run in the background and you'll have to kill it by pressing
		// the red square in eclipse
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// -- set the layout manager and add items
		// 5, 5 is the border around the edges of the areas
		setLayout(new BorderLayout(5, 5));

		m_controlPanel = new ControlPanel(this);
		this.add(m_controlPanel, BorderLayout.EAST);

		m_infoArea = new JTextArea(5, 2);
		this.add(m_infoArea, BorderLayout.SOUTH);

		// -- show the frame on the screen
		setVisible(true);
	}

	public void replaceGraphPanel() {

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		ScatterChartOutputRecorder recorder = (ScatterChartOutputRecorder) arg0;
		ChartPanel chartPanel = new ChartPanel(recorder.getChart());
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		if (null == m_chartPanel) {
			m_chartPanel = chartPanel;
			add(m_chartPanel, BorderLayout.CENTER);
		} else {
			remove(m_chartPanel);
			m_chartPanel = chartPanel;
			add(m_chartPanel, BorderLayout.CENTER);
		}
		
		m_infoArea.setText(recorder.getReport());
		
		revalidate();
	}
}
