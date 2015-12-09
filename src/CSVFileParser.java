import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author zhoulingyan
 * @since 12,2,2015
 */
public class CSVFileParser {

	public static ArrayList<Job> parse(File file) {
		ArrayList<Job> jobAList = new ArrayList<Job>();
		BufferedReader fin = null;
		try {
			fin = new BufferedReader(new FileReader(file));
			String line = null;
			int jid = 1;
			while ((line = fin.readLine()) != null) {
				try {
					String[] fields = line.split(",");

					String name = fields[0].trim();
					int life = Integer.parseInt(fields[1].trim());
					int arrival = Integer.parseInt(fields[2].trim());
					Job job = new Job(name, jid, life, arrival);
					jid +=1;
					jobAList.add(job);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fin) {
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jobAList;
	}
}
