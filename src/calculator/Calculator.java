package calculator;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public interface Calculator extends Remote {

	/**
	 * Erzeugt Pi
	 * @param anzahlNachkommastellen die Berechnet werden sollen
	 * @return Pi mit der Anzahl der Berechneten nachKommastellen
	 * @throws RemoteException wenn die Anzahl der Nachkommastellen kleiner als 0 ist
	 */
	public BigDecimal pi(int anzahlNachkommastellen) throws RemoteException;
}
