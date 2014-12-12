package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Stefan Polydor
 * @version 12.12.14
 */

public interface Compute extends Remote {
	<T> T executeTask(Task<T> t) throws RemoteException;
}