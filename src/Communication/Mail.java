package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class Mail {

	private String ip;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private Session session;

	private ActiveMQQueue destination;

	private Destination orderQueue;

	private Connection connection;

	private ConnectionFactory connectionFactory;

	private MessageConsumer consumer;

	public Mail(String ip) {
		this.ip	= ip;

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.ip, this.password, "tcp://192.168.0.18:61616");
		session = null;

		try {
			connection = connectionFactory.createConnection();

			session = connection.createSession(true, Session.SESSION_TRANSACTED);
			orderQueue = session.createQueue(this.ip);

			consumer = session.createConsumer(orderQueue);

			connection.start();
		} catch (JMSException e) {

		}
	}

	public String checkMailbox() {
		/*
		String ret = "";
		try {
			// MessageConsumer consumer = session.createConsumer(destination);

			QueueBrowser browser = session.createBrowser((Queue) destination);
			Enumeration enumeration = browser.getEnumeration();

			while (enumeration.hasMoreElements()) {
				ret += ((TextMessage) enumeration.nextElement()).getText() + '\n';
			}

			browser.close();

			return ret;
		} catch (JMSException jmse) {

		}
		return "";
		*/


		return "";
	}

	public void sendMail(String ip_ziel, String message) {
		try {
			destination = this.session.createTopic( ip_ziel );

			MessageProducer producer = session.createProducer(destination);
			MapMessage orderMessage;





			//destination = new ActiveMQQueue(ip);
			//MessageProducer producer = session.createProducer(destination);
			//TextMessage nachricht = session.createTextMessage(message);
		// while (consumer.receive(1000) != null) {
		// }

			producer.send(nachricht);
		//consumer.receive(1000));
		//enumeration.hasMoreElements());
		//(Message) enumeration.nextElement());
		} catch (JMSException e) {

		}
	}

	public void mailStop() {
		try {
			connection.close();
		} catch (JMSException e) {}
	}
}
