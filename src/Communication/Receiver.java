package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver implements Runnable {

	private Session session=null;

	private Connection connection;

	private MessageConsumer consumer=null;

	private Destination destination=null;

	private ConnectionFactory connectionFactory;

	private String user;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private String url;

	private String chatroom;

	private volatile boolean runState;

	private TextMessage message;

	private boolean firstTimeRun=true;

	@Override
	public void run() {
		while(runState) {
			try {
				message = (TextMessage) consumer.receive();
				if(firstTimeRun == false) {
					System.out.println("" + message.getText());
				} else
					firstTimeRun=false;
			} catch (JMSException e) {

			} catch (NullPointerException e){

			}
		}
	}

	/**
	 * Erzeugt ein Sender Objekt
	 * @param user Username
	 * @param url Adresse des ActiveMQ (Message Broker)
	 * @param chatroom name des Chatrooms (z.B.: Test2)
	 */
	public Receiver(String user, String url, String chatroom) {
		this.user=user;
		this.url=url;
		this.chatroom = chatroom;

		connectionFactory = new ActiveMQConnectionFactory(this.user, this.password, this.url); // Neue ActiveMQConnectionFactory mit dem user, password und url des Message Brokers erstellen
		try {
			connection = connectionFactory.createConnection(); // Verbindung erzeugen
			connection.start(); // Verbindung starten
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // session erzeugen
			destination = session.createTopic( this.chatroom ); // Ziel festlegen
			consumer = session.createConsumer( destination ); // Empfaenger erzeugen

		} catch (JMSException e) {

		}
	}

	/**
	 * Setzt den Laufstatus auf true und startet
	 */
	public void start() {
			runState = true;
	}

	/**
	 * Stoppt den Thread und alle offenen Verbindungen des Receiver Objektes
	 */
	public void stop() {
		try {
			runState=false;
			connection.stop();
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {

		}
	}

}
