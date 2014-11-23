package Communication;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Mail {

	private String ip;

	private final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private QueueSession session;

	private QueueConnection connection;

	private ActiveMQConnectionFactory connectionFactory;

	private Queue queue;

	private QueueSender sender;

	public Mail(String ip,String broker_ip) {
		this.ip =ip;
		try {
			connectionFactory = new ActiveMQConnectionFactory(ip, this.password, "tcp://"+broker_ip+":61616"); //
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
			QueueReceiver reciver = session.createReceiver(queue);
			try {
				while (true) {
					TextMessage recived = (TextMessage) reciver.receiveNoWait();

					toReturn += recived.getText()+"\n";
				}
			} catch (NullPointerException npe) {
				return toReturn;
			}
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
			connection.stop();
			session.close();
			connection.close();
		} catch (JMSException e) {}
	}
}
