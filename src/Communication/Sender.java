package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

	private Session session;

	private Connection connection;

	private MessageProducer producer;

	private Destination destination;

	private ConnectionFactory connectionFactory;

	private TextMessage message;

	private String user;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private String url;

	private String chatroom;

	private String text;

	private String ip;

	/**
	 * Erstellt ein Sender Objekt
	 * @param user Username
	 * @param url Adresse des ActiveMQ (Message Broker)
	 * @param chatroom name des Chatrooms (z.B.: Test2)
	 * @param ip IP-Adresse des aktuellen Benutzers
	 */
	public Sender(String user, String url, String chatroom, String ip) {
		try {
			this.user = user; // user speichern
			this.url = url; // Adresse des ActiveMQ (Message Broker)
			this.chatroom = chatroom; // name des Chatrooms (z.B.: Test2)
			this.ip	= ip; // IP-Adresse des aktuellen Benutzers

			this.connectionFactory = new ActiveMQConnectionFactory(this.user, this.password, this.url); // Neue ActiveMQConnectionFactory mit dem user, password und url des Message Brokers erstellen
			this.connection = this.connectionFactory.createConnection(); // Verbindung erzeugen
			this.connection.start(); // Verbindung starten

			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // Session erzeugen
			this.destination = this.session.createTopic( this.chatroom ); // Ziel festlegen (Topic; ist der Chatroom)

			this.producer = this.session.createProducer(destination); // Sender erzeugen
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // Liefermodus einstellen

		} catch (JMSException e) {
			System.out.println("Keine ActiveMQ bei der angegebenen Adresse gefunden!");
			System.exit(0);
		}
	}

	/**
	 * Alles vom Sender stoppen
	 */
	public void stop() {
		try {
			this.connection.stop();
		} catch (JMSException e) {
		} finally {

			try { producer.close(); } catch ( Exception e ) {}
			try { session.close(); } catch ( Exception e ) {}
			try { connection.close(); } catch ( Exception e ) {}

		}
	}

	/**
	 * Nachricht senden
	 * @throws JMSException
	 */
	public void sendMessage() throws JMSException {
		this.message = this.session.createTextMessage(this.user + " [" + this.ip + "]: " + text); // erzeugt eine Textnachricht
		this.producer.send(this.message); // sendet sie
		this.text = null;
	}

	/**
	 * Setzt den Text und sendet eine Nachricht
	 * @param text Text der gesendet werden soll
	 */
	public void setText(String text) {
		this.text = text;
		try {
			this.sendMessage();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
