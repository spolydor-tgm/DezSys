package arguments;


import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Stefan Polydor & Patrick Kocsis
 * @version 06.01.14
 */
public class CmdArgumentParser {

	public enum ProgramType{
		SERVER, CLIENT, PROXY, NONE
	}

	public URI clientURI; // URI, wenn der Benutzer Client gewaehlt hat

	public int piNachkommastellen;

	public ArrayList<URI> proxyURIs; // URIs von den Benutzern, wenn Proxy ausgewaehlt war

	public int port;

	private boolean istServer = false;

	private boolean istProxy = false;

	private boolean istClient = false;

	private final static String synopsis="Verfuegbare Optionen:\n" +
			"-c --client <URI> <piNachkommastellen> \n" +
			"-s --server <port> \n" +
			"-p --proxy <port> <URIs...> \n"+
			"-h --hilfe \n";

    /**
     * Verarbeitet die Argumente.
     * @param args - Die zu parsenden Argumente.
     */
	public CmdArgumentParser(String[] args) {

		DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
		ArgumentBuilder abuilder = new ArgumentBuilder();
		GroupBuilder gbuilder = new GroupBuilder();

		// Angabe der zu verarbeitenden Parametern
		Option clientkOption = obuilder.withLongName("client").withShortName("c").withRequired(false).withDescription("is a Client")
				.withArgument(abuilder.withName("uri , piDigits").withMinimum(2).withMaximum(2).create()).create();
		Option serverOption = obuilder.withLongName("server").withShortName("s").withRequired(false).withDescription("is a Server")
				.withArgument(abuilder.withName("port").withMinimum(1).withMaximum(1).create()).create();
		Option proxyOption = obuilder.withLongName("proxy").withShortName("p").withRequired(false).withDescription("is a Proxy")
				.withArgument(abuilder.withName("port, URI").withMinimum(1).withMaximum(101).create()).create();
		Option helpOption = obuilder.withLongName("help").withShortName("h").withRequired(false).withDescription("help")
				.withArgument(abuilder.withName("help").create()).create();

		// Erstellung der Optionsgruppe, welche spaeter an den Parser weitergegeben wird
		Group options = gbuilder.withName("options")
				.withOption(clientkOption)
				.withOption(proxyOption)
				.withOption(serverOption)
				.withOption(helpOption).create();
		
		Parser p = new Parser();
		p.setGroup(options);
		
		Parser parser = new Parser();
		parser.setGroup(options);

		try { // Verarbeitung der Argumente
			CommandLine cl = parser.parse(args);
			if (cl == null){
				System.err.println("Parsefehler");
				System.out.println(synopsis);
				System.exit(-1); // Parsefehler
			}
			// Auslesen der Argumente
			if(cl.hasOption(clientkOption)) {
				istClient = true;
				String rawURI = (String) cl.getValues(clientkOption).get(0);
				String rawDigits = (String) cl.getValues(clientkOption).get(1);
				try { // Parsen
					this.clientURI = new URI("rmi://" + rawURI);
					this.piNachkommastellen = Integer.parseInt(rawDigits);
				} catch(Exception e) {
					System.err.println("Parsefehler, bitte ueberpruefe deine Eingabe!");
					System.exit(-1);
				}
			}
			if(cl.hasOption(serverOption)) {
				istServer = true;
				String rawPort = (String) cl.getValue(serverOption);
				try{ // Parsen
					this.port = Integer.parseInt(rawPort);
				}catch(Exception e){
					System.err.println("Parsefehler, bitte ueberpruefe deine Eingabe!");
					System.exit(-1);
				}
			}
			if(cl.hasOption(proxyOption)) {
				istProxy = true;
				String rawPort = (String) cl.getValues(proxyOption).get(0);
				List<String> l = cl.getValues(proxyOption);
				
				try{ // Parsen
					this.port = Integer.parseInt(rawPort);
					this.proxyURIs = new ArrayList<URI>();
					for(int i = 1; i < l.size(); i++){
						URI u = new URI("rmi://" + l.get(i));
						proxyURIs.add(u);
			        }
				} catch(Exception e) {
					System.err.println("Parsefehler, bitte ueberpruefe deine Eingabe!");
					System.out.println(synopsis);
					System.exit(-1);
				}
			}
		}catch(Exception e){
			System.err.println("Ueberpruefe deine angegebenen Argumente");
			System.out.println(synopsis);
			System.exit(-1);
		}

		// Der User koennte vielleicht auch nur einen Server, Client oder Proxy auf einmal angeben
		boolean istGesetzt = false;
		if(istClient)
			istGesetzt = true;
		if(istProxy) {
			if(istGesetzt)
				System.out.println(synopsis);
			istGesetzt = true;
		}
		if(istServer){
			if(istGesetzt)
				System.out.println(synopsis);
			istGesetzt = true;
		}

		// Wenn der Benutzer keinen Server/Client/Proxy ausgewaehlt hat, wird die Synopsis ausgegeben
		if(!istGesetzt) {
			System.out.println(synopsis);
			System.exit(-1);
		}
	}

	/**
	 * Holt den Programtyp
	 * @return ProgramType der zurzeit ausgewaehlt ist. Falls keiner ausgewaehlt ist, wird ProgramType.NONE zurueckgegeben
	 */
	public ProgramType getProgramType(){
		if(istServer)
			return ProgramType.SERVER;
		if(istProxy)
			return ProgramType.PROXY;
		if(istClient)
			return ProgramType.CLIENT;
		return ProgramType.NONE;
	}
}