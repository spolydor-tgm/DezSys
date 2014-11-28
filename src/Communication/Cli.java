package Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Cli implements Runnable{

	private Sender chatroomSender;

	private Receiver chatroomReceiver;

	private Mail mail;

	private boolean connectedToChatroom=false;

	private BufferedReader stdIn;

	private final String ip;

	/**
	 * Neues Cli-Objekt erstellen
	 * Standard InputStream oeffnen
	 * IP-Adresse des Benutzers speichern
	 */
	public Cli() {
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		String[] ip = new String[0];
		try {
			ip = Inet4Address.getLocalHost().toString().split("/");
		} catch (UnknownHostException e) {

		}
		this.ip = ip[1];

	}

	/**
	 * Verbindung mit einem Chatroom erstellen
	 * @param ip_broker Adresse des Brokers (ActiveMQ)
	 * @param username Username
	 * @param chatroom Name des Chatroom
	 * @param ip_user IP-Adresse des Benutzers
	 */
	public void connectToChatroom(String ip_broker, String username, String chatroom,String ip_user) {

		chatroomReceiver = new Receiver(username,ip_broker,chatroom);
		chatroomSender = new Sender(username,ip_broker,chatroom,ip_user);

		Thread chatroomRec = new Thread(chatroomReceiver);

		chatroomReceiver.start();
		chatroomRec.start();

		// chatroomSender.start();


		connectedToChatroom=true;
	}

	/**
	 * Chatroom verlassen und alle offenen Verbindungen des Chatroom stoppen
	 */
	public void exitChatroom() {

		this.chatroomSender.stop();
		this.chatroomReceiver.stop();
		// this.mail.mailStop();
		connectedToChatroom = false;
	}

	/**
	 * Programm beenden
	 */
	public void exitProgram() {
		System.exit(0);
	}


	@Override
	public void run() {

		try {
			String input="";
			// Ausgabe der moeglichen Befehle + parameter
			System.out.println("vsdbchat ip_broker username chatroom");
			System.out.println("MAIL ip_des_benutzers ip_message_broker nachricht");
			System.out.println("MAILBOX ip_broker");
			// System.out.println(ip);

			while(input != null) {
				input = stdIn.readLine(); // Eingabe einlesen
				// System.out.println(input);
				input=input.trim();
				String[] inputInformation;
				inputInformation = input.split(" "); // Eingabe nach leerzeichen auftrennen
				if(inputInformation[0].equals("vsdbchat") && connectedToChatroom == false){ // vsdbchat starten, wenn noch keiner aktiv ist
					if(inputInformation.length == 4) {
						String ipbroker = "tcp://"+inputInformation[1]+":61616";
						this.connectToChatroom(ipbroker, inputInformation[2], inputInformation[3], ip);
						connectedToChatroom = true;
					}else {
						System.out.println('\n' + "Falsche Eingabe, bitte wie folgt eingeben");
						System.out.println("vsdbchat ip_broker username chatroom" + '\n');
					}
				}

				if (inputInformation[0].equals("MAIL")) { // mail versenden
					String nachricht = "";
					if (inputInformation.length >= 4) {
						if (mail == null) {
							mail = new Mail(this.ip,inputInformation[2]);
						}
						for (int x = 2; x < inputInformation.length; x++) // Nachricht zusammenbauen
							nachricht = nachricht + inputInformation[x] + " ";

						mail.sendMail(inputInformation[1], nachricht);
					} else {
						System.out.println('\n' + "Falsche Eingabe, bitte wie folgt eingeben");
						System.out.printf("MAIL ip_des_benutzers ip_broker nachricht" + '\n');
					}
				}

				if (inputInformation[0].equals("MAILBOX")) { // Mailbox abrufen

						if (inputInformation.length == 2){

							if (mail == null) {
								mail = new Mail(this.ip, inputInformation[1]);
							}
							System.out.println(mail.checkMailbox());
						}
				}

				if (input.equals("EXIT") && connectedToChatroom == false) { // Programm beenden
					this.exitProgram();
				}
				if (input.equals("EXIT") && connectedToChatroom == true) { // Chatroom verlassen
					this.exitChatroom();
				}

				if(connectedToChatroom) { // Wenn im chatroom dann Nachricht senden
					chatroomSender.setText(input);
				}
			}
		} catch (IOException e) {

		}
	}

}
