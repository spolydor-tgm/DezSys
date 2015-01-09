package calculator;

import java.math.BigDecimal;
import java.net.URI;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class NetzwerkCalculator implements Calculator {

    private List<URI> servers; // Liste der gespeicherten Server

    private int position; // Position der Load Balance

    public NetzwerkCalculator(){
        servers = new ArrayList<URI>();
    }

    /**
     * Fuegt den Server uri dem Loadbalancer hinzu
     * @param uri URI die zu dem RMI Server fuehrt
     */
    public void addServer(URI uri) {
        servers.add(uri);
    }

    /**
     * Leitet die Berechnung von Pi an einen der Server des Load Balance pools
     * @param anzahlNachkommastellen die Berechnet werden sollen
     * @return BigDecimal Pi mit den Nachkommastellen
     * @throws RemoteException RemoteException
     */
    public BigDecimal pi(int anzahlNachkommastellen) throws RemoteException {
        BigDecimal pi = null;

        position = (position + 1) % servers.size(); // Erhoeht die Position fuer die Loadbalance

        URI server = servers.get(position); // Naechster Server, welcher zum berechnen dient

        String name = "Calculator";
        String host = server.getHost();
        int port = server.getPort();

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            Calculator calc = (Calculator) registry.lookup(name);
            pi = calc.pi(anzahlNachkommastellen);
        } catch (NotBoundException nbe) {}

        System.out.println("NetzwerkCalculatorPi aufgerufen, Parameter: [anzahlNachkommastellen = " + anzahlNachkommastellen + "]");
        System.out.println("Leitet die Berechnung an den Server: [host = " + host + ", port = " + port + "]");

        return pi;
    }
}
