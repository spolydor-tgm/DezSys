package Communication;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver implements Runnable {

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

	private TextMessage message;

	@Override
	public void run() {
		if(runState) {
			try {
				message = (TextMessage) consumer.receive();
				System.out.println(""+message.getText());
			} catch (JMSException e) {

			}
		}
	}

	public Receiver(String user, String password, String url, String chatroom) {
		this.user=user;
		this.password=password;
		this.url=url;
		this.chatroom = chatroom;

		connectionFactory = new ActiveMQConnectionFactory(this.user, this.password, this.url);
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( this.chatroom );
			consumer = session.createConsumer( destination );

		} catch (JMSException e) {

		}
	}

	public void start() {
		try {
			connection.start();
			runState = true;
		} catch (JMSException e) {

		}
	}

	public void stop() {
		try {
			connection.stop();
			runState=false;
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {

		}
	}

}
