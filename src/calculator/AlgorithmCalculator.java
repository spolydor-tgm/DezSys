package calculator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class AlgorithmCalculator implements Calculator, Serializable {

    private static final long serialVersionUID = 227L;

    private static final BigDecimal VIER = BigDecimal.valueOf(4); // Konstante die bei der Berechnung von Pi verwendet wird

    private static final int RUNDUNGSMETHODE = BigDecimal.ROUND_HALF_EVEN; // Rundungsmethode, welche waehrend der Berechnung von Pi verwendet wird

    /**
     * Berechnet den Wert von Pi bis zu der Angegeben Anzahl der Stellen (Nachkommastellen).
     * Der Wert wird mit folgender Formel berechnet: pi/4 = 4*arctan(1/5) - arctan(1/239) und mit einer Erweiterung mit arctan(x)
     * @param stellen Nachkommastellen
     * @return BigDecimal Wert von Pi
     */
    private static BigDecimal computePi(int stellen) {
        int scale = stellen + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(VIER).subtract(arctan1_239).multiply(VIER);
        return pi.setScale(stellen, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Der Wert wird mit folgender Formel berechnet: arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + (x^9)/9 ...
     * @param kehrwertX Kehrwert von X
     * @param scale scale
     * @return BigDecimal des arctan
     */
    private static BigDecimal arctan(int kehrwertX, int scale) {
        BigDecimal ergebnis, nummer, term;
        BigDecimal invertX = BigDecimal.valueOf(kehrwertX);
        BigDecimal invertX2 = BigDecimal.valueOf(kehrwertX * kehrwertX);
 
        nummer = BigDecimal.ONE.divide(invertX, scale, RUNDUNGSMETHODE);
 
        ergebnis = nummer;
        int i = 1;

        do {
            nummer = nummer.divide(invertX2, scale, RUNDUNGSMETHODE);
            int denom = 2 * i + 1;
            term = nummer.divide(BigDecimal.valueOf(denom), scale, RUNDUNGSMETHODE);

            if ((i % 2) != 0)
                ergebnis = ergebnis.subtract(term);
            else
                ergebnis = ergebnis.add(term);
            i++;
        } while (term.compareTo(BigDecimal.ZERO) != 0);
        return ergebnis;
    }

    /**
     * Berechnet Pi mit den angegebenen Nachkommastellen
     * @param anzahlNachkommastellen Anzahl der Nachkommastellen
     * @return BigDecimal Pi mit den Nachkommastellen
     * @throws RemoteException wenn die Anzahl der Nachkommastellen kleiner als 0 ist
     */
	public BigDecimal pi(int anzahlNachkommastellen) throws RemoteException {
        if(anzahlNachkommastellen < 0)
            throw new RemoteException("Verursacht durch", new NumberFormatException("Der Parameter muss groesser als 0 sein und ist: " + anzahlNachkommastellen));

        System.out.println("AlgorithmCalculatorPi aufgerufen, Parameter: [anzahlNachkommastellen = " + anzahlNachkommastellen + "]");
		return computePi(anzahlNachkommastellen);
	}

}
