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

	public Receiver(String user, String url, String chatroom) {
		this.user=user;
		this.url=url;
		this.chatroom = chatroom;

		connectionFactory = new ActiveMQConnectionFactory(this.user, this.password, this.url);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( this.chatroom );
			consumer = session.createConsumer( destination );

		} catch (JMSException e) {

		}
	}

	public void start() {
			runState = true;
	}

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
