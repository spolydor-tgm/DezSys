package Communication;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender implements Runnable {

	private Session session;

	private Connection connection;

	private MessageProducer producer;

	private Destination destination;

	private ConnectionFactory connectionFactory;

	private TextMessage message;

	private String user;

	private String password;

	private String url;

	private String chatroom;

	private volatile boolean runState;

	private String text;

	private String ip;

	public Sender(String user, String password, String url, String chatroom, String ip) {
		try {
			this.user = user;
			this.password = password;
			this.url = url;
			this.chatroom = chatroom;
			this.ip	= ip;

			this.connectionFactory = new ActiveMQConnectionFactory(this.user, this.password, this.url);
			this.connection = this.connectionFactory.createConnection();

			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.destination = this.session.createTopic( this.chatroom );

			this.producer = this.session.createProducer(destination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		} catch (JMSException e) {
			System.out.println("Keine ActiveMQ bei der angegebenen Adresse gefunden!");
			System.exit(0);
		}
	}

	public void start() {
		try {

			this.connection.start();
			this.runState = true;

		} catch (JMSException jmse) {}
	}

	public void stop() {
		try {

			this.connection.stop();
			this.runState = false;

		} catch (JMSException e) {
		} finally {

			try { producer.close(); } catch ( Exception e ) {}
			try { session.close(); } catch ( Exception e ) {}
			try { connection.close(); } catch ( Exception e ) {}

		}
	}

	public void sendMessage() throws JMSException {
		this.message = this.session.createTextMessage(this.ip + ": " + text);
		this.producer.send(this.message);
		System.out.println(this.message.getText());
		this.text = null;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void run() {
		if (runState) {
			if (text != null) {
				try {
					this.sendMessage();
				} catch (JMSException e) {}
			}
		}
	}
}
