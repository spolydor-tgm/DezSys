package Communication;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Reciver implements Runnable {

	private Session session=null;

	private Connection connection;

	private MessageConsumer consumer=null;

	private Destination destination=null;

	private ConnectionFactory connectionFactory;

	private String user;

	private String password;

	private String url;

	private String chatroom;

	private volatile boolean runState;

	private Message message;

	@Override
	public void run() {
		try {
			if(runState == true) {
				message = (TextMessage) consumer.receive();
			}
		} catch (JMSException e) {

		}
	}

	public void Reciver(String user, String password, String url, String chatroom) {
		this.user=user;
		this.password=password;
		this.url=url;

		connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( chatroom );
			consumer = session.createConsumer( destination );

		} catch (JMSException e) {

		}
	}

	public void start() {
		try {
			connection.start();
		} catch (JMSException e) {

		}
	}

	public void stop() {
		runState=false;
		try {
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
