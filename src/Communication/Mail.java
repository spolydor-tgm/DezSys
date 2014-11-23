package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Mail {

	private String ip = "";

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private Session session;

	private ActiveMQQueue destination;

	private Connection connection;

	private ConnectionFactory connectionFactory;

	public Mail() {
		try {
			String[] ip = Inet4Address.getLocalHost().toString().split("/");
			this.ip = ip[1];
		} catch (UnknownHostException e) {

		}

		connectionFactory = new ActiveMQConnectionFactory(this.ip, this.password, "tcp://192.168.0.18:61616");

		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
		} catch (JMSException e) {

		}

	}

	public String checkMailbox() {
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
	}

	public void sendMail(String ip, String message) {
		try {
			destination = new ActiveMQQueue(ip);
			MessageProducer producer = session.createProducer(destination);
			TextMessage nachricht = session.createTextMessage(message);
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
