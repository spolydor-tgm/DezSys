package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Mail {

	private String ip;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private QueueSession session;

	private QueueConnection connection;

	private ActiveMQConnectionFactory connectionFactory;

	private Queue queue;

	private QueueSender sender;

	/**
	 * Erzeugen einer Mail
	 * @param ip IP-Adresse des aktuellen Benutzers
	 * @param broker_ip IP-Adresse des Brokers
	 */
	public Mail(String ip,String broker_ip) {
		this.ip =ip;
		try {
			connectionFactory = new ActiveMQConnectionFactory(ip, this.password, "tcp://"+broker_ip+":61616"); // Neue ActiveMQConnectionFactory mit dem user, password und url des Message Brokers erstellen
			connection = connectionFactory.createQueueConnection(); // Verbindung mit einer Queue erstellen
			connection.start(); // Verbindung starten
			session=connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // session erzeugen


		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mailbox abrufen
	 * @return alle Mails, die an den Benutzer gesendet wurden (alle seit dem letzen Abruf)
	 */
	public String checkMailbox() {

		String toReturn = "";
		try {
			queue=session.createQueue(ip);
			QueueReceiver reciver = session.createReceiver(queue);
			try {
				while (true) { // Solange eine Nachricht zurueckkommt
					TextMessage recived = (TextMessage) reciver.receiveNoWait();

					toReturn += recived.getText()+"\n";
				}
			} catch (NullPointerException npe) { // Nullpointer exception, wenn keine Nachricht mehr in der Queue ist
				return toReturn; // Rueckgabe aller Mails
			}
		} catch (JMSException jmse) {

		}
		return toReturn;
	}

	/**
	 * Email sendne
	 * @param ip_destination Ziel-Adresse (IP-Adresse des Benutzers an den die E-Mail versendet wird)
	 * @param message Inhalt der Nachricht, welche versendet wird
	 */
	public void sendMail(String ip_destination, String message) {
		try {
			Queue queueToSend = session.createQueue(ip_destination); // Queue erzeugen. Queuename = IP des Ziels
			sender=session.createSender(queueToSend); // Sender erzeugen. Sender sendet an Ziel Queue
			TextMessage nachricht;
			nachricht=session.createTextMessage(message); // Nachricht erzeugen
			sender.send(queueToSend,nachricht); // Nachricht an ZielQueue senden
		} catch (JMSException e) {

		}
	}

	/**
	 * Mail stoppen
	 * Alle Verbindugen schliessen
	 */
	public void mailStop() {
		try {
			connection.stop();
			session.close();
			connection.close();
		} catch (JMSException e) {}
	}
}
