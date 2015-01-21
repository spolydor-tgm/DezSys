package main.java;

import arguments.CmdArgumentParser;
import client.Client;
import server.Proxy;
import server.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class Main {

    /**
     * Ueberprueft die Usereingabe und startet den ausgewaehlten Dienst (Server/Client/Proxy)
     * @param args Argumente, die von dem Benutzer eingegeben wurden
     */
	public static void main(String... args){
        // SecurityManager, Setzt eine Policy Datei, falls keine gegeben ist
        if (System.getSecurityManager() == null) {
        	try{
        	    System.setProperty("java.security.policy", System.class.getResource("src/main/program.policy").toString());
        	}catch(Exception e){
                e.printStackTrace();
        		System.err.println("Policy Datei: program.policy wurde nicht gefunden oder konnte nicht als Eigenschaft gesetzt werden");
                System.exit(0);
        	}
            System.setSecurityManager(new SecurityManager());
        }

        CmdArgumentParser parser = new CmdArgumentParser(args);

        switch (parser.getProgramType()) {
            case CLIENT:
                Client client = new Client(parser.clientURI);
			try {
				System.out.println(client.pi(parser.piNachkommastellen));
			} catch (RemoteException re) {}
                break;
            case SERVER:
                Server server = new Server(parser.port);
                try {
                    try {
						server.serve();
					} catch (AlreadyBoundException abe) {}
                } catch (RemoteException re) {}
                break;
            case PROXY:
                Proxy proxy = new Proxy(parser.port, parser.proxyURIs);
                try {
                    proxy.serve();
                } catch (RemoteException | AlreadyBoundException e) {}
                break;
            default:
                System.out.println("Kein ProgramType gesetzt");
                break;
        }
    }
}
