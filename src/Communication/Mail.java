package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Mail {

	private String ip;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private QueueSession session;

	private Destination destination;

	private Destination orderQueue;

	private QueueConnection connection;

	private ActiveMQConnectionFactory connectionFactory;

	private MessageConsumer consumer;

	Queue queue;

	QueueSender sender;
	public Mail(String ip) {
		this.ip =ip;
		try {
			connectionFactory = new ActiveMQConnectionFactory("patrick", this.password, "tcp://192.168.0.19:61616");
			connection = connectionFactory.createQueueConnection();
			connection.start();
			session=connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);


		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public String checkMailbox() {

		String toReturn = "";
		try {
			queue=session.createQueue(ip);
			while(true) {
				QueueReceiver reciver = session.createReceiver(queue);
				TextMessage recived = (TextMessage) reciver.receiveNoWait();
				if(recived == null){
					break;
				}
				toReturn += recived.getText();

			}
			return toReturn;
		} catch (JMSException jmse) {

		}
		return toReturn;
	}

	public void sendMail(String ip_destination, String message) {
		try {
			Queue queueToSend = session.createQueue(ip_destination);
			sender=session.createSender(queueToSend);
			TextMessage nachricht;
			nachricht=session.createTextMessage(message);
			sender.send(queueToSend,nachricht);
		} catch (JMSException e) {

		}
	}

	public void mailStop() {
		try {
			connection.close();
		} catch (JMSException e) {}
	}



}
