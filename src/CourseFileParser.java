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
public class CourseFileParser {

	public static ArrayList<Job> parse(File file) {
		ArrayList<Job> jobAList = new ArrayList<Job>();
		BufferedReader fin = null;
		try {
			fin = new BufferedReader(new FileReader(file));
			String name = null;
			String lifeStr = null;
			int jid = 1;
			while ((name = fin.readLine()) != null
					&& (lifeStr = fin.readLine()) != null) {
				try {
					name = name.trim();
					int life = Integer.parseInt(lifeStr.trim());
					Job job = new Job(name, jid, life, 0);
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
