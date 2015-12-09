import java.io.FileNotFoundException;

/**
 * 
 * @author zhoulingyan
 * @since 12,6,2015
 */
public interface IOutputRecorder {
	/**
	 * @throws FileNotFoundException
	 * 
	 */
	public void begin() throws FileNotFoundException;

	/**
	 * 
	 */
	public void tick();

	/**
	 * @throws FileNotFoundException 
	 * 
	 */
	public void end() throws FileNotFoundException;
}
