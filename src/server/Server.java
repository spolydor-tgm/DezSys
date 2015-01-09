package server;

import calculator.AbstractWorker;
import calculator.AlgorithmCalculator;
import calculator.Calculator;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class Server extends AbstractWorker {

    private int port;

    private Calculator stub;

    /**
     * Erzeugt einen neuen Server
     * @param port an dem der Server empfaengt
     */
	public Server(int port) {
        AlgorithmCalculator calculator = new AlgorithmCalculator();
        setCalculator(calculator);

        this.port = port;
	}

    /**
     * Startet den RMI Server
     * @throws RemoteException RemoteException
     * @throws AlreadyBoundException AlreadyBoundException
     */
    public void serve() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);

        String name = "Calculator";
        Calculator stub;
        stub = (Calculator) UnicastRemoteObject.exportObject(this.getCalculator(), port);
        registry.rebind(name, stub);
        this.stub = stub;
    }
}
