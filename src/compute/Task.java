package compute;

/**
 * @author Stefan Polydor
 * @version 12.12.14
 */
public interface Task<T> {
	T execute();
}