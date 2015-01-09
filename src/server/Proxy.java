package server;


import calculator.NetzwerkCalculator;

import java.net.URI;
import java.util.List;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class Proxy extends Server {

    /**
    * Instanziert einen neuen Load Balancer
    * @param port an dem empfangen wird
    * @param servers zu dem weitergeleitet wird
    */
	public Proxy(int port, List<URI> servers) {
        super(port);

        NetzwerkCalculator calculator = new NetzwerkCalculator();

        for(URI uri : servers)
            calculator.addServer(uri);

        super.setCalculator(calculator);
	}
}
