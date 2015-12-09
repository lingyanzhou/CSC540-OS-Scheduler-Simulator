import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * @author zhoulingyan
 * @since 12,9,2015
 */
public class MultiOutputRecorder implements IOutputRecorder {
	private ArrayList<IOutputRecorder> outputRecorders = new ArrayList<IOutputRecorder>();

	public void add(IOutputRecorder recorder) {
		outputRecorders.add(recorder);
	}
	
	@Override
	public void begin() throws FileNotFoundException {
		for (IOutputRecorder recorder : outputRecorders) {
			recorder.begin();
		}
	}
	
	@Override
	public void tick() {
		for (IOutputRecorder recorder : outputRecorders) {
			recorder.tick();
		}
		
	}

	@Override
	public void end() throws FileNotFoundException{

		for (IOutputRecorder recorder : outputRecorders) {
			recorder.end();
		}
	}
}
