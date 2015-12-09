import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2778366636606214842L;

	private AppFrame m_appFrame;

	private String m_inPathName = null;
	private String m_outPathName = null;
	private String m_methodName = "";
	private String m_inFormatName = "";
	private String m_outFormatName = "";

	private String m_quantumName = "";

	private JButton m_StartButton;
	private JTextField m_RRQuantumField;
	private JButton m_LoadButton;
	private JButton m_SaveButton;
	private JRadioButton m_IFCourseButton;
	private JRadioButton m_IFCSVButton;
	private JRadioButton m_OFCSVButton;
	private JRadioButton m_OFLatexButton;
	private JRadioButton m_SchedFIFOButton;
	private JRadioButton m_SchedSJFButton;
	private JRadioButton m_SchedRRButton;
	private JRadioButton m_SchedSRTButton;

	public ControlPanel(AppFrame appFrame) {
		m_appFrame = appFrame;

		setLayout(new GridLayout(30, 1, 2, 2));

		m_StartButton = new JButton("Start");
		m_StartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SchedulerSimulator simulator = new SchedulerSimulator();
				try {
					IScheduler scheduler = null;

					scheduler = SchedulerSimulator.createScheduler(
							m_methodName, m_quantumName);

					MultiOutputRecorder outputRecorder = new MultiOutputRecorder();

					IOutputRecorder outputRecorder1 = null;
					outputRecorder1 = SchedulerSimulator.createOutputRecorder(
							scheduler, m_outFormatName, m_inPathName,
							m_outPathName);

					ScatterChartOutputRecorder outputRecorder2 = null;
					outputRecorder2 = new ScatterChartOutputRecorder(scheduler,
							m_inPathName, m_appFrame, m_appFrame);

					outputRecorder.add(outputRecorder1);
					outputRecorder.add(outputRecorder2);

					JobSubmitter jobSubmitter = SchedulerSimulator
							.createJobSubmitter(m_inFormatName, new File(
									m_inPathName));
					simulator.run(scheduler, outputRecorder, jobSubmitter);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		m_LoadButton = new JButton("Load");
		m_LoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				chooser.setCurrentDirectory(workingDirectory);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					m_inPathName = chooser.getSelectedFile().getPath();
				}
			}
		});

		m_SaveButton = new JButton("Save");
		m_SaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				chooser.setCurrentDirectory(workingDirectory);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					m_outPathName = chooser.getSelectedFile().getPath();

				}
			}
		});

		// -- each radio button needs it's own action listener
		m_IFCourseButton = new JRadioButton("Course", true);
		m_IFCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_inFormatName = "COURSE";
			}
		});
		m_IFCSVButton = new JRadioButton("CSV", true);
		m_IFCSVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_inFormatName = "CSV";
			}
		});
		m_OFCSVButton = new JRadioButton("CSV", true);
		m_OFCSVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				m_outFormatName = "CSV";
			}
		});
		m_OFLatexButton = new JRadioButton("Latex", true);
		m_OFLatexButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				m_outFormatName = "LATEX";
			}
		});

		// -- create a ButtonGroup to make the buttons work as a mutually
		// exclusive (only one will
		// ever be selected) set
		ButtonGroup group = new ButtonGroup();
		group.add(m_IFCourseButton);
		group.add(m_IFCSVButton);

		ButtonGroup group1 = new ButtonGroup();
		group1.add(m_OFCSVButton);
		group1.add(m_OFLatexButton);

		m_SchedFIFOButton = new JRadioButton("FIFO", true);
		m_SchedFIFOButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_methodName = "FIFO";
			}
		});
		m_SchedSJFButton = new JRadioButton("SJF", true);
		m_SchedSJFButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_methodName = "SJF";
			}
		});
		m_SchedRRButton = new JRadioButton("RR", true);
		m_SchedRRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_methodName = "RR";
			}
		});
		m_SchedSRTButton = new JRadioButton("SRT", true);
		m_SchedSRTButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_methodName = "SRT";
			}
		});

		ButtonGroup group2 = new ButtonGroup();
		group2.add(m_SchedFIFOButton);
		group2.add(m_SchedSJFButton);
		group2.add(m_SchedRRButton);
		group2.add(m_SchedSRTButton);

		m_RRQuantumField = new JTextField(3);
		m_RRQuantumField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_quantumName = m_RRQuantumField.getText();
			}
		});

		m_RRQuantumField.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						m_quantumName = m_RRQuantumField.getText();
					}

					public void removeUpdate(DocumentEvent e) {
						m_quantumName = m_RRQuantumField.getText();
					}

					public void insertUpdate(DocumentEvent e) {
						m_quantumName = m_RRQuantumField.getText();
					}
				});

		add(m_StartButton);
		add(new JLabel("RR Quantum:"));
		add(m_RRQuantumField);
		add(m_LoadButton);
		add(m_SaveButton);
		add(new JLabel("Input Format:"));
		add(m_IFCourseButton);
		add(m_IFCSVButton);
		add(new JLabel("Output Format:"));
		add(m_OFCSVButton);
		add(m_OFLatexButton);
		add(new JLabel("Strategy:"));
		add(m_SchedFIFOButton);
		add(m_SchedSJFButton);
		add(m_SchedRRButton);
		add(m_SchedSRTButton);

		m_StartButton.setEnabled(true);
		m_RRQuantumField.setEnabled(true);
		m_LoadButton.setEnabled(true);
		m_SaveButton.setEnabled(true);
		m_IFCourseButton.setEnabled(true);
		m_IFCSVButton.setEnabled(true);
		m_OFCSVButton.setEnabled(true);
		m_OFLatexButton.setEnabled(true);
		m_SchedFIFOButton.setEnabled(true);
		m_SchedSJFButton.setEnabled(true);
		m_SchedRRButton.setEnabled(true);
		m_SchedSRTButton.setEnabled(true);
	}
}