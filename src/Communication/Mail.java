package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Mail {

	private String ip;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private Session session;

	private ActiveMQQueue destination;

	private Connection connection;

	private ConnectionFactory connectionFactory;

	private QueueSend qs;

	public Mail(String ip) {
		this.ip	= ip;

		/*
		connectionFactory = new ActiveMQConnectionFactory(this.ip, this.password, "tcp://192.168.0.18:61616");

		try {
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
		} catch (JMSException e) {

		}
		*/

		try {
			InitialContext ic = QueueSend.getInitialContext("tcp://192.168.0.18:61616");
			qs = new QueueSend();
			qs.init(ic, "" + this.ip);
		} catch (NamingException e) {

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
			qs.setJmsFactory(ip);
			qs.send(message);
		} catch (JMSException e) {

		}

		/*
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
		*/
	}

	public void mailStop() {
		try {
			qs.close();
			connection.close();
		} catch (JMSException e) {}
	}
}
