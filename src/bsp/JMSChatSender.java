package bsp;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JMSChatSender {

  //private static String user = ActiveMQConnection.DEFAULT_USER;
  private static String user = "patrick";
  private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
  private static String url = "tcp://192.168.0.18:61616";
  private static String subject = "VSDBChat1";
	
  public static void main( String[] args ) {
	  // Create the connection.
	  Session session = null;
	  Connection connection = null;
	  MessageProducer producer = null;
	  Destination destination = null;
			
	  try {
	    	
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url );
			connection = connectionFactory.createConnection();
			connection.start();
		
			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( subject );
				  
			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
			
			// Create the message
		  BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		  String input = "";
		  while (true) {
			  input = stdIn.readLine();
			  while (input != null) {
				  TextMessage message = session.createTextMessage(""+input);
				  producer.send(message);
				  System.out.println(message.getText());
				  input=null;
			  }
		  }
			//connection.stop();
	      
	  } catch (Exception e) {
	  	
	  	System.out.println("[MessageProducer] Caught: " + e);
	  	e.printStackTrace();
	  	
	  } finally {
	  	
			try { producer.close(); } catch ( Exception e ) {}
			try { session.close(); } catch ( Exception e ) {}
			try { connection.close(); } catch ( Exception e ) {}
			
	  }
      
  } // end main
	
}
