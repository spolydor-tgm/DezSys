package client;

import calculator.NetzwerkCalculator;
import calculator.AbstractWorker;

import java.net.URI;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class Client extends AbstractWorker {

    /**
     * Erzeugt eine neue Client mit einem NetzwerkCalculator mit der gegebenen URI
     * @param serverUri URI die einem zum entsprechenden Server weiterleitet, welcher die Funktion zur Berechnung von Pi bereitstellt
     */
    public Client(URI serverUri) {
        NetzwerkCalculator networkedCalculator = new NetzwerkCalculator();
        networkedCalculator.addServer(serverUri);
        this.setCalculator(networkedCalculator);
    }
}
