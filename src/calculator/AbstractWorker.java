package calculator;

import java.math.BigDecimal;
import java.rmi.RemoteException;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public abstract class AbstractWorker {

	private Calculator calculator; // Rechner

    /**
     * Berechnet pi bis auf die angegebenen Nachkommastellen
     * @param anzahlNachkommastellen Anzahl der Nachkommastellen
     * @return BigDecimal beinhalet Pi, berechnet bis zur angegebenen Nachkommastelle
     * @throws RemoteException
     */
	public BigDecimal pi(int anzahlNachkommastellen) throws RemoteException {
		return calculator.pi(anzahlNachkommastellen);
	}

    /**
     * Speichert den Calculator
     * @param calculator ist der neue Calculator
     */
    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     * Gibt den Cacluclator zurueck
     * @return Calculator
     */
    public Calculator getCalculator() {
        return calculator;
    }
}
